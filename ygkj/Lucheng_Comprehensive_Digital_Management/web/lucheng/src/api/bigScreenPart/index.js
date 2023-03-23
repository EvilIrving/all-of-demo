import { http } from '@/config/httpRequset'; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = '/big-screen';

// 报警
export const alarmPeople = {
  // 海塘预警
  seawallWarning: function (data) {
    return http.get(pre + '/giveAnAlarm/seawallWarning', data);
  },
  // 海塘报警
  seawallGiveAnAlarm: function (data) {
    return http.get(pre + '/giveAnAlarm/seawallGiveAnAlarm', data);
  },
  // 水位报警
  waterGiveAnAlarm: function () {
    return http.get(pre + '/giveAnAlarm/waterGiveAnAlarm');
  },
  // 降雨报警
  rainGiveAnAlarm: function () {
    return http.get(pre + '/giveAnAlarm/rainGiveAnAlarm');
  },
  // 台风报警
  typhoonGiveAnAlarm: function () {
    return http.get(pre + '/giveAnAlarm/typhoonGiveAnAlarm');
  },
  // 运行报警
  runGiveAnAlarm: function () {
    return http.get(pre + '/giveAnAlarm/runGiveAnAlarm');
  },
  // 统计报警所有的数值
  giveAnAlarmCount: function () {
    return http.get(pre + '/giveAnAlarm/giveAnAlarmCount');
  },
  //台风影响
  typhoonInfluence: function (data) {
    return http.get(pre + '/giveAnAlarm/typhoonInfluence', data);
  },
  //台风影响海棠列表
  seawallTyphoon: function (data) {
    return http.get(pre + '/giveAnAlarm/seawallTyphoon', data);
  },
  //海棠周边列表
  seawallAmbitus: function (data) {
    return http.get(pre + '/giveAnAlarm/seawallAmbitus', data);
  },
  //位移，沉降列表
  displacement: function (data) {
    return http.get(pre + '/giveAnAlarm/displacement', data);
  },

  //根据海塘编码查询，保护范围线,管保区域
  getRangebyCode: function (data) {
    return http.get('/project-manage/seawallRange/getRangebyCode', data);
  },
};
// 工情
export const WorkingConditionsApi = {
  // 水库、泵站、水闸工情统计
  workingConditionsTotal: function () {
    return http.get(pre + '/workingConditions/workingConditionsTotal');
  },
  // 水库、泵站、水闸工情列表
  workingConditionsList: function (data) {
    return http.post(pre + '/workingConditions/workingConditionsList', data);
  },
  overAlert: function (data) {
    return http.get(pre + '/sectionInformation/overAlert', data);
  },

};
// 水情
export const waterAndRain = {
  // 曲线
  waterLevels: function (data) {
    return http.post('/graph/waterAndRain/waterLevels', data);
  },
  // 水情列表
  waterStationsList: function (data) {
    return http.post('/graph/waterAndRain/waterStations', data);
  }
};

// 水闸分布图
export const SluiceDistributionMap = {
  // 列表
  latestData: function () {
    return http.get(pre + '/wagaScattergram/latestData');
  },
  // 闸口孔数
  latestGatage: function () {
    return http.get(pre + '/wagaScattergram/latestGatage',);
  }
};

export function newGateOvs (data) {
  return http.get(pre + '/wagaScattergram/newGateOvs', data);
}

export function sectionList (data) {
  return http.post(pre + '/sectionInformation/list', data);
}

export function floodControl (data) {
  return http.post(pre + '/floodControlDispatching/mapPoints', data);
}

export function hourlyRainfall (data) {
  return http.post(pre + '/floodControlDispatching/hourlyRainfall', data);
}

export const HeavyRainsArchive = {
  // 刪除数据
  del: function (data) {
    return http.get(pre + '/heavyRainsArchive/del', data);
  },
  // 详情-查询时间范围内小时雨量值
  hourlyRainfallvalue: function (data) {
    return http.get(pre + '/heavyRainsArchive/hourlyRainfallvalue', data);
  },
  // 暴雨归档列表
  list: function (data) {
    return http.post(pre + '/heavyRainsArchive/list', data);
  },
  // 计算时间范围内所有雨量站降雨量总值的平均值
  precipitation: function (data) {
    return http.get(pre + '/heavyRainsArchive/precipitation', data);
  },
  // 保存数据
  save: function (data) {
    return http.post(pre + '/heavyRainsArchive/saveOrUpdate', data);
  },
}

export const getHikvisionTree = {
  boatList: function (data) {
    return http.post("/project-manage/projectManage/getHikvisionTree", data);
  },
};

export function dmInfo (data) {
  return http.post("/big-screen/floodControlDispatching/dmInfo", data)
}

export function dmWaterLevel (data) {
  return http.post("big-screen/floodControlDispatching/dmWaterLevel", data)
}