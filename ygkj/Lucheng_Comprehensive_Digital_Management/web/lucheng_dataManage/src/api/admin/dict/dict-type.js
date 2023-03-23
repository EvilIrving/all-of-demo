import axios from '_l/api.request'

/**
 * 添加字典类型表
 * @param dictType
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addDictType = (dictType) => {
  return axios.request({
    url: '/system-center/system/dict-type/save',
    method: 'post',
    data: dictType
  })
}

/**
 * 修改字典类型表
 * @param dictType
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateDictType = (dictType) => {
  return axios.request({
    url: '/system-center/system/dict-type/update',
    method: 'post',
    data: dictType
  })
}

/**
 * 删除字典类型表
 * @param id
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteDictTypeById = (id) => {
  return axios.request({
    url: '/system-center/system/dict-type/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

/**
 * 批量删除字典类型表
 * @param ids
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteBatchDictTypeById = (ids) => {
  return axios.request({
    url: '/system-center/system/dict-type/deleteBatch',
    method: 'post',
    data: ids
  })
}
