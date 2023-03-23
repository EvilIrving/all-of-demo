import { http } from '@/config/httpRequset'; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = '/water-saving';

/**
 * 取水监管详表
 * @param {*} data
 */
export function getWaterSavingSuperviseList(data) {
  return http.get(pre + '/waterSavingActionSupervise/wsaSuperviseList', data);
}

/**
 * 取水监管详表统计
 * @param {*} data
 */
export function getWaterSavingSuperviseStat(data) {
  return http.get(pre + '/waterSavingActionSupervise/stats4WsaSupervise', data);
}
