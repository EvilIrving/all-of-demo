<template>
	<div class="big-card">
		<panel-item title="大中型水库" hasSwitch tc @tclick="tclick">
			<template #title-switch>
				<i
					title="地图落点"
					@click.stop="handleReservoirChange('skbtn')"
					:class="[skbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
				/>
				<i title="查看详表" @click.stop="openDetailsDialog" class="icon-liebiao icon-sty" />
				<i @click="goToAMap('水库')" title="一张图" class="icon-yizhangtu icon-sty" />
				<i title="洪水预报弹窗" class="icon-hongshui-yubao icon-sty" @click="FloodForecasting" />
			</template>
			<!-- 当前出库水库 -->
			<div class="reservoir-sty">
				<p>
					<span>当前出库</span>
					<span>
						<span style="color: #1cfff5;font-size: 24px;line-height: 24px;margin: 0 5px;">{{
							resData.drainageRsvrNum
						}}</span>
						座
					</span>
				</p>
				<p>
					<span>累计出库</span>
					<span>
						<span style="color: #1cfff5;font-size: 24px;line-height: 24px; margin: 0 5px;">{{
							resData.drainageTotal
						}}</span>
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
									<span class="number">{{ item.drainage24H ? Number(item.drainage24H).toFixed(1) : "0" }}</span
									>万m³
								</p>
							</div>
						</div>
					</el-col>
				</el-row>
			</div>
		</panel-item>
		<ReservoirDetail v-if="detailVisible" :visible.sync="detailVisible" type="大中型水库" />
	</div>
</template>

<script>
import PanelItem from "../components/PanelItem.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import ReservoirDetail from "./dialog/ReservoirDetail.vue";
import { mapGetters, mapActions } from "vuex";
import { goAMap } from "./utils/common";
export default {
	name: "ReservoirCard",
	layerName: "digReservoirLayer",
	legendName: "水库",
	components: { PanelItem, ReservoirDetail },
	mounted() {
		this.$nextTick(() => {
			this.rsvrProjDrainageStats();
			this.timer = setInterval(() => {
				this.rsvrProjDrainageStats();
			}, 300000);
		});
	},
	beforeDestroy() {
		clearInterval(this.timer);
	},
	data() {
		return {
			detailVisible: false,
			skbtn: false, //水库点位展示标记
			//大中型水库统计数据
			resData: {},
			realtimeChartData: {
				columns: ["name", "data"],
				rows: []
			},
			realtimeChartOption: {
				grid: {
					top: "15%",
					bottom: "1%",
					containLabel: true
				},
				setting: {
					yAxisName: ["mm"],
					itemStyle: { color: "#fff" },
					label: {
						show: true,
						fontSize: 18,
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
							fontSize: this.$commonSetting.chartFontSize
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
			timer: null
		};
	},
	computed: {
		...mapGetters(["ScreenMap", "currentSelectArea", "previewModule", "formatterCurrentTime"]),
		inDrainResData() {
			let resList = this.resData.list || [];
			return resList.filter(item => Number(item.drainage24H) > 0);
		},
		map() {
			return this.$store.state.screen.mapComponent;
		}
	},
	methods: {
		...mapActions(["SetDetailDialogParam"]),
		// 标题点击打开统计卡片
		tclick(title) {
			this.$emit("tclick", title);
		},
		/**
		 * 打开详情弹框点击事件
		 */
		openDetailsDialog() {
			this.detailVisible = true;
		},
		/**
		 * 跳转一张图
		 */
		goToAMap(type) {
			goAMap(type);
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
		 * 洪水预备
		 */
		FloodForecasting() {
			this.$emit("FloodForecasting");
		},
		handleReservoirChange(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				scale: 0.7,
				anchor: [27, 30],
				anchorXUnits: "pixels",
				src: require("@/assets/images/legend/shuiku-juhe.png")
			};
			let pointList = this.inDrainResData.map(point => {
				let imgSrc = require("@/assets/images/legend/shuiku-juhe.png");
				if (point.overLimitVal != null && point.overLimitVal > 0) {
					imgSrc = require("@/assets/images/dianwei-juhe-chaoji.png");
				}
				return {
					name: point.resName,
					type: "水库",
					lng: point.lowLeftLong,
					lat: point.lowLeftLat,
					code: point.resCode,
					src: imgSrc,
					props: {
						实时水位: `${point.waterLevel} m`,
						汛限水位: `${point.currentLimitLevel} m`,
						更新时间: `${point.dataTime}`,
						鉴定结论: `${point.safetyConclusion}`
					}
				};
			});
			if (this[btnType]) {
				this.ScreenMap.drawPoint(pointList, style, "digReservoirLayer");
				this.$emit("legend-change", "水库", {
					label: "水库",
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
				this.ScreenMap.drawClusterPoint([], {}, "digRreservoirLayer");
				this.$emit("legend-change", "水库", {});
			}
		}
	}
};
</script>

<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.big-card {
	height: 100%;
	// padding: 0 16px;
	::v-deep.el-carousel {
		height: 100%;
		.el-carousel__container {
			height: 100%;
		}
	}
	.reservoir-sty {
		> p {
			line-height: 32px;
			span {
				color: #fff;
				font-size: 15px;
			}
			display: flex;
			flex-direction: column;
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
}
</style>

<style lang="scss">
.big-card .flood-discharge {
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
</style>
