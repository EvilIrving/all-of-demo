import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/warning-service";

// 预警信息  通知消息
export const inforApi = {

	// 预警类型
	getEarlyType: function () {
		return http.get(pre + "/msg/types");
	},
	// 预警信息 统计
	statistic: function (data) {
		return http.post(pre + "/warningComponent/statistic", data);
	},
	// 预警信息 统计
	chartStatistic: function (data) {
		return http.post(pre + "/warningComponent/chartStatistic", data);
	},
	// 详情
	getEarlyDetail: function (data) {
		return http.post(pre + "/warningComponent/warnings", data);
	},
}