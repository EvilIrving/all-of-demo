/*
 * @Author: hanyu
 * @LastEditTime: 2021-12-01 09:56:46
 * @Description: 
 * @FilePath: /rscp-big-screen/src/api/api_dike.js
 */
import axios from "../config/httpRequset";

// 堤防统计
export function dikeStatisticApi(data) {
  return axios.get("/mgt/bm/dikeei/getStatisticBigScreen", data);
}