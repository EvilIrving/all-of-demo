<!--
 * @Author: dyn
 * @Date: 2021-06-30 16:11:38
 * @LastEditTime: 2021-07-12 10:20:13
 * @LastEditors: Please set LastEditors
 * @Description: 河网
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\RiverCard.vue
-->
<template>
	<div class="river-card">
		<panel-item title="河网" has-switch tc @tclick="tclick">
			<template #title-switch>
				<i
					title="地图落点"
					@click="handleRiverNet('hwbtn')"
					:class="[hwbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
				/>
				<i v-show="!previewModule" title="查看详表" class="icon-liebiao icon-sty" @click="openDetailsDialog('河网')" />
				<i v-show="!previewModule" @click="goToAMap('河道水位')" title="一张图" class="icon-yizhangtu icon-sty" />
			</template>

			<el-carousel trigger="click" arrow="never" :autoplay="true" :interval="interval">
				<el-carousel-item class="carousel-item-content">
					<div class="river-style">
						<div id="river"></div>
					</div>
				</el-carousel-item>

				<el-carousel-item class="carousel-item-content">
					<panel-item title="" :action-tabs="['纳洪能力', '纳洪量']" @change="handleEngineeringChange">
						<!-- 河网 -->
						<div class="rainfall-style">
							<p v-if="riverNetworkType == 0">
								平均纳洪能力
								<span> {{ realtimeChartList.avgWarnStorageAbility }} </span> mm
							</p>
							<p v-else>
								纳洪量
								<span> {{ realtimeChartList.totalWarnStorageSub }} </span> 万m³
							</p>
						</div>
						<!-- 河网雷达图 -->
						<!-- <div id="riverEchart" style="width: 100%; height: 250px; margin-top: 20px"></div> -->
						<ve-histogram
							ref="chart"
							height="145px"
							width="100%"
							:data="realtimeChartData"
							:settings="realtimeChartOption.setting"
							:extend="realtimeChartOption.extend"
							:grid="realtimeChartOption.grid"
							:tooltip-visible="false"
							:legend-visible="false"
							:after-config="realtimeConfig"
						/>
					</panel-item>
				</el-carousel-item>
			</el-carousel>
		</panel-item>
	</div>
</template>

<script>
import * as echarts from "echarts"; // echart插件
import PanelItem from "../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { mapGetters, mapActions } from "vuex";
import { goAMap } from "./utils/common";
export default {
	name: "RiverCard",
	legendName: "河网",
	layerName: "riverNetLayer",
	components: {
		PanelItem
	},
	mounted() {
		this.$nextTick(() => {
			this.getRightMapPoints("河网");
			this.riverNetworkStaStatsOther(); //河网数据接口
			this.riverNetworkStaStats();
			this.riverNetwork();
			this.timer = setInterval(() => {
				this.getRightMapPoints("河网");
				this.riverNetworkStaStatsOther(); //河网数据接口
				this.riverNetworkStaStats();
				this.riverNetwork();
			}, 300000);
		});
	},
	beforeDestroy() {
		clearInterval(this.timer);
	},
	data() {
		return {
			interval: 5000,
			riverNetworkData: {
				rnName: [],
				waterLevel: [],
				warningLevel: [],
				guaranteeLevel: []
			},
			errorData: [], //河网柱状图统计数据
			riverChart: null,
			hwbtn: false, //河网点位展示标记
			//河网列表
			riverNetList: [],
			riverNetworkType: 0,
			realtimeChartList: {},
			realtimeChartData: {
				columns: ["name", "data"],
				rows: []
			},
			realtimeChartOption: {
				grid: {
					top: 40,
					bottom: "1%",
					containLabel: true
				},
				setting: {
					yAxisName: ["mm"],
					itemStyle: { color: "#fff" },
					label: {
						show: true,
						fontSize: 16,
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
							fontSize: this.$commonSetting.chartFontSize,
							formatter: function(item) {
								return item.replace("水系", "");
							}
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
			timer: null
		};
	},
	computed: {
		...mapGetters(["ScreenMap", "currentSelectArea", "previewModule", "formatterCurrentTime"]),
		map() {
			return this.$store.state.screen.mapComponent;
		}
	},
	methods: {
		...mapActions(["changeScreenLoading", "getForecastRainfallLayer", "SetDetailDialogParam"]),
		// 标题点击打开统计卡片
		tclick(title) {
			this.$emit("tclick", title);
		},
		/**
		 * 跳转一张图
		 */
		goToAMap(type) {
			goAMap(type);
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
				case "河网":
					{
						this.riverNetList = res.data;
					}
					break;
			}
		},
		// 河网落点
		handleRiverNet(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				scale: 0.7,
				anchor: [35, 16],
				anchorXUnits: "pixels",
				src: require("@/assets/images/hewang-zheng-chang.png")
			};

			let pointList = [];
			for (let point of this.riverNetList) {
				let imgSrc = require("@/assets/images/generalPoints/general-hydrology-abnormal.png");
				if (point.overWarn && Number(point.overWarn) > 0) {
					//超警
					if (point.compareLast == 1) {
						//上升
						imgSrc = require("@/assets/images/generalPoints/rivernet-abnormal-up.png");
					} else if (point.compareLast == -1) {
						//下降
						imgSrc = require("@/assets/images/generalPoints/rivernet-abnormal-down.png");
					} else {
						//持平
						imgSrc = require("@/assets/images/generalPoints/rivernet-abnormal.png");
					}
				} else {
					//不超警
					if (point.compareLast == 1) {
						//上升
						imgSrc = require("@/assets/images/generalPoints/rivernet-normal-up.png");
					} else if (point.compareLast == -1) {
						//下降
						imgSrc = require("@/assets/images/generalPoints/rivernet-normal-down.png");
					} else {
						//持平
						imgSrc = require("@/assets/images/generalPoints/rivernet-normal.png");
					}
				}
				pointList.push({
					stCode: point.mainStCode,
					stType: point.stType,
					lng: point.stLong,
					lat: point.stLat,
					type: "河网",
					src: imgSrc,
					name: point.mainStName,
					props: { 水位: `${point.waterLevel || "-"}m` }
				});
			}
			if (this[btnType]) {
				this.$emit("legend-change", "河网", {
					label: "河网",
					children: [
						{ label: "正常下降", icon: "icon-he-wang-zhengchang" },
						{ label: "超警下降", icon: "icon-ha-wang-chaojing" },
						{ label: "正常上升", icon: "icon-rivernet-legend-normal-up" },
						{ label: "超警上升", icon: "icon-rivernet-legend-abnormal-up" },
						{ label: "正常持平", icon: "icon-rivernet-legend-normal" },
						{ label: "超警持平", icon: "icon-rivernet-legend-abnormal" }
					]
				});
				this.map.drawPoint(pointList, style, "riverNetLayer");
			} else {
				this.map.drawPoint([], {}, "riverNetLayer");
				this.$emit("legend-change", "河网", {});
			}
		},
		/**
		 * 打开详情弹框点击事件
		 */
		openDetailsDialog(val) {
			this.SetDetailDialogParam({
				mediumSized: "",
				codeTpye: val,
				analyseDialogShow: true
			});
		},
		realtimeConfig(options) {
			let data = options.series[0].data;
			let colorArr = [];
			colorArr = ["#2886c6", "#50bfda", "#89e3ec"];
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
						color: "#00C1FF",
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
		},

		handleEngineeringChange(val) {
			this.riverNetworkType = val;
			this.realtimeChartOption.setting.yAxisName = [this.riverNetworkType == 0 ? "mm" : "万m³"];
			this.riverNetworkStaStats();
		},
		/**
		 * 第二页河网数据
		 */
		async riverNetworkStaStats() {
			await disasterPreventionApi.NetworkStaStats().then(res => {
				if (res.code == 0) {
					this.realtimeChartList = res.data;
					this.realtimeChartData.rows = [];
					if (this.riverNetworkType == 0) {
						let rows = [];
						for (let item of res.data.list) {
							if (rows.length < 5) {
								if (item.warnStorageAbility != null) {
									rows.push({ name: item.rnName, data: item.warnStorageAbility });
								}
							} else break;
						}
						this.realtimeChartData.rows = rows;
					} else {
						let rows = [];
						for (let item of res.data.list) {
							if (rows.length < 5) {
								if (item.warnStorageSub != null) {
									rows.push({ name: item.rnName, data: item.warnStorageSub });
								}
							} else break;
						}
						this.realtimeChartData.rows = rows;
					}
					this.$refs["chart"].echarts.resize();
				}
			});
		},
		/**
		 * 第一页河网
		 */
		async riverNetworkStaStatsOther() {
			await disasterPreventionApi.riverNetworkStaStats().then(res => {
				if (res.code == 0) {
					this.riverNetworkData.rnName = [];
					this.riverNetworkData.waterLevel = [];
					this.riverNetworkData.warningLevel = [];
					this.riverNetworkData.guaranteeLevel = [];

					res.data.list.forEach((val, index) => {
						let arr = [];
						arr.push(index);
						arr.push(val.warningLevel);
						arr.push(val.guaranteeLevel);
						this.errorData.push(arr);
						this.riverNetworkData.rnName.push(val.rnName.slice(0, 2));
						this.riverNetworkData.waterLevel.push(val.waterLevel.toFixed(2));
						this.riverNetworkData.warningLevel.push(val.warningLevel.toFixed(2));
						this.riverNetworkData.guaranteeLevel.push(val.guaranteeLevel.toFixed(2));
					});
					this.$nextTick(() => {
						this.riverNetwork();
					});
				}
			});
		},
		/**
		 * 河网柱状图
		 */
		handleWindowResize() {
			if (!this.riverChart) return;
			this.riverChart.resize();
		},

		riverNetwork() {
			var chartDom = document.getElementById("river");
			this.riverChart = echarts.init(chartDom);
			var errorData = this.errorData;
			var warnArr = this.riverNetworkData.warningLevel;
			var guaranteeArr = this.riverNetworkData.guaranteeLevel;
			var option;
			function renderItemLow(params, api) {
				var xValue = api.value(0);
				var lowPoint = api.coord([xValue, api.value(1)]);
				var halfWidth = (api.size([1, 0])[0] * 0.6) / 2;
				var style = api.style({
					stroke: "#EEC80B",
					fill: null,
					lineDash: [5, 5]
				});
				return {
					type: "line",
					transition: ["shape"],
					shape: {
						x1: lowPoint[0] - halfWidth + 5,
						y1: lowPoint[1],
						x2: lowPoint[0] + halfWidth - 5,
						y2: lowPoint[1]
					},
					style: style
				};
			}
			function renderItemHigh(params, api) {
				var xValue = api.value(0);
				var highPoint = api.coord([xValue, api.value(2)]);
				var halfWidth = (api.size([1, 0])[0] * 0.6) / 2;
				var style2 = api.style({
					stroke: "#FF7144",
					fill: null,
					lineDash: [5, 5]
				});
				return {
					type: "line",
					transition: ["shape"],
					shape: {
						x1: highPoint[0] - halfWidth + 5,
						y1: highPoint[1],
						x2: highPoint[0] + halfWidth - 5,
						y2: highPoint[1]
					},
					style: style2
				};
			}
			function tooltipFormat(data) {
				if (data.data.guarantee) {
					let htmlText =
						`<p class="rivertooltip__item rivertooltip__item-error"><span>保证: </span><span>${data.data.guarantee}m</span></p>` +
						`<p class="rivertooltip__item rivertooltip__item-warn"><span>警戒: </span><span>${data.data.warn}m</span></p>` +
						`<p class="rivertooltip__item rivertooltip__item-normal"><span>实时: </span><span>${data.value}m</span></p>`;
					return htmlText;
				}
			}
			let barData = [];
			let shadowArr = [];
			this.riverNetworkData.waterLevel.forEach((item, idx) => {
				let barPic = require("@/assets/images/bartop-normal.png");
				if (item > warnArr[idx]) {
					barPic = require("@/assets/images/bartop-warn.png");
				}
				if (item > guaranteeArr[idx]) {
					barPic = require("@/assets/images/bartop.png");
				}
				let obj = {
					value: item,
					warn: warnArr[idx],
					guarantee: guaranteeArr[idx],
					symbol: "image://" + barPic,
					symbolRepeat: true,
					symbolSize: ["30%", "20%"],
					symbolOffset: [0, 10],
					symbolMargin: "-48%",
					animationDelay: function(dataIndex, params) {
						return params.index * 10;
					}
				};
				barData.push(obj);
				shadowArr.push(3);
			});
			option = {
				grid: {
					top: 10,
					left: 0,
					right: 0,
					bottom: 35
				},
				xAxis: [
					{
						type: "category",
						axisLabel: {
							show: true,
							textStyle: {
								color: "#fff",
								fontSize: this.$commonSetting.chartFontSize
							},
							margin: 16
						},
						axisTick: {
							show: false
						},
						axisLine: {
							show: false
						},
						splitLine: {
							show: false
						},
						data: this.riverNetworkData.rnName
					}
				],
				yAxis: {
					type: "value",
					show: false
				},
				tooltip: {
					show: true,
					formatter: tooltipFormat
				},
				series: [
					{
						// 外边框
						name: "background",
						type: "pictorialBar", //echarts图的类型
						symbol: "reat", //内部类型（方块，圆，svg，base64图片）
						symbolSize: [35, 1000], //size,单个symbol的大小
						symbolOffset: [0, 10],
						itemStyle: {
							normal: {
								color: "rgba(255, 255, 225, 0.2)"
							}
						},
						tooltip: {
							show: true,
							formatter: function(data) {
								let htmlText =
									`<p class="rivertooltip__item rivertooltip__item-error"><span>保证: </span><span>${
										barData[data.dataIndex].guarantee
									}m</span></p>` +
									`<p class="rivertooltip__item rivertooltip__item-warn"><span>警戒: </span><span>${
										barData[data.dataIndex].warn
									}m</span></p>` +
									`<p class="rivertooltip__item rivertooltip__item-normal"><span>实时: </span><span>${
										barData[data.dataIndex].value
									}m</span></p>`;
								return htmlText;
							}
						},
						z: -180, //图层值
						symbolRepeat: null, //是否重复symbol
						data: [1, 1, 1, 1, 1, 1, 1],
						barGap: "-100%",
						animationEasing: "elasticOut"
					},
					{
						type: "custom",
						name: "警戒值",
						tooltip: {
							show: false
						},
						itemStyle: {
							normal: {
								borderWidth: 1.5
							}
						},
						label: {
							show: false,
							formatter: function(val) {
								return val.data[1];
							},
							position: ["-5%", "90%"],
							color: "#EEC80B",
							fontWeight: "bold",
							align: "right",
							fontSize: 16
						},
						renderItem: renderItemLow,
						encode: {
							x: 0,
							y: [1, 2]
						},
						data: errorData,
						z: 120
					},
					{
						type: "custom",
						name: "保证值",
						label: {
							show: false,
							formatter: function(val) {
								return val.data[2];
							},
							position: ["-5%", "-90%"],
							fontWeight: "bold",
							fontSize: 16,
							align: "right",
							color: "#FF7144"
						},
						tooltip: {
							show: false
						},
						itemStyle: {
							normal: {
								borderWidth: 1.5
							}
						},
						renderItem: renderItemHigh,
						encode: {
							x: 0,
							y: [1, 2]
						},
						data: errorData,
						z: 120
					},
					{
						type: "pictorialBar",
						name: "河网水位",
						hoverAnimation: true,
						label: {
							show: true,
							position: ["20%", "-10%"],
							fontSize: 18,
							color: "#fff"
						},
						showBackground: true,
						backgroundStyle: {
							color: "rgba(220, 220, 220, 0.8)"
						},
						data: barData
					}
				]
			};
			this.riverChart.setOption(option);
			this.$nextTick(() => {
				this.handleWindowResize();
			});
		}
	}
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.river-card {
	height: 100%;
	// padding: 0 16px;
	::v-deep.el-carousel {
		height: 100%;
		.el-carousel__container {
			height: 100%;
		}
	}

	.river-style {
		height: 100%;
		margin: 13px 0 0 0;
		font-size: 18px;
		#river {
			height: calc(100% - 20px);
		}
	}
	.rainfall-style {
		margin: 10px 0;
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
	.water-gate-sty {
		padding: 10px;
		// margin-bottom: 10px;
		background: url(~assets/images/WaterDisastersImage/shui-zha.png) no-repeat top center;
		background-size: 100% 100%;
		> div:nth-child(1) {
			display: flex;
			background: none;
			padding: 10px 0 15px 0;
			justify-content: space-between;
			border-bottom: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
			> p:nth-child(1) {
				color: #1cfff5;
				font-size: 24px;
			}
			> p:nth-child(2) {
				color: #fff;
				font-size: 20px;
				> span {
					color: #1cfff5;
					font-size: 24px;
				}
			}
			> p:nth-child(3) {
				font-size: 20px;
				color: #fff;
				> i {
					margin: 2px;
					vertical-align: unset;
				}
				> span {
					color: #1cfff5;
					font-size: 24px;
				}
			}
		}
		> div:nth-child(2) {
			display: flex;
			background: none;
			padding: 15px 0 10px 0;
			justify-content: space-between;
			> p:nth-child(1) {
				> i {
					font-size: 17px;
					margin: 8px 10px 0 0;
					color: #fff;
				}
				> span {
					font-size: 17px;
					line-height: 34px;
					margin-right: 10px;
				}
				font-size: 16px;
				color: #fff;
				display: flex;
				justify-content: left;
				::v-deep.el-input {
					width: 80px;
				}
				::v-deep.el-input-number {
					position: relative;
					display: inline-block;
					width: 5.25rem;
					border: 1px solid #ffffff;
					border-radius: 4px;
				}
				::v-deep.el-input__inner {
					background: none;
				}
				::v-deep.el-input-number__decrease {
					border-left: 0.0625rem solid #fff;
					background: none;
					> i {
						color: #fff;
					}
				}
				::v-deep.el-input-number__increase {
					height: 1.05rem;
					border-left: 0.0625rem solid #fff;
					background: none;
					> i {
						color: #fff;
					}
				}
			}
			> p:nth-child(2) {
				font-size: 20px;
				line-height: 1.7rem;
				color: #fff;
				font-size: 16px;
				> span {
					font-size: 28px;
					color: #1cfff5;
					padding: 0;
					margin: 0;
				}
			}
		}
	}
}
</style>

<style lang="scss">
.river-card .panel-item .panel-item__content--tabs .tab-button,
.river-card .panel-item .panel-item__title--tabs .tab-button {
	display: inline-block;
	font-size: 16px;
	padding: 0 12px;
}
.river-card .panel-item .panel-item__content--tabs .tab-button.active,
.river-card .panel-item .panel-item__title--tabs .tab-button.active {
	padding: 0 12px;
}
</style>
