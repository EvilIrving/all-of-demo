import {http} from "@/config/httpRequset";
const pre = "/project-manage";
//地图管理

export const mapManageApi = {
  //工程列表
  getProjectList: function(data) {
    return http.post(pre + "/projectManage/projects", data);
  },
  // 视频列表
  getVideoList: function(data) {
    return http.get(pre + "/projectManage/projectCameras/" + data);
  },
  // 工程对应的各个单位负责人信息
  getChargePerson: function(data) {
    return http.get(pre + "/engineeringSupervision/threeCharge", data);
  },
  //河道查询
  getRiverList: function(data) {
    return http.post("/graph/riverProtection/rivers", data);
  },
  //当前水位
  getCurrentWaterLevel: function(data) {
    return http.get(pre + "/projectManage/reservoirNewestWaterLevel/" + data);
  },
  //获取工程对应建设内容信息
  getPjBdIf: function(data) {
    return http.get(pre + "/projectManage/proBuildContents/", data);
  },
  //获取工程对应外链信息
  getOtherLink: function(data) {
    return http.get(pre + "/projectManage/proLinks/" + data);
  },
  //测面工程列表
  getProjectInScope: function (data) {
    return http.postJson(pre + "/projectManage/projectInScope", data)
  }
};
