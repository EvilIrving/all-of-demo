---
title: Uni-app请求封装与拦截
date: 2022-06-02 12:59:52
update: 2022-06-02
tags: 
- Uni-app
- Axios
categories: 
- Uni-app
---

``` javascript
let localHost =  'http://192.168.*.***:****'
const config = {
  baseUrl: process.env.NODE_ENV === 'production' ?  localHost : localHost 
}
/**
 * 分以下三种情况对响应的消息进行处理；
 *
 * @param res
 * @param resolve
 * @param reject
 */
function requestHandle(res, resolve, reject) {
  if (typeof res.statusCode === 'number') {
    // 请求状态码是数字，表示：能和服务器建立连接；
    if (res.statusCode === 200) {
      // [1]:返回200： 服务器正常,服务器返回的消息放在res.data里；
      switch (res.data.status) {
        // 以下状态码->正常通过；
        case 200:
        case '200':
          resolve(res.data);
          break;
        case 403:
        case 404:
        case '403':
        case '404':
          uni.showToast({
            title: `${res.data.msg}`,
            icon: 'none',
            duration: 1500
          });
          reject(res);
          break;
        case 401:
        case 405:
        case '401':
        case '405':
          uni.showToast({
            title: `${res.data.msg}`,
            icon: 'none',
            duration: 1500
          });
          uni.setStorageSync('token', '')
          uni.navigateTo({
            url: '/pages/login/login'
          });
          reject(res);
          break;
        default:
          // 如果以上的状态码都没有匹配上->不使用数据;
          // uni.showToast({
          //   title: `${res.data.msg}`,
          //   icon: 'none',
          //   duration: 1500
          // });
          resolve(res.data);
          break;
      }
    } else {
      // [2]:返回其他：请求出错，http状态码不是200；
      uni.showToast({
        title: `HTTP状态码：${res.statusCode},${res.errMsg}`,
        icon: 'none',
        duration: 1500
      })
      reject(res);
    }
  } else {
    // [3]:不能和服务建立连接; 没有http的状态码；
    const errMsg = replaceErrMsg(res.errMsg || '');
    if (errMsg) {
      uni.showToast({
        title: errMsg,
        icon: 'none',
        duration: 1500
      })
    }
    reject(res);
  }
};

/**
 * 替换错误消息的文案
 *
 * @param errMsg uni官方给的错误消息；
 * @returns 替换后的错误消息
 */
function replaceErrMsg(errMsg) {
  const errList = [
    {
      android: 'request unknow host error',
      ios: '似乎已断开与互联网的连接',
      err: '似乎已断开与互联网的连接',
    },
    {
      android: 'request:fail ok',
      ios: '',
      err: '服务器开小差了～',
    },
    {
      android: 'request:fail interrupted',
      ios: '',
      err: '服务器开小差了～',
    },
    {
      // 当手机在功能内进入休眠状态。过段时间唤醒时，toast会报错。某些手机会有问题。
      android: 'createRequestTask:fail',
      ios: '',
      err: '',
    }
  ];
  const target = errList.find(item => {
    return (item.android && errMsg.indexOf(item.android) != -1) || (item.ios && errMsg.indexOf(item.ios) != -1) ? true : false;
  })
  return target ? target.err : errMsg;
}

function formatParams(data) {
  let str = '';
  for (const key in data) {
    if (data.hasOwnProperty(key)) {
      str += `${key}/${data[key]}/`
    }
  }
  return str;
}

function reLogin() {
  uni.removeStorageSync('token')
  uni.navigateTo({
    url: '/pages/login/login'
  })
}

const http = {
  /**
   * GET请求
   * @param url 要请求的API地址
   * @param params 承载的数据
   * @param auth 接口请求是否免登录
   * @returns 返回一个Promise对象
   */
  get(url, params = {}, auth) {
    const token = uni.getStorageSync('token');
    const isAuth = auth ? auth : false
    header['content-type'] = 'application/x-www-form-urlencoded'
    if (!isAuth) {
      header['Authorization'] = token
    }
    if (token || isAuth) {
      return new Promise((resolve, reject) => {
        uni.request({
          url: config.baseUrl + url + formatParams(params),
          method: 'GET',
          header: header,
          complete(res) {
            requestHandle(res, resolve, reject);
          }
        })
      });
    } else {
      reLogin()
    }

  },

  /**
   * POST请求
   * @param url 要请求的API地址
   * @param params 承载的数据
   * @param auth 接口请求是否免登录
   * @returns 返回一个Promise对象
   */
  post(url, params = {}, auth) {
    const token = uni.getStorageSync('token');
    const isAuth = auth ? auth : false
    let header = {}
    header['content-type'] = 'application/x-www-form-urlencoded'
    if (!isAuth) {
      header['Authorization'] = token
    }
    if (token || isAuth) {
      return new Promise((resolve, reject) => {
        uni.request({
          url: config.baseUrl + url,
          data: params,
          method: 'POST',
          header: header,
          complete(res) {
            requestHandle(res, resolve, reject);
          }
        })
      });
    } else {
      reLogin()
    }
  }
}

export default http

```
