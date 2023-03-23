import { http } from '@/config/httpRequset'; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = '/big-screen';

export function projectList (data) {
  return http.post(pre + '/floodControlDispatching/projectList', data);
}

export function dmInfo (data) {
  return http.post(pre + '/floodControlDispatching/dmInfo', data)
}

export function getHiddenDangerList () {
  return http.post(pre + '/giveAnAlarm/riskHiddenDanger');
}

export function socialResourcesCountByType (prcd) {
  return http.post(pre + '/socialResources/countByType', { prcd });
}

export function socialResourcesList (type, prcd, keyWord) {
  return http.post(pre + '/socialResources/list', { type, prcd, keyWord })
}

export function forecastAlert () {
  return http.get(pre + '/sectionInformation/forecastAlert')
}

export function chooseSection () {
  return http.post(pre + '/floodControlDispatching/chooseSection')
}

export function forecastSection (data) {
  return http.post(pre + '/floodControlDispatching/forecastSection', data)
}