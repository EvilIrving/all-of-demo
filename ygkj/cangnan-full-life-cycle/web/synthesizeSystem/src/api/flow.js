import { http } from "@/config/httpRequset.js";

export function getWaterFlowList(data) {
	return http.get("big-screen/waterDisasterPrevention/waterFlowList", data);
}

/** C位统计 */
export function getWaterFlowStat(data) {
	return http.get("big-screen/waterDisasterPrevention/waterFlowStats", data);
}
