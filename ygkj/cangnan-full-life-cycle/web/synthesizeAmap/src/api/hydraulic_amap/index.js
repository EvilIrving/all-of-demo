import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/graph";
const pre1 = "/plan-digitization";
const pre2 = "/water-saving";
const pre3 = "/river-service";
const pre4 = "/water-area";
const pre5 = '/soil-erosion';



//水利综合一张图
export const hydraulicApi = {
    // 获取行政区划
    getArea(data) {
        return http.post("/graph/adcd", data);
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
    // 水闸工情接口
    wagaProjList: function (data) {
        return http.get("big-screen/waterDisasterPrevention/wagaProjList", data);
    },
    //水情弹框
    waterStationInfo: function (data) {
        return http.post(pre + "/waterAndRain/waterLevels", data);
    },
    // 墒情
    soilMoistureMonitorStations: function (data) {
        return http.post(pre + "/waterAndRain/soilMoistureMonitorStations", data);
    },
    // 墒情弹窗
    moistureStationInfo: function (data) {
        return http.post(pre + "/waterAndRain/soilMoistureMonitorLevels", data);
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
    //详情列表统计
    countProjectNum: function (data) {
        return http.get("project-manage/projectManage/countProjectNum", data);
    },
    /**
     * 规划接口
     */
    // 根据分类查询规划类型
    getPlanTypeByClassify: function (data) {
        return http.get(pre1 + "/topicManage/getPlanTypeByClassify", data);
    },
    //查询一个规划下面的所有的项目， 包括子规划下面的项目， 并按投资金额倒序
    getAllProjByPrId: function (data) {
        return http.get(pre1 + "/topicManage/getAllProjByPrId", data);
    },
    // 分页查询规划信息
    pagePlanRec: function (data) {
        return http.get(pre1 + "/topicManage/pagePlanRec", data);
    },
    //点位接口
    listAllProjByPlanClassify: function (data) {
        return http.get(pre1 + "/topicManage/listAllProjByPlanClassify", data);
    },
    //查询一项完整的规划项目
    loadFullPlanProj: function (data) {
        return http.get(pre1 + "/topicManage/loadFullPlanProj", data);
    },


    /**
     * 智能比对
     * 
     */
    //添加多个对比记录
    addMultiCompareRecord: function (data) {
        return http.postJson(pre1 + "/compare/addMultiCompareRecord", data);
    },
    //查看一项对比综合评估
    getCompareOverview: function (data) {
        return http.get(pre1 + "/compare/getCompareOverview", data);
    },

    // 防汛仓库查询
    warehouses: function (data) {
        return http.post("flood-protect/warehouse/warehouses", data);
    },
    // 仓库根据行政区划分类-仓库数量
    warehousesGroupByArea: function (data) {
        return http.get("flood-protect/warehouse/warehousesGroupByArea", data);
    },
    // 防汛仓库统计
    warehousesCount: function (data) {
        return http.get("flood-protect/warehouse/warehousesCount", data);
    },
    // 防汛队伍查询
    teams: function (data) {
        return http.post("flood-protect/team/teams", data);
    },
    // 队伍根据行政区划分类-队伍数量
    teamGroupByArea: function (data) {
        return http.get("flood-protect/team/teamGroupByArea", data);
    },
    // 防汛仓库统计
    teamCount: function (data) {
        return http.get("flood-protect/team/teamCount", data);
    },
    // 视频统计
    cameraStatusByAdcd4Amap: function (data) {
        return http.get("project-manage/cameraOps/cameraStatusByAdcd4Amap", data);
    },

    /**
     * 河湖信息接口
     * 
     */
    riverUnionInfo: function (data) {
        return http.post("river-service/riverUnionInfo", data);
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

    // 取水口 取水口统计
    wainStats: function (data) {
        return http.get(pre2 + "/wainMonitor/wainStats", data);
    },
    // 查询最新的取水口监测列表
    wainMonitor: function (data) {
        return http.get(pre2 + "/wainMonitor/list", data);
    },
    // 查询取水口监测曲线
    intakeCurve: function (data) {
        return http.get(pre2 + "/wainMonitor/intakeCurve", data);
    },
    // 新版雨水口排污口基础数据表
    // 查询雨水口/排污口列表
    rivInlet: function (data) {
        return http.get(pre3 + "/rivInlet/list", data);
    },
    // 雨水口/排污口
    rivInletStats: function (data) {
        return http.get(pre3 + "/rivInlet/rivInletStats", data);
    },
    // 分页查询河段基础信息
    pageRivSegBaseInfo: function (data) {
        return http.get(pre3 + "/riverInfoV2/pageRivSegBaseInfo", data);
    },
    // 河道通用统计
    riverStats: function (data) {
        return http.get(pre3 + "/riverInfoV2/riverStats", data);
    },
    // 详表统计  对分页查询河段基础信息进行统计
    pageRivSegStats: function (data) {
        return http.get(pre3 + "/riverInfoV2/pageRivSegStats", data);
    },

    //实时流量信息
    mountainRiverWaterLevel: function (data) {
        return http.postJson("/digitization/waterStatusReport/mountainRiverWaterLevel", data);
    },
    //流量曲线
    getMrwlCurve: function (data) {
        return http.get("/digitization/waterStatusReport/mrwlCurve", data);
    },

    //获取积水测站信息
    floodPointHistoricCurve: function (data) {
        return http.get("/flood-protect/waterLogging/floodPointHistoricCurve", data);
    },

    //获取协同平台token
    getDynamicToken: function(data){
        return http.post("/project-manage/projectManage/getDynamicToken", data);
    },

    //实时降雨
    grandTotalRainfallGrid: function (data) {
        return http.post("/big-screen/waterDisasterPrevention/grandTotalRainfallGrid", data);
    },
    // 地下水位站查询（不分页）
    waterStationsList: function (data) {
        return http.post(pre2 + "/messageManage/waterStationsList", data);
    },
    // 水井信息查询（不分页）
    waterWellsList: function (data) {
        return http.post(pre2 + "/messageManage/waterWellsList", data);
    },

    //水库水位统计
    attStBaseCountByType1: function (data) {
        return http.get("/project-manage/projectManage/attStBaseCountByType", data);
    },
    // 根据id查询河段详情
    rivSegInfoById: function (data) {
        return http.get(pre3 + "/riverInfoV2/rivSegInfoById", data);
    },
    // 巡查分页查找
    loadPage: function (data) {
        return http.post(pre4 + "/WsPatrolInfo/loadPage", data);
    },
    // 分页查找上面的统计
    stats4LoadPage: function (data) {
        return http.get(pre4 + "/WsPatrolInfo/stats4LoadPage", data);
    },


    /**
     * 生产建设项目
     * 
     */
    // 根据年份统计生产建设项目数量
    projStatistics: function (data) {
        return http.get(pre5 + "/soilErosionSupervision/projStatistics", data);
    },
    // 项目信息
    getProjs: function (data) {
        return http.post(pre5 + "/soilErosionSupervision/getProjs", data);
    },
    /**
     * 
     * @param {*} data 取水户
     * @returns 
     */
    // 查询取水户
    listWiu: function (data) {
        return http.get(pre2 + "/wiu/listWiu", data);
    },
    // 查询取水户的详表统计
    stat4ListWiu: function (data) {
        return http.get(pre2 + "/wiu/stat4ListWiu", data);
    },
    // 取水户统计
    wiuStats: function (data) {
        return http.get(pre2 + "/wiu/wiuStats", data);
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
};