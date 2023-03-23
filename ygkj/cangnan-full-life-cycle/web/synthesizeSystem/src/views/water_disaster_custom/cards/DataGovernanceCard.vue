<template>
	<div class="data-governance-card">
		<page-pane title="数据治理" @onTitle="() => {}">
			<template #append>
				<i class="icon-liebiao" @click="onDetailListClick" />
			</template>
			<table-title title="数据来源" />
			<div class="data-source">
				<div class="pyramid-stereoscopic">
					<div class="triangle-wrap left-side">
						<div class="triangle-side"></div>
					</div>
					<div class="triangle-wrap right-side">
						<div class="triangle-side"></div>
					</div>
				</div>
				<div class="source-list">
					<ul>
						<li
							v-for="(item, index) in dataSource"
							:key="index"
							:style="`--source-list-color:${['#3ee5e3', '#3bbcd9', '#378bcc'][index]}`"
						>
							<p class="department">
								<span>{{ item.department }}</span>
							</p>
							<p class="detail">
								<span> {{ item.tableNumber }}张表 {{ item.dataNumber }}数据</span>
							</p>
						</li>
					</ul>
				</div>
			</div>
			<CapsuleTabs v-model="currentTab" :tabs="['治理规则', '治理成效']" />
			<div v-if="currentTab == 0" class="governance-rule">
				<el-row>
					<el-col :span="12">
						<ve-gauge
							height="160px"
							v-bind="governanceGaugeOption"
							:data="governanceGaugeData"
							:tooltip-visible="false"
							:legend-visible="false"
						></ve-gauge>
					</el-col>
					<el-col :span="12">
						<div class="governance-rule-list">
							<ul>
								<li style="--rule-list-color: #3ee5e3">{{ governanceRule.hasRuleTableNumber }}张表经过治理入库</li>
								<li style="--rule-list-color: #eec80b">{{ governanceRule.noRuleTableNumber }}张表尚未有治理规则</li>
							</ul>
						</div>
					</el-col>
				</el-row>
			</div>
			<div v-else-if="currentTab == 1" class="governance-result">
				<div class="governance-result__content">
					<div class="result-item">
						<span><i class="icon-diamond" />累计发现问题数</span>
						<span class="unit">{{ governanceResult.questionCount }}</span>
					</div>
					<div class="result-item">
						<span><i class="icon-diamond" />推送信息数</span>
						<span class="unit">{{ governanceResult.messageCount }}</span>
					</div>
					<div class="result-item">
						<span> <i class="icon-diamond" />数据治理入库数据</span>
						<span class="unit">{{ governanceResult.persistenceCount }}</span>
					</div>
				</div>
			</div>
			<ve-histogram
				v-bind="governanceRuleOption"
				:data="governanceRuleData"
				:tooltip-visible="false"
				:legend-visible="false"
				:after-config="afterHistogramConfig"
				:loading="governanceRuleData.rows.length == 0"
				height="160px"
				width="100%"
			/>
			<table-title title="数据完整率" />
			<ve-histogram
				v-bind="dataCompletenessOption"
				:data="dataCompletenessData"
				:tooltip-visible="false"
				:legend-visible="false"
				:after-config="afterHistogramConfig"
				:loading="dataCompletenessData.rows.length == 0"
				height="160px"
				width="100%"
			/>
		</page-pane>
		<el-dialog
			top="10vh"
			width="1200px"
			destroy-on-close
			append-to-body
			:visible.sync="visible"
			:close-on-click-modal="false"
			class="primary-dialog detail-table"
		>
			<div class="primary-dialog__title" slot="title">
				<p class="primary-dialog__title--label">详表</p>
			</div>
			<div class="primary--dialog__content">
				<el-row :gutter="20" class="query-sty">
					<el-col :span="6">
						<div class="demo-input-suffix">
							<p style="width: 120px">数据资源名称:</p>
							<el-input v-model="dataName" size="small" @input="getList" />
						</div>
					</el-col>
				</el-row>
				<div class="tab-list-sty" style="padding-bottom: 20px">
					<el-table
						v-loading="loading"
						element-loading-text="拼命加载中"
						element-loading-spinner="el-icon-loading"
						element-loading-background="rgba(0, 0, 0, 0.8)"
						ref="travelTable"
						:data="tableData"
						:height="500"
						stripe
					>
						<el-table-column type="index" label="序号" width="55" />
						<el-table-column label="表名" prop="dataName" />
						<el-table-column label="治理规则数量" prop="persistenceCount" />
						<el-table-column label="数据治理入库比例" prop="persistenceRate" />
						<el-table-column label="发现问题数量" prop="questionCount" />
						<el-table-column label="发现问题数量" prop="messageCount" />
					</el-table>
				</div>
			</div>
		</el-dialog>
	</div>
</template>
<script>
import "v-charts/lib/style.css";
import { TableTitle } from "./CardPages/components.js";
import { PagePane, PagePaneItem } from "../components/PagePane";
import { genBodyLinearGradients, generateSeries, genNormalLinearGradients, generateDataZoom } from "./utils/chart.js";
import {
	getDataSource,
	getGovernanceRule,
	getGovernanceResult,
	getDataCompleteRate,
	getDataGovernanceList
} from "@/api/data-management/index.js";
import CapsuleTabs from "../components/CapsuleTabs.vue";

const color1 = genNormalLinearGradients("#F7C739", "#E02020", { x: 0, y: 0, x2: 0, y2: 1 });
const color2 = genNormalLinearGradients("#193CC5", "#F7C739", { x: 0, y: 0, x2: 1, y2: 0 });
const color3 = genNormalLinearGradients("#6F9DF2", "#193CC5", { x: 0, y: 0, x2: 1, y2: 0 });
const color4 = genNormalLinearGradients("#6F9DF2", "#19C57D", { x: 0, y: 0, x2: 0, y2: 1 });

export default {
	name: "DataGovernance",
	components: { TableTitle, PagePane, PagePaneItem, CapsuleTabs },
	data() {
		this.HistogramOption = {
			grid: {
				top: 35,
				bottom: "2%",
				containLabel: true
			},
			settings: {
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
					}
				}
			}
		};
		return {
			currentTab: 0,
			governanceGaugeOption: {
				grid: {
					top: "10%",
					left: 0,
					right: 0,
					bottom: 20
				},
				extend: {
					series: {
						itemStyle: {
							color: "#fdfdfd"
						},
						pointer: {
							width: 5
						},
						// echart v5.0 support
						anchor: {
							show: true,
							showAbove: true,
							size: 25,
							itemStyle: {
								borderColor: "#fdfdfd",
								borderWidth: 10
							}
						},
						radius: "90%",
						axisTick: { show: false },
						axisLine: {
							lineStyle: {
								fontSize: 18,
								color: [
									[0.2, color4],
									[0.4, color3],
									[0.6, color2],
									[1, color1]
								],
								width: 12
							}
						},
						title: { show: false },
						detail: {
							valueAnimation: true,
							fontSize: 20,
							color: "#fff",
							offsetCenter: [0, "60%"],
							formatter: v => `${v}%`
						},
						splitLine: { distance: 100, length: 8 },
						min: 0,
						max: 100,
						splitNumber: 5,
						axisLabel: {
							color: "#aaa",
							distance: 0,
							fontSize: 14
						}
					}
				}
			},
			governanceGaugeData: {
				columns: ["name", "data"],
				rows: [{ name: "name", data: 0 }]
			},
			governanceRuleOption: Object.assign(this.HistogramOption, { settings: { yAxisName: ["条 "] } }),
			governanceRuleData: {
				columns: ["name", "data"],
				rows: [
					{ name: "频率规则", data: 9 },
					{ name: "数据范围", data: 8 },
					{ name: "数据有效", data: 7 },
					{ name: "浮点转换", data: 7 }
				]
			},

			dataCompletenessOption: Object.assign(this.HistogramOption, { settings: { yAxisName: ["%"] } }),
			dataCompletenessData: {
				columns: ["name", "data"],
				rows: [
					{ name: "水灾害防御", data: 16 },
					{ name: "河湖库保护", data: 3 },
					{ name: "水事务监管", data: 4 },
					{ name: "水资源保护", data: 33 },
					{ name: "水发展规划", data: 75 }
				]
			},
			visible: false,
			loading: false,
			dataName: "",
			tableData: [],
			dataSource: [],
			governanceRule: { noRuleTableNumber: 0, hasRuleTableNumber: 0 },
			governanceResult: {
				questionCount: 0,
				messageCount: 0,
				persistenceCount: 0
			}
		};
	},
	mounted() {
		this.getDataSource();
		this.getGovernanceRule();
		this.getGovernanceResult();
		this.getDataCompleteRate();
	},
	methods: {
		afterHistogramConfig(options) {
			let data = options.series[0].data;
			let color = genBodyLinearGradients("#2886c6", "#50bfda");
			options.series = generateSeries({ data, color: color }, false);
			if (data.length > 7) {
				options.dataZoom = generateDataZoom(4, data.length);
			}
			return options;
		},
		getList() {
			getDataGovernanceList({ dataName: this.dataName }).then(res => {
				if (res.code == 0) {
					this.tableData = res.data.list;
				}
			});
		},
		onDetailListClick() {
			this.visible = true;
			this.getList();
		},
		getDataSource() {
			getDataSource().then(res => {
				if (res.code == 0) {
					this.dataSource = res.data;
				}
			});
		},
		getGovernanceRule() {
			getGovernanceRule().then(res => {
				if (res.code == 0) {
					const { noRuleTableNumber: noNumber, hasRuleTableNumber: hasNumber, chartArray } = res.data;
					this.governanceRule.noRuleTableNumber = noNumber;
					this.governanceRule.hasRuleTableNumber = hasNumber;
					this.governanceGaugeData.rows = [
						{
							name: "name",
							data: Math.floor((hasNumber / (hasNumber + noNumber)) * 100)
						}
					];
					this.governanceRuleData.rows = chartArray.map(item => ({ name: item.ruleType, data: item.number }));
				}
			});
		},
		getGovernanceResult() {
			getGovernanceResult().then(res => {
				if (res.code == 0) {
					this.governanceResult = res.data;
				}
			});
		},
		getDataCompleteRate() {
			getDataCompleteRate().then(res => {
				if (res.code == 0) {
					this.dataCompletenessData.rows = res.data.map(item => ({
						name: item.tableName.replace(/基础信息表/, "信息"),
						data: Math.floor(item.rate * 100)
					}));
				}
			});
		}
	}
};
</script>
<style lang="scss">
.data-source {
	display: flex;
	margin-top: 30px;
	.source-list {
		margin-top: 10px;
		> ul > li {
			position: relative;
			margin-bottom: 15px;
			&::before {
				content: " ";
				position: absolute;
				top: -10px;
				left: 0;
				width: 20px;
				height: 4px;
				background-color: var(--source-list-color);
			}
			p {
				line-height: 1.5;
			}
			.department {
				font-size: 14px;
				color: #fff;
			}
			.detail {
				font-size: 14px;
				color: #1cfff5;
			}
		}
	}
}
.pyramid-stereoscopic {
	position: relative;
	width: 168px;
	height: 160px;
	.triangle-wrap {
		position: absolute;
		.triangle-side {
			background: #000;
			height: 130px;
			width: 85px;
			clip-path: polygon(0 100%, 50% 0, 100% 100%);
		}
		&.left-side {
			left: 0;
			.triangle-side {
				background: linear-gradient(
					to bottom,
					#3bdbda 0px,
					#3ee4e3 30px,
					#3bbcd9 31px,
					#3bbcd9 60px,
					#399ad0 61px,
					#378bcc 90px,
					#345ebf 91px,
					#345ebf 120px
				);
				transform-origin: right bottom;
				transform: rotate(18deg);
			}
		}
		&.right-side {
			left: 84px;
			.triangle-side {
				background: linear-gradient(
					to bottom,
					#37ccca 0px,
					#37ccca 30px,
					#34a6bf 31px,
					#34a6bf 60px,
					#307ab2 61px,
					#307ab2 90px,
					#2e54a9 91px,
					#2e54a9 120px
				);
				transform-origin: left bottom;
				transform: rotate(-18deg);
			}
		}
	}
}
.icon-diamond {
	display: inline-block;
	height: 1.5rem;
	width: 1.5rem;
	background-image: url(~assets/images/WaterDisastersImage/diamond.gif);
}
.governance-rule {
	.governance-rule-list {
		height: 160px;
		ul {
			height: 100%;
			display: flex;
			flex-direction: column;
			justify-content: space-evenly;
			li {
				font-size: 15px;
				color: #ffffff;
				position: relative;
				&::before {
					content: " ";
					position: absolute;
					top: -10px;
					left: 0;
					width: 20px;
					height: 4px;
					background-color: var(--rule-list-color);
				}
			}
		}
	}
}
.governance-result {
	.governance-result__content {
		padding-top: 4px;
		height: 160px;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		.result-item {
			display: flex;
			flex: row;
			align-items: center;
			justify-content: space-between;
			height: 46px;
			background-image: linear-gradient(90deg, rgba(0, 193, 255, 0.2) 0%, rgba(249, 249, 249, 0) 100%);
			i {
				margin-right: 8px;
			}
			span {
				display: flex;
				align-items: center;
			}
			.unit {
				font-size: 20px;
				color: #1ef8f2;
				&::after {
					content: "个";
					margin-left: 4px;
					font-size: 14px;
					color: #ffffff;
				}
			}
		}
	}
}
.query-sty {
	padding: 15px 0;
	color: #fff;
	.derive-sty {
		padding: 0 10px;
		text-align: center;
		border-radius: 4px;
		line-height: 30px;
		float: right;
		color: #fff;
		cursor: pointer;
		border-color: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%) !important;
		background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
	}
	.aa {
		margin-right: 20px;
	}
	.demo-input-suffix {
		display: flex;
		justify-content: left;
		align-items: center;
	}
}
.mg-l16 {
	margin-left: 16px;
}
</style>
