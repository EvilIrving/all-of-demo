/*
 * @Author: hanyu
 * @LastEditTime: 2022-10-24 11:30:02
 * @Description: 请求封装、请求拦截
 * @FilePath: \lucheng_project\src\config\httpRequset.js
 */
import axios from "axios";
import config from "./config";
// import { Message } from "element-ui";
import qs from "qs";
import router from "../router/index";
import { sessionData } from "../plugins/storage";
import { ElMessage } from "../utils/message_api";
let Message = new ElMessage();
const http = {};
window.isReresh = false;

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
  return new Promise((resolve, reject) => {
    $http({
      method: "get",
      url: url,
      params: params || {},
      headers: {
        "content-type": "application/x-www-form-urlencoded;charset=UTF-8",
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
      .catch((error) => { });
  });
};

http.post = (url, params) => {
  params = params ? params : {test: 'test'};
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
      .catch((error) => { });
  });
};

http.postJson = (url, params) => {
  return new Promise((resolve, reject) => {
    $http({
      method: "post",
      url: url,
      data: params ? params : {test: 'test'},
      headers: {
        "content-type": "application/json",
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
      .catch((error) => { });
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
        // alert(error, "网络异常");
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

const urlList = [
  '/authentication-system/sys/users',
  "/ums/sys/users",
  "/project-manage/projectRel/updateProjRel",
  "/digitization/tideFcst/addOrUpdateTideFcst",
  "/project-manage/regularReview/saveOrUpdate",
  "/ums/sys/dept/users",
]
// http request 拦截器
$http.interceptors.request.use(
  (config) => {

    if (!urlList.includes(config.url)) {
      config.data = qs.stringify(config.data);
      config.headers["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8";
    } else {
      config.headers["Content-Type"] = "application/json"
    }

    let userInfo = sessionData("get", "userInfo");
    if (userInfo && userInfo.accessToken) {
      config.headers["authorization"] = userInfo.accessToken;
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
    if (response.data.code == "401") {
      sessionData("clean", "userInfo");
      Message.warning("由于您长时间未操作，登录状态已失效");
      router.replace({
        path: "/login", // 重新回到登陆页
      });
    }
    return response;
  },
  (err) => {
    return Promise.resolve(err.response);
  }
);

export default http;
