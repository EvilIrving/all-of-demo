/*
 * @Author: hanyu
 * @LastEditTime: 2021-08-06 14:35:00
 * @Description: 存放常用方法
 * @FilePath: \rscp-big-screen\src\utils\utils.js
 */

/** 
 * @function: 
 * @description: 监测对象是否某值是否为空，设置为 - 
 * @param {*} obj 传入对象
 * @return {*}
 */
export function filterParams(obj) {
  let _newPar = {};
  for (let key in obj) {
    if ((obj[key] === 0 || obj[key]) && obj[key].toString().replace(/(^\s*)|(\s*$)/g, '') !== '') {
      _newPar[key] = obj[key];
    } else {
      _newPar[key] = '-';
    }
  }
  // 返回新对象
  return _newPar;
}

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

export function isEmpty(val) {
	if (val == "" || val == undefined || val == null || val == "null") {
		return "-";
	} else {
		return val;
	}
}