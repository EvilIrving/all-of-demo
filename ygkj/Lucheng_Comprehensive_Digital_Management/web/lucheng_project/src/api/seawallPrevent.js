import axios from "../config/httpRequset";
import http from "../config/httpRequset";

// 数据字典查询枚举
export function getDict(data) {
  return http.post("/flood-protect/dict/dicts?dictCode=" + data);
}

export const disasterPreventionApi = {
  // 按工程统计摄像头
  statsCameraByProject: function (data) {
    return axios.get("/project-manage/cameraOps/statsCameraByProject", data);
  },
  // 统计摄像头在线离线状态
  countCameraStatus: function (data) {
    return axios.get("/project-manage/cameraOps/countCameraStatus", data);
  },
  // 查询所有的摄像头
  selectCamera: function (data) {
    return axios.get("/project-manage/cameraOps/selectCamera", data);
  },
  // 查询所有摄像头列表
  listCamera: function (data) {
    return axios.get("/project-manage/cameraOps/pageCamera", data);
  },
  // 查询所有摄像头列表统计
  stats4PageCamera: function (data) {
    return axios.get("/project-manage/cameraOps/stats4PageCamera", data);
  },
  //救援仓库
  warehousesList: function (data) {
    return http.post("/flood-protect/warehouse/warehouses", data);
  },
};

// 海塘工程名录
export const seawallListApi = {
  // 列表
  list: function (data) {
    return axios.get("/project-manage/seawall/pageSeawall", data);
  },
  // 海塘预警
  htyj: function (data) {
    return axios.get("/big-screen/giveAnAlarm/seawallWarning", data);
  },
  // 海塘报警
  htbj: function (data) {
    return axios.get("/big-screen/giveAnAlarm/seawallGiveAnAlarm", data);
  },
  // 安全鉴定
  aqjd: function (data) {
    return axios.get("/project-manage/seawall/seawallAqjd", data);
  },
  // 海塘等级
  htdj: function (data) {
    return axios.get("/project-manage/seawall/seawallHtdj", data);
  },
};

// 基础信息管理-避灾点管理
export const basMsgBzdApi = {
  // 列表
  list: function (data) {
    return axios.post("/big-screen/avoidancePoint/pageList", data);
  },
  // 保存
  save: function (data) {
    return axios.post("/big-screen/avoidancePoint/saveOrUpdate", data);
  },
  // 删除
  del: function (data) {
    return axios.post("/big-screen/avoidancePoint/delete", data);
  },
};

// 培训演练管理
export const trainingExercisesApi = {
  // 演练管理列表
  rehearsalList: function (data) {
    return axios.post("/project-manage/practiceManagement/list", data);
  },
  // 演练管理新增修改
  rehearsalSave: function (data) {
    return axios.post("/project-manage/practiceManagement/saveOrUpdate", data);
  },
  // 演练管理删除
  rehearsalDelete: function (data) {
    return axios.post("/project-manage/practiceManagement/delete", data);
  },
  // 演练详情
  rehearsalDetail: function (data) {
    return axios.post("/project-manage/practiceManagement/getById", data);
  },
  // 培训管理列表
  trainingList: function (data) {
    return axios.post("/project-manage/trainingManagement/list", data);
  },
  // 培训管理新增修改
  trainingSave: function (data) {
    return axios.post("/project-manage/trainingManagement/saveOrUpdate", data);
  },
  // 培训管理删除
  trainingDelete: function (data) {
    return axios.post("/project-manage/trainingManagement/delete", data);
  },
  // 培训详情
  trainingDetail: function (data) {
    return axios.post("/project-manage/trainingManagement/getById", data);
  },
};

// 风暴潮预报
export const stormSurgeWarningApi = {
  // 风暴潮预报列表
  list: function (data) {
    return axios.get("/digitization/tideFcst/pageTideFcst", data);
  },
  // 风暴潮站点列表
  stationList: function (data) {
    return axios.get("/digitization/tideFcst/listTileFcstSt/" + data);
  },
  // 风暴潮列表
  stormSurgeList: function (data) {
    return axios.post("/digitization/tideFcst/listTileFcstVal", data);
  },
  // 新增 郭公山站点
  addGuoGongShan: function (data) {
    return axios.postJson("/digitization/tideFcst/addOrUpdateTideFcst", data);
  },
  //断面配置-列表
  getSectionList: function (data) {
    return axios.post("/project-manage/sectionConfiguration/list", data)
  },
  //断面配置-新增/编辑
  editSectionList: function (data) {
    return axios.post('/project-manage/sectionConfiguration/saveOrUpdate', data)
  },
  //断面配置-删除
  delSectionConfiguration: function (data) {
    return axios.get('/project-manage/sectionConfiguration/del', data)
  },
  //风速、天文潮查询
  getAcquisition: function (data) {
    return axios.post('/project-manage/sectionConfiguration/automaticAcquisition', data)
  },
  //预测断面列表
  forecastSectionList: function (data) {
    return axios.post('/project-manage/sectionConfiguration/forecastSectionPage', data)
  },
  //预测断面详情
  forecastSectionInfo: function (data) {
    return axios.get('/project-manage/sectionConfiguration/forecastSectionInfo', data)
  }

};

// 基础信息-工程监控
export const basMsgGcjkApi = {
  // 查询各个站点数量
  stationNum: function (data) {
    return axios.get("/project-manage/projectRel/countRel/" + data);
  },
  // 分页查询工程(未)绑定的站点
  list: function (data) {
    return axios.post("/project-manage/projectRel/pageProjRel", data);
  },
  // 设置(取消)主站
  updateMainSt: function (data) {
    return axios.post("/project-manage/projectRel/updateMainSt", data);
  },
  // 编辑工程站点绑定关系
  updateProjRel: function (data) {
    return axios.postJson("/project-manage/projectRel/updateProjRel", data);
  },
  //解除工程绑定关系
  delProjRel: function (data) {
    return axios.post("/project-manage/projectRel/delProjRel", data);
  },
};

// 风险预警管理-规则配置
export const ruleConfiguration = {
  // 列表
  list: function (data) {
    return axios.post("/project-manage/warningRule/list", data);
  },
  stList: function (data) {
    return axios.post("/project-manage/warningRule/stList", data);
  },
  stListByType: function (data) {
    return axios.post("/project-manage/warningRule/listRuleSt", data);
  },
  // 雨量站关联站点列表
  rainlistByType: function (data) {
    return axios.post("/graph/waterAndRain/rainfall", data);
  },
  // 根据id
  getById: function (data) {
    return axios.get("/project-manage/warningRule/getById", data);
  },
  // 新增
  save: function (data) {
    return axios.post("/project-manage/warningRule/insert", data);
  },
  // 删除
  del: function (data) {
    return axios.get("/project-manage/warningRule/delete", data);
  },
  // 更新
  update: function (data) {
    return http.post("/project-manage/warningRule/update", data);
  },
  // 导出
  exportList: function (data) {
    return axios.get("/project-manage/warningRule/exportList", data);
  },
};
// 风险预警管理-报警管理
export const alarmManagement = {
  // 列表
  list: function (data) {
    return axios.post("/project-manage/warningInfo/list", data);
  },
  // 单条数据
  getById: function (data) {
    return axios.get("/project-manage/warningInfo/getById", data);
  },
  // 转发
  forward: function (data) {
    return axios.post("/project-manage/warningInfo/saveOrUpdate", data);
  },
};
// 预警自定义部门
export const customDep = {
  customTree: function (data) {
    return axios.post("/project-manage/deptCustom/customTree", data);
  },
  zzdTree: function (data) {
    return axios.post("/project-manage/deptCustom/zzdTree", data);
  },
  delete: function (data) {
    return axios.get("/project-manage/deptCustom/delete", data);
  },
  insert: function (data) {
    return axios.post("/project-manage/deptCustom/insert", data);
  },
  update: function (data) {
    return axios.post("/project-manage/deptCustom/update", data);
  },
};
// 预警人员
export const customPeople = {
  // 根据id删除预警人员
  delete: function (data) {
    return axios.get("/project-manage/warningUser/delete", data);
  },
  // 导出
  exportWarning: function (data) {
    return axios.get("/project-manage/warningUser/exportWarning", data);
  },
  // 新增预警人员
  insert: function (data) {
    return axios.post("/project-manage/warningUser/insert", data);
  },
  // 根据deptid查询预警人员列表
  list: function (data) {
    return axios.post("/project-manage/warningUser/list", data);
  },
  // 更新预警人员
  update: function (data) {
    return axios.post("/project-manage/warningUser/update", data);
  },
};

// 获取带科室的人员列表
export function getCustomPeopleWithDepartment(data) {
  return axios.post("/project-manage/warningUser/userList", data);
}

// 获取全部工程列表
export function getAlleEngineering(data) {
  return axios.post("/project-manage/warningRule/projectList", data);
}

// 督办中心
export const supervisionCenter = {
  // 列表
  list: function (data) {
    return axios.post("/project-manage/oversee/page", data);
  },
  // 删除
  delete: function (data) {
    return axios.delete(`/project-manage/oversee/del/${data}`);
  },
  // 更新
  update: function (data) {
    return axios.post("/project-manage/oversee/update", data);
  },
  //新增
  save: function (data) {
    return axios.post("/project-manage/oversee/save", data);
  },
  // 督办登陆
  loginByOverseeWorkId: function (workId) {
    return axios.get(`/project-manage/oversee/loginByOverseeWorkId/${workId}`);
  },
  // // 督办登陆
  // loginByOverseeWorkId: function (workId) {
  //   return axios.get(`/project-manage/oversee/loginByOverseeWorkId/${workId}`);
  // },
};
// 组织模块-用户管理
export const userController = {
  // 列表
  userslist: function (data) {
    return axios.get("/ums/sys/users", data);
  },
};

// 测站管理
export const stController = {
  // 列表
  page: function (data) {
    return axios.post("/project-manage/st/page", data);
  },
  // 列表
  update: function (data) {
    return axios.post("/project-manage/st/update", data);
  },
};

// 海塘管理
export const seaController = {
  // 列表
  page: function (data) {
    return axios.get("/project-manage/seawall/pageSeawall", data);
  },
  // 列表
  update: function (data) {
    return axios.post("/project-manage/seawall/editWarningLevel", data);
  },
};
// 基础信息管理-专家库管理
export const basMsgZjkApi = {
  // 水利专家列表
  list: function (data) {
    return axios.post("/project-manage/waterexpert/listWaterExpert", data);
  },
  // 查询水利专家详情
  details: function (data) {
    return axios.get("/project-manage/waterexpert/selectWaterExpert", data);
  },
  // 新增或修改水利专家
  save: function (data) {
    return axios.post("/project-manage/waterexpert/editWaterExpert", data);
  },
  // {dtsCmfId}删除水利专家
  del: function (data) {
    return axios.delete("/project-manage/waterexpert/delWaterExpert/" + data);
  },
};

// 基础信息管理-知识库管理
export const basMsgZskApi = {
  // 分页查找
  list: function (data) {
    return axios.post("/project-manage/lbKnowledge/loadPage", data);
  },
  // 新增或修改
  save: function (data) {
    return axios.put("/project-manage/lbKnowledge/saveOrUpdate", data);
  },
  // 删除
  del: function (data) {
    return axios.delete("/project-manage/lbKnowledge/del?id=" + data);
  },
};

// 基础信息管理-救援队伍管理
export const rescueTeamApi = {
  // 分页查找
  list: function (data) {
    return axios.post("/flood-protect/team/teams", data);
  },
  // 新增
  insert: function (data) {
    return axios.put("/flood-protect/team/insertTeam", data);
  },
  // 修改
  update: function (data) {
    return axios.put("/flood-protect/team/updateTeam", data);
  },
  // 删除
  del: function (data) {
    return axios.post("/flood-protect/team/deleteTeam", data);
  },
};

// 基础信息管理-救援队伍人员管理
export const rescueTeamPeopleApi = {
  // 分页查找
  list: function (data) {
    return axios.get("/flood-protect/team/teamPersons", data);
  },
  // 保存
  save: function (data) {
    return axios.put("/flood-protect/team/insertPerson", data);
  },
  // 删除
  del: function (data) {
    return axios.put("/flood-protect/team/deletePerson", data);
  },
};

// 基础信息管理-救援队伍设备管理
export const rescueTeamEquipApi = {
  // 分页查找
  list: function (data) {
    return axios.get("/flood-protect/team/teamDeviceList", data);
  },
  // 新增
  insert: function (data) {
    return axios.post("/flood-protect/team/insertteamDevice", data);
  },
  // 修改
  update: function (data) {
    return axios.post("/flood-protect/team/updateteamDevice", data);
  },
  // 删除
  del: function (data) {
    return axios.post("/flood-protect/team/deleteDevice", data);
  },
};

// 基础信息管理-救援仓库管理
export const rescueWarehouseApi = {
  // 分页查找
  list: function (data) {
    return axios.post("/flood-protect/warehouse/warehouses", data);
  },
  // 新增
  insert: function (data) {
    return axios.put("/flood-protect/warehouse/insertWarehouse", data);
  },
  // 修改
  update: function (data) {
    return axios.put("/flood-protect/warehouse/updateWarehouse", data);
  },
  // 删除
  del: function (data) {
    return axios.post("/flood-protect/warehouse/delete", data);
  },

};

// 基础信息管理-救援仓库物资管理
export const rescueMaterialApi = {
  // 分页查找
  list: function (data) {
    return axios.post("/flood-protect/warehouse/selectDevice", data);
  },
  warehouseStaticsList: function (data) {
    return axios.get("/flood-protect/attWarehouse/warehouseStaticsList", data);
  },
  // 保存
  save: function (data) {
    return axios.post("/flood-protect/warehouse/insertOrUpdateDevice", data);
  },
  // 删除
  del: function (data) {
    return axios.post("/flood-protect/warehouse/deleteDevice", data);
  },
};

// 基础信息管理-出入报废物资管理
export const intoOutScrappedApi = {
  // 入库列表
  intoList: function (data) {
    return axios.post("/flood-protect/attWarehouse/addList", data);
  },
  // 出库列表
  outList: function (data) {
    return axios.post("/flood-protect/attWarehouse/outList", data);
  },
  // 入库新增
  intoStockAdd: function (data) {
    return axios.post("/flood-protect/attWarehouse/warehouseAdd", data);
  },
  // 出库，报废新增
  outScrappedAdd: function (data) {
    return axios.post("/flood-protect/attWarehouse/warehouseOut", data);
  },
  // 物资规格清单
  warehouseMaterialStandardList: function (data) {
    return axios.get("/flood-protect/attWarehouse/warehouseMaterialStandardList", data);
  },
  // 防汛仓库
  warehousesList: function (data) {
    return http.post("/flood-protect/warehouse/warehouses", data);
  },
};


export const getHikvisionTree = {
  boatList: function (data) {
    return axios.post("/project-manage/projectManage/getHikvisionTree", data);
  },
};

export const getTideTree = {
  list: function (data) {
    return axios.post("/graph/waterAndRain/waterStations", data);
  },
};
//基础信息-应急总结管理
export const emergencySummary = {
  floodPreventionBulletin: function (data) {
    return axios.post('/project-manage/emergencySummary/loadPage', data)
  },
  del: function (data) {
    return axios.post("project-manage/emergencySummary/del ", data);
  },

}