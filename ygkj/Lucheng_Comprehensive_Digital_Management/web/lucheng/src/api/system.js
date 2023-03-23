/*
 * @Author: wupengfei
 * @LastEditTime: 2021-01-19 09:00:01
 * @LastEditors: lsh
 * @Descripttion:
 * @FilePath: \FloodDisasterDispatch\src\api\system.js
 */
import { http } from "../config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/graph";

// 获取行政区划
export function getArea(data) {
  return http.post(pre + "/adcd", data);
}

// 获取流域
export function getBasin() {
  return http.get(pre + "/basin");
}

// 气象统计
export function weatherPredict(data) {
  return http.get("/meteorological-visualization/weatherPredict/weatherPredict/areaName=" + data);
}

// 当前天气（包含未来数个整点的气象数据）
export function currentWeather(data) {
  return http.get("/meteorological-visualization/weatherPredict/currentWeather", data);
}

// 数据字典查询枚举
export function getDict(data) {
  return http.post("/flood-protect/dict/dicts?dictCode=" + data);
}

//用户管理

//获取科室列表--树
export function getDeptList() {
  return http.get(pre + "/dept/deptTree");
}

//获取科室列表--列表
export function getDeptPageList() {
  return http.get(pre + "/dept/list");
}

//新增科室
export function addDepartment(data) {
  return http.post(pre + "/dept/department", data);
}

//修改科室
export function editDepartment(data) {
  return http.put(pre + "/dept/department", data);
}

//删除科室
export function deleteDepartment(deptId) {
  return http.delete(pre + "/dept/department/" + deptId);
}

//用户新增
export function addUser(data) {
  return http.post(pre + "/user/addUser", data);
}

//用户更新
export function updateUser(data) {
  return http.put(pre + "/user/updateUser", data);
}

//根据用户id获取用户信息
export function getUserInfo(userId) {
  return http.get(pre + "/user/" + userId);
}

//删除用户
export function deleteUser(data) {
  return http.post(pre + "/user/deleteUser", data);
}

//改变用户状态
export function changeUserStatus(userId, status) {
  return http.put(pre + "/user/changeUserStatus/" + userId + "/" + status, {});
}

//企业管理

//获取企业列表列表
export function getCompanyList() {
  return http.get(pre + "/company/list");
}

//根据企业id获取用户列表
export function getCompanyUsers(data) {
  return http.post(pre + "/company/getCompanyUsers", data);
}

//更新企业信息
export function updateCompany(data) {
  return http.put(pre + "/company/update", data);
}

//更新企业审核状态
export function changeCompanyStatus(data) {
  return http.put(pre + "/company/changeStatus", data);
}

//验证企业是否注册
export function checkCompany(socialCreditCode) {
  return http.get(pre + "/company/check/" + socialCreditCode);
}

//新增企业
export function addCompany(data) {
  return http.post(pre + "/company/add", data);
}

//编辑修改企业用户
export function updateCompanyUser(data) {
  return http.post(pre + "/company/updateCompanyUser", data);
}

// 流程配置
// 获取水利用户列表
export function getWaterUsersList(data) {
  return http.post(pre + "/processCustomize/queryOperator", data);
}

// 流程定制
export function madeProcess(data) {
  return http.post(pre + "/processCustomize/chooseOperator", data);
}

// 获取已选操作人员
export function getOperators(step) {
  return http.get(pre + "/processCustomize/getOperators?step=" + step);
}
