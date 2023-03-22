/**
 * @description 为使用Vant组件库的移动端封装的axios模块，使用了Vant的交互
 */

import axios, { AxiosResponse, AxiosRequestConfig } from 'axios';
import qs from 'qs';
import { storage } from './storage';
import { config } from '@/config';
import { Toast, Dialog } from 'vant';

interface UserInfo {
  username: string;
  timestamp: number;
  id: string;
  phone?: string;
  accessToken?: string;
  refreshToken?: string;
}

const option: AxiosRequestConfig = {
  baseURL: config.baseApi,
  timeout: 15000
};
const request = axios.create(option);
// 请求注入器
request.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const userInfo: UserInfo = storage.get('userInfo_obj') as UserInfo;
    // 断言无法解决 storage.get('userInfo_obj') 可能为undefined的情况
    const accessToken = userInfo ? userInfo.accessToken : '';
    if (config.headers['Content-Type'] != 'application/json') {
      config.data = qs.stringify(config.data, {
        arrayFormat: 'indices',
        allowDots: true
      });
      config.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
    }

    config.headers['authorization'] = accessToken;
    return config;
  },
  (err: Promise<any>) => Promise.reject(err)
);
// 响应拦截器
request.interceptors.response.use(
  async (response: AxiosResponse) => {
    const res: request.BaseResponse<any> = response.data;
    if (res.code !== 0) {
      // TO DO 请求错误判断
      // 若后台返回错误值，此处返回对应错误对象，下面 error 就会接收
      Toast.fail(res.message || '');
      return Promise.reject(new Error(res.message || 'Error'));
    }
    // 注意返回值
    else return response.data;
  },
  (error: any) => {
    if (error && error.response) {
      switch (error.response.status) {
        case 400:
          error.message = '请求错误(400)';
          break;
        case 401:
          error.message = '未授权,请登录(401)';
          break;
        case 403:
          error.message = '拒绝访问(403)';
          break;
        case 404:
          error.message = `请求地址出错: ${error.response.config.url}`;
          break;
        case 405:
          error.message = '请求方法未允许(405)';
          break;
        case 408:
          error.message = '请求超时(408)';
          break;
        case 500:
          error.message = '服务器内部错误(500)';
          break;
        case 501:
          error.message = '服务未实现(501)';
          break;
        case 502:
          error.message = '网络错误(502)';
          break;
        case 503:
          error.message = '服务不可用(503)';
          break;
        case 504:
          error.message = '网络超时(504)';
          break;
        case 505:
          error.message = 'HTTP版本不受支持(505)';
          break;
        default:
          error.message = `连接错误: ${error.message}`;
      }
    } else {
      if (error.message == 'Network Error') error.message == '网络异常，请检查后重试！';
      error.message = '连接到服务器失败，请联系管理员';
    }
    Toast.fail(error.message);
    Promise.reject(error);
  }
);

export default request;
