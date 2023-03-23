<!--
 * @Author: your name
 * @Date: 2021-10-11 18:18:20
 * @LastEditTime: 2021-10-11 20:59:08
 * @LastEditors: Please set LastEditors
 * @Description: 水利设施保险小组件
 * @FilePath: \synthesizeSystem\src\views\water_disaster_custom\cards\WaterInsuranceFacilityCard.vue
-->
<template>
	<div class="dangerous-card">
		<panel-item title="水利设施保险" has-switch>
			<template #title-switch>
				<i title="地图落点" :class="[hwbtn % 2 == 0 ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']" />
				<i title="查看详表" class="icon-liebiao icon-sty" />
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
			<!-- 水利设施保险图表 -->
			<ve-histogram
				height="150px"
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
</template>

<script>
import PanelItem from "../components/PanelItem.vue";
import { goAMap } from "./utils/common";
export default {
	name: "ReservoirCard",
	components: {
		PanelItem
	},
	mounted() {},
	data() {
		return {
			hwbtn: 1, //水利设施保险
			realtimePreChartTab: "理赔数量",
			realtimeChartOption: {
				grid: {
					top: "19%",
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
			}
		};
	},
	methods: {
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
		insuranceChange(val) {
			this.realtimePreChartTab = val;
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
		},
		/**
		 * 跳转一张图
		 */
		goToAMap(type) {
			goAMap(type);
		}
	}
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.dangerous-card {
	height: 100%;
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
}
</style>
