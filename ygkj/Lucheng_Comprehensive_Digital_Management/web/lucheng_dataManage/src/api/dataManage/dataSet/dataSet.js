import axios from '_l/api.request'

/**
 * 获取数据表列数据
 * @param data 数据
 * @returns {AxiosPromise}
 */
export const getTableColumnData = (data) => {
  return axios.request({
    url: '/data-manage/dataSet/preview',
    method: 'post',
    data: data
  })
}

/**
 * 根据id获取详情
 * @returns {AxiosPromise}
 */
export const getDataSetById = (id) => {
  return axios.request({
    url: '/data-manage/dataSet/select/' + id,
    method: 'get',
  })
}

/**
 * 新增数据集数据
 * @param data 数据
 * @returns {AxiosPromise}
 */
export const addDataSet = (data) => {
  return axios.request({
    url: '/data-manage/dataSet/save',
    method: 'post',
    data: data
  })
}

/**
 * 修改数据集数据
 * @param data 数据
 * @returns {AxiosPromise}
 */
export const updateDataSet = (data) => {
  return axios.request({
    url: '/data-manage/dataSet/update',
    method: 'put',
    data: data
  })
}

/**
 * 删除数据集列表数据
 * @param dataSetId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteDataSetById = (dataSetId) => {
  return axios.request({
    url: '/data-manage/dataSet/delById/' + dataSetId,
    method: 'delete'
  })
}
