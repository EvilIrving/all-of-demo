import { http } from '@/config/httpRequset'; // {http}封装好的请求 {axios}拿到的是走拦截器的axios可以按需请求改造
const pre = 'big-screen';

export  const forecastedApi = {
  //预测表
  forecastChart:function(data){
    return http.get('big-screen/floodControlDispatching/forecast',data)
  }
}

export function sectionInformationHydrograph(data) {
  return http.post('big-screen/sectionInformation/hydrograph',data)
}