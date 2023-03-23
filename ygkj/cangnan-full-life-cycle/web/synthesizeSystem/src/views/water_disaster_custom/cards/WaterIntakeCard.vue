<!--
 * @Author: 
 * @Date: 2021-07-01 13:43:37
 * @LastEditTime: 2021-07-02 11:44:16
 * @LastEditors: Please set LastEditors
 * @Description: 取水户
 * @FilePath: -
-->
<template>
	<div class="water-card">
		<panel-item title="取水户" has-switch tc @tclick="tclick">
			<template slot="title-switch">
				<i
					title="地图落点"
					:class="[ldbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
					@click="btnClick()"
				/>
				<i
          @click="goToAmap('取水户')"
          title="一张图"
          class="icon-yizhangtu icon-sty"
        />
				<el-dropdown v-show="ldbtn == true" placement="bottom-start" trigger="click">
					<span class="el-dropdown-link">
						<span style="font-size: 1rem">{{ checkYear }}</span>
						<i class="el-icon-caret-bottom"></i>
					</span>
					<el-dropdown-menu slot="dropdown" class="year-dropdown">
						<el-dropdown-item v-for="(item, index) in optionList" :key="index">
							<el-radio v-model="checkYear" :label="item.value" @change="handleYear">{{ item.label }}</el-radio>
						</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>

				<i title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('取水户')" />
				<!-- <i
          title="浙江省水资源管理系统"
          class="icon-diannano icon-sty"
          @click="toPath"
        /> -->
			</template>

			<div class="charts-panel">
				<!-- 取水户统计 -->
				<sub-title-click-card title="取水户统计" type="radio">
					<div class="card-content">
						<div class="chart-tab">
							<span :class="['chart-tab-item', tab1 == 1 ? 'active' : '']" @click="tab1Change(1)">按区域</span>
							<span :class="['chart-tab-item', tab1 == 2 ? 'active' : '']" @click="tab1Change(2)">按超许可</span>
						</div>
						<div class="line-box" v-if="tab1 == 1">
							<ve-histogram
								ref="bar1"
								height="100%"
								width="100%"
								:data="barData1"
								:settings="barOption.setting"
								:extend="barOption.extend"
								:grid="barOption.grid"
								:tooltip-visible="false"
								:legend-visible="false"
								:after-config="barConfig"
							/>
						</div>
						<div class="pie-box" v-if="tab1 == 2">
							<div id="pieChart1" style="width: 50%; height: 100%"></div>
							<div class="legend-panel">
								<div class="legend-item" v-for="(item, index) in pieChartData1" :key="index">
									<div>
										<i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
										<span class="text">{{ item.name }}</span>
									</div>
									<span class="num">{{ item.value }}</span>
									<span>户</span>
								</div>
							</div>
						</div>
					</div>
				</sub-title-click-card>
				<!-- 取水许可统计 -->
				<sub-title-click-card title="取水许可统计" type="radio">
					<div class="card-content">
						<div class="count-panel">
							<div class="count-panel-item">
								<div class="count-item">
									<i class="icon-count icon-qushuihu-count-1"></i>
									<div class="text">
										<p>取水许可证</p>
										<p class="num">{{ count1 }}<span>本</span></p>
									</div>
								</div>
							</div>
							<div class="line"></div>
							<div class="count-panel-item">
								<div class="count-item">
									<i class="icon-count icon-qushuihu-count-2"></i>
									<div class="text">
										<p>共许可取水量</p>
										<p class="num">{{ count2 }}<span>亿m³</span></p>
									</div>
								</div>
							</div>
						</div>
						<div class="chart-tab">
							<span :class="['chart-tab-item', tab2 == 0 ? 'active' : '']" @click="tab2Change(0)">按区域</span>
							<span :class="['chart-tab-item', tab2 == 1 ? 'active' : '']" @click="tab2Change(1)">按取水用途</span>
						</div>
						<div class="line-box" v-if="tab2 == 0">
							<ve-histogram
								ref="bar2"
								height="100%"
								width="100%"
								:data="barData2"
								:settings="barOption.setting"
								:extend="barOption.extend"
								:grid="barOption.grid"
								:tooltip-visible="false"
								:legend-visible="false"
								:after-config="barConfig"
							/>
						</div>
						<div class="pie-box" v-if="tab2 == 1">
							<div id="pieChart2" style="width: 40%; height: 100%"></div>
							<div class="legend-panel">
								<div class="legend-item" v-for="(item, index) in pieChartData2" :key="index">
									<div>
										<i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
										<span class="text">{{ item.name }}</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</sub-title-click-card>
				<!-- 周“三率”统计 -->
				<sub-title-click-card title="周“三率”统计" type="radio">
					<div class="card-content">
						<div class="top-count">
							<div class="top-count-item">
								<p class="label">监测点(个)</p>
								<p class="num">454</p>
							</div>
							<div class="top-count-item">
								<p class="label">上线率(%)</p>
								<p class="num">99</p>
							</div>
							<div class="top-count-item">
								<p class="label">完整率(%)</p>
								<p class="num">88</p>
							</div>
							<div class="top-count-item">
								<p class="label">及时率(%)</p>
								<p class="num">99</p>
							</div>
						</div>
						<div id="lineChart" style="width: 100%; height: 200px"></div>
					</div>
				</sub-title-click-card>
			</div>
		</panel-item>
		<WaterIntakeDetail v-if="detailVisible" :visible="detailVisible" @close="closeDetail"></WaterIntakeDetail>
	</div>
</template>

<script>
import PanelItem from "../components/PanelItem.vue";
import SubTitleClickCard from "../components/SubTitleClickCard.vue";

import * as echarts from "echarts"; // echart插件

import { mapGetters, mapActions } from "vuex";
import { generateDataZoom } from "./utils/chart.js";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

import { queryMapserver } from "@/api/loadMapServe";
import mapConfig from "@/config/mapConfig";
import WaterIntakeDetail from "./dialog/WaterIntakeDetail.vue";
import { goAmap } from "./utils/common";
export default {
	name: "WaterIntakeCard",
	layerName: ["qshLayer1", "qshLayer2", "qshLayer3", "waterDoorAreaLayer"],
	legendName: ["取水户", "取水预警"],
	components: {
		PanelItem,
		SubTitleClickCard,
		WaterIntakeDetail
	},
	data() {
		return {
			timer: null,
			optionList: [
				{ label: "取水户", value: "取水户" },
				{ label: "取水预警", value: "取水预警" }
			],
			ldbtn: false,
			tab1: 1, // 取水户统计类型切换
			tab2: 0, // 取水许可类型切换

			count1: 0,
			count2: 0,

			barData1: {
				columns: ["key", "value"],
				rows: []
			},
			barOption: {
				grid: {
					top: 32,
					bottom: "5%",
					right: "0%",
					left: "0%",
					containLabel: true
				},
				setting: {
					yAxisName: ["户"],
					itemStyle: { color: "#fff" },
					label: {
						show: true,
						fontSize: this.$commonSetting.chartFontSize,
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
						},
						max: value => {
							return value.max > 1 ? null : 1;
						}
					}
				}
			},
			barData2: {
				columns: ["key", "value"],
				rows: []
			},

			totalLength1: 0,
			totalLength2: 0,
			pieChartData1: [],
			pieChartData2: [],

			lineData: {
				name: ["05-01", "05-02", "05-02", "05-02", "05-02", "05-02", "05-02"],
				list: [80, 65, 87, 57, 99, 89, 78]
			},

			pointList: [],
			checkYear: "取水户",
			areaGeometryArr: [],
			waterDoorWarnList: [],
			detailVisible: false
		};
	},
	computed: {
		...mapGetters(["ScreenMap", "currentSelectArea", "previewModule", "formatterCurrentTime", "AffairId"]),
		map() {
			return this.$store.state.screen.mapComponent;
		}
	},
	watch: {},
	created() {
		this.getAreaGeometry();
	},
	mounted() {
		this.wiuStats();
		this.wiuWarnByAdcd();
		this.intakeSuperviseStats();
		this.getCount();
		this.lineChartOpt();
		this.getWaterIntakeList();
		this.timer = setInterval(() => {
			this.wiuStats();
			this.intakeSuperviseStats();
			this.getCount();
		}, 60000);
		window.addEventListener("resize", this.handleWindowResize);
	},
	beforeDestroy() {
		window.removeEventListener("resize", this.handleWindowResize);
	},
	methods: {
		...mapActions(["SetDetailDialogParam"]),
		goToAmap(type){
			goAmap(type);
		},
		getAreaGeometry() {
			const searchUrl = mapConfig.mapServer + "wenzhou/wenzhou_fangxunend/MapServer/3/query";
			const opt = {
				where: "1=1",
				outFields: "*",
				f: "pjson"
			};
			queryMapserver(searchUrl, opt).then(res => {
				if (res.features) {
					this.areaGeometryArr = res.features;
				}
			});
		},

		handleYear() {
			if (this.checkYear == "取水户") {
				this.clearAreaLayer();
				this.drawPoint();
			} else {
				this.clearPointLayer();
				this.drawArea();
			}
			// this.getRedLineData();
		},
		clearPointLayer() {
			this.$emit("legend-change", "取水户", {});
			this.map.drawClusterPoint([], {}, "qshLayer1");
			this.map.drawClusterPoint([], {}, "qshLayer2");
			this.map.drawClusterPoint([], {}, "qshLayer3");
		},
		clearAreaLayer() {
			this.$emit("legend-change", "取水预警", {});
			this.map.drawAreaPolygon([], "waterDoorAreaLayer");
		},
		closeDetail() {
			this.detailVisible = false;
		},

		// 获取取水户统计
		async wiuStats() {
			let opt = {
				type: this.tab1
			};
			await waterResourceApi.wiuStats(opt).then(res => {
				if (this.tab1 == 1) {
					this.barData1.rows = res.data;
					this.$refs["bar1"].echarts.resize();
				} else {
					this.pieChartData1 = [];
					this.totalLength1 = 0;
					res.data.forEach(item => {
						let obj = {
							name: item.key,
							value: item.value
						};
						this.totalLength1 = this.totalLength1 + item.value;
						this.pieChartData1.push(obj);
					});
					this.$nextTick(() => {
						this.pieChartOpt1();
					});
				}
			});
		},
		//根据行政区划获取到的取用水量已达到或控制指标的地区，进行评估与预警
		async wiuWarnByAdcd() {
			await waterResourceApi.wiuWarnByAdcd().then(res => {
				if (res.code == 0) {
					this.waterDoorWarnList = res.data;
					this.waterDoorWarnList.map(item => {
						item.geometry = this.getGeometryByName(item.adnm);
						item.attr = item;
						if (item.intakeState) {
							item.style = this.getColor(item.intakeState);
						}
					});
				}
			});
		},
		getColor(val) {
			const lineColors = ["#19AF47", "#008FE8", "#E89100"];
			const fillColors = ["rgba(25,175,71,0.5)", "rgba(0,143,232,0.5)", "rgba(232,145,0,0.5)"];
			let colorObj = {
				fillColor: "",
				lineColor: ""
			};
			colorObj.fillColor = fillColors[val - 1];
			colorObj.lineColor = lineColors[val - 1];
			return colorObj;
		},
		getGeometryByName(name) {
			let data = this.areaGeometryArr.filter(item => {
				return item.attributes.NAME == name;
			});
			return data[0].geometry.rings;
		},
		// 获取取水监管许可统计
		async intakeSuperviseStats() {
			let opt = {
				type: this.tab2 == 0 ? 1 : 3
			};
			await waterResourceApi.intakeSuperviseStats(opt).then(res => {
				if (this.tab2 == 0) {
					this.barData2.rows = res.data;
					this.$refs["bar2"].echarts.resize();
				} else {
					this.pieChartData2 = [];
					this.totalLength2 = 0;
					res.data.forEach(item => {
						let obj = {
							name: item.key,
							value: item.value
						};
						this.totalLength2 = this.totalLength1 + item.value;
						this.pieChartData2.push(obj);
					});
					this.$nextTick(() => {
						this.pieChartOpt2();
					});
				}
			});
		},

		// 获取取水监管许可 总数
		async getCount() {
			let opt = {
				type: 4
			};
			await waterResourceApi.intakeSuperviseStats(opt).then(res => {
				this.count1 = res.data.totalWptNum;
				this.count2 = res.data.sumPermitSize;
			});
		},

		// 落点数据
		async getWaterIntakeList() {
			let opt = {
				areaCode: this.administrationValue,
				warning: this.wtaerIntakeWarnType,
				wiuType: this.wtaerIntakeType
			};
			await waterResourceApi.listWiu(opt).then(res => {
				this.pointList = res.data;
			});
		},
		btnClick() {
			this.ldbtn = !this.ldbtn;
			this.clearPointLayer();
			this.clearAreaLayer();
			if (this.ldbtn) {
				if (this.checkYear == "取水户") {
					this.drawPoint();
				} else {
					this.drawArea();
				}
			}
		},
		drawPoint() {
			let style1 = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				srcs: require("@/assets/images/qushuihu-ld-jh-1.png"),
				src: require("@/assets/images/qushuihu-ld-1.png")
			};
			let style2 = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				srcs: require("@/assets/images/qushuihu-ld-jh-2.png"),
				src: require("@/assets/images/qushuihu-ld-2.png")
			};
			let style3 = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				srcs: require("@/assets/images/qushuihu-ld-jh-3.png"),
				src: require("@/assets/images/qushuihu-ld-3.png")
			};
			let pointList1 = [];
			let pointList2 = [];
			let pointList3 = [];
			for (let point of this.pointList) {
				let obj = {
					name: point.wiuName,
					type: "取水户",
					lng: point.wiuLong,
					lat: point.wiuLat,
					props: {
						当年取水量: `${point.intake || "-"}万m³`
					},
					obj: point
				};
				if (point.intakeState == 1) {
					pointList1.push(obj);
				} else if (point.intakeState == 2) {
					pointList2.push(obj);
				} else if (point.intakeState == 3) {
					pointList3.push(obj);
				}
			}
			this.$emit("legend-change", "取水户", {
				label: "取水户",
				clusterLayer: ["qshLayer1", "qshLayer2", "qshLayer3"],
				children: [
					{
						label: "正常",
						icon: "qushuihu-legend-1"
					},
					{
						label: "接近许可",
						icon: "qushuihu-legend-2"
					},
					{
						label: "超许可",
						icon: "qushuihu-legend-3"
					}
				]
			});
			this.map.drawClusterPoint(pointList1, style1, "qshLayer1");
			this.map.drawClusterPoint(pointList2, style2, "qshLayer2");
			this.map.drawClusterPoint(pointList3, style3, "qshLayer3");
		},
		drawArea() {
			this.map.drawAreaPolygon(this.waterDoorWarnList, "waterDoorAreaLayer");
			this.$emit("legend-change", "取水预警", {
				label: "取水户",
				children: [
					{
						label: "正常",
						icon: "qushuihu-area-legend-1"
					},
					{
						label: "接近许可",
						icon: "qushuihu-area-legend-2"
					},
					{
						label: "超许可",
						icon: "qushuihu-area-legend-3"
					}
				]
			});
		},

		// 柱状图
		barConfig(options) {
			let data = options.series[0].data;
			let colorArr = [];
			colorArr = ["#15E2DB", "#9DFDFB", "#A6FFFF"];
			let color = {
				type: "linear",
				x: 0,
				x2: 1,
				y: 0,
				y2: 0,
				colorStops: [
					{
						offset: 0,
						color: colorArr[0]
					},
					{
						offset: 0.5,
						color: colorArr[0]
					},
					{
						offset: 0.5,
						color: colorArr[1]
					},
					{
						offset: 1,
						color: colorArr[1]
					}
				]
			};
			let barWidth = 15;
			let constData = [];
			let showData = [];
			data.filter(function(item) {
				if (item) {
					constData.push(1);
					showData.push(item);
				} else {
					constData.push(0);
					showData.push({
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

			options.series = [
				{
					z: 1,
					name: "数据",
					type: "bar",
					barWidth: barWidth,
					barGap: "0%",
					data: data,
					itemStyle: {
						normal: {
							color: color
						}
					},
					label: {
						show: true,
						fontSize: 14,
						position: "top",
						color: "#15E2DB"
					}
				},
				{
					z: 2,
					name: "数据",
					type: "pictorialBar",
					data: constData,
					symbol: "diamond",
					symbolOffset: ["0%", "50%"],
					symbolSize: [barWidth, 10],
					itemStyle: {
						normal: {
							color: color
						}
					},
					tooltip: {
						show: false
					}
				},
				{
					z: 3,
					name: "数据",
					type: "pictorialBar",
					symbolPosition: "end",
					data: showData,
					symbol: "diamond",
					symbolOffset: ["0%", "-50%"],
					symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
					itemStyle: {
						normal: {
							borderColor: colorArr[2],
							borderWidth: 2,
							color: colorArr[2]
						}
					},
					tooltip: {
						show: false
					}
				}
			];
			options.dataZoom = generateDataZoom(5, this.barData1.rows.length);
			return options;
		},
		// 饼图 取水户统计
		pieChartOpt1() {
			const _this = this;
			var chartDom = document.getElementById("pieChart1");
			var myChart = echarts.init(chartDom);
			var option;
			let colorList = ["#19AF47", "#008FE8", "#E89100"];
			let color = [];
			colorList.forEach(item => {
				let obj = {
					type: "linear",
					x: 0,
					y: 0,
					x2: 1,
					y2: 1,
					colorStops: [
						{
							offset: 0,
							color: item // 0% 处的颜色
						},
						{
							offset: 1,
							color: "rgba(0,0,0)" // 100% 处的颜色
						}
					],
					global: false // 缺省为 false
				};
				color.push(obj);
			});
			option = {
				series: [
					{
						type: "pie",
						radius: ["50%", "80%"],
						centen: [0, 0],
						avoidLabelOverlap: false,
						label: {
							show: true,
							position: "center",
							normal: {
								// normal 是图形在默认状态下的样式
								show: true,
								position: "center",
								color: "#fff",
								fontSize: 14,
								formatter: this.totalLength1 + "户\n总户数"
							}
						},
						labelLine: {
							show: false
						},
						data: this.pieChartData1,
						color: color
					}
				]
			};
			option && myChart.setOption(option);
		},
		// 饼图 取水许可统计
		pieChartOpt2() {
			const _this = this;
			var chartDom = document.getElementById("pieChart2");
			var myChart = echarts.init(chartDom);
			var option;
			let colorList = ["#19af47", "#008fe8", "#e89100", "#6267ff", "#a000c8", "#ff551f", "#eec80b"];
			let color = [];
			colorList.forEach(item => {
				let obj = {
					type: "linear",
					x: 0,
					y: 0,
					x2: 1,
					y2: 1,
					colorStops: [
						{
							offset: 0,
							color: item // 0% 处的颜色
						},
						{
							offset: 1,
							color: "rgba(0,0,0)" // 100% 处的颜色
						}
					],
					global: false // 缺省为 false
				};
				color.push(obj);
			});
			option = {
				series: [
					{
						type: "pie",
						radius: ["50%", "80%"],
						centen: [0, 0],
						avoidLabelOverlap: false,
						label: {
							show: true,
							position: "center",
							normal: {
								// normal 是图形在默认状态下的样式
								show: true,
								position: "center",
								color: "#fff",
								fontSize: 14,
								formatter: this.totalLength2 + "亿m³\n总数"
							}
						},
						labelLine: {
							show: false
						},
						data: this.pieChartData2,
						color: color
					}
				]
			};
			option && myChart.setOption(option);
		},
		// 线图
		lineChartOpt() {
			const _this = this;
			var chartDom = document.getElementById("lineChart");
			var myChart = echarts.init(chartDom);
			var option;
			option = {
				grid: {
					top: 32,
					bottom: "5%",
					right: "0%",
					left: "0%",
					containLabel: true
				},
				dataZoom: generateDataZoom(3, this.lineData.name.length),
				xAxis: {
					type: "category",
					data: this.lineData.name,
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
					axisTick: {
						show: false
					},
					splitLine: {
						lineStyle: {
							color: "#fff",
							opacity: 0.5
						}
					}
				},
				yAxis: {
					type: "value",
					name: "%",
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
					axisTick: {
						show: false
					},
					splitLine: {
						lineStyle: {
							color: "#fff",
							opacity: 0.5
						}
					},
					max: value => {
						return value.max > 1 ? null : 1;
					}
				},
				tooltip: {
					show: true,
					trigger: "axis",
					formatter: "{b0}: {c0}%"
				},
				legend: {
					show: true,
					textStyle: {
						color: "#fff"
					},
					right: "right"
				},
				series: [
					{
						name: "每日取水监测点上线率",
						data: this.lineData.list,
						type: "line",
						smooth: true,
						symbol: "none",
						color: "#1CFFF5"
					}
				]
			};
			option && myChart.setOption(option);
		},
		// 取水户统计类型切换
		tab1Change(val) {
			this.tab1 = val;
			this.$nextTick(() => {
				this.wiuStats();
			});
		},
		// 取水许可统计类型切换
		tab2Change(val) {
			this.tab2 = val;
			this.$nextTick(() => {
				this.intakeSuperviseStats();
			});
		},
		// 跳转
		toPath() {
			window.open("https://www.baidu.com");
		},
		// 打开详情弹框点击事件
		openDetailsDialog(val) {
			// this.SetDetailDialogParam({
			//   mediumSized: "",
			//   codeTpye: val,
			//   analyseDialogShow: true,
			// });
			this.detailVisible = true;
		},
		// 标题点击打开统计卡片
		tclick(title) {
			this.$emit("tclick", title);
		},
		// 当窗口缩放时，echart动态调整自身大小
		handleWindowResize() {
			this.$refs.bar.echarts.resize();
			this.pieChartOpt();
		}
	}
};
</script>

<style lang="scss" scoped>
.charts-panel {
	display: flex;
	flex-direction: column;
}
.el-dropdown {
	vertical-align: top;
}
.el-dropdown-link {
	margin-right: 12px;
}
.chart-tab {
	margin-bottom: 10px;
	&-item {
		margin-right: 10px;
		font-size: 14px;
		padding: 4px 10px;
		cursor: pointer;
		&.active {
			background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
			border-radius: 15px;
		}
	}
}
.line-box {
	width: 100%;
	height: 200px;
	margin-top: 10px;
	margin-bottom: 30px;
}

.pie-box {
	height: 200px;
	display: flex;
	justify-content: space-around;
	align-items: center;
	.legend-panel {
		width: 60%;
		margin-left: 30px;
		.legend-item {
			float: left;
			line-height: 25px;
			display: flex;
			justify-content: space-between;
			font-size: 12px;
			.text {
				display: inline-block;
				width: 60px;
				font-size: 12px;
				margin-right: 10px;
			}
			.num {
				color: #1cfff5;
				font-size: 12px;
				margin-right: 5px;
			}
		}
	}
}

.count-panel {
	display: flex;
	justify-content: space-between;
	margin-bottom: 20px;
	&-item {
		.count-item {
			display: flex;
			align-items: center;
			.text {
				font-size: 14px;
				margin-left: 10px;
				line-height: 25px;
				.num {
					font-size: 20px;
					color: #56fefe;
				}
				span {
					color: #fff;
					font-size: 12px;
				}
			}
		}
	}
	.line {
		border: 1px solid rgba(255, 255, 255, 0.4);
	}
}

.top-count {
	display: flex;
	justify-content: space-between;
	text-align: center;
	margin-bottom: 10px;
	&-item {
		padding: 0 5px;
		font-size: 16px;
		color: #fff;
		.num {
			color: #1ef8f2;
			font-size: 24px;
			margin-top: 8px;
		}
	}
}

.icon-sty {
	cursor: pointer;
	margin-right: 10px;
}
.icon-diannano {
	background-image: url(~assets/images/diannao.png);
	background-size: 100% 100%;
	display: inline-block;
	width: 20px;
	height: 18px;
}
.icon-count {
	display: inline-block;
	width: 48px;
	height: 48px;
	background-size: 100% 100%;
}
.icon-qushuihu-count-1 {
	background-image: url(~assets/images/qushuihu-count-1.png);
}
.icon-qushuihu-count-2 {
	background-image: url(~assets/images/qushuihu-count-2.png);
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
	background: #6267ff;
}
.icon-color-5 {
	@include chart-icon();
	background: #a000c8;
}
.icon-color-6 {
	@include chart-icon();
	background: #ff551f;
}
.icon-color-7 {
	@include chart-icon();
	background: #eec80b;
}
.icon-color-8 {
	@include chart-icon();
	background: #98d89a;
}
.icon-color-9 {
	@include chart-icon();
	background: #004fe3;
}
</style>
