/*
 * @Author: hanyu
 * @LastEditTime: 2022-07-19 10:19:31
 * @Description: 水库API管理
 * @FilePath: \rscp-big-screen\src\api\api_reservoir.js
 */
import axios from "../config/httpRequset";
import { localData } from "../../src/plugins/storage";
// 水库统计
export function reservoirLeftStatisticApi(data) {
  // data.adcd = localData("get", "userInfo").adcd;
  data.superviseLevel = data.superviseLevel ? data.superviseLevel : "";
  return axios.get("/mgt/bm/reservoirei/getStatisticBigScreen", data);
}
// 安全检查
export function statisticCheckBigScreenApi(data) {
  // data.adcd = localData("get", "userInfo").adcd;
  return axios.get("/mgt/bm/project/statisticCheckBigScreen", data);
}
// 安全鉴定统计  
export function statisticSeawallSafeBigScreen(data) {
  // data.adcd = localData("get", "userInfo").adcd;
  return axios.get("/mgt/bm/seawallei/statisticSeawallSafeBigScreen", data);
}

// 水库照片列表
export function reservoirPhotoListApi(data) {
  return axios.post("/mgt/bm/projectappearance/listJson", data);
}

//查询附件
export function getFile(data) {
  return axios.post("/mgt/lookup/fileListJson", data);
}

// 水库视频影像列表
export function reservoirVideoListApi(data) {
  return axios.post("/mgt/bm/projectvideo/listJson", data);
}

// 水库摄像头列表
export function reservoirCameraListApi(data) {
  return axios.post("/bm/camera/listBind", data);
}

//获取台风信息
export function getTyInfoApi(data) {
  return axios.get("/mgt/st/typhoon/listJson?fetchAll=true&tfid=" + data);
}

//获取台风信息
export function getTyListApi(data) {
  return axios.get("/mgt/st/typhoon/lastest", data);
}

//获取人员列表
export function reservoirPersonListApi(data) {
  return axios.post("/mgt/temp/threepeople/listJson", data);
}

//获取各工程流域统计
export function projectBasinDataApi(data) {
  return axios.post("/mgt/bm/project/statisticBasinBigScreen", data);
}

// 水库智管大屏--------
//获取水库总览统计  //获取 生態 灌溉 發電 供水 統計
export function reservoirBigScreen(data) {
  return axios.get("mgt/bm/reservoirWT/reservoirBigScreen", data);
}

//获取一键生成列表
export function generateProject(data) {
  return axios.post("mgt/bm/reservoirWT/generateProject", data);
}

//获取限制运用弹窗曲线
export function dailogDetail(data) {
  return axios.post("mgt/bm/reservoirWT/dailogDetail", data);
}

//获取限制运用左侧统计
export function xzyyStatisticApi(data) {
  return axios.post("/mgt/bm/reservoirWT/reservoirBigScreen", data);
}

//获取右侧纳蓄统计
export function nxStatisticApi(data) {
  return axios.post("/mgt/bm/reservoirWT/xslCount", data);
}

//获取右侧灌溉面积统计
export function ggAreaStatisticApi(data) {
  return axios.post("/mgt/bm/reservoirWT/irrigationArea", data);
}

//获取右侧可供水量统计
export function availableWaterSupply(data) {
  return axios.post("/mgt/bm/reservoirWT/availableWaterSupply", data);
}

//获取左侧 研判统计
export function threeMalCount(data) {
  return axios.post("/mgt/bm/reservoirWT/threeMalCount", data);
}

//获取左侧 研判柱状图
export function threeYardBarChart(data) {
  return axios.post("/mgt/bm/reservoirWT/threeYardBarChart", data);
}

//获取责任人水库列表
export function searchEntity(data) {
  return axios.post("/mgt/bm/toDoTasks/searchEntity", data);
}

//获取责任人水库统计
export function searchCount(data) {
  return axios.post("/mgt/bm/toDoTasks/searchCount", data);
}
//获取责任人任务数量
export function selectGzt(data) {
  return axios.post("/mgt/bm/toDoTasks/selectGzt", data);
}
//获取责任人不同状态任务表格
export function statusTypeCount(data) {
  return axios.post("/mgt/bm/toDoTasks/statusTypeCount", data);
}

//控运计划水位表格
export function spcpListJson(data) {
  return axios.get("/mgt/cu/spcp/listJson", data);
}

//控运计划水位表格
export function cxjgTable(data) {
  return axios.post("/mgt/bm/toDoTasks/cxjgTable", data);
}


//控运计划tab页 水位
export function kyjhTable(data) {
  return axios.post("/mgt/bm/toDoTasks/kyjhTable", data);
}

//水库库容tab页 水位
export function sknxTable(data) {
  return axios.post("/mgt/bm/toDoTasks/sknxTable", data);
}

//责任清单
export const responsibilityListApi = {
  dutyParent(data) {
    return axios.post("/mgt/bm/toDoTasks/dutyParent", data);
  },
  dutyTree(data) {
    return axios.post("/mgt/bm/toDoTasks/dutyTree", data);
  },
  dutyDetail(data) {
    return axios.post("/mgt/bm/toDoTasks/dutyResumption", data);
  },
}

//灌溉 
export const wateringApi = {
  rightNum(data) {
    return axios.post("/mgt/bm/supplystatistics/countIrrigation", data);
  },
}

//发电 
export const electricityApi = {
  rightNum(data) {
    return axios.post("/mgt/bm/capacitystatistics/countNum", data);
  },
}

//生态 
export const ecologyApi = {
  rightNum(data) {
    return axios.post("/mgt/bm/water/countNum", data);
  },
}

//防洪列表 及 弹窗
export function floodListJson(data) {
  return axios.post("/mgt/temp/dispatchrecord/listJson", data);
}

//溢洪列表
export function floodListYihong(data) {
  return axios.post("/mgt/bm/ctroChcuDif/getFloodList", data);
}


//防洪统计
export function floodCountStorage(data) {
  return axios.post("/mgt/bm/ctroChcuDif/countStorage", data);
}
//安全鉴定
export function safeAppraisalApi(data) {
  return axios.post("/mgt/bm/reservoirWT/safeAppraisal", data);
}

//任务 
export const taskApi = {
  list(data) {
    return axios.post("/mgt/bm/toDoTasks/listTask", data);
  },
  deal(data) {
    return axios.post("/mgt/bm/toDoTasks/dispose", data);
  },
}

//除险加固
export function cyjgNumApi(data) {
  return axios.post("/mgt/bm/reservoirWT/reinforcement", data);
}

//问题处置
export function problemApi(data) {
  return axios.post("/mgt/bm/reservoirWT/dealProblem", data);
}

//问题处置-四平台
export function problemFourApi(data) {
  return axios.post("/mgt/bm/reservoirWT/smFloodcheckOther", data);
}

//个人中心 -> 风险隐患
export function riskHiddenDanger(data) {
  return axios.post("/mgt/bm/reservoirWT/riskHiddenDanger", data);
}

//发送浙政钉消息
export function oaMsgApi(data) {
  return axios.post("/mgt/bm/toDoTasks/sendZzd", data);
}

// 风景区打点
export function scenicSpotApi(data) {
  return axios.post("/mgt/bm/water/countScenic", data);
}

// 防洪座次打点
export function floodTimesApi(data) {
  return axios.post("/mgt/bm/ctroChcuDif/search", data);
}

// 拦洪座次打点
export function floodExportListApi(data) {
  return axios.post("/mgt/bm/reservoirWT/floodRetentionSearch", data);
}

// 简报数据
export function bulletinApi(data) {
  return axios.post("/mgt/bm/reservoirWT/bulletin", data);
}
// 纳蓄点位列表
export function statislistJson(data) {
  return axios.post("/mgt/temp/resstoragecapacity/statislistJson", data);
}

// 生成智慧水库首页点击日志
export function clickLog(data) {
  return axios.get("/mgt/sys/log/ssg", data);
}

