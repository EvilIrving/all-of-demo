<template>
	<screen-silde-panel
		:class="[isBigScrren && 'is-big-screen']"
		position="right"
		top="8%"
		:height="height"
		@change="collapseChange"
	>
		<template #slot-panel>
			<!-- 右侧弹框 -->
			<div ref="collapseContent" class="collapse-panel__border">
				<!-- 第二版大中型水库 -->
				<right-second-edition @FloodForecasting="FloodForecasting" />
				<div class="collapse-panel__content" v-if="false">
					<panel-item title="水库" has-switch>
						<!-- 水库 标题右侧图标点击事件 -->
						<template #title-switch>
							<i
								title="地图落点"
								@click="handleReservoirChange('skbtn')"
								:class="[skbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
							/>
							<i title="查看详表" class="icon-liebiao icon-sty" @click="openDetailsDialog('水库')" />
							<i @click="goToAMap('水库')" title="一张图" class="icon-yizhangtu icon-sty" />
						</template>
						<!-- 水库总数  超汛限数 -->
						<div class="reservoir-sty">
							<p style="font-size:20px;">
								总数 <span> {{ reservoirData.allTotal }} </span> 座
							</p>
							<p style="font-size:20px;">
								预警 <span> {{ reservoirData.allOverLimit }} </span> 座
							</p>
						</div>
						<!-- 大中型水库详情 -->
						<!-- 水库总数量 -->
						<div class="area-style" @click="reservoirType('大中型')">
							<p><i class="icon-shuiku icon-sty"></i><span style="font-size:24px;">大中型</span></p>
							<p style="font-size:20px;">
								总数 <span> {{ reservoirData.lmTotal }} </span> 座
							</p>
							<p style="font-size:20px;">
								超汛限 <span> {{ reservoirData.lmOverLimit }} </span> 座
							</p>
						</div>
						<!-- 水库详情图表展示 -->
						<div class="diagram-sty">
							<el-row>
								<el-col :span="12" class="border-L">
									<echartInstrument :seriesData="seriesData"></echartInstrument>
									<div style="display: flex; justify-content: center;margin:-18px 0 5px 0;">
										<p class="storage-capacity"></p>
										<p
											style="
                        padding: 0 6px;
                        color: #1cfff5;
                        font-size: 20px;
                        font-weight: 500;
                      "
										>
											平均蓄水率
										</p>
										<p class="storage-capacity-sty"></p>
									</div>
								</el-col>
								<el-col :span="12">
									<div style="display: flex; justify-content: center;margin:0 0 10px 0;">
										<p class="storage-capacity"></p>
										<p
											style="
                        padding: 0 6px;
                        color: #1cfff5;
                        font-size: 20px;
                        font-weight: 500;"
										>
											纳蓄能力(mm)
										</p>
										<p class="storage-capacity-sty"></p>
									</div>
									<vue-seamless-scroll
										:data="reservoirDetails.storageList"
										class="border-R"
										:class-option="classOption"
									>
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
						<!-- 小(Ⅰ)型水库 -->
						<div class="area-style">
							<p><i class="icon-shuiku icon-sty"></i><span style="font-size:24px;">小（Ⅰ）型</span></p>
							<p style="font-size:20px;">
								总数 <span> {{ reservoirData.s1Total }} </span> 座
							</p>
							<p style="font-size:20px;">
								溢流 <span> {{ reservoirData.s1OverLimit }} </span> 座
							</p>
						</div>
						<!-- 小（II）型 -->
						<div class="area-style" style="margin-bottom: 10px">
							<p><i class="icon-shuiku icon-sty"></i><span style="font-size:24px;">小（Ⅱ）型</span></p>
							<p style="font-size:20px;">
								总数 <span> {{ reservoirData.s2Total }} </span> 座
							</p>
							<p style="font-size:20px;">
								溢流 <span> {{ reservoirData.s2OverLimit }} </span> 座
							</p>
						</div>
					</panel-item>
					<panel-item title="水闸" has-switch>
						<!-- 水闸 标题右侧图标点击事件 -->
						<template #title-switch>
							<i
								title="地图落点"
								@click="handleSluiceChange('szbtn')"
								:class="[szbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
							/>
							<i title="查看详表" class="icon-liebiao icon-sty" @click="openDetailsDialog('水闸')" />
							<i @click="goToAMap('水闸工情')" title="一张图" class="icon-yizhangtu icon-sty" />
						</template>
						<!-- 水闸 -->
						<div class="rainfall-style">
							<p>
								总数 <span> {{ wagaData.wagaProjNum }} </span> 个
							</p>
						</div>
						<div class="water-gate-sty">
							<!-- 大中型水库 -->
							<div class="area-style">
								<p>温瑞平</p>
								<p>
									总数 <span> {{ wagaData.wrpNum }} </span> 个
								</p>
								<p>
									<i style="margin-right: 10px" class="icon-kai-qi-shu icon-sty"></i>已开启
									<span> {{ wagaData.wrpOpenNum }} </span> 个
								</p>
							</div>
							<div class="area-style">
								<p>
									<i class="el-icon-time icon-sty"></i><span style="font-size:20px;">累计:</span>
									<el-input-number
										v-model="num"
										size="small"
										controls-position="right"
										:min="1"
										:max="72"
										@input="changeHours"
									/>
									<span style="font-size:20px;color:#fff;">小时</span>
								</p>
								<p style="font-size:20px;">
									泄洪总量 <span> {{ wagaData.drainage }} </span> 万m³
								</p>
							</div>
						</div>
					</panel-item>
					<panel-item title="河网" has-switch>
						<!-- 河网 标题右侧图标点击事件 -->
						<template #title-switch>
							<i
								title="地图落点"
								@click="handleRiverNet('hwbtn')"
								:class="[hwbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
							/>
							<i title="查看详表" class="icon-liebiao icon-sty" @click="openDetailsDialog('河网')" />
							<i @click="goToAMap('河道水位')" title="一张图" class="icon-yizhangtu icon-sty" />
						</template>
						<!-- 监测点占比展示 -->
						<div class="river-style">
							<div id="river" style="height: 250px"></div>
						</div>
					</panel-item>
				</div>
			</div>

			<!-- 右侧边栏第二板 -->
			<div class="collapse-panel__border" style="margin-left: 16px">
				<div class="collapse-panel__content">
					<!-- 潮位 -->
					<panel-item
						title="潮位"
						has-switch
						:action-tabs="['温州', '龙湾', '瑞安', '鳌江']"
						@change="handleEngineeringChange"
					>
						<!-- 潮位 标题右侧图标点击事件 -->
						<template #title-switch>
							<i
								@click="handleTideChange('cwbtn')"
								:class="[cwbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
							/>
							<i class="icon-liebiao icon-sty" @click="openDetailsDialog('潮位')" />
							<i @click="goToAMap('潮位')" class="icon-yizhangtu icon-sty" />
						</template>
						<!-- tab切换 -->
						<div class="sidebar-list">
							<el-carousel height="230px" trigger="click" arrow="never" :autoplay="false">
								<el-carousel-item style="height:215px;">
									<echartLine ref="lineChart" :extraOption="extraOption"></echartLine>
								</el-carousel-item>
								<el-carousel-item>
									<el-row>
										<el-col
											:span="8"
											v-for="(item, index) in astroTideData"
											class="sea-level-sty"
											:class="index == 1 ? 'sea-left-level-style' : ''"
											:key="index"
										>
											<div class="level-sty">
												<p>{{ item.date.slice(5, 10) }}</p>
												<p>{{ item.lunar }}</p>
											</div>
										</el-col>
									</el-row>
									<el-row class="sea-level-style">
										<el-col :span="8" class="sea-level-sty">
											<div class="sea-sty">
												<p>
													{{ astroTideData[0] && astroTideData[0].amMax.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-shang-sheng icon-sty" />{{ astroTideData[0] && astroTideData[0].amMax.tdz }}
													m
												</p>
											</div>
											<div class="sea-sty">
												<p>
													{{ astroTideData[0] && astroTideData[0].amMin.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-chaowei-xiajiang icon-sty" />{{
														astroTideData[0] && astroTideData[0].amMin.tdz
													}}
													m
												</p>
											</div>
										</el-col>
										<el-col :span="8" class="sea-level-sty sea-left-level-style">
											<div class="sea-sty">
												<p>
													{{ astroTideData[1] && astroTideData[1].amMax.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-shang-sheng icon-sty" />{{ astroTideData[1] && astroTideData[1].amMax.tdz }}
													m
												</p>
											</div>
											<div class="sea-sty">
												<p>
													{{ astroTideData[1] && astroTideData[1].amMin.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-chaowei-xiajiang icon-sty" />{{
														astroTideData[1] && astroTideData[1].amMin.tdz
													}}
													m
												</p>
											</div>
										</el-col>
										<el-col :span="8" class="sea-level-sty">
											<div class="sea-sty">
												<p>
													{{ astroTideData[2] && astroTideData[2].amMax.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-shang-sheng icon-sty" />{{ astroTideData[2] && astroTideData[2].amMax.tdz }}
													m
												</p>
											</div>
											<div class="sea-sty">
												<p>
													{{ astroTideData[2] && astroTideData[2].amMin.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-chaowei-xiajiang icon-sty" />{{
														astroTideData[2] && astroTideData[2].amMin.tdz
													}}
													m
												</p>
											</div>
										</el-col>
									</el-row>
									<el-row>
										<el-col :span="8" class="sea-level-sty">
											<div class="sea-sty">
												<p>
													{{ astroTideData[0] && astroTideData[0].pmMax.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-shang-sheng icon-sty" />{{ astroTideData[0] && astroTideData[0].pmMax.tdz }}
													m
												</p>
											</div>
											<div class="sea-sty">
												<p>
													{{ astroTideData[0] && astroTideData[0].pmMin.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-chaowei-xiajiang icon-sty" />{{
														astroTideData[0] && astroTideData[0].pmMin.tdz
													}}
													m
												</p>
											</div>
										</el-col>
										<el-col :span="8" class="sea-level-sty sea-left-level-style">
											<div class="sea-sty">
												<p>
													{{ astroTideData[1] && astroTideData[1].pmMax.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-shang-sheng icon-sty" />{{ astroTideData[1] && astroTideData[1].pmMax.tdz }}
													m
												</p>
											</div>
											<div class="sea-sty">
												<p>
													{{ astroTideData[1] && astroTideData[1].pmMin.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-chaowei-xiajiang icon-sty" />{{
														astroTideData[1] && astroTideData[1].pmMin.tdz
													}}
													m
												</p>
											</div>
										</el-col>
										<el-col :span="8" class="sea-level-sty">
											<div class="sea-sty">
												<p>
													{{ astroTideData[2] && astroTideData[2].pmMax.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-shang-sheng icon-sty" />{{ astroTideData[2] && astroTideData[2].pmMax.tdz }}
													m
												</p>
											</div>
											<div class="sea-sty">
												<p>
													{{ astroTideData[2] && astroTideData[2].pmMin.tm.slice(11, 16) }}
												</p>
												<p>
													<i class="icon-chaowei-xiajiang icon-sty" />
													<span>
														{{ astroTideData[2] && astroTideData[2].pmMin.tdz }}
													</span>
													m
												</p>
											</div>
										</el-col>
									</el-row>
								</el-carousel-item>
							</el-carousel>
						</div>
					</panel-item>
					<!-- 海塘 -->
					<panel-item title="海塘" has-switch>
						<!-- @change="seawallChange" -->
						<!-- 海塘 标题右侧图标点击事件 -->
						<template #title-switch>
							<i
								title="地图落点"
								@click="handleSeawallChange('htbtn')"
								:class="[htbtn == true ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
							/>
							<i class="icon-liebiao icon-sty" @click="openDetailsDialog('海塘')" />
							<!-- <i title="查看详表" class="icon-liebiao icon-sty" /> -->
							<i @click="goToAMap('海塘')" title="一张图" class="icon-yizhangtu icon-sty" />
						</template>
						<!-- 海塘总数图表详情 -->
						<div>
							<el-row>
								<el-col :span="12" class="hydrops-sty" style="padding:0; height: 190px;">
									<!-- <echartPie :seriesData="seawallDataList"></echartPie> -->
									<div id="seawallEchart" style="width:232px;height: 190px;"></div>
								</el-col>
								<el-col :span="12" class="hydrops-sty" style="margin-bottom: 10px">
									<div>
										<div v-for="(item, index) in seawallDataList" :key="index">
											<p>
												<i
													style="vertical-align: baseline;"
													:class="[
														'icon-sty',
														item.name == '100年以上'
															? 'icon-year-101'
															: item.name == '100年一遇'
															? 'icon-year-100'
															: item.name == '50年一遇'
															? 'icon-year-50'
															: item.name == '20年一遇'
															? 'icon-year-20'
															: item.name == '10年一遇'
															? 'icon-year-10'
															: 'icon-year-5'
													]"
												/>
												<span>{{ item.name }}</span>
											</p>
											<p>
												<span> {{ item.partLength }} </span>km
											</p>
										</div>
									</div>
								</el-col>
							</el-row>
						</div>
					</panel-item>
					<!-- 病险工程 -->
					<panel-item title="病险工程" has-switch>
						<!-- 病险工程 标题右侧图标点击事件 -->
						<template #title-switch>
							<i
								title="地图落点"
								@click="weakProjectChange('1')"
								:class="[count % 2 ? 'icon-yinzhang-active' : 'icon-yinzhang-1', 'icon-sty']"
							/>
							<i class="icon-liebiao icon-sty" @click="openDetailsDialog('病险工程')" />
							<!-- <i class="icon-yizhangtu icon-sty" /> -->
						</template>
						<el-row style="margin-top:10px;letter-spacing: 2px;">
							<el-col
								:span="11"
								v-for="(item, index) in dilapData"
								:key="index"
								:class="['weakness-sty', dangerouslyShow == item.key ? 'background-sty' : '']"
							>
								<div @click="dangerously(item)">
									<p>
										<i
											:class="[
												'icon-sty',
												item.key == '病险水库'
													? 'icon-bx-shui-ku'
													: item.key == '病险海塘'
													? 'icon-bx-hai-tang'
													: item.key == '病险水闸'
													? 'icon-bx-shui-zha'
													: 'icon-bx-shan-tang'
											]"
										/>
									</p>
									<div style="font-size:20px;">
										<p>{{ item.key }}</p>
										<p>
											<span> {{ item.value }} </span> {{ item.key == "病险海塘" ? "段" : "座" }}
										</p>
									</div>
								</div>
							</el-col>
						</el-row>
					</panel-item>
					<!-- 积水 -->
					<panel-item
						title="风险隐患"
						has-switch
						:action-tabs="['风险类型', '隐患点销号情况']"
						@change="potentialRisksChange"
					>
						<!-- 积水 标题右侧图标点击事件 -->
						<template #title-switch>
							<i class="icon-yinzhang-1 icon-sty" />
							<i class="icon-liebiao icon-sty" @click="openDetailsDialog('风险隐患')" />
							<i class="icon-yizhangtu icon-sty" />
						</template>
						<!-- 图标详情 -->
						<div>
							<el-row style="margin-top: 6px;">
								<el-col :span="12" class="hydrops-sty">
									<echartPie :seriesData="projRiskSeriesData" :extraOption="projRiskExtraOption"></echartPie>
								</el-col>
								<el-col :span="12" class="hydrops-sty" style="padding-top: 15px;">
									<div>
										<div v-for="(item, index) in projRiskSeriesData" :key="index">
											<p>
												<i
													style="vertical-align: baseline;"
													:class="[
														item.name == '隐患点'
															? 'icon-title-cheng-bg'
															: item.name == '高风险点'
															? 'icon-title-huang-bg'
															: 'icon-title-qinglan-bg',
														'icon-sty'
													]"
												/>{{ item.name }}
											</p>
											<p>
												<span> {{ item.value / 100 }} </span> %
											</p>
										</div>
									</div>
								</el-col>
							</el-row>
						</div>
					</panel-item>
				</div>
			</div>
		</template>
	</screen-silde-panel>
</template>

<script>
// echart插件
import * as echarts from "echarts";
// 侧边栏布局组件
import { ScreenSildePanel } from "@/components";
// 标题
import PanelItem from "../components/PanelItem";
//轮播组件
import vueSeamlessScroll from "vue-seamless-scroll";
// 积水饼图
import EchartPie from "../charts/echart_pie/EchartPie.vue";
// 折线图
import EchartLine from "../charts/echart_line/EchartLine.vue";
// api接口调用
import { disasterPreventionApi } from "@/api/RightSildePanel";
// 仪表盘图
import EchartInstrument from "../charts/echart_instrument/EchartInstrument.vue";
// 第二版大中型水库
import RightSecondEdition from "./RightSecondEdition.vue";
import { goAMap } from "@/views/water_disaster_custom/cards/utils/common";

export default {
	name: "RightSildePanel",
	props: {
		height: { type: [Number, String] }
	},
	components: {
		ScreenSildePanel,
		EchartInstrument,
		vueSeamlessScroll,
		RightSecondEdition,
		PanelItem,
		EchartPie,
		EchartLine
	},
	data() {
		return {
			projRiskExtraOption: {
				color: [
					{
						type: "linear",
						x: 0,
						y: 0,
						x2: 1,
						y2: 1,
						colorStops: [
							{
								offset: 0,
								color: "#FF7144" // 0% 处的颜色
							},
							{
								offset: 1,
								color: "rgba(0,0,0,0.7)" // 100% 处的颜色
							}
						],
						global: false // 缺省为 false
					},
					{
						type: "linear",
						x: 0,
						y: 0,
						x2: 1,
						y2: 1,
						colorStops: [
							{
								offset: 0,
								color: "#EEC80B" // 0% 处的颜色
							},
							{
								offset: 1,
								color: "rgba(0,0,0,0.7)" // 100% 处的颜色
							}
						],
						global: false // 缺省为 false
					},
					{
						type: "linear",
						x: 0,
						y: 0,
						x2: 1,
						y2: 1,
						colorStops: [
							{
								offset: 0,
								color: "#1CFFF5" // 0% 处的颜色
							},
							{
								offset: 1,
								color: "rgba(0,0,0,0.7)" // 100% 处的颜色
							}
						],
						global: false // 缺省为 false
					}
				]
			},
			//病险工程
			dangerouslyShow: "",
			//水库数据
			reservoirData: {},
			//水库二级数据详情
			reservoirDetails: {},
			//水库图表数据
			seriesData: [],
			//水闸统计数据
			wagaData: {},
			//累计泄洪时间
			num: 1,
			//河网数据
			riverNetworkData: {
				rnName: [],
				waterLevel: [],
				warningLevel: [],
				guaranteeLevel: []
			},
			//潮位图表数据
			seriesLineData: [],
			//潮位图表X轴数据
			extraOption: {},
			//未来三天潮位预测数据
			astroTideData: [],
			//海塘数据
			seawallData: {
				totalLength: "477.2"
			},
			//病险分析
			dilapData: [],
			//风险隐患
			projRiskData: {},
			//风险隐患饼图数据
			projRiskSeriesData: [
				{ value: 3000, name: "隐患点" },
				{ value: 400, name: "高风险点" },
				{ value: 2000, name: "薄弱点" }
			],
			//地图对象
			map: null,
			//水库列表
			reservoirList: [],
			//水闸列表
			wageStList: [],
			//潮位列表
			tideStList: [],
			//河网列表
			riverNetList: [],
			//海塘列表
			seawallList: [],
			//病险工程列表
			weakProjectList: [],
			customColors: ["#EEC80B", "#00C1FF", "#EEC80B", "#00C1FF", "#FF7144"],
			skbtn: false, //水库点位展示标记
			szbtn: false, //水闸点位展示标记
			cwbtn: false, //潮位点位展示标记
			hwbtn: false, //河网点位展示标记
			htbtn: false, //海塘点位展示标记
			bxbtn: false, //病险点位展示标记
			activeName: "1", //水库详情列表tab切换
			errorData: [], //河网柱状图统计数据
			// typeName: "段数", //海塘tab  切换默认选中数据
			//海塘曲线图数据
			seawallDataList: [
				{ value: 13, name: "100年一遇" },
				{ value: 54, name: "50年一遇" },
				{ value: 42, name: "20年一遇" },
				{ value: 19, name: "10年一遇" },
				{ value: 0, name: "10年以下" }
			],
			count: 2,
			riverChart: null
		};
	},
	computed: {
		isBigScrren() {
			return this.$route.query.large == "true";
		},
		classOption() {
			return {
				step: 0.2, // 数值越大速度滚动越快
				limitMoveNum: 2, // 开始无缝滚动的数据量 this.dataList.length
				hoverStop: true, // 是否开启鼠标悬停stop
				direction: 1, // 0向下 1向上 2向左 3向右
				openWatch: true, // 开启数据实时监控刷新dom
				singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
				singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
				waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
			};
		}
	},
	mounted() {
		this.$nextTick(() => {
			this.seawallProjStats(); //海塘数据接口
			this.riverNetworkStaStats(); //河网数据接口
			this.rsvrProjStats2("大中型");
			this.getRightMapPoints("水库");
			this.getRightMapPoints("水闸");
			this.getRightMapPoints("河网");
			this.getRightMapPoints("潮位");
			this.getRightMapPoints("海塘");
			this.getRightMapPoints("病险工程");
			this.rsvrProjStats1(); //水库一级数据接口
			this.tideCurve("温州"); //潮位数据接口
			this.astroTideTable("温州"); //未来三天潮位数据接口
			this.seawallEchart();
			this.projRiskStats("风险类型"); //分线隐患接口
			this.dilapProjStats(); //病险工程
			this.riverNetwork();
		});
	},
	methods: {
		/**
		 * 海塘图表
		 */
		seawallEchart() {
			var chartDom = document.getElementById("seawallEchart");
			var myChart = echarts.init(chartDom);
			var option;
			const color0 = {
				type: "linear",
				x: 0,
				y: 0,
				x2: 1,
				y2: 1,
				colorStops: [
					{
						offset: 0,
						color: "#00EBC7" // 0% 处的颜色
					},
					{
						offset: 1,
						color: "rgba(0,0,0,0.7)" // 100% 处的颜色
					}
				],
				global: false // 缺省为 false
			};
			const color1 = {
				type: "linear",
				x: 0,
				y: 0,
				x2: 1,
				y2: 1,
				colorStops: [
					{
						offset: 0,
						color: "#19AF47" // 0% 处的颜色
					},
					{
						offset: 1,
						color: "rgba(0,0,0,0.7)" // 100% 处的颜色
					}
				],
				global: false // 缺省为 false
			};
			const color2 = {
				type: "linear",
				x: 0,
				y: 0,
				x2: 1,
				y2: 1,
				colorStops: [
					{
						offset: 0,
						color: "#EEC80B" // 0% 处的颜色
					},
					{
						offset: 1,
						color: "rgba(0,0,0,0.7)" // 100% 处的颜色
					}
				],
				global: false // 缺省为 false
			};
			const color3 = {
				type: "linear",
				x: 0,
				y: 0,
				x2: 1,
				y2: 1,
				colorStops: [
					{
						offset: 0,
						color: "#E89100" // 0% 处的颜色
					},
					{
						offset: 1,
						color: "rgba(0,0,0,0.7)" // 100% 处的颜色
					}
				],
				global: false // 缺省为 false
			};
			const color4 = {
				type: "linear",
				x: 0,
				y: 0,
				x2: 1,
				y2: 1,
				colorStops: [
					{
						offset: 0,
						color: "#DD00DB" // 0% 处的颜色
					},
					{
						offset: 1,
						color: "rgba(0,0,0,0.7)" // 100% 处的颜色
					}
				],
				global: false // 缺省为 false
			};
			const color5 = {
				type: "linear",
				x: 0,
				y: 0,
				x2: 1,
				y2: 1,
				colorStops: [
					{
						offset: 0,
						color: "#E80000" // 0% 处的颜色
					},
					{
						offset: 1,
						color: "rgba(0,0,0,0.7)" // 100% 处的颜色
					}
				],
				global: false // 缺省为 false
			};
			option = {
				tooltip: {
					show: true,
					trigger: "item",
					formatter: function(data) {
						return "段数：" + data.data.value + "<br/>" + "长度占比 : " + data.data.lengthPercent + "%";
					},
					confine: true,
					extraCssText: "width:160px;height:50px;font-size:18px;"
				},
				series: [
					{
						type: "pie",
						radius: ["60%", "90%"],
						avoidLabelOverlap: false,
						label: {
							show: false,
							position: "center",
							normal: {
								// normal 是图形在默认状态下的样式
								show: true,
								position: "center",
								color: "#fff",
								fontSize: 18,
								formatter: "总长度\n" + this.seawallData.totalLength + "km" // {b}:数据名； {c}：数据值； {d}：百分比，可以自定义显示内容，
							}
						},
						labelLine: {
							show: false
						},
						data: this.seawallDataList,
						color: [color0, color1, color2, color3, color4, color5]
					}
				]
			};

			option && myChart.setOption(option);
		},
		/**
		 * 洪水预备
		 */
		FloodForecasting() {
			this.$emit("FloodForecasting");
		},
		//病险工程
		dangerously(val) {
			this.count = 2;
			if (val.key == this.dangerouslyShow) {
				this.dangerouslyShow = "";
				this.dangerouslyMapPoints({ type: "病险工程", dilapProjType: val.key });
				this.$emit("legend-change", "病险工程", {});
			} else {
				this.dangerouslyShow = val.key;
				this.dangerouslyMapPoints({ type: "病险工程", dilapProjType: val.key });
			}
		},
		/**
		 * 病险工程全部点位
		 */
		async weakProjectChange(btnType) {
			this.count++;
			if (this.count % 2) {
				this.dangerouslyShow = "病险工程";
				this.dangerouslyMapPoints({ type: "病险工程" });
			} else {
				this.dangerouslyShow = "";
				this.dangerouslyMapPoints({ type: "病险工程" });
				this.$emit("legend-change", "病险工程", {});
			}
		},
		/**
		 * 病险工程单独点位处理
		 */
		async dangerouslyMapPoints(type) {
			let res = await disasterPreventionApi.rightMapPoints(type);
			if (res.code == 0) {
				this.weakProjectList = res.data;
				this.handleWeakProjectChange("病险工程");
			}
		},
		/**
		 * 河网柱状图
		 */
		handleWindowResize() {
			if (!this.riverChart) return;
			this.riverChart.resize();
		},
		riverNetwork() {
			var chartDom = document.getElementById("river");
			this.riverChart = echarts.init(chartDom);
			var errorData = this.errorData;
			var warnArr = this.riverNetworkData.warningLevel;
			var guaranteeArr = this.riverNetworkData.guaranteeLevel;
			var option;
			function renderItemLow(params, api) {
				var xValue = api.value(0);
				var lowPoint = api.coord([xValue, api.value(1)]);
				var halfWidth = (api.size([1, 0])[0] * 0.6) / 2;
				var style = api.style({
					stroke: "#EEC80B",
					fill: null,
					lineDash: [5, 5]
				});
				return {
					type: "line",
					transition: ["shape"],
					shape: {
						x1: lowPoint[0] - halfWidth + 5,
						y1: lowPoint[1],
						x2: lowPoint[0] + halfWidth - 5,
						y2: lowPoint[1]
					},
					style: style
				};
			}
			function renderItemHigh(params, api) {
				var xValue = api.value(0);
				var highPoint = api.coord([xValue, api.value(2)]);
				var halfWidth = (api.size([1, 0])[0] * 0.6) / 2;
				var style2 = api.style({
					stroke: "#FF7144",
					fill: null,
					lineDash: [5, 5]
				});
				return {
					type: "line",
					transition: ["shape"],
					shape: {
						x1: highPoint[0] - halfWidth + 5,
						y1: highPoint[1],
						x2: highPoint[0] + halfWidth - 5,
						y2: highPoint[1]
					},
					style: style2
				};
			}
			let barData = [];
			let shadowArr = [];
			this.riverNetworkData.waterLevel.forEach((item, idx) => {
				let barPic = require("@/assets/images/bartop-normal.png");
				if (item > warnArr[idx]) {
					barPic = require("@/assets/images/bartop-warn.png");
				}
				if (item > guaranteeArr[idx]) {
					barPic = require("@/assets/images/bartop.png");
				}
				let obj = {
					value: item,
					warn: warnArr[idx],
					guarantee: guaranteeArr[idx],
					symbol: "image://" + barPic,
					symbolRepeat: true,
					symbolSize: ["30%", "20%"],
					symbolOffset: [0, 10],
					symbolMargin: "-48%",
					animationDelay: function(dataIndex, params) {
						return params.index * 10;
					}
				};
				barData.push(obj);
				shadowArr.push(3);
			});
			option = {
				grid: {
					top: 10,
					left: 20,
					right: 0,
					bottom: 35
				},
				xAxis: [
					{
						type: "category",
						axisLabel: {
							show: true,
							textStyle: {
								color: "#fff",
								fontSize: "20"
							},
							margin: 16
						},
						axisTick: {
							show: false
						},
						axisLine: {
							show: false
						},
						splitLine: {
							show: false
						},
						data: this.riverNetworkData.rnName
					}
				],
				yAxis: {
					type: "value",
					show: false
				},
				tooltip: {
					show: true,
					formatter: function(data) {
						let htmlText =
							`<p class="rivertooltip__item rivertooltip__item-error"><span>保证: </span><span>${data.data.guarantee}m</span></p>` +
							`<p class="rivertooltip__item rivertooltip__item-warn"><span>警戒: </span><span>${data.data.warn}m</span></p>` +
							`<p class="rivertooltip__item rivertooltip__item-normal"><span>实时: </span><span>${data.value}m</span></p>`;
						return htmlText;
					}
				},
				series: [
					{
						type: "custom",
						name: "警戒值",
						tooltip: {
							show: false
						},
						itemStyle: {
							normal: {
								borderWidth: 1.5
							}
						},
						label: {
							show: false,
							formatter: function(val) {
								return val.data[1];
							},
							position: ["-5%", "90%"],
							color: "#EEC80B",
							fontWeight: "bold",
							align: "right",
							fontSize: 16
						},
						renderItem: renderItemLow,
						encode: {
							x: 0,
							y: [1, 2]
						},
						data: errorData,
						z: 120
					},
					{
						type: "custom",
						name: "保证值",
						label: {
							show: false,
							formatter: function(val) {
								return val.data[2];
							},
							position: ["-5%", "-90%"],
							fontWeight: "bold",
							fontSize: 16,
							align: "right",
							color: "#FF7144"
						},
						tooltip: {
							show: false
						},
						itemStyle: {
							normal: {
								borderWidth: 1.5
							}
						},
						renderItem: renderItemHigh,
						encode: {
							x: 0,
							y: [1, 2]
						},
						data: errorData,
						z: 120
					},
					{
						type: "pictorialBar",
						name: "河网水位",
						hoverAnimation: true,
						label: {
							show: true,
							position: ["20%", "-10%"],
							fontSize: 18,
							color: "#fff"
						},
						showBackground: true,
						backgroundStyle: {
							color: "rgba(220, 220, 220, 0.8)"
						},
						data: barData
					}
				]
			};
			this.riverChart.setOption(option);
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
		 * 水库类型详情
		 */
		reservoirType(val) {
			this.rsvrProjStats2(val);
		},
		/**
		 * 水库二级数据详情
		 */
		async rsvrProjStats2(val) {
			this.seriesData = [];
			let obj = {
				scaleType: val
			};
			await disasterPreventionApi.rsvrProjStats2(obj).then(res => {
				if (res.code == 0) {
					let str = "平均蓄水率";
					this.seriesData.push(res.data.avgStorageRatio);
					this.reservoirDetails = res.data;
					this.$nextTick(() => {
						this.handleWindowResize();
					});
				}
			});
		},
		/**
		 * 累计泄洪时间
		 */
		changeHours() {
			this.wagaProjStats(this.num);
		},
		/**
		 * 水闸统计数据
		 */
		async wagaProjStats(val) {
			let obj = {
				hours: val
			};
			await disasterPreventionApi.wagaProjStats(obj).then(res => {
				if (res.code == 0) {
					this.wagaData = res.data;
				}
			});
		},
		/**
		 * 河网
		 */
		async riverNetworkStaStats() {
			await disasterPreventionApi.riverNetworkStaStats().then(res => {
				if (res.code == 0) {
					res.data.list.forEach((val, index) => {
						let arr = [];
						arr.push(index);
						arr.push(val.warningLevel);
						arr.push(val.guaranteeLevel);
						this.errorData.push(arr);
						this.riverNetworkData.rnName.push(val.rnName.slice(0, 2));
						this.riverNetworkData.waterLevel.push(val.waterLevel);
						this.riverNetworkData.warningLevel.push(val.warningLevel);
						this.riverNetworkData.guaranteeLevel.push(val.guaranteeLevel);
					});
					this.$nextTick(() => {
						this.riverNetwork();
					});
				}
			});
		},
		/**
		 * 修改进度条的值
		 */
		format(val) {
			return parseInt((val.waterLevel / val.guaranteeLevel).toFixed(2) * 100);
		},
		/**
		 * 潮位接口
		 */
		async tideCurve(val) {
			await disasterPreventionApi.tideCurve({ tideStaName: val }).then(res => {
				if (res.code == 0) {
					let arr = [];
					let obj = [];
					let warnTide = [];
					let xAxisArr = [];
					let series = [];
					this.extraOption = {};
					if (res.data.astroTide.length > 0) {
						res.data.astroTide.forEach(val => {
							if (val.tdz && val.tm) {
								arr.push(val.tdz);
								xAxisArr.push(val.tm.slice(5, 16));
							}
							warnTide.push(res.data.warnTide);
						});
						res.data.realTide.forEach(val => {
							obj.push(val.tdz);
						});
						let xAxis = [
							{
								axisLabel: {
									show: true,
									textStyle: {
										color: "#fff",
										fontSize: "18"
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
							name: "预报值",
							smooth: false, //关键点，为true是不支持虚线的，实线就用true
							itemStyle: {
								normal: {
									lineStyle: {
										width: 2,
										type: "dashed"
									}
								}
							}
						},
						{
							data: obj,
							type: "line",
							smooth: true,
							name: "实时值"
						},
						{
							data: warnTide,
							type: "line",
							smooth: true,
							name: "警戒值",
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
				}
			});
		},
		/**
		 * 处理潮位change事件
		 */
		handleEngineeringChange(index, type) {
			this.tideCurve(type);
			this.astroTideTable(type);
		},
		/**
		 * 海塘tab切换
		 */
		// seawallChange(index, type) {
		//   this.seawallProjStats(type);
		//   // this.typeName = type;
		// },
		/**
		 * 未来三天文潮表格
		 */
		async astroTideTable(val) {
			await disasterPreventionApi.astroTideTable({ tideStaName: val }).then(res => {
				if (res.code == 0) {
					this.astroTideData = res.data;
				}
			});
		},
		/**
		 * 海塘数据接口请求
		 */
		async seawallProjStats() {
			await disasterPreventionApi.seawallProjStats().then(res => {
				if (res.code == 0) {
					this.seawallData = res.data;
					let arr = [];
					res.data.list.forEach(val => {
						let obj = {};
						obj.value = val.partNum;
						obj.partLength = val.partLength;
						obj.lengthPercent = val.lengthPercent;
						obj.name =
							val.type == "lt10Year"
								? "10年以下"
								: val.type == "eq10Year"
								? "10年一遇"
								: val.type == "eq20Year"
								? "20年一遇"
								: val.type == "eq50Year"
								? "50年一遇"
								: val.type == "eq100Year"
								? "100年一遇"
								: "100年以上";
						arr.push(obj);
					});
					this.seawallDataList = arr.reverse();
					this.$nextTick(() => {
						this.seawallEchart();
					});
				}
			});
		},
		/**
		 * 风险隐患tab切换
		 */
		potentialRisksChange(index, type) {
			this.projRiskStats(type);
		},
		/**
		 * 风险隐患数据
		 */
		async projRiskStats(val) {
			await disasterPreventionApi.projRiskStats({ type: val }).then(res => {
				if (res.code == 0) {
					this.projRiskData = res.data;
					this.projRiskSeriesData = [
						{ value: res.data.hiddenTrouble * 100, name: "隐患点" },
						{ value: res.data.highRisk * 100, name: "高风险点" },
						{ value: res.data.weakSpot * 100, name: "薄弱点" }
					];
				}
			});
		},
		/**
		 * 病险工程
		 */
		async dilapProjStats() {
			await disasterPreventionApi.dilapProjStats().then(res => {
				if (res.code == 0) {
					this.dilapData = res.data;
				}
			});
		},
		/**
		 * 打开详情弹框点击事件
		 */
		openDetailsDialog(val) {
			this.$emit("open-analyse-dialog", val);
		},
		/**
		 * 水库点位开关
		 * @param {number} index 开关的序号
		 */
		handleReservoirChange(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				src: require("@/assets/images/legend-large-normal.png"),
				anchor: [12, 12],
				anchorXUnits: "pixels"
			};
			let pointList = [];
			for (let point of this.reservoirList) {
				pointList.push({
					name: point.resName,
					type: "水库",
					point,
					lng: point.lowLeftLong,
					lat: point.lowLeftLat,
					code: point.resCode,
					props: { 水位: `${point.waterLevel}m` }
				});
			}
			if (this[btnType]) {
				this.map.drawPoint(pointList, style, "reservoirLayer");
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
				this.map.drawPoint([], {}, "reservoirLayer");
				this.$emit("legend-change", "水库", {});
			}
		},
		/**
		 * 水闸点位开关
		 */
		handleSluiceChange(btnType) {
			this[btnType] = !this[btnType];
			let style = { src: require("@/assets/images/legend/zhamen1.png") };
			let pointList = [];
			for (let point of this.wageStList) {
				pointList.push({
					lng: point.startLong,
					lat: point.startLat,
					type: "水闸",
					code: point.wagaCode,
					name: point.wagaName,
					props: { 闸上水位: `${point.upz}m`, 闸下水位: `${point.dwz}m` }
				});
				// pointList.push({
				//   lng: point.stLong,
				//   lat: point.stLat,
				//   type: "水闸",
				//   code: point.projCode,
				//   name: point.stName,
				//   props: { 闸上水位: `${point.upz}m`, 闸下水位: `${point.dwz}m` },
				// });
			}
			if (this[btnType]) {
				this.map.drawPoint(pointList, style, "sluiceLayer");
				this.$emit("legend-change", "水闸", {
					label: "水闸",
					children: [
						{
							label: "正常",
							icon: "icon-zhamen1"
						},
						{
							label: "超警",
							icon: "icon-zhamen2"
						}
					]
				});
			} else {
				this.map.drawPoint([], {}, "sluiceLayer");
				this.$emit("legend-change", "水闸", {});
			}
		},
		/**
		 * 潮位点位开关
		 */
		handleTideChange(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				src: require("@/assets/images/legend/choawei.png"),
				anchor: [6, 6],
				anchorXUnits: "pixels"
			};
			let pointList = [];
			for (let point of this.tideStList) {
				pointList.push({
					lng: point.stLong,
					lat: point.stLat,
					type: "潮位",
					name: point.stName,
					props: { 潮位: `${point.tdz}m` }
				});
			}
			if (this[btnType]) {
				this.$emit("legend-change", "潮位", {
					label: "潮位",
					children: [{ label: "正常", icon: "icon-choawei" }]
				});
				this.map.drawPoint(pointList, style, "tideLayer");
			} else {
				this.map.drawPoint([], {}, "tideLayer");
				this.$emit("legend-change", "潮位", {});
			}
		},
		/**
		 * 河网点位开关
		 */
		handleRiverNet(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				src: require("@/assets/images/legend/legend-stagnant-normal.png"),
				anchor: [6, 6],
				anchorXUnits: "pixels"
			};
			let pointList = [];
			for (let point of this.riverNetList) {
				pointList.push({
					lng: point.longitude,
					lat: point.latitude,
					type: "河网",
					name: point.mainStName,
					props: { 水位: `${point.waterLevel}m` }
				});
			}
			if (this[btnType]) {
				this.$emit("legend-change", "河网", {
					label: "河网",
					children: [
						{ label: "正常", icon: "icon-legend-stagnant-normal" },
						{ label: "超警", icon: "icon-legend-stagnant-warn" }
					]
				});
				this.map.drawPoint(pointList, style, "riverNetLayer");
			} else {
				this.map.drawPoint([], {}, "riverNetLayer");
				this.$emit("legend-change", "河网", {});
			}
		},
		/**
		 * 海塘点位开关
		 */
		handleSeawallChange(btnType) {
			this[btnType] = !this[btnType];
			if (this[btnType]) {
				for (let seawall of this.seawallList) {
					let color = "#E80000";
					if (seawall.designTideStandard < 10) {
						color = "#E80000";
					} else if (seawall.designTideStandard == 10) {
						color = "#DD00DB";
					} else if (seawall.designTideStandard == 20) {
						color = "#E89100";
					} else if (seawall.designTideStandard == 50) {
						color = "#EEC80B";
					} else if (seawall.designTideStandard == 100) {
						color = "#19AF47";
					}
					let style = {
						lineWidth: 4,
						lineColor: color
					};
					let lineString = (seawall.coorpot && seawall.coorpot.replaceAll("&quot;", "'")) || "[]";
					let linelist = lineString && eval(lineString);
					linelist = linelist.map(line => {
						line = line.map((item, index) => {
							return [item.lng, item.lat];
						});
						return {
							lineList: line,
							data: { type: "海塘", name: seawall.seawallName }
						};
					});
					this.map.drawLineEx(linelist, style);
				}
				this.$emit("legend-change", "海塘", {
					label: "海塘",
					children: [
						{ label: "正常", icon: "icon-legend-dike-normal" },
						{ label: "病险", icon: "icon-legend-dike-abnormal" }
					]
				});
			} else {
				this.map.removeLineLayer();
				this.$emit("legend-change", "海塘", {});
			}
		},
		/**
		 * 病险点位开关
		 */
		handleWeakProjectChange(btnType) {
			this[btnType] = !this[btnType];
			let style = {
				src: require("@/assets/images/legend-dike-normal.png"),
				anchor: [12, 12],
				anchorXUnits: "pixels"
			};
			let pointList = [];
			for (let point of this.weakProjectList) {
				let imgSrc = require("@/assets/images/legend/bxsk.png");
				if (point.projectType == "水库") {
					imgSrc = require("@/assets/images/legend/bxsk.png");
				} else if (point.projectType == "海塘") {
					imgSrc = require("@/assets/images/legend/bxht.png");
				} else if (point.projectType == "山塘") {
					imgSrc = require("@/assets/images/legend/bxst.png");
				} else {
					imgSrc = require("@/assets/images/legend/bxsz.png");
				}
				pointList.push({
					lng: point.longitude,
					lat: point.latitude,
					type: point.projectType,
					name: point.projectName,
					src: imgSrc,
					code: point.projectCode,
					props: {}
				});
			}
			if (this.dangerouslyShow) {
				this.$emit("legend-change", "病险工程", {
					label: "病险工程",
					children: [
						{ label: "病险水库", icon: "icon-legend-bxsk" },
						{ label: "病险海塘", icon: "icon-legend-bxht" },
						{ label: "病险水闸", icon: "icon-legend-bxs" },
						{ label: "病险山塘", icon: "icon-legend-bxst" }
					]
				});
				this.map.drawPoint(pointList, style, "riverNetLayer");
			} else {
				this.map.drawPoint([], {}, "riverNetLayer");
				this.$emit("legend-change", "河网", {});
			}
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
				case "潮位":
					{
						this.tideStList = res.data;
					}
					break;
				case "海塘":
					{
						this.seawallList = res.data;
					}
					break;
				// case "病险工程":
				//   {
				//     this.weakProjectList = res.data;
				//   }
				//   break;
			}
		},
		/**
		 * 伸缩改变事件
		 */
		collapseChange(value) {
			this.$store.commit("ChangeRightCollapse", value);
		}
	}
};
</script>

<style lang="scss" scoped>
@import "../style/SildePanel.scss";
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
	margin: 10px 0;
	> p:nth-child(1) {
		margin-right: 16px;
		> span {
			color: #1cfff5;
		}
	}
	> p:nth-child(2) {
		background: url(~assets/images/WaterDisastersImage/bg-ji-shui.png) no-repeat top center;
		background-size: 100% 100%;
	}
}
.area-style {
	padding: 5px !important;
	// margin: 0;
	margin: 15px 0;
	height: 40px;
	> p:nth-child(1) {
		line-height: 1.8rem;
		font-size: 20px;
		color: #1cfff5;
		font-weight: 500;
		> i {
			vertical-align: text-top;
			margin-top: 2px;
			margin-right: 0;
		}
		> span {
			color: #1cfff5;
		}
	}
	> p:nth-child(2) {
		line-height: 25px;
		color: #fff;
		> span {
			font-size: 28px;
			color: #1ff1f0;
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
		}
	}
}
.diagram-sty {
	margin: 15px 0;
	color: #fff;
	.border-L {
		height: 140px;
		border-right: 1px solid rgba($color: #00c1ff, $alpha: 0.4);
	}
	::v-deep.border-R::-webkit-scrollbar {
		/*滚动条整体样式*/
		width: 0; /*高宽分别对应横竖滚动条的尺寸*/
	}
	.border-R {
		padding-left: 10px;
		height: 105px;
		overflow-y: auto;
		font-size: 20px;
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
	width: 20%;
	height: 2px;
}
.storage-capacity-sty {
	background-image: linear-gradient(64deg, #00c1ff 5%, rgba(0, 193, 255, 0) 100%);
	margin-top: 9px;
	width: 20%;
	height: 2px;
}
.rainfall-style {
	display: flex;
	justify-content: center;
	> p {
		font-size: 20px;
		> span {
			margin: 0 10px;
			color: #1cfff5;
			font-size: 28px;
		}
	}
}
.water-gate-sty {
	padding: 10px;
	margin-bottom: 10px;
	background: url(~assets/images/WaterDisastersImage/shui-zha.png) no-repeat top center;
	background-size: 100% 100%;
	> .area-style {
		background: none;
		margin: 10px 0;
		> p:nth-child(1) {
			font-size: 24px;
			// font-weight: 600;
		}
		> p:nth-child(2) {
			font-size: 20px;
		}
		> p:nth-child(3) {
			font-size: 20px;
		}
	}
	> div:nth-child(1) {
		border-bottom: 1px solid rgba($color: #1cfff5, $alpha: 0.2);
	}
	> div:nth-child(2) {
		> p:nth-child(1) {
			> i {
				font-size: 17px;
				line-height: 30px;
				margin: 0;
				color: #fff;
			}
			font-size: 16px;
			color: #fff;
			display: flex;
			justify-content: left;
			::v-deep.el-input {
				width: 80px;
				height: 40px;
			}
			::v-deep.el-input-number {
				position: relative;
				display: inline-block;
				width: 5.25rem;
				border: 1px solid #ffffff;
				border-radius: 4px;
			}
			::v-deep.el-input__inner {
				background: none;
			}
			::v-deep.el-input-number__decrease {
				border-left: 0.0625rem solid #fff;
				background: none;
				> i {
					color: #fff;
				}
			}
			::v-deep.el-input-number__increase {
				height: 14px;
				border-left: 0.0625rem solid #fff;
				background: none;
				> i {
					color: #fff;
				}
			}
		}
		> p:nth-child(2) {
			font-size: 16px;
			> span {
				padding: 0;
				margin: 0;
			}
		}
	}
}
.level-style {
	border-left: 1px solid #00c1ff;
	border-right: 1px solid #00c1ff;
}
.sea-level-sty {
	font-size: 16px;
	text-align: center;
	padding: 7px;
	> div:nth-child(2) {
		> p:nth-child(2) {
			color: #5dff30;
		}
	}
	.level-sty {
		font-family: PingFangSC-Regular;
		letter-spacing: 0;
		text-align: center;
		line-height: 24px;
		> p:nth-child(1) {
			font-size: 24px;
			color: #f4f8fd;
			font-weight: 500;
		}
		> p:nth-child(2) {
			font-size: 16px;
			color: #00fff4;
			font-weight: 400;
		}
	}
	.sea-sty {
		padding: 5px 0;
		font-size: 17px;
		display: flex;
		justify-content: space-between;
		> p:nth-child(2) {
			> i {
				vertical-align: baseline;
			}
			color: #00fff4;
		}
	}
}
.river-sty {
	> p {
		text-align: right;
		color: #00c1ff;
		font-size: 16px;
	}
}
.river-style {
	margin: 13px 0 0 0;
	font-size: 18px;
}
.seawall-sty {
	margin: 10px 0;
	color: #fff;
	font-size: 18px;
	> div {
		padding: 0 10px;
		display: flex;
		line-height: 30px;
		justify-content: center;
		justify-content: space-between;
	}
	> div:nth-child(1) {
		> p:nth-child(2) {
			> span {
				color: #00c1ff;
				font-size: 24px;
			}
		}
	}
	> div:nth-child(2) {
		> p:nth-child(2) {
			> span {
				color: #eec80b;
				font-size: 24px;
			}
		}
	}
}
.cc {
	border-right: 1px solid rgba($color: #00c1ff, $alpha: 0.4);
}
.hydrops-sty {
	height: 170px;
	color: #fff;
	font-size: 20px;
	font-weight: 400;
	> div {
		padding: 10px;
		> div {
			display: flex;
			padding: 0 0 5px 0;
			justify-content: space-between;
			> p:nth-child(2) {
				> span {
					color: #1cfff5;
				}
			}
		}
	}
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
}
.background-sty {
	background: url(~assets/images/WaterDisastersImage/bai-jing-style.png) no-repeat top center;
	background-size: 100% 100%;
}
.weakness-sty {
	padding: 10px 0;
	margin: 10px 0;
	> div:nth-child(1) {
		font-size: 16px;
		color: #ffffff;
		line-height: 24px;
		display: flex;
		justify-content: center;
		> div {
			> p {
				> span {
					font-size: 20px;
					color: #eec80b;
					font-weight: 600;
				}
			}
		}
		> p {
			> i {
				margin-top: 5px;
				margin-right: 10px;
			}
		}
	}
}
.water-sty {
	margin-left: 10px;
	display: flex;
	justify-content: space-between;
	> span:nth-child(1) {
		color: #1cfff5;
	}
	> span:nth-child(2) {
		color: #eec80b;
	}
	> span:nth-child(3) {
		color: #ff7144;
	}
}
.sea-level-style {
	border-top: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
	border-bottom: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
}
.sea-left-level-style {
	border-left: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
	border-right: 1px solid rgba($color: #00c1ff, $alpha: 0.5) !important;
}
</style>
