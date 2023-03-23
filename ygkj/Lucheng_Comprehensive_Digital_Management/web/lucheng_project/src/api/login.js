/*
 * @Author: your name
 * @Date: 2020-10-10 09:22:39
 * @LastEditTime: 2022-08-23 15:11:12
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \lucheng_project\src\api\login.js
 */
import axios from '../config/httpRequset'
const pre = '/auth-service';



//根据userId获取用户信息
export function getUser(userId) {
    return axios.get(pre + '/user/' + userId)
}

//获取文件
export function getFileList(data) {
    return axios.post(pre + '/file/load', data);
}

export function validateTicket(data) {
    return axios.post(pre + '/auth/validateTicket', data);
}

//获取用户列表
export function getUserList(data) {
    return axios.post(pre + "/user/pageInfo", data);
}

