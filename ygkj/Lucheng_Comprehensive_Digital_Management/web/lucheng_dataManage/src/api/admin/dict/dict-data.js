import axios from '_l/api.request'

/**
 * 添加字典数据表
 * @param dictData
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addDictData = (dictData) => {
  return axios.request({
    url: '/system-center/system/dict-data/save',
    method: 'post',
    data: dictData
  })
}

/**
 * 修改字典数据表
 * @param dictData
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateDictData = (dictData) => {
  return axios.request({
    url: '/system-center/system/dict-data/update',
    method: 'post',
    data: dictData
  })
}

/**
 * 删除字典数据表
 * @param dictDataId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteDictDataById = (dictDataId) => {
  return axios.request({
    url: '/system-center/system/dict-data/delete',
    method: 'post',
    params: {
      id: dictDataId
    }
  })
}

/**
 * 批量删除字典数据表
 * @param dictDataIds
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteBatchDictDataById = (dictDataIds) => {
  return axios.request({
    url: '/system-center/system/dict-data/deleteBatch',
    method: 'post',
    data: dictDataIds
  })
}
