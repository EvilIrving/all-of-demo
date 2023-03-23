import { http } from "@/config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = "/meteorological-visualization";

// 天气实况
export const weatherApi = {

  // 气象落点
  meteorological: function () {
    return http.get(pre + "/meteorological");
  },
  // 气象弹窗
  meteorologicalPastTimeData: function (data) {
    return http.post(pre + "/meteorological/meteorologicalPastTimeData", data);
  },
  //地图选点获取详细数据
  getMapPointDetail: function (data) {
    return http.post(pre + '/weatherPredict/mapPointDetail', data);
  },
  //雨量预测（1、3、6h）
  caculateRainfall: function (data) {
    return http.post(pre + '/weatherPredict/caculateRainfall', data);
  },
  //获取风暴潮预警
  stormTideSelect: function () {
    return http.get("/project-manage/stormTide/select");
  },
  
}