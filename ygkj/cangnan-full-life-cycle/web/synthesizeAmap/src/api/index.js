/*
 * @Author: your name
 * @Date: 2020-10-10 09:22:39
 * @LastEditTime: 2021-01-18 11:36:25
 * @LastEditors: lsh
 * @Description: In User Settings Edit
 * @FilePath: \FloodDisasterDispatch\src\api\index.js
 */
import { http } from "../config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = '/auth-service';

//示例
export function unitListApi(data) {
    return http.post(pre + '/openUnit/pageList', data)
}