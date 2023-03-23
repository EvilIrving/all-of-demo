import axios from '@/libs/api.request';
import { applicationAxios } from '@/libs/api.request';

/**
 * 添加配置
 * @param config
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addConfig = (config) => {
  return axios.request({
    url: '/system-center/system/config/save',
    method: 'post',
    data: config
  })
}

/**
 * 修改配置
 * @param config
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateConfig = (config) => {
  return axios.request({
    url: '/system-center/system/config/update',
    method: 'post',
    data: config
  })
}

/**
 * 删除配置
 * @param configId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteConfigById = (configId) => {
  return axios.request({
    url: '/system-center/system/config/delete',
    method: 'post',
    params: {
      id: configId
    }
  })
}

/**
 * 批量删除配置
 * @param configIds
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteBatchConfigById = (configIds) => {
  return axios.request({
    url: '/system-center/system/config/deleteBatch',
    method: 'post',
    data: configIds
  })
}

/**
 * 获取加密公钥
 * @returns {AxiosPromise}
 */
export const getPublickey = () => {
  //console.log(applicationAxios);
  return applicationAxios.request({
    url: '/auth-center/getPubKey',
    method: 'get',
  })
}
