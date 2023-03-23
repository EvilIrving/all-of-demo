/*
 * @Author: wqn
 * @Date: 2022-11-22 14:32:04
 * @LastEditors: wqn
 * @LastEditTime: 2022-11-22 15:57:45
 */

/**
 * @description 对 Date 的扩展，将 Date 转化为指定格式的 String
 * 年(y)可以用 1-4 个占位符
 * 月(M)、日(d)、12小时(h)、24小时(H)、分(m)、秒(s)、周(E)、季度(q)可以用 1-2 个占位符
 * 早午晚(T)、毫秒(S)只能用 1 个占位符
 * @param {string} fmt 日期格式
 * @returns {string} 格式化后的日期
 */
Date.prototype.format = function (fmt) {
  const o = {
    "M+": this.getMonth() + 1, "d+": this.getDate(), "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12,
    "H+": this.getHours(),
    "m+": this.getMinutes(),
    "s+": this.getSeconds(),
    "q+": Math.floor(this.getMonth() / 3 + 1),
    "S": this.getMilliseconds()
  }
  const week = { "0": "\u65e5", "1": "\u4e00", "2": "\u4e8c", "3": "\u4e09", "4": "\u56db", "5": "\u4e94", "6": "\u516d" }
  const time = { "0": "\u51cc\u6668", "1": "\u4e0a\u5348", "2": "\u4e0b\u5348", "3": "\u665a\u4e0a" }
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length))
  if (/(E+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length > 1 ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""])
  if (/(T)/.test(fmt)) fmt = fmt.replace(RegExp.$1, time[parseInt(this.getHours() / 6)])
  for (let k in o) {
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? o[k] : ('00' + o[k]).substr(('' + o[k]).length))
  }
  return unescape(fmt.replace(/\/u/g, '%u'))
}

/**
 * 查询元素在数组中的索引值
 * @param {*} ele 
 * @returns {number} 索引值
 */
Array.prototype.getIndex = function (ele) {
  for (var i = 0; i < this.length; i++) {
    if (this[i] == ele) {
      return i
    }
  }
  return null
}