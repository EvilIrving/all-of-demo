<template>
	<div class="data-service-card">
		<page-pane title="数据服务" @onTitle="() => {}">
			<template #append>
				<i class="icon-liebiao" @click="onDetailListClick" />
			</template>
			<table-title title="数据申请排名" />
			<ve-bar
				v-bind="requestRankOption"
				:tooltip-visible="false"
				:legend-visible="false"
				height="200px"
				:data="requestRankData"
				:loading="requestRankData.rows.length == 0"
			/>
			<table-title title="数据API请求" />
			<ve-histogram
				v-bind="requestDeptCountOption"
				:data="requestDeptCountData"
				:tooltip-visible="false"
				:legend-visible="false"
				:after-config="afterRequestCountConfig"
				:loading="requestDeptCountData.rows.length == 0"
				height="160px"
				width="100%"
			/>
			<table-title title="服务接口调用次数" />
			<CapsuleTabs v-model="requestCount" :tabs="['最新上线服务', '服务调用次数']" />
			<div v-if="requestCount == 0" class="service-online-latest">
				<el-table :data="latestData" :height="270" style="margin-top:10px">
					<el-table-column label="接口名称" prop="apiName"></el-table-column>
					<el-table-column label="类别" prop="apiCategory"></el-table-column>
				</el-table>
			</div>
			<div v-else-if="requestCount == 1" class="service-call-count">
				<ve-bar
					v-bind="requestCallOption"
					:data="requestCallData"
					:tooltip-visible="false"
					:legend-visible="false"
					height="280px"
				></ve-bar>
			</div>
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
				<p class="primary-dialog__title--label">
					详表
				</p>
			</div>
			<div class="primary--dialog__content">
				<div class="flex flex-row py-8">
					<div class="flex flex-row">
						<div>
							<i class="icon-data-service-stat"></i>
						</div>
						<div class="flex flex-col px-8">
							<div class="text-white text-16">调用次数</div>
							<div class="text-24 text-blue pt-2">
								24
								<span class="text-white text-16">次</span>
							</div>
						</div>
					</div>
					<div class="flex flex-row">
						<div>
							<i class="icon-data-service-stat"></i>
						</div>
						<div class="flex flex-col px-8">
							<div class="text-white text-16">调用最多单位</div>
							<div class="text-24 text-blue pt-2">
								水利中心
								<span class="text-white text-24">|</span>
								10次
							</div>
						</div>
					</div>
				</div>
				<div class="tab-list-sty">
					<el-table
						v-loading="loading"
						element-loading-text="拼命加载中"
						element-loading-spinner="el-icon-loading"
						element-loading-background="rgba(0, 0, 0, 0.8)"
						ref="travelTable"
						:data="tableData"
						:height="250"
						stripe
					>
						<el-table-column type="index" label="序号" width="55" />
						<el-table-column label="服务接口名称" prop="apiName" />
						<el-table-column label="创建时间" prop="createTIme" />
						<el-table-column label="申请数量" prop="applyNumber" />
						<el-table-column label="累计调用次数" prop="totalInvokeNumber" />
						<el-table-column label="今日调用次数" prop="todayInvokeNumber" />
					</el-table>
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import "@/style/v-charts/style.css";
import {
	applyRank,
	invokeStatistic,
	latestService,
	invokeNumber,
	getDataServiceList
} from "@/api/micro-service/data-management/";
import { TableTitle } from "./CardPages/components.js";
import { PagePane, PagePaneItem } from "../components/PagePane";
import { genBodyLinearGradients, generateSeries, generateDataZoom, genNormalLinearGradients } from "./utils/chart.js";
import CapsuleTabs from "../components/CapsuleTabs.vue";

export default {
	name: "DataServiceCard",
	components: { TableTitle, PagePane, PagePaneItem, CapsuleTabs },
	data() {
		return {
			requestRankOption: {
				grid: {
					top: -5,
					bottom: -20,
					right: 50,
					containLabel: true
				},
				settings: {
					itemStyle: {
						color: "#1ef8f2",
						barBorderRadius: 20
					},
					label: {
						show: true,
						fontSize: this.$commonSetting.chartFontSize,
						fontWeight: "bold",
						position: [240, "0%"],
						color: "#1ef8f2"
					}
				},
				extend: {
					xAxis: { show: false },
					yAxis: {
						axisLine: { show: false },
						axisLabel: {
							color: "#ffffff",
							fontWeight: 500,
							fontSize: this.$commonSetting.chartFontSize
						}
					},
					series: {
						showBackground: true,
						backgroundStyle: {
							barBorderRadius: 20
						},
						barWidth: 12
					}
				}
			},
			requestRankData: {
				columns: ["name", "data"],
				rows: [
					{ name: "水文中心", data: 256 },
					{ name: "数据中心", data: 171 },
					{ name: "运管中心", data: 89 },
					{ name: "规划中心", data: 31 },
					{ name: "监管中心", data: 17 }
				]
			},
			requestDeptCountOption: {
				grid: {
					top: 35,
					bottom: "2%",
					containLabel: true
				},
				settings: {
					yAxisName: ["次"],
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
			},
			requestDeptCountData: {
				columns: ["name", "data"],
				rows: [
					{ name: "05月", data: 9 },
					{ name: "06月", data: 8 },
					{ name: "07月", data: 7 },
					{ name: "08月", data: 7 }
				]
			},
			requestCount: 0,

			latestData: [],
			requestCallOption: {
				grid: {
					top: 20,
					bottom: -20,
					right: 50,
					containLabel: true
				},
				settings: {
					itemStyle: {
						color: "#1ef8f2",
						barBorderRadius: 20
					},
					label: {
						show: true,
						fontSize: this.$commonSetting.chartFontSize,
						fontWeight: "bold",
						position: [310, "0%"],
						color: "#1ef8f2"
					}
				},
				extend: {
					xAxis: { show: false },
					yAxis: {
						offset: 120,
						axisLine: { show: false },
						axisLabel: {
							inside: false,
							color: "#ffffff",
							fontWeight: 500,
							fontSize: this.$commonSetting.chartFontSize,
							margin: -120,
							verticalAlign: "bottom",
							lineHeight: 40,
							align: "left"
						}
					},
					series: {
						showBackground: true,
						backgroundStyle: {
							barBorderRadius: 20
						},
						barWidth: 12
					}
				}
			},
			requestCallData: {
				columns: ["name", "data"],
				rows: [
					{ name: "水文中心", data: 256 },
					{ name: "数据中心", data: 171 },
					{ name: "运管中心", data: 89 },
					{ name: "规划中心", data: 31 },
					{ name: "监管中心", data: 17 }
				]
			},

			// 详表弹窗

			visible: false,
			loading: false,
			tableData: []
		};
	},
	mounted() {
		this.getApplyRank();
		this.getInvokeStatistic();
		this.getLatestService();
		this.getInvokeNumber();
	},
	methods: {
		getApplyRank() {
			applyRank().then(res => {
				if (res.code == 0) {
					this.requestRankData.rows = res.data.map(item => ({
						name: item.applicant_department,
						data: item.apply_number
					}));
				}
			});
		},
		getInvokeStatistic() {
			invokeStatistic().then(res => {
				if (res.code == 0) {
					this.requestDeptCountData.rows = res.data.map(item => ({ name: `${item.month}月`, data: item.invoke_num }));
				}
			});
		},
		getLatestService() {
			latestService().then(res => {
				if (res.code == 0) {
					this.latestData = res.data;
				}
			});
		},
		getInvokeNumber() {
			invokeNumber().then(res => {
				if (res.code == 0) {
					this.requestCallData.rows = res.data.map(item => ({ name: item.api_name, data: item.num }));
				}
			});
		},
		getList() {
			this.loading = true;
			getDataServiceList().then(res => {
				this.loading = false;
				if (res.code == 0) {
					this.tableData = res.data.list;
				}
			});
		},
		onDetailListClick() {
			this.visible = true;
			this.getList();
		},
		afterRequestCountConfig(options) {
			let data = options.series[0].data;
			let color = genBodyLinearGradients("#2886c6", "#50bfda");
			options.series = generateSeries({ data, color: color });
			return options;
		}
	}
};
</script>

<style lang="scss" scoped>
.flex {
	display: flex;
}
.flex-row {
	flex-direction: row;
}
.flex-col {
	flex-direction: column;
}
.pt-2 {
	padding-top: 4px;
}
.py-8 {
	padding-top: 16px;
	padding-bottom: 16px;
}
.px-8 {
	padding-left: 16px;
	padding-right: 16px;
}
.mr-4 {
	margin-right: 8px;
}
.text-16 {
	font-size: 16px;
}
.text-24 {
	font-size: 24px;
}
.text-white {
	color: #fff;
}
.text-blue {
	color: #56fefe;
}
</style>
