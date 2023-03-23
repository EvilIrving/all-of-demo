<template>
	<div class="rain-forecast-card">
		<panel-item title="水库" :inSimulation="previewModule" has-switch tc @tclick="tclick">
			<template #title-switch>
				<i
					title="地图落点"
					@click.stop="handleReservoirChange('skbtn')"
					:class="[skbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
				/>
				<i v-show="!previewModule" title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog" />
				<i v-show="!previewModule" @click="goToAMap('水库')" title="一张图" class="icon-yizhangtu icon-sty" />
				<i title="洪水预报弹窗" class="icon-hongshui-yubao icon-sty" @click="FloodForecasting" />
			</template>

			<el-carousel trigger="click" arrow="never" :autoplay="false" :interval="interval">
				<el-carousel-item class="carousel-item-content">
					<!-- 水库总数  超汛限数 -->
					<div class="reservoir-sty">
						<p style="font-size: 20px">
							总数 <span> {{ reservoirData.allTotal }} </span> 座
						</p>
						<p style="font-size: 20px">
							预警 <span> {{ reservoirData.allOverLimit }} </span> 座
						</p>
					</div>
					<!-- 大中型水库详情 -->
					<div :class="[resSelect.大中型 == true ? 'reservoir-style' : '']">
						<div class="area-style" @click="reservoirType('大中型', '大中型')">
							<p><i class="icon-shuiku icon-sty"></i><span>大中型</span></p>
							<p>
								总数 <span> {{ reservoirData.lmTotal }} </span> 座
							</p>
							<p>
								超汛限 <span> {{ reservoirData.lmOverLimit }} </span> 座
							</p>
						</div>
					</div>
					<!-- 小(Ⅰ)型水库 -->
					<div :class="[resSelect.小Ⅰ型 == true ? 'reservoir-style' : '']">
						<div class="area-style" @click="reservoirType('小（Ⅰ）型', '小Ⅰ型')">
							<p><i class="icon-shuiku icon-sty"></i><span>小（Ⅰ）型</span></p>
							<p>
								总数 <span> {{ reservoirData.s1Total }} </span> 座
							</p>
							<p>
								溢流 <span> {{ reservoirData.s1OverLimit }} </span> 座
							</p>
						</div>
					</div>
					<!-- 小（II）型 -->
					<div :class="[resSelect.小Ⅱ型 == true ? 'reservoir-style' : '']">
						<div class="area-style" @click="reservoirType('小（Ⅱ）型', '小Ⅱ型')">
							<p><i class="icon-shuiku icon-sty"></i><span>小（Ⅱ）型</span></p>
							<p>
								总数 <span> {{ reservoirData.s2Total }} </span> 座
							</p>
							<p>
								溢流 <span> {{ reservoirData.s2OverLimit }} </span> 座
							</p>
						</div>
					</div>
				</el-carousel-item>

				<el-carousel-item class="carousel-item-content">
					<!-- 大中型水库详情 -->
					<div :class="[resSelect.大中型 == true ? 'reservoir-style' : '']">
						<div class="area-style" @click="reservoirType('大中型', '大中型')">
							<p><i class="icon-shuiku icon-sty"></i><span>大中型</span></p>
							<p>
								总数 <span> {{ reservoirData.lmTotal }} </span> 座
							</p>
							<p>
								超汛限 <span> {{ reservoirData.lmOverLimit }} </span> 座
							</p>
						</div>
					</div>
					<!-- 水库详情图表展示 -->
					<div class="diagram-sty">
						<el-row class="diagram-sty-content">
							<el-col :span="12" class="border-L">
								<echartInstrument :seriesData="seriesData" :extraOption="instrumentOption"></echartInstrument>
								<div
									style="
                    display: flex;
                    justify-content: center;
                    margin: -5px 0 5px 0;
                  "
								>
									<p class="storage-capacity"></p>
									<p
										style="
                      padding: 0 6px;
                      color: #1cfff5;
                      font-size: 18px;
                      font-weight: 500;
                    "
									>
										平均蓄水率
									</p>
									<p class="storage-capacity-sty"></p>
								</div>
							</el-col>
							<el-col :span="12">
								<div
									style="
                    display: flex;
                    justify-content: center;
                    margin: 0 0 10px 0;
                  "
								>
									<p class="storage-capacity"></p>
									<p
										style="
                      padding: 0 4px;
                      color: #1cfff5;
                      font-size: 20px;
                      font-weight: 500;
                      text-align: center;
                    "
									>
										纳蓄能力(mm)
									</p>
									<p class="storage-capacity-sty"></p>
								</div>
								<vue-seamless-scroll :data="reservoirDetails.storageList" class="border-R" :class-option="classOption">
									<div
										class="classOption-sty"
										style="display: flex; justify-content: space-between"
										v-for="(item, index) in reservoirDetails.storageList"
										:key="index"
									>
										<p>{{ item.resName }}</p>
										<p>
											<span style="color: #fff; font-size: 20px">
												{{ item.floodStorageAbility == null ? "-" : item.floodStorageAbility }}
											</span>
										</p>
									</div>
								</vue-seamless-scroll>
							</el-col>
						</el-row>
					</div>
				</el-carousel-item>

				<el-carousel-item class="carousel-item-content">
					<!-- 当前出库水库 -->
					<div class="reservoir-sty1">
						<p>
							<span>当前出库</span>
							<span>
								<span
									style="
                    color: #1cfff5;
                    font-size: 24px;
                    line-height: 24px;
                    margin: 0 5px;
                  "
								>
									{{ resData.drainageRsvrNum }}
								</span>
								座
							</span>
						</p>
						<p>
							<span>累计出库</span>
							<span>
								<span
									style="
                    color: #1cfff5;
                    font-size: 24px;
                    line-height: 24px;
                    margin: 0 5px;
                  "
								>
									{{ resData.drainageTotal }}</span
								>
								万m³
							</span>
						</p>
					</div>
					<!-- 详情出库量 -->
					<div class="flood-discharge">
						<el-row class="flood-discharge-list">
							<el-col
								v-for="(item, index) in inDrainResData"
								:key="index"
								:span="12"
								:class="['flood-discharge-item', [2].includes(index % 4) && 'white-bg']"
							>
								<div class="flood-discharge-item__content">
									<p class="item-title">{{ item.resName }}</p>
									<div class="item-text">
										<i class="icon-xie-hong"></i>
										<p class="item-label">
											出库
											<span class="number"> {{ item.drainage24H ? Number(item.drainage24H).toFixed(1) : "0" }} </span
											>万m³
										</p>
									</div>
								</div>
							</el-col>
						</el-row>
					</div>
				</el-carousel-item>
			</el-carousel>
		</panel-item>
		<ReservoirDetail v-if="detailVisible" :visible.sync="detailVisible" type="水库" />
	</div>
</template>

<script>
import PanelItem from "../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { mapGetters, mapActions } from "vuex";
import vueSeamlessScroll from "vue-seamless-scroll";
import EchartInstrument from "../charts/echart_instrument/EchartInstrument.vue"; // 仪表盘图
import { goAMap } from "./utils/common";
import ReservoirDetail from "./dialog/ReservoirDetail.vue";

export default {
	name: "ReservoirCard",
	layerName: ["reservoirLayer", "abnormalReservoirLayer"],
	legendName: "水库",
	components: {
		PanelItem,
		vueSeamlessScroll,
		EchartInstrument,
		ReservoirDetail
	},
	mounted() {
		this.$nextTick(() => {
			this.rsvrProjStats2("大中型");
			this.getRightMapPoints("水库");
			this.rsvrProjStats1(); //水库一级数据接口
			this.rsvrProjDrainageStats();
			this.timer = setInterval(() => {
				this.rsvrProjStats2("大中型");
				this.getRightMapPoints("水库");
				this.rsvrProjStats1();
				this.rsvrProjDrainageStats();
			}, 300000);
		});
	},
	data() {
		this.classOption = {
			step: 0.2, // 数值越大速度滚动越快
			limitMoveNum: 2, // 开始无缝滚动的数据量 this.dataList.length
			hoverStop: true, // 是否开启鼠标悬停stop
			direction: 1, // 0向下 1向上 2向左 3向右
			openWatch: true, // 开启数据实时监控刷新dom
			singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
			singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
			waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
		};
		return {
			interval: 5000,
			resSelect: { 大中型: false, 小Ⅰ型: false, 小Ⅱ型: false },
			skbtn: false, //水库点位展示标记
			//水库列表
			reservoirList: [],
			//水库数据
			reservoirData: {},
			//水库二级数据详情
			reservoirDetails: {},
			//水库图表数据
			seriesData: [],
			instrumentOption: {},
			//大中型水库统计数据
			resData: {},
			timer: null,
			detailVisible: false
		};
	},
	beforeDestroy() {
		clearInterval(this.timer);
	},
	computed: {
		...mapGetters(["ScreenMap", "currentSelectArea", "previewModule", "formatterCurrentTime", "AffairId"]),
		map() {
			return this.$store.state.screen.mapComponent;
		},
		inDrainResData() {
			let resList = this.resData.list || [];
			return resList.filter(item => Number(item.drainage24H) > 0);
		},
		currentTime() {
			let time = this.formatterCurrentTime();
			return time != "Invalid date" ? time : "请选择时间";
		}
	},
	watch: {
		currentTime: {
			//预演时数据源更换
			immediate: true,
			handler(val) {
				this.$nextTick(() => {
					if (this.currentTime != "请选择时间") {
						this.rsvrPreviewProjStats1();
						this.rsvrProjStats2("大中型", true);
					} else {
						this.rsvrProjStats1();
						this.rsvrProjStats2("大中型");
					}
				});
			}
		}
	},
	methods: {
		...mapActions(["changeScreenLoading", "getForecastRainfallLayer", "SetDetailDialogParam"]),
		// 标题点击打开统计卡片
		tclick(title) {
			this.$emit("tclick", title);
		},
		/**
		 * 洪水预报
		 */
		FloodForecasting() {
			this.$emit("FloodForecasting");
		},
		/**
		 * 大中型水库
		 */
		async rsvrProjDrainageStats() {
			await disasterPreventionApi.rsvrProjDrainageStats().then(res => {
				if (res.code == 0) {
					this.resData = res.data;
				}
			});
		},
		/**
		 * 水库点位开关
		 * @param {number} index 开关的序号
		 */
		handleReservoirChange(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				srcs: require("@/assets/images/MapPoint/dianwei-juhe.png"),
				src: require("@/assets/images/MapPoint/reservoir.png")
			};
			let abnormalStyle = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				srcs: require("@/assets/images/MapPoint/dianwei-juhe-chaoji.png"),
				src: require("@/assets/images/MapPoint/reservoir-warn.png")
			};
			let pointList = [];
			let abnormalList = [];
			for (let point of this.reservoirList) {
				let str = {
					实时水位: `${point.waterLevel || "-"}m`,
					汛限水位: `${point.currentLimitLevel || "-"}m`,
					更新时间: `${point.dataTime || "-"}`,
					鉴定结论: `${point.safetyConclusion || "-"}`
				};
				let obj = {
					实时水位: `${point.waterLevel || "-"}m`,
					汛限水位: `${point.smallRsvrCurLimitLevel || "-"}m`,
					更新时间: `${point.dataTime || "-"}`,
					溢洪道高程: `${point.normalSpillwayCrestElevation || "-"}m`,
					鉴定结论: `${point.safetyConclusion || "-"}`
				};
				let pointData = {
					name: point.resName,
					type: "水库",
					lng: point.lowLeftLong,
					lat: point.lowLeftLat,
					code: point.resCode,
					stationCode: point.stCode || "",
					props: point.engScal < 4 ? str : obj
				};
				if (point.overLimitVal != null && point.overLimitVal > 0) {
					abnormalList.push(pointData);
				} else {
					pointList.push(pointData);
				}
			}
			if (this[btnType]) {
				this.map.drawClusterPoint(pointList, style, "reservoirLayer");
				this.map.drawClusterPoint(abnormalList, abnormalStyle, "abnormalReservoirLayer");
				this.$emit("legend-change", "水库", {
					label: "水库",
					clusterLayer: ["reservoirLayer"],
					children: [
						{
							label: "正常",
							icon: "icon-legend-water-general"
						},
						{
							label: "超警",
							icon: "icon-legend-water-flood"
						}
					]
				});
			} else {
				this.map.drawClusterPoint([], {}, "reservoirLayer");
				this.map.drawClusterPoint([], {}, "abnormalReservoirLayer");
				this.$emit("legend-change", "水库", {});
			}
		},
		/**
		 * 打开详情弹框点击事件
		 */
		openDetailsDialog(val) {
			this.detailVisible = true;
		},
		/**
		 * 跳转一张图
		 */
		goToAMap(type) {
			goAMap(type);
		},
		/**
		 * 水库类型详情
		 */
		reservoirType(val, data) {
			let style = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				srcs: require("@/assets/images/dianwei-juhe.png"),
				src: require("@/assets/images/legend/shuiku-juhe.png")
			};
			this.resSelect[data] = !this.resSelect[data];
			this.rsvrProjStats2(val).then(() => {
				let pointList = [];
				if (this.resSelect[data]) {
					for (let point of this.reservoirDetails.storageList) {
						let imgSrc = require("@/assets/images/legend-large-normal.png");
						if (point.overLimitVal != null && point.overLimitVal > 0) {
							imgSrc = require("@/assets/images/legend-large-abnormal.png");
						}
						pointList.push({
							name: point.resName,
							type: "水库",
							lng: point.lowLeftLong,
							lat: point.lowLeftLat,
							code: point.resCode,
							src: imgSrc,
							props: {
								汛限水位: `${point.currentLimitLevel || "-"}m`,
								实时水位: `${point.waterLevel || "-"}m`
							}
						});
					}
					this.map.drawClusterPoint(pointList, style, `${val}Layer`);
					this.$emit("legend-change", "水库", {
						label: "水库",
						clusterLayer: `${val}Layer`,
						children: [
							{
								label: "正常",
								icon: "icon-legend-water-general-reservoir"
							},
							{
								label: "超警",
								icon: "icon-legend-water-flood-control"
							}
						]
					});
				} else {
					this.map.drawClusterPoint([], {}, `${val}Layer`);
					this.$emit("legend-change", "水库", {});
				}
			});
		},
		handleWindowResize() {
			if (!this.riverChart) return;
			this.riverChart.resize();
		},
		/**
		 * 水库一级数据
		 */
		async rsvrProjStats1() {
			await disasterPreventionApi.rsvrProjStats1().then(res => {
				if (res.code == 0) {
					this.reservoirData = res.data;
					this.$nextTick(() => {
						this.handleWindowResize();
					});
				}
			});
		},
		/**
		 * 预演水库数据
		 */
		async rsvrPreviewProjStats1() {
			let opt = {
				drillAffairId: this.AffairId,
				dataTime: new Date(this.formatterCurrentTime()).format("yyyy-MM-dd hh:mm:ss")
			};
			await disasterPreventionApi.rsvrProjStats1(opt).then(res => {
				if (res.code == 0) {
					this.reservoirData = res.data;
					this.$nextTick(() => {
						this.handleWindowResize();
					});
				}
			});
		},
		/**
		 * 水库二级数据详情
		 */
		async rsvrProjStats2(val, flag) {
			this.seriesData = [];
			let obj = {
				scaleType: val
			};
			if (flag) {
				obj.drillAffairId = this.AffairId;
				obj.dataTime = new Date(this.formatterCurrentTime()).format("yyyy-MM-dd hh:mm:ss");
			}
			await disasterPreventionApi.rsvrProjStats2(obj).then(res => {
				if (res.code == 0) {
					// console.log(res, 9988);
					let str = "平均蓄水率";
					this.instrumentOption = {
						tooltip: {
							show: true,
							formatter: function() {
								return '<span style="font-size:18px">温州市总蓄水量' + res.data.totalStorage + "万m³</span>";
							},
							confine: true
						}
					};
					this.seriesData.push(res.data.avgStorageRatio);
					this.reservoirDetails = res.data;
					this.$nextTick(() => {
						this.handleWindowResize();
					});
				}
			});
		},
		/**
		 * 点位list
		 */
		async getRightMapPoints(type) {
			let opt = {
				type
			};
			let res = await disasterPreventionApi.rightMapPoints(opt);
			switch (type) {
				case "水库":
					{
						this.reservoirList = res.data;
					}
					break;
			}
		}
	}
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.rain-forecast-card {
	height: 100%;
	.flood-discharge {
		user-select: none;
		color: #fff;
		width: 100%;
		overflow-y: scroll;
		&::-webkit-scrollbar {
			display: none;
		}
		.flood-discharge-item {
			padding: 10px 0;
			border-right: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
			position: relative;
			&.white-bg {
				&:after {
					content: "";
					position: absolute;
					height: 100%;
					width: 200%;
					left: 0;
					top: 0;
					opacity: 0.5;
					background-image: linear-gradient(90deg, #ffffff33 0%, #f9f9f900 100%);
				}
			}
			&:nth-child(even) {
				border-right: 0;
			}
			.flood-discharge-item__content {
				.item-title {
					font-size: 16px;
					text-align: center;
					padding-top: 5px;
				}
				.item-text {
					display: flex;
					padding-top: 5px;
					font-size: 18px;
					justify-content: center;
					> i {
						margin: 5px 10px 0 0;
					}
					.item-label {
						font-size: 14px;
						.number {
							margin: 0 5px;
							font-size: 24px;
							color: #1cfff5;
						}
					}
				}
			}
		}
	}
}
::v-deep .rivertooltip__item {
	padding: 4px 8px;
	font-size: 18px;
	&.rivertooltip__item-error {
		color: #ff7144;
	}
	&.rivertooltip__item-warn {
		color: #eec80b;
	}
}
::v-deep .panel-item__title--switch {
	i {
		cursor: pointer;
	}
}
.reservoir-sty {
	> p {
		@include flexbox;
		@include flexflow(row, nowrap);
		line-height: 32px;
		padding: 4px 0;
		span {
			color: #fff;
			font-size: 24px;
		}
		display: flex;
		// flex-direction: column;
		align-items: center;
	}
	// > p:nth-child(1) {
	//   margin-right: 16px;
	//   > span {
	//     color: #1cfff5;
	//   }
	// }
	> p:nth-child(2) {
		background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat top center;
		background-size: 100% 100%;
	}
}
.reservoir-style {
	$color: #1cfff485;
	// position: absolute;
	content: "";
	width: 100%;
	// height: 2.5rem;
	left: 0;
	background: linear-gradient(to bottom, $color, $color) left top no-repeat,
		linear-gradient(to bottom, $color, $color) left top no-repeat,
		linear-gradient(to bottom, $color, $color) right top no-repeat,
		linear-gradient(to bottom, $color, $color) right top no-repeat,
		linear-gradient(to bottom, $color, $color) left bottom no-repeat,
		linear-gradient(to bottom, $color, $color) left bottom no-repeat,
		linear-gradient(to bottom, $color, $color) right bottom no-repeat,
		linear-gradient(to bottom, $color, $color) right bottom no-repeat;
	/*设置大小*/
	background-size: 12px 2px, 2px 12px;
	border-radius: 3px;
}
::v-deep .el-carousel {
	.el-carousel__container {
		height: 100%;
	}
}

.carousel-item-content {
	// padding: 0 16px;
	@include flexbox;
	@include flexflow(column, nowrap);
	@include flexJC(space-around);
}
.reservoir-sty1 {
	margin: 0;
	@include flexbox;
	@include flexJC(space-between);
	p {
		margin: 0;
		line-height: 32px;
		width: 50%;
		margin: 12px 0;
		@include flexbox;
		@include flexflow(column, nowrap);
		@include flexAC;
		@include flexJC(center);
		background: url(~assets/images/WaterDisastersImage/rainfall-bg.png) no-repeat;
		background-size: 100% 100%;
	}
	> p:nth-child(1) {
		margin-right: 16px;
		> span {
			color: #1cfff5;
			font-size: 16px;
		}
	}
	> p:nth-child(2) {
		background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat top center;
		background-size: 100% 100%;
		> span {
			font-size: 16px;
			> span {
				color: #eec80b;
			}
		}
	}
}
.area-style {
	padding: 5px !important;
	// margin: 0;
	margin: 0;
	height: 40px;
	p {
		@include flexbox;
		@include flexflow(row, nowrap);
		@include flexAC;
		span {
			margin: 0;
		}
	}

	> p:nth-child(1) {
		line-height: 1.8rem;
		color: #1cfff5;
		font-weight: 500;
		@include flexbox;
		> i {
			vertical-align: text-top;
			margin-top: 2px;
			margin-right: 0;
		}
		> span {
			padding-left: 2px;
			color: #1cfff5;
		}
	}
	> p:nth-child(2) {
		line-height: 25px;
		color: #fff;
		> span {
			// font-size: 28px;
			color: #1ff1f0;
			padding: 0 6px;
		}
	}
	> p:nth-child(3) {
		line-height: 25px;
		color: #fff;
		> i {
			vertical-align: text-top;
			margin-top: 7px;
			margin-right: 0;
		}
		> span {
			font-size: 28px;
			color: #eec80b;
			padding: 0 6px;
		}
	}
}
.diagram-sty {
	// margin: 15px 0;
	color: #fff;
	height: calc(100% - 60px);
	.diagram-sty-content {
		height: 100%;
	}
	.border-L {
		height: 90%;
		border-right: 1px solid rgba($color: #00c1ff, $alpha: 0.4);
	}
	::v-deep.border-R::-webkit-scrollbar {
		/*滚动条整体样式*/
		width: 0; /*高宽分别对应横竖滚动条的尺寸*/
	}
	.border-R {
		padding-left: 10px;
		height: 90%;
		overflow-y: auto;
		font-size: 16px;
		color: #1cfff5;
		.classOption-sty {
			padding: 5px 0;
			display: flex;
			justify-content: space-between;
			> p:nth-child(1) {
				// width: 100px;
				overflow-x: auto;
				overflow: hidden;
			}
		}
	}
}
.storage-capacity {
	margin-top: 9px;
	background-image: linear-gradient(64deg, rgba(0, 193, 255, 0) 5%, #00c1ff 100%);
	width: 15%;
	height: 2px;
}
.storage-capacity-sty {
	background-image: linear-gradient(64deg, #00c1ff 5%, rgba(0, 193, 255, 0) 100%);
	margin-top: 9px;
	width: 20%;
	height: 2px;
}

.icon-sty {
	margin-right: 10px;
	&.icon-year-100 {
		@include chart-icon();
		background: #19af47;
	}
	&.icon-year-50 {
		@include chart-icon();
		background: #eec80b;
	}
	&.icon-year-20 {
		@include chart-icon();
		background: #e89100;
	}
	&.icon-year-10 {
		@include chart-icon();
		background: #dd00db;
	}
	&.icon-year-5 {
		@include chart-icon();
		background: #e80000;
	}
	&.risk-legend {
		width: 16px;
		height: 16px;
		border-radius: 50%;
		display: inline-block;
		@include chart-icon();
	}
}
</style>
