/*
 * @Author: hanyu
 * @LastEditTime: 2021-11-18 09:30:13
 * @Description: 
 * @FilePath: /rscp-big-screen/src/api/api_gate.js
 */
import axios from "../config/httpRequset";

// 泵站统计
export function gateStatisticApi(data) {
  return axios.get("/mgt/bm/gatestationct/getStatisticBigScreen", data);
}

