import axios from "../config/httpRequset"; // {axios}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
let manage = '/project-manage'
//实时雨情
export function rainfallApi(data) {
  return axios.get('/big-screen/floodDrought/rainfall', data);
}

//文件上传
export function fileUploadApi(data) {
  return axios.post('/ums/upload', data);
}

//实时水情
export function waterStationsApi(data) {
  return axios.post('/graph/waterAndRain/waterStations', data);
}

//验收申请统计
export function appApplicationCountApi(data) {
  return axios.post('project-manage/contract/appApplicationCount', data);
}

//-验收申请
export const contractApi = {
  //合同管理-合同验收-验收申请-分页查询
  list: function (data) {
    return axios.post(`${manage}/contract/loadPageApplication`, data)
  },
  //合同管理-合同验收-验收申请-新增修改
  save: function (data) {
    return axios.post(`${manage}/contract/saveOrUpdateApplication`, data)
  },
  //合同管理-合同验收-验收申请-删除
  del: function (data) {
    return axios.post(`${manage}/contract/delApplication`, data)
  },
  //合同管理-合同验收-验收申请节点-流程图
  nodeList: function (data) {
    return axios.post(`${manage}/contract/loadApplicationNodes`, data)
  },
  //合同管理-合同验收-验收申请节点-通过
  passApplicationNode: function (data) {
    return axios.post(`${manage}/contract/passApplicationNode`, data)
  },
  //合同管理-合同验收-验收申请节点-驳回
  rejectApplicationNode: function (data) {
    return axios.post(`${manage}/contract/rejectApplicationNode`, data)
  },
  //合同管理-合同验收-验收申请统计
  countApplication: function (data) {
    return axios.post(`${manage}/contract/countApplication`, data)
  },
  //合同管理-合同验收-验收申请流程节点提醒
  reminderApplicationNode: function (data) {
    return axios.post(`project-manage/contract/reminderApplicationNode`, data)
  },
  //合同管理-合同验收-验收申请流程节点提醒
  reminderApplicationNode: function (data) {
    return axios.post(`project-manage/contract/reminderApplicationNode`, data)
  },
}

//-消息提醒
export const messageApi = {
  //消息提醒
  list: function (data) {
    return axios.post(`project-manage/message/loadMessage`, data)
  },
  //设置消息提醒已读
  send: function (data) {
    return axios.post(`project-manage/message/readMessage`, data)
  },
}

// 考核管理 -督察检查
export const supervisedCheck = {
  // 考核管理-督察检查-运行单位 写死deptIds 67011533 或水主管单位 写死deptIds67038494
  deptList: function (data) {
    return axios.post("/project-manage/deptCustom/deptList", data);
  },
  //新增
  save: function (data) {
    return axios.post("project-manage/contract/saveOrUpdateInspection", data);
  },

}

// 物业管理-人员
export const propertyPeopleApi = {
  // 列表
  list: function (data) {
    return axios.post("/ums/sys/getWyhUserList", data);
  },
  // 列表
  unitList: function (data) {
    return axios.post("/project-manage/professionalCompany/list", data);
  },
};

//工程列表
export function getAllProjectApi(data) {
  return axios.post('project-manage/companyBindingPrcd/getAllProject', data);
}

//1是督办 2是处理 3是归档
export function handingInspectionApi(data) {
  return axios.post("project-manage/contract/handingInspection", data);
}

// 考核管理 -督察检查
export const superviseApi = {
  // 督察检查总数
  counts: function (data) {
    return axios.post("project-manage/contract/appInspectionCount", data);
  },
  //评价
  savePjById: function (data) {
    return axios.post("project-manage/overseeEvaluation/savePjById", data);
  },
  //判断是否是水利局的
  getWaterDept: function (data) {
    return axios.post("project-manage/overseeEvaluation/getWaterDept", data);
  },

}

