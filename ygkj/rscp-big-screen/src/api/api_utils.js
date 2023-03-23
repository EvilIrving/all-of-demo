/*
 * @Author: hanyu
 * @LastEditTime: 2022-02-18 15:29:02
 * @Description: 
 * @FilePath: /rscp-big-screen/src/api/api_utils.js
 */
import axios from '../config/httpRequset'

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
  return axios.get("/mgt/bm/reservoirWT/getAdcdTree", data);
}

// 获取用户九龙治水权限
export function getUserJlzsPermissionApi(data) {
  return axios.post('/mgt/bm/reservoirWT/getCurOpers', data)
}