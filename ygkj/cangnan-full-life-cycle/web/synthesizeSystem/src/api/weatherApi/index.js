import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/meteorological-visualization";

// 天气实况
export const weatherApi = {

	// 气象落点
	meteorological: function () {
		return http.get(pre + "/meteorological");
	},
	// 气象弹窗
	meteorologicalPastTimeData: function (data) {
		return http.post(pre + "/meteorological/meteorologicalPastTimeData", data);
	},
}