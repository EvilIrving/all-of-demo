/*
 * @Author: dyn
 * @Date: 2022-02-22 14:19:03
 * @LastEditTime: 2022-02-22 15:21:38
 * @LastEditors: dyn
 * @Description: 文件服务
 * @FilePath: \project_manage_web\src\api\file.js
 */
import axios from '@/libs/api.request'
/**
 * 根据文件ids查询文件信息
 * @param fileIds 
 * @returns {*|AxiosPromise<any>}
 */
export const getFileByIds = (fileIds) => {
    return axios.request({
        url: '/file-center/system/file/list',
        method: 'post',
        data: {
            fileIds: fileIds
        }
    })
}
