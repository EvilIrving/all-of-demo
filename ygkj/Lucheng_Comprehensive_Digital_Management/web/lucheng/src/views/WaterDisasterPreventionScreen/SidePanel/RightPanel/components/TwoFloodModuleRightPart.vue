<template>
	<div>
		<div ref="collapseContent" class="collapse-panel__border" style="width: 490px">
			<div class="collapse-panel__content">
				<!-- 潮位 -->
				<panel-item title="潮位" hasSwitch>
					<template #title-switch>
						<i
							title="地图落点"
							@click="handleTideChange('cwbtn')"
							:class="[cwbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
						/>
						<i title="查看详表" @click="openDetailsDialog('潮位分析', '防御复盘')" class="icon-liebiao icon-sty" />
						<i @click="goToAMap('潮位')" title="一张图" class="icon-xin-yizhangtu icon-sty" />
					</template>
					<!-- 潮位图表切换 -->
					<div style="margin:10px 0;">
						<capsule-tabs
							v-model="tideStaName"
							:tabs="['温州', '龙湾', '瑞安', '鳌江']"
							@change="tideStaNameTabChange"
						/>
					</div>
					<!-- 潮位图表 -->
					<echartLine style="height:215px;width:100%;" ref="lineChart" :extraOption="extraOption" />
				</panel-item>
				<!-- 水毁 -->
				<panel-item title="水毁" hasSwitch>
					<template #title-switch>
						<i title="地图落点" :class="[szbtn % 2 == 0 ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']" />
						<i title="查看详表" class="icon-liebiao icon-sty" />
					</template>
					<!-- 水毁详情 -->
					<div class="rainfall-style">
						<p><i class="icon-shui-hui" style="margin-right:10px;" /> 工程损失 <span> 0 </span> 个</p>
					</div>
					<!-- 水毁图表切换 -->
					<div style="margin-bottom:10px;">
						<capsule-tabs
							v-model="realtimePreChart"
							:tabs="['行政区划', '工程类型']"
							@change="forecastChartTabChange"
						/>
					</div>
					<!-- 水毁图表 -->
					<ve-histogram
						height="195px"
						width="100%"
						:tooltip-visible="false"
						:legend-visible="false"
						:settings="realtimeChartOption.setting"
						:extend="realtimeChartOption.extend"
						:grid="realtimeChartOption.grid"
						:after-config="realtimeConfig"
						:data="realtimeChartData"
					/>
				</panel-item>
				<!-- 水利设施保险 -->
				<panel-item title="水利设施保险" hasSwitch>
					<template #title-switch>
						<i
							title="地图落点"
							@click="handleInsuranceChange('bxbtn')"
							:class="[hwbtn % 2 == 0 ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
						/>
						<i title="查看详表" class="icon-liebiao icon-sty" @click="openDetailsDialog('水利设施保险', '防御复盘')" />
						<!--  -->
						<i title="一张图" class="icon-shuili-baoxian icon-sty" />
					</template>
					<!-- 水利设施保险详情 -->
					<div>
						<el-row>
							<el-col :span="12" class="insurance-item">
								<div
									@click="insuranceChange('理赔数量')"
									:class="['claim-settlement-sty', realtimePreChartTab == '理赔数量' ? 'active' : '']"
								>
									<p><i class="icon-lipei-hetong" /></p>
									<div>
										<p>理赔数量</p>
										<p><span>0</span> 个</p>
									</div>
								</div>
							</el-col>
							<el-col :span="12" class="insurance-item">
								<div
									@click="insuranceChange('理赔金额')"
									:class="['claim-settlement-sty', realtimePreChartTab == '理赔金额' ? 'active' : '']"
								>
									<p><i class="icon-lipei-jine" /></p>
									<div>
										<p>理赔金额</p>
										<p><span>0</span> 万元</p>
									</div>
								</div>
							</el-col>
						</el-row>
					</div>
					<!-- 水利设施保险图表切换 -->
					<!-- <div style="margin-bottom:10px;">
          <capsule-tabs v-model="realtimePreChartTab" :tabs="['理赔数量', '理赔金额']" @change="forecastChartTabChange" />
        </div> -->
					<!-- 水利设施保险图表 -->
					<ve-histogram
						height="195px"
						width="100%"
						:tooltip-visible="false"
						:legend-visible="false"
						:settings="realtimeChartOption.setting"
						:extend="realtimeChartOption.extend"
						:grid="realtimeChartOption.grid"
						:after-config="realtimeConfig"
						:data="realtimeChartData"
					/>
				</panel-item>
			</div>
		</div>
	</div>
</template>

<script>
// 地图缓存
import { mapGetters } from "vuex";
// 标题组件
import PanelItem from "../../../components/PanelItem";
// tab切换
import CapsuleTabs from "../../../components/CapsuleTabs";
// api接口调用
import { disasterPreventionApi } from "@/api/RightSildePanel";
// 折线图
import EchartLine from "../../../charts/echart_line/EchartLine.vue";
import { goAMap } from "@/views/water_disaster_custom/cards/utils/common";

export default {
	name: "TwoFloodModuleRightPart",
	components: {
		PanelItem,
		EchartLine,
		CapsuleTabs
	},
	data() {
		return {
			cwbtn: false, //潮位点位展示标记
			szbtn: 1, //水闸点位展示标记
			hwbtn: 1, //河网点位展示标记
			bxbtn: false, //堤防保险展示标记
			extraOption: {},
			riverNetworkType: 0,
			realtimePreChartTab: "理赔数量",
			realtimePreChart: 0,
			realtimeChartList: {},
			//大中型水库统计数据
			resData: {},
			realtimeChartData: {
				columns: ["name", "data"],
				rows: [
					{
						name: "鹿城",
						data: 0
					},
					{
						name: "平阳",
						data: 0
					},
					{
						name: "瓯江",
						data: 0
					},
					{
						name: "瑞安",
						data: 0
					},
					{
						name: "文成",
						data: 0
					}
				]
			},
			realtimeChartOption: {
				grid: {
					top: "15%",
					bottom: "1%",
					containLabel: true
				},
				setting: {
					yAxisName: ["mm"],
					itemStyle: { color: "#00C1FF" },
					label: {
						show: true,
						fontSize: 18,
						fontWeight: "bold",
						position: "top",
						color: "#fff",
						formatter: ({ value }) => Number(value).toFixed(1)
					}
				},
				extend: {
					"textStyle.color": "#fff",
					barWidth: 15,
					xAxis: {
						axisLabel: {
							interval: 0,
							fontSize: "16"
						}
					},
					yAxis: {
						nameTextStyle: {
							color: "#ffffff99",
							fontSize: "16"
						},
						axisLine: {
							show: false
						},
						axisLabel: {
							color: "#fff",
							fontSize: "16"
						},
						splitLine: {
							lineStyle: {
								color: "#00C1FF",
								fontSize: "18",
								opacity: 0.3
							}
						},
						max: value => {
							return value.max > 1 ? null : 1;
						}
					}
				}
			},
			// 潮位
			tideStaName: 0,
			tideStaNameVal: "温州",
			tideStaStart: "",
			tideStaEnd: "",
			insuranceList: [
				{
					lat: 27.675135971592585,
					lng: 120.2718940499314,
					name: "测试风险点"
				}
			]
		};
	},
	watch: {
		"$store.state.typhoonTimeData": function(val) {
			this.tideStaStart = val[0];
			this.tideStaEnd = val[1];
			this.tideReplayCurve({
				tideStaName: "温州", // 测站名 温州 龙湾 瑞安 鳌江
				start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
				end: this.tideStaEnd // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
			});
		}
	},
	mounted() {
		this.$nextTick(() => {
			let sTime =
				this.$moment()
					.subtract(3, "days")
					.format("YYYY-MM-DD HH") + ":00:00";
			let eTime =
				this.$moment()
					.subtract(0, "days")
					.format("YYYY-MM-DD HH") + ":00:00";
			this.tideStaStart = sTime;
			this.tideStaEnd = eTime;
			this.getRightMapPoints("潮位");
			this.tideReplayCurve({
				tideStaName: "温州", // 测站名 温州 龙湾 瑞安 鳌江
				start: sTime, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
				end: eTime // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
			});
		});
	},
	computed: {
		...mapGetters(["ScreenMap", "mapComponent"]),
		map() {
			return this.$store.state.screen.mapComponent;
		},
		inDrainResData() {
			let resList = this.resData.list || [];
			return resList.filter(item => Number(item.drainage) > 0);
		}
	},
	methods: {
		forecastChartTabChange(val) {
			// console.log(345,val);
		},
		insuranceChange(val) {
			this.realtimePreChartTab = val;
		},
		/**
		 * 点位list
		 */
		async getRightMapPoints(type) {
			let opt = {
				type
			};
			let res = await disasterPreventionApi.rightMapPoints(opt);
			switch (type) {
				case "潮位":
					{
						this.tideStList = res.data;
					}
					break;
			}
		},
		/**
		 * 潮位点位开关
		 */
		handleTideChange(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				scale: 0.5,
				anchor: [49, 16],
				anchorXUnits: "pixels",
				src: require("@/assets/images/cao-wei.png")
			};
			let pointList = [];
			for (let point of this.tideStList) {
				pointList.push({
					lng: point.stLong,
					lat: point.stLat,
					type: "潮位",
					name: point.stName,
					props: { 潮位: `${point.tdz}m` }
				});
			}
			if (this[btnType]) {
				this.$emit("legend-change", "潮位", {
					label: "潮位",
					children: [{ label: "正常", icon: "icon-chao-wei-1" }]
				});
				this.map.drawPoint(pointList, style, "tideLayer");
			} else {
				this.map.drawPoint([], {}, "tideLayer");
				this.$emit("legend-change", "潮位", {});
			}
		},
		/**
		 * 潮位tab切换
		 */
		tideStaNameTabChange(val) {
			switch (val) {
				case 0:
					{
						this.tideReplayCurve({
							tideStaName: "温州", // 测站名 温州 龙湾 瑞安 鳌江
							start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
							end: this.tideStaEnd // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
						});
					}
					break;
				case 1:
					{
						this.tideReplayCurve({
							tideStaName: "龙湾", // 测站名 温州 龙湾 瑞安 鳌江
							start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
							end: this.tideStaEnd // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
						});
					}
					break;
				case 2:
					{
						this.tideReplayCurve({
							tideStaName: "瑞安", // 测站名 温州 龙湾 瑞安 鳌江
							start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
							end: this.tideStaEnd // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
						});
					}
					break;
				case 3:
					{
						this.tideReplayCurve({
							tideStaName: "鳌江", // 测站名 温州 龙湾 瑞安 鳌江
							start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
							end: this.tideStaEnd // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
						});
					}
					break;
			}
		},
		/**
		 * 潮位接口
		 */
		async tideReplayCurve(val) {
			await disasterPreventionApi.tideReplayCurve(val).then(res => {
				if (res.code == 0) {
					let arr = [];
					let obj = [];
					let str = [];
					let series = [];
					let warnTide = [];
					let xAxisArr = [];
					this.extraOption = {};
					if (res.data.astroTide.length > 0) {
						res.data.astroTide.forEach(val => {
							if (val.tdz && val.tm) {
								arr.push(val.tdz);
								xAxisArr.push(val.tm.slice(5, 16));
							}
							warnTide.push(res.data.warnTide);
						});
						res.data.realTide.forEach(val => {
							obj.push(val.tdz);
						});
						res.data.subTide.forEach(val => {
							str.push(val.tdz);
						});
						let xAxis = [
							{
								axisLabel: {
									show: true,
									textStyle: {
										color: "#fff",
										fontSize: "15"
									},
									formatter: function(value) {
										var ret = ""; //拼接加\n返回的类目项
										var maxLength = 6; //每项显示文字个数
										var valLength = value.length; //X轴类目项的文字个数
										var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
										if (rowN > 1) {
											//如果类目项的文字大于3,
											for (var i = 0; i < rowN; i++) {
												var temp = ""; //每次截取的字符串
												var start = i * maxLength; //开始截取的位置
												var end = start + maxLength; //结束截取的位置
												//这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
												temp = value.substring(start, end) + "\n";
												ret += temp; //凭借最终的字符串
											}
											return ret;
										} else {
											return value;
										}
									}
								},
								data: xAxisArr
							}
						];
						this.extraOption.xAxis = xAxis;
						this.extraOption.legend = {
							show: true,
							top: "2",
							right: "10",
							orient: "horizontal",
							textStyle: {
								color: "#fff",
								fontSize: "16"
							},
							icon: "circle",
							itemWidth: 12,
							nameTextStyle: {
								color: "#00C1FF",
								fontSize: "16"
							}
						};
					}
					series.push(
						{
							data: str,
							type: "line",
							smooth: false,
							name: "增水",
							lineStyle: {
								width: 0, // 线宽是0
								color: "rgba(0, 0, 0, 0)" // 线的颜色是透明的
							},
							itemStyle: {
								opacity: 0
							}
						},
						{
							data: obj,
							type: "line",
							smooth: false,
							name: "实时水位",
							itemStyle: {
								normal: {
									lineStyle: {
										// color:  "#FF0000",
										width: 2
										// type: "dashed",
									}
								}
							},
							markPoint: {
								symbol: "circle",
								symbolSize: 10,
								data: [
									{
										name: "最大值",
										type: "max"
									}
								],
								label: {
									position: "top",
									fontSize: 18,
									fontWeight: "bold",
									formatter: function(data) {
										return data.value + "m";
									}
								}
							}
						},
						{
							data: warnTide,
							type: "line",
							smooth: true,
							name: "警戒水位",
							lineStyle: {
								color: "#FF0000",
								type: "dashed"
							},
							itemStyle: {
								opacity: 0
							}
						}
					);
					this.extraOption.series = series;
					this.extraOption.color = ["#FFF", "#55FFFF", "#FF0000"];
					this.extraOption.legend = {
						data: ["天文潮水位", "实时水位", "警戒水位"]
					};
					this.extraOption.grid = {
						top: 40
					};
				}
			});
		},
		/**
		 * 堤防保险点位开关
		 */
		handleInsuranceChange(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				src: require("@/assets/images/MapPoint/dike-insurance.png")
			};
			let pointList = [];
			for (let point of this.insuranceList) {
				pointList.push({
					lng: point.lng,
					lat: point.lat,
					type: "堤防保险",
					name: point.name,
					props: { 发起时间: "2019-10-25" }
				});
			}
			if (this[btnType]) {
				this.$emit("legend-change", "水利设施保险", {
					label: "设施保险",
					children: [{ label: "理赔点", icon: "icon-legend-dikeinsurance" }]
				});
				this.map.drawPoint(pointList, style, "insuranceLayer");
			} else {
				this.map.drawPoint([], {}, "insuranceLayer");
				this.$emit("legend-change", "水利设施保险", {});
			}
		},

		/**
		 * 打开详情弹框点击事件
		 */
		openDetailsDialog(val, data) {
			this.$emit("open-analyse-dialog", val, data);
		},
		handleReservoirChange(val) {
			this.skbtn++;
			if (this.skbtn % 2 != 0) {
				this.ScreenMap.drawPoint([], {}, "reservoirLayer");
				return;
			}
			this.$emit("handleReservoirChange", val);
			let style = {
				src: require("@/assets/images/legend-large-normal.png"),
				anchor: [12, 12],
				anchorXUnits: "pixels"
			};

			let pointList = this.inDrainResData.map(point => {
				return {
					name: point.resName,
					type: "水库",
					point,
					lng: point.lowLeftLong,
					lat: point.lowLeftLat,
					code: point.resCode,
					props: { 水位: `${point.waterLevel}m` }
				};
			});
			this.ScreenMap.drawPoint(pointList, style, "reservoirLayer");
		},
		handleSluiceChange(val) {
			this.szbtn++;
			this.$emit("handleSluiceChange", val);
		},
		handleRiverNet(val) {
			this.hwbtn++;
			this.$emit("handleRiverNet", val);
		},
		/**
		 * 跳转一张图
		 */
		goToAMap(type) {
			goAMap(type);
		},
		handleEngineeringChange(val) {
			this.riverNetworkType = val;
			this.realtimeChartOption.setting.yAxisName = [this.riverNetworkType == 0 ? "mm" : "万m³"];
			this.riverNetworkStaStats();
		},
		/**
		 * 河网数据
		 */
		async riverNetworkStaStats() {
			await disasterPreventionApi.NetworkStaStats().then(res => {
				if (res.code == 0) {
					this.realtimeChartList = res.data;
					this.realtimeChartData.rows = [];
					if (this.riverNetworkType == 0) {
						let rows = [];
						for (let item of res.data.list) {
							if (rows.length < 5) rows.push({ name: item.rnName, data: item.warnStorageAbility });
							else break;
						}
						this.realtimeChartData.rows = rows;
					} else {
						let rows = [];
						for (let item of res.data.list) {
							if (rows.length < 5) rows.push({ name: item.rnName, data: item.warnStorageSub });
							else break;
						}
						this.realtimeChartData.rows = rows;
					}
				}
			});
		},
		/**
		 * 柱状图表组件参数
		 */

		realtimeConfig(options) {
			let data = options.series[0].data;
			let colorArr = [];
			colorArr = ["#00A29D", "#1EF8F2", "#1EF8F2"];
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
			let barWidth = 30;
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
						fontSize: 18,
						fontWeight: "bold",
						position: "top",
						color: "#1EF8F2",
						formatter: ({ value }) => Number(value).toFixed(1)
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
			return options;
		}
	}
};
</script>

<style lang="scss" scoped>
@import "../../../style/SildePanel.scss";
.collapse-panel__content {
	.icon-sty {
		margin-right: 10px;
	}
	.insurance-item {
		display: flex;
		justify-content: center;
		padding: 10px 0;
	}
	.claim-settlement-sty {
		padding: 6px 0;
		width: 80%;
		color: #fff;
		display: flex;
		justify-content: center;
		&.active {
			background: linear-gradient(to bottom, #1cfff485, #1cfff485) left top no-repeat,
				linear-gradient(to bottom, #1cfff485, #1cfff485) left top no-repeat,
				linear-gradient(to bottom, #1cfff485, #1cfff485) right top no-repeat,
				linear-gradient(to bottom, #1cfff485, #1cfff485) right top no-repeat,
				linear-gradient(to bottom, #1cfff485, #1cfff485) left bottom no-repeat,
				linear-gradient(to bottom, #1cfff485, #1cfff485) left bottom no-repeat,
				linear-gradient(to bottom, #1cfff485, #1cfff485) right bottom no-repeat,
				linear-gradient(to bottom, #1cfff485, #1cfff485) right bottom no-repeat;
			background-size: 0.75rem 0.125rem, 0.125rem 0.75rem;
			border-radius: 0.1875rem;
		}
		> div {
			margin-left: 20px;
			> p:nth-child(2) {
				padding-top: 5px;
				margin-right: 15px;
				> span {
					font-family: PingFangSC-Medium;
					font-size: 28px;
					color: #1cfff5;
					letter-spacing: 0;
					line-height: 24px;
					font-weight: 500;
				}
			}
		}
	}
	.rainfall-style {
		margin: 20px 0;
		display: flex;
		justify-content: center;
		> p {
			font-size: 20px;
			> span {
				margin: 0 10px;
				color: #1cfff5;
				font-size: 24px;
			}
		}
	}
}
</style>
