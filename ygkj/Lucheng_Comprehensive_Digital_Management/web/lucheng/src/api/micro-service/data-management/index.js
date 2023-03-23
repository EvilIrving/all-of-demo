import { http } from "@/config/httpRequset";
const pre = "/data-management";

/** 数据服务 申请排名 */
export function applyRank(data) {
	return http.get(pre + "/dataService/applyRank", data);
}

/** 数据服务 数据api请求 */
export function invokeStatistic(data) {
	return http.get(pre + "/dataService/invokeStatistic", data);
}

/** 数据服务 最新上线服务 */
export function latestService(data) {
	return http.get(pre + "/dataService/newestService", data);
}

/** 数据服务 服务调用次数 */
export function invokeNumber(data) {
	return http.get(pre + "/dataService/invokeNumber", data);
}

/** 数据服务 详表 */
export function getDataServiceList(data) {
	return http.post(pre + "/dataService", data);
}

/** 数据服务 c位 */
export function getDataService4C(data) {
	return http.get(pre + "/dataService/cBit");
}

/** 数据治理 数据来源 */
export function getDataSource() {
	return http.get(pre + "/dataGovernance/dataSources");
}

/** 数据治理 数据规则 */
export function getGovernanceRule() {
	return http.get(pre + "/dataGovernance/governanceRules");
}

/** 数据治理 治理成效 */
export function getGovernanceResult() {
	return http.get(pre + "/dataGovernance/governanceEffectiveness");
}

/** 数据治理 数据完整率 */
export function getDataCompleteRate() {
	return http.get(pre + "/dataGovernance/dataCompleteRate");
}

/** 数据治理 c位数据 */
export function getGovernance4C() {
	return http.get(pre + "/dataGovernance/cBit");
}

/** 数据治理 详表 */
export function getDataGovernanceList(data) {
	return http.post(pre + "/dataGovernance/form", data);
}

/** 数据分布 饼图 */
export function getTotalStatistic() {
	return http.get(pre + "/dataDistribution/totalStatistic");
}

/**
 * 数据分布 实时数据
 */
export function getRealtimeData() {
	return http.get(pre + "/dataDistribution/realTimeData");
}

/**
 * 数据分布 基础数据
 * @param {{name:string}} data
 */
export function getBasicData(data) {
	return http.post(pre + "/dataDistribution/basicData", data);
}

/**
 * 数据责任 一数一责
 */
export function getDataAndDuty() {
	return http.get(pre + "/dataDuty/dataAndDuty");
}

/**
 *
 */
export function getDuty4C() {
	return http.get(pre + "/dataDuty/cBit");
}
/**
 * 数据责任 列表详情
 * @param {{categoryId:string;dutyPerson:string;pageNum:number;pageSize:number;tableIds:string;tableSchema:string;tables:string}}
 */
export function getDetaiList(data) {
	return http.post(pre + "/dataDuty/detaiList", data);
}

/**
 * 数据责任分布
 */
export function getDutyDataDistribute() {
	return http.get(pre + "/dataDuty/dutyDataDistribute");
}

/**
 * 责任人分布
 */
export function getDutyManDistribute() {
	return http.get(pre + "/dataDuty/dutyManDistribute");
}
