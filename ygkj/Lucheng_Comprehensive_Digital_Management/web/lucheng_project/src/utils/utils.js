/*
 * @Author: hanyu
 * @LastEditTime: 2022-05-23 16:06:38
 * @Description: 存放常用方法
 * @FilePath: \lucheng_project\src\utils\utils.js
 */
import axios from "axios";
import { sessionData } from "../plugins/storage";
/**
 * @description: URL编码
 * @param { Object } 参数json对象
 * @return { String }
 * @author: hanyu
 */
export function toParams(param) {
  let result = '';
  for (let name in param) {
    if (typeof param[name] != 'function') {
      if (param[name] === null) {
        result += '&' + name + '=';
      } else {
        result += '&' + name + '=' + encodeURI(param[name]);
      }
    }
  }
  return result.substring(1);
}

/**
 * @description: 防抖函数
 * @param { Function } fn 高频函数
 * @param { Number, String } wait 等待时间
 * @returns { Function }
 * @author: hanyu
 */
export function debounce(fn, wait) {
  let context = this,
    args = arguments,
    timer = null;
  return function() {
    context = this;
    args = arguments;
    clearTimeout(timer);
    timer = setTimeout(function() {
      fn.apply(context, args);
    }, wait || 250);
  };
}

/**
 * @description: 对象深拷贝
 * @param { Object } obj 对象
 * @returns { Object }
 * @author: hanyu
 */
export function cloneObj(obj) {
  let str;
  let newObj = obj.constructor === Array ? [] : {};
  if (
    Object.prototype.toString.call(obj) !== '[object Object]' &&
    Object.prototype.toString.call(obj) !== '[object Array]'
  ) {
    return;
  } else if (window.JSON) {
    str = JSON.stringify(obj); // 系列化对象
    newObj = JSON.parse(str); // 还原
  } else {
    for (let i in obj) {
      newObj[i] = typeof obj[i] === 'object' ? this.cloneObj(obj[i]) : obj[i];
    }
  }
  return newObj;
}

/**
 * 对后台请求回来的省市区数据处理
 */

export function disposeCascaderData(data, obj) {
  let objData = {
    label: data.commonAdcd.adnm,
    value: data.commonAdcd.adcd,
  };
  if (data.subset && data.subset.length > 0) {
    objData.children = [];
    data.subset.map((item) => {
      disposeCascaderData(item, objData);
    });
    if (obj && obj.children) {
      obj.children.push(objData);
    }
  } else {
    obj.children.push(objData);
  }
  return objData;
}

/**
 * @description: 获取URL参数
 * @param { String } 字符串
 * @returns { String }
 * @author: hanyu
 */
export function getQueryString(name){
  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i=0;i<vars.length;i++) {
    var pair = vars[i].split("=");
    if(pair[0] == name){return pair[1];}
  }
  return(false);
}


export function exportDetail(url,param) {
  let userInfo = sessionData("get", "userInfo");
    let { accessToken } = userInfo ? userInfo : { accessToken: "" };
    axios({
      url,
      method: "post",
      data: param ,
      responseType: "blob",
      headers: {
        authorization: accessToken,
      },
    }).then((res) => {
      const content = res.data;
      const blob = new Blob([content], { type: "application/vnd.ms-excel" });
      const link = document.createElement("a"); // 创建a标签
      let filename = res.headers["content-disposition"];
      // console.log(filename, 12345);
      filename = filename ? filename.substr(filename.indexOf("=") + 1) : "";
      filename = decodeURIComponent(filename);
      link.download = filename;
      link.style.display = "none";
      link.href = URL.createObjectURL(blob);
      document.body.appendChild(link);
      link.click(); // 执行下载
      URL.revokeObjectURL(link.href); // 释放url
      document.body.removeChild(link); // 释放标签
    });
}