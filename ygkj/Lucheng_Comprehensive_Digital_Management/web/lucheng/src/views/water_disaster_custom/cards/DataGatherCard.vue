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
					<el-radio-group v-model="checked">
						<el-radio label="水情" @click.native.prevent="onPointChange('水情')"></el-radio>
						<el-radio label="雨情" @click.native.prevent="onPointChange('雨情')"></el-radio>
						<el-radio label="工情" @click.native.prevent="onPointChange('工情')"></el-radio>
						<el-radio label="视频" @click.native.prevent="onPointChange('视频')"></el-radio>
					</el-radio-group>
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
				<i class="icon-liebiao" @click="detailVisible = true"></i>
			</template>
			<TableTitle title="分类统计" />
			<CapsuleTabs v-model="categoryType" :tabs="['全部', '在线', '离线']" @change="getClassificationStat" />
			<ve-ring
				height="250px"
				v-bind="categoryOption"
				:data="categoryData"
				:tooltip-visible="false"
				:after-config="afterCategoryConfig"
				:loading="categoryData.rows.length == 0"
			/>
			<TableTitle title="在线率统计" />
			<CapsuleTabs v-model="onlineStatType" :tabs="['水情', '雨情', '工情', '视频']" @change="getOnlineRateStat" />
			<div class="online-rate-stat">
				<div class="online-rate-stat__box" style="--var-gradient-start: #042142;--var-gradient-start-bd: #00bef8;">
					<div class="box-content">
						<p>
							{{ ["水情", "雨情", "工情", "视频"][onlineStatType] }}站点总数<span>{{ onlineData.total }}</span
							>个
						</p>
					</div>
				</div>
				<div class="online-rate-stat__box" style="--var-gradient-start: #0c5b67;--var-gradient-start-bd: #1bfcf2;">
					<div class="box-content">
						<p>当前在线总数</p>
						<p>
							<span>{{ onlineData.onlineNumber }}</span
							>个
						</p>
					</div>
				</div>
				<div class="online-rate-stat__box" style="--var-gradient-start: #2b2f17;--var-gradient-start-bd: #f2c301;">
					<div class="box-content">
						<p>当前离线数量</p>
						<p>
							<span>{{ onlineData.offlineNumber }}</span
							>个
						</p>
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
		<DataGatherDetail :visible.sync="detailVisible" />
	</div>
</template>

<script>
import "@/style/v-charts/style.css";
import { dataGatherClassification, dataGatherOnlineRate, getWaterStations } from "@/api/micro-service/graph";
import { selectCamera } from "@/api/micro-service/project-manage";
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
import DataGatherDetail from "./dialog/DataGatherDetail.vue";

export default {
	name: "DataGatherCard",
	layerName: "dataGatherLayer",
	legendName: ["水情", "雨情", "工情", "视频"],
	components: { PagePane, TableTitle, CapsuleTabs, DataGatherDetail },
	data() {
		const wrappedNumber = number => `<span style="color:#1cfff5;font-size:18px;margin:0 3px">${number}</span>`;

		return {
			checked: "",
			detailVisible: false,
			dropdownVisible: false,

			categoryType: 0,
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
				rows: []
			},
			onlineStatType: 0,
			onlineData: { offlineNumber: 0, onlineNumber: 0, total: 0 },
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
				}
			},
			onlineStatData: {
				columns: ["name", "data", "online", "offline"],
				rows: []
			}
		};
	},
	computed: {
		map() {
			return this.$store.state.screen.mapComponent;
		}
	},
	mounted() {
		this.getClassificationStat();
		this.getOnlineRateStat();
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
			if (this.checked == value) {
				this.checked = "";
			} else {
				this.checked = value;
			}

			this.clearPoint();
			this.clearLegend();
			if (this.checked !== "") this.getPointData(this.checked);
		},
		/**
		 * 清除图层
		 */
		clearPoint() {
			this.map.drawClusterPoint([], {}, "dataGatherLayer");
		},
		/**
		 * 从不同的接口获取点位数据
		 * @param {'雨情'|'水情'|'工情'|'视频'} type
		 */
		getPointData(type) {
			this.addLegend(type);
			if (type == "视频") {
				const onlineSrc = require("@/assets/images/MapPoint/video-online.png");
				const onlineSrcs = require("@/assets/images/MapPoint/group/video-online.png");
				const offlineSrc = require("@/assets/images/MapPoint/video-offline.png");
				const offlineSrcs = require("@/assets/images/MapPoint/group/video-offline.png");

				selectCamera().then(res => {
					if (res.code == 0) {
						const pointList = res.data.map(point => ({
							name: point.cameraName,
							type: "视频",
							lng: Number(point.longitude),
							lat: Number(point.latitude),
							code: point.cameraIndexCode,
							previewUrl: point.previewUrl || "",
							cameraIndexCode: point.cameraIndexCode,
							cameraSource: point.cameraSource || "",
							stationCode: point.cameraIndexCode || "",
							src: point.status == 1 ? onlineSrc : offlineSrc,
							srcs: point.status == 1 ? onlineSrcs : offlineSrcs,
							props: {
								状态: point.status == 1 ? "在线" : "离线"
							}
						}));
						this.map.drawClusterPoint(
							pointList,
							{ scale: 0.7, anchor: [27, 30], anchorXUnits: "pixels" },
							"dataGatherLayer"
						);
					}
				});
			} else if (type == "雨情") {
				getWaterStations({ stationType: "PP" }).then(res => {
					if (res.code == 0) {
						const onlineSrc = require("@/assets/images/MapPoint/video-online.png");
						const onlineSrcs = require("@/assets/images/MapPoint/group/video-online.png");
						const offlineSrc = require("@/assets/images/MapPoint/video-offline.png");
						const offlineSrcs = require("@/assets/images/MapPoint/group/video-offline.png");
						const pointList = res.data.map(point => ({
							name: point.stName,
							type: "雨量",
							lng: point.stLong,
							lat: point.stLat,
							code: point.stCode,
							src: point.online ? onlineSrc : offlineSrc,
							srcs: point.online ? onlineSrcs : offlineSrcs,
							props: {
								状态: point.online ? "在线" : "离线"
							}
						}));
						this.map.drawClusterPoint(
							pointList,
							{ scale: 0.7, anchor: [27, 30], anchorXUnits: "pixels" },
							"dataGatherLayer"
						);
					}
				});
			} else if (type == "水情") {
				getWaterStations({ stationType: "RR,ZZ" }).then(res => {
					if (res.code == 0) {
						const onlineSrc = require("@/assets/images/MapPoint/video-online.png");
						const onlineSrcs = require("@/assets/images/MapPoint/group/video-online.png");
						const offlineSrc = require("@/assets/images/MapPoint/video-offline.png");
						const offlineSrcs = require("@/assets/images/MapPoint/group/video-offline.png");
						const pointList = res.data.map(point => ({
							name: point.stName,
							type: { RR: "水库", ZZ: "河道" }[point.stationType],
							lng: point.stLong,
							lat: point.stLat,
							code: point.stCode,
							src: point.online ? onlineSrc : offlineSrc,
							srcs: point.online ? onlineSrcs : offlineSrcs,
							props: {
								状态: point.online ? "在线" : "离线"
							}
						}));
						this.map.drawClusterPoint(
							pointList,
							{ scale: 0.7, anchor: [27, 30], anchorXUnits: "pixels" },
							"dataGatherLayer"
						);
					}
				});
			} else if (type == "工情") {
				getWaterStations({ stationType: "DD" }).then(res => {
					if (res.code == 0) {
						const onlineSrc = require("@/assets/images/MapPoint/video-online.png");
						const onlineSrcs = require("@/assets/images/MapPoint/group/video-online.png");
						const offlineSrc = require("@/assets/images/MapPoint/video-offline.png");
						const offlineSrcs = require("@/assets/images/MapPoint/group/video-offline.png");
						const pointList = res.data.map(point => ({
							name: point.stName,
							type: "闸站",
							lng: point.stLong,
							lat: point.stLat,
							code: point.stCode,
							src: point.online ? onlineSrc : offlineSrc,
							srcs: point.online ? onlineSrcs : offlineSrcs,
							props: {
								状态: point.online ? "在线" : "离线"
							}
						}));
						this.map.drawClusterPoint(
							pointList,
							{ scale: 0.7, anchor: [27, 30], anchorXUnits: "pixels" },
							"dataGatherLayer"
						);
					}
				});
			}
		},
		/**
		 * 分类统计
		 */
		getClassificationStat() {
			const { categoryType } = this;
			this.categoryData.rows = [];
			dataGatherClassification({ type: ["全部", "在线", "离线"][categoryType] }).then(res => {
				if (res.code == 0) {
					this.categoryData.rows = res.data.map(item => ({ name: item.type, data: item.num }));
				}
			});
		},
		/**
		 * 在线率统计
		 */
		getOnlineRateStat() {
			const { onlineStatType } = this;
			this.onlineStatData.rows = [];
			dataGatherOnlineRate({ type: ["水情", "雨情", "工情", "视频"][onlineStatType] }).then(res => {
				if (res.code == 0) {
					/** @typedef {{month:string;onlineNumber:number;offlineNumber:number}[]} ChartData */
					const {
						chart = [],
						offlineNumber = 0,
						onlineNumber = 0,
						total = 0
					} = /** @type {{chart:ChartData;offlineNumber:number,onlineNumber:number;total:number}} */ (res.data);
					this.onlineStatData.rows = chart.map(item => ({
						name: `${item.month.split("-")[1]}月`,
						online: item.onlineNumber,
						offline: item.offlineNumber,
						data: Math.floor(item.rate * 100)
					}));
					this.onlineData = { offlineNumber, onlineNumber, total };
				}
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
	.el-radio,
	.el-radio__label {
		line-height: 26px;
		font-size: 14px;
	}
	.el-radio__label {
		color: #1cfff5;
	}
	.el-radio__inner {
		background: transparent;
		border: 1px solid #1cfff5;
	}
	.el-radio__inner::after {
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
