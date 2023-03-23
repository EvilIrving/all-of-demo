import { http } from '@/config/httpRequset'; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = '/wra';

/**
 * 水资源公报详表
 * @param {{adcd?:string;year?:string}} data
 */
export function getWrbWatResList(data) {
  return http.get(pre + '/wrbWaterResource/wrbWatResList', data);
}
/**
 * 水资源统计
 * @param {{adcd:string;year:string}} data
 */
export function getStats4WrbWatResList(data) {
  return http.get(pre + '/wrbWaterResource/stats4WrbWatResList', data);
}

/**
 * 用水量详表
 * @param {{adcd?:string;year?:string}} data
 */
export function getWrbWatConList(data) {
  return http.get(pre + '/wrbWaterConsume/wrbWatConList', data);
}

/**
 * 用水量统计
 * @param {{adcd?:string;year?:string}} data
 */
export function getStats4WrbWatConList(data) {
  return http.get(pre + '/wrbWaterConsume/stats4WrbWatConList', data);
}

/**
 * 水库蓄水量详表
 * @param {{startYear:string;endYear:string;resCode:string}} data
 */
export function getWrbStoreRsvrList(data) {
  return http.get(pre + '/wrbStoreRsvr/wrbStoreRsvrList', data);
}

/**
 * 水库蓄水量表头统计
 * @param {{startYear:string;endYear:string;resCode:string}}
 */
export function getStats4WrbStoreRsvrList(data) {
  return http.get(pre + '/wrbStoreRsvr/stats4WrbStoreRsvrList', data);
}

/**
 * 用水效率详表
 * @param {{}} data
 */
export function getWatConEffectList(data) {
  return http.get(pre + '/watConEffect/watConEffectList', data);
}

/**
 * 用水效率统计
 * @param {{}} data
 */
export function getStats4WatConEffectList(data) {
  return http.get(pre + '/watConEffect/stats4WatConEffectList', data);
}

/**
 * 用水效率卡片
 * @param {{targetType:1|2;type:1|2;year:string}} data
 */
export function getUnitCard4WatConEffect(data) {
  return http.get(pre + '/watConEffect/unitCard4WatConEffect', data);
}

/**
 * 用水C位
 * @param {{year:string}} data
 */
export function getUnitCWindows4WatConEffect(data) {
  return http.get(pre + '/watConEffect/unitCWindows4WatConEffect', data);
}

/**
 * 节水行动态势详表
 * @param {*} data
 */
export function getWaterSavingActionList(data) {
  return http.get(pre + '/waterSavingActionNorm/wsaNormList', data);
}
/**
 * 节水行动态势详表统计项
 * @param {*} data
 */
export function getWaterSavingActionNormStats(data) {
  return http.get(pre + '/waterSavingActionNorm/stats4WsaNorm', data);
}
