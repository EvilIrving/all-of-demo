/*
 * @Author: dengzhao
 * @LastEditTime: 2021-11-19 15:39:56
 * @Description:
 * @FilePath: \lucheng\src\api\seawall.js
 */
import { http } from '@/config/httpRequset.js';
//获取海塘二维码
export function getSeawallQr(data) {
  return http.post('/big-screen/giveAnAlarm/qrCode', data);
}
export function listDict(data) {
  return http.post('/project-manage/projectRel/listDict', data);
}
//查询码分
export function getCode(data) {
  return http.get(`/big-screen/healthCode/listColor/${data}`);
}
//保存码分
export function setCode(data) {
  return http.postJson(`/big-screen/healthCode/saveColor`, data);
}
//查询权重
export function listWeight(data) {
  return http.get(`/big-screen/healthCode/listWeight/${data}`);
}

//保存权重
export function saveWeight(data) {
  return http.postJson('/big-screen/healthCode/saveWeight', data);
}
//海塘工程信息
export function getSeawallProjects(data) {
  return http.post('/project-manage/projectManage/projects', data);
}
//值班人员
export function getSeawallSelectDuty(data) {
  return http.post('/project-manage/projectRel/selectDuty/' + data);
}
//分页查询工程巡查列表
export function getSeawallPagePatrol(data) {
  return http.post('/project-manage/projectRel/pagePatrol', data);
}
//分页查询工程重大历史险情
export function getSeawallPageGreatDanger(data) {
  return http.post('/project-manage/projectRel/pageGreatDanger', data);
}
//分页查询工程隐患列表
export function getSeawallPageDanger(data) {
  return http.post('/project-manage/projectRel/pageDanger', data);
}
//档案树
export function getSeawallAchievementStructure(data) {
  return http.post('/authentication-system/ums/sysAchievement/achievementStructure', data);
}
//档案树列表
export function getSeawallPageList(data) {
  return http.post('/authentication-system/ums/sysFile/pageList', data);
}
//工程关联测站列表
export function getSeawallListSt(data) {
  return http.post('/project-manage/projectRel/listSt', data);
}
//测站对应数据
export function getSeawallWaterLevels(data) {
  return http.post('/graph/waterAndRain/waterLevels', data);
}
//测站对应小时降雨量
export function rainfall24(data) {
  return http.get(`graph/waterAndRain/rainfall24/${data}`);
}
//管理信息
export function getSeawallSelectManageInfo(data) {
  return http.get('/project-manage/projectRel/selectManageInfo', data);
}
//管理信息
export function getSeawallselectRuntimeWarn(data) {
  return http.get('/project-manage/projectRel/selectRuntimeWarn', data);
}
//管理信息
export function getSeawallSelectWarn(data, code) {
  return http.get(`big-screen/giveAnAlarm/selectWarn/${code}`, data);
}
// 沉降位移信息
export function getAqjcWarn(data) {
  return http.get(`big-screen//giveAnAlarm/aqjcWarn`, data);
}
//风暴潮预报列表
export function pageTideFcst(data) {
  return http.get('digitization/tideFcst/pageTideFcst/', data);
}

//根据预报id查询风暴潮列表
export function listTileFcst(data) {
  return http.get('digitization/tideFcst/listTileFcst/' + data);
}

//根据海塘id查询交叉建筑物
export function bmAccBuildList(data) {
  return http.post('/project-manage/bmAccBuild/bmAccBuildList', data);
}
//根据海塘id查询附属建筑物
export function selectAncillaryBuilding(data) {
  return http.post('/project-manage/ancillarybuilding/selectAncillaryBuilding ', data);
}
//根据海塘id查询桩号
export function loadPage(data) {
  return http.post('/project-manage/bmseawallstructinfo/loadPage', data);
}

//根据海塘查询台风
export function seawallTyphoonDif(data) {
  return http.post('/big-screen/giveAnAlarm/seawallTyphoonDif', data);
}

//督办查询
export function superviseList(data) {
  return http.post('/project-manage/oversee/page', data);
}
//督办新增
export function saveOrUpdate(data) {
  return http.postJson('/project-manage/oversee/save', data);
}
//督办新增
export function specialist(data) {
  return http.post('/project-manage/waterexpert/listWaterExpert', data);
}
//知识库
export function knowledgeList(data) {
  return http.post('/project-manage/lbKnowledge/loadPage', data);
}

//用户管理
export function usersList(data) {
  return http.get('/ums/sys/users', data);
}

//用户管理
export function getAqjcDatas(data) {
  return http.get('/big-screen/giveAnAlarm/getAqjcData', data);
}
