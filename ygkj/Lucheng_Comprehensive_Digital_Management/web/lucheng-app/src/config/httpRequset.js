import axios from 'axios'
import config from "./config";
import qs from 'qs'
import router from '../router/index'
import { Toast } from 'vant';
const http = {}

import { localData, sessionData } from "../plugins/storage";


var $http = axios.create({
  timeout: 15000,
  baseURL: config.baseUrl,
  validateStatus(status) {
    switch (status) {
      case 400:
        Toast('错误请求')
        break;
      case 401:
        Toast('未授权，请重新登录')
        break;
      case 403:
        Toast('拒绝访问')
        break;
      case 404:
        Toast('请求错误,未找到该资源')
        break;
      case 405:
        Toast('请求方法未允许')
        break;
      case 408:
        Toast('请求超时')
        break;
      case 500:
        Toast('服务器端出错')
        break;
      case 501:
        Toast('网络未实现')
        break;
      case 502:
        Toast('网络错误')
        break;
      case 503:
        Toast('服务不可用')
        break;
      case 504:
        Toast('网络超时')
        break;
      case 505:
        Toast('http版本不支持该请求')
        break;
    }
    return status >= 200 && status < 300
  }
})

http.get = (url, params, contentType) => {
  params = params || {};
  return new Promise((resolve, reject) => {
    $http({
      method: "get",
      url: url,
      params: params || {},
      headers: {
        "content-type":
          contentType || "application/x-www-form-urlencoded;charset=UTF-8"
      }
    }).then(res => {
      resolve(res.data);
    }).catch(error => {
      reject(error);
    });
  })
}

http.post = (url, params) => {
  params = params ? params : {test: 'test'}
  return new Promise((resolve, reject) => {
    $http.post(url, params).then(res => {
      if (res.status === 200) {
        resolve(res.data);
      } else {
        Toast('服务器异常，请联系管理员')
        reject(res.data.msg)
      }
    }).catch(error => {
      Toast(error, '网络异常');
    })
  })
}

http.postJson = (url, params) => {
  const userInfo = localData("get", "userInfo");
  const { accessToken } = userInfo ? userInfo : { accessToken: "" };
  const opt = {
    baseURL: config.baseUrl,
    headers: { authorization: accessToken, 'Content-Type': 'application/json' },
  };
  return new Promise((resolve, reject) => {
    axios
      .post(url, params, opt)
      .then((res) => {
        if (res.status === 200) {
          resolve(res.data);
        } else {
          Toast('服务器异常，请联系管理员');
          reject(res.data.msg);
        }
      })
      .catch((error) => {
        Toast(error, '网络异常');
        // alert(error, "网络异常");
      });
  });
};

http.put = (url, params) => {
  params = params ? params : {}
  return new Promise((resolve, reject) => {
    $http.put(url, params).then(res => {
      if (res.status === 200) {
        resolve(res.data);
      } else {
        Toast(res.data.msg)
        reject(res.data.msg)
      }
    }).catch(error => {
      Toast(error, '网络异常');
    })
  })
}

http.delete = (url, params) => {
  params = params || {};
  return new Promise((resolve, reject) => {
    $http.delete(url, params).then(res => {
      if (res.status === 200) {
        resolve(res.data);
      } else {
        Toast(res.data.msg)
        reject(res.data.msg)
      }
    }).catch(error => {
      Toast(error, '网络异常');
    })
  })
}

// const token = 'eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.AVJX7jFklZWaWZsFTXzHhvUXoLEqHGVvFxkOXXxdxLyUZ8ebI3lyDSVH1-0hooUx0myePIHIPSxdsg4dIqsFpEYQXB82cBXYrjWR_n2wtGdHNisUsMihaV4STbu6kFRKLwQzujDTIpOZaYZd3IT--gbg8hycyFu4gpwXpNFBm9c.cicIwYfI-fu8Z-hp.WgW7NQL4QWvoA28x4vChoMnZ-rDOu3beUOemad9YieokpyuQ8lathNSSz7q-bcBmI7cf8R2HVpX7x4M3auN22psnjpDjPcjYfaAPD3SRT4hdLdnpSKst9Ccpzb_HJfrz43yvckXwPNpNr8xS4NiGAHZ9Fhxki_NMa0vVoYqCIcCMxF3aNrJMlf59Ctx-7ophpbWqCJBjlU9X7NzaSEQfftCKYYGtx0UG01RlBywLrw5AE3SnZA.vuwW-oRXyN5KhQL_06fXeg';
// http request 拦截器
$http.interceptors.request.use(
  conf => {
    if (conf.url !== '/ums/upload') {
      conf.data = qs.stringify(conf.data);
      conf.headers = {
        'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
        'Access-Control-Allow-Origin': "*"
      };
    }
    let userInfo = sessionData("get", "userInfo");
    let {
      accessToken
    } = userInfo ? userInfo : {
      accessToken: ""
    };
    if (userInfo) {
      conf.headers['authorization'] = accessToken;
    }
    // if (userInfo) {
    //   conf.headers.authorization = userInfo.accessToken;
    // }

    if (conf.data.status === 401) {
      router.replace({
        path: '/' // 重新回到登陆页
      })
    }
    return conf;
  },
  err => {
    return Promise.reject(err);
  }
);

//响应拦截器即异常处理
$http.interceptors.response.use(async (response) => {
  if (response.data.code === 401) {
    Toast(response.data.message)
    await sessionStorage.clear()
    await localStorage.clear()
    router.push({
      path: '/?go=1' // 重新回到登陆页
    })
  } else {
    return response
  }
}, err => {
  return Promise.resolve(err.response)
})

export default http