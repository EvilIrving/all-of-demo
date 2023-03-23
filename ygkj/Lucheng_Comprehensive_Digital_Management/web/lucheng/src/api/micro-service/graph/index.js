//@ts-check
import { http } from "@/config/httpRequset";
const pre = "/graph";

/**
 * 数据汇聚分类统计
 * @param {{type:'全部'|'在线'|'离线'}} data
 */
export function dataGatherClassification(data) {
	return http.post(pre + "/dataAggregation/classificationStatistics", data);
}

/**
 * 数据汇聚在线率统计
 * @param {{type:'雨情'|'水情'|'工情'|'视频';year:string}} data
 */
export function dataGatherOnlineRate(data) {
	return http.post(pre + "/dataAggregation/onlineRateStatistics", data);
}

/**
 * 获取站点信息
 * @typedef {'PP'|'RR'|'ZZ'|'DD'|string} StationType 站点类型 允许使用不同的类型使用逗号分割 `PP,RR`
 * @param {{stationType:StationType;areaCode:string;stationName:string}} data
 */
export function getWaterStations(data) {
	return http.post(pre + "/waterAndRain/waterStations", data);
}
