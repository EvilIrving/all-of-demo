/*
 * @Author: your name
 * @Date: 2020-10-10 09:22:39
 * @LastEditTime: 2021-01-19 09:12:21
 * @LastEditors: lsh
 * @Description: In User Settings Edit
 * @FilePath: \FloodDisasterDispatch\src\api\login.js
 */
import { http } from "../config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/auth-service";

//登陆
export function login(data) {
  return http.post(pre + "/auth/login", data);
}

//登出
export function logout(data) {
  return http.post(pre + "/auth/logout", data);
}

//根据userId获取用户信息
export function getUser(userId) {
  return http.get(pre + "/user/" + userId);
}

//获取文件
export function getFileList(data) {
  return http.post(pre + "/file/load", data);
}

export function validateTicket(data) {
  return http.post(pre + "/auth/validateTicket", data);
}

//获取用户列表
export function getUserList(data) {
  return http.post(pre + "/user/pageInfo", data);
}
