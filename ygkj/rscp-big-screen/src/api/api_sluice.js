/*
 * @Author: hanyu
 * @LastEditTime: 2021-11-18 09:28:15
 * @Description: 
 * @FilePath: /rscp-big-screen/src/api/api_sluice.js
 */
import axios from "../config/httpRequset";

// 水闸统计
export function sluiceStatisticApi(data) {
  return axios.get("/mgt/bm/sluiceei/getStatisticBigScreen", data);
}

