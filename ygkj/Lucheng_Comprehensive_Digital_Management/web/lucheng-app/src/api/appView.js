import axios from "../config/httpRequset";
let manage = "/project-manage";
//超警统计
export const alarmStatic = function () {
  return axios.post("/big-screen/floodDrought/countOverAlarm");
};
//汛情摘要
export const floodPaper = {
  //雨情
  rainCondition: function (data) {
    return axios.get(
      "/big-screen/giveAnAlarm/floodPreventionBulletinApp",
      data
    );
  },
  //超讯超警
  waterStation: function (data) {
    return axios.post("/graph/waterAndRain/waterStations", data);
  },
};

//档案
export const appFiles = {
  //档案类型
  getAppAchievement: function (data) {
    return axios.post("/ums/ums/sysAchievement/appAchievement", data);
  },
};
//物业审批-维修养护
export const propertyMt = {
  //首页数据
  getHiddenAppTotal: function (data) {
    return axios.get(`${manage}/hiddenDanger/hiddenAppTotal`, data);
  },
  //工程名称获取
  getProjectName: function (data) {
    return axios.post(`${manage}/serviceItem/loadSkPage`, data);
  },
  //任务下达-单位查找-水管单位
  getWaterManageUser: function (data) {
    return axios.post(`/ums/sys/getWaterManageUserList`, data);
  },
  //任务下达-单位查找-水利局
  getUserData: function (data) {
    return axios.get(`/ums/sys/userList`, data);
  },
  //任务下达-列表
  DailyTask: function (data) {
    return axios.post(`${manage}/maintenance/loadPageMtDailyTask`, data);
  },
  //任务下达-新增
  updateDailyTask: function (data) {
    return axios.post(`${manage}/maintenance/saveOrUpdateMtDailyTask`, data);
  },
  //业务审批-处理信息
  implementSubmit: function (data) {
    return axios.post(`${manage}/hiddenDanger/implementSubmit`, data);
  },
  //业务审批-审批意见
  submitProgressInfo: function (data) {
    return axios.post(`${manage}/hiddenDanger/submitProgressInfo`, data);
  },
  //进度查询
  getHiddenDangerDetail: function (data) {
    return axios.get(`${manage}/hiddenDanger/getHiddenDangerDetail`, data);
  },
  //消息提醒
  getMsgRead: function (data) {
    return axios.get(`${manage}/hiddenDanger/msgRead`, data);
  },
  //消息提醒-列表
  getMsgList: function (data) {
    return axios.get(`${manage}/hiddenDanger/msgList`, data);
  },
  //消息提醒-发送
  sendNoticeMsg: function (data) {
    return axios.get(`${manage}/hiddenDanger/sendNoticeMsg`, data);
  },
};

//人员考勤
export const staffAttendance = {
  //人员考勤
  list: function (data) {
    return axios.get("/project-manage/attendance/attendanceCalendar", data);
  },
  //公司列表
  companyList: function (data) {
    return axios.get("/project-manage/attendance/attendanceCompany", data);
  },
  //公司考勤列表
  compantAttendance: function (data) {
    return axios.post("/project-manage/attendance/companyAnalysis", data);
  },
  //公司考勤详情列表
  attendanceDetail: function (data){
    return axios.post('/project-manage/attendance/companySummary', data)
  }
};
