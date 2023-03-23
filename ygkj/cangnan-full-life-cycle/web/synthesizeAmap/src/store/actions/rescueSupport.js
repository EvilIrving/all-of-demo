
import { waterRainApi } from "@/api/dataScreen/rescueSupport/index";
import { rainApi } from "@/api/dataScreen/floodJudge/index";

// 雨情
export async function getRainList(store, { params, callback }) {
  let res = await waterRainApi.rain(params);
  if (res.code == 0) {
    store.commit('BIGSCREEN_RAIN_LIST', res.data)
    callback && callback(res.data)
  }
}

// 水情
export async function getWaterList(store, { params, callback }) {
  let res = await waterRainApi.water(params);
  if (res.code == 0) {
    store.commit('BIGSCREEN_WATER_LIST', res.data)
    callback && callback(res.data)
  }
}

// 获取实时雨量
export async function getRealRainList(store, { params, callback }) {
  let res = await rainApi.realRain(params);
  if (res.code == 0) {
    if(params.minutes == 60){
      store.commit('SET_ONE_RAIN', res.data);
    }else if(params.minutes == 180){
      store.commit('SET_THREE_RAIN', res.data);
    }else{
      store.commit('SET_SIX_RAIN', res.data);
    }
    callback && callback(res.data);
  }
}

// 获取预报雨量
export async function getForecastRainList(store, { params, callback }) {
  let res = await rainApi.realRain(params);
  if (res.code == 0) {
    if(params.hour == 1){
      store.commit('SET_FORECAST_ONE_RAIN', res.data);
    }else if(params.minutes == 3){
      store.commit('SET_FORECAST_THREE_RAIN', res.data);
    }else{
      store.commit('SET_FORECAST_SIX_RAIN', res.data);
    }
    callback && callback(res.data);
  }
}