import axios from "../config/httpRequset";
const pre = '/river-service';
const pre1 = '/hydrology-digitization';
export const projectSupervisionApi = {
  // 行政区划
  getAdcdList: function (data) {
    return axios.post("graph/adcd", data);
  },
  
  /**
   * 蓝线管理异常信息配置条目接口
   */

  // 条件筛选监管项目列表
  projectList: function (data) {
    return axios.post(pre + "/blueProj/projectList", data);
  },
  // 根据行政区划获取河流
  getRiver: function (data) {
    return axios.get(pre + "/blueProj/getRiver", data);
  },
  // 选择河道
  getRiverList: function (data) {
    return axios.get(pre + "/riverInfoV2/pageRivSegBaseInfo", data);
  },
  getRiverListByAreaCode: function (data) {
    return axios.get(pre + "/blueProj/getRiver", data);
  },
  // 首页统计
  homeOverView: function (data) {
    // return axios.get(pre + "/blueProj/homeOverView", data);
    return axios.get(pre + "/blueProj/projectStaticsForHome", data);
  },
  // 新增监管项目
  addProject: function (data) {
    return axios.post(pre + "/blueProj/addProject", data);
  },
  // 根据id删除项目
  delete: function (data) {
    return axios.get(pre + "/blueProj/delete", data);
  },

  /**
   * 蓝线管理异常信息配置条目接口
   */

  // 根据条件筛选获取异常配置项列表
  exInfoList: function (data) {
    return axios.post(pre + "/blueExInfo/exInfoList", data);
  },
  // 更新异常信息配置条目
  updateExInfo: function (data) {
    return axios.post(pre + "/blueExInfo/updateExInfo", data);
  },
  // 根据id删除异常信息配置条目
  deleteExInfo: function (data) {
    return axios.get(pre + "/blueExInfo/deleteExInfo", data);
  },
  // 新增异常信息配置条目
  addExInfo: function (data) {
    return axios.post(pre + "/blueExInfo/addExInfo", data);
  },
  // 升降序某一配置项
  upOrDown: function (data) {
    return axios.get(pre + "/blueExInfo/upOrDown", data);
  },

  /**
   * 蓝线管理巡查接口
   */

  //  新增蓝线管理巡查
  addInspection: function (data) {
    return axios.post(pre + "/blueInspect/addInspection", data);
  },
  // 获取部门人员树
  getDeptUserTree: function (data) {
    return axios.get(pre + "/blueInspect/getDeptUserTree", data);
  },
  // 部门树形结构
  WarnDeptFindTree: function (data) {
    return axios.post(pre1 + "/WarnDept/findTree", data);
  },
  // 更新蓝线管理
  updateInspection: function (data) {
    return axios.post(pre + "/blueInspect/updateInspection", data);
  },

  /**
   * 蓝线管理巡查异常问题接口
   */

  //  新增巡查异常问题
  addedBlueLineProblem: function (data) {
    return axios.post(pre + "/blueProblem/新增蓝线问题", data);
  },
  // 设置改巡查问题异常，或者正常
  setProblem: function (data) {
    return axios.get(pre + "/blueProblem/setProblem", data);
  },
  // 查看当前项目详情，根据项目id
  detail: function (data) {
    return axios.get(pre + "/blueProj/detail", data);
  },
  // 获取问题列表根据巡查id
  getList: function (data) {
    return axios.get(pre + "/blueProblem/getList", data);
  },
  // 复核派发
  review: function (data) {
    return axios.post(pre + "/blueInspect/review", data);
  },
  // 新增巡查异常问题
  addProblem: function (data) {
    return axios.post(pre + "/blueProblem/addProblem", data);
  },
  // 确认是否隐患
  hiddenDanger: function (data) {
    return axios.get(pre + "/blueInspect/hiddenDanger", data);
  },
  // 案件结办
  caseClosed: function (data) {
    return axios.post(pre + "/blueInspect/caseClosed", data);
  },
  updateProject: function (data) {
    return axios.post(pre + "/blueProj/updateProject", data);
  },

  //  根据项目查询对应的举报信息列表
  getInformList: function (data) {
    return axios.post(pre + "/projectInform/getInformList", data);
  },
}

export const approvalProject = {
  list: function (data) {
    return axios.post("/project-manage/investProj/list", data);
  },
  edit: function (data) {
    return axios.post("/project-manage/investProj/edit", data);
  },
  editPlan: function (data) {
    return axios.post("/project-manage/investProj/editPlan", data);
  },
  queryPlan: function (data) {
    return axios.post("/project-manage/investProj/queryPlan", data);
  },
  editConstruction: function (data) {
    return axios.post("/project-manage/investProj/editConstruction", data);
  },
  queryConstruction: function (data) {
    return axios.post("/project-manage/investProj/queryConstruction", data);
  },
  editFinance: function (data) {
    return axios.post("/project-manage/investProj/editFinance", data);
  },
  queryFinance: function (data) {
    return axios.post("/project-manage/investProj/queryFinance", data);
  },
}



