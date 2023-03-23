<template>
	<div class="dike-card">
		<panel-item
			title="堤防"
			has-switch
			tc
			@tclick="tclick"
			:action-tabs="['按规模统计', '按行政区划']"
			@change="handleTabChange"
		>
			<template slot="label-description"></template>

			<template slot="title-switch">
				<i
					title="地图落点"
					:class="[showBtn ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
					@click="handleStagnantWater()"
				/>
				<i
					title="查看详表"
					class="icon-liebiao icon-sty"
					style="margin-left: 10px"
					@click="openDetailsDialog('堤防')"
				/>
				<i
          v-show="!previewModule"
          @click="goToAmap('堤防')"
					style="margin-left: 10px"
          title="一张图"
          class="icon-yizhangtu icon-sty"
        />
			</template>
			<!-- 第一个tab -->
			<div class="area-box" v-if="activeTab == 0">
				<!-- 堤防 -->
				<div class="rain-forecast-card">
					<div class="rainfall-style">
						<p style="margin-top: -10px;">
							堤防总数 <span> {{ alldikenum }}</span> 条
						</p>
					</div>
				</div>
				<!-- 堤防数据 -->
				<card-tabss
					v-model="dikeCardSelect"
					v-loading="dikeCardLoading"
					element-loading-background="rgba(0, 0, 0, 0.8)"
					:list="dikeCardList"
					@change="dikePreCardClick"
				></card-tabss>
			</div>
			<!-- 第二个tab -->
			<div class="is-water-box" style="margin-top:5px" v-if="activeTab == 1">
				<ve-histogram
					ref="chart"
					height="100%"
					width="100%"
					:data="barData"
					:settings="barOption.setting"
					:extend="barOption.extend"
					:grid="barOption.grid"
					:tooltip-visible="false"
					:legend-visible="false"
					:after-config="barConfig"
				/>
				<!-- 
          :data-zoom="barOption.dataZoom" -->
			</div>

			<!-- </div> -->
		</panel-item>
	</div>
</template>
<script>
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import CardTabss from "../../WaterDisasterPreventionScreen/components/CardTabss.vue";
import EchartPie from "../../WaterDisasterPreventionScreen/charts/echart_pie/EchartPie.vue"; // 积水饼图
import { generateDataZoom } from "./utils/chart.js";
import { goAmap } from "./utils/common";
export default {
	layerName: "dikeLayers",
	name: "dikeCard",
	legendName: "堤防",
	components: {
		CardTabss,
		PanelItem,
		EchartPie
	},

	data() {
		return {
			//堤防列表
			dikeStList: [],

			showBtn: false, // 落点
			dike2: "",
			dike4: "",
			dike5: "",
			alldikenum: "",
			barData: {
				columns: ["name", "data"],
				rows: []
			},

			//堤防行政区划图表
			barOption: {
				grid: {
					top: 40,
					bottom: "5%",
					containLabel: true
				},
				setting: {
					yAxisName: ["条"],
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
					barWidth: 10,
					xAxis: {
						axisLabel: {
							interval: 0,
							fontSize: this.$commonSetting.chartFontSize,
							formatter: function(value) {
								var ret = "";
								var maxLength = 5; //每项显示文字个数
								var valLength = value.length; //X轴类目项的文字个数
								var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
								if (rowN > 1) {
									for (var i = 0; i < rowN; i++) {
										var temp = ""; //每次截取的字符串
										var start = i * maxLength; //开始截取的位置
										var end = start + maxLength; //结束截取的位置
										temp = value.substring(start, end) + "\n";
										ret += temp; //凭借最终的字符串
									}
									return ret;
								} else {
									return value;
								}
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
			activeTab: 0,
			dikeCardSelect: -1,

			dikeCardLoading: false,
			timer: null
		};
	},
	mounted() {
		this.$nextTick(() => {
			this.getDikeLoggingList(); //地图点位获取
			this.dikeproject(); //tab1堤防规模数量统计
			this.getstatisticalProjectNum(); //tab1堤防数量统计
			this.getstatisticalDiskNumByArea(); //tab2堤防行政区划数量统计
			this.timer = setInterval(() => {
				this.getDikeLoggingList(); //地图点位获取
				this.getstatisticalDiskNumByArea();
				this.getstatisticalProjectNum();
				this.dikeproject(); //tab1堤防规模数量统计
			}, 300000);
		});
	},
	beforeDestroy() {
		clearInterval(this.timer);
	},
	computed: {
		...mapGetters(["ScreenMap"]),
		/** 堤防规模数量卡片数据 */
		dikeCardList() {
			return [
				{
					label: "2级堤防",
					data: { number: this.dike2, unit: "条" }
				},
				{
					label: "4级堤防",
					data: { number: this.dike4, unit: "条" }
				},
				{
					label: "5级堤防",
					data: { number: this.dike5, unit: "条" }
				}
			];
		}
	},
	methods: {
		...mapActions(["SetDikeCount"]),
		goToAmap(type){
      goAmap(type);
    },
		// 单级堤防数据
		dikePreCardClick(val) {
			console.log(val);
		},
		// 获取点位
		async getDikeLoggingList() {
			let res = await disasterPreventionApi.dikeproject();
			this.dikeStList = res.data.list;
		},
		// 地图点位
		handleStagnantWater() {
			this.showBtn = !this.showBtn;
			if (this.showBtn) {
				this.ScreenMap.drawCoorpotLine(
					this.dikeStList,
					"dike",
					data => ({
						name: data.dikeName,
						stationCode: data.stCode || "",
						props: {
							行政区划: `${data.countryAdnm || "-"}`,
							"堤防长(m)": `${data.dikeLen || "-"}`,
							堤防规模: `${data.dikeGrad || "-"}级堤防`
						}
					}),
					data => {
						if (data.dikeGrad == 2) {
							return { lineWidth: 4, lineColor: "#f40005" };
						} else if (data.dikeGrad == 4) {
							return { lineWidth: 4, lineColor: "#ffda17" };
						} else if (data.dikeGrad == 5) {
							return { lineWidth: 4, lineColor: "#4fff00" };
						}
					}
				);
				this.handleLegendChange();
			} else {
				this.ScreenMap.drawCoorpotLine([], "dike");
				this.handleLegendChange(false);
			}
		},
		// echarts

		barConfig(options) {
			let data = options.series[0].data;
			let colorArr = [];
			colorArr = ["#15E2DB", "#9DFDFB", "#A6FFFF"];
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
			let barWidth = 15;
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
						fontSize: 14,
						position: "top",
						color: "#1EF8F2"
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
			options.dataZoom = generateDataZoom(4, this.barData.rows.length);
			return options;
		},
		//工程总数量统计
		async getstatisticalProjectNum() {
			await disasterPreventionApi.getstatisticalProjectNum({ type: "堤防" }).then(res => {
				if (res.code == 0) {
					this.alldikenum = res.data;
				}
			});
		},
		//工程规模数量统计--2级堤防
		async dikeproject() {
			await disasterPreventionApi.dikeproject({ dikeDrad: "2" }).then(res => {
				if (res.code == 0) {
					this.dike2 = res.data.list.length;
				}
			});
			//工程规模数量统计--四级堤防
			await disasterPreventionApi.dikeproject({ dikeDrad: "4" }).then(res => {
				if (res.code == 0) {
					this.dike4 = res.data.list.length;
				}
			});
			//工程规模数量统计--五级堤防
			await disasterPreventionApi.dikeproject({ dikeDrad: "5" }).then(res => {
				if (res.code == 0) {
					this.dike5 = res.data.list.length;
				}
			});
		},
		//堤防规模数量统计// 按行政区划统计
		async getstatisticalDiskNumByArea() {
			await disasterPreventionApi.getstatisticalDiskNumByArea({ type: "堤防" }).then(res => {
				if (res.code == 0) {
					let rows = [];
					for (let item of res.data) {
						rows.push({ name: item.adnm, data: item.num });
					}
					this.barData.rows = rows;
					this.activeTab == 1 ? this.$refs["chart"].echarts.resize() : "";
				}
			});
		},

		/**
		 * 打开详情弹框点击事件
		 */
		openDetailsDialog(val) {
			this.$emit("openDialog");
		},
		// 标题点击打开统计卡片
		tclick(title) {
			this.$emit("tclick", title);
		},
		handleTabChange(val) {
			this.activeTab = val;
		},
		handleLegendChange(state = true) {
			if (state) {
				this.$emit("legend-change", "堤防规模", {
					label: "堤防规模",
					children: [
						{
							label: "2级堤防",
							icon: "icon-two-legend"
						},
						{
							label: "4级堤防",
							icon: "icon-four-legend"
						},
						{
							label: "5级堤防",
							icon: "icon-five-legend"
						}
					]
				});
			} else {
				this.$emit("legend-change", "堤防规模", {});
			}
		}
	}
};
</script>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.dike-card {
	width: 100%;
	height: 100%;
	.area-box,
	.is-water-box {
		width: 100%;
		height: calc(100% - 3.425rem);
	}
	.is-water-box {
		display: flex;
	}
	.pie-box {
		width: 100%;
		height: calc(100% - 24px);
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
.dike-card .panel-item .panel-item__content--tabs .tab-button,
.dike-card .panel-item .panel-item__title--tabs .tab-button {
	display: inline-block;
	font-size: 16px;
	padding: 0 12px;
}
.dike-card .panel-item .panel-item__content--tabs .tab-button.active,
.dike-card .panel-item .panel-item__title--tabs .tab-button.active {
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
}
.icon-color-1 {
	@include chart-icon();
	background: #15e2db;
}
.icon-color-2 {
	@include chart-icon();
	background: #008fe8;
}
.chart-panel {
	height: 200px;
}
.pie-box {
	display: flex;
	justify-content: space-around;
	align-items: center;
	width: 100%;
	.legend-panel {
		width: 40%;
		.legend-item {
			line-height: 25px;
			display: flex;
			justify-content: space-between;
			.text {
				font-size: 14px;
				margin-right: 10px;
			}
			.num {
				color: #1cfff5;
				font-size: 1rem;
			}
		}
	}
}
</style>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.rain-forecast-card {
	height: 100%;
	padding: 10 16px;
	.rainfall-style {
		@include flexbox;
		@include flexJC(center);
		margin-top: 5px;
		height: 18%;
	}
	::v-deep .panel-item {
		.panel-item__content {
			@include flexbox;
			@include flexflow(column, nowrap);
			@include flexJC(space-around);
		}
	}
}
</style>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.dialog-content {
	padding: 8px 0 16px;
}
.primary-dialog-tabs {
	padding: 8px 0;
	span {
		border: 1px solid #ffffff;
		color: #fff;
		padding: 4px 16px;
		opacity: 0.6;
		margin-right: 16px;
		cursor: pointer;
		&.active {
			border: 1px solid #56fefe;
			color: #56fefe;
		}
	}
}
.content-box {
	padding-top: 8px;
	.search-row {
		padding-top: 12px;
		span {
			display: inline-block;
			width: 80px;
			text-align: right;
			padding-right: 12px;
			color: #fff;
		}
	}
}
.rain-card {
	height: 100%;
	// padding: 0 16px;
	.dialog-content {
		padding: 8px 0 16px;
	}
	.primary-dialog-tabs {
		padding: 8px 0;
		span {
			border: 1px solid #ffffff;
			color: #fff;
			padding: 4px 16px;
			opacity: 0.6;
			margin-right: 16px;
			cursor: pointer;
			&.active {
				border: 1px solid #56fefe;
				color: #56fefe;
			}
		}
	}
	.content-box {
		padding-top: 8px;
		.search-row {
			padding-top: 12px;
			span {
				display: inline-block;
				width: 80px;
				text-align: right;
				padding-right: 12px;
				color: #fff;
			}
		}
	}

	::v-deep .panel-item__title--switch {
		i {
			cursor: pointer;
		}
	}
	.typhoon-search {
		width: 360px;
		height: 100%;
		@include flexbox;
		@include flexflow(column, nowrap);
		> p {
			padding: 8px 0 12px 0;
			color: #fff;
		}
		.search-row {
			margin-bottom: 12px;
			line-height: 44px;
			.search-label {
				color: #fff;
				height: 100%;
				@include flexbox;
				@include flexAC;
				@include flexJC(center);
			}
		}
		.primary-btn {
			color: #fff;
			margin: 6px 0;
		}
		.typhoon-list {
			height: 270px;
		}
	}

	.prediction-sty {
		text-align: center;
		color: #ffffff;
		padding-top: 15px;
		padding-bottom: 15px;
		.border-sty {
			border-left: 1px solid rgba(35, 140, 239, 0.4);
			border-right: 1px solid rgba(35, 140, 239, 0.4);
		}
		.rainfall-sty {
			font-size: 20px;
			color: #1cfff5;
		}
	}

	.areal-rainfall-sty {
		display: flex;
		justify-content: space-between; /* 横向中间自动空间 */
		> div:nth-child(1) {
			vertical-align: middle;
			> i {
				vertical-align: middle;
			}
			> span {
				color: #fff;
			}
		}
		> div:nth-child(2) {
			line-height: 21px;
			.tab-switch-sty {
				padding: 2px 10px;
				border-radius: 15px;
				background: #00c1ff;
			}
			> span {
				color: #fff;
				margin-left: 15px;
				font-size: 15px;
			}
		}
	}

	.early-warning-sty {
		font-size: 20px;
		font-weight: bold;
		padding: 15px 20px;
		background-image: linear-gradient(to right, rgba(117, 124, 52, 0.3), rgba(0, 0, 0, 0));
		> p {
			color: #eec80b;
		}
		> p:nth-child(2) {
			margin-top: 16px;
			> span {
				color: #eec80b;
				> span {
					color: #fff;
					font-size: 14px;
				}
			}
		}
	}
	.early-warning-style {
		font-size: 20px;
		font-weight: bold;
		padding: 15px 20px;
		background-image: linear-gradient(to right, rgba(192, 110, 63, 0.3), rgba(0, 0, 0, 0));
		> p {
			color: #ff7144;
		}
		> p:nth-child(2) {
			margin-top: 16px;
			> span {
				color: #ff7144;

				> span {
					color: #fff;
					font-size: 14px;
				}
			}
		}
	}

	.switch-list {
		::v-deep.el-carousel--horizontal {
			overflow-x: hidden;
			overflow-y: hidden;
		}
	}

	.sidebar-list {
		.warning-style {
			font-size: 16px;
			color: #ffffff;
			line-height: 24px;
			font-weight: 400;
		}
		::v-deep.el-carousel--horizontal {
			overflow-x: hidden;
			overflow-y: hidden;
			height: unset;
		}
	}
}
</style>
<style lang="scss">
.rain-card .v-charts-component-loading {
	background: transparent;
}
.rain-card .station-list {
	display: flex;
	.statistic {
		width: 50%;
		margin-right: 16px;
		margin-top: 8px;
		padding: 8px 0;
		color: #fff;
		font-size: 18px;
		display: flex;
		justify-content: center;
		align-items: baseline;
		background: url(~assets/images/WaterDisastersImage/rainfall-bg.png) no-repeat top center;
		background-size: 100% 100%;
		.number {
			margin: 0 3px;
			font-size: 28px;
			color: #1ef8f2;
			line-height: 24px;
		}
		&:last-child {
			margin-right: 0;
		}
	}
}
</style>
