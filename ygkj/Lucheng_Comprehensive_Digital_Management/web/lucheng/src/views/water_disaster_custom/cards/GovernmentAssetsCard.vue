<!--
 * @Autor: ysl
 * @Date: 2021-08-23 09:50:53
 * @LastEditors: ysl
 * @LastEditTime: 2021-09-27 15:17:42
 * @Description: 资产管理
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/governmentAssetsCard.vue
-->
<template>
	<div class="government-assets-card">
		<panel-item title="资产管理" has-switch tc @tclick="tclick('资产管理')">
			<template #title-switch>
				<span style="visibility: hidden">123123</span>
			</template>

			<div class="charts-panel">
				<!-- 资产总览 -->
				<sub-title-click-card :title="['资产总览']">
					<div slot="title-operate">
						<el-radio v-model="checkRadio" label="1">数量</el-radio>
						<el-radio v-model="checkRadio" label="2">金额</el-radio>
						<i
							v-show="!previewModule"
							title="查看详表"
							class="icon-liebiao icon-sty"
							@click.stop="openDetailsDialog('资产总览')"
							style="margin-left: 10px"
						/>
					</div>
					<div class="chart-panel more-panel">
						<div class="count-panel">
							<div class="count-item">
								<i class="icon-count icon-sybh-count-1"></i>
								<div class="text">
									<p>资产总数</p>
									<p class="num">{{ assetsObj.totalNum || 0 }} <span>件</span></p>
								</div>
							</div>
							<div class="count-item">
								<i class="icon-count icon-sybh-count-2"></i>
								<div class="text">
									<p>到期件数</p>
									<p class="num outNum">{{ assetsObj.expireNum || 0 }} <span>件</span></p>
								</div>
							</div>
							<div class="count-item">
								<i class="icon-count icon-sybh-count-3"></i>
								<div class="text">
									<p>资产总额</p>
									<p class="num">{{ assetsObj.totalAmount || 0 }} <span>万元</span></p>
								</div>
							</div>
						</div>
						<div class="pie-box">
							<div id="deptTypePie" style="width:50%;height:100%" ref="deptTypePie"></div>
							<div class="legend-panel bar_box">
								<div class="legend-item" v-for="(item, index) in deptType" :key="index">
									<div>
										<i :class="['icon-circle', 'icon-color-' + (index + 1)]" />
										<span class="text">{{ item.label }}</span>
									</div>
									<span style="font-size: 0.75rem; display:flex">
										<span class="num">{{ item.value || 0 }}</span>
										{{ unit }}
									</span>
								</div>
							</div>
						</div>
					</div>
				</sub-title-click-card>
				<!-- 报废 -->
				<sub-title-click-card :title="[`报废 ${scrapMaxValue} 件`]">
					<div slot="title-operate">
						<i
							v-show="!previewModule"
							title="查看详表"
							class="icon-liebiao icon-sty"
							@click.stop="openDetailsDialog('报废')"
							style="margin-left: 10px"
						/>
					</div>

					<div class="chart-panel">
						<div class="bar_box">
							<el-row v-for="(item, index) in scrapBarDataList" :key="index" :gutter="14" class="bar_line">
								<el-col :span="7" class="text">{{ item.deptName }}</el-col>
								<el-col :span="14">
									<el-progress
										:show-text="false"
										:stroke-width="8"
										:percentage="(item.value / scrapMaxValue) * 100"
										:color="'#E89100'"
									></el-progress>
								</el-col>
								<el-col :span="3"
									><span :style="{ color: '#E89100' }">{{ item.value }}</span
									>/{{ scrapMaxValue }}</el-col
								>
							</el-row>
						</div>
					</div>
				</sub-title-click-card>
				<!-- 领用 -->
				<sub-title-click-card :title="[`领用 ${collectMaxValue} 件`]">
					<div slot="title-operate">
						<i
							v-show="!previewModule"
							title="查看详表"
							class="icon-liebiao icon-sty"
							@click.stop="openDetailsDialog('领用')"
							style="margin-left: 10px"
						/>
					</div>

					<div class="chart-panel">
						<div class="bar_box">
							<el-row v-for="(item, index) in collectBarDataList" :key="index" :gutter="14" class="bar_line">
								<el-col :span="7" class="text">{{ item.deptName }}</el-col>
								<el-col :span="14">
									<el-progress
										:show-text="false"
										:stroke-width="8"
										:percentage="(item.value / collectMaxValue) * 100"
										:color="'#1df8f3'"
									></el-progress>
								</el-col>
								<el-col :span="3"
									><span :style="{ color: '#1df8f3' }">{{ item.value }}</span
									>/{{ collectMaxValue }}</el-col
								>
							</el-row>
						</div>
					</div>
				</sub-title-click-card>
			</div>
		</panel-item>
	</div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import SubTitleClickCard from "../components/SubTitleClickCard.vue";
import { oaApi } from "@/api/governmentOaApi";
import { generateDataZoom } from "./utils/chart.js";

import * as echarts from "echarts"; // echart插件

export default {
	name: "governmentAssetsCard",
	components: {
		PanelItem,
		SubTitleClickCard
	},
	computed: {
		...mapGetters(["previewModule"]),
		map() {
			return this.$store.state.screen.mapComponent;
		}
	},

	data() {
		return {
			activeTab: 0,

			monthOption: [],
			yearDropdownVisible: false,
			checkYear: "",
			checkMonth: "",
			barData1: {
				columns: ["deptName", "peopleNum"],
				rows: []
			},
			barData2: {
				columns: ["deptName", "totalCost"],
				rows: []
			},
			checkRadio: "1",
			scrapBarDataList: [],
			scrapMaxValue: 100,
			collectBarDataList: [],
			collectMaxValue: 100,
			assetsObj: {},
			unit: "件",
			deptType: [],
			totalLength: 0,
			colorList: [
				"#00EBC7",
				"#008FE8",
				"#19AF47",
				"#FF7D7D",
				"#89BA8B",
				"#E89100",
				"#FF7144",
				"#004FE3",
				"#A80BEE",
				"#8A8AE1",
				"#EE0B6B",
				"#28C9F6",
				"#FFEC36",
				"#2DDF3B",
				"#DBA380",
				"#FF1111",
				"#5836FF",
				"#B05EFF"
			]
		};
	},
	created() {},
	mounted() {
		this.getOaOverView(1);
		this.getOaOverView(2);
		this.getOaOverView(4);
		this.getScrapApplyStatsNumByDept();
		this.getCollectApplyAtatsNumByDept();
		this.deptTypePie();
	},
	watch: {
		checkRadio(val) {
			val == "1" ? this.getOaOverView(2) : this.getOaOverView(3);
		}
	},
	methods: {
		...mapActions(["SetDetailDialogParam", "SetGovernmentAssetsCount"]),
		// 获取资产管理数据
		getOaOverView(type) {
			let opt = {
				type: type // 1，统计资产总数，到期件数，资产总额；2，按部门总计资产件数；3，按部门总计资产总额；4，大屏C位统计
			};
			oaApi.stats4ScreenUnit(opt).then(res => {
				if (res.code == 0) {
					switch (type) {
						case 1:
							this.assetsObj = res.data;
							this.assetsObj.totalAmount = Number(this.assetsObj.totalAmount / 10000).toFixed(2);
							break;
						case 2:
							this.unit = "件";
							this.totalLength = 0;
							this.deptType = res.data.map(item => {
								item.label = item.key;
								this.totalLength += item.value;
								return item;
							});
							this.deptTypePie();
							break;
						case 3:
							this.unit = "万元";
							this.totalLength = 0;
							this.deptType = res.data.map(item => {
								item.value = Number(item.value).toFixed(2);
								item.label = item.key;
								this.totalLength += Number(item.value);
								return item;
							});
							this.totalLength = Number(this.totalLength).toFixed(2);
							this.deptTypePie();
							break;
						case 4:
							// 设置c位数据
							this.SetGovernmentAssetsCount({
								totalAmount: (Number(res.data.totalAmount) / 10000).toFixed(2),
								totalNum: res.data.totalNum,
								scrapNum: res.data.scrapNum,
								collectNum: res.data.collectNum,
								expireNum: res.data.expireNum
							});
							break;
					}
				}
			});
		},
		// 获取报废数据
		getScrapApplyStatsNumByDept() {
			oaApi.scrapApplyStatsNumByDept({}).then(res => {
				this.scrapBarDataList = res.data.list.map(item => {
					item.value = item.approveNum;
					return item;
				});
				this.scrapMaxValue = res.data.totalNum;
			});
		},
		// 获取领用数据
		getCollectApplyAtatsNumByDept() {
			oaApi.scrapApplyStatsNumByDept({}).then(res => {
				this.collectBarDataList = res.data.list.map(item => {
					item.value = item.approveNum;
					return item;
				});
				this.collectMaxValue = res.data.totalNum;
			});
		},

		/**
		 * 打开详情弹框点击事件
		 */
		openDetailsDialog(val) {
			this.$emit("openDialog", val);
		},
		// 标题点击打开统计卡片
		tclick(title) {
			this.$emit("tclick", title);
		},
		setColor(color) {
			return {
				type: "linear",
				x: 0,
				y: 0,
				x2: 1,
				y2: 1,
				colorStops: [
					{
						offset: 0,
						color: color // 0% 处的颜色
					},
					{
						offset: 1,
						color: "rgba(0,0,0)" // 100% 处的颜色
					}
				],
				global: false // 缺省为 false
			};
		},
		// 资产总览 饼图
		deptTypePie() {
			var chartDom = document.getElementById("deptTypePie");
			var myChart = echarts.init(chartDom);
			var option;
			let _this = this;
			let color = [];
			this.colorList.forEach((item, index) => {
				color.push(this.setColor(item));
			});
			option = {
				tooltip: {
					show: true,
					trigger: "item",
					formatter: function(data) {
						return (
							data.data.label +
							"<br/><span style='font-size: 20px;color: #1CFFF5;line-height: 26px;'>" +
							data.value +
							`</span> <span  style='font-size: 14px;'> ${_this.unit}</span>`
						);
					},
					confine: true,
					extraCssText: "width:160px;height:50px;font-size:18px;"
				},
				series: [
					{
						type: "pie",
						radius: ["50%", "80%"],
						centen: [0, 0],
						avoidLabelOverlap: false,
						label: {
							show: false,
							position: "center",
							normal: {
								// normal 是图形在默认状态下的样式
								show: true,
								position: "center",
								color: "#fff",
								fontSize: 16,
								formatter: this.totalLength + "\n" + _this.unit
							}
						},
						labelLine: {
							show: false
						},
						data: this.deptType,
						color: color
					}
				]
			};
			option && myChart.setOption(option);
		}
	}
};
</script>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.government-assets-card {
	width: 100%;
	height: 100%;
	.sub-title-card {
		height: auto;
	}
	::v-deep .el-radio {
		color: #fff;
		.el-radio__inner {
			background-color: transparent;
			&:hover {
				border-color: #fff;
			}
		}
		&.is-checked {
			.el-radio__inner {
				border-color: #15e2db;
				background: transparent;
				&::after {
					width: 0.4rem;
					height: 0.4rem;
					left: 45%;
					top: 45%;
					background-color: #15e2db;
				}
			}
			.el-radio__label {
				color: #15e2db;
			}
		}
	}
	.area-box,
	.is-water-box {
		width: 100%;
		height: calc(100% - 34px);
	}
	.bar_box {
		overflow: hidden auto;
		height: 100%;
		width: 100%;
		padding: 8px;
		.bar_line {
			margin-top: 0;
			width: 98%;
			.text {
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
		}
		&::-webkit-scrollbar-track-piece {
			background-color: transparent;
		}
		&::-webkit-scrollbar {
			width: 3px;
		}
		&::-webkit-scrollbar-thumb {
			background-color: #15e2db;
		}
	}
	.bar_line {
		width: 100%;
		margin: 0.8rem;
		display: flex;
		align-items: center;
		&:last-child {
			margin-bottom: 0;
		}
	}
	.is-water-box {
		display: flex;
	}
	.pie-box {
		width: 100%;
		height: calc(100% - 116px);
	}
	.pie-legend {
		display: flex;
		flex-direction: column;
		justify-content: center;
		width: 60%;
		height: 100%;
		font-size: 16px;
		padding-left: 30px;
		> div {
			display: flex;
			justify-content: space-between;
			line-height: 30px;

			&:last-child .legend-circle {
				background: #ff7144;
			}
			.legend-circle {
				display: inline-block;
				height: 14px;
				width: 14px;
				border-radius: 50%;
				background: #1cfff5;
				vertical-align: middle;
				margin-right: 10px;
			}
			.legend-num {
				font-size: 20px;
				color: #1cfff5;
				margin-right: 7px;
			}
		}
	}

	.el-dropdown {
		cursor: pointer;
		top: -2px;
		right: 10px;
		.el-dropdown-link {
			display: flex;
			align-items: center;
			&.dropdown-active {
				span,
				i {
					color: #1cfff5;
				}
			}
		}
	}
	.el-icon-caret-bottom {
		margin-left: 3px;
	}
	.count-panel {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
		.count-item {
			width: 50%;
			display: flex;
			align-items: center;
			margin: 5px 0 0;
			.text {
				font-size: 14px;
				margin-left: 10px;
				line-height: 25px;
				.num {
					font-size: 20px;
					color: #56fefe;
				}
				.outNum {
					color: #e89100;
				}
				span {
					color: #fff;
					font-size: 12px;
				}
			}
		}
	}

	.icon-count {
		display: inline-block;
		width: 48px;
		height: 48px;
		background-size: 100% 100%;
		&.icon-sybh-count-1 {
			background-image: url(~assets/images/assets_count_1.png);
		}
		&.icon-sybh-count-2 {
			background-image: url(~assets/images/assets_count_2.png);
		}
		&.icon-sybh-count-3 {
			background-image: url(~assets/images/assets_count_3.png);
		}
	}
}
#barChart {
	width: 100%;
	height: 100%;
}
</style>

<style lang="scss">
.el-dropdown-menu.year-dropdown {
	max-height: 200px;
	overflow: auto;
	background: rgba(2, 10, 28, 0.7);
	//   background: #061639;
	border: 1px solid #1cfff5;
	.el-dropdown-menu__item {
		line-height: 26px;
	}
	.el-radio {
		color: #fff;
	}
	.el-radio__input.is-checked .el-radio__inner {
		border-color: #1cfff5;
		background: transparent;
		&::after {
			width: 8px;
			height: 8px;
			background: #1cfff5;
		}
	}
	.el-radio__input.is-checked + .el-radio__label {
		color: #1cfff5;
	}
	.el-radio__inner {
		background: transparent;
		border: 1px solid #fff;
	}
}
.el-dropdown-menu.year-dropdown.el-popper[x-placement^="bottom"] .popper__arrow::after {
	border-bottom-color: rgba(2, 10, 28, 0.7);
}
</style>

<style lang="scss">
.government-assets-card .panel-item .panel-item__content--tabs .tab-button,
.government-assets-card .panel-item .panel-item__title--tabs .tab-button {
	display: inline-block;
	font-size: 16px;
	padding: 0 12px;
}
.government-assets-card .panel-item .panel-item__content--tabs .tab-button.active,
.government-assets-card .panel-item .panel-item__title--tabs .tab-button.active {
	padding: 0 12px;
}
</style>

<style lang="scss" scoped>
.icon-sty {
	cursor: pointer;
	margin-right: 10px;
}
.icon-circle {
	width: 12px !important;
	height: 12px !important;
	border-radius: 50%;
	&.icon-color-1 {
		@include chart-icon();
		background: #00ebc7;
	}
	&.icon-color-2 {
		@include chart-icon();
		background: #008fe8;
	}
	&.icon-color-3 {
		@include chart-icon();
		background: #19af47;
	}
	&.icon-color-4 {
		@include chart-icon();
		background: #ff7d7d;
	}
	&.icon-color-5 {
		@include chart-icon();
		background: #89ba8b;
	}
	&.icon-color-6 {
		@include chart-icon();
		background: #e89100;
	}
	&.icon-color-7 {
		@include chart-icon();
		background: #ff7144;
	}
	&.icon-color-8 {
		@include chart-icon();
		background: #004fe3;
	}
	&.icon-color-9 {
		@include chart-icon();
		background: #a80bee;
	}
	&.icon-color-10 {
		@include chart-icon();
		background: #8a8ae1;
	}
	&.icon-color-11 {
		@include chart-icon();
		background: #ee0b6b;
	}
	&.icon-color-12 {
		@include chart-icon();
		background: #28c9f6;
	}
	&.icon-color-13 {
		@include chart-icon();
		background: #ffec36;
	}
	&.icon-color-14 {
		@include chart-icon();
		background: #2ddf3b;
	}
	&.icon-color-15 {
		@include chart-icon();
		background: #dba380;
	}
	&.icon-color-16 {
		@include chart-icon();
		background: #ff1111;
	}
	&.icon-color-17 {
		@include chart-icon();
		background: #5836ff;
	}
	&.icon-color-18 {
		@include chart-icon();
		background: #b05eff;
	}
}
.chart-panel {
	height: 150px;
	overflow: hidden;
	position: relative;
	&.more-panel {
		height: 300px;
	}
}
.pie-box {
	display: flex;
	justify-content: space-around;
	align-items: center;
	width: 100%;
	.legend-panel {
		width: 45%;
		height: 100%;
		overflow-y: auto;
		.legend-item {
			line-height: 25px;
			display: flex;
			justify-content: space-between;
			> div {
				width: 50%;
				display: flex;
				align-items: center;
				overflow: hidden;
				text-overflow: ellipsis;
			}
			.text {
				font-size: 12px;
				// margin-right: 8px;
				width: 80%;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
				height: 1.5rem;
				line-height: 1.5rem;
				// word-break: keep-all;
			}
			.num {
				color: #1cfff5;
				font-size: 12px;
			}
		}
	}
}
</style>
