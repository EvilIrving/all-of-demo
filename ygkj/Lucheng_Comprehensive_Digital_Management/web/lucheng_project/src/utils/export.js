//@ts-check
import axios from "axios";
import config from "../config/config";
import { sessionData } from "../plugins/storage";
import {staffAttendance} from "@/api/propertyManage";

/** @description 封装用于下载文件的axios */
const request = axios.create({ timeout: 100000, baseURL: config.baseUrl });

request.interceptors.request.use(
  (config) => {
    /** @type {{accessToken:string}} */
    const userInfo = sessionData("get", "userInfo");
    const { accessToken } = userInfo ? userInfo : { accessToken: "" };
    config.headers["authorization"] = accessToken;
    config.responseType = "blob";
    return config;
  },
  (err) => {
    return Promise.reject(err);
  }
);

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
    const link = document.createElement("a");
    const body = document.querySelector("body");
    if (!body) return;
    link.href = window.URL.createObjectURL(blob);
    link.download = filename; //html5
    // fix Firefox
    link.style.display = "none";
    console.log(link, 'l;;;;;;;;;;;;;;;;;')
    body.appendChild(link);
    link.click();
    body.removeChild(link);
    window.URL.revokeObjectURL(link.href);
  }
}

/**
 * 通用导出Promise
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
          if (res.status == 200) {
            const blob = new Blob([res.data]);
            const filename = decodeURIComponent(
              res.headers["content-disposition"]?.match(FILENAME_REGEX)?.[1] ||
              "下载文件"
            );
            console.log(res.headers["content-disposition"]?.match(FILENAME_REGEX), 'filename')
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

//演练导出
export function exportPractice(data) {
  return exportPromise("/project-manage/practiceManagement/exportList")(data);
}
//培训导出
export function exportTraining(data) {
  return exportPromise("/project-manage/trainingManagement/exportList")(data);
}
//风险预警管理规则配置
export function exportRuleList(data) {
  return exportPromise("/project-manage/warningRule/exportList")(data);
}
//人员导出
export function exportPersonList(data) {
  return exportPromise("/project-manage/warningUser/exportWarning")(data);
}
//救援队伍导出
export function exportTeamList(data) {
  return exportPromise("/flood-protect/team/exportTeams")(data);
}
//队伍人员导出
export function exportPeopleList(data) {
  return exportPromise("/flood-protect/team/exportTeamPersons")(data);
}
//队伍设备导出
export function exportEquipList(data) {
  return exportPromise("/flood-protect/team/exportTeamDevice")(data);
}

//队伍设备导出
export function exportDevice(data) {
  return exportPromise("/flood-protect/warehouse/exportDevice")(data);
}

//救援队伍导出
export function exportWareHouses(data) {
  return exportGetPromise('/flood-protect/warehouse/exportWareHouses')(data);
}

//检修导出
export function exportOverhaul(data) {
  return exportGetPromise('/project-manage/runAction/exportOverhaul')(data);
}

//维修资金导出
export function exportMtFund(data) {
  return exportGetPromise('/project-manage/maintenance/exportMtFund')(data);
}

//调度运行导出
export function exportSchedulingOperation(data) {
  return exportGetPromise('project-manage/runAction/exportDisDom')(data);
}
//年度维养导出
export function exportMtDmPlan(data) {
  return exportGetPromise('/project-manage/maintenance/exportMtDmPlan')(data);
}

//合同验收-年度验收-导出
export function exportContractOperate(data) {
  return exportGetPromise("/project-manage/contract/exportAnnualAcceptance")(data);
}

//考核管理 - 督察检查 - 导出
export function exportInspection(data) {
  return exportGetPromise("/project-manage/contract/exportInspection")(data);
}

//日常检查- 导出
export function exportDailyCheck(data) {
  return exportGetPromise("/project-manage/projCheck/exportDailyCheck")(data);
}

//汛前检查- 导出
export function exportLgMpFc(data) {
  return exportGetPromise("/project-manage/projCheck/exportLgMpFc")(data);
}

//汛后检查 - 导出
export function mpAfExport(data) {
  return exportGetPromise("/project-manage/projCheck/mpAfExport")(data);
}

//年度检查- 导出
export function exportBhLgMpAc(data) {
  return exportGetPromise("/project-manage/projCheck/exportBhLgMpAc")(data);
}

//特殊检查 - 导出
export function exportSpecialCheck(data) {
  return exportGetPromise("/project-manage/projCheck/exportSpecialCheck")(data);
}

//专项检查 - 导出
export function mpSpExport(data) {
  return exportGetPromise("/project-manage/projCheck/mpSpExport")(data);
}

//定期考评单条数据的导出
export function exportGetByIdToExcel(data) {
  return exportGetPromise("/project-manage/regularReview/getByIdToExcel")(data);
}

//定期监测
export function regularMonitorExport(data) {
  return exportPromise("/project-manage/projMonitor/regularMonitorExport")(data);
}

//水域变化报表
export function watersReportExport(data) {
  return exportPromise("/project-manage/watersReport/exportList")(data);
}

//违法占用报表
export function watersReportInfoExport(data) {
  return exportPromise("/project-manage/watersReportInfo/exportList")(data);
}

//水域变化报表
export function watersChangeExport(data) {
  return exportPromise("/project-manage/watersChange/exportList")(data);
}

//避灾点
export function avoidancePointExport(data) {
  return exportPromise("/big-screen/avoidancePoint/exportList")(data);
}
//避灾点
export function approvalProjectExport(data) {
  return exportPromise("/project-manage/investProj/export")(data);
}

//值班值守
export function exportDuty(data) {
  return exportPromise("/project-manage/bhDutyRecord/exportDuty")(data);
}
//人员考勤
export function exportStaffAttendance(data){
  return exportPromise('/project-manage/attendance/exportAttendanceCalendar')(data)
}

//操作日志
export function exportlogs(data){
  return exportPromise('/ums/sys/logs/export')(data)
}

//数据维护
export function exportMaintainInfo(data) {
  console.log(data, 'datadatadata')
  return exportGetPromise('/project-manage/dataMaintenance/exportMaintainInfo')(data)
}