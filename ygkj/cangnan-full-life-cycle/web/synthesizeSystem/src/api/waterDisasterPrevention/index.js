import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/big-screen";
const pre1 = "/graph";

//水利综合一张图
export const disasterPreventionApi = {
	//气象预报
	/**
	 * 天气预报
	 */
	weatherForecast: function(data) {
		return http.get(pre + "/floodDrought/weatherForecast", data);
	},
	/**
	 * 气象图表
	 */
	rainWindCurve: function(data) {
		return http.get(pre + "/floodDrought/rainWindCurve", data);
	},
	// 实时降雨量
	/**
	 * 实时降雨量
	 */
	rainfallSum: function(data) {
		return http.get(pre + "/floodDrought/rainfall", data);
	},
	// 降雨分析
	/**
	 * 单小时累计面雨量
	 */
	areaRainfallSingle: function(data) {
		return http.get(pre + "/floodDrought/predictRainfallStatistic", data);
	},
	/**
	 * 实时面雨量
	 */
	pastHoursRainfall: function(data) {
		return http.get(pre + "/floodDrought/pastHoursRainfall", data);
	},
	/**
	 * 单小时预报面雨量
	 */
	areaRainfallForecastSingle: function(data) {
		return http.get(pre + "/floodDrought/areaRainfallForecastSingle", data);
	},
	// 内涝分析
	/**
	 * 城市积水
	 */
	waterloggingList: function(data) {
		return http.get(pre + "/floodDrought/waterloggingList", data);
	},
	/**
	 * 河网水位
	 */
	innerRiverList: function(data) {
		return http.get(pre + "/floodDrought/innerRiverList", data);
	},
	//堤防c位
	getcAStatisticalProject: function(data) {
		return http.get("project-manage/newProjectManage/cAStatisticalProject", data);
	},
	// 堤防
	dikeproject: function(data) {
		return http.post("project-manage/newProjectManage/dikeProject", data);
	},
	// 泵站
	// 山塘
	//获取实时台风
	getRealTyphoon() {
		return http.get("/meteorological-visualization/typhoons/currentTyphoon");
	},
	//台风分析
	typhoonAnalysis: function(code, type) {
		return http.get(`/meteorological-visualization/typhoons/analysis?code=${code}&type=${type}`);
	},
	//台风查询
	typhoons: function(data) {
		return http.post("/meteorological-visualization/typhoons", data);
	},
	//获取台风详情
	getTyphoonDetail(id) {
		return http.get(pre1 + `/disasterPrevention/selectTyphoon/${id}`);
	},
	// 水库
	/**
	 * 水库列表
	 */
	reservoirList: function(data) {
		return http.get(pre + "/floodDrought/rsvrStats", data);
	},
	// 水闸
	/**
	 * 水闸列表
	 */
	wagaStList: function(data) {
		return http.get(pre + "/floodDrought/wagaStList", data);
	},
	/**
	 * 潮位列表
	 */
	tideStList: function(data) {
		return http.get(pre + "/floodDrought/tideStList", data);
	},

	/**
	 * 堤防灾害保险
	 */
	dikeInfo: function(data) {
		return http.get(pre + "/floodDrought/dikeAndSeawallLengthCount", data);
	},
	/**
	 * 工程分析
	 */
	projectCount: function(data) {
		return http.get(pre + "/floodDrought/projectCount", data);
	},
	/**
	 *
	 */
	attStBaseCountByType: function(data) {
		return http.get(pre + "/floodDrought/attStBaseCountByType", data);
	},
	/**
	 * 抢险物资和队伍
	 */
	emergencySupport: function(data) {
		return http.get(pre + "/floodDrought/emergencySupportCount", data);
	},
	/**
	 * 应急预案
	 */
	reservoirArchivesCount: function(data) {
		return http.get(pre + "/floodDrought/emergencySupportCount", data);
	},
	warnMapPoints: function(data) {
		return http.get(pre + "/waterDisasterPrevention/warnMapPoints", data);
	},
	/**
	 * 台风演练统计
	 */
	rainstormDrillStatistics: function(data) {
		return http.post(pre + "/waterDisasterPrevention/rainstormDrillStatistics", data);
	},

	//==================================================================
	// 第七版大屏接口 ref 水旱灾害大屏第七版左半部分接口
	//==================================================================

	/** 气象预报 */
	weatherWarning: function(data) {
		return http.get(pre + "/waterDisasterPrevention/weatherWarning", data);
	},
	/** 天气实况 */
	wholeWeather: function(data) {
		return http.get(pre + "/waterDisasterPrevention/wholeWeather", data);
	},
	/** 短临预报面雨量数据 */
	predictRainfallByAreaName: function(data) {
		return http.post(pre + "/waterDisasterPrevention/predictRainfallByAreaName", data);
	},
	/** 短临预报极值 */
	rainfallMaxValuePredict: function(data) {
		return http.post(pre + "/waterDisasterPrevention/rainfallMaxValuePredict", data);
	},
	/** 短临预报表格数据 */
	rainfallPredictGrid: function(data) {
		return http.post(pre + "/waterDisasterPrevention/rainfallPredictGrid", data);
	},
	/** 实时降雨面雨量数据 */
	realtimeRainfallByAreaName: function(data) {
		return http.get(pre + "/floodDrought/rainfall", data);
	},
	/** 实时降雨极值和图表数据 */
	rainfallMaxValueAndGridRealtime: function(data) {
		return http.post(pre + "/waterDisasterPrevention/grandTotalMaxRainfallStatistic", data);
	},
	/** 实时降雨极值和图表数据 */
	grandTotalReservoirRainfall: function(data) {
		return http.post(pre + "/waterDisasterPrevention/grandTotalReservoirRainfall", data);
	},
	/** 山洪预警数据 */
	floodWarning: function(data) {
		return http.get(pre + "/waterDisasterPrevention/floodWarning", data);
	},
	/** 山洪预警转移人口 */
	mountainTorrentBaseStats: function(data) {
		return http.get(pre + "/waterDisasterPrevention/mountainTorrentBaseStats", data);
	},
	//==================================================================
	// 防御复盘场景
	//==================================================================
	replayRainfallStation: function(data) {
		return http.post(pre + "/waterDisasterPrevention/judgeRainfallIntervalStation", data);
	},
	replayRainfallMax: function(data) {
		return http.post(pre + "/waterDisasterPrevention/customRainfallSurfaceStatistic", data);
	},
	replayRainfallSurface: function(data) {
		return http.post(pre + "/waterDisasterPrevention/customRainfallSurfaceData", data);
	},
	//台风列表
	getTyphoons: function(data) {
		return http.post("/meteorological-visualization/typhoons", data);
	},
	//根据复盘id删除收藏复盘
	delDuplicateCollection: function(data) {
		return http.get("/big-screen/duplicateCollection/del", data);
	},
	//获取复盘列表
	getDuplicateCollectionList: function(data) {
		return http.post("/big-screen/duplicateCollection/getList", data);
	},
	//新增或修改收藏复盘
	saveOrUpdateDuplicate: function(data) {
		return http.post("/big-screen/duplicateCollection/saveOrUpdate", data);
	},

	//拓扑图接口
	getHydrProjStaDiagram: function(data) {
		return http.get("/big-screen/waterDisasterPrevention/hydrProjStaDiagram", data);
	},
	listPrevTfInfo: function(data) {
		return http.get("/flood-protect/torrentialFlood/listPrevTfInfo", data);
	},
	updateDrillRainContourGisImg: function(data) {
		return http.postJson("/big-screen/waterDisasterPrevention/updateDrillRainContourGisImg", data);
	},
	clearFloodWarning: function() {
		return http.get("/big-screen/waterDisasterPrevention/clearFloodWarning");
	},
	//添加或更新一项大屏场景信息
	addOrUpdateScene: function(data) {
		return http.postJson("/big-screen/unitScene/addOrUpdateScene", data);
	},
	//添加或更新一项大屏组件信息
	addOrUpdateUnit: function(data) {
		return http.post("/big-screen/unitScene/addOrUpdateUnit", data);
	},
	//删除一项大屏场景信息
	deleteScene: function(id) {
		return http.delete("/big-screen/unitScene/deleteScene?id=" + id);
	},
	//删除一项大屏组件信息
	deleteUnit: function(id) {
		return http.delete("/big-screen/unitScene/deleteUnit?id=" + id);
	},
	//查询一项完整的大屏
	listFullBigScreen: function(data) {
		return http.get("/big-screen/unitScene/listFullBigScreen", data);
	},
	//查询大屏场景信息
	listScene: function(data) {
		return http.get("/big-screen/unitScene/listScene", data);
	},
	//查询大屏组件信息
	listUnit: function(data) {
		return http.get("/big-screen/unitScene/listUnit", data);
	},
	//设置场景为默认场景
	setSceneAsDefault: function(data) {
		return http.post("/big-screen/unitScene/setSceneAsDefault", data);
	},
	//各个大屏组件
	unitNumStatistic: function(data) {
		return http.get("/big-screen/unitScene/unitNumStatistic", data);
	},
	// 海塘报警统计
	seawallWarnStatistic: function(data) {
		return http.postJson(pre + "/waterDisasterPrevention/seawallWarnStatistic", data);
	},
	// 潮位报警统计
	tideWarnStatistic: function(data) {
		return http.postJson(pre + "/waterDisasterPrevention/tideWarnStatistic", data);
	},
	//获取演练方案接口
	listDrillCase: function(data) {
		return http.get("/big-screen/drill/listDrillCase", data);
	},
	//根据方案id获取affairId
	startDrillAffair: function(data) {
		return http.post("/big-screen/drill/startDrillAffair", data);
	},
	//退出演练
	closeDrillAffair: function(data) {
		return http.post("/big-screen/drill/closeDrillAffair", data);
	},

	// 三江流域调度C位
	getWaterSchedule4C: function(data) {
		return http.get(pre + "/waterDisasterPrevention/hydrProjStaDiagramStatistic", data);
	}
};
