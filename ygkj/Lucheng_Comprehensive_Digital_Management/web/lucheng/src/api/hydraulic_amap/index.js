import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/graph";

//水利综合一张图
export const hydraulicApi = {
	// 获取行政区划
	getArea(data) {
		return http.post("/graph/adcd", data);
	},
	// 测站展示接口
	rainfallHistoryMax(data) {
		return http.post("/graph/waterAndRain/rainfallHistoryMax", data);
	},
	// 获取流域
	getBasin() {
		return http.get("/graph/basin");
	},
	//工程数量统计
	countProjectNumber: function () {
		return http.get("/project-manage/projectManage/countProjectNumber");
	},

	// 水位测站列表
	waterStations: function (data) {
		return http.post(pre + "/waterAndRain/waterStations", data);
	},
	//水情弹框
	waterStationInfo: function (data) {
		return http.post(pre + "/waterAndRain/waterLevels", data);
	},
	// 雨量测站列表
	rainStations: function (data) {
		return http.post(pre + "/waterAndRain/rainfall", data);
	},
	// 雨量弹框
	rainStationInfo: function (data) {
		return http.post(pre + "/waterAndRain/hourRainfall", data);
	},
	// 工程列表
	getProjects: function (data) {
		return http.post("/project-manage/projectManage/projects", data);
	},
	// 水质列表
	waterQualityStation: (data) => {
		return http.get(pre + "/waterQuality/list", data);
	},
	// 积水列表
	getLogging: function (data) {
		return http.get("/flood-protect/waterLogging/floodPoint", data);
	},
	// 视频列表
	getCameraOps: function (data) {
		return http.get("/project-manage/cameraOps/pageCamera", data);
	},
	//台风列表
	getTyphoons: function (data) {
		return http.post("/meteorological-visualization/typhoons", data);
	},
	//台风详情
	getTyphoonDetail: function (code) {
		return http.get("/meteorological-visualization/typhoons/detail/" + code);
	},
	//台风分析
	typhoonAnalysis: function (code, type) {
		return http.get(`/meteorological-visualization/typhoons/analysis?code=${code}&type=${type}`);
	},
	//监测站分类 统计
	attStBaseCountByType: function (data) {
		return http.get("/big-screen/floodDrought/attStBaseCountByType", data);
	},
	//工程分析分类 统计
	projectCount: function (data) {
		return http.get("/big-screen/floodDrought/projectCount", data);
	},
	//周边分析-树
	getperipheralAnalysisTree: function (data) {
		return http.post("/project-manage/peripheralAnalysis", data);
	},
	//周边分析-表
	getperipheralAnalysisTable: function (data) {
		return http.post("/project-manage/peripheralAnalysis/records", data);
	},
	//获取水位站统计
	projStatStatistic: function (data) {
		return http.get("project-manage/projectManage/projStatStatistic", data);
	},
	//按类型水库列表
	rsvrNewestWaterLevel: function (data) {
		return http.postJson("project-manage/projectManage/rsvrNewestWaterLevel", data);
	},
	//获取水位站列表
	waterStationsBody: function (data) {
		return http.postJson("graph/waterAndRain/waterStationsBody", data);
	},
	//详情列表
	projectsDetailList: function (data) {
		return http.post("project-manage/projectManage/projects", data);
	},
	getAdcdList: function (data) {
		return http.post("graph/adcd", data);
	},
	//根据流域查视频数量
	statsCameraByBasin: function (data) {
		return http.get("project-manage/cameraOps/statsCameraByBasin", data);
	},
	//根据行政区划查视频数量
	statsCameraByAdcd: function (data) {
		return http.get("project-manage/cameraOps/statsCameraByAdcd", data);
	},
	//水质统计接口
	bswqStatistic: function () {
		return http.get("graph/waterQuality/bswqStatistic?type=1");
	},
	//积水统计接口
	floodPointStatistic: function () {
		return http.get("flood-protect/waterLogging/floodPointStatistic?type=1");
	},
	lmRsvrDrillWlCurve: function (data) {
		return http.get("big-screen/waterDisasterPrevention/lmRsvrDrillWlCurve", data);
	},
	//获取积水测站信息
	floodPointHistoricCurve: function (data) {
		return http.get("flood-protect/waterLogging/floodPointHistoricCurve", data);
	},
	//获取协同平台token
	getDynamicToken: function (data) {
		return http.post("/project-manage/projectManage/getDynamicToken", data);
	},
	/**
	 * 智能比对
	 * 
	 */
	//添加多个对比记录
	addMultiCompareRecord: function (data) {
		return http.postJson("/plan-digitization/compare/addMultiCompareRecord", data);
	},
	//查看一项对比综合评估
	getCompareOverview: function (data) {
		return http.get("/plan-digitization/compare/getCompareOverview", data);
	},

	//流量曲线
	getMrwlCurve: function (data) {
		return http.get("/digitization/waterStatusReport/mrwlCurve", data);
	},
	//大屏闸站统计接口
	gateStats4BigScreen: function (data) {
		return http.get("/project-manage/newProjectManage/gateStats4BigScreen", data);
	},
	//工程条件查询统计
	stats4ProjectList: function (data) {
		return http.post("/project-manage/projectManage/stats4ProjectList", data);
	},
	//大屏灌区统计接口
	irrStats4BigScreen: function (data) {
		return http.get("/project-manage/newProjectManage/irrStats4BigScreen", data);
	},

  // 查询最新的水质监测列表
  waterQualityV2: function (data) {
		return http.get(pre + "/waterQualityV2/list", data);
	},
	// 水质监测站点统计
	qualityStats: function (data) {
		return http.get(pre + "/waterQualityV2/qualityStats", data);
	},
	// 查询水质监测
	qualityCurve: function (data) {
		return http.get(pre + "/waterQualityV2/qualityCurve", data);
	},
	// 水质监测C位
	qualityUnitCWindow: function (data) {
		return http.get(pre + "/waterQualityV2/qualityUnitCWindow", data);
	},
};


