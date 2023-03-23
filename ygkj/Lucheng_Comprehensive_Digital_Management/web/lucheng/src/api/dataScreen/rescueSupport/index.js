/*
 * @Author: hanyu
 * @LastEditTime: 2021-01-27 09:58:26
 * @Description: 强险支持
 * @FilePath: /FloodDisasterDispatch/src/api/dataScreen/rescueSupport/index.js
 */

import { http } from "../../../config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = '/flood-protect'
// 水、雨情列表
export const waterRainApi = {
  // 雨情
  rain: data => {
    return http.post(pre + "/emergencySupport/rainfallWarningInfo", data);
  },
  // 水情
  water: data => {
    return http.get(pre + "/waterLevelAnalyse/floatWinWaterLevel", data);
  }
};
