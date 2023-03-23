/*
 * @Author: dengzhao
 * @LastEditTime: 2022-06-28 16:04:51
 * @Description: 
 * @FilePath: \lucheng_project\src\api\projectUnitDetail.js
 */
import axios from "../config/httpRequset";
import aside from "element-ui/packages/aside";
import { retrieveColumnLayout } from "echarts/lib/layout/barGrid";
let manage = '/project-manage'
//物业管理事项列表
export function loadSkPageApi(data) {
  return axios.post(`${manage}/serviceItem/loadSkPage`, data)
}
//获取人员
export function usersApi(data) {
  return axios.postJson(`/authentication-system/sys/users`, data)
}
//检修操作
export const operation = {
  loadPageOverhaul: function (data) {
    return axios.post(`${manage}/runAction/loadPageOverhaul`, data)
  },
  delOverhaul: function (data) {
    return axios.post(`${manage}/runAction/delOverhaul`, data)
  },
  saveOrUpdateOverhaul: function (data) {
    return axios.post(`${manage}/runAction/saveOrUpdateOverhaul`, data)
  }
}

//年度维修
export const annulMaintenance = {
  //年度维修养护计划分页查找
  annulList: function (data) {
    return axios.post(`${manage}/maintenance/loadPageMtDmPlan`, data);
  },
  //日常任务创建-分页查找
  DailyTask: function (data) {
    return axios.post(`${manage}/maintenance/loadPageMtDailyTask`, data);
  },
  //维修养护资金-分页查找
  fundList: function (data) {
    return axios.post(`${manage}/maintenance/loadPageMtFund`, data);
  },

  //维修养护计划-新增更新
  updateDmPlan: function (data) {
    return axios.post(`${manage}/maintenance/saveOrUpdateMtDmPlan`, data);
  },
  //日常任务创建-新增更新
  updateDailyTask: function (data) {
    return axios.post(`${manage}/maintenance/saveOrUpdateMtDailyTask`, data);
  },
  //维修养护资金-新增更新
  updateFund: function (data) {
    return axios.post(`${manage}/maintenance/saveOrUpdateMtFund`, data);
  },

  //维修养护计划-删除
  delDmPlan: function (data) {
    return axios.get(`${manage}/maintenance/delMtDmPlan`, data);
  },
  //日常问任务创建-删除
  delDailyTask: function (data) {
    return axios.get(`${manage}/maintenance/delMtDailyTask`, data);
  },
  //维修养护资金-删除
  delFund: function (data) {
    return axios.get(`${manage}/maintenance/delMtFund`, data);
  },

  //日常任务-单位查找-水管单位
  getWaterManageUser: function (data) {
    return axios.post(`/ums/sys/getWaterManageUserList`, data)
  },
  //日常任务-人员查找
  getWyhUserByProject: function (data) {
    return axios.post(`/project-manage/professionalCompany/getWyhUserByProject`, data)
  },
  //日常任务-单位查找-水利局
  getUserData: function (data) {
    return axios.get(`/ums/sys/userList`, data)
  },
  //维修养护资金-申报单位
  getWaterManageUserList: function (data) {
    return axios.post(`/project-manage/professionalCompany/getWyhAndWaterManageList`, data)
  },
}

//隐患处理
export const hiddenDanger = {
  getHiddenDangerDetail: function (data) {
    return axios.get(`${manage}/hiddenDanger/getHiddenDangerDetail`, data);
  },
  implementSubmit: function (data) {
    return axios.post(`${manage}/hiddenDanger/implementSubmit`, data);
  },
  submitProgressInfo: function (data) {
    return axios.post(`${manage}/hiddenDanger/submitProgressInfo`, data);
  }
}

//年度汇总
export function loadPageMtDailyTaskApi(data) {
  return axios.post(`${manage}/maintenance/loadPageMtDailyTask`, data)
}

//工程检查-- 日常检查
export function dailyCheckList(data) {
  return axios.post(`${manage}/projCheck/dailyCheckList`, data)
}

//工程检查-- 巡查轨迹
export function checkTrack(data) {
  return axios.get(`${manage}/projCheck/checkTrack`, data)
}

//汛前检查
export const preFloodInspectionApi = {
  //汛前检查列表查询
  bhLgMpFcList: function (data) {
    return axios.post(`${manage}/projCheck/bhLgMpFcList`, data);
  },
  //汛前检查 - 新增或更新
  saveOrUpdate: function (data) {
    return axios.post(`${manage}/projCheck/saveOrUpdateBhLgMpFc`, data);
  },
  //汛前检查 - 删除
  delete: function (data) {
    return axios.get(`${manage}/projCheck/deleteBhLgMpFc`, data);
  }
}

//汛后检查
export const postFloodInspectionApi = {
  //汛后检查列表查询
  mpAfList: function (data) {
    return axios.post(`${manage}/projCheck/mpAfList`, data);
  },
  //汛后检查 - 新增或更新
  saveOrUpdate: function (data) {
    return axios.post(`${manage}/projCheck/mpAfSaveOrUpdate`, data);
  },
  //汛后检查 - 删除
  delete: function (data) {
    return axios.get(`${manage}/projCheck/id`, data);
  }
}

//年度检查
export const annualInspectionApi = {
  //年度检查列表查询
  bhLgMpAcList: function (data) {
    return axios.post(`${manage}/projCheck/bhLgMpAcList`, data);
  },
  //年度检查 - 新增或更新
  saveOrUpdate: function (data) {
    return axios.post(`${manage}/projCheck/saveOrUpdateBhLgMpAc`, data);
  },
  //年度检查 - 删除
  delete: function (data) {
    return axios.get(`${manage}/projCheck/deleteBhLgMpAc`, data);
  }
}

//特殊检查
export const specialInspectionApi = {
  //特殊检查列表查询
  specialCheckList: function (data) {
    return axios.post(`${manage}/projCheck/specialCheckList`, data);
  },
  //特殊检查 - 新增或更新
  saveOrUpdate: function (data) {
    return axios.post(`${manage}/projCheck/saveOrUpdateSpecialCheck`, data);
  },
  //特殊检查 - 删除
  delete: function (data) {
    return axios.get(`${manage}/projCheck/deleteSpecialCheck`, data);
  }
}

//专项检查
export const specialExaminationApi = {
  //专项检查列表查询
  mpSpList: function (data) {
    return axios.post(`${manage}/projCheck/mpSpList`, data);
  },
  //专项检查 - 新增或更新
  saveOrUpdate: function (data) {
    return axios.post(`${manage}/projCheck/mpSpSaveOrUpdate`, data);
  },
  //专项检查 - 删除
  delete: function (data) {
    return axios.get(`${manage}/projCheck/mpSpDelete`, data);
  }
}
//值班值守
export const beOnDuty = {
  //获取列表
  getOnDutyList: function (data) {
    return axios.post(`${manage}/bhDutyRecord/list`, data)
  },
  //删除列表
  delOnDutyList: function (data) {
    return axios.get(`${manage}/bhDutyRecord/del`, data)
  },
  //新增或更新
  saveOnDuty: function (data) {
    return axios.post(`${manage}/bhDutyRecord/saveOrUpdate`, data)
  },
  //人员列表
  getBhUser: function (data) {
    return axios.post(`project-manage/bhDep/getBhUser`, data)
  },
  //人员导入
  importDuty: function (data) {
    return axios.post(`project-manage/bhDutyRecord/importDuty`, data)
  },
}

//绿化保洁
export const greenCleaning = {
  //获取列表
  getGreenCleaningDetail: function (data) {
    return axios.post(`${manage}/greenCleaning/list`, data);
  },
  //删除
  delGreenCleaning: function (data) {
    return axios.get(`${manage}/greenCleaning/del`, data);
  },
  //新增
  saveGreenCleaning: function (data) {
    return axios.post(`${manage}/greenCleaning/saveOrUpdate`, data);
  },
  //详情
  getGreenCleaning: function (data) {
    return axios.get(`${manage}/greenCleaning/getById`, data)
  }
}

//生物防治
export const biologicalControl = {
  //获取列表
  getBiologicalControlList: function (data) {
    return axios.post(`${manage}/biologicalControl/list`, data)
  },
  //删除
  delBiologicalControl: function (data) {
    return axios.get(`${manage}/biologicalControl/del`, data)
  },
  //更新或新增
  saveBiologicalControl: function (data) {
    return axios.post(`${manage}/biologicalControl/saveOrUpdate`, data)
  },
  //详情
  getBiologicalControlDetail: function (data) {
    return axios.get(`${manage}/biologicalControl/getById`, data)
  }
}

//数据化维护
export const databasedM = {
  //硬件设备列表
  getHardwareMtList: function (data) {
    return axios.post(`${manage}/hardwareMaintenance/list`, data)
  },
  //硬件设备删除
  delHardware: function (data) {
    return axios.get(`${manage}/hardwareMaintenance/del`, data)
  },
  //硬件设备新增更新
  saveHardwareList: function (data) {
    return axios.post(`${manage}/hardwareMaintenance/saveOrUpdate`, data)
  },
  //详情
  getHardwareDetail: function (data) {
    return axios.get(`${manage}/hardwareMaintenance/getById`, data)
  },
  //数据维护列表
  getDataMtList: function (data) {
    return axios.post(`${manage}/dataMaintenance/list`, data)
  },
  //数据维护删除
  delDataMtList: function (data) {
    return axios.get(`${manage}/dataMaintenance/del`, data)
  },
  //数据维护新增
  saveDataMtList: function (data) {
    return axios.post(`${manage}/dataMaintenance/saveOrUpdate`, data)
  },
  //详情
  getDataMtDetail: function (data) {
    return axios.get(`${manage}/dataMaintenance/getById`, data)
  }

}

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
}

export function getFileUrlApi(data) {
  return axios.get(`project-manage/maintenance/getFileUrl`, data)
}
