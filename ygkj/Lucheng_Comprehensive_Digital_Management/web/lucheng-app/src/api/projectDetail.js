/*
 * @Author: wanxin
 * @LastEditTime: 2022-08-17 17:27:31
 * @Descripttion: 
 * @FilePath: \lucheng-app\src\api\projectDetail.js
 */
import axios from "../config/httpRequset";
const pre = "/project-manage";
// 督办中心
export const supervisionCenter = {
  // 更新
  update: function (data) {
    return axios.post("/project-manage/oversee/update", data);
  },
  // 列表
  list: function (data) {
    return axios.post("/project-manage/oversee/page", data);
  },
  // 验证码
  sendVeri: function (data) {
    return axios.post("/project-manage/oversee/sendVerificationCode", data);
  },
};
// 首页
export const homeIndex = {
  //工程信息
  getProjectList: function (data) {
    return axios.post(pre + "/projectManage/projects", data);
  },
  //水情项目
  waterStations: function (data) {
    return axios.post("graph/waterAndRain/waterStations", data);
  },

  // 工程对应的各个单位负责人信息
  getChargePerson: function (data) {
    return axios.get(pre + "/engineeringSupervision/threeCharge", data);
  },
  // 气象预报
  weatherWarning: function (data) {
    return axios.get(
      "/big-screen/waterDisasterPrevention/weatherWarning",
      data
    );
  },
  // 天气预报

  weatherForecast: function () {
    return axios.get("/meteorological-visualization/weatherPredict/listJsonForWeather", { areaName: '鹿城区' });
  },
  getSeawallSelectWarn(data) {
    return axios.get("big-screen/giveAnAlarm/selectWarn/" + data);
  },
  selectRuntimeWarn(data) {
    return axios.get("/project-manage/projectRel/selectRuntimeWarn", data);
  },
  //根据海塘查询台风
  seawallTyphoonDif(data) {
    return axios.post("/big-screen/giveAnAlarm/seawallTyphoonDif", data);
  },
};
// 实时监控
export const realTimeMonitoring = {
  // 工程关联测站列表
  stationList: function (data) {
    return axios.post("/project-manage/projectRel/listSt", data);
  },
  // 潮位测站数据
  waterLevels: function (data) {
    return axios.post("/graph/waterAndRain/waterLevels", data);
  },
  // 工程
  waterRainHours: function (data) {
    return axios.post("/graph/waterAndRain/waterRainHours", data);
  },
  hourRainfall: function (data) {
    return axios.post("/graph/waterAndRain/hourRainfall", data);
  },
  getAqjcData: function (data) {
    return axios.get("/big-screen/giveAnAlarm/getAqjcData", data);
  },

};
// 考核管理 -督察检查
export const supervisedCheck = {
  //  考核管理-督察检查-分页查询
  list: function (data) {
    return axios.post("/project-manage/contract/loadPageInspection", data);
  },
  //  考核管理- 督察检查 - 删除
  del: function (data) {
    return axios.post("/project-manage/contract/delInspection", data);
  },
  // 考核管理-督察检查-新增修改
  save: function (data) {
    return axios.post("/project-manage/contract/saveOrUpdateInspection", data);
  },
  // 考核管理-督察检查-运行单位 写死deptIds 67011533 或水主管单位 写死deptIds67038494
  deptList: function (data) {
    return axios.post("/project-manage/deptCustom/deptList", data);
  },
  // 考核管理-督察检查-参加单位
  joinUnits: function (data) {
    return axios.post("/project-manage/contract/getResponsibleUnits", data);
  },
}
// 潮位预报
export const tidalLevelForecast = {
  // 断面列表
  list: function (data) {
    return axios.get("project-manage/sectionConfiguration/forecastMaxHour", data);
  },
  // 潮位测站数据
  singleList: function (data) {
    return axios.get("project-manage/sectionConfiguration/forecastSectionHour", data);
  },
  // hourRainfall: function (data) {
  //   return axios.post("/graph/waterAndRain/hourRainfall", data);
  // },
  // getAqjcData: function (data) {
  //   return axios.get("/big-screen/giveAnAlarm/getAqjcData", data);
  // },
};

// 降雨预报
export const rainfallForecast = {
  list: function (data) {
    return axios.get("/meteorological-visualization/weatherPredict/getRainFall", data);
  },
  // testList: function (data) {
  //   return axios.post("big-screen/waterDisasterPrevention/predictRainfallByAreaName", data);
  // },
  // 降雨预报数据
  rainFallGrid: function (data) {
    return axios.get("/meteorological-visualization/weatherPredict/rainFallGrid", data);
  },
};

// 山洪风险
export const flashFloodRisk = {
  // 落点列表
  list: function (data) {
    return axios.get("flood-protect/torrentialFlood/listPrevObj", data);
  },
  // 负责人列表
  pvPchargeList: function (data) {
    return axios.post("flood-protect/torrentialFlood/pvPchargeList", data);
  },
  // 影响人员列表
  affectPopulationList: function (data) {
    return axios.post("flood-protect/torrentialFlood/affectPopulationList", data);
  },
};
// 报警
export const policeMessage = {
  //获取首页报警数量
  AlarmNum: function (data) {
    return axios.get("/project-manage/warningMsg/getAlarmNum", data);
  },
  // 列表
  list: function (data) {
    return axios.post("/project-manage/warningMsg/list", data);
  },
  // 详情
  getById: function (data) {
    return axios.get("/project-manage/warningInfo/getById", data);
  },

  // 人员列表
  userList: function (data) {
    return axios.post("/project-manage/warningUser/userList", data);
  },
  // 转发
  save: function (data) {
    return axios.post("/project-manage/warningInfo/saveOrUpdate", data);
  },
  // 全部已读
  readAll: function (data) {
    return axios.get("/project-manage/warningMsg/readAll", data);
  },
  // 单条已读
  readById: function (data) {
    return axios.get("/project-manage/warningMsg/readById", data);
  },
  // 审批列表
  getApproval: function (data) {
    return axios.get("/project-manage/warningMsg/getApproval", data);
  },
  // 审批单条已读
  readMessage: function (data) {
    return axios.post("/project-manage/message/readMessage", data);
  },
  // 审批全部已读
  readAllMessage: function (data) {
    return axios.post("/project-manage/message/readAllMessage", data);
  },
};