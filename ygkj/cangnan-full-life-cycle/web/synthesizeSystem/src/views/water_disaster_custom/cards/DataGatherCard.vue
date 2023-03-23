<template>
	<div class="data-gather-card">
		<page-pane title="数据汇聚">
			<template #append>
				<el-popover
					placement="bottom-start"
					width="80"
					trigger="hover"
					@show="dropdownVisible = true"
					@hide="dropdownVisible = false"
					popper-class="selection-popper"
				>
					<el-checkbox-group v-model="checked" @change="onPointChange">
						<el-checkbox label="水情"></el-checkbox>
						<el-checkbox label="雨情"></el-checkbox>
						<el-checkbox label="工情"></el-checkbox>
						<el-checkbox label="视频"></el-checkbox>
					</el-checkbox-group>
					<i
						class="dropdown-wrap"
						style="margin-right:8px"
						:class="dropdownVisible && 'dropdown-active'"
						slot="reference"
					>
						<i :class="dropdownVisible ? 'icon-yinzhang-active' : 'icon-yinzhang-1'" />
						<i class="el-icon-caret-bottom" />
					</i>
				</el-popover>
				<i class="icon-liebiao"></i>
			</template>
			<TableTitle title="分类统计" />
			<CapsuleTabs :tabs="['总数', '在线', '离线']" />
			<ve-ring
				height="250px"
				v-bind="categoryOption"
				:data="categoryData"
				:tooltip-visible="false"
				:after-config="afterCategoryConfig"
				:loading="categoryData.rows.length == 0"
			/>
			<TableTitle title="在线率统计">
				<template #append>
					<el-select v-model="year">
						<el-option label="2021" :value="2021"></el-option>
					</el-select>
				</template>
			</TableTitle>
			<CapsuleTabs :tabs="['水情', '雨情', '工情', '视频']" />
			<div class="online-rate-stat">
				<div class="online-rate-stat__box" style="--var-gradient-start: #042142;--var-gradient-start-bd: #00bef8;">
					<div class="box-content">
						<p>水情站点总数<span>556</span>个</p>
					</div>
				</div>
				<div class="online-rate-stat__box" style="--var-gradient-start: #0c5b67;--var-gradient-start-bd: #1bfcf2;">
					<div class="box-content">
						<p>当前站点总数</p>
						<p><span>256</span>个</p>
					</div>
				</div>
				<div class="online-rate-stat__box" style="--var-gradient-start: #2b2f17;--var-gradient-start-bd: #f2c301;">
					<div class="box-content">
						<p>当前离线数量</p>
						<p><span>16</span>个</p>
					</div>
				</div>
			</div>
			<ve-histogram
				v-bind="onlineStatOption"
				ref="chart"
				height="250px"
				width="100%"
				:data="onlineStatData"
				:legend-visible="false"
				:after-config="afterOnlineStatConfig"
				:loading="onlineStatData.rows.length == 0"
			/>
		</page-pane>
	</div>
</template>

<script>
import "v-charts/lib/style.css";
import { TableTitle } from "./CardPages/components.js";
import { PagePane } from "../components/PagePane";
import CapsuleTabs from "../components/CapsuleTabs.vue";
import {
	genBodyLinearGradients,
	generateSeries,
	generateDataZoom,
	wrapFormatter,
	genNormalLinearGradients
} from "./utils/chart";
export default {
	name: "DataGatherCard",
	components: { PagePane, TableTitle, CapsuleTabs },
	data() {
		const wrappedNumber = number => `<span style="color:#1cfff5;font-size:18px;margin:0 3px">${number}</span>`;

		return {
			checked: [],
			dropdownVisible: false,
			categoryOption: {
				grid: {
					top: 40,
					bottom: "2%",
					containLabel: true
				},
				settings: { radius: [45, 75] },
				extend: {
					"textStyle.color": "#fff",
					series: {
						center: ["50%", "35%"],
						hoverAnimation: false,
						label: { show: false }
					}
				},
				legend: {
					textStyle: { color: "#fff" },
					bottom: "0%",
					selectedMode: false,
					icon: "circle"
				}
			},
			categoryData: {
				columns: ["name", "data"],
				rows: [
					{ name: "水情", data: 60 },
					{ name: "雨情", data: 79 },
					{ name: "工情", data: 17 },
					{ name: "视频", data: 29 }
				]
			},
			year: 2021,
			onlineStatOption: {
				grid: {
					top: "15%",
					bottom: "2%",
					containLabel: true
				},
				tooltip: {
					formatter: p => {
						const data = this.onlineStatData.rows[p.dataIndex];
						const { online, offline } = data;
						return "在线数量:" + wrappedNumber(online) + "<br/>" + "离线数量:" + wrappedNumber(offline);
					}
				},
				settings: {
					itemStyle: { color: "#fff" },
					label: {
						show: true,
						fontSize: this.$commonSetting.chartFontSize,
						fontWeight: "bold",
						position: "top",
						color: "#fff"
					},
					yAxisName: ["%"]
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
						},
						max: 100
					}
				},
				log: true
			},
			onlineStatData: {
				columns: ["name", "data", "online", "offline"],
				rows: [
					{ name: "5月", data: 16, online: 95, offline: 23 },
					{ name: "6月", data: 3, online: 95, offline: 23 },
					{ name: "7月", data: 4, online: 95, offline: 23 },
					{ name: "8月", data: 33, online: 95, offline: 23 },
					{ name: "9月", data: 75, online: 95, offline: 23 }
				]
			}
		};
	},
	methods: {
		afterCategoryConfig(options) {
			let data = options.series[0].data;
			let count = 0;
			if (data.length > 0) count = data.map(i => i.value).reduce((total, cur) => total + cur);
			options.title = {
				show: true,
				text: `{count|${count}}个\n总数`,
				textStyle: { color: "#fff", rich: { count: { fontSize: 22 } } },
				textAlign: "center",
				left: "48%",
				top: "25%"
			};
			options.color = options.color.map(item => {
				return genNormalLinearGradients(item, "#000");
			});
			return options;
		},
		afterOnlineStatConfig(options) {
			let data = options.series[0].data;
			let color = genBodyLinearGradients("#2886c6", "#50bfda");
			options.series = generateSeries({ data, color: color }, true, "在线率");
			options.dataZoom = generateDataZoom(5, data.length);
			return options;
		},
		onPointChange(value) {
			this.clearLegend();
			value.forEach(type => {
				this.addLegend(type);
				// 落点
			});
		},
		clearLegend() {
			this.$emit("legend-change", "水情", {});
			this.$emit("legend-change", "雨情", {});
			this.$emit("legend-change", "工情", {});
			this.$emit("legend-change", "视频", {});
		},
		addLegend(type) {
			const legendMap = {
				水情: {
					label: "水情",
					children: [
						{ label: "在线", icon: "icon-two-legend" },
						{ label: "离线", icon: "icon-four-legend" }
					]
				},
				雨情: {
					label: "雨情",
					children: [
						{ label: "在线", icon: "icon-two-legend" },
						{ label: "离线", icon: "icon-four-legend" }
					]
				},
				工情: {
					label: "工情",
					children: [
						{ label: "在线", icon: "icon-two-legend" },
						{ label: "离线", icon: "icon-four-legend" }
					]
				},
				视频: {
					label: "视频",
					children: [
						{ label: "在线", icon: "icon-two-legend" },
						{ label: "离线", icon: "icon-four-legend" }
					]
				}
			};
			this.$emit("legend-change", type, legendMap[type]);
		}
	}
};
</script>

<style lang="scss">
.data-gather-card {
	.dropdown-wrap {
		cursor: pointer;
		&.dropdown-active {
			i {
				color: #1cfff5;
			}
		}
	}
	.dropdown-wrap i {
		vertical-align: middle;
	}
	.table-title__append {
		.el-select {
			width: 90px;
			.el-input__inner {
				background-color: #0000;
				height: 32px;
			}
			.el-input__suffix {
				.el-input__icon {
					line-height: 1;
				}
			}
		}
	}

	.online-rate-stat {
		margin-top: 8px;
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		--var-gradient-end: #0003;
		.online-rate-stat__box {
			position: relative;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			width: 49%;
			height: 68px;
			margin-bottom: 8px;
			&:first-child {
				width: 100%;
				height: 34px;
			}

			.box-content {
				position: absolute;
				width: 100%;
				z-index: 2;
				> p {
					font-size: 15px;
					color: #ffffff;
					text-align: center;
					line-height: 24px;
					padding: 4px 4px;
					> span {
						font-size: 24px;
						color: #1cfff5;
						text-align: right;
						line-height: 24px;
						padding: 4px 4px;
					}
				}
			}
			&::after {
				content: "";
				position: absolute;
				width: 100%;
				height: 100%;
				top: 0;
				left: 0;
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
}
.el-popper[x-placement^="bottom"] .popper__arrow {
	border-bottom-color: #1cfff5;
}
.el-popover.selection-popper {
	background: rgba(2, 10, 28, 0.7);
	border: 1px solid #1cfff5;
	.el-checkbox,
	.el-checkbox__label {
		line-height: 26px;
		font-size: 14px;
	}
	.el-checkbox__inner {
		background: transparent;
		border: 1px solid #1cfff5;
	}
	.el-checkbox__inner::after {
		border: 1px solid #1cfff5;
		border-left: 0;
		border-top: 0;
	}
}
.selection-popper.el-popper[x-placement^="bottom"] .popper__arrow {
	border-bottom-color: #1cfff5 !important;
	&::after {
		border-bottom-color: rgba(2, 10, 28, 0.7);
	}
}
</style>
