import axios from "axios";
import config from "./config";
import { MessageBox, Message } from "element-ui";
import qs from "qs";
// import router from "../router/index";
import { localData } from "../plugins/storage";

//axios
const service = axios.create({
  timeout: 100000,
  baseURL: config.baseUrl
});

//request
service.interceptors.request.use(
  config => {
    let userInfo = localData("get", "userInfo");
    let { accessToken } = userInfo ? userInfo : { accessToken: "" };
    config.headers["authorization"] = accessToken;
    config.headers["doNotAuth"] = true;
    return config;
  },
  err => {
    return Promise.reject(err);
  }
);

//response
service.interceptors.response.use(
  response => {
    // console.log(response, 1)
    const res = response.data;
    if (res.code !== 0) {
      if (res.code == undefined) {
        //单点登录时的判断
        return res;
      }
      const { msg, message } = response.data;
      Message({
        message: (msg ? msg : message) || "请求失败",
        type: "error",
        duration: 3 * 1000
      });
      if (res.code === 401) {
        // to re-login
        MessageBox.confirm(
          "token已失效，您可以取消以停留在此页，或重新登录",
          "确认登出",
          {
            confirmButtonText: "重新登入",
            cancelButtonText: "取消",
            type: "warning"
          }
        ).then(() => {
          // router.replace({
          //   path: "/login"
          // });
        });
      }
      return Promise.reject(new Error(msg ? msg : message || "Error"));
    } else {
      // console.log(res, 4)
      return res;
    }
  },
  err => {
    if (err && new Error(err) == "Error: Error: Network Error") {
      Message.warning("请求失败，请检查网络环境！");
      return Promise.reject(err);
    }
    if (
      new Error(err) != "Error: Error: Network Error" &&
      err.response == undefined
    ) {
      Message.warning("请求超时，请检查网络是否畅通！");
      return Promise.reject(err);
    }
    if (err.response != undefined && err.response.status) {
      switch (err.response.status) {
        case 400:
          console.log(err.response.status, 56);
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
        default:
          Message.error("请求失败! 状态码 " + err.response.status);
      }
      return Promise.reject(
        new Error("请求失败! 状态码 " + err.response.status)
      );
    }
  }
);

//封装请求
const http = {};

http.get = (url, params, contentType) => {
  return new Promise((resolve, reject) => {
    service({
      method: "get",
      url: url,
      params: params || {},
      headers: {
        "content-type":
          contentType || "application/x-www-form-urlencoded;charset=UTF-8"
      }
    })
      .then(res => {
        resolve(res);
      })
      .catch(error => {
        reject(error);
      });
  });
};

http.post = (url, params) => {
  return new Promise((resolve, reject) => {
    service({
      method: "post",
      url: url,
      data: params
        ? qs.stringify(params, {
          allowDots: true
        })
        : "",
      headers: {
        "content-type": "application/x-www-form-urlencoded;charset=UTF-8"
      }
    })
      .then(res => {
        resolve(res);
      })
      .catch(error => {
        reject(error);
      });
  });
};

http.postJson = (url, params) => {
  return new Promise((resolve, reject) => {
    service({
      method: "post",
      url: url,
      data: params ? params : {},
      headers: {
        "content-type": "application/json"
      }
    })
      .then(res => {
        resolve(res);
      })
      .catch(error => {
        reject(error);
      });
  });
};

http.delete = (url, params, contentType) => {
  if (contentType == "json") {
    contentType = "application/json";
  } else {
    params = qs.stringify(params, {
      allowDots: true
    });
  }
  return new Promise((resolve, reject) => {
    service({
      method: "delete",
      url: url,
      params: params ? params : "",
      headers: {
        "content-type":
          contentType || "application/x-www-form-urlencoded;charset=UTF-8"
      }
    })
      .then(res => {
        resolve(res);
      })
      .catch(error => {
        reject(error);
      });
  });
};

http.put = (url, params, contentType) => {
  if (contentType == "json") {
    contentType = "application/json";
  } else {
    params = qs.stringify(params, {
      allowDots: true
    });
  }
  return new Promise((resolve, reject) => {
    service({
      method: "put",
      url: url,
      data: params ? params : "",
      headers: {
        "content-type":
          contentType || "application/x-www-form-urlencoded;charset=UTF-8"
      }
    })
      .then(res => {
        resolve(res);
      })
      .catch(error => {
        reject(error);
      });
  });
};

http.patch = (url, params, contentType) => {
  if (contentType == "json") {
    contentType = "application/json";
  } else {
    params = qs.stringify(params, {
      allowDots: true
    });
  }
  return new Promise((resolve, reject) => {
    service({
      method: "patch",
      url: url,
      data: params ? params : "",
      headers: {
        "content-type":
          contentType || "application/x-www-form-urlencoded;charset=UTF-8"
      }
    })
      .then(res => {
        resolve(res);
      })
      .catch(error => {
        reject(error);
      });
  });
};

export { http, service as axios };
