<!--
 * @Author: dyn
 * @Date: 2021-07-01 13:43:37
 * @LastEditTime: 2021-08-12 19:55:57
 * @LastEditors: ysl
 * @Description: 水资源承载能力
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/WaterResCarryCard.vue
-->
<template>
	<div class="water-card">
		<panel-item title="水资源承载能力" has-switch tc @tclick="tclick">
			<template #title-switch>
				<i title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('水资源承载能力')" />
				<i title="设置" class="icon-hongshui-yubao icon-sty" @click="setHandle" />
			</template>

			<div class="charts-panel">
				<!-- 水量/水质评价 -->
				<sub-title-click-card :title="['水量评价', '水质评价']" @tabChange="waterVoluQualiTabChangeHandle">
					<div slot="title-operate">
						<i
							title="地图落点"
							:class="[vqbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
							@click="voluQualDrawMapHandle('vqbtn')"
						/>
					</div>
					<div class="chart-panel pie-box">
						<div id="waterVoluQualiPie" style="width: 50%; height: 100%"></div>
						<div class="legend-panel">
							<div class="legend-item" v-for="(item, index) in waterVoluQualiData" :key="index">
								<div>
									<i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
									<span class="text">{{ item.name }}</span>
								</div>
								<span class="num">{{ item.value }}</span>
							</div>
						</div>
					</div>
				</sub-title-click-card>

				<!-- 经济/人口评价 -->
				<sub-title-click-card :title="['经济评价', '人口评价']" @tabChange="econoPopuTabChangeHandle">
					<div slot="title-operate">
						<i
							title="地图落点"
							:class="[epbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
							@click="econoPopuDrawMapHandle('epbtn')"
						/>
					</div>
					<div class="chart-panel">
						<ve-histogram
							ref="chart"
							height="100%"
							width="100%"
							:data="econoPopuData"
							:settings="econoPopuOption.setting"
							:extend="econoPopuOption.extend"
							:grid="econoPopuOption.grid"
							:data-zoom="econoPopuOption.dataZoom"
							:tooltip-visible="false"
							:legend-visible="false"
							:after-config="econoPopuBarConfig"
						/>
					</div>
				</sub-title-click-card>
				<!-- 用水评价 -->
				<sub-title-click-card :title="['用水评价']">
					<div slot="title-operate">
						<!-- <i title="地图落点" class="icon-yinzhang-1 icon-sty" /> -->
					</div>
					<div class="chart-panel">
						<!-- <echartLine
              ref="lineChart"
              :extraOption="waterUseOption"
            ></echartLine> -->
						<div id="lineChart" style="width: 100%; height: 100%"></div>
					</div>
				</sub-title-click-card>
			</div>
		</panel-item>

		<module-analy-dialog ref="dialog" :visible="dialogVisible" @closeDialog="closeDialog" />
	</div>
</template>

<script>
import PanelItem from "../components/PanelItem.vue";
import SubTitleClickCard from "../components/SubTitleClickCard.vue";

import * as echarts from "echarts"; // echart插件

import { mapGetters, mapActions } from "vuex";
import { generateDataZoom } from "./utils/chart.js";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

import ModuleAnalyDialog from "../dialog/ModuleAnalyDialog";

import EchartLine from "../../WaterDisasterPreventionScreen/charts/echart_line/EchartLine.vue"; // 折线图

export default {
	name: "WaterResCarryCard",
	layerName: ["areaLayer", "econoOverlay"],
	legendName: "水资源承载能力",
	components: {
		PanelItem,
		SubTitleClickCard,
		EchartLine,
		ModuleAnalyDialog
	},
	data() {
		return {
			timer: null,
			voluQualTab: "水量评价", // 水量/水质评价
			waterVoluQualiData: [], // 水量/水质 数据
			totalLength: 0,
			econoPopuTab: "经济评价", // 经济/人口评价
			econoPopuData: {
				columns: ["name", "curr", "supp"],
				rows: []
			},
			econoPopuOption: {
				grid: {
					top: "24%",
					bottom: "5%",
					left: "0%",
					containLabel: true
				},
				setting: {
					yAxisName: ["万元"],
					itemStyle: { color: "#fff" },
					label: {
						show: true,
						fontSize: this.$commonSetting.chartFontSize,
						fontWeight: "bold",
						position: "top",
						color: "#fff"
					}
				},
				extend: {
					"textStyle.color": "#fff",
					barWidth: 15,
					xAxis: {
						axisLabel: {
							interval: 0,
							fontSize: this.$commonSetting.chartFontSize
						}
					},
					legend: {
						right: "0%",
						icon: "circle",
						itemWidth: 10,
						textStyle: { color: "#FFF" }
					},
					yAxis: {
						nameTextStyle: {
							color: "#ffffff99",
							fontSize: this.$commonSetting.chartFontSize
						},
						axisLine: {
							show: false
						},
						axisLabel: {
							color: "#fff",
							fontSize: this.$commonSetting.chartFontSize
						},
						splitLine: {
							lineStyle: {
								color: "#fff",
								opacity: 0.5
							}
						}
					}
				}
			},
			waterUseData: {},
			waterUseOption: {},
			vqbtn: false,
			epbtn: false,
			areaList: [],
			dialogVisible: false
		};
	},
	computed: {
		...mapGetters(["ScreenMap", "currentSelectArea", "previewModule", "formatterCurrentTime", "AffairId"]),
		map() {
			return this.$store.state.screen.mapComponent;
		}
	},
	mounted() {
		this.waterVoluQualiTabChangeHandle("水量评价");
		this.econoPopuTabChangeHandle("经济评价");
		this.waterUseDataList();
		window.addEventListener("resize", this.handleWindowResize);
		this.timer = setInterval(() => {
			this.waterVoluQualiTabChangeHandle("水量评价");
			this.econoPopuTabChangeHandle("经济评价");
			this.waterUseDataList();
		}, 60000);
	},
	beforeDestroy() {
		window.removeEventListener("resize", this.handleWindowResize);
	},
	methods: {
		...mapActions(["SetDetailDialogParam"]),

		// tab切换  水量评价/水质评价
		waterVoluQualiTabChangeHandle(val) {
			this.voluQualTab = val;
			this.stats4WraUnit(val);
		},
		// tab切换  经济评价/人口评价
		econoPopuTabChangeHandle(val) {
			this.econoPopuTab = val;
			this.stats4WraUnitEconPopu(val);
		},

		// 获取数据 水量评价 水质评价
		async stats4WraUnit(type) {
			let opt = { type: "1" };
			switch (type) {
				case "水量评价":
					opt.type = "1";
					break;
				case "水质评价":
					opt.type = "2";
					break;
			}
			await waterResourceApi.stats4WraUnit(opt).then(res => {
				this.waterVoluQualiData = [
					{ name: "不超载", value: res.data.disOverload },
					{ name: "临界状态", value: res.data.criticalState },
					{ name: "超载", value: res.data.overload },
					{ name: "严重超载", value: res.data.superOverload }
				];
				this.totalLength = res.data.disOverload + res.data.criticalState + res.data.overload + res.data.superOverload;
				this.areaList = res.data.tableData;
			});
			this.$nextTick(() => {
				this.waterVoluQualiPie();
				if (this.vqbtn) this.voluQualDrawMapHandle("vqbtn");
			});
		},
		// 获取数据 经济评价 人口评价
		async stats4WraUnitEconPopu(type) {
			let opt = { type: "3" };
			switch (type) {
				case "经济评价":
					opt.type = "3";
					break;
				case "人口评价":
					opt.type = "4";
					break;
			}
			await waterResourceApi.stats4WraUnit(opt).then(res => {
				this.econoPopuData.rows = [];
				res.data.forEach(item => {
					let obj = {
						name: item.adnm,
						curr: (item.currentEco || item.currentPop) && (item.currentEco || item.currentPop).toFixed(2),
						supp: (item.supportEco || item.supportPop) && (item.supportEco || item.supportPop).toFixed(2)
					};
					this.econoPopuData.rows.push(obj);
				});
			});
			this.$nextTick(() => {
				this.$refs["chart"].echarts.resize();

				this.map.removeOverlayByName("econoOverlay");
				// this.setMapOverLay();
				if (this.epbtn) this.econoPopuDrawMapHandle("epbtn");
			});
		},
		// 用水评价 数据
		async waterUseDataList() {
			this.waterUseData = {
				name: [],
				now: [],
				pre: [],
				watConControlNorm: 0
			};
			let opt = { type: "5" };
			await waterResourceApi.stats4WraUnit(opt).then(res => {
				this.waterUseData.watConControlNorm = res.data.watConControlNorm;
				res.data.historicCurve.forEach((item, i) => {
					this.waterUseData.name.push(item.year);
					this.waterUseData.now.push(item.water.toFixed(2));
					this.waterUseData.pre.push(i == res.data.historicCurve.length - 1 ? item.water.toFixed(2) : null);
				});
				res.data.predictCurve.forEach(item => {
					this.waterUseData.name.push(item.year);
					this.waterUseData.pre.push(item.water.toFixed(2));
				});
			});
			this.$nextTick(() => {
				this.waterUseConfig();
			});
		},

		// 水量/水质 落点
		voluQualDrawMapHandle(btnType) {
			this[btnType] = !this[btnType];
			this.$emit("legend-change", "水量评价", {});
			this.$emit("legend-change", "水质评价", {});
			if (this[btnType]) {
				this.$emit("legend-change", this.voluQualTab, {
					label: this.voluQualTab,
					children: [
						{
							label: "不超载",
							icon: "water-buchaozai"
						},
						{
							label: "临界状态",
							icon: "water-linjie"
						},
						{
							label: "超载",
							icon: "water-chaozai"
						},
						{
							label: "严重超载",
							icon: "water-yanzhong"
						}
					]
				});

				this.areaList.forEach(item => {
					if (item.evaDesc == "不超载") {
						this.map.showArea(item.adnm, "rgba(25, 175, 71, 0.4)");
					} else if (item.evaDesc == "临界状态") {
						this.map.showArea(item.adnm, "rgba(0, 143, 232,0.4)");
					} else if (item.evaDesc == "超载") {
						this.map.showArea(item.adnm, "rgba(232, 145, 0,0.4)");
					} else if (item.evaDesc == "严重超载") {
						this.map.showArea(item.adnm, "rgba(232,0,0,0.4)");
					}
				});
			} else {
				this.$emit("legend-change", this.voluQualTab, {});
				this.map.removeLayerByName("areaLayer");
			}
		},
		// 经济评价/ 人口评价点位数据
		async getEconoPopuData() {
			let opt = { type: this.econoPopuTab == "经济评价" ? 3 : 4 };
			let result = [];
			await waterResourceApi.stats4WraUnit(opt).then(res => {
				result = res.data;
			});
			return result;
		},
		// 经济评价/ 人口评价落点按钮
		econoPopuDrawMapHandle(btnType) {
			this[btnType] = !this[btnType];
			this.$emit("legend-change", "经济评价", {});
			this.$emit("legend-change", "人口评价", {});
			if (this[btnType]) {
				this.setMapOverLay();
				this.$emit("legend-change", this.econoPopuTab, {
					label: this.econoPopuTab,
					children: [
						{
							label: "最大值",
							icon: "water-max"
						},
						{
							label: "最小值",
							icon: "water-min"
						},
						{
							label: "正常值",
							icon: "water-normal"
						}
					]
				});
				//
			} else {
				this.map.removeOverlayByName("econoOverlay");
				this.$emit("legend-change", this.econoPopuTab, {});
			}
		},
		//经济评价 / 人口评价落点
		setMapOverLay() {
			if (this.epbtn) {
				this.getEconoPopuData().then(res => {
					this.map.addFixedOverlay(res, "econoOverlay", this.econoPopuTab);
				});
			}
		},
		// 打开模型设置
		setHandle() {
			this.$refs["dialog"].init();
			this.dialogVisible = true;
		},
		// 关闭模型设置
		closeDialog() {
			this.dialogVisible = false;
		},

		// 水量评价/水质评价 饼图
		waterVoluQualiPie() {
			var chartDom = document.getElementById("waterVoluQualiPie");
			var myChart = echarts.init(chartDom);
			var option;
			const color0 = {
				type: "linear",
				x: 0,
				y: 0,
				x2: 1,
				y2: 1,
				colorStops: [
					{
						offset: 0,
						color: "#19AF47" // 0% 处的颜色
					},
					{
						offset: 1,
						color: "rgba(0,0,0)" // 100% 处的颜色
					}
				],
				global: false // 缺省为 false
			};
			const color1 = {
				type: "linear",
				x: 0,
				y: 0,
				x2: 1,
				y2: 1,
				colorStops: [
					{
						offset: 0,
						color: "#008fe8" // 0% 处的颜色
					},
					{
						offset: 1,
						color: "rgba(0,0,0)" // 100% 处的颜色
					}
				],
				global: false // 缺省为 false
			};
			const color2 = {
				type: "linear",
				x: 0,
				y: 0,
				x2: 1,
				y2: 1,
				colorStops: [
					{
						offset: 0,
						color: "#e89100" // 0% 处的颜色
					},
					{
						offset: 1,
						color: "rgba(0,0,0)" // 100% 处的颜色
					}
				],
				global: false // 缺省为 false
			};
			const color3 = {
				type: "linear",
				x: 0,
				y: 0,
				x2: 1,
				y2: 1,
				colorStops: [
					{
						offset: 0,
						color: "#e80000" // 0% 处的颜色
					},
					{
						offset: 1,
						color: "rgba(0,0,0)" // 100% 处的颜色
					}
				],
				global: false // 缺省为 false
			};
			option = {
				tooltip: {
					show: true,
					trigger: "item",
					formatter: function(data) {
						return (
							data.name +
							"<br/><span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'>" +
							data.value +
							"</span> <span  style='font-size: 14px;'> 个</span>"
						);
					},
					confine: true,
					extraCssText: "width:160px;height:50px;font-size:18px;"
				},
				series: [
					{
						type: "pie",
						radius: ["50%", "80%"],
						centen: [0, 0],
						avoidLabelOverlap: false,
						label: {
							show: false,
							position: "center",
							normal: {
								// normal 是图形在默认状态下的样式
								show: true,
								position: "center",
								color: "#fff",
								fontSize: 14,
								formatter: this.totalLength + "个\n" + "地区"
							}
						},
						labelLine: {
							show: false
						},
						data: this.waterVoluQualiData,
						color: [color0, color1, color2, color3]
					}
				]
			};
			option && myChart.setOption(option);
		},
		// 经济/人口评价 柱状图
		econoPopuBarConfig(options) {
			let curr = options.series[0].data;
			let supp = options.series[1].data;
			let colorArr1 = ["#15E2DB", "#9DFDFB", "#A6FFFF"];
			let colorArr2 = ["#06a2d4", "#62d9ff", "#64d9ff"];
			let color1 = {
				type: "linear",
				x: 0,
				x2: 1,
				y: 0,
				y2: 0,
				colorStops: [
					{
						offset: 0,
						color: colorArr1[0]
					},
					{
						offset: 0.5,
						color: colorArr1[0]
					},
					{
						offset: 0.5,
						color: colorArr1[1]
					},
					{
						offset: 1,
						color: colorArr1[1]
					}
				]
			};
			let color2 = {
				type: "linear",
				x: 0,
				x2: 1,
				y: 0,
				y2: 0,
				colorStops: [
					{
						offset: 0,
						color: colorArr2[0]
					},
					{
						offset: 0.5,
						color: colorArr2[0]
					},
					{
						offset: 0.5,
						color: colorArr2[1]
					},
					{
						offset: 1,
						color: colorArr2[1]
					}
				]
			};
			let barWidth = 15;
			let constData1 = [];
			let showData1 = [];
			let constData2 = [];
			let showData2 = [];
			curr.filter(function(item) {
				if (item) {
					constData1.push(1);
					showData1.push(item);
				} else {
					constData1.push(0);
					showData1.push({
						value: 1,
						itemStyle: {
							normal: {
								borderColor: "rgba(0,0,0,0)",
								borderWidth: 2,
								color: "rgba(0,0,0,0)"
							}
						}
					});
				}
			});
			supp.filter(function(item) {
				if (item) {
					constData2.push(1);
					showData2.push(item);
				} else {
					constData2.push(0);
					showData2.push({
						value: 1,
						itemStyle: {
							normal: {
								borderColor: "rgba(0,0,0,0)",
								borderWidth: 2,
								color: "rgba(0,0,0,0)"
							}
						}
					});
				}
			});

			options.tooltip = {
				show: true,
				formatter: data => {
					if (this.econoPopuTab == "经济评价") {
						return (
							data.name +
							"<br/><span style='font-size: 14px;'>当前GDP：" +
							curr[data.dataIndex] +
							"万元</span> <br/><span style='font-size: 14px;'>可支撑GDP：" +
							supp[data.dataIndex] +
							"万元</span>"
						);
					} else {
						return (
							data.name +
							"<br/><span style='font-size: 14px;'>当前人口：" +
							curr[data.dataIndex] +
							"万人</span> <br/><span style='font-size: 14px;'>可支撑人口：" +
							supp[data.dataIndex] +
							"万人</span>"
						);
					}
				}
			};

			options.series = [
				{
					z: 1,
					name: this.econoPopuTab == "经济评价" ? "当前GDP" : "当前人口",
					type: "bar",
					barWidth: barWidth,
					barGap: "30%",
					data: curr,
					itemStyle: {
						normal: {
							color: color1
						}
					},
					label: {
						show: true,
						fontSize: 12,
						position: "top",
						color: "#1EF8F2"
					}
				},
				{
					z: 2,
					name: "",
					type: "pictorialBar",
					data: constData1,
					symbol: "diamond",
					symbolOffset: ["-60%", "55%"],
					symbolSize: [barWidth, 10],
					itemStyle: {
						normal: {
							color: color1
						}
					},
					tooltip: {
						show: false
					}
				},
				{
					z: 3,
					name: "",
					type: "pictorialBar",
					symbolPosition: "end",
					data: showData1,
					symbol: "diamond",
					symbolOffset: ["-92%", "-50%"],
					symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
					itemStyle: {
						normal: {
							borderColor: colorArr1[2],
							borderWidth: 2,
							color: colorArr1[2]
						}
					},
					tooltip: {
						show: false
					}
				},
				{
					z: 1,
					name: this.econoPopuTab == "经济评价" ? "可支撑GDP" : "可支撑人口",
					type: "bar",
					barWidth: barWidth,
					barGap: "30%",
					data: supp,
					itemStyle: {
						normal: {
							color: color2
						}
					},
					label: {
						show: true,
						fontSize: 12,
						position: "top",
						color: "#06a2d4"
					}
				},
				{
					z: 2,
					name: "",
					type: "pictorialBar",
					data: constData2,
					symbol: "diamond",
					symbolOffset: ["65%", "50%"],
					symbolSize: [barWidth, 10],
					itemStyle: {
						normal: {
							color: color2
						}
					},
					tooltip: {
						show: false
					}
				},
				{
					z: 3,
					name: "",
					type: "pictorialBar",
					symbolPosition: "end",
					data: showData2,
					symbol: "diamond",
					symbolOffset: ["90%", "-50%"],
					symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
					itemStyle: {
						normal: {
							borderColor: colorArr2[2],
							borderWidth: 2,
							color: colorArr2[2]
						}
					},
					tooltip: {
						show: false
					}
				}
			];
			options.dataZoom = generateDataZoom(4, this.econoPopuData.rows.length);
			options.legend.selectedMode = false;
			options.yAxis[0].name = this.econoPopuTab == "经济评价" ? "万元" : "万人";
			return options;
		},
		// 用水评价 线图
		waterUseConfig() {
			const _this = this;
			var chartDom = document.getElementById("lineChart");
			var myChart = echarts.init(chartDom);
			let iconPath1 = require("@/assets/images/xushuiliang.png");
			let iconPath2 = require("@/assets/images/xushuiliang-dotted.png");
			let options = {
				dataZoom: generateDataZoom(6, this.waterUseData.name.length),
				grid: {
					top: "20%",
					bottom: "5%",
					left: "0%",
					right: "0%",
					containLabel: true
				},
				xAxis: {
					type: "category",
					data: this.waterUseData.name,
					axisLabel: {
						show: true,
						textStyle: {
							color: "#fff"
						}
					},
					axisTick: {
						show: false
					},
					axisLine: {
						lineStyle: {
							color: "#fff",
							fontSize: "18"
						}
					}
				},
				yAxis: {
					type: "value",
					name: "亿m³",
					nameTextStyle: {
						color: "#ffffff99",
						fontSize: this.$commonSetting.chartFontSize
					},
					axisLine: {
						show: false
					},
					axisLabel: {
						color: "#fff",
						fontSize: this.$commonSetting.chartFontSize
					},
					splitLine: {
						lineStyle: {
							show: false,
							color: "#fff",
							opacity: 0.2
						}
					},
					max: _this.waterUseData.watConControlNorm + 5
				},
				legend: {
					right: "10",
					selectedMode: false,
					textStyle: {
						color: "#fff",
						fontSize: 12
					},
					itemHeight: 3,
					data: [
						{
							name: "需水量",
							icon: "image://" + iconPath1
						},
						{
							name: "预测需水量",
							icon: "image://" + iconPath2
						}
					]
				},
				tooltip: {
					trigger: "axis",
					formatter: function(data) {
						if (data.length > 1) {
							return (
								data[0].name +
								"<br/><span>需水量：</span>" +
								data[0].value +
								"<span>亿m³</span>" +
								"<br/>十四五用水总量控制指标：" +
								_this.waterUseData.watConControlNorm +
								"<span>亿m³</span>"
							);
						} else {
							return (
								data[0].name +
								"<br/><span>预测需水量：</span>" +
								data[0].value +
								"<span>亿m³</span>" +
								"<br/>十四五用水总量控制指标：" +
								_this.waterUseData.watConControlNorm +
								"<span>亿m³</span>"
							);
						}
					},
					confine: true
				},
				series: [
					{
						name: "需水量",
						type: "line",
						smooth: true,
						data: this.waterUseData.now,
						symbol: "none",
						lineStyle: {
							color: "#1EF8F2"
						},
						markLine: {
							symbol: "none",
							label: {
								color: "#E89100",
								fontWeight: "bold",
								position: "middle",
								formatter: "十四五用水总量控制指标"
							},
							lineStyle: {
								normal: {
									type: "solid",
									color: "#E89100",
									width: 2
								}
							},
							data: [{ yAxis: this.waterUseData.watConControlNorm, name: "max" }]
						}
					},
					{
						name: "预测需水量",
						type: "line",
						smooth: true,
						symbol: "none",
						lineStyle: {
							color: "#1EF8F2",
							type: "dotted" //'dotted'虚线 'solid'实线
						},
						data: this.waterUseData.pre
					}
				]
			};

			options && myChart.setOption(options);
		},
		// 打开详情弹框点击事件
		openDetailsDialog(val) {
			this.SetDetailDialogParam({
				mediumSized: "",
				codeTpye: val,
				analyseDialogShow: true
			});
		},
		// 标题点击打开统计卡片
		tclick(title) {
			this.$emit("tclick", title);
		},
		// 当窗口缩放时，echart动态调整自身大小
		handleWindowResize() {
			if (!this.chart) return;
			this.waterUseConfig();
		}
	}
};
</script>

<style lang="scss" scoped>
.charts-panel {
	display: flex;
	flex-direction: column;
}
.icon-sty {
	cursor: pointer;
	margin-right: 10px;
}
.icon-circle {
	width: 12px !important;
	height: 12px !important;
	border-radius: 50%;
}
.icon-color-1 {
	@include chart-icon();
	background: #19af47;
}
.icon-color-2 {
	@include chart-icon();
	background: #008fe8;
}
.icon-color-3 {
	@include chart-icon();
	background: #e89100;
}
.icon-color-4 {
	@include chart-icon();
	background: #e80000;
}
.chart-panel {
	height: 200px;
}
.pie-box {
	display: flex;
	justify-content: space-around;
	align-items: center;
	.legend-panel {
		width: 40%;
		.legend-item {
			line-height: 25px;
			display: flex;
			justify-content: space-between;
			.text {
				font-size: 14px;
				margin-right: 10px;
			}
			.num {
				color: #1cfff5;
				font-size: 1rem;
			}
		}
	}
}
</style>

<style lang="scss"></style>
