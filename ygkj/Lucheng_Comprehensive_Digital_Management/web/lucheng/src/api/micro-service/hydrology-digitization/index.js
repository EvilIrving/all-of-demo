import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/hydrology-digitization";

/**
 * 查询水文预报列表
 * @param {{releaseDate?:string;stName?:string}} data
 */
export function getHydrologyForecast(data) {
	return http.get(pre + "/hydrFcst/pageHydrFcst", data);
}
/**
 * 组件河道数据
 */
export function getHydrologyData() {
	return http.get(pre + "/hydrFcst/newestHydrFcst");
}
/**
 * 组件潮位数据
 */
export function getTideData() {
	return http.get(pre + "/tideFcst/newestTideFcst");
}
/**
 * 流域生态流量列表
 * @param {{bas:string;warnType:1|2;sectName:string}} data
 */
export function getSectFlowList(data) {
	return http.get("/hydrology-digitization/sectFlow/getSectFlowList", data);
}
/**
 * 流域生态流量统计项
 * @param {{bas:string;warnType:1|2;sectName:string}} data
 */
export function getStats4SectFlowList(data) {
	return http.get(pre + "/sectFlow/stats4SectFlowList", data);
}
