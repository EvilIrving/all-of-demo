<template>
	<div class="data-duty-card">
		<page-pane title="数据责任" @onTitle="() => {}">
			<template #append>
				<i class="icon-liebiao" @click="visible = true" />
			</template>
			<table-title title="一数一责" />
			<div class="responsible-stat">
				<div
					class="responsible-stat__box"
					style="
            --var-gradient-start: #062744;
            --var-gradient-start-bd: #01baf1;
          "
				>
					<div class="box-content">
						<p>明确负责人</p>
						<p><span>80</span>张</p>
					</div>
				</div>
				<div
					class="responsible-stat__box"
					style="
            --var-gradient-start: #0c5b67;
            --var-gradient-start-bd: #1bfcf2;
          "
				>
					<div class="box-content">
						<p>未明确负责人</p>
						<p><span>80</span>张</p>
					</div>
				</div>
			</div>
			<div class="responsible-stat--chart">
				<ve-histogram
					v-bind="responsibleStatOption"
					ref="chart"
					height="160px"
					width="100%"
					:data="responsibleStatData"
					:tooltip-visible="false"
					:legend-visible="false"
					:after-config="afterResponsibleStatConfig"
					:loading="responsibleStatData.rows.length == 0"
				/>
			</div>

			<table-title title="责任人分布" />
			<div class="custom-legend-chart">
				<ve-ring
					width="160px"
					height="160px"
					v-bind="respDistributedOption"
					:data="respDistributedData"
					:tooltip-visible="false"
					:legend-visible="false"
					:after-config="afterRespDistributedConfig"
					:loading="respDistributedData.rows.length == 0"
				/>
				<div class="custom-legend" style="height: 160px">
					<ul class="list">
						<li v-for="(item, index) in respDistributedData.rows" :key="index" class="list-item">
							<i class="dot" :style="{ backgroundColor: respDistributedColor[index] }" />

							<div class="legend-unit">
								{{ item.name }}

								<span class="unit-data">
									<span>{{ item.data }}</span>
									人
								</span>
							</div>
						</li>
					</ul>
				</div>
			</div>
			<table-title title="数据责任分布" />
			<div class="custom-legend-chart">
				<ve-ring
					width="160px"
					height="160px"
					v-bind="resDeptOption"
					:data="resDeptData"
					:tooltip-visible="false"
					:legend-visible="false"
					:after-config="afterRespDistributedConfig"
					:loading="resDeptData.rows.length == 0"
				/>
				<div class="custom-legend" style="height: 160px">
					<ul class="list">
						<li v-for="(item, index) in resDeptData.rows" :key="index" class="list-item">
							<i
								class="dot"
								:style="{
									backgroundColor:
										respDistributedColor[
											index >= respDistributedColor.length ? index % respDistributedColor.length : index
										]
								}"
							/>

							<div class="legend-unit">
								{{ item.name }}
								<span class="unit-data">
									<span>{{ item.data }}</span>
									张
								</span>
							</div>
						</li>
					</ul>
				</div>
			</div>
			<ve-histogram
				v-bind="resDeptCountOption"
				ref="chart"
				height="160px"
				width="100%"
				:data="resDeptCountData"
				:tooltip-visible="false"
				:legend-visible="false"
				:after-config="afterResDeptCountDataConfig"
				:loading="resDeptCountData.rows.length == 0"
			/>
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
							<el-table-column label="表责任人" prop="dutyUser" />
							<el-table-column label="所在部门" prop="dutyDepartment" />
							<el-table-column label="是否多源">
								<template #default="{ row }">
									{{ row.dsCount > 0 ? "是" : "否" }}
								</template>
							</el-table-column>
							<el-table-column label="来源">
								<template #default="{ row }">
									{{ row.latestDsInfo ? row.latestDsInfo.dsName : "-" }}
								</template>
							</el-table-column>
							<el-table-column label="维护方式">
								<template #default="{ row }">
									{{ row.latestDsInfo ? (row.latestDsInfo.auto ? "自动" : "手动") : "-" }}
								</template>
							</el-table-column>
							<el-table-column label="最新更新时间">
								<template #default="{ row }">
									{{ row.latestDsInfo ? row.latestDsInfo.createTime : "-" }}
								</template>
							</el-table-column>
						</el-table>
						<el-pagination
							layout="prev, pager, next"
							:total="total"
							:page-size="30"
							:current-page.sync="pageNum"
							@current-change="getList"
						/>
					</div>
				</div>
			</el-dialog>
		</page-pane>
	</div>
</template>

<script>
import "@/style/v-charts/style.css";
import {
	getDataAndDuty,
	getDetaiList,
	getDutyDataDistribute,
	getDutyManDistribute
} from "@/api/micro-service/data-management/";
import { TableTitle } from "./CardPages/components.js";
import { PagePane, PagePaneItem } from "../components/PagePane";
import {
	genBodyLinearGradients,
	generateSeries,
	generateDataZoom,
	wrapFormatter,
	genNormalLinearGradients
} from "./utils/chart.js";

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
	name: "DataDutyCard",
	components: { PagePane, PagePaneItem, TableTitle },
	data() {
		return {
			responsibleStatOption: {
				grid: {
					top: 20,
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
							fontSize: this.$commonSetting.chartFontSize,
							formatter: v => wrapFormatter(v, 6)
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
			responsibleStatData: {
				columns: ["name", "data"],
				rows: [
					{ name: "张三", data: 399 },
					{ name: "李四", data: 399 },
					{ name: "王五", data: 1 },
					{ name: "刘六", data: 1 }
				]
			},

			respDistributedColor: [],
			respDistributedOption: RingOptions,
			respDistributedData: {
				columns: ["name", "data"],
				rows: [
					{ name: "水文中心", data: 128 },
					{ name: "数据中心", data: 60 },
					{ name: "运管中心", data: 79 },
					{ name: "规划中心", data: 17 },
					{ name: "监管中心", data: 29 }
				]
			},
			resDeptOption: RingOptions,
			resDeptData: {
				columns: ["name", "data"],
				rows: [
					{ name: "水利工程", data: 20 },
					{ name: "水文监测数据", data: 30 }
				]
			},

			resDeptCountOption: {
				grid: {
					top: 20,
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
							fontSize: this.$commonSetting.chartFontSize,
							formatter: v => wrapFormatter(v, 6)
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
			resDeptCountData: {
				columns: ["name", "data"],
				rows: [
					{ name: "水文中心", data: 16 },
					{ name: "数据中心", data: 3 },
					{ name: "运管中心", data: 4 },
					{ name: "规划中心", data: 33 },
					{ name: "监管中心", data: 75 }
				]
			},
			dataAndDuty: {
				tableDutyedCount: 0,
				tableUnDutyCount: 0
			},
			visible: false,
			loading: false,
			tableData: [],
			total: 0,
			pageNum: 0
		};
	},
	mounted() {
		this.getList();
		this.getDataAndDuty();
		this.getDutyManDistribute();
		this.getDutyDataDistribute();
	},
	methods: {
		getList() {
			this.loading = true;
			getDetaiList({ pageSize: 30, pageNum: this.pageNum }).then(res => {
				this.loading = false;
				if (res.code == 0) {
					this.tableData = res.data.list;
					this.total = res.data.totalRows;
				}
			});
		},
		afterResponsibleStatConfig(options) {
			let data = options.series[0].data;
			let color = genBodyLinearGradients("#2886c6", "#50bfda");
			options.series = generateSeries({ data, color: color });
			// options.dataZoom = generateDataZoom(4, data.length);
			return options;
		},
		afterResDeptCountDataConfig(options) {
			let data = options.series[0].data;
			let color = genBodyLinearGradients("#2886c6", "#50bfda");
			options.series = generateSeries({ data, color: color });
			options.dataZoom = generateDataZoom(5, data.length);
			return options;
		},
		afterRespDistributedConfig(options) {
			this.respDistributedColor = options.color;
			let data = options.series[0].data;
			let count = 0;
			if (data.length > 0) count = data.map(i => i.value).reduce((total, cur) => total + cur);
			options.title = {
				show: true,
				text: `{count|${count}}个\n总数`,
				textStyle: { color: "#fff", rich: { count: { fontSize: 22 } } },
				textAlign: "center",
				left: 75,
				top: "35%"
			};
			options.color = options.color.map(item => {
				return genNormalLinearGradients(item, "#000");
			});
			return options;
		},
		getDataAndDuty() {
			getDataAndDuty().then(res => {
				if (res.code == 0) {
					const { tableDutyedCount, tableUnDutyCount, dutyManStatistical } = res.data;
					this.dataAndDuty = { tableDutyedCount, tableUnDutyCount };
					this.responsibleStatData.rows = dutyManStatistical.map(item => ({
						data: item.tableCnt,
						name: item.username
					}));
				}
			});
		},
		getDutyManDistribute() {
			getDutyManDistribute().then(res => {
				if (res.code == 0) {
					this.respDistributedData.rows = res.data.dutyManDistributeByDept.map(item => ({
						name: item.deptName,
						data: item.userCount
					}));
				}
			});
		},
		getDutyDataDistribute() {
			getDutyDataDistribute().then(res => {
				if (res.code == 0) {
					this.resDeptCountData.rows = res.data.dutyDataDistributeByDept.map(item => ({
						name: item.deptName,
						data: item.tableCnt
					}));
					this.resDeptData.rows = res.data.dutyDataDistributeByCategory.map(item => ({
						name: item.category_name,
						data: item.categoryCnt
					}));
				}
			});
		}
	}
};
</script>

<style lang="scss" scoped>
.data-duty-card {
	.responsible-stat {
		display: flex;
		justify-content: space-between;
		--var-gradient-end: #030a1722;
		.responsible-stat__box {
			width: 176px;
			height: 68px;

			position: relative;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			.box-content {
				z-index: 2;
				> p {
					font-size: 15px;
					color: #ffffff;
					text-align: center;
					line-height: 24px;
					padding: 0 4px;
					> span {
						font-size: 24px;
						color: #1cfff5;
						text-align: right;
						line-height: 24px;
						padding: 0 4px;
					}
				}
			}

			&::after {
				content: "";
				position: absolute;
				width: 100%;
				height: 100%;
				background-image: linear-gradient(
					90deg,
					var(--var-gradient-start-bd) 0%,
					var(--var-gradient-end) 40%,
					var(--var-gradient-end) 60%,
					var(--var-gradient-start-bd) 100%
				);
				z-index: 0;
			}
			&::before {
				content: "";
				height: calc(100% - 4px);
				width: calc(100% - 4px);
				left: 2px;
				top: 2px;
				position: absolute;
				background-image: linear-gradient(
					90deg,
					var(--var-gradient-start) 0%,
					var(--var-gradient-end) 40%,
					var(--var-gradient-end) 60%,
					var(--var-gradient-start) 100%
				);
				z-index: 1;
			}
		}
	}
	.responsible-stat--chart {
		margin-top: 4px;
	}
	.custom-legend-chart {
		display: flex;
		flex-direction: row;
		.custom-legend {
			width: 210px;
			height: 100%;
			overflow-y: scroll;
			align-items: center;
			&::-webkit-scrollbar-thumb:vertical {
				background-color: #1ef8f2;
			}
			.list {
				width: 100%;
				padding-left: 8px;

				.list-item {
					line-height: 30px;
					font-size: 14px;
					color: #fff;
					font-weight: 500;
					position: relative;
					padding-left: 20px;
					overflow: hidden;
					.dot {
						content: "";
						position: absolute;
						left: 0;
						top: 8px;
						width: 12px;
						height: 12px;
						border-radius: 50%;
					}

					.unit-data {
						display: block;
						float: right;
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
</style>
<style lang="scss">
.detail-table {
	.el-pagination {
		.el-pager {
			li {
				color: #fff;
				background: transparent;
				&.active {
					color: #409eff;
				}
			}
		}
	}
}
</style>
