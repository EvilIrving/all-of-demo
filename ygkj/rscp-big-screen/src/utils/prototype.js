/*
 * @Author: hanyu
 * @LastEditTime: 2021-12-03 09:41:49
 * @Description: 存放原型拓展方法
 * @FilePath: /rscp-big-screen/src/utils/prototype.js
 */

// 对日期进行格式化输出
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


// 查询元素在数组中的索引值
Array.prototype.indexValue = function (arr) {
  for (var i = 0; i < this.length; i++) {
    if (this[i] == arr) {
      return i;
    }
  }
}

// 查询scale工程类型
String.prototype.searchSacle = function () {
  let scale = this[0]
  let scaleName = ''
  switch(scale){
    case '1':
      scaleName = '大(1)型'
    break;
    case '2':
      scaleName = '大(2)型'
    break;
    case '3':
      scaleName = '中型'
    break;
    case '4':
      scaleName = '小(1)型'
    break;
    case '5':
      scaleName = '小(2)型'
    break;
  }
  return scaleName
}
