<template>
	<div class="collapse-panel__content">
		<!-- 大中型水库 -->
		<panel-item title="大中型水库" hasSwitch>
			<template #title-switch>
				<i
					title="地图落点"
					@click="handleReservoirChange('skbtn')"
					:class="[skbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
				/>
				<i title="查看详表" @click="openDetailsDialog('水库', '大中型水库')" class="icon-liebiao icon-sty" />
				<i @click="goToAMap('水库')" title="一张图" class="icon-yizhangtu icon-sty" />
				<i title="洪水预报弹窗" class="icon-hongshui-yubao icon-sty" @click="FloodForecasting" />
			</template>
			<!-- 当前出库水库 -->
			<div class="reservoir-sty">
				<p>
					当前出库
					<span style="line-height: 35px; margin: 0 5px">
						{{ resData.drainageRsvrNum }}
					</span>
					座
				</p>
				<p>
					累计出库
					<span style="line-height: 35px; margin: 0 5px"> {{ resData.drainageTotal }}</span>
					万m³
				</p>
			</div>
			<!-- 详情出库量 -->
			<div class="flood-discharge">
				<el-row class="flood-discharge-list">
					<el-col
						v-for="(item, index) in inDrainResData"
						:key="index"
						:span="12"
						:class="['flood-discharge-item', [2].includes(index % 4) && 'white-bg']"
					>
						<div class="flood-discharge-item__content">
							<p class="item-title">{{ item.resName }}</p>
							<div class="item-text">
								<i class="icon-xie-hong"></i>
								<p class="item-label">
									出库
									<span class="number"> {{ item.drainage24H ? Number(item.drainage24H).toFixed(1) : "0" }} </span>万m³
								</p>
							</div>
						</div>
					</el-col>
				</el-row>
			</div>
		</panel-item>
		<!-- 水闸 -->
		<panel-item title="水闸" hasSwitch>
			<template #title-switch>
				<i
					title="地图落点"
					@click="handleSluiceChange('szbtn')"
					:class="[szbtn % 2 == 0 ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
				/>
				<i title="查看详表" @click="openDetailsDialog('水闸')" class="icon-liebiao icon-sty" />
				<i @click="goToAMap('水闸工情')" title="一张图" class="icon-yizhangtu icon-sty" />
			</template>
			<div class="rainfall-style">
				<p>
					总数 <span> {{ wagaData.wagaProjNum }} </span> 个
				</p>
			</div>
			<!-- 水闸详情 -->
			<div class="water-gate-sty">
				<!-- 大中型水库 -->
				<div>
					<p>温瑞平</p>
					<p>
						总数 <span> {{ wagaData.wrpNum }} </span> 个
					</p>
					<p>
						<i style="margin-right: 10px" class="icon-kai-qi-shu"></i>已开启 <span> {{ wagaData.wrpOpenNum }} </span> 个
					</p>
				</div>
				<div>
					<p>
						<i class="el-icon-time"></i><span style="line-height: 2.125rem">累计:</span>
						<el-input-number
							v-model="num"
							size="small"
							controls-position="right"
							:min="1"
							:max="72"
							@change="changeHours"
						/>
						<span style="font-size: 18px; color: #fff; line-height: 2.125rem">小时</span>
					</p>
					<p style="font-size: 18px">
						泄洪总量 <span> {{ wagaData.drainage }} </span> 万m³
					</p>
				</div>
			</div>
		</panel-item>
		<!-- 河网 -->
		<panel-item title="河网" :action-tabs="['纳洪能力', '纳洪量']" hasSwitch @change="handleEngineeringChange">
			<template #title-switch>
				<i
					title="地图落点"
					@click="handleRiverNet('hwbtn')"
					:class="[hwbtn % 2 == 0 ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
				/>
				<i title="查看详表" @click="openDetailsDialog('河网')" class="icon-liebiao icon-sty" />
				<i @click="goToAMap('河道水位')" title="一张图" class="icon-yizhangtu icon-sty" />
			</template>
			<div class="rainfall-style">
				<p v-if="riverNetworkType == 0">
					平均纳洪能力
					<span> {{ realtimeChartList.avgWarnStorageAbility }} </span> mm
				</p>
				<p v-else>
					纳洪量 <span> {{ realtimeChartList.totalWarnStorageSub }} </span> 万m³
				</p>
			</div>
			<!-- 河网雷达图 -->
			<!-- <div id="riverEchart" style="width: 100%; height: 250px; margin-top: 20px"></div> -->
			<ve-histogram
				height="250px"
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
	</div>
</template>

<script>
// echart插件
import * as echarts from "echarts";
// 标题组件
import PanelItem from "../../../components/PanelItem";
// api接口调用
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { mapGetters } from "vuex";
import { goAMap } from "@/views/water_disaster_custom/cards/utils/common";

export default {
	name: "FloodModule",
	components: {
		PanelItem
	},
	data() {
		return {
			skbtn: false, //水库点位展示标记
			szbtn: 1, //水闸点位展示标记
			hwbtn: 1, //河网点位展示标记
			//累计出库时间
			num: 1,
			riverNetworkType: 0,
			realtimeChartList: {},
			//水闸统计数据
			wagaData: {},
			//大中型水库统计数据
			resData: {},
			realtimeChartData: {
				columns: ["name", "data"],
				rows: []
			},
			realtimeChartOption: {
				grid: {
					top: "15%",
					bottom: "1%",
					containLabel: true
				},
				setting: {
					yAxisName: ["mm"],
					itemStyle: { color: "#fff" },
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
								color: "#fff",
								opacity: 0.5
							}
						},
						max: value => {
							return value.max > 1 ? null : 1;
						}
					}
				}
			}
		};
	},
	mounted() {
		this.$nextTick(() => {
			this.riverNetworkStaStats();
			this.rsvrProjDrainageStats();
			this.wagaStaStats(this.num);
			/* this.riverEchart(); */
		});
	},
	computed: {
		...mapGetters(["ScreenMap"]),
		inDrainResData() {
			let resList = this.resData.list || [];
			return resList.filter(item => Number(item.drainage24H) > 0);
		}
	},
	methods: {
		/**
		 * 打开详情弹框点击事件
		 */
		openDetailsDialog(val, data) {
			this.$emit("open-analyse-dialog", val, data);
		},
		handleReservoirChange(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				src: require("@/assets/images/legend/shuiku-juhe.png")
			};
			let pointList = this.inDrainResData.map(point => {
				let imgSrc = require("@/assets/images/legend/shuiku-juhe.png");
				if (point.overLimitVal != null && point.overLimitVal > 0) {
					imgSrc = require("@/assets/images/dianwei-juhe-chaoji.png");
				}
				return {
					name: point.resName,
					type: "水库",
					lng: point.lowLeftLong,
					lat: point.lowLeftLat,
					code: point.resCode,
					src: imgSrc,
					props: {
						实时水位: `${point.waterLevel} m`,
						汛限水位: `${point.currentLimitLevel} m`,
						更新时间: `${point.dataTime}`,
						鉴定结论: `${point.safetyConclusion}`
					}
				};
			});
			if (this[btnType]) {
				this.ScreenMap.drawPoint(pointList, style, "reservoirLayer");
				this.$emit("legend-change", "水库", {
					label: "水库",
					children: [
						{
							label: "正常",
							icon: "icon-legend-water-general-reservoir"
						},
						{
							label: "超警",
							icon: "icon-legend-water-flood-control"
						}
					]
				});
			} else {
				this.ScreenMap.drawClusterPoint([], {}, "reservoirLayer");
				this.$emit("legend-change", "水库", {});
			}
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
		/**
		 * 大中型水库
		 */
		async rsvrProjDrainageStats() {
			await disasterPreventionApi.rsvrProjDrainageStats().then(res => {
				if (res.code == 0) {
					this.resData = res.data;
				}
			});
		},
		/**
		 * 洪水预备
		 */
		FloodForecasting() {
			this.$emit("FloodForecasting");
		},
		/**
		 * 水闸统计数据
		 */
		async wagaStaStats(val) {
			let obj = {
				hours: val
			};
			await disasterPreventionApi.wagaProjStats(obj).then(res => {
				if (res.code == 0) {
					this.wagaData = res.data;
				}
			});
		},
		/**
		 * 河网雷达图
		 */
		riverEchart() {
			var chartDom = document.getElementById("riverEchart");
			var myChart = echarts.init(chartDom);
			var option;
			option = {
				radar: [
					{
						indicator: [
							{ name: "乐成", max: 65 },
							{ name: "江南", max: 60, axisLabel: { show: false } },
							{ name: "温瑞", max: 30, axisLabel: { show: false } },
							{ name: "瑞平", max: 38, axisLabel: { show: false } },
							{ name: "鳌江", max: 52, axisLabel: { show: false } },
							{ name: "永强", max: 25, axisLabel: { show: false } }
						],
						center: ["50%", "50%"],
						radius: 95,
						name: {
							textStyle: {
								color: "#fff"
							}
						},
						splitArea: {
							areaStyle: {
								color: "rgba(114, 172, 209, 0)", //圆环颜色
								shadowColor: "#fff", // 圆颜色
								shadowBlur: 10
							}
						},
						axisLine: {
							lineStyle: {
								color: "rgba(114, 172, 209, 0.7)" // 分割线
							}
						},
						splitLine: {
							lineStyle: {
								color: "rgba(114, 172, 209, 0.7)" //圆线
							}
						},
						axisLabel: {
							show: true
						}
					}
				],
				series: [
					{
						// name: '预算 vs 开销（Budget vs spending）',
						type: "radar",
						data: [
							{
								value: [42, 30, 20, 35, 50, 18],
								name: "预算分配（Allocated Budget）",
								label: {
									normal: {
										show: true
									}
								},
								areaStyle: {
									color: new echarts.graphic.RadialGradient(0.1, 0.6, 1, [
										{
											color: "rgba(238, 200, 11, 0.5)",
											offset: 0
										},
										{
											color: "rgba(232, 0, 0, 1)",
											offset: 1
										}
									])
								}
							}
						]
					}
				]
			};
			option && myChart.setOption(option);
		},
		/**
		 * 累计出库时间
		 */
		changeHours() {
			this.wagaStaStats(this.num);
		}
	}
};
</script>

<style lang="scss" scoped>
@import "../../../style/SildePanel.scss";
.collapse-panel__content {
	.icon-sty {
		margin-right: 0.3125rem;
	}
	.reservoir-sty {
		font-size: 20px;
		margin: 5px 0;
		> p:nth-child(1) {
			margin-right: 16px;
			> span {
				color: #1cfff5;
			}
		}
		> p:nth-child(2) {
			background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat top center;
			background-size: 100% 100%;
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
.flood-discharge {
	user-select: none;
	color: #fff;
	width: 470px;
	height: 250px;
	overflow-y: scroll;
	&::-webkit-scrollbar {
		display: none;
	}
	.flood-discharge-item {
		padding: 16px 0;
		border-right: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
		position: relative;
		&.white-bg {
			&:after {
				content: "";
				position: absolute;
				height: 100%;
				width: 200%;
				left: 0;
				top: 0;
				opacity: 0.5;
				background-image: linear-gradient(90deg, #ffffff33 0%, #f9f9f900 100%);
			}
		}
		&:nth-child(even) {
			border-right: 0;
		}
		.flood-discharge-item__content {
			.item-title {
				font-size: 24px;
				text-align: center;
			}
			.item-text {
				display: flex;
				padding: 5px 0;
				font-size: 18px;
				justify-content: center;
				> i {
					margin: 5px 10px 0 0;
				}
				.item-label {
					.number {
						margin: 0 5px;
						font-size: 24px;
						color: #1cfff5;
					}
				}
			}
		}
	}
}
</style>
