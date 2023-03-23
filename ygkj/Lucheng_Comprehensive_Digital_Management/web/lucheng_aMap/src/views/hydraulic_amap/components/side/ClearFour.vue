<template>
	<div class="project-side" ref="stationSide">
		<div class="statistics-chart" ref="statisticsChart">
			<!-- 切换 -->
			<div style="margin: 5px 0;height: 45px;">
				<span :class="selectedVal == '条件选择' ? 'selected-tab-sty' : 'tab-sty'" @click="selected('条件选择')"
					>条件选择</span
				>
				<span :class="selectedVal == '统计' ? 'selected-tab-sty' : 'tab-sty'" @click="selected('统计')">统计</span>
			</div>
			<!-- 条件选择 -->
			<div style="margin: 5px 0;" v-if="selectedVal == '条件选择'">
				<div style="margin-bottom:15px;">
					<span class="title-sty">四乱类型：</span>
					<el-select style="height:" v-model="fourType" placeholder="四乱类型" @change="getList" clearable>
						<el-option v-for="item in fourOptions" :key="item.label" :label="item.label" :value="item.label" />
					</el-select>
				</div>
				<div style="margin-bottom:15px;">
					<span class="title-sty">严重程度：</span>
					<el-select style="height:" v-model="severity" placeholder="四乱类型" @change="getList" clearable>
						<el-option v-for="item in orderSeverity" :key="item.label" :label="item.label" :value="item.label" />
					</el-select>
				</div>
				<div style="margin-bottom:15px;">
					<span class="title-sty">问题状态：</span>
					<el-select style="height:" v-model="status" placeholder="四乱类型" @change="getList" clearable>
						<el-option v-for="item in checkOptions" :key="item.value" :label="item.label" :value="item.value" />
					</el-select>
				</div>
				<div>
					<span class="title-sty">上传时间：</span>
					<el-date-picker
						v-model="uploadTime"
						type="year"
						placeholder="监测年份"
						@change="getList"
						value-format="yyyy-MM-dd"
						format="yyyy-MM-dd"
					/>
				</div>
			</div>
			<!-- 统计图表 -->
			<div style="margin: 5px 0;" v-if="selectedVal == '统计'">
				<ve-histogram
					ref="chart"
					height="300px"
					width="100%"
					:data="barData"
					:settings="barOption.setting"
					:extend="barOption.extend"
					:grid="barOption.grid"
					:tooltip-visible="false"
					:legend-visible="false"
					:after-config="barConfig"
					:data-zoom="barOption.dataZoom"
				/>
			</div>
		</div>
		<!-- 四乱问题列表 -->
		<div class="statistics-list" ref="statisticsList" :style="listStyle">
			<i class="split-line icon-split-line"></i>
			<div class="search-area" ref="searchArea">
				<span class="amount-sty">四乱总数<br />{{ dataList.length }}</span>
				<div class="left-area">
					<el-input v-model="problemDescription" placeholder="请输入关键字查询" @input="getList">
						<template slot="prepend">
							<el-button slot="append" icon="el-icon-search" />
						</template>
					</el-input>
				</div>
				<div class="right-area" @click="lookDetail">
					<i class="icon-liebiao-detail" />
				</div>
			</div>
			<!-- 简表 -->
			<div class="table-list" :style="tableListStyle">
				<screen-table :columnList="columnList" :dataList="dataList" :tableHeight="tableHeight" :loading="tbLoading" />
			</div>
			<!-- 详表弹窗 -->
			<primary-dialog
				mainTitle="清四乱信息详表"
				:visible="detailDialog"
				@handleClose="handleDetailClose"
				width="50%"
				height="600"
				:hasAnalysis="false"
			>
				<div class="dialog-content scope-content">
					<RiversDetailDialog v-if="detailDialog" :projType="'清四乱'" />
				</div>
			</primary-dialog>
		</div>
	</div>
</template>

<script>
import { ScreenTable, PrimaryDialog } from "@/components/";
import RiversDetailDialog from "../dialog/RiversDetailDialog";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";

export default {
	name: "ClearFour",
	components: {
		ScreenTable,
		PrimaryDialog,
		RiversDetailDialog
	},
	data() {
		return {
			detailDialog: false,
			fourType: "",
			severity: "",
			selectedVal: "条件选择",
			uploadTime: "",
			tableHeight: "",
			barOption: {
				dataZoom: {
					type: "inside",
					startValue: 0,
					endValue: 4
				},
				grid: {
					top: 40,
					bottom: "1%",
					containLabel: true
				},
				setting: {
					yAxisName: ["个"],
					itemStyle: { color: "#fff" },
					label: {
						show: true,
						fontSize: 16,
						fontWeight: "bold",
						position: "top",
						color: "#fff"
					}
				},
				extend: {
					"textStyle.color": "#fff",
					barWidth: 15,
					legend: {
						show: true,
						right: 0,
						top: 10,
						icon: "circle",
						itemWidth: 8,
						itemHeight: 8,
						data: [
							{
								name: "问题数量",
								icon: "circle",
								textStyle: {
									color: "#F2A600" // 图例文字颜色
								}
							},
							{
								name: "整改数量",
								icon: "circle",
								textStyle: {
									color: "#1CFFF5" // 图例文字颜色
								}
							}
						]
					},
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
			},
			fourOptions: [
				{
					label: "全部"
				},
				{
					label: "乱占"
				},
				{
					label: "乱采"
				},
				{
					label: "乱堆"
				},
				{
					label: "乱建"
				},
				{
					label: "其他"
				}
			],
			orderSeverity: [
				{
					label: "全部"
				},
				{
					label: "轻微"
				},
				{
					label: "中度"
				},
				{
					label: "重大"
				}
			],
			checkOptions: [
				{
					label: "未处理",
					value: 0
				},
				{
					label: "已处理",
					value: 1
				},
				{
					label: "逾期未处理",
					value: 2
				},
				{
					label: "驳回",
					value: 3
				}
			],
			status: "",
			problemDescription: "",
			columnList: [
				{
					prop: "problemDescription",
					label: "四乱名称"
				},
				{
					prop: "fourType",
					label: "四乱类型"
				},
				{
					prop: "severity",
					label: "严重程度"
				},
				{
					prop: "status",
					label: "问题状态"
				}
			],
			dataList: [],
			barData: {
				columns: ["name", "total", "online", "outline"],
				rows: []
			},
			tableListStyle: {}, //表格样式
			listStyle: {}, //表格父容器样式
			//结束
			chartData: {
				columns: ["key", "value"],
				rows: []
			},
			tbLoading: false
		};
	},
	mounted() {
		this.$nextTick(() => {
			this.getList();
		});
	},
	computed: {
		mapObj() {
			return this.$store.state.hydraulicAmap.mapComponent;
		}
	},
	methods: {
		barConfig(options) {
			const onlineData = options.series[1].data;
			const outlineData = options.series[2].data;
			let colorArr2 = ["#15ddd7", "#7dfefa", "#9dfffc"];
			let colorArr = ["#F2A600", "#E8B80D", "#C29400"];

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
			let color2 = {
				type: "linear",
				x: 0,
				x2: 1,
				y: 0,
				y2: 0,
				colorStops: [
					{
						offset: 0,
						color: colorArr2[0]
					},
					{
						offset: 0.5,
						color: colorArr2[0]
					},
					{
						offset: 0.5,
						color: colorArr2[1]
					},
					{
						offset: 1,
						color: colorArr2[1]
					}
				]
			};
			let barWidth = 20;
			let constData = [];
			let showData = [];
			// 在线柱状图
			constData = onlineData.map(item => (item ? 1 : 0));
			showData = onlineData.map(item =>
				item
					? item
					: {
							value: 1,
							itemStyle: {
								normal: {
									borderColor: "rgba(0,0,0,0)",
									borderWidth: 2,
									color: "rgba(0,0,0,0)"
								}
							}
					  }
			);
			const onlineSeries = [
				{
					z: 1,
					name: "问题数量",
					type: "bar",
					barWidth: barWidth,
					barGap: "0%",
					data: onlineData,
					itemStyle: {
						normal: {
							color: color
						}
					},
					label: {
						show: true,
						fontSize: 18,
						position: "top",
						color: "#F2A600"
					}
				},
				{
					z: 2,
					name: "问题数量",
					type: "pictorialBar",
					data: constData,
					symbol: "diamond",
					symbolOffset: [(-1 * barWidth) / 2, "50%"],
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
					name: "问题数量",
					type: "pictorialBar",
					symbolPosition: "end",
					data: showData,
					symbol: "diamond",
					symbolOffset: [(-1 * barWidth) / 2, "-50%"],
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
			// 离线柱状图
			constData = outlineData.map(item => (item ? 1 : 0));
			showData = outlineData.map(item =>
				item
					? item
					: {
							value: 1,
							itemStyle: {
								normal: {
									borderColor: "rgba(0,0,0,0)",
									borderWidth: 2,
									color: "rgba(0,0,0,0)"
								}
							}
					  }
			);
			const outlineSeries = [
				{
					z: 1,
					name: "整改数量",
					type: "bar",
					barWidth: barWidth,
					barGap: "0%",
					data: outlineData,
					itemStyle: {
						normal: {
							color: color2
						}
					},
					label: {
						show: true,
						fontSize: 18,
						position: "top",
						color: "#1CFFF5"
					}
				},
				{
					z: 2,
					name: "整改数量",
					type: "pictorialBar",
					data: constData,
					symbol: "diamond",
					symbolOffset: [barWidth / 2, "50%"],
					symbolSize: [barWidth, 10],
					itemStyle: {
						normal: {
							color: color2
						}
					},
					tooltip: {
						show: false
					}
				},
				{
					z: 3,
					name: "整改数量",
					type: "pictorialBar",
					symbolPosition: "end",
					data: showData,
					symbol: "diamond",
					symbolOffset: [barWidth / 2, "-50%"],
					symbolSize: [barWidth - 4, (10 * (barWidth - 4)) / barWidth],
					itemStyle: {
						normal: {
							borderColor: color2[2],
							borderWidth: 2,
							color: color2[2]
						}
					},
					tooltip: {
						show: false
					}
				}
			];
			options.series = [...onlineSeries, ...outlineSeries];
			return options;
		},
		// 统计图表
		async statsOfWaterResearch() {
			await hydraulicApi.statisProblemByArea().then(res => {
				console.log(res);
				if (res.code == 0) {
					let rows = [];
					for (let index in res.data) {
						rows.push({
							name: res.data[index].county,
							// total: res.data[index].problemNum,
							online: res.data[index].problemNum,
							outline: res.data[index].reviewStatusNum
						});
					}
					this.barData.rows = rows;
				}
			});
		},
		//查看详表
		lookDetail() {
			this.detailDialog = true;
		},
		//详表弹窗关闭
		handleDetailClose() {
			this.detailDialog = false;
		},
		// 选中
		selected(val) {
			this.selectedVal = val;
			if (val == "统计") {
				this.statsOfWaterResearch();
			}
		},
		// 获取列表
		async getList() {
			this.tbLoading = true;
			this.dataList = [];
			const opt = {
				fourType: this.fourType,
				severity: this.severity,
				status: this.status,
				uploadTime: this.uploadTime,
				problemDescription: this.problemDescription
			};
			await hydraulicApi.WsPatrolProblem(opt).then(res => {
				if (res.code == 0) {
					let list = res.data.list;
					list.map(val => {
						if (val.status == "0") {
							val.status = "未处理";
						} else if (val.status == "1") {
							val.status = "已处理";
						} else if (val.status == "2") {
							val.status = "逾期未处理";
						} else if (val.status == "3") {
							val.status = "驳回";
						}
						return val;
					});
					this.dataList = list;
				}
				this.tbLoading = false;
				this.drawPoint();
			});
		},
		drawPoint() {
			let style1 = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				srcs: require("@/assets/images/qsl-legend-ju-1.png"),
				src: require("@/assets/images/qsl-legend-1.png")
			};
			let style2 = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				srcs: require("@/assets/images/qsl-legend-ju-2.png"),
				src: require("@/assets/images/qsl-legend-2.png")
			};
			let style3 = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				srcs: require("@/assets/images/qsl-legend-ju-3.png"),
				src: require("@/assets/images/qsl-legend-3.png")
			};
			let style4 = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				srcs: require("@/assets/images/qsl-legend-ju-4.png"),
				src: require("@/assets/images/qsl-legend-4.png")
			};
			let style5 = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				srcs: require("@/assets/images/qsl-legend-ju-4.png"),
				src: require("@/assets/images/qsl-legend-4.png")
			};
			let pointList1 = [];
			let pointList2 = [];
			let pointList3 = [];
			let pointList4 = [];
			let pointList5 = [];
			for (let point of this.dataList) {
				let obj = {
					name: point.address,
					type: "清四乱",
					lng: Number(point.lng),
					lat: Number(point.lat),
					props: {
						四乱类型: `${point.fourType || "-"}`,
						严重程度: `${point.severity || "-"}`,
						更新时间: `${point.createTime || "-"}`
					},
					obj: point
				};
				if (point.fourType == "乱占") {
					pointList1.push(obj);
				} else if (point.fourType == "乱采") {
					pointList2.push(obj);
				} else if (point.fourType == "乱堆") {
					pointList3.push(obj);
				} else if (point.fourType == "乱建") {
					pointList4.push(obj);
				} else if (point.fourType == "其他") {
					pointList5.push(obj);
				}
			}
			this.mapObj._drawClusterPoint(pointList1, style1, "qslLayer1");
			this.mapObj._drawClusterPoint(pointList2, style2, "qslLayer2");
			this.mapObj._drawClusterPoint(pointList3, style3, "qslLayer3");
			this.mapObj._drawClusterPoint(pointList4, style4, "qslLayer4");
			this.mapObj._drawClusterPoint(pointList5, style5, "qslLayer5");
		},
		//计算表格高度
		calcTableHeight() {
			this.$nextTick(() => {
				setTimeout(() => {
					this.tableHeight =
						this.$refs.statisticsList.offsetHeight -
						this.$refs.catalogTitle.offsetHeight -
						this.$refs.searchArea.offsetHeight;
					this.tableListStyle = {
						height: this.tableHeight + "px"
					};
				}, 100);
			});
		}
	}
};
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
.project-side {
	width: 100%;
	height: 100%;
	@include flexbox;
	@include flexflow(column, nowrap);
	.tab-sty {
		font-size: 14px;
		color: #ffffff;
		text-align: center;
		line-height: 16px;
		margin-right: 10px;
		padding: 9px 14px;
		font-weight: 400;
		opacity: 0.6;
		border: 1px solid #ffffff;
	}
	.title-sty {
		font-size: 16px;
		color: #ffffff;
		font-weight: 400;
	}
	.selected-tab-sty {
		margin-right: 10px;
		padding: 10px 15px;
		font-size: 14px;
		color: #56fefe;
		text-align: center;
		line-height: 16px;
		font-weight: 400;
		border: 1 solid #56fefe;
		box-shadow: inset 0 0 5px 0 #56fefe;
	}
	.amount-sty {
		background: rgba(72, 152, 251, 0.1);
		border: 0 solid #56fefe;
		box-shadow: inset 0 0 5px 0 #56fefe;
		padding: 10px;
		font-size: 16px;
		color: #56fefe;
		text-align: center;
		line-height: 18px;
		font-weight: 400;
	}
	// .statistics-chart {
	//   position: relative;
	//   flex: 1;
	//   .title-right {
	//     position: absolute;
	//     right: 0;
	//     .icon-btn-reset {
	//       cursor: pointer;
	//       height: 30px;
	//     }
	//   }
	//   .chart-tabs {
	//     margin-top: 10px;
	//     @include flexbox;
	//     @include flexAC;
	//     span {
	//       display: inline-block;
	//       padding: 4px 8px;
	//       width: 80px;
	//       // height: 28px;
	//       color: #4898fb;
	//       border: 1px solid #4898fb;
	//       text-align: center;
	//       line-height: 16px;
	//       font-size: 12px;
	//       margin-right: 16px;
	//       cursor: pointer;
	//       &.active {
	//         color: #a6e9fb;
	//         background: rgba(72, 152, 251, 0.1);
	//         border: 1px solid #23e3fb;
	//       }
	//     }
	//   }
	//   .chart-box {
	//     width: 100%;
	//     height: 240px;
	//   }
	// }
	.statistics-list {
		@include flex(2);
		.table-list {
			height: 100%;
			width: 100%;
		}
	}
}
</style>
<style lang="scss">
.custom-filter {
	.type-name {
		font-family: PingFangSC-Regular;
		font-size: 14px;
		color: #ffffff;
		font-weight: 400;
	}
	.filter-time {
		height: 48px;
		display: flex;
		align-items: center;
		> * {
			margin: 0 3px;
		}
		.el-button {
			margin-left: 16px;
		}
	}
	.el-radio {
		margin: 8px 0;
		width: 90px;
		.el-radio__inner {
			background-color: transparent;
			border: 1px solid #4694f6;
		}
		.el-radio__input.is-checked {
			.el-radio__inner {
				background: transparent;
				&:after {
					background-color: #1ef8f2;
					border-color: #1ef8f2;
					transform: translate(-50%, -50%) scale(1.8);
				}
			}
		}
		.el-radio__label {
			color: #fff;
		}
	}
	.el-date-editor {
		width: 139px;
		.el-input__prefix {
			color: #3f9bfc;
			left: unset;
			right: 8px;
			.el-input__icon {
				line-height: 1;
			}
		}
		.el-input__suffix {
			.el-input__icon {
				line-height: 1;
			}
		}
		.el-input__inner {
			padding-left: 8px;
			width: 139px;
			height: 32px;
			border: 1px solid #3f9bfc;
			border-radius: 2px;
		}
		&.el-date-editor--time-select {
			width: 88px;
			.el-input__inner {
				width: 88px;
			}
		}
	}
	.rainfall-range {
		display: flex;
		color: #fff;
		justify-content: left;
		// align-items: center;
		margin-top: 8px;
		margin-bottom: 24px;
		.btn-sty {
			padding: 5px 10px;
			font-size: 14px;
			margin-left: 10px;
			line-height: 20px;
			color: #c0c4cc;
			background: rgba(0, 0, 0, 0.4);
			border: 1px solid #3f9bfc;
		}
		.el-select {
			width: auto;
			.el-input__inner {
				// width: 88px;
				height: 32px;
				border: 1px solid #3f9bfc;
				border-radius: 2px;
			}
			.el-input__suffix {
				.el-input__icon {
					line-height: 1;
				}
			}
		}
		> .el-input {
			width: auto;
			.el-input__inner {
				width: 98px;
				height: 32px;
				border: 1px solid #3f9bfc;
				border-radius: 2px;
			}
		}
	}
	.screen-button {
		width: 72px;
		height: 32px;
		border: 1px solid #4898fb;
		border-radius: 2px;
		background: #0000;
		color: #4898fb;
		padding: 0;
	}
	.split-row {
		margin-top: 10px;
		margin-bottom: 10px;
	}
}
</style>
<style lang="scss" scoped>
@import "@/style/theme.scss";
.rain-station {
	@include flexbox;
	@include flexflow(column, wrap);
	width: 100%;
	height: calc(100% - 16px);
	color: #fff;
	font-size: 14px;
	> div {
		width: 100%;
	}
	.rain-range {
		margin-top: 16px;
		margin-bottom: 16px;
		> div {
			text-align: center;
		}
		.wave {
			line-height: 44px;
		}
	}
	.table-box {
		width: 100%;
		@include flex(1);
		@include flexbox;
		@include flexflow(column, wrap);
		position: relative;
		::v-deep .table-wrap {
			position: absolute;
			top: 0;
		}
	}
	.chart-box {
		width: 100%;
		height: 240px;
	}
}
</style>
<style lang="scss">
.year-sel.el-date-editor.el-input,
.year-sel.el-date-editor.el-input__inner {
	width: 100%;
}
.el-input-group__append,
.el-input-group__prepend {
	background: rgba(0, 0, 0, 0.4);
	border: 0;
	color: #fff;
}
</style>
