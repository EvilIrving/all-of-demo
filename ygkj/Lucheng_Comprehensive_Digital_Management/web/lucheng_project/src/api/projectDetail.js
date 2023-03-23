/*
 * @Author: wanxin
 * @LastEditTime: 2022-05-24 12:15:46
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\api\projectDetail.js
 */
import axios from "../config/httpRequset";
const pre = "/project-manage";
// 信息总览
export const perviewApi = {
  // 水位接口
  waterlevel: function (data) {
    return axios.post("/project-manage/overview/profileMap", data);
  },
  // 六个责任人
  sixPeople: function (data) {
    return axios.get(
      "/project-manage/engineeringSupervision/threeCharge",
      data
    );
  },
  //  信息总览的运行监测
  operation: function (data) {
    return axios.get("/project-manage/overview/operation", data);
  },
  //  信息总览俯视图工程数据
  planView: function (data) {
    return axios.get("/project-manage/overview/planView", data);
  },
  // 视频列表
  getVideoList: function (data) {
    return axios.get(pre + "/projectManage/projectCameras/" + data);
  },
  //工程关联测站列表
  getSeawallListSt: function (data) {
    return axios.post(pre + "/projectRel/listSt", data);
  },
  //新增或者修改海塘保护范围线,管保区域
  saveOrUpdate: function (data) {
    return axios.post(pre + "/seawallRange/saveOrUpdate", data);
  },
  //根据海塘编码查询，保护范围线,管保区域
  getRangebyCode: function (data) {
    return axios.get(pre + "/seawallRange/getRangebyCode", data);
  },
  getSeawallWaterLevels: function (data) {
    return axios.post("/graph/waterAndRain/waterLevels", data);
  },
  rainPop: (data) => {
    return axios.post('/graph/waterAndRain/hourRainfall', data);
  },
  getAqjcDatas: (data) => {
    return axios.get('/big-screen/giveAnAlarm/getAqjcData', data);
  }
};
// 标化接过来的数据 维修养护
export const maintenance = {
  // 工程简介
  introduction: function (seawallCode) {
    return axios.get(pre + `/bhDep/introduction/${seawallCode}`);
  },
  // 管理制度文件
  manageinstitution: function (seawallCode) {
    return axios.get(pre + `/bhDep/manageinstitution/${seawallCode}`);
  },
  // 组织架构
  organization: function (seawallCode) {
    return axios.get(pre + `/bhDep/organization/${seawallCode}`, );
  },
  //人员信息
  listPeople: function (data) {
    return axios.post(pre + "/bhDep/listPeople", data);
  },
  //管护经费
  managefund: function (data) {
    return axios.post(pre + "/bhDep/managefund", data);
  },
  //维修养护资金申报
  mfd: function (data) {
    return axios.post(pre + "/bhDep/mfd", data);
  },
  //维修养护项目维护", data);
  mfpm: function (data) {
    return axios.post(pre + "/bhDep/mfpm", data);
  },
  //维修养护资金使用
  mfu: function (data) {
    return axios.post(pre + "/bhDep/mfu", data);
  },
  //组织架构详情
  organizationDetail: function (data) {
    return axios.post(pre + "/bhDep/organizationDetail", data);
  },
  //年度培训
  personTrain: function (data) {
    return axios.post(pre + "/bhDep/personTrain", data);
  },

};
// 日常管理--工程巡查
export const ProjectPatrolApi = {
  // 工程巡查列表接口
  list: function (data) {
    return axios.post("/project-manage/projectRel/pagePatrol", data);
  },
  // 工程巡查列表删除接口
  // del: function (data) {
  //   return axios.get(
  //     "/project-manage/engineeringSupervision/threeCharge",
  //     data
  //   );
  // },
};
// 日常管理--汛前检查
export const floodInspectionApi = {
  // 汛前检查列表接口
  list: function (data) {
    return axios.post("project-manage/projectRel/pageBeforeFlood", data);
  },
  // 汛前检查列表新增
  // add: function (data) {
  //   return axios.get(
  //     "/project-manage/engineeringSupervision/threeCharge",
  //     data
  //   );
  // },
  // 汛前检查列表修改
  // edit: function (data) {
  //   return axios.get("/project-manage/overview/operation", data);
  // },
  // 汛前检查列表删除
  // del: function (data) {
  //   return axios.get("/project-manage/overview/planView", data);
  // },
};
// 日常管理--年度检查
export const annualInspectionApi = {
  // 年度检查列表接口
  list: function (data) {
    return axios.post("project-manage/projectRel/pageYearReport", data);
  },
  // 年度检查列表新增
  // add: function (data) {
  //   return axios.get(
  //     "/project-manage/engineeringSupervision/threeCharge",
  //     data
  //   );
  // },
  // 年度检查列表修改
  // edit: function (data) {
  //   return axios.get("/project-manage/overview/operation", data);
  // },
  // 删除
  // del: function (data) {
  //   return axios.get("/project-manage/overview/planView", data);
  // },
};
// 日常管理--白蚁检查
export const termiteInspectionApi = {
  // 白蚁检查列表接口
  list: function (data) {
    return axios.post("project-manage/projectRel/pageTermiteCheck", data);
  },
  // 白蚁检查列表新增
  // add: function (data) {
  //   return axios.get(
  //     "/project-manage/engineeringSupervision/threeCharge",
  //     data
  //   );
  // },
  // 白蚁检查列表修改
  // edit: function (data) {
  //   return axios.get("/project-manage/overview/operation", data);
  // },
  // 白蚁检查列表删除
  // del: function (data) {
  //   return axios.get("/project-manage/overview/planView", data);
  // },
};


//水库、水闸、堤防工程
export function getProjectsApi(data) {
  return axios.post(pre + "/projectManage/projects", data);
}