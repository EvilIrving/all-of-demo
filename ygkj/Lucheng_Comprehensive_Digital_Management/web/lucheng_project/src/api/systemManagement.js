import axios from '../config/httpRequset'

// 单位列表
export function userUnitListApi (data) {
  return axios.get('/authentication-system/sys/getUnitList', data)
}

// 用户列表
export function queryByUnitApi (data) {
  return axios.post('/authentication-system/sys/users', data)
}

// 启用or停用
export function enableUserApi (data) {
  return axios.post('/authentication-system/sys/enableUser', data)
}

// 新增修改权限菜单
export function systemMenuSaveApi (data) {
  return axios.post('/authentication-system/sys-res/saveOrUpdate', data)
}

// 获取资源树
export function sysResTreeApi (data) {
  return axios.post('/authentication-system/sys-res/sysResTree', data)
}

// 权限菜单根据pid查询子集菜单列表
export function sysResListApi (data) {
  return axios.post('/authentication-system/sys-res/list', data)
}

// 权限菜单详情
export function sysResDetailApi (data) {
  return axios.get('/authentication-system/sys-res/detail', data)
}

// 删除权限菜单
export function sysResDelApi (data) {
  return axios.get('/authentication-system/sys-res/del', data)
}

// 查询所有角色
export function sysJobListApi (data) {
  return axios.post('/authentication-system/sys-job/list', data)
}

//新增修改角色
export function sysJobSaveApi (data) {
  return axios.post('/authentication-system/sys-job/saveOrUpdate', data)
}

//角色已经绑定的资源树
export function sysJobResTreeApi (data) {
  return axios.post('/authentication-system/sys-job/jobResTree', data)
}

//角色已经绑定的资源树
export function sysJobResIdsApi (data) {
  return axios.post('/authentication-system/sys-job/jobResIds', data)
}

//绑定角色与资源的关系
export function sysBindResJobApi (data) {
  return axios.post('/authentication-system/sys-job/bindResJob', data)
}

// 删除角色
export function sysJobDelApi (data) {
  return axios.get('/authentication-system/sys-job/del', data)
}

//绑定用户角色
export function bindUserJobApi (data) {
  return axios.post('/authentication-system/sys/bindUserJob', data)
}

//权限功能按钮的列表
export function permissionListApi (data) {
  return axios.post('/authentication-system/sys-res/permissionList', data)
}

//操作日志
export function operationListApi (data) {
  return axios.post('/authentication-system/sys/logs', data)
}

//登录日志
export function loadPageApi (data) {
  return axios.post('/authentication-system/sys/login/logs/loadPage', data)
}

//值班管理-列表查询
export function onDutyListApi (data) {
  return axios.get('/project-manage/dutyManagement/loadPage', data)
}

//值班管理-编辑
export function onDutyEditApi (data) {
  return axios.get('/project-manage/dutyManagement/updateDuty', data)
}