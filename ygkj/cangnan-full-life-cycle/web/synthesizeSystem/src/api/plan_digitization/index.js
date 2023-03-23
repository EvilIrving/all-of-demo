import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造

export const planDigitizationApi = {
	getListPlanTypeOfDevPlan: function(data) {
		return http.get("/plan-digitization/devPlan/listPlanTypeOfDevPlan", data);
	},
	getStatsOfDevPlan: function(data) {
		return http.get("/big-screen/waterDevPlan/statsOfDevPlan", data);
	},
	/** C位统计*/
	getStatsCwindowOfDevPlan: function(data) {
		return http.get("/big-screen/waterDevPlan/statsCwindowOfDevPlan", data);
	},
	/** 详表上的筛选项 */
	getFilterOptions: function(data) {
		return http.get("/plan-digitization/devPlan/listDevPlanRec", data);
	},
	/** 详表上面的统计 */
	getDevPlanProjStats: function(data) {
		return http.get("/plan-digitization/devPlan/devPlanProjStats", data);
	},
	/** 详表 */
	getPageDevPlanProj: function(data) {
		return http.get("/plan-digitization/devPlan/pageDevPlanProj", data);
	},

};
