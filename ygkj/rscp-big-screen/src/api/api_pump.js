/*
 * @Author: hanyu
 * @LastEditTime: 2021-11-18 09:27:48
 * @Description: 
 * @FilePath: /rscp-big-screen/src/api/api_pump.js
 */
import axios from "../config/httpRequset";

// 泵站统计
export function pumpStatisticApi(data) {
  return axios.get("/mgt/bm/pumpei/getStatisticBigScreen", data);
}

