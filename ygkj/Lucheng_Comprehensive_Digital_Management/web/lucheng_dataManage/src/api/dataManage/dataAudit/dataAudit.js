import axios from '_l/api.request'

/**
 * 获取数据申请列表数据
 * @param data
 * @returns {AxiosPromise}
 */
export const getDataAuditList = (data) => {
  return axios.request({
    url: '/data-manage/dataApply/page',
    method: 'get',
    params: data
  })
}

/**
 * 审核数据申请
 * @param data
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const auditDataApply = (data) => {
  return axios.request({
    url: '/data-manage/dataApply/update',
    method: 'post',
    data: data
  })
}

/**
 * 删除我发起的数据修改请求
 * @param id
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteAduitDataById = (id) => {
  return axios.request({
    url: '/data-manage/dataApply/del/' + id,
    method: 'delete'
  })
}