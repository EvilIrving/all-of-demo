import { http } from "@/config/httpRequset.js";

//避灾点列表
export function getWaterFlowList(data) {
	return http.post("/big-screen/avoidancePoint/pageList", data);
}

/** 避灾点C位统计 */
export function getWaterFlowStat(data) {
	return http.post("/big-screen/avoidancePoint/totalList", data);
}


//救援队伍C位统计
export function getTeamCountList(data) {
	return http.get("/flood-protect/team/teamCountList", data);
}

//救援队伍列表
export function getTeamList(data) {
	return http.post("/flood-protect/team/teams", data);
}
//救援队伍落点人员
export function getTeamPersonsList(data) {

	return http.get("/flood-protect/team/teamPersons", data);
}
//救援队伍落点设备
export function getTeamDeviceList(data) {
	return http.get("/flood-protect/team/teamDeviceList", data);
}
//救援队伍落点设备类型统计
export function getTeamDeviceByType(data) {
	return http.get("/flood-protect/team/teamDeviceByType", data);
}

//救援仓库
export function getWaterWarehouses(data) {
	return http.post("/flood-protect/warehouse/warehouses", data);
}

//救援仓库c位统计
export function getWaterWarehousesCount(data) {
	return http.get("flood-protect/warehouse/warehousesCount", data);
}
//救援仓库物资
export function getSelectDevice(data) {
	return http.get("/flood-protect/attWarehouse/warehouseStaticsList", data);
}

