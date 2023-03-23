import { http } from "@/config/httpRequset";

/** 综合查询台风列表 */
export function getTyhoonList(data) {
	return http.post("/meteorological-visualization/typhoons/historyAndCurrent", data);
}

/** 历史台风 */
export function getHistoryTyphoons(data) {
	return http.post("/meteorological-visualization/typhoons", data);
}

/** 获取相似台风 */
export function getSimilarTyphoons({ code, type }) {
	return http.get(`/meteorological-visualization/typhoons/analysis?code=${code}&type=${type}`);
}

/** 单个台风信息 */
export function getTyphoonDetail({ code }) {
	return http.get(`meteorological-visualization/typhoons/detail/${code}`);
}
/** 台风影响工程 */
export function getTyphoonEffectedProject(data) {
	return http.post("/project-manage/floodRes/listTyphoonAllRealProject", data);
}
/** 台风C位窗口 */
export function getTyphoonStat4C(data) {
	return http.get("big-screen/waterDisasterPrevention/typhoonStats4C", data);
}

//台风影响信息
export function getTyphoonAffects(data) {
	return http.post("/meteorological-visualization/typhoons/typhoonAffects", data);
}

//台风静态风险-统计各个行政区划台风登录次数
export function landTimesEachAdcd(data){
	return http.get("/meteorological-visualization/typhoons/landTimesEachAdcd", data);
}


