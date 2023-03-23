/*
 * @Author: hanyu
 * @LastEditTime: 2022-01-13 09:56:00
 * @Description: 
 * @FilePath: /rscp-big-screen/src/api/api_basin.js
 */
import axios from "../config/httpRequset";
// 流域统计
export function basinStatisticApi(data) {
  return axios.get("/mgt/bm/reservoirei/getBasinBigScreen", data);
}

// 获取堤防线数据
export function dikeLineApi(data) {
  return axios.get("/mgt/bm/rmsr/listJson", data);
}

// 获取堤防长度
export function dikeLengthApi(data) {
  return axios.get("/mgt/bm/dikeei/getStatisticBigScreen", data);
}
// 获取驾驶舱统计数据
export function resProstatistical(data) {
  return axios.get("/mgt/bm/watertube/resProstatistical", data);
}
// 获取台风公告数据
export function realTimeTyphoon(data) {
  return axios.get("/mgt/bm/reservoirWT/typhoon", data);
}
