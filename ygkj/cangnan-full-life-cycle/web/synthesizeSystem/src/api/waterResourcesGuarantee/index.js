import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/big-screen";
const pre1 = "/wra";
const pre4 = "/water-area";
const pre2 = "/river-service";

// 水资源保障
export const waterResourceApi = {
	// 根据id查询河段详情
	rivSegInfoById: function (data) {
		return http.get(pre2 + "/riverInfoV2/rivSegInfoById", data);
	},
	// 巡查分页查找
	WsPatrolInfo: function (data) {
		return http.post(pre4 + "/WsPatrolInfo/loadPage", data);
	},
	// 分页查找上面的统计
	stats4LoadPage: function (data) {
		return http.get(pre4 + "/WsPatrolInfo/stats4LoadPage", data);
	},
	// 详表统计  对分页查询河段基础信息进行统计
	pageRivSegStats: function (data) {
		return http.get(pre2 + "/riverInfoV2/pageRivSegStats", data);
	},
	// 河道地图落点
	pageRivSegBaseInfo: function (data) {
		return http.get(pre2 + "/riverInfoV2/pageRivSegBaseInfo", data);
	},
	// 河流分布——河流分析按行政区划统计
	rivSegStaticByArea: function (data) {
		return http.get(pre2 + "/riverInfoV2/rivSegStaticByArea", data);
	},
	// 河流分布——河流分析按河段等级统计
	rivSegStaticByAreaLevel: function (data) {
		return http.get(pre2 + "/riverInfoV2/rivSegStaticByAreaLevel", data);
	},
	// 河流分布——河流分析按健康分析统计
	rivSegStaticByHealthState: function (data) {
		return http.get(pre2 + "/riverInfoV2/rivSegStaticByHealthState", data);
	},
	// 河流分布——C位统计
	rivSegCStatic: function (data) {
		return http.get(pre2 + "/riverInfoV2/rivSegCStatic", data);
	},

	
	/**
	 * 水资源公报 
	 */
	// 降水量统计
	stats4WrbRainfall: function (data) {
		return http.get(pre + "/waterGuarantee/stats4WrbRainfall", data);
	},
	// 降水量统计 C位
	statsCwindow4WrbRainfall: function (data) {
		return http.get(pre + "/waterGuarantee/statsCwindow4WrbRainfall", data);
	},
	// 降水量统计 详情
	table4WrbRainfall: function (data) {
		return http.get(pre + "/waterGuarantee/table4WrbRainfall", data);
	},
	// 水资源统计
	stats4WrbWra: function (data) {
		return http.get(pre + "/waterGuarantee/stats4WrbWra", data);
	},
	// 水资源统计 C位
	statsCwindow4WrbWra: function (data) {
		return http.get(pre + "/waterGuarantee/statsCwindow4WrbWra", data);
	},
	// 水资源统计 详情
	table4WrbWra: function (data) {
		return http.get(pre + "/waterGuarantee/table4WrbWra", data);
	},
	// 供水量统计
	stats4WrbWaterSupply: function (data) {
		return http.get(pre + "/waterGuarantee/stats4WrbWaterSupply", data);
	},
	// 供水量统计 C位
	statsCwindow4WrbWaterSupply: function (data) {
		return http.get(pre + "/waterGuarantee/statsCwindow4WrbWaterSupply", data);
	},
	// 供水量统计 详情
	table4WrbWaterSupply: function (data) {
		return http.get(pre + "/waterGuarantee/table4WrbWaterSupply", data);
	},
	// 用水量统计
	stats4WrbWaterConsume: function (data) {
		return http.get(pre + "/waterGuarantee/stats4WrbWaterConsume", data);
	},
	// 用水量统计 C位
	statsCwindow4WrbWaterConsume: function (data) {
		return http.get(pre + "/waterGuarantee/statsCwindow4WrbWaterConsume", data);
	},
	// 用水量统计 详情
	table4WrbWaterConsume: function (data) {
		return http.get(pre + "/waterGuarantee/table4WrbWaterConsume", data);
	},
	// 水库蓄水统计
	stats4WrbRsvrStore: function (data) {
		return http.get(pre + "/waterGuarantee/stats4WrbRsvrStore", data);
	},
	// 水库蓄水 C位
	statsCwindow4WrbRsvrStore: function (data) {
		return http.get(pre + "/waterGuarantee/statsCwindow4WrbRsvrStore", data);
	},
	// 水库蓄水 详情
	table4WrbRsvrStore: function (data) {
		return http.get(pre + "/waterGuarantee/table4WrbRsvrStore", data);
	},
	// 水库蓄水 落点弹窗
	curve4WrbRsvrStore: function (data) {
		return http.get(pre + "/waterGuarantee/curve4WrbRsvrStore", data);
	},

	/**
	 * 水资源承载能力
	 */
	// 水量评价 水质评价 经济评价 人口评价 用水评价 统计
	stats4WraUnit: function (data) {
		return http.get(pre + "/waterGuarantee/stats4WraUnit", data);
	},
	// C位
	statsCwindow4WraUnit: function (data) {
		return http.get(pre + "/waterGuarantee/statsCwindow4WraUnit", data);
	},
	// 水量评价 详表
	waterConsumeEvaluation: function (data) {
		return http.get(pre1 + "/waterAbilityEva/waterConsumeEvaluation", data);
	},
	// 水质评价 详表
	waterQualityEvaluation: function (data) {
		return http.get(pre1 + "/waterAbilityEva/waterQualityEvaluation", data);
	},
	// 经济评价 详表
	ecoAppraisalTable: function (data) {
		return http.get(pre1 + "/waterAbilityEva/ecoAppraisalTable", data);
	},
	// 人口评价 详表
	popAppraisalTable: function (data) {
		return http.get(pre1 + "/waterAbilityEva/popAppraisalTable", data);
	},

	// 双指数模型
	watGdpPreCurveByDouExpoModel: function (data) {
		return http.get(pre1 + "/waterAbilityEva/watGdpPreCurveByDouExpoModel", data);
	},
	//流域生态流量组件统计以及C位窗口
	stats4SectFlow: function (data) {
		return http.get(pre + "/waterGuarantee/stats4SectFlow", data);
	},
	//查询断面流量列表
	getSectFlowList: function (data) {
		return http.get("/hydrology-digitization/sectFlow/getSectFlowList", data);
	},
	//断面流量曲线
	getSectFlowCurve: function (data) {
		return http.get("/hydrology-digitization/sectFlow/sectFlowCurve", data);
	},
	//断面流量统计
	sectFlowStats: function (data) {
		return http.get("/hydrology-digitization/sectFlow/sectFlowStats", data);
	},

	// 趋势模型
	watGdpPreCurveByTrendPreModel: function (data) {
		return http.get(pre1 + "/waterAbilityEva/watGdpPreCurveByTrendPreModel", data);
	},


	/**
	 * 数字珊溪
	 */
	// C位
	processStatisticss: function (data) {
		return http.get(pre + "/waterProtection/patrolInfoProcessStatisticss", data);
	},
	// 总览
	digitalShanXiOverview: function (data) {
		return http.get(pre + "/waterDisasterPrevention/digitalShanXiOverview", data);
	},
	// 巡查 详表
	happeningListJson: function (data) {
		return http.post(pre + "/waterProtection/happeningListJson", data);
	},
	// 巡查 详表 统计
	patrolInfoPatrolChaosHappStatic: function () {
		return http.get(pre + "/waterProtection/patrolInfoPatrolChaosHappStatic");
	},
	// 巡查 行政区划
	patrolChaosHappStaticByAreaCode: function () {
		return http.get(pre + "/waterProtection/patrolChaosHappStaticByAreaCode");
	},
	// 巡查 乱点分析
	patrolChaosHappStaticByChaosType: function () {
		return http.get(pre + "/waterProtection/patrolChaosHappStaticByChaosType");
	},
	// 巡查 处置状态
	patrolChaosHappStaticByChaosStatus: function () {
		return http.get(pre + "/waterProtection/patrolChaosHappStaticByChaosStatus");
	},


	/**
	 * 水域变化
	 */
	// C位
	cPositionStatisticsWaterArea: function () {
		return http.get("river-service/waterAreaContr/cPositionStatisticsWaterArea");
	},
	// 水域面积 柱状图
	histogramWaterArea: function () {
		return http.get("river-service/waterAreaContr/histogramWaterArea");
	},
	// 水域面积 总数统计
	overallStatisticsWaterArea: function (data) {
		return http.get("river-service/waterAreaContr/overallStatisticsWaterArea", data);
	},
	// 水域占补 总数统计
	statisticsReclaimedAreaWater: function (data) {
		return http.get("river-service/AttWaterareaCompensation/statisticsReclaimedAreaWater", data);
	},
	// 水域占补 柱状图
	statisticsReclaimedByYear: function (data) {
		return http.get("river-service/AttWaterareaCompensation/statisticsReclaimedByYear", data);
	},
	// 详表
	loadPage: function (data) {
		return http.post("river-service/waterAreaContr/loadPage", data);
	},
	// 水域面积 落点
	waterAreaEachAdministrative: function (data) {
		return http.get("river-service/waterAreaContr/waterAreaEachAdministrative", data);
	},
	// 水域占补 落点
	waterCompensationEachAdministrative: function (data) {
		return http.get("river-service/AttWaterareaCompensation/waterCompensationEachAdministrative", data);
	},

	/**
	 * 取水户
	 */
	// C位
	wiuUnit4CWindow: function () {
		return http.get(pre + "/waterGuarantee/wiuUnit4CWindow");
	},
	//根据行政区划获取到的取用水量已达到或控制指标的地区，进行评估与预警
	wiuWarnByAdcd: function(){
		return http.get("water-saving/wiu/wiuWarnByAdcd");
	},

	// 取水户统计
	wiuStats: function (data) {
		return http.get(pre + "/waterGuarantee/wiuStats", data);
	},
	// 取水监管许可统计
	intakeSuperviseStats: function (data) {
		return http.get(pre + "/waterGuarantee/intakeSuperviseStats", data);
	},
	// 详表
	listWiu: function (data) {
		return http.get("/water-saving/wiu/listWiu", data);
	},
	// 详表 统计
	stat4ListWiu: function () {
		return http.get("water-saving/wiu/stat4ListWiu");
	},
	// 取水量列表
	listUseByMonth: function (data) {
		return http.post("water-saving/fetchWater/listUseByMonth", data);
	},
	// 取水许可
	currentWptOfWiu: function (data) {
		return http.get("water-saving/fetchWater/currentWptOfWiu", data);
	},
	// 档案
	wiuDocList4Config: function (data) {
		return http.post("water-saving/busWiuDocConfig/wiuDocList4Config", data);
	},


	/**
	 * 清四乱
	 */
	// C位
	statisProblemC: function () {
		return http.get("water-area/WsPatrolProblem/statisProblemC");
	},
	// 统计图数据
	statisProblemByArea: function () {
		return http.get("water-area/WsPatrolProblem/statisProblemByArea");
	},
	// 详表
	loadPageFourChaos: function (data) {
		return http.post("water-area/WsPatrolProblem/loadPage", data);
	},

	/**
	 * 批后监管
	 */
	// C位
	homeOverView: function () {
		return http.get("river-service/blueProj/homeOverView");
	},
	// 统计图数据 流域
	projectStatisByBasin: function () {
		return http.get("river-service/blueProj/projectStatisByBasin");
	},
	// 统计图数据 河道
	projectStatisByRiverLevel: function () {
		return http.get("river-service/blueProj/projectStatisByRiverLevel");
	},
	// 统计图数据 行政区划
	projectStatisByAdcd: function () {
		return http.get("river-service/blueProj/projectStatisByAdcd");
	},
	// 统计图数据 项目阶段
	projectStatisByProjectStage: function () {
		return http.get("river-service/blueProj/projectStatisByProjectStage");
	},
	// 详表
	projectList: function (data) {
		return http.post("river-service/blueProj/projectList", data);
	},

	/**
	 * 美丽河湖
	 */
	// C位
	projectStatic: function (data) {
		return http.get("river-service/AttBeautLakes/projectStatic", data);
	},
	// 详表 统计
	eachProjectStatistics: function (data) {
		return http.get("river-service/AttBeautLakes/eachProjectStatistics", data);
	},
	// 详表 美丽河湖
	getBeautifulList: function (data) {
		return http.post("river-service/AttBeautLakes/loadPage", data);
	},
	// 详表 中小河流
	AttSmallMediumRiver: function (data) {
		return http.post("river-service/AttSmallMediumRiver/loadPage", data);
	},
	// 详表 水美乡镇
	AttBeautVillage: function (data) {
		return http.post("river-service/AttBeautVillage/loadPage", data);
	},
	// 详表 农村池塘
	AttRuralPond: function (data) {
		return http.post("river-service/AttRuralPond/loadPage", data);
	},


	// 水源保障C位
	rsvrWatSrc4CWindow: function (data) {
		return http.get(pre + "/waterGuarantee/rsvrWatSrc4CWindow", data);
	},
	// 水源保障列表和柱状图
	waterGuaranteeRsvrWatSrcTable: function (data) {
		return http.get(pre + "/waterGuarantee/rsvrWatSrcTable", data);
	},
	// 用水效率C位
	wpgEffect4CWindow: function (data) {
		return http.get(pre + "/waterGuarantee/wpgEffect4CWindow", data);
	},
	// 用水效率列表&柱状图
	wpgEffect: function (data) {
		return http.get(pre + "/waterGuarantee/wpgEffect", data);
	},
	// 三条红线用水总量
	redLineWatCsm: function (data) {
		return http.get(pre + "/waterGuarantee/redLineWatCsm", data);
	},
	// 三条红线用水效率
	redLineWpgEffect: function (data) {
		return http.get(pre + "/waterGuarantee/redLineWpgEffect", data);
	},
	// 三条红线C位
	redLine4CWindow: function (data) {
		return http.get(pre + "/waterGuarantee/redLine4CWindow", data);
	},
	// 节水指标曲线
	watSavNormByYearInterval: function (data) {
		return http.get(pre + "/waterGuarantee/watSavNormByYearInterval", data);
	},
	// 节水指标落点&列表
	watSavNormByYear: function (data) {
		return http.get(pre + "/waterGuarantee/watSavNormByYear", data);
	},
	// 节水指标C位
	watSavNorm4CWindow: function (data) {
		return http.get(pre + "/waterGuarantee/watSavNorm4CWindow", data);
	},
	// 取水监管可证统计
	// intakeSuperviseStats: function (data) {
	// 	return http.get(pre + "/waterGuarantee/intakeSuperviseStats", data);
	// },
	// 取水监管C位
	intakeSupervise4CWindow: function (data) {
		return http.get(pre + "/waterGuarantee/intakeSupervise4CWindow", data);
	},
	// 取水接管列表
	getWptList: function (data) {
		return http.get("water-saving/wpt/list", data);
	},
	// 查询最新的取水口监测列表
	wainMonitor: function (data) {
		return http.get("/water-saving/wainMonitor/list", data);
	},
	// 查询取水口监测曲线
	intakeCurve: function (data) {
		return http.get("/water-saving/wainMonitor/intakeCurve", data);
	},


	/**
	 * 水电站
	 */
	// C位
	mainTotalNum: function (data) {
		return http.get(pre + "/hystComponent/mainTotalNum", data);
	},
	// 排行
	rateOrderByMoon: function (data) {
		return http.get(pre + "/hystComponent/rateOrderByMoon", data);
	},
	// 水电站数量统计
	countByType: function (data) {
		return http.get(pre + "/hystComponent/countByType", data);
	},
	// 详表
	hystList: function (data) {
		return http.post(pre + "/hystComponent/hystList", data);
	},
	// 弹窗 水电站详情
	hystDetail: function (data) {
		return http.get(pre + "/hystComponent/detail", data);
	},
	// 落点
	hystDropPoint: function (data) {
		return http.get(pre + "/hystComponent/dropPoint", data);
	},
	// 水电站详情
	hystPointDetail: function (data) {
		return http.get(pre + "/hystComponent/detail", data);
	},
	// 生态流量曲线
	getFlowCurve: function (data) {
		return http.post(pre + "/hystComponent/getFlowCurve", data);
	},


	/**
	 * 河长巡查
	 */
	// C位 饼图统计
	chiefPatrolProblemStats: function (data) {
		return http.get("/big-screen/riverLakeProtect/chiefPatrolProblemStats", data);
	},
	// 详表统计
	statsByQueryCondition: function (data) {
		return http.get("/river-service/chiefPatrol/statsByQueryCondition", data);
	},
	// 详表 落点
	queryPatrolProblem: function (data) {
		return http.get("/river-service/chiefPatrol/queryPatrolProblem", data);
	},
	/**
	 * 水厂供水
	 */
	 
	//  水厂统计
	waterWorksStats: function (data) {
		return http.get("project-manage/waterWorks/waterWorksStats", data);
	},
	// 查询水厂列表
	listWaterWorks: function (data) {
		return http.get("project-manage/waterWorks/listWaterWorks", data);
	},
	// 水厂水量水质曲线
	waterWorksStateCurve: function (data) {
		return http.get("project-manage/waterWorks/waterWorksStateCurve", data);
	},
	// 查询水厂列表表头统计
	stats4listWaterWorks: function (data) {
		return http.get("project-manage/waterWorks/stats4listWaterWorks", data);
	},
	
	/**
	 * 水源供水
	 */
	// 水源地统计 按规模
	statisSwhsNumByScale: function (data) {
		return http.get("river-service/AttSwhsBase/statisSwhsNumByScale", data);
	},
	// 水源地统计 按类型
	statisSwhsNumByType: function (data) {
		return http.get("river-service/AttSwhsBase/statisSwhsNumByType", data);
	},
	// 水源地统计 行政区划
	statisSwhsNumByArea: function (data) {
		return http.get("river-service/AttSwhsBase/statisSwhsNumByArea", data);
	},
	// 组件-水量预警等级数量统计（数量OR人口）
	statisSwarnGradeNum: function (data) {
		return http.get("river-service/AttSwhsBase/statisSwarnGradeNum", data);
	},
	// 组件-水量预警等级数量按行政区划统计
	statisSwarnGradeByAdcd: function (data) {
		return http.get("river-service/AttSwhsBase/statisSwarnGradeByAdcd", data);
	},
	// 组件-C位统计
	statisC: function (data) {
		return http.get("river-service/AttSwhsBase/statisC", data);
	},
	// 分页查找
	AttSwhsBaseLoadPage: function (data) {
		return http.post("river-service/AttSwhsBase/loadPage", data);
	},
	// 水源地,预警数量统计
	waterSuantityStatistic: function (data) {
		return http.post("river-service/AttSwhsBase/waterSuantityStatistic", data);
	},

	// 前期影响雨量列表
	precedeAffectRainfall: function (data) {
		return http.get("/graph/waterAndRain/precedeAffectRainfall", data);
	},
	// 前期影响雨量C位
	precedeAffectRainfall4CWindow: function (data) {
		return http.get("/graph/waterAndRain/precedeAffectRainfall4CWindow", data);
	},
	// 前期影响雨量曲线
	precedeAffectRainfallCurve: function (data) {
		return http.get("/graph/waterAndRain/precedeAffectRainfallCurve", data);
	},

};
