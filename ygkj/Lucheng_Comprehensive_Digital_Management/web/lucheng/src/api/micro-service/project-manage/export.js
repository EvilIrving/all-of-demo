//@ts-check
import axios from 'axios';
import config from '@/config/config';
import { localData } from '@/plugins/storage';

/**
 * 用到的微服务列表
 */
const microService = {
  projectManage: '/project-manage',
  meteorological: '/meteorological-visualization',
  graph: '/graph',
  bigScreen: 'big-screen',
  floodProtect: '/flood-protect',
  hydrologyDigitization: '/hydrology-digitization',
  wra: '/wra',
  planDigitization: '/plan-digitization',
  waterSaving: '/water-saving'
};

/** @description 封装用于下载文件的axios */
const request = axios.create({ timeout: 100000, baseURL: config.baseUrl });

request.interceptors.request.use(
  (config) => {
    /** @type {{accessToken:string}} */
    const userInfo = localData('get', 'userInfo');
    const { accessToken } = userInfo ? userInfo : { accessToken: '' };
    config.headers['authorization'] = accessToken;
    config.responseType = 'blob';
    return config;
  },
  (err) => {
    return Promise.reject(err);
  }
);
/**
 * 流域类型虚拟注释
 * @typedef {'飞云江'|'鳌江'|'瓯江'} Basin
 */

/**
 * 以下载方式保存Blob格式
 * @param {Blob} blob Blob格式的数据
 * @param {String} filename 文件名
 */
export function saveFile(blob, filename) {
  // @ts-ignore
  if (window.navigator.msSaveOrOpenBlob) {
    // @ts-ignore
    navigator.msSaveBlob(blob, filename);
  } else {
    const link = document.createElement('a');
    const body = document.querySelector('body');
    if (!body) return;
    link.href = window.URL.createObjectURL(blob);
    link.download = filename; //html5
    // fix Firefox
    link.style.display = 'none';
    body.appendChild(link);
    link.click();
    body.removeChild(link);
    window.URL.revokeObjectURL(link.href);
  }
}

/**
 * 通用导出Promise   Post
 * @param {string} url 请求微服务后的路径
 * @returns {(data:any)=>Promise<import('axios').AxiosResponse>}
 */
function exportPromise(url) {
  const FILENAME_REGEX = /filename=(.+)/;
  return (data) => {
    return new Promise((resolve, reject) => {
      request
        .post(url, data)
        .then((res) => {
          console.log(res, 'dassad')
          if (res.status == 200) {
            const blob = new Blob([res.data]);
            const filename = decodeURIComponent(
              res.headers['content-disposition']?.match(FILENAME_REGEX)?.[1] || '下载文件'
            );
            saveFile(blob, filename);
            resolve(res);
          }
        })
        .catch((err) => reject(err));
    });
  };
}

/**
 * 通用导出Promise  Get
 * @param {string} url 请求微服务后的路径
 * @returns {(data:any)=>Promise<import('axios').AxiosResponse>}
 */
 function exportGetPromise(url) {
  const FILENAME_REGEX = /filename=(.+)/;
  return (data) => {
    return new Promise((resolve, reject) => {
      request(
          {
            method: 'get',
            url: url,
            params: data,
          }
        )
        .then((res) => {
          console.log(res, 'dassad')
          if (res.status == 200) {
            const blob = new Blob([res.data]);
            const filename = decodeURIComponent(
              res.headers['content-disposition']?.match(FILENAME_REGEX)?.[1] || '下载文件'
            );
            saveFile(blob, filename);
            resolve(res);
          }
        })
        .catch((err) => reject(err));
    });
  };
}
/**
 * 导出灌区
 * @param {object} data
 * @param {string} data.areaCode 行政区划编码
 * @param {Basin} data.basin 流域
 * @param {string} data.levels 工程规模:大（Ⅰ）型：1，大（Ⅱ）型：2，中型：3，小（Ⅰ）型：4，小（Ⅱ）型：5
 */
export function exportIrrProject(data) {
  return exportPromise(microService.projectManage + '/newProjectManage/exportIrrProject')(data);
}

/**
 * 导出闸站
 * @param {object} data
 * @param {string} data.areaCode 行政区划编码
 * @param {string} data.levels 工程规模:大（Ⅰ）型：1，大（Ⅱ）型：2，中型：3，小（Ⅰ）型：4，小（Ⅱ）型：5
 */
export function exportGateProject(data) {
  return exportPromise(microService.projectManage + '/newProjectManage/exportGateProject')(data);
}

/**
 * 规划导出
 * @param {object} data
 * @param {string} data.ptId
 * @param {string} data.areaCode
 * @param {string} data.startYear
 * @param {string} data.endYear
 * @param {string} data.projectName
 */
export function exportPlanProj(data) {
  return exportPromise(microService.projectManage + '/newProjectManage/exportPlanProj')(data);
}

/**
 * 视频监控导出
 * @param {*} data
 */
export function exportPageCamera(data) {
  return exportPromise(microService.projectManage + '/cameraOps/exportPageCamera')(data);
}

/**
 * 短临预报导出
 * @param {*} data
 */
export function exportRainfallPredictGrid(data) {
  return exportPromise(microService.meteorological + '/weatherPredict/exportRainfallPredictGrid')(data);
}

/**
 * 河网导出
 * @param {*} data
 */
export function exportRivernet(data) {
  return exportPromise(microService.graph + '/waterAndRain/exportRivernet')(data);
}

/**
 * 风险隐患导出
 * @param {*} data
 */
export function exportBusRisk(data) {
  return exportPromise(microService.bigScreen + '/busRiskList/exportBusRisk')(data);
}

/**
 * 海塘导出
 * @param {*} data
 */
export function exportSeawall(data) {
  return exportPromise(microService.projectManage + '/seawall/exportSeawall')(data);
}

/**
 * 积水导出
 * @param {*} data
 */
export function exportFloodPoint(data) {
  return exportPromise(microService.floodProtect + '/waterLogging/exportFloodPoint')(data);
}

/**
 * 水闸导出
 * @param {*} data
 */
export function exportWrpWagaUnitTable(data) {
  return exportPromise(microService.projectManage + '/waga/exportWrpWagaUnitTable')(data);
}

/**
 * 水库导出
 * @param {*} data
 */
export function exportRsvr(data) {
  return exportPromise(microService.projectManage + '/rsvr/exportRsvr')(data);
}

/**
 * 洪水风险图
 * @param {*} data
 */
export function exportFloodRiskDisasterData(data) {
  return exportPromise(microService.hydrologyDigitization + '/FloodRiskDisasterData/export')(data);
}

/**
 * 防汛仓库导出
 * @param {*} data
 */
export function exportWareHouses(data) {
  return exportGetPromise(microService.floodProtect + '/warehouse/exportWareHouses')(data);
}

/**
 * 防汛队伍
 * @param {*} data
 */
export function exportTeams(data) {
  return exportPromise(microService.floodProtect + '/team/exportTeams')(data);
}

/**
 * 供水量
 * @param {*} data
 */
export function exportWrabWaterSupply(data) {
  return exportPromise(microService.wra + '/wrbWaterSupply/exportWrabWaterSupply')(data);
}
/**
 * 用水量
 * @param {*} data
 */
export function exportWrbWatConList(data) {
  return exportPromise(microService.wra + '/wrbWaterConsume/exportWrbWatConList')(data);
}
/**
 * 降水量
 * @param {*} data
 */
export function exportWrbRainfallList(data) {
  return exportPromise(microService.wra + '/wrbRainfall/exportWrbRainfallList')(data);
}
/**
 * 水资源量
 * @param {*} data
 */
export function exportWrbWatResList(data) {
  return exportPromise(microService.wra + '/wrbWaterResource/exportWrbWatResList')(data);
}
/**
 * 水库蓄水量
 * @param {*} data
 */
export function exportWrbStoreRsvrList(data) {
  return exportPromise(microService.wra + '/wrbStoreRsvr/exportWrbStoreRsvrList')(data);
}
/**
 * 规划导出
 * @param {*} data
 */
export function exportDdvPlanProj(data) {
  return exportPromise(microService.planDigitization + '/devPlan/exportDevPlanProj')(data);
}

/**
 * 流域生态流量导出
 * @param {*} data
 */
export function exportSectFlowList(data) {
  return exportPromise(microService.hydrologyDigitization + '/sectFlow/exportSectFlowList')(data);
}

/**
 * 水源保障流量导出
 * @param {*} data
 */
export function exportRsvrWatSrc(data) {
  return exportPromise(microService.projectManage + '/rsvr/exportRsvrWatSrc')(data);
}

/**
 * 水研究
 * @param {*} data
 */
export function exportWaterResearchDerivation(data) {
  return exportPromise(microService.projectManage + '/rsvr/exportRsvrWatSrc')(data);
}

/**
 * 用水效率
 * @param {*} data
 */
 export function exportWatConEffectList(data) {
  return exportPromise(microService.wra + '/watConEffect/exportWatConEffectList')(data);
}
/**
 * 水质监控
 * @param {*} data
 */
export function exportWaterQualityList(data) {
	return exportPromise(microService.graph + "/waterQualityV2/exportList")(data);
}

/**
 * 节水指标导出
 * @param {*} data
 */
export function exportWaterSavingAction(data) {
  return exportPromise(microService.wra + '/waterSavingActionNorm/exportWsaNormList')(data);
}

/**
 * 取水监管导出
 * @param {*} data
 */
export function exportWsaSuperviseList(data) {
  return exportPromise(microService.waterSaving + '/waterSavingActionSupervise/exportWsaSuperviseList')(data);
}

/**
 * 导出：海塘弹窗-隐患-工程巡查
 * @param {object} data
 */
export function exportPagePatrol(data) {
  return exportPromise(microService.projectManage + '/projectRel/exportPagePatrol')(data);
}

/**
 * 导出：海塘弹窗-隐患-工程隐患
 * @param {object} data
 */
export function exportPageDanger(data) {
  return exportPromise(microService.projectManage + '/projectRel/exportPageDanger')(data);
}


/**
 * 海塘雨量导出
 * @param {*} data
 */
 export function exportHourRainfall(data) {
  return exportPromise(microService.graph + '/waterAndRain/exportHourRainfall')(data);
}


/**
 * 海塘潮位导出
 * @param {*} data
 */
export function exportWaterLevels(data) {
   console.log(data)
  return exportPromise(microService.graph + '/waterAndRain/exportWaterLevels')(data);
}


/**
 * 海塘沉降位移导出
 * @param {*} data
 */
 export function exportGetAqjcData(data) {
  return exportGetPromise(microService.bigScreen + '/giveAnAlarm/exportGetAqjcData')(data);
}

/**
 * 雨量报表导出
 * @param {*} data
 */
 export function exportRainfallReport(data) {
  return exportGetPromise(microService.bigScreen + '/giveAnAlarm/exportRainfallReport')(data);
}
