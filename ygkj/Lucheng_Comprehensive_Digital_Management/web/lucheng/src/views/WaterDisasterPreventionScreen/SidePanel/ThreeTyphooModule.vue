<template>
	<!-- 台风板块 -->
	<div class="collapse-panel__content">
		<panel-item :title="currentScene == '防御复盘' ? '复盘台风' : '历史台风'">
			<!-- <template #title-switch>
        <i title="地图落点" class="icon-yinzhang-active icon-sty" />
        <i title="一张图" class="icon-yizhangtu icon-sty" />
      </template> -->
			<!-- small-module-sty 为标题下容器样式 -->
			<div class="small-module-sty">
				<!-- 台风名称 -->
				<div class="typhoon-name-sty">
					<p>
						{{ historyTyphoonData.name == null ? '-' : historyTyphoonData.name }}
						{{ historyTyphoonData.ename == null ? '-' : historyTyphoonData.ename }}
						<span> {{ particularsData.power == null ? '-' : particularsData.power }}</span> 级
					</p>
					<p>
						影响时间：<span> {{ historyTyphoonData.hours == null ? '-' : historyTyphoonData.hours }} </span>h
					</p>
				</div>
				<!-- 台风详情 -->
				<div class="typhoon-details-sty">
					<div>
						<el-row>
							<el-col :span="12" class="typhoon-time-sty">
								<p><i class="icon-time-bg icon-sty"></i></p>
								<div>
									<p>{{ particularsData.time == null ? '-' : particularsData.time.slice(11, 16) }}</p>
									<p>{{ particularsData.time == null ? '-' : particularsData.time.slice(0, 10) }}</p>
								</div>
							</el-col>
							<el-col :span="12" class="typhoon-time-sty typhoon-distance-sty">
								<p><i class="icon-location-bg icon-sty"></i></p>
								<div>
									<p>{{ particularsData.distance == null ? '-' : particularsData.distance }} <span>公里</span></p>
									<p>距离温州</p>
								</div>
							</el-col>
						</el-row>
						<el-row>
							<el-col :span="24" class="typhoon-time-sty windstorm">
								<p><i class="icon-time-to-backup icon-sty"></i></p>
								<div>
									<p>{{ particularsData.strong == null ? '-' : particularsData.strong }}</p>
									<p>台风级别</p>
								</div>
							</el-col>
						</el-row>
					</div>
					<!-- 台风详细信息 -->
					<!-- 中心位置 -->
					<div class="typhoon-information-sty">
						<div>
							<p>
								{{ particularsData.lng == null ? '-' : particularsData.lng }} <span> °E</span>
								{{ particularsData.lat == null ? '-' : particularsData.lat }}<span> °N</span>
							</p>
							<div>
								<span>中心位置</span>
								<p class="typhoon-singlet-line"></p>
							</div>
						</div>
						<div class="typhoon-information-style"></div>
					</div>
					<!-- 中心气压 -->
					<div class="typhoon-information-sty" @click="AccordingSwitch('中心气压')">
						<div>
							<p>{{ particularsData.pressure == null ? '-' : particularsData.pressure }} <span> 百帕</span></p>
							<div>
								<span>中心气压</span>
								<p class="typhoon-singlet-line"></p>
							</div>
						</div>
						<div class="typhoon-pressure-style"></div>
					</div>
					<!-- 风力风速 -->
					<div class="typhoon-information-sty" @click="AccordingSwitch('风力风速')">
						<div>
							<p>
								{{ particularsData.power == null ? '-' : particularsData.power }} <span>级</span>
								{{ particularsData.speed == null ? '-' : particularsData.speed }}<span>米 / 秒</span>
							</p>
							<div>
								<span>风力风速</span>
								<p class="typhoon-singlet-line"></p>
							</div>
						</div>
						<div class="wind-speed-style"></div>
					</div>
					<!-- 移动速度 -->
					<div class="typhoon-information-sty" @click="AccordingSwitch('移动速度')">
						<div>
							<p>
								{{ particularsData.move_speed == null ? '-' : particularsData.move_speed }} <span>千米 / 小时</span>
							</p>
							<div>
								<span>移动速度</span>
								<p class="typhoon-singlet-line"></p>
							</div>
						</div>
						<div class="typhoon-movement-style"></div>
					</div>
				</div>
				<!-- tab表格 -->
				<div class="areal-rainfall-sty">
					<div>
						<i class="icon-diamond icon-sty" />
						<span>{{ echartName }}</span>
					</div>
				</div>
				<div id="windSpeed" style="width: 100%; height: 390px"></div>
			</div>
		</panel-item>
	</div>
</template>

<script>
import * as echarts from 'echarts';
import { mapActions, mapGetters } from 'vuex';
import PanelItem from '../components/PanelItem';
import CapsuleTabs from '../components/CapsuleTabs';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention';

export default {
	name: 'TyphoonModule',
	props: ['typhoonList', 'particularsVal'],
	components: {
		PanelItem,
		CapsuleTabs
	},
	data() {
		return {
			realtimePreChartTab: 0,
			realtimePreChartTabList: ['路径', '生成地', '季节'],
			sluiceTopArr: [],
			typhoonsType: '',
			historyTyphoonData: {},
			particularsData: {},
			speedList: [],
			powerList: [],
			timeList: [],
			JsonList: [],
			echartName: '风力风速',
			echartNameList: [],
			echartUnitList: []
		};
	},
	computed: {
		...mapGetters(['ScreenMap', 'currentSelectArea', 'currentScene'])
	},
	mounted() {
		this.$nextTick(() => {
			if (this.$store.state.particularsVal) {
				this.historyTyphoonData = this.$store.state.particularsVal;
				this.getTyphoonDetail();
			}
		});
	},
	watch: {
		'$store.state.typhoonData': function(val) {
			this.historyTyphoonData.name = val.name;
			this.historyTyphoonData.ename = val.englishName;
			this.historyTyphoonData.hours = val.hours;
			this.historyTyphoonData.id = val.id;
			this.getTyphoonDetail();
		},
		typhoonList(val) {
			this.particularsData = val;
		}
	},
	methods: {
		/**
		 * 图表切换
		 */
		AccordingSwitch(val) {
			this.echartName = val;
			this.speedList = [];
			this.powerList = [];
			this.timeList = [];
			if (val == '中心气压') {
				this.JsonList.forEach(val => {
					this.speedList.push(val.pressure);
					this.powerList.push(null);
					this.echartNameList = ['中心气压', ''];
					this.echartUnitList = ['气压(hPa)', ''];
					let timer = val.time.slice(0, 10) + ' ' + val.time.slice(11, 20);
					this.timeList.push(timer);
				});
				this.TyphoonWindSpeed();
			} else if (val == '移动速度') {
				this.JsonList.forEach(val => {
					this.speedList.push(val.move_speed);
					this.powerList.push(null);
					this.echartNameList = ['移动速度', ''];
					this.echartUnitList = ['km/h', ''];
					let timer = val.time.slice(0, 10) + ' ' + val.time.slice(11, 20);
					this.timeList.push(timer);
				});
				this.TyphoonWindSpeed();
			} else {
				this.JsonList.forEach(val => {
					this.speedList.push(val.speed);
					this.powerList.push(val.power);
					this.echartNameList = ['风速', '风力'];
					this.echartUnitList = ['风速(m/s)', '风力(级)'];
					let timer = val.time.slice(0, 10) + ' ' + val.time.slice(11, 20);
					this.timeList.push(timer);
				});
				this.TyphoonWindSpeed();
			}
		},
		/**
		 * 台风分析
		 */
		async getTyphoonDetail() {
			if (this.historyTyphoonData.id) {
				await disasterPreventionApi.getTyphoonDetail(this.historyTyphoonData.id).then(res => {
					if (res.code == 0) {
						this.JsonList = JSON.parse(res.data.points).reverse();
						this.JsonList.forEach(val => {
							this.speedList.push(val.speed);
							this.powerList.push(val.power);
							this.echartNameList = ['风速', '风力'];
							this.echartUnitList = ['风速(m/s)', '风力(级)'];
							let timer = val.time.slice(0, 10) + ' ' + val.time.slice(11, 20);
							this.timeList.push(timer);
						});
						this.particularsData = JSON.parse(res.data.points).slice(-1)[0];
						this.$nextTick(() => {
							this.TyphoonWindSpeed();
						});
						this.ScreenMap.showTyphoon(res.data);
					}
				});
			}
		},
		/**
		 * 风力风速
		 */
		TyphoonWindSpeed() {
			var chartDom = document.getElementById('windSpeed');
			var myChart = echarts.init(chartDom);
			var option;
			option = {
				tooltip: {
					trigger: 'axis',
					axisPointer: {
						type: 'cross',
						crossStyle: {
							color: '#999'
						}
					}
				},
				legend: {
					icon: 'line',
					textStyle: {
						color: '#ffffff'
					},
					data: this.echartNameList
				},
				grid: {
					top: '20%',
					left: '3%',
					right: '5%',
					bottom: '3%',
					containLabel: true
				},
				xAxis: [
					{
						type: 'category',
						rotate: '50',
						inverse: true,
						data: this.timeList,
						axisPointer: {
							type: 'shadow'
						},
						axisTick: {
							show: false
						},
						axisLabel: {
							show: true,
							textStyle: {
								fontSize: '18',
								color: 'rgba(255,255,255,1)' //这里用参数代替了
							},
							formatter: function(value) {
								var ret = ''; //拼接加\n返回的类目项
								var maxLength = 10; //每项显示文字个数
								var valLength = value.length; //X轴类目项的文字个数
								var rowN = Math.ceil(valLength / maxLength); //类目项需要换行的行数
								if (rowN > 1) {
									//如果类目项的文字大于3,
									for (var i = 0; i < rowN; i++) {
										var temp = ''; //每次截取的字符串
										var start = i * maxLength; //开始截取的位置
										var end = start + maxLength; //结束截取的位置
										//这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
										temp = value.substring(start, end) + '\n';
										ret += temp; //凭借最终的字符串
									}
									return ret;
								} else {
									return value;
								}
							}
						},
						axisLine: {
							position: 'top',
							lineStyle: {
								fontSize: 17,
								color: 'rgba(255,255,255,0.60)'
							}
						}
					}
				],
				yAxis: [
					{
						type: 'value',
						name: this.echartUnitList[0],
						position: 'top',
						nameLocation: 'start',
						min: function(value) {
							return value.min - 1;
						},
						max: function(value) {
							return value.max + 1;
						},
						position: 'top',
						inverse: true,
						axisLabel: {
							fontSize: 17,
							formatter: '{value} ml'
						},
						nameTextStyle: {
							color: 'rgba(255,255,255,0.60)',
							fontSize: 17,
							position: 'top'
						},
						axisLine: {
							show: false,
							position: 'top'
						},
						splitLine: {
							show: true,
							lineStyle: {
								fontSize: 17,
								position: 'top',
								color: 'rgba(255,255,255,0.60)' //这里用参数代替了
							}
						},
						//  去掉背景的网格线
						axisLabel: {
							show: true,
							textStyle: {
								fontSize: 17,
								color: 'rgba(255,255,255,0.6)' //这里用参数代替了
							}
						}
					},
					{
						type: 'value',
						name: this.echartUnitList[1],
						// nameLocation: "start",
						min: function(value) {
							return value.min - 1;
						},
						max: function(value) {
							return value.max + 1;
						},
						// inverse: true,
						position: 'top',
						axisLabel: {
							fontSize: 17,
							formatter: '{value} °C'
						},
						nameTextStyle: {
							fontSize: 17,
							color: 'rgba(255,255,255,0.60)'
						},
						axisLine: {
							show: false
						},
						splitLine: {
							show: false,
							lineStyle: {
								fontSize: 17,
								color: 'rgba(255,255,255,0.60)' //这里用参数代替了
							}
						},
						//  去掉背景的网格线
						axisLabel: {
							show: true,
							textStyle: {
								fontSize: 17,
								color: 'rgba(255,255,255,0.6)' //这里用参数代替了
							}
						}
					}
				],
				series: [
					{
						name: this.echartNameList[0],
						type: 'line',
						symbol: 'none',
						smooth: true,
						itemStyle: {
							normal: {
								color: '#eec80b',
								lineStyle: {
									fontSize: 17,
									color: '#eec80b'
								}
							}
						},
						data: this.speedList
					},
					{
						name: this.echartNameList[1],
						type: 'line',
						symbol: 'none',
						smooth: true,
						yAxisIndex: 1,
						itemStyle: {
							normal: {
								color: '#00f5ff',
								lineStyle: {
									fontSize: 17,
									color: '#00f5ff'
								}
							}
						},
						data: this.powerList
					}
				]
			};
			option && myChart.setOption(option);
		}
	}
};
</script>
<style lang="scss" scoped>
@import '../style/SildePanel.scss';
.small-module-sty {
	padding: 5px 0;
	.typhoon-name-sty {
		> p:nth-child(1) {
			font-size: 18px;
			color: #33ffff;
			text-align: left;
			line-height: 24px;
			font-weight: 500;
			> span {
				margin-left: 5px;
				font-size: 24px;
			}
		}
		> p {
			font-size: 14px;
			color: #ffffff;
			line-height: 24px;
			font-weight: 500;
			> span {
				margin-left: 5px;
				color: #33ffff;
				font-size: 20px;
			}
		}
	}
	::v-deep .el-table th {
		font-size: 15px;
		color: #1ef8f2;
	}
	::v-deep .el-table td {
		padding: 0;
		color: #fff;
		font-size: 14px;
	}
	::v-deep .el-table .el-checkbox__inner {
		border: 0.0625rem solid #00c1ff;
		background-color: #1ef8f2;
		background: rgba(0, 0, 0, 0);
	}
	::v-deep .el-table-column--selection .cell {
		padding-left: 8px;
		padding-right: 10px;
	}
	.icon-diamond {
		display: inline-block;
		height: 1.5rem;
		width: 1.5rem;
		background-image: url(~assets/images/WaterDisastersImage/diamond.gif);
	}
	// 台风详情内容样式
	.typhoon-details-sty {
		margin: 10px;
		padding: 10px 0;
		border-top: 1px solid rgba($color: #00c1ff, $alpha: 0.25);
		border-bottom: 1px solid rgba($color: #00c1ff, $alpha: 0.25);
		.typhoon-time-sty {
			display: flex;
			justify-content: left;
			> p {
				margin-right: 16px;
			}
			> div {
				> p:nth-child(1) {
					font-size: 24px;
					color: #00f5ff;
					line-height: 24px;
					font-weight: 500;
				}
				> p:nth-child(2) {
					padding-top: 5px;
					font-size: 14px;
					color: #ffffff;
					line-height: 24px;
					font-weight: 400;
				}
			}
		}
		.typhoon-distance-sty {
			> div {
				> p:nth-child(1) {
					> span {
						font-size: 14px;
						color: #ffffff;
						line-height: 24px;
						font-weight: 400;
					}
				}
			}
		}
		.windstorm {
			padding: 10px 0;
			border-bottom: 1px solid rgba($color: #00c1ff, $alpha: 0.25);
		}
		.typhoon-information-style {
			height: 80px;
			width: 100px;
			float: right;
			margin: 10px 0;
			color: #00f5ff;
			background: url(~assets/images/WaterDisastersImage/zhong-xin.png) no-repeat top center;
			background-size: 100% 100%;
		}
		.typhoon-pressure-style {
			height: 80px;
			width: 100px;
			float: right;
			color: #00f5ff;
			background: url(~assets/images/WaterDisastersImage/zhong-xin-qi-ya.png) no-repeat top center;
			background-size: 100% 100%;
		}
		.wind-speed-style {
			height: 80px;
			width: 100px;
			float: right;
			color: #00f5ff;
			background: url(~assets/images/WaterDisastersImage/fengshu-fengli.png) no-repeat top center;
			background-size: 100% 100%;
		}
		.typhoon-movement-style {
			height: 80px;
			width: 100px;
			float: right;
			color: #00f5ff;
			background: url(~assets/images/WaterDisastersImage/yi-dong-shu-du.png) no-repeat top center;
			background-size: 100% 100%;
		}
		.typhoon-information-sty {
			position: relative;
			display: flex;
			justify-content: space-between;
			> div {
				> p {
					position: absolute;
					top: 30px;
					left: 0;
					font-size: 24px;
					color: #1ff1f0;
					line-height: 24px;
					font-weight: 500;
					> span {
						font-size: 14px;
						color: #ffffff;
						line-height: 24px;
						font-weight: 400;
					}
				}
				> div {
					display: flex;
					margin-top: 50px;
					justify-content: space-between;
					> span {
						font-size: 14px;
						color: #ffffff;
						line-height: 24px;
						font-weight: 400;
						margin-top: 15px;
						margin-right: 16px;
					}
				}
			}
		}
		.typhoon-singlet-line {
			width: 15.625rem;
			height: 30px;
			background: url(~assets/images/WaterDisastersImage/tai-feng-dan-xian.png) no-repeat top center;
			background-size: 100% 100%;
		}
	}
	.areal-rainfall-sty {
		display: flex;
		justify-content: space-between; /* 横向中间自动空间 */
		> div:nth-child(1) {
			vertical-align: middle;
			> i {
				vertical-align: middle;
				margin-top: -6px;
				margin-right: 10px;
			}
			> span {
				font-size: 18px;
				color: #ffffff;
				line-height: 24px;
				font-weight: 500;
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
}
</style>
