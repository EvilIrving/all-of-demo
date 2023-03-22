---
title: Vue中封装Axios
date: 2022-05-31 17:36:34
update:  2022-05-31 17:36:34
tags: 
- Vue
- Axios
categories: 
- Vue
---


```JavaScript
import axios from "axios";
import config from "./config";
import { Message } from "element-ui";
import qs from "qs";
import router from "../router/index";
import Cookies from "js-cookie";

const http = {};

var $http = axios.create({
  timeout: 15000,
  baseURL: config.baseUrl,
  validateStatus(status) {
    switch (status) {
      case 400:
        Message.error("错误请求");
        break;
      case 401:
        Message.warning("未授权，请重新登录");
        break;
      case 403:
        Message.warning("拒绝访问");
        break;
      case 404:
        Message.error("请求错误,未找到该资源");
        break;
      case 405:
        Message.warning("请求方法未允许");
        break;
      case 408:
        Message.warning("请求超时");
        break;
      case 500:
        Message.warning("服务器端出错");
        break;
      case 501:
        Message.warning("网络未实现");
        break;
      case 502:
        Message.warning("网络错误");
        break;
      case 503:
        Message.warning("服务不可用");
        break;
      case 504:
        Message.warning("网络超时");
        break;
      case 505:
        Message.warning("http版本不支持该请求");
        break;
    }
    return status >= 200 && status < 300;
  },
});

http.get = (url, params) => {
  params = params || {};
  return new Promise((resolve, reject) => {
    $http
      .get(url, { params: params })
      .then((res) => {
        if (res.status === 200) {
          resolve(res.data);
        } else {
          Message.error({
            message: res.data.msg,
          });
          reject(res.data.msg);
        }
      })
      .catch((error) => {
        alert(error, "网络异常");
      });
  });
};

http.post = (url, params) => {
  params = params ? params : {};
  return new Promise((resolve, reject) => {
    $http
      .post(url, params)
      .then((res) => {
        if (res.status === 200) {
          resolve(res.data);
        } else {
          Message.error({
            message: res.data.msg,
          });
          reject(res.data.msg);
        }
      })
      .catch((error) => {
        alert(error, "网络异常");
      });
  });
};

http.postJson = (url, params) => {
  const accessToken = Cookies.get("Admin-Token");
  const opt = {
    baseURL: config.baseUrl,
    headers: { authorization: accessToken },
  };
  return new Promise((resolve, reject) => {
    axios
      .post(url, params, opt)
      .then((res) => {
        if (res.status === 200) {
          resolve(res.data);
        } else {
          Message.error({
            message: res.data.msg,
          });
          reject(res.data.msg);
        }
      })
      .catch((error) => {
        alert(error, "网络异常");
      });
  });
};

http.putJson = (url, params) => {
  const accessToken = Cookies.get("Admin-Token");
  const opt = {
    baseURL: config.baseUrl,
    headers: { authorization: accessToken },
  };
  return new Promise((resolve, reject) => {
    axios
      .put(url, params, opt)
      .then((res) => {
        if (res.status === 200) {
          resolve(res.data);
        } else {
          Message.error({
            message: res.data.msg,
          });
          reject(res.data.msg);
        }
      })
      .catch((error) => {
        alert(error, "网络异常");
      });
  });
};

http.put = (url, params) => {
  params = params ? params : {};
  return new Promise((resolve, reject) => {
    $http
      .put(url, params)
      .then((res) => {
        if (res.status === 200) {
          resolve(res.data);
        } else {
          Message.error({
            message: res.data.msg,
          });
          reject(res.data.msg);
        }
      })
      .catch((error) => {
        alert(error, "网络异常");
      });
  });
};

http.delete = (url, params) => {
  params = params || {};
  return new Promise((resolve, reject) => {
    $http
      .delete(url, params)
      .then((res) => {
        if (res.status === 200) {
          resolve(res.data);
        } else {
          Message.error({
            message: res.data.msg,
          });
          reject(res.data.msg);
        }
      })
      .catch((error) => {
        alert(error, "网络异常");
      });
  });
};

//指定Content-Type为FormData
http.postFormData = (url, params) => {
  params = params ? params : {};
  return new Promise((resolve, reject) => {
    const accessToken = Cookies.get("Admin-Token");
    axios
      .post(url, params, {
        baseURL: config.baseUrl,
        headers: {
          authorization: accessToken,
          "Content-Type": "application/json",
        },
      })
      .then((res) => {
        if (res.status === 200) {
          resolve(res.data);
        } else {
          Message.error({
            message: res.data.msg,
          });
          reject(res.data.msg);
        }
      })
      .catch((error) => {
        alert("网络异常" + error);
      });
  });
};

// http request 拦截器
$http.interceptors.request.use(
  (config) => {
    const accessToken = Cookies.get("Admin-Token");

    config.data = qs.stringify(config.data, {
      arrayFormat: "indices",
      allowDots: true,
    });
    config.headers = {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
      authorization: accessToken,
    };
    if (config.data.status === 401) {
      router.replace({
        path: "/login", // 重新回到登陆页
      });
    }
    return config;
  },
  (err) => {
    return Promise.reject(err);
  }
);

//响应拦截器即异常处理
$http.interceptors.response.use(
  (response) => {
    if (response.data.code === 401) {
      Message.error({
        message: response.data.message,
      });
      router.replace({
        path: "/login", // 重新回到登陆页
      });
    } else {
      return response;
    }
  },
  (err) => {
    return Promise.resolve(err.response);
  }
);

export default http;
//axios
import axios from './config/httpRequset'
Vue.prototype.$http = axios

//全局变量config 
import config from './config/config'
Vue.prototype.$config = config

```
