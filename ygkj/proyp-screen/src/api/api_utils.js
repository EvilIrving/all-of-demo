
import axios from './request.js'

// 获取用户信息
export function getUserInfoApi(data) {
  return axios.post('/mgt/bm/pmpi/getCurrentUser', data)
}

// 行政区划选择
export function listADCDtreeApi(data) {
  return axios.get("/mgt/bm/project/listADCDtree2", data);
}

// 行政区划树形结构
export function getAdcdTree(data) {
  return axios.get("/mgt/bm/reservoirWT/adcdTree", data);
}
// 大屏风险闭环列表
export function getRiskList(data) {
  return axios.get("/mgt/temp/proriskcolse/listJsonInfo", data);
}
// 大屏风险闭环生成列表
export function getGerneRiskList(data) {
  return axios.get("/mgt/temp/proriskcolse/statsListJson", data);
}

export function getNewGerneRiskList(data) {
  // 新的统计逻辑
  return axios.get("/mgt/temp/proriskcolse/listJsonInfo", data);
}