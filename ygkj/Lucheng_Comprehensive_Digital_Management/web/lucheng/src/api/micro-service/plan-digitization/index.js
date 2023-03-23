//@ts-check
import { http } from "@/config/httpRequset";

/**
 *
 * @param {any} data
 */
export function getListPlanTypeOfDevPlan(data) {
	return http.get("/plan-digitization/devPlan/listPlanTypeOfDevPlan", data);
}
/**
 *
 * @param {any} data
 */
export function getStatsOfDevPlan(data) {
	return http.get("/big-screen/waterDevPlan/statsOfDevPlan", data);
}
/**
 * C位统计
 * @param {any} data
 */
export function getStatsCwindowOfDevPlan(data) {
	return http.get("/big-screen/waterDevPlan/statsCwindowOfDevPlan", data);
}
/**
 * 详表上的筛选项
 * @param {any} data
 */
export function getFilterOptions(data) {
	return http.get("/plan-digitization/devPlan/listDevPlanRec", data);
}
/**
 * 详表上面的统计
 * @param {any} data
 */
export function getDevPlanProjStats(data) {
	return http.get("/plan-digitization/devPlan/devPlanProjStats", data);
}
/**
 * 详表
 * @param {any} data
 */
export function getPageDevPlanProj(data) {
	return http.get("/plan-digitization/devPlan/pageDevPlanProj", data);
}
