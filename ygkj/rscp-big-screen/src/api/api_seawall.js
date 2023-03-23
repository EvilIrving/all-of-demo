
import axios from "../config/httpRequset";


// 海塘统计
export function seawallLeftStatisticApi(data) {
  data.superviseLevel = data.superviseLevel ? data.superviseLevel : ''
  return axios.post('/mgt/bm/seawallei/getStatisticBigScreen', data)
}

// file文件下载
export function fileDownload() {
  return axios.get('/mgt/bm/ataf/listJson?id=52CE082EE9E54F3092C4CF4FFF996B01')
}


// 海塘照片列表
export function seawallPhotoListApi(data) {
  return axios.post('/mgt/bm/projectappearance/listJson', data)
}

//查询附件
export function getFile(data) {
  return axios.post("/mgt/lookup/fileListJson", data);
}

// 岸段列表
export function adListApi(data) {
  return axios.post('/mgt/coastalTideAlert/page?fetchAll=true', data)
}

// 获取视频列表
export function videoListApi(data) {
  return axios.post('/bm/camera/listBind?fetchAll=true', data)
}

// 获取视频token
export function videoSelectApi(data) {
  return axios.post('/bm/camera/select', data)
}

// 获取视频链接
export const getVideoUrlApi = {
  url1: function (data) {
    return axios.get2("https://211.lives9.top:30443/api/v1/stream/start", data)
  },
  url2: function (data) {
    return axios.get2("https://170.lives9.top:30443/api/v1/stream/start", data)
  },
  url3: function (data) {
    return axios.get2("https://70.lives9.top:30443/api/v1/stream/start", data)
  }
}

// 潮位站分布列表
export function cwzListApi(data) {
  return axios.post('/mgt/coastalTideAlert/listCoastal?fetchAll=true', data)
}

// 获取方案
export function getPlanApi(data) {
  return axios.post('/mgt/cta/tideplan/listJson', data)
}

// 智慧海塘

export const zhSeawall = {

  // 海塘地图悬浮窗市级统计
  adcdLocation: function (data) {
    return axios.post("/mgt/bm/watertube/adcdLocation", data)
  },
  // 海塘 三色码 统计
  seawallThreeCodeSta: function (data) {
    return axios.post("/mgt/bm/watertube/seawallThreeCodeSta", data)
  },
  // 三色码 柱状图
  seawallthreeYardBarChart: function (data) {
    return axios.post("/mgt/bm/watertube/seawallthreeYardBarChart", data)
  },
  // 一线海塘
  seawallAline: function (data) {
    return axios.post("/mgt/bm/watertube/seawallAline", data)
  },
  // 安全鉴定
  safeStaNum: function (data) {
    return axios.post("mgt/bm/watertube/safeStaNum", data)
  },
  // 问题处置
  problemDealSta: function (data) {
    return axios.post("/mgt/bm/watertube/problemDealSta", data)
  },
  // 获取点位数据
  resProPoint: function (data) {
    return axios.post("/mgt/bm/watertube/resProPoint", data)
  },
  // 获取统计
  resProScaleCount: function (data) {
    return axios.post("/mgt/bm/watertube/resProScaleCount", data)
  },
  // 获取点位数据
  resProstatistical: function (data) {
    return axios.post("/mgt/bm/watertube/resProstatistical", data)
  },
  // 工程打点
  seawallProList: function (data) {
    return axios.post("/mgt/bm/watertube/seawallProList", data)
  },
  // 海塘打点
  seawallProIndo: function (data) {
    return axios.post("/mgt/bm/watertube/seawallProIndo", data)
  },
  // 问题处置详情
  dealProblemInfo: function (data) {
    return axios.post("/mgt/bm/watertube/dealProblemInfo", data)
  },
  // 除险加固统计
  reinforcementSta: function (data) {
    return axios.post("/mgt/bm/watertube/reinforcementSta", data)
  },
  // 口门封堵
  seawallDoor: function (data) {
    return axios.post("/mgt/bm/watertube/seawallDoor", data)
  },
  // 沿塘闸站
  seawallAssPrcd: function (data) {
    return axios.post("/mgt/bm/watertube/seawallAssPrcd", data)
  },
  // 海塘总数
  seawallTotal: function (data) {
    return axios.post("/mgt/bm/watertube/seawallTotal", data)
  },
  // 闭环管控 统计数据
  seawallDoorSta: function (data) {
    return axios.post("/mgt/bm/watertube/seawallDoorSta", data)
  },

}

// 海塘落点
export function seawallProIndo(data) {
  return axios.post('/mgt/bm/watertube/seawallProIndo', data)
}

// 沿塘设置打点
export function ytFacility(data) {
  return axios.post('/mgt/bm/watertube/ytFacility', data)
}
// 潮位站落点
export function listCoastal(data) {
  return axios.post('/mgt/coastalTideAlert/listCoastal?fetchAll=true', data)
}
// 海塘落点
export function dealProblemInfo(data) {
  return axios.post('/mgt/bm/watertube/dealProblemInfo', data)
}

// 获取实况数据
export function getRealTimeData(data) {
  return axios.post('/mgt/bm/watertube/realTimeTide', data)
}
// 安澜工程
export function safeReinforNum(data) {
  return axios.post('/mgt/bm/watertube/safeReinforNum', data)
}
// 智慧海塘附属建筑物 落点
export function zhhtFacility(data) {
  return axios.post('/mgt/bm/watertube/zhhtFacility', data)
}

// 智慧海塘附属建筑物水闸,大中型闸站,闸站,泵站
export function zhhtSeawallAssPrcd(data) {
  return axios.post('/mgt/bm/watertube/zhhtSeawallAssPrcd', data)
}

// 海塘行政区划接口
export function adcdTree(data) {
  return axios.post('/mgt/bm/reservoirWT/adcdTree', data)
}

// 口门管控 落点
export function seawallDoorList(data) {
  return axios.post('/mgt/bm/watertube/seawallDoorList', data)
}
