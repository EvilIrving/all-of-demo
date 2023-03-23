/*
 * @Author: hanyu
 * @LastEditTime: 2020-11-18 16:20:29
 * @Description: 存放原型拓展方法
 * @FilePath: /pingHuWaterPlatform/src/utils/prototype.js
 */

/**
 * @description: 对日期进行格式化输出
 * @param { String } format 日期输出格式（如：yyyy-MM-dd）
 * @return { String }
 * @author: hanyu
 */
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "h+": this.getHours(), //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
        "S": this.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] :
                ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}

/**
 * @description: 输出名字后两字
 * @param { null }
 * @return { String }
 * @author: hanyu
 */
String.prototype.smallName = function () {
  if(this.length > 2){
    return this.slice(this.length - 2, this.length);
  }else{
    return this;
  }
}