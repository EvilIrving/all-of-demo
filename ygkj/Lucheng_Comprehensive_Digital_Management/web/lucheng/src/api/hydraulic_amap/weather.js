import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/meteorological-visualization";

export const weatherPredict = {
  // 风场时间轴
  windFarmsTimeList: function () {
    return http.get(pre + "/site/windFarmsTimeList");
  },
  // 风场详细数据
  getWindDataList: function (id) {
      return http.get(pre + `/site/wind/${id}`);
  },
  // 浪场时间轴
  waveFieldList: function () {
      return http.get(pre + "/site/waveFieldList");
  },
  // 浪场详细数据
  getWaveDataList: function (id) {
      return http.get(pre + `/site/wave/${id}`);
  },
  //卫星云图
  getWenzhouCloudData: function(){
    return http.get(`/hydrology-digitization/comprehensiveAnalysis/wenzhouCloudData`);
  },
  //雷达降雨
  getWenzhouRadarData: function(){
    return http.get(`/hydrology-digitization/comprehensiveAnalysis/wenzhouRadarData`);
  }
}