import { http } from "../../../config/httpRequset"; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = '/flood-protect';
const pre1 = '/graph';
const pre2 = "/digitization";
//调度方案
export const dispatchApi = {
  //抢险调度方案--测站信息和队伍信息
  emergencyDispatch: data => {
    return http.post(pre + "/emergencySupport/emergencyDispatch", data);
  },
  //抢险调度方案--物资列表
  goodsList: () => {
    return http.get(pre + "/emergencySupport/materials");
  },
  //智能分析
  getIntelligentAnalysis: data => {
    return http.postJson(pre + "/emergencySupport/intelligentAnalysis", data);
  },
  //水雨情简况
  waterRainSimpleReport: function () {
    return http.get(pre1 + "/waterRainAnalysis/selectWaterRainSimpleReport");
  },
  //日历
  getMonitorInfoByDate: function (data) {
    return http.get(pre2 + "/monitorManage/info/getMonitorInfoByDate", data);
  },
  //一键研判-全部
  warnStatsAll: function () {
    return http.get(pre + "/resInfo/warnStatsAll");
  },
  //周边分析
  peripheralAnalysis: function (data) {
    return http.post("/project-manage/peripheralAnalysis", data);
  },
  //周边分析
  records: function (data) {
    return http.post("project-manage/peripheralAnalysis/records", data);
  },
};