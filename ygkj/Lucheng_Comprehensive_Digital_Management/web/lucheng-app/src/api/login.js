/*
 * @Author: your name
 * @Date: 2020-10-10 09:22:39
 * @LastEditTime: 2022-11-23 11:03:05
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \lucheng-app\src\api\login.js
 */
import axios from "../config/httpRequset"; // {axios}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/auth-service";

//登陆
export function login(data) {
  return axios.post(pre + "/auth/login", data);
}

//单点登陆
export function loginTicketApi(data) {
  return axios.post(pre + "/auth/validateTicket", data);
}
//单点登陆
export function loginZzdLoginApi(data) {
  return axios.post(pre + "/auth/zzdLogin", data);
}
//浙里办和支付宝单点登陆
export function zwwLoginApi(data) {
  return axios.post(pre + "/auth/zwwLogin", data);
}
//浙里办微信小程序单点登陆
export function zlbLoginApi(data) {
  return axios.post(pre + "/auth/zlbLogin", data);
}

export function sendZzdVerificationApi(data) {
  return axios.get(pre + "/auth/sendZzdVerification", data);
}


//登出
export function logout(data) {
  return axios.post(pre + "/auth/logout", data);
}

//根据userId获取用户信息 
export function getUser(userId) {
  return axios.get(pre + "/user/" + userId);
}

export function publicKey(data) {
  return axios.post("auth-service/auth/rdspwd", data, true);
}

//获取文件
export function getFileList(data) {
  return axios.post(pre + "/file/load", data);
}

export function validateTicket(data) {
  return axios.post(pre + "/auth/validateTicket", data);
}

//获取用户列表
export function getUserList(data) {
  return axios.post(pre + "/user/pageInfo", data);
}
