/*
 * @Author: hanyu
 * @LastEditTime: 2021-07-15 10:44:40
 * @Description: 请求封装、请求拦截
 * @FilePath: /rscp-big-screen/src/config/httpRequset.js
 */
import axios from 'axios'
import config from './config'
import { Message } from 'element-ui'
import qs from 'qs'
// import router from '../router/index'
import { localData } from '../plugins/storage';


const http = {}

var $http = axios.create({
    timeout: null,
    baseURL: config.baseUrl
})

var $http2 = axios.create({
  timeout: null
})

http.post = (url, params)=>{
    params = params ? params : {}
    return new Promise((resolve, reject) => {
        $http.post(url, params).then(res => {
            if(res.status === 200){
              resolve(res.data);
            }else{
              // checkStatus(res.status)
              resolve(res.data)

            }
        }).catch(error=>{
          // reject(res.data.message)
        })
    })
}

http.get = (url, params)=>{
  params = params || {};
  return new Promise((resolve, reject) => {
      $http.get(url, {params:params}).then(res => {
          if(res.status === 200){
              resolve(res.data);
          }else{
            // checkStatus(res.status)
            resolve(res.data)

          }
      }).catch(error=>{
          
      })
  })
}

http.get2 = (url, params)=>{
  params = params || {};
  return new Promise((resolve, reject) => {
      $http2.get(url, {params:params}).then(res => {
          if(res.status === 200){
              resolve(res.data);
          }else{
            // checkStatus(res.status)
            resolve(res.data)
          }
      }).catch(error=>{
          
      })
  })
}

function checkStatus(status){
  switch (status) {
    case 301:
      Message.error('登录状态失效')
      break;
    case 400:
      Message.error('错误请求')
      break;
    case 401:
      Message.warning('未授权，请重新登录')
      break;
    case 403:
      Message.warning('拒绝访问')
      break;
    case 404:
      Message.error('请求错误,未找到该资源')
      break;
    case 405:
      Message.warning('请求方法未允许')
      break;
    case 408:
      Message.warning('请求超时')
      break;
    case 500:
      Message.warning('服务器端出错')
      break;
    case 501:
      Message.warning('网络未实现')
      break;
    case 502:
      Message.warning('网络错误')
      break;
    case 503:
      Message.warning('服务不可用')
      break;
    case 504:
      Message.warning('网络超时')
      break;
    case 505:
      Message.warning('http版本不支持该请求')
      break;
  }
}

// http request 拦截器
$http.interceptors.request.use(
  config => {
    config.data = qs.stringify(config.data);
    config.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
    let token = localData('get', 'token')
    if(token){
      config.headers.token = token
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  }
);

//响应拦截器即异常处理
$http.interceptors.response.use(response => {
  if (response.data.statusCode == '301') {
    // router.replace({
    //     path: '/login' // 重新回到登陆页
    // })
  }
  return response
}, err => {
  return Promise.resolve(err.response)
})

export default http