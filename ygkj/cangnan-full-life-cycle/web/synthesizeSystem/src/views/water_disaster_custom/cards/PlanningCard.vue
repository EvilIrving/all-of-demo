<template>
	<div class="planning-card">
		<page-pane>
			<template slot="title">
				<el-select v-model="titleId" class="page-title-select" @change="onTitleChange">
					<el-option v-for="(item, index) in titleOption" :key="index" :label="item.planName" :value="item.id" />
				</el-select>
			</template>
			<template slot="append">
				<i :class="[pointActive ? 'icon-yinzhang-active' : 'icon-yinzhang-1']" @click="onPointClick" />
				<i class="icon-liebiao" @click="openDetailsDialog('规划')" />
				<i @click="goToAmap('发展规划')" title="一张图" class="icon-yizhangtu icon-sty" style="margin-left: 10px" />
			</template>
			<div class="planning-statistics">
				<div class="custom-legend-chart">
					<ve-ring
						height="180px"
						width="160px"
						v-bind="investOption"
						:data="investData"
						:tooltip-visible="false"
						:legend-visible="false"
						:after-config="afterInvestConfig"
						:loading="investData.rows.length == 0"
					/>
					<div class="custom-legend">
						<ul class="list">
							<li v-for="(item, index) in investData.rows" :key="index" class="list-item">
								<i class="dot" :style="{ backgroundColor: investColors[index] }" />
								<div class="legend-unit">
									<div>{{ item.name }}</div>
									<div>
										<span>{{ item.data }}</span> 亿元
									</div>
								</div>
							</li>
						</ul>
					</div>
				</div>

				<div class="invest-summary">
					<div class="summary-item">
						<i class="icon-invest-total"></i>
						<div class="item-content">
							<p class="title">总投资</p>
							<div class="label">
								{{ Number(investStatData.amountInvestTotal / 10000).toFixed(2) || "-" }}<span>亿元</span>
							</div>
						</div>
					</div>
					<div class="summary-item">
						<i class="icon-invest-carry-over"></i>
						<div class="item-content">
							<p class="title">结转投资</p>
							<div class="label">
								{{ Number(investStatData.amountInvestIndirect / 10000).toFixed(2) || "-" }}<span>亿元</span>
							</div>
						</div>
					</div>
					<div class="summary-item">
						<i class="icon-invest-finish"></i>
						<div class="item-content">
							<p class="title">已完成投资</p>
							<div class="label">
								{{ Number(investStatData.amountInvested / 10000).toFixed(2) || "-" }}<span>亿元</span>
							</div>
						</div>
					</div>
					<div class="summary-item">
						<i class="icon-invest-finish-rate"></i>
						<div class="item-content">
							<p class="title">投资完成率</p>
							<div class="label">{{ Number(investStatData.investFinishRate).toFixed(1) || "-" }}<span>%</span></div>
						</div>
					</div>
				</div>
			</div>

			<table-title title="项目类型" />
			<div class="custom-legend-chart">
				<ve-ring
					height="180px"
					width="160px"
					v-bind="prjectOption"
					:data="projectData"
					:tooltip-visible="false"
					:legend-visible="false"
					:after-config="afterProjectConfig"
					:loading="projectData.rows.length == 0"
				/>
				<div class="custom-legend">
					<ul class="list">
						<li v-for="(item, index) in projectData.rows" :key="index" class="list-item">
							<i class="dot" :style="{ backgroundColor: projectColors[index] }" />
							{{ item.name }}({{ item.data }})
						</li>
					</ul>
				</div>
			</div>
			<table-title title="分区县水利规划投资完成率" />
			<div style="height:calc(100% - 598px)">
				<ve-histogram
					v-bind="finishRateOption"
					ref="chart"
					height="100%"
					width="100%"
					:data="finishRateData"
					:tooltip-visible="false"
					:legend-visible="false"
					:after-config="afterFinishRateConfig"
					:loading="finishRateData.rows.length == 0"
				/>
			</div>
		</page-pane>
	</div>
</template>
<script>
import "v-charts/lib/style.css";
import { PagePane, PagePaneItem } from "../components/PagePane";
import { genBodyLinearGradients, generateSeries, generateDataZoom, genNormalLinearGradients } from "./utils/chart.js";
import { TableTitle } from "./CardPages/components.js";
import { planDigitizationApi } from "@/api/plan_digitization/index.js";
import { mapActions } from "vuex";
import { goAmap } from "./utils/common";
const RingOptions = {
	grid: {
		top: 40,
		bottom: "2%",
		containLabel: true
	},
	settings: { radius: [45, 75] },
	extend: {
		"textStyle.color": "#fff",
		series: {
			center: [80, "50%"],
			hoverAnimation: false,
			label: { show: false }
		}
	}
};
export default {
	name: "PlanningCard",
	layerName: "planningLayer",
	legendName: "规划",
	components: { PagePane, PagePaneItem, TableTitle },
	data() {
		return {
			titleOption: [],
			titleId: null,

			pointActive: false,
			finishRateList: {},
			finishRateData: {
				columns: ["name", "data"],
				rows: []
			},
			finishRateOption: {
				grid: {
					top: 40,
					bottom: "2%",
					containLabel: true
				},
				settings: {
					yAxisName: ["%"],
					itemStyle: { color: "#fff" },
					label: {
						show: true,
						fontSize: this.$commonSetting.chartFontSize,
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
						axisLine: { show: false },
						axisLabel: { color: "#fff", fontSize: this.$commonSetting.chartFontSize },
						splitLine: {
							lineStyle: {
								color: "#fff",
								opacity: 0.5
							}
						},
						max: 100
					}
				}
			},
			projectData: {
				columns: ["name", "data"],
				rows: []
			},
			prjectOption: RingOptions,
			projectColors: [],

			investData: {
				columns: ["name", "data"],
				rows: []
			},
			investOption: RingOptions,
			investColors: [],

			investStatData: {},

			coordinateList: []
		};
	},
	computed: {
		map() {
			return this.$store.state.screen.mapComponent;
		}
	},
	mounted() {
		this.getPlanType().then(() => {
			this.onTitleChange(this.titleId);
		});
	},
	methods: {
		...mapActions(["SetDetailDialogParam", "setPlanning"]),
		goToAmap(type) {
			goAmap(type);
		},
		/**
		 * 打开详情弹框点击事件
		 */
		openDetailsDialog(val) {
			let title = this.titleOption.find(item => item.id == this.titleId).planName;
			let id = this.titleId;
			this.SetDetailDialogParam({
				mediumSized: `${title}|${id}`,
				codeTpye: val,
				analyseDialogShow: true
			});
		},
		onTitleChange(value) {
			let { id, planName } = this.titleOption.find(item => item.id == value);
			this.$EventBus.$emit("planningCountChange", { value: id, title: planName });
			this.setPlanning({ id: id, title: planName });
			this.getStatsOfDevPlan();
		},
		showLegend(show = true) {
			let title = this.titleOption.find(item => item.id == this.titleId).planName;
			this.$emit("legend-change", "规划", {});
			if (show)
				this.$emit("legend-change", "规划", {
					label: `${title}`,
					children: [
						{
							label: "项目",
							icon: "icon-legends-planning"
						}
					]
				});
		},
		onPointClick() {
			let lineStyle = {
				lineWidth: 4,
				lineColor: "#4CEBC7"
			};
			let pointStyle = {
				scale: 0.5,
				anchor: [49, 16],
				anchorXUnits: "pixels",
				src: require("@/assets/images/MapPoint/planning.png")
			};
			this.pointActive = !this.pointActive;
			if (this.pointActive) {
				const lineList = this.coordinateList.filter(item => Object.keys(item).includes("lineList"));
				const pointList = this.coordinateList.filter(item => !Object.keys(item).includes("lineList"));
				this.map.drawLineEx(lineList, lineStyle);
				this.map.drawPoint(pointList, pointStyle, "planningLayer");
				this.showLegend();
			} else {
				this.map.removeLineLayer();
				this.map.drawPoint([], {}, "planningLayer");
				this.showLegend(false);
			}
		},
		afterFinishRateConfig(options) {
			let data = options.series[0].data;
			let color = genBodyLinearGradients("#2886c6", "#50bfda");
			options.series = generateSeries({ data, color: color });
			options.dataZoom = generateDataZoom(4, data.length);
			return options;
		},
		afterProjectConfig(options) {
			this.projectColors = options.color;
			let data = options.series[0].data;
			let count = 0;
			if (data.length > 0) count = data.map(i => i.value).reduce((total, cur) => total + cur);
			options.title = {
				show: true,
				text: `{count|${count}}个\n总数`,
				textStyle: { color: "#fff", rich: { count: { fontSize: 22 } } },
				textAlign: "center",
				left: 75,
				top: "40%"
			};
			options.color = options.color.map(item => {
				return genNormalLinearGradients(item, "#000");
			});
			return options;
		},
		afterInvestConfig(options) {
			this.investColors = options.color;
			let data = options.series[0].data;
			let count = 0;
			if (data.length > 0) count = data.map(i => i.value).reduce((total, cur) => total + cur);

			options.color = options.color.map(item => {
				return genNormalLinearGradients(item, "#000");
			});
			options.title = {
				show: true,
				text: `{count|${count}}万元\n规划总投资`,
				textStyle: { color: "#fff", rich: { count: { fontSize: 22 } } },
				textAlign: "center",
				left: 75,
				top: "40%"
			};

			return options;
		},

		async getPlanType() {
			await planDigitizationApi.getListPlanTypeOfDevPlan().then(res => {
				if (res.code == 0) {
					this.titleOption = res.data.reverse().map(item => ({
						id: item.id,
						planName: item.ptName
					}));
					this.titleId = this.titleOption[0].id;
				}
			});
		},
		async getStatsOfDevPlan() {
			await planDigitizationApi.getStatsOfDevPlan({ ptId: this.titleId }).then(res => {
				if (res.code == 0) {
					let { investFinishRate4County, projStat4PlanRec, investTotal4County, ...rest } = res.data;
					this.finishRateData.rows = investFinishRate4County.map(item => ({
						name: item.areaName,
						data: parseInt(item.rate).toFixed(1)
					}));

					this.projectData.rows = projStat4PlanRec.map(item => ({
						name: item.prName,
						data: item.num
					}));
					this.investData.rows = investTotal4County.map(item => {
						let data = Number((item.amount / 10000).toFixed(1));

						return {
							name: item.areaName,
							data
						};
					});
					this.investStatData = rest;
				}
			});
			planDigitizationApi.getPageDevPlanProj({ ptId: this.titleId }).then(res => {
				if (res.code == 0) {
					let coordinateList = [];
					const filteredList = res.data.list
						.filter(item => item.mapCoordinate != "")
						.map(item => {
							let coordinateData = JSON.parse(item.mapCoordinate);
							let { point, line } = coordinateData;
							let props = {
								总投资: `${item.amountInvestTotal}万元`,
								已完成投资: `${item.amountInvested}万元`
							};

							line = line.map(item => {
								let lineList = [];
								if (item.coodinate.length > 0) {
									let temp = [];
									for (let i = 0; i < item.coodinate.length / 2; i++) {
										temp.push(item.coodinate.slice(i * 2, i * 2 + 2));
									}
									lineList = temp;
								}
								return { lineList, data: { props, name: item.projectName } };
							});
							point = point.map(item => ({
								lng: item.lat,
								lat: item.lng,
								name: item.projectName,
								props
							}));
							return { point, line };
						});
					filteredList.forEach(item => {
						coordinateList = [
							...item.line.map(line => ({ props: item.props, ...line })),
							...item.point.map(point => ({ props: item.props, ...point })),
							...coordinateList
						];
					});
					//
					this.coordinateList = coordinateList;
				}
			});
		}
	}
};
</script>
<style lang="scss">
.planning-card {
	.page-title-select {
		width: 180px;
		.el-input__inner {
			background: #0000;
			font-size: 24px;
			font-weight: 600;
			padding: 0;
		}
	}
	.planning-statistics {
		margin-top: 16px;
		display: flex;
		flex-wrap: wrap;

		.invest-summary {
			width: 100%;
			display: flex;
			flex-wrap: wrap;
			.summary-item {
				width: 50%;
				display: flex;
				padding: 8px;
				.item-content {
					margin-left: 16px;
					.label {
						margin-top: 8px;
						color: #56fefe;
						font-size: 20px;
						> span {
							font-size: 14px;
							color: #fff;
							margin-left: 3px;
						}
					}
				}
			}
		}
	}
	.custom-legend-chart {
		display: flex;
		flex-direction: row;
		.custom-legend {
			width: 210px;
			height: 180px;
			overflow-y: scroll;
			&::-webkit-scrollbar-thumb:vertical {
				background-color: #1ef8f2;
			}
			.list {
				.list-item {
					padding: 10px 6px;
					font-size: 16px;
					color: #fff;
					font-weight: 500;
					position: relative;
					padding-left: 20px;
					.dot {
						content: "";
						position: absolute;
						left: 0;
						top: 50%;
						width: 12px;
						height: 12px;
						border-radius: 50%;
						transform: translateY(-50%);
					}
					.legend-unit {
						display: flex;
						justify-content: space-between;
						> div:nth-child(2) {
							> span {
								font-size: 20px;
								color: #1cfff5;
							}
						}
					}
				}
			}
		}
	}
}
</style>
