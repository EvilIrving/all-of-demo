---
title: JavaScript Snippets
date: 2021-05-31 17:00:00
update: 2023-01-31 17:00:00
tags: 
- JavaScript
- Snippets
categories: 
- JavaScript
---
## 原型函数

### 对日期进行格式化输出

```javascript
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
```

### 输出名字后两字

```javascript
/**
 * @description: 输出名字后两字
 * @param { null }
 * @return { String }
 */
String.prototype.smallName = function () {
  if(this.length > 2){
    return this.slice(this.length - 2, this.length);
  }else{
    return this;
  }
}
```

## 其他函数

### 防抖

防抖：在用户停止某个操作一段时间之后才执行相应的监听函数，当事件被触发时，设定一个周期延迟执行动作，若期间又被触发，则重新设定周期，直到周期结束，执行监听函数。

```javascript
/**
 * @description: 防抖函数
 * @param { Function } fn 高频函数
 * @param { Number, String } wait 等待时间
 * @returns { Function }
 */
function debounce(fn, wait) {
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
let debounce = (fn, wait) => {
    let timer, timeStamp = 0, context, args
    let run = () => {
        timer = setTimeout(() => {
            fn.apply(context, args)
        }, wait)
    }

    let clean = () => {
        clearTimeout(timer)
        timer = null
    }

    return function (params) {
        context = this
        args = arguments
        let now = (new Date()).getTime()
        if (now - timeStamp < wait) {
            clean()
            run()
        } else {
            run()
        }
        timeStamp = now
    }
}
```

### 节流

节流：当持续触发事件时，保证隔间时间触发一次事件，固定周期内，只执行一次动作，若有新事件触发，不执行。周期结束后，又有事件触发，开始新的周期。

```js
let throtting = (fn, wait) => {
    let timer, context, args
    let run = () => {
        timer = setTimeout(() => {
            fn.apply(context, args)
            clearTimeout(timer)
            timer = null
        }, wait)
    }

    return function () {
        context = this
        args = arguments
        if (!timer) run()
    }
}
```

### getQueryString

```javascript
function getQueryString(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) return unescape(r[2]);
      return null;
    }

function getQueryString(name){
  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i=0;i<vars.length;i++) {
    var pair = vars[i].split("=");
    if(pair[0] == name){return pair[1];}
  }
  return(false);
}

```

### url查询参数转为params

```js
function query2Dict(param) {
    let pattern = /([^?&=]+)=([^&#]*)/g;
    let dict = {}, search = null
    if (typeof param === 'object' && param instanceof Location) { search = param.search }
    else if (typeof param === 'string') { search = param }
    else {
        throw new Error('参数类型非法！请传入window.loaction对象或者url字符串。')
    }
    search.replace(pattern, function (rs, $1, $2) {
        let key = decodeURIComponent($1)
        let value = decodeURIComponent($2)
        dict[key] = value
        return rs
    })
    return dict
}
```

### 屏幕自动滚动

```javascript
setInterval(() => {
    window.scrollBy({
        top: 1,
        left: 0,
        behavior: "smooth"
    });
}, 30);
```

### URL编码

```javascript
/**
 * @description: URL编码
 * @param { Object } 参数json对象
 * @return { String }
 */
function toParams(param) {
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
```

### 是否为url

```javascript
/**
 * @description 是否为url
 * @param {String} path path
 * @returns Boolean
 */
function isUrl(path) {
  // 开始符 ^
  // 协议部分http(s)://        表示为((https|http|ftp|rtsp|mms)?:\/\/)
  // 域名部分                  表示为(([A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\.)+
  // 顶级域名com cn等为2-6位   表示为([a-zA-Z]{2,6})
  // 端口部分                  表示为(:\d+)?, ?表示0次或1次
  // 请求路径如/login          表示为 (\/.*)?
  // 问号传参及哈希值如?age=1   表示为 (\?.*)?和(#.*)?
  // 结束符 $
  const reg = /^((https|http|ftp|rtsp|mms)?:\/\/)(([A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\.)+([A-Za-z]{2,6})(:\d+)?(\/.*)?(\?.*)?(#.*)?$/
  return reg.test(path)
}
```

### isEmpty

```javascript
 function isEmpty(v) {
  if (v == null || v == '' || v == undefined || v == 'null') {
    return '-';
  } else {
    if(v.length > 8){
      if(isNum(v)){
          return parseFloat(v).toFixed(2)
      }else{
        return v;
      }
    }else{
      return v;
    }
  }
}
```

### isNum

```javascript
/**
 * @description:  是否是数字
 * @param { String } str 字符串
 * @returns { Boolean }
 */
function isNum(str){
  var numReg = /^[0-9]*$/
  var numRe = new RegExp(numReg)
  if (!numRe.test(str)) {
    return false;
  }else{
    return true;
  }
}
```

### 对象深拷贝

```javascript
/**
 * @description: 对象深拷贝
 * @param { Object } obj 对象
 * @returns { Object }
 */
 function cloneObj(obj) {
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
```

### 保留两位方法

```javascript
function returnFloat(value) {
        var value = Math.round(parseFloat(value) * 100) / 100;
        var xsd = value.toString().split(".");
        if (xsd.length == 1) {
            value = value.toString() + ".00";
            return value;
        }
        if (xsd.length > 1) {
            if (xsd[1].length < 2) {
                value = value.toString() + "0";
            }
            return value;
        }
    }
```

### 格式化数字 每隔3位添加一个逗号

```javascript
/**
 * @description: 格式化数字 每隔3位添加一个逗号
 * @param { Number } value
 * @param { Boolean } int 是否返回整数 默认false
 * @return { String }
 */
function numFormat(value, int) {
  if (!value) return "0.00";

  /*原来用的是Number(value).toFixed(0)，这样取整时有问题，例如0.51取整之后为1，感谢Nils指正*/
  /*后来改成了 Number(value)|0,但是输入超过十一位就为负数了，具体见评论 */
  var intPart = Number(value) - (Number(value) % 1); //获取整数部分（这里是windy93的方法）
  var intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, "$1,"); //将整数部分逢三一断

  var floatPart = ".00"; //预定义小数部分
  var value2Array = value.toString().split(".");

  //=2表示数据有小数位
  if (value2Array.length == 2) {
    floatPart = value2Array[1].toString(); //拿到小数部分

    if (floatPart.length == 1) {
      //补0,实际上用不着
      return intPartFormat + "." + floatPart + "0";
    } else {
      return intPartFormat + "." + floatPart;
    }
  } else {
    if (int) return intPartFormat;
    return intPartFormat + floatPart;
  }
}
```

### 根据日期获取星期几

```javascript
/**
 * @description: 根据日期获取星期几
 * @param { String } dateString 日期字符串（如：2020-05-02）
 * @return { String }
 */
function getWeek(dateString) {
  let dateArray = dateString.split("-");
  let date = new Date(dateArray[0], parseInt(dateArray[1] - 1), dateArray[2]);
  return "周" + "日一二三四五六".charAt(date.getDay());
}
```

### 以下载方式保存Blob格式

```javascript
/**
 * 以下载方式保存Blob格式
 * @param {Blob} blob Blob格式的数据
 * @param {String} filename 文件名
 */
function saveFile(blob, filename) {
    if (window.navigator.msSaveOrOpenBlob) {
        navigator.msSaveBlob(blob, filename);
    } else {
        let link = document.createElement("a");
        let body = document.querySelector("body");
        link.href = window.URL.createObjectURL(blob);
        link.download = filename; //html5
        // fix Firefox
        link.style.display = "none";
        body.appendChild(link);
        link.click();
        body.removeChild(link);
        window.URL.revokeObjectURL(link.href);
    }
}
```

### 查找数组中最值

```javascript
// 查找数组中最小值
arrayMin(arrs) {
    var min = arrs[0];
    for (var i = 1, ilen = arrs.length; i < ilen; i += 1) {
        if (arrs[i] < min) {
            min = arrs[i];
        }
    }
    return min;
}

// 查找数组中最大值
arrayMax(arrs) {
    var max = arrs[0];
    for (var i = 1, ilen = arrs.length; i < ilen; i++) {
        if (arrs[i] > max) {
            max = arrs[i];
        }
    }
    return max;
}
```

### toSort按对象键名排序

```javascript
toSort(propertyName) {
      return (a, b) => {
        // return a[propertyName] - b[propertyName]    // 升序
        return b[propertyName] - a[propertyName]; // 降序
      };
    }
```

### 对象判空

```javascript
if (Object.keys(object).length === 0) {
    return false // 如果为空,返回false
}
return true // 如果不为空，则会执行到这一步，返回true


if (JSON.stringify(data) === '{}') {
    return false // 如果为空,返回false
}
return true // 如果不为空，则会执行到这一步，返回true


for (var i in obj) { // 如果不为空，则会执行到这一步，返回true
    return true
}
return false // 如果为空,返回false
```

### 取消console.log

```javascript
chainWebpack(config) {
    config.optimization.minimizer('terser').tap((args) => {
      args[0].terserOptions.compress.drop_console = true
      return args
    })
}
```

### 计时器清除

定时器返回值其实就是一个整数，并且是顺序生成的，因此循环清除定时器，在你构造的定时器数量不是太夸张（小于100）的情况下，该方法可以清除所有定时器

```js
var end = setTimeout(function(){},1);
var start = (end -100)>0?end-100:0;
for(var i=start;i<=end;i++){
  clearTimeout(i);
}
```

```js
let endTid = setTimeout(function () {});
for (let i = 0; i <= endTid; i++) {
  clearTimeout(i)
  clearInterval(i)
}
```

## 参考

- <https://bigfrontend.dev/problem>
