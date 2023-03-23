/*
 * @Author: dyn
 * @Date: 2022-02-17 15:43:02
 * @LastEditTime: 2022-02-24 13:55:19
 * @LastEditors: dyn
 * @Description: Modify here please
 * @FilePath: \project_manage_web\src\api\common.js
 */
import axios from '@/libs/api.request'

/**
 *  注意：
 *  1：request 里面得传递参数可以有两种
 *    1）：data
 *        这是以body方式传递参数
 *        例如：
 *        一：data: params  //post body方式 {key:value,key:value,}
 *        二：data: {
            item: userIds
          } 形成得格式：{item:xxxxx}
 *    2）：params
 *        这是以url传递参数
 *        例如：
 *        params: {
            id: userId  // url请求方式  getUserById?id=userId
          }
 *
 */

/**
 * 请求表格接口
 * @param url 接口地址
 * @param methed  请求方式
 * @param queryParams 请求参数
 * @returns {*|AxiosPromise<any>|void}
 */
export const getModulesTables = (url, methed, queryParams) => {
  return axios.request({ // 这里返回的是一个Promise，request方法传入一个配置对象，配置项可参考axios
    url: url,
    method: methed,
    params: queryParams
  })
}

/**
 * 根据type 查询字典数据
 * @param dictType
 * @returns {*|AxiosPromise<any>}
 */
export const getDictDataByDictType = (dictType) => {
  return axios.request({
    url: '/system-center/system/dict-data/getDictDataListByDictType',
    params: {
      dictType: dictType
    }
  })
}


/**
 * 获取行政区划树形
 * @param parentAdcd 父级行政区划
 * @returns {*|AxiosPromise<any>}
 */
export const getAreaTree = (parentAdcd) => {
  return axios.request({
    url: '/system-center/sys-adcd/tree',
    params: {
      parentAdcd: parentAdcd
    }
  })
}