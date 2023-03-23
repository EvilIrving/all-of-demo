/*
 * @Author: wqn
 * @Date: 2022-11-30 14:00:08
 * @LastEditors: wqn
 * @LastEditTime: 2022-11-30 16:46:25
 */
import axios from "axios"
import qs from 'qs'
import config from "config/config.js"
import { localData } from 'utils/storage';

const instance = axios.create({
  baseURL: config.baseUrl,
  timeout: null,
})

instance.interceptors.request.use((config) => {
    config.headers['token'] = localData('get', 'token') ||  ''
  return config
}, (error) => {
  return Promise.reject(error)
})

instance.interceptors.response.use((response) => {
  const res = response.data
  if (res.success) return res
  return Promise.reject({
    errorType: 'InterfaceProblems',
    message: res.msg || '接口返回存在问题'
  })
}, (error) => {
  return Promise.reject(error)
})

function get(url, params = {}, contentType = 'application/x-www-form-urlencoded;charset=UTF-8') {
  return new Promise((resolve, reject) => {
    instance({
      method: 'get', url, params,
      headers: { 'Content-Type': contentType }
    }).then(res => { resolve(res) }).catch(error => { reject(error) })
  })
}

function post(url, data) {
  return new Promise((resolve, reject) => {
    instance({
      method: 'post', url, data: data ? qs.stringify(data, { allowDots: true }) : '',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
      }
    }).then(res => { resolve(res) }).catch(error => { reject(error) })
  })
}

function postJson(url, data) {
  return new Promise((resolve, reject) => {
    instance({
      method: 'post', url, data: data || {},
      headers: { 'Content-Type': 'application/json' }
    }).then(res => { resolve(res) }).catch(error => { reject(error) })
  })
}

export default { get, post, postJson }