import axios from '_l/api.request'

/**
 * 删除数据源列表数据
 * @param dataSourceId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteDataSourceById = (dataSourceId) => {
  return axios.request({
    url: '/data-manage/dataSource/delById/' + dataSourceId,
    method: 'delete'
  })
}

/**
 * 更新频次
 * @param data
 * @returns {AxiosPromise}
 */
export const updateFrequency = (data) => {
  return axios.request({
    url: '/data-manage/dataAssessFrequency/batchSave',
    method: 'post',
    data: data
  })
}

/**
 * 删除更新频次
 * @param data
 * @returns {AxiosPromise}
 */
export const deleteFrequency = (data) => {
  return axios.request({
    url: '/data-manage/dataAssessFrequency/batchDel',
    method: 'delete',
    data: data
  })
}

/**
 * 添加所属部门
 * @param data
 * @returns {AxiosPromise}
 */
export const addDataAccessDept = (data) => {
  return axios.request({
    url: '/data-manage/dataAccessDept/batchSave',
    method: 'post',
    data: data
  })
}

/**
 * 删除所属部门
 * @param data
 * @returns {AxiosPromise}
 */
export const deleteDataAccessDept = (data) => {
  return axios.request({
    url: '/data-manage/dataAccessDept/batchDel',
    method: 'delete',
    data: data
  })
}