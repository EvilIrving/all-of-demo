/*
 * @Author: dyn
 * @Date: 2022-02-17 15:43:02
 * @LastEditTime: 2022-03-10 15:07:38
 * @LastEditors: dyn
 * @Description: Modify here please
 * @FilePath: \project_manage_web\src\libs\api.request.js
 */
import HttpRequest from '@/libs/axios'
// 当前系统地址
export const baseUrl = "https://slgh.lucheng.gov.cn/api";
//export const baseUrl = "http://192.168.2.195:15000/";
//export const baseUrl = "http://192.168.2.57:8090/";
//export const baseUrl = "http://112.17.127.75:29000/prod-api/"
//export const baseUrl = process.env.VUE_APP_BASE_API;

// 应用系统地址
//export const applicationUrl = "http://192.168.2.57:8090/";
//export const applicationUrl = "http://192.168.2.118:15000/";
export const applicationUrl = "https://slgh.lucheng.gov.cn/api";
//export const applicationUrl = process.env.VUE_APP_BASE_API;
//const service = "http://192.168.2.59:15000/"; //hc
// const service = "http://192.168.2.57:15000/"; //lwy
// const Service = "/api/";
// const baseUrl = process.env.NODE_ENV === "production" ? Service : service;

export const applicationAxios = new HttpRequest(applicationUrl);

const axios = new HttpRequest(baseUrl);
export default axios

