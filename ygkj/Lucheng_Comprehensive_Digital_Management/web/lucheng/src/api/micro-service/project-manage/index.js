import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/project-manage";

/**
 * 查询所有的摄像头
 * @param {*} data
 */
export function selectCamera(data) {
	return http.get(pre + "/cameraOps/selectCamera", data);
}

/**
 * 分页查询所有摄像头列表
 * @param {{pageSize:number,pageNumber:number}} data
 * @returns
 */
export function listCamera(data) {
	return http.get(pre + "/cameraOps/pageCamera", data);
}
/**
 * 水源保障统计项
 * @param {*} data
 * @returns
 */
export function getStats4rsvrWatSrcTable(data) {
	return http.get(pre + "/rsvr/stats4rsvrWatSrcTable", data);
}
