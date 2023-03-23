import axios from '../config/httpRequset';

const service = '/project-manage';

/**
 * @typedef {{pageSize:number;pageNum:number}&T} Paged
 * @template T
 */

/**
 * 新增目录
 * @param {{pid:string;directoryName:string}} data
 */
export function insertDirectory(data) {
  return axios.post(service + '/dataMaintenance/directory/insert', data);
}

/**
 * 目录树查询
 */
export function queryDirectory() {
  return axios.post(service + '/dataMaintenance/directory/tree');
}

/**
 * 更新目录
 * @param {{id:string;directoryName:string}} data
 */
export function updateDirectory(data) {
  return axios.post(service + '/dataMaintenance/directory/update', data);
}

/**
 * 分页查询维护信息
 * @param {Paged<{id?:string;directoryId?:string;dataItem?:string}>} data
 */
export function queryInfo(data) {
  return axios.post(service + '/dataMaintenance/info/queryPage', data);
}

/**
 * @typedef {Object} MaintenanceInfo
 * @property {string} id
 * @property {string} dataItem
 * @property {string} dataMaintainName
 * @property {string} dataMaintainPhone
 * @property {string} description
 * @property {string} directoryId
 * @property {string} directoryName
 * @property {string} source
 * @property {string} thirdPartyUnit JSON对象
 */

/**
 * 新增维护信息
 * @param {MaintenanceInfo} data
 */
export function insertInfo(data) {
  return axios.post(service + '/dataMaintenance/info/insert', data);
}

/**
 * 更新维护信息
 * @param {MaintenanceInfo} data
 */
export function updateInfo(data) {
  return axios.post(service + '/dataMaintenance/info/update', data);
}

/**
 * 删除维护信息
 * @param {{id:string}} data
 */
export function deleteInfo(data) {
  return axios.get(service + '/dataMaintenance/info/delete', data);
}

/**
 * 新增维护任务
 * @param {any} data
 */
export function insertSendInfo(data) {
  return axios.post(service + '/dataMaintenance/sendInfo/insert', data);
}

/**
 * 分页查询维护任务
 * @param {Paged<{}>} data
 */
export function querySendInfo(data) {
  return axios.post(service + '/dataMaintenance/sendInfo/queryPage', data);
}

/**
 * 删除维护任务
 * @param {{id:string}}} data
 */
export function deleteSendInfo(data) {
  return axios.get(service + '/dataMaintenance/sendInfo/delete', data);
}

/**
 * 更新维护任务
 * @param {any} data
 */
export function updateSendInfo(data) {
  return axios.post(service + '/dataMaintenance/sendInfo/update', data);
}

/**
 * 三个责任人修改记录的列表
 * @param {any} data
 */
 export function queryChangeRecordApi(data) {
  return axios.post(service + '/threepeople/queryChangeRecord', data);
}
  