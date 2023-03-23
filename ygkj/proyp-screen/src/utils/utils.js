/**
 * @description: URL编码
 * @param { Object } 参数json对象
 * @return { String }
 * @author: hanyu
 */
export function toParams (param) {
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
export function debounce (fn, wait) {
  let context = this,
    args = arguments,
    timer = null;
  return function () {
    context = this;
    args = arguments;
    clearTimeout(timer);
    timer = setTimeout(function () {
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
export function cloneObj (obj) {
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
export function getQueryString (name) {

  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i = 0; i < vars.length; i++) {
    var pair = vars[i].split("=");
    if (pair[0] == name) { return pair[1]; }
  }
  return (false);
}

export function isEmpty (val) {
  if (val == "" || val == undefined || val == null || val == "null") {
    return "-";
  } else {
    return val;
  }
}

export function fontChart (res) {
  let docEl = document.documentElement,
    clientWidth =
      window.innerWidth ||
      docEl.clientWidth ||
      document.body.clientWidth;
  if (!clientWidth) return;
  let fontSize = clientWidth / 1920;
  return res * fontSize;
}

/**
 * @description 组装echarts立方体series参数
 * @param {string} name 对应图例名称
 * @param {number} width 立方体宽度
 * @param {number} move 立方体偏移量
 * @param {string} color 立方体颜色
 * @param {array} data 数据
 * @return {array} 返回数组，注意使用时可能需要解构后存入series
 */
export function getEchartsCubeSeries (name, width, move, color, data) {
  let barColor = [
    { offset: 0, color: "#1fa4f0" },
    { offset: 0.5, color: "#24ddff" },
    { offset: 0.5, color: "#00bdfd" },
    { offset: 1, color: "#0586f5" },
  ]
  let topColor = "#23e2fa"
  let textColor = "#00BAFF"
  if (color === 'blue') {
    barColor = [
      { offset: 0, color: "#1fa4f0" },
      { offset: 0.5, color: "#24ddff" },
      { offset: 0.5, color: "#00bdfd" },
      { offset: 1, color: "#0586f5" },
    ]
    topColor = "#23e2fa"
    textColor = "#00BAFF"
  } else if (color === 'green') {
    barColor = [
      { offset: 0, color: "#0dd0cd" },
      { offset: 0.5, color: "#19f6a6" },
      { offset: 0.5, color: "#17edb0" },
      { offset: 1, color: "#0bc8d5" },
    ]
    topColor = "#00F398"
    textColor = "#00F398"
  } else if (color === 'yellow') {
    barColor = [
      { offset: 0, color: "#fcee27" },
      { offset: 0.5, color: "#f2fe35" },
      { offset: 0.5, color: "#feed32" },
      { offset: 1, color: "#eec901" },
    ]
    topColor = "#fff799"
    textColor = "#fcee27"
  }
  return [
    {
      name,
      type: "pictorialBar",
      symbolSize: [width, 3],
      symbolOffset: [move, 3],
      symbol: "triangle",
      symbolRotate: 180,
      itemStyle: {
        color: {
          x: 0,
          y: 0,
          x2: 1,
          y2: 0,
          type: "linear",
          global: false,
          colorStops: barColor,
        },
      },
      animation: false,
      data: data.map((e) => (e > 0 ? 1 : null)),
    },
    {
      name,
      type: "bar",
      barGap: 0,
      barWidth: width,
      itemStyle: {
        color: {
          x: 1,
          y: 0,
          x2: 0,
          y2: 0,
          type: "linear",
          global: false,
          colorStops: barColor,
        },
      },
      animation: false,
      data: data,
    },
    {
      name,
      type: "pictorialBar",
      symbol: "diamond",
      symbolSize: [width, 6],
      symbolOffset: [move, -3],
      itemStyle: { color: topColor },
      label: {
        show: true,
        position: "top",
        textStyle: { color: textColor, fontSize: 16 },
        distance: 0,
      },
      symbolPosition: "end",
      animation: false,
      data: data.map((e) => (e > 0 ? e : null)),
    },
  ]
}

/**
 * @description 获取当前页面的源地址
 * @return {string}
 */
export function getWebPageOrigin () {
  let url = '';
  if (window.location.origin) {
    url = window.location.origin;
  } else {
    url = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
  }
  return url;
}