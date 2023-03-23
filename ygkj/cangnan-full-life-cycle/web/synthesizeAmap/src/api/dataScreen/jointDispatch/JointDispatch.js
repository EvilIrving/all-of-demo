import { http } from "../../../config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/flood-protect";
const pre1 = '/graph';

//联合调度
export const dispatchApi = {
    // 报警数量
    warnNum: data => {
        return http.get(pre + "/resInfo/warnNum", data);
    },
    // 地图点位
    mapPoint: data => {
        return http.get(pre + "/resInfo/resProList", data);
    },
    //工情内涝
    floodPoint: data => {
        return http.get(pre + "/waterLogging/floodPoint", data);
    },
    //河网水位
    riverWaterLevel: data => {
        return http.get(pre + "/waterLogging/riverWaterLevel", data);
    },
    //潮位
    waterStations: data => {
        return http.post(pre1 + "/waterAndRain/waterStations", data);
    },
    //报警统计
    waterLoggingWarnNum: data => {
        return http.get(pre + "/waterLogging/warnNum", data);
    },
    //闸门
    wagaList: data => {
        return http.get(pre + "/waterLogging/wagaList", data);
    },
    //泵站排水量
    listPustProj: data => {
        return http.get(pre + "/floodProject/listPustProj", data);
    },
    //三条曲线图
    floodPointCurveList: data => {
        return http.get(pre + "/waterLogging/floodPointCurveList", data);
    },
    //内涝模型
    floodPointForecastCurve: data => {
        return http.get(pre + "/waterLogging/floodPointForecastCurve", data);
    },
    // 水位弹框
    waterStationInfo: function(data) {
        return http.post(pre1 + "/waterAndRain/waterLevels", data);
    },

};