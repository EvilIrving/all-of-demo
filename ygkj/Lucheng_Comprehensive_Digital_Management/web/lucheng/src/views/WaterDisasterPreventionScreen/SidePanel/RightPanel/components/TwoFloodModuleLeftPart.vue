<template>
	<div>
		<div ref="collapseContent" class="collapse-panel__border" style="width: 490px">
			<div class="collapse-panel__content">
				<!-- 大中型水库 -->
				<panel-item title="水库" hasSwitch>
					<template #title-switch>
						<i
							title="地图落点"
							@click="handleReservoirChange('skbtn')"
							:class="[skbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
						/>
						<i title="查看详表" @click="openDetailsDialog('水库分析', '防御复盘')" class="icon-liebiao icon-sty" />
						<i @click="goToAMap('水库')" title="一张图" class="icon-xin-yizhangtu icon-sty" />
					</template>
					<!-- 水库超警 -->
					<div class="reservoir-sty">
						<div>
							<p>
								<i class="icon-chao-jing icon-sty" />超警
								<span> {{ rsvrReplayStatsData != null ? rsvrReplayStatsData.warnNum : "-" }} </span> 座
							</p>
							<div>
								<p>超警时间最长</p>
								<p>
									<span>
										{{
											rsvrReplayStatsData.maxWarnTimeRsvr != null ? rsvrReplayStatsData.maxWarnTimeRsvr.resName : "-"
										}}
									</span>
									<span>{{
										rsvrReplayStatsData.maxWarnTimeRsvr != null
											? rsvrReplayStatsData.maxWarnTimeRsvr.overWarnMinutes
											: "-"
									}}</span
									>min
								</p>
							</div>
						</div>
					</div>
					<!-- 累计拦蓄水量 -->
					<div class="reservoir-sty">
						<div style="background:none;padding:0;" class="cut-off-rule">
							<p style="padding-bottom:0;border-bottom:none;">
								<i class="icon-lan-xushuiliang icon-sty" />累计拦截蓄水量
								<span style="color: #1CFFF5;">
									{{ rsvrReplayStatsData != null ? rsvrReplayStatsData.totalStorage : "-" }}
								</span>
								万m³
							</p>
							<div style="padding-top: 5px;">
								<p>拦截蓄水量最大</p>
								<p>
									<span style="color: #1CFFF5;">
										{{
											rsvrReplayStatsData.maxStorageRsvr != null ? rsvrReplayStatsData.maxStorageRsvr.resName : "-"
										}}</span
									>
									<span style="color: #1CFFF5;">
										{{
											rsvrReplayStatsData.maxStorageRsvr != null ? rsvrReplayStatsData.maxStorageRsvr.capacitySub : "-"
										}}</span
									>
									万m³
								</p>
							</div>
						</div>
					</div>
					<div class="reservoir-sty">
						<div style="background:none;padding: 0;">
							<p style="padding-bottom:0;border-bottom:none;">
								<i class="icon-xie-hong icon-sty" />累计泄洪量
								<span style="color: #1CFFF5;">
									{{ rsvrReplayStatsData != null ? rsvrReplayStatsData.totalDrainage : "-" }}
								</span>
								<span style="font-size: 18px;color: #FFF;">万m³</span>
								<span style="color: #1CFFF5; margin-left:10px;">
									{{ rsvrReplayStatsData != null ? rsvrReplayStatsData.drainageNum : "-" }} </span
								>座
							</p>
							<div style="padding-top: 5px;">
								<p>泄洪量最大</p>
								<p>
									<span style="color: #1CFFF5;">
										{{
											rsvrReplayStatsData.maxDrainageRsvr != null ? rsvrReplayStatsData.maxDrainageRsvr.resName : "-"
										}}
									</span>
									<span style="color: #1CFFF5;">
										{{
											rsvrReplayStatsData.maxDrainageRsvr != null ? rsvrReplayStatsData.maxDrainageRsvr.drainage : "-"
										}}</span
									>
									万m³
								</p>
							</div>
						</div>
					</div>
				</panel-item>
				<!-- 水闸 -->
				<panel-item title="水闸" hasSwitch>
					<template #title-switch>
						<i
							title="地图落点"
							@click="handleSluiceChange('szbtn')"
							:class="[szbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
						/>
						<i title="查看详表" @click="openDetailsDialog('水闸分析', '防御复盘')" class="icon-liebiao icon-sty" />
						<i @click="goToAMap('水闸工情')" title="一张图" class="icon-xin-yizhangtu icon-sty" />
					</template>
					<div class="reservoir-sty">
						<div style="background:none;" class="cut-off-rule">
							<p style="padding-bottom:0;border-bottom:none;" class="cut-off-rule">
								<i class="icon-xie-hong icon-sty" />累计排洪量
								<span style="color: #1CFFF5;">
									{{ wrpWagaReplayStatsData != null ? wrpWagaReplayStatsData.drainageTotal : "-" }}
								</span>
								万m³
							</p>
						</div>
					</div>
					<div class="reservoir-sty">
						<div style="background:none;padding: 0;">
							<p style="padding-bottom:0;border-bottom:none;">
								<i style="margin: 7px 10px 0 0;" class="icon-kai-qi-shu" />水闸开启数
								<span style="color: #1CFFF5;">
									{{ wrpWagaReplayStatsData != null ? wrpWagaReplayStatsData.openGateNum : "-" }}
								</span>
								个
							</p>
							<div style="padding-top: 5px;">
								<p>开闸时间最长</p>
								<p>
									<span style="color: #1CFFF5;">
										{{
											wrpWagaReplayStatsData.maxOpenGateTimeWaga == null
												? "-"
												: wrpWagaReplayStatsData.maxOpenGateTimeWaga.wagaName
										}}
									</span>
									<span style="color: #1CFFF5;">
										{{
											wrpWagaReplayStatsData.maxOpenGateTimeWaga != null
												? wrpWagaReplayStatsData.maxOpenGateTimeWaga.openGateMinutes
												: "-"
										}}
									</span>
									min
								</p>
							</div>
						</div>
					</div>
				</panel-item>
				<!-- 河网 -->
				<panel-item title="河网" hasSwitch>
					<template #title-switch>
						<i
							title="地图落点"
							@click="handleRiverNet('hwbtn')"
							:class="[hwbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
						/>
						<i title="查看详表" @click="openDetailsDialog('河网分析', '防御复盘')" class="icon-liebiao icon-sty" />
						<i @click="goToAMap('河道水位')" title="一张图" class="icon-xin-yizhangtu icon-sty" />
					</template>
					<!-- 水库超警 -->
					<div class="reservoir-sty">
						<div>
							<p>
								<i class="icon-chao-jing icon-sty" />超警
								<span>
									{{ rivernetReplayStatsData.overWarnNum != null ? rivernetReplayStatsData.overWarnNum : "-" }}
								</span>
								座
							</p>
							<div>
								<p>超警时间最长</p>
								<p>
									<span>
										{{
											rivernetReplayStatsData.maxOverWarnTime != null
												? rivernetReplayStatsData.maxOverWarnTime.rnName
												: "-"
										}}
									</span>
									<span>
										{{
											rivernetReplayStatsData.maxOverWarnTime != null
												? rivernetReplayStatsData.maxOverWarnTime.overWarnMinutes
												: "-"
										}}
									</span>
									min
								</p>
							</div>
						</div>
					</div>
					<capsule-tabs
						v-model="realtimePreChartTab"
						:tabs="['温瑞', '瑞平', '乐成', '永强', '江南', '鳌江']"
						@change="forecastChartTabChange"
					/>
					<echartLine style="margin-top: 30px;height:300px; width:100%;" ref="lineChart" :extraOption="extraOption" />
				</panel-item>
			</div>
		</div>
	</div>
</template>

<script>
import { mapGetters } from "vuex";
// 标题组件
import PanelItem from "../../../components/PanelItem";
import CapsuleTabs from "../../../components/CapsuleTabs";
// api接口调用
import { disasterPreventionApi } from "@/api/RightSildePanel";
// 折线图
import EchartLine from "../../../charts/echart_line/EchartLine.vue";
import { goAMap } from "@/views/water_disaster_custom/cards/utils/common";

export default {
	name: "TwoFloodModuleLeftPart",
	components: {
		PanelItem,
		EchartLine,
		CapsuleTabs
	},
	data() {
		return {
			rsvrReplayStatsData: {},
			wrpWagaReplayStatsData: {},
			realtimePreChartTab: 0,
			extraOption: {},
			rivernetReplayStatsData: {},
			skbtn: false, //水库点位展示标记
			szbtn: false, //水闸点位展示标记
			hwbtn: false, //河网点位展示标记
			//大中型水库统计数据
			resData: {},
			realtimeChartData: {
				columns: ["name", "data"],
				rows: []
			},
			tideStaStart: "",
			tideStaEnd: "",
			riverNetList: [],
			wageStList: [],
			reservoirList: [],
			waterLevelName: "温瑞"
		};
	},
	watch: {
		startTime: {
			immediate: true,
			handler(val) {
				if (val != "请选择时间") {
					this.tideStaStart = val + ":00";
				}
			}
		},
		currentTime: {
			immediate: true,
			handler(val) {
				if (val != "请选择时间") {
					this.tideStaEnd = val + ":00";
					this.$nextTick(() => {
						this.rsvrReplayStats();
						this.wrpWagaReplayStats();
						this.rivernetReplayStats();
					});
				}
			}
		}
	},
	mounted() {
		this.$nextTick(() => {
			this.tideStaStart =
				this.$moment()
					.subtract(3, "days")
					.format("YYYY-MM-DD HH") + ":00:00";
			this.tideStaEnd =
				this.$moment()
					.subtract(0, "days")
					.format("YYYY-MM-DD HH") + ":00:00";
			this.rsvrReplayStats();
			this.wrpWagaReplayStats();
			this.rivernetReplayStats();
			this.getRightMapPoints("水库");
			this.getRightMapPoints("水闸");
			this.getRightMapPoints("河网");
		});
	},
	computed: {
		...mapGetters(["ScreenMap", "mapComponent", "formatterStartTime", "formatterCurrentTime"]),

		startTime() {
			let time = this.formatterStartTime();
			return time != "Invalid date" ? time : "请选择时间";
		},
		currentTime() {
			let time = this.formatterCurrentTime();
			return time != "Invalid date" ? time : "请选择时间";
		},
		map() {
			return this.$store.state.screen.mapComponent;
		}
	},
	methods: {
		/**
		 * 河网tab切换
		 */
		forecastChartTabChange(val) {
			this.realtimePreChartTab = val;
			switch (val) {
				case 0:
					{
						this.waterLevelName = "温瑞";
						this.rivernetReplayStats();
					}
					break;
				case 1:
					{
						this.waterLevelName = "瑞平";
						this.rivernetReplayStats();
					}
					break;
				case 2:
					{
						this.waterLevelName = "乐成";
						this.rivernetReplayStats();
					}
					break;
				case 3:
					{
						this.waterLevelName = "永强";
						this.rivernetReplayStats();
					}
					break;
				case 4:
					{
						this.waterLevelName = "江南";
						this.rivernetReplayStats();
					}
					break;
				case 5:
					{
						this.waterLevelName = "鳌江";
						this.rivernetReplayStats();
					}
					break;
			}
		},
		/**
		 * 水库接口
		 */
		async rsvrReplayStats() {
			let obj = {
				start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
				end: this.tideStaEnd // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
			};
			await disasterPreventionApi.rsvrReplayStats(obj).then(res => {
				if (res.code == 0) {
					this.rsvrReplayStatsData = res.data;
				}
			});
		},
		/**
		 * 水闸
		 */
		async wrpWagaReplayStats(val) {
			let obj = {
				start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
				end: this.tideStaEnd // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
			};
			await disasterPreventionApi.wrpWagaReplayStats(obj).then(res => {
				if (res.code == 0) {
					this.wrpWagaReplayStatsData = res.data;
				}
			});
		},
		/**
		 * 河网接口
		 */
		async rivernetReplayStats(val) {
			let obj = {
				waterLevelCurve: true,
				rnName: this.waterLevelName, // 测站名 温州 龙湾 瑞安 鳌江
				start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
				end: this.tideStaEnd // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
			};
			await disasterPreventionApi.rivernetReplayStats(obj).then(res => {
				if (res.code == 0) {
					this.rivernetReplayStatsData = res.data;
					let arr = [];
					let obj = [];
					let series = [];
					let warnTide = [];
					let xAxisArr = [];
					this.extraOption = {};
					if (res.data.list.length > 0) {
						res.data.list[0].waterLevelList.forEach(val => {
							if (val.z && val.tm) {
								arr.push(val.z);
								xAxisArr.push(val.tm.slice(5, 16));
							}
							warnTide.push(res.data.list[0].warningLevel);
						});
						let xAxis = [
							{
								//去掉刻度线
								axisTick: {
									show: false
								},
								axisLine: {
									lineStyle: {
										// 设置x轴颜色
										color: "rgba(0,168,236,0.7)"
									}
								},
								axisLabel: {
									show: true,
									textStyle: {
										color: "#fff",
										fontSize: "15"
									},
									formatter: function(value) {
										var ret = ""; //拼接加\n返回的类目项
										var maxLength = 6; //每项显示文字个数
										var valLength = value.length; //X轴类目项的文字个数
										var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
										if (rowN > 1) {
											//如果类目项的文字大于3,
											for (var i = 0; i < rowN; i++) {
												var temp = ""; //每次截取的字符串
												var start = i * maxLength; //开始截取的位置
												var end = start + maxLength; //结束截取的位置
												//这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
												temp = value.substring(start, end) + "\n";
												ret += temp; //凭借最终的字符串
											}
											return ret;
										} else {
											return value;
										}
									}
								},
								data: xAxisArr
							}
						];
						this.extraOption.xAxis = xAxis;
					}
					series.push(
						{
							data: arr,
							type: "line",
							smooth: true,
							name: "实时水位",
							itemStyle: {
								normal: {
									lineStyle: {
										width: 2
									}
								}
							},
							markPoint: {
								symbol: "circle",
								symbolSize: 10,
								data: [
									{
										name: "最大值",
										type: "max"
									}
								],
								label: {
									position: "top",
									fontSize: 18,
									formatter: function(data) {
										return data.value + "m";
									}
								}
							}
						},
						{
							data: warnTide,
							type: "line",
							smooth: true,
							name: "警戒水位",
							lineStyle: {
								color: "#FF0000",
								type: "dashed"
							},
							itemStyle: {
								opacity: 0
							}
						}
					);
					this.extraOption.series = series;
					this.extraOption.grid = { left: 40 };
					this.extraOption.color = ["#33FFFF", "#FF0000"];
				}
			});
		},
		/**
		 * 打开详情弹框点击事件
		 */
		openDetailsDialog(val, data) {
			this.$emit("open-analyse-dialog", val, data);
		},
		/**
		 * 跳转一张图
		 */
		goToAMap(type) {
			goAMap(type);
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
				case "水闸":
					{
						this.wageStList = res.data;
					}
					break;
				case "河网":
					{
						this.riverNetList = res.data;
					}
					break;
			}
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
				srcs: require("@/assets/images/dianwei-juhe.png"),
				src: require("@/assets/images/legend/shuiku-juhe.png")
			};
			let pointList = [];
			for (let point of this.reservoirList) {
				let imgSrc = require("@/assets/images/hewang-chao-jing.png");
				if (point.overLimitVal != null && point.overLimitVal > 0) {
					imgSrc = require("@/assets/images/dianwei-juhe-chaoji.png");
				}
				let str = {
					实时水位: `${point.waterLevel} m`,
					汛限水位: `${point.currentLimitLevel} m`,
					更新时间: `${point.dataTime}`,
					鉴定结论: `${point.safetyConclusion}`
				};
				let obj = {
					实时水位: `${point.waterLevel} m`,
					汛限水位: `${point.smallRsvrCurLimitLevel} m`,
					更新时间: `${point.dataTime}`,
					溢洪道高程: `${point.normalSpillwayCrestElevation} m`,
					鉴定结论: `${point.safetyConclusion}`
				};
				pointList.push({
					name: point.resName,
					type: "水库",
					lng: point.lowLeftLong,
					lat: point.lowLeftLat,
					code: point.resCode,
					src: imgSrc,
					stationCode: point.stCode || "",
					props: point.engScal < 4 ? str : obj
				});
			}
			if (this[btnType]) {
				this.map.drawClusterPoint(pointList, style, "reservoirLayer");
				this.$emit("legend-change", "水库", {
					label: "水库",
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
				this.$emit("legend-change", "水库", {});
			}
		},
		/**
		 * 水闸点位开关
		 */
		handleSluiceChange(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				scale: 0.5,
				anchor: [24, 16],
				anchorXUnits: "pixels",
				src: require("@/assets/images/MapPoint/sluice.png")
				// srcs:require("@/assets/images/MapPoint/sluice.png")
			};
			let pointList = [];
			for (let point of this.wageStList) {
				let imgSrc = require("@/assets/images/MapPoint/sluice.png");
				// let imgSrcs = require("@/assets/images/shuizha-dianwei-kaiqi.png");
				if (!point.gateOpened) {
					imgSrc = require("@/assets/images/MapPoint/sluice-warn.png");
					// imgSrcs = require("@/assets/images/MapPoint/sluice.png");
				}
				pointList.push({
					lng: point.startLong,
					lat: point.startLat,
					type: "水闸",
					src: imgSrc,
					// srcs:imgSrcs,
					code: point.projCode,
					name: point.stName,
					props: { 闸上水位: `${point.upz}m`, 闸下水位: `${point.dwz}m` }
				});
			}
			if (this[btnType]) {
				this.map.drawPoint(pointList, style, "sluiceLayer");
				this.$emit("legend-change", "水闸", {
					label: "水闸",
					children: [
						{
							label: "开启",
							icon: "icon-shui-zha-kaiqi"
						},
						{
							label: "关闭",
							icon: "icon-shui-zha-guanbi"
						}
					]
				});
			} else {
				this.map.drawPoint([], {}, "sluiceLayer");
				this.$emit("legend-change", "水闸", {});
			}
		},
		/**
		 * 河网点位开关
		 */
		handleRiverNet(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				scale: 0.5,
				anchor: [49, 16],
				anchorXUnits: "pixels",
				src: require("@/assets/images/hewang-zheng-chang.png")
			};
			let pointList = [];
			for (let point of this.riverNetList) {
				let imgSrc = require("@/assets/images/generalPoints/general-hydrology-abnormal.png");
				if (!point.gateOpened) {
					imgSrc = require("@/assets/images/generalPoints/general-hydrology.png");
				}
				pointList.push({
					lng: point.stLong,
					lat: point.stLat,
					type: "河网",
					src: imgSrc,
					name: point.mainStName,
					props: { 水位: `${point.waterLevel}m` }
				});
			}
			if (this[btnType]) {
				this.$emit("legend-change", "河网", {
					label: "河网",
					children: [
						{ label: "正常", icon: "icon-he-wang-zhengchang" },
						{ label: "超警", icon: "icon-ha-wang-chaojing" }
					]
				});
				this.map.drawPoint(pointList, style, "riverNetLayer");
			} else {
				this.map.drawPoint([], {}, "riverNetLayer");
				this.$emit("legend-change", "河网", {});
			}
		}
	}
};
</script>

<style lang="scss" scoped>
@import "../../../style/SildePanel.scss";
.collapse-panel__content {
	.icon-sty {
		margin-right: 10px;
	}
	.reservoir-sty {
		font-size: 18px;
		margin: 10px 0;
		> div {
			width: 100%;
			padding: 10px 20px;
			line-height: 24px;
			background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat top center;
			background-size: 100% 100%;
			> p:nth-child(1) {
				display: flex;
				color: #fff;
				font-size: 18px;
				line-height: 24px;
				justify-content: center;
				padding-bottom: 10px;
				border-bottom: 1px solid rgba($color: #fff, $alpha: 0.2);
				> i {
					margin-top: 2px;
				}
				> span {
					margin: 0 10px;
					font-size: 24px;
					font-weight: 500;
					letter-spacing: 0;
					line-height: 24px;
					color: #eec80b;
				}
			}
			> div {
				color: #fff;
				display: flex;
				padding: 10px;
				justify-content: space-between;
				> p:nth-child(2) {
					> span {
						font-size: 24px;
						color: #eec80b;
					}
					> span:nth-child(2) {
						font-size: 24px;
						margin: 0 10px;
					}
				}
			}
		}
		.cut-off-rule {
			padding-bottom: 10px;
			border-bottom: 1px solid rgba($color: #00c1ff, $alpha: 0.4);
		}
	}
}
</style>
