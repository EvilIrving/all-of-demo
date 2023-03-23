import axios from "../config/httpRequset";
// const pre = '/auth-service';
const pre = '/ums/ums';
//档案管理

// 获取归档目录（树形结构）
export function getSysAchievement() {
  return axios.get(pre + "/sysAchievement");
}


//添加归档目录信息
export function addSysAchievement(data) {
  return axios.post(pre + "/sysAchievement", data);
}

//删除指定目录
export function deleteSysAchievement(id) {
  return axios.delete(pre + `/sysAchievement/${id}`);
}

//更新归档目录信息
export function updateSysAchievement(data) {
  return axios.put(pre + '/sysAchievement', data);
}

//查找目录下的文件
export function getCatalogFile(data) {
  return axios.post(pre + '/sysAchievement/files', data);
}


// 档案管理----------新


//档案统计-档案数据统计
export function getDataStatisApi(data) {
  return axios.post(pre + '/sysFile/archivalDataStatistics', data);
}
//档案统计-档案总量统计
export function getTotaFileDataApi(data) {
  return axios.post(pre + '/sysAchievement/statisticsTotalArchives', data);
}
//档案统计-数字档案上传曲线图
export function getUploadChartDataApi(data) {
  return axios.post(pre + '/sysFile/statUploadFile', data);
}
//档案统计-访问量排行
export function getTotalFileRankApi(data) {
  return axios.post(pre + '/SysArchiveLog/trafficFileRank', data);
}
//档案统计-数字档案访问量曲线图
export function getTrafficChartDataApi(data) {
  return axios.post(pre + '/SysArchiveLog/trafficFileGraph', data);
}


//档案管理-数据整编-左侧文件树
export function getFileTree(data) {
  return axios.post(pre + '/sysAchievement/achievementStructure', data);
  // return axios.post(pre + '/sysFile/viewFile', data);
}
export function lookFileLogApi(data) {
  return axios.post(pre + '/sysFile/viewFile', data);
}
//档案管理-数据整编-文件参数类型
export function getFileTypeDataApi(data) {
  return axios.post(pre + '/sysFile/fileParameterType', data);
}
//档案管理-数据整编-文件表格数据
export function getUpFileListApi(data) {
  return axios.post(pre + '/sysFile/pageList', data);
}
//档案管理-数据整编-收藏文件表格数据
export function getCollectFileListApi(data) {
  return axios.post(pre + '/SysArchiveCollection/pageList', data);
}
//档案管理-数据整编-文件收藏
export function collectFileApi(data) {
  return axios.post(pre + '/SysArchiveCollection/insert', data);
}
//档案管理-数据整编-文件取消收藏
export function unfavoriteApi(data) {
  return axios.post(pre + '/SysArchiveCollection/cancelCollection', data);
}
//档案管理-我的收藏-文件取消收藏
export function unCollectApi(data) {
  return axios.post(pre + '/SysArchiveCollection/delete', data);
}
//档案管理-数据整编-文件删除
export function deleteUpFileApi(data) {
  return axios.post(pre + '/sysFile/delFile', data);
}
//档案管理-数据整编-文件更新
export function updateUpFileApi(data) {
  return axios.post(pre + '/sysFile/updateFile', data);
}
//档案管理-数据整编-文件下载
export function downloadFileApi(data) {
  return axios.get(pre + '/sysFile/downloadFile', data);
}


//档案管理-日志查询-左侧时间列表
export function getTimeListApi(data) {
  return axios.post(pre + '/SysArchiveLog/archiveLogTimeParameter', data);
}
//档案管理-日志查询-日志访问表格数据
export function getFileListApi(data) {
  return axios.post(pre + '/SysArchiveLog/pageList', data);
}


//权限管理-权限管理-目录表格数据
export function getDirectoryListApi(data) {
  return axios.post(pre + '/sysAchievement/pageList', data);
}
// //权限管理-权限管理-根据id查找目录
// export function getDirectoryByIdApi(data){
//     return axios.post(pre + '/SysArchiveLabel/findById', data);
// }
//权限管理-权限管理-保存目录
export function addDirectoryApi(data) {
  return axios.post(pre + '/sysAchievement/insert', data);
}
//权限管理-权限管理-更新目录
export function updateDirectoryApi(data) {
  return axios.post(pre + '/sysAchievement/update', data);
}
//权限管理-权限管理-删除目录
export function deleteDirectoryApi(data) {
  return axios.post(pre + '/sysAchievement/delete', data);
}
//权限管理-权限管理-人员选择-部门结构树
export function getDepartTreeApi(data) {
  return axios.get('/authentication-system/sys/archiveDept', data);
}
//权限管理-权限管理-修改权限
export function addJurisdictionApi(data) {
  return axios.post(pre + '/SysAchievementJurisdiction/insert', data);
}


//权限管理-标签管理-表格数据
export function getLabelDataApi(data) {
  return axios.post(pre + '/SysArchiveLabel/pageList', data);
}
//权限管理-标签管理-根据id查找
export function getLabelByIdApi(data) {
  return axios.post(pre + '/SysArchiveLabel/findById', data);
}
//权限管理-标签管理-保存
export function addLabelApi(data) {
  return axios.post(pre + '/SysArchiveLabel/insert', data);
}
//权限管理-标签管理-删除
export function deleteLabelApi(data) {
  return axios.post(pre + '/SysArchiveLabel/delete', data);
}
//工程列表
export function getAllProjectApi(data) {
  return axios.post('project-manage/companyBindingPrcd/getAllProject', data);
}
//获取科室列表--树
export function getDeptList() {
  return axios.get("/auth-service/dept/deptTree");
}
//获取用户列表
export function getUserList(data) {
  return axios.post("/auth-service/user/pageInfo", data);
}
//获取所有工程
export function getAllProjectList(data) {
  return axios.post("/project-manage/companyBindingPrcd/getAllProject ", data);
}
// 物业管理-公司
export const propertyUnitApi = {
  // 列表
  list: function (data) {
    return axios.post("/project-manage/professionalCompany/list", data);
  },
  // 保存
  save: function (data) {
    return axios.post("/project-manage/professionalCompany/saveOrUpdate", data);
  },
  // 删除
  del: function (data) {
    return axios.get("/project-manage/professionalCompany/delete", data);
  },
  properList: function (data) {
    return axios.get("/ums/sys/getDeptTreeTableList", { pid: 67011533 });
  },
  userList: function (data) {
    return axios.post("/ums/sys/dept/users", data);
  }
};

// 物业管理-人员
export const propertyPeopleApi = {
  // 列表
  list: function (data) {
    return axios.post("/ums/sys/getWyhUserList", data);
  },
  // 保存
  save: function (data) {
    return axios.post("/ums/sys/saveWyhUser ", data);
  },
  // 删除
  del: function (data) {
    return axios.get("/ums/sys/delWyhUser", data);
  },
  // 修改密码
  pwd: function (data) {
    return axios.post("/ums/sys/wyhUser/pwd", data);
  },
};

// 物业管理-绑定单位
export const propertyProjectApi = {
  // 查询
  detail: function (data) {
    return axios.post("/project-manage/companyBindingPrcd/getBindingPrcd", data);
  },
  // 保存
  save: function (data) {
    return axios.post("/project-manage/companyBindingPrcd/save", data);
  },
};


// 物业管理-责任人
export const chargePeopleApi = {
  // 分页查询三个责任人
  queryByPage: function (data) {
    return axios.post("/project-manage/threepeople/queryPage", data);
  },
  // 单条查询三个责任人
  query: function (data) {
    return axios.post("/project-manage/threepeople/queryOne", data);
  },
  // 新增三个责任人
  save: function (data) {
    return axios.post("/project-manage/threepeople/saveOrUpdate", data);
  },
};

// 物业管理-事项
export const matterApi = {
  // 调度运行-分页查找
  queryByPage: function (data) {
    return axios.post("project-manage/runAction/loadPageDisDom", data);
  },
  // 调度运行-删除
  del: function (data) {
    return axios.post("project-manage/runAction/deleteDisDom", data);
  },
  // 新增更新
  save: function (data) {
    return axios.post("project-manage/runAction/saveOrUpdateDisDom", data);
  },
  // 闸门状态-查找
  queryBySluiceGate: function (data) {
    return axios.post("project-manage/runAction/loadGate", data);
  },
  // 闸门状态-新增或更新
  sluiceGateSave: function (data) {
    return axios.post("project-manage/runAction/saveOrUpdateGate", data);
  },
  // 闸门状态-删除
  sluiceGateDelete: function (data) {
    return axios.post("project-manage/runAction/deleteGate", data);
  },
  // 泄洪洞状态-查找
  queryBySpillwayTunnel: function (data) {
    return axios.post("project-manage/runAction/loadSluiceway", data);
  },
  // 泄洪洞状态-新增或更新
  spillwayTunnelSave: function (data) {
    return axios.post("project-manage/runAction/saveOrUpdateSluiceway", data);
  },
  // 泄洪洞状态-查找
  spillwayTunnelDelete: function (data) {
    return axios.post("project-manage/runAction/deleteSluiceway", data);
  },
};

// 物业管理-管理单位
export const operationUnit = {
  // 管理单位-单位职责新增
  saveOrUpdateUnit: function (data) {
    return axios.post("/project-manage/operationUnit/saveOrUpdateUnit", data);
  },
  // 管理单位-单位职责删除
  delUnit: function (data) {
    return axios.post("/project-manage/operationUnit/delUnit", data);
  },
  // 管理单位-单位职责查询
  queryUnit: function (data) {
    return axios.post("/project-manage/operationUnit/queryUnit", data);
  },
  // 管理单位-岗位设置删除
  delPosition: function (data) {
    return axios.post("/project-manage/operationUnit/delPosition", data);
  },
  // 管理单位-岗位设置单条查询
  queryPosition: function (data) {
    return axios.post("/project-manage/operationUnit/queryPosition", data);
  },
  //管理单位-岗位设置分页查询
  queryPositions: function (data) {
    return axios.post("/project-manage/operationUnit/queryPositions", data);
  },
  // 管理单位-岗位设置新增
  saveOrUpdatePosition: function (data) {
    return axios.post("/project-manage/operationUnit/saveOrUpdatePosition", data);
  },
  // 管理单位-岗位职责查询
  queryJob: function (data) {
    return axios.post("/project-manage/operationUnit/queryJob", data);
  },
  // 管理单位-岗位职责删除
  delJob: function (data) {
    return axios.post("/project-manage/operationUnit/delJob", data);
  },
  // 管理单位-岗位职责新增
  saveOrUpdateJob: function (data) {
    return axios.post("/project-manage/operationUnit/saveOrUpdateJob", data);
  },
  // 管理单位-服务事项分页查找
  servicelist: function (data) {
    return axios.post("/project-manage/serviceItem/loadPage", data);
  },
  // 管理单位-服务事项新增或修改
  servicesave: function (data) {
    return axios.post("/project-manage/serviceItem/saveOrUpdate", data);
  },
  // 管理单位-服务事项删除
  servicedel: function (data) {
    return axios.get("/project-manage/serviceItem/del", data);
  },
  // 管理单位-所有工程
  allProjects: function (data) {
    return axios.post("/project-manage/operationUnit/allProjects", data);
  },
  // 管理单位-鹿城区街道
  lcArea: function (data) {
    return axios.post("/project-manage/operationUnit/lcArea", data);
  },

};
// 合同管理
export const contractOperate = {
  // 合同验收-年度验收-分页查询
  AAlist: function (data) {
    return axios.post("/project-manage/contract/loadPageAnnualAcceptance", data);
  },
  // 合同验收-年度验收-删除
  AAdel: function (data) {
    return axios.post("/project-manage/contract/delAnnualAcceptance", data);
  },
  // 合同验收-年度验收-新增修改
  AAsaveOrUpdate: function (data) {
    return axios.post("/project-manage/contract/saveOrUpdateAnnualAcceptance", data);
  },
  //合同管理-约定验收标准-约定事项-分页查询
  loadPageCovenant: function (data) {
    return axios.post("/project-manage/contract/loadPageCovenant", data);
  },
  //合同管理-约定验收标准-约定事项-删除
  delCovenant: function (data) {
    return axios.post("/project-manage/contract/delCovenant", data);
  },
  //合同管理-约定验收标准-约定事项-新增修改
  saveOrUpdateCovenants: function (data) {
    return axios.post("/project-manage/contract/saveOrUpdateCovenants", data);
  },
  //合同管理-约定验收标准-验收标准-分页查询
  loadPageCriteria: function (data) {
    return axios.post("/project-manage/contract/loadPageCriteria", data);
  },
  //合同管理-约定验收标准-验收标准-删除
  delCriteria: function (data) {
    return axios.post("/project-manage/contract/delCriteria", data);
  },
  //合同管理-约定验收标准-验收标准-新增修改
  saveOrUpdateCriteria: function (data) {
    return axios.post("/project-manage/contract/saveOrUpdateCriteria", data);
  },
}

//业主单位
export function deptListApi(data) {
  return axios.post("/project-manage/deptCustom/deptList", data);
}

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
  // 个人考勤详情
  attendancePeople: function (data) {
    return axios.get("/project-manage/attendance/attendancePeople", data);
  },
  // 个人考勤曲线分析
  curveAnalysis: function (data) {
    return axios.get("/project-manage/attendance/curveAnalysis", data);
  },
  //考勤分析列表
  attendanceList: function (data) {
    return axios.post("/project-manage/attendance/attendanceAnalysis", data);
  },

}

export const regularCheck = {
  //  定期考评列表
  list: function (data) {
    return axios.post("/project-manage/regularReview/list", data);
  },
  //  定期考评删除
  del: function (data) {
    return axios.get("/project-manage/regularReview/del", data);
  },
  // 定期考评新增或修改
  save: function (data) {
    return axios.postJson("/project-manage/regularReview/saveOrUpdate", data);
  },
  // 定期考评数据详情
  getById: function (data) {
    return axios.get("/project-manage/regularReview/getById", data);
  },
  // 年度考评统计的列表
  yearList: function (data) {
    return axios.post("/project-manage/regularReview/yearList", data);
  },
  // 查询模板
  mbList: function (data) {
    return axios.post("/project-manage/regularReview/mbList", data);
  },
}

//人员培训
export const staffTraining = {
  //  人员培训-分页查询
  list: function (data) {
    return axios.post("/project-manage/persontrain/loadPagePersonTrain", data);
  },
  // 人员培训-删除
  del: function (data) {
    return axios.post("/project-manage/persontrain/delPersonTrain", data);
  },
  // 人员培训-新增修改
  save: function (data) {
    return axios.post("/project-manage/persontrain/saveOrUpdatePersonTrain", data);
  },
}

//定期监测
export const regularMonitor = {
  //  定期监测-分页查询
  list: function (data) {
    return axios.post("/project-manage/projMonitor/regularMonitorList", data);
  },
  // 定期监测-删除
  del: function (data) {
    return axios.get("/project-manage/projMonitor/regularDelete", data);
  },
  // 定期监测-新增修改
  save: function (data) {
    return axios.post("/project-manage/projMonitor/regularSaveOrUpdate", data);
  },
}

//定期监测
export const bhControlPlan = {
  //  控运计划查询
  list: function (data) {
    return axios.post("/project-manage/bhControlPlan/loadPage", data);
  },
  // 定期监测-删除
  del: function (data) {
    return axios.post("/project-manage/bhControlPlan/del", data);
  },
  // 控运计划新增或修改
  save: function (data) {
    return axios.post("/project-manage/bhControlPlan/saveOrUpdate", data);
  },
}
export const staffAttendance = {
  //人员考勤
  list: function (data) {
    return axios.get('/project-manage/attendance/attendanceCalendar', data)
  },

}

//建设单位
export function getDeptTreeTableListApi() {
  return axios.get("/ums/sys/getDeptTreeTableList?pid=67011533");
}