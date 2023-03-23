
// 埋点对象
var zwlog;

// 页面埋点数据
var pageLogMap = {};

/*
 * *******用户信息采集，同时声明 Zwlog 对象实例
 * uerId 用户 ID  没登录就非必填，登录了必填
 */
export function initZwlog(userId) {
  let uerId = userId ? userId : "";
  zwlog = new ZwLog({ _user_id: uerId });
}

/*
 * *******PV 日志参数- global_args
 * url 页面路径 【选填】（未传参默认获取当前路由地址）
 * enterPageTime 【必填】（默认在路由改变的时候自动获取时间） 进入页面的时间 new Date()
 * leavePageTime 【必填】（默认在路由改变的时候自动获取时间） 离开页面的时间 new Date()
 * loadTime【必填】（页面onShow中获取当前时间） 加载完的时间 new Date()
 * responseTime 【必填】（页面onShow的nextTick中获取当前时间） 响应完的时间 new Date()
 */
export function zwlogPvGlobal({
                                _this = null,
                                enterPageTime = null,
                                leavePageTime = null,
                                loadTime = null,
                                responseTime = null,
                                pagePath = null
                              } = {}) {
  if (!zwlog) initZwlog();
  let path = _this?_this.$route.path:pagePath;
  let title = _this?_this.$route.meta.title:null;
  if (!pageLogMap.hasOwnProperty(path))
    pageLogMap[path] = {
      enterPageTime: null,
      leavePageTime: null,
      loadTime: null,
      responseTime: null,
      title:null,
      pagePath:null
    };

  if (enterPageTime) pageLogMap[path].enterPageTime = enterPageTime;
  if (leavePageTime) pageLogMap[path].leavePageTime = leavePageTime;
  if (loadTime) pageLogMap[path].loadTime = loadTime;
  if (responseTime) pageLogMap[path].responseTime = responseTime;
  if (title) pageLogMap[path].title = title;
  if (path) pageLogMap[path].path = path;

  if (
      pageLogMap[path].enterPageTime &&
      pageLogMap[path].leavePageTime &&
      pageLogMap[path].loadTime &&
      pageLogMap[path].responseTime&&
      pageLogMap[path].title&&
      pageLogMap[path].path
  ) {
    /*
     * miniAppId 应用开发管理 平台 appId
     * miniAppName 应用开发管理 平台应用名称
     * log_status 用户登录状态 （01:未登录/ 02:单点登录）
     * Page_duration 浏览时长  用户从进入到离开当 前页面的时长
     * t2 页面加载时间  页面启动到加载完成 的时间
     * t0  页面响应时间  页面启动到页面响应 完成的时间
     * pageId  应用页面 ID
     * pageName  应用页面名称
     */
    let Page_duration =
        pageLogMap[path].leavePageTime.getTime() -
        pageLogMap[path].enterPageTime.getTime();
    let t2 =
        pageLogMap[path].loadTime.getTime() -
        pageLogMap[path].enterPageTime.getTime();
    let t0 =
        pageLogMap[path].responseTime.getTime() -
        pageLogMap[path].enterPageTime.getTime();
    setTimeout(() => {
      let pvParams = {
        miniAppId: "2002276620",
        miniAppName: "鹿城水利全域数字化",
        log_status: "02", //登录02 未登录01
        Page_duration: Page_duration / 1000 + "秒",
        t2: t2 / 1000 + "秒",
        t0: t0 / 1000 + "秒",
        pageId: path,
        pageName: pageLogMap[path].title,
      };
      zwlog.onReady(function () {
        zwlog.sendPV(pvParams);
        delete pageLogMap[path];
      });
    }, 500);
  } else {
    let result = "";
    for (let k in pageLogMap[path]) {
      if (!pageLogMap[path][k]) result += k + "，";
    }
    console.warn("zheliban === 浙里办页面" + path + "埋点缺少参数=>>", result);
  }
}

function addZwLogEvent(_this, code) {
  if (initZwlog() && _this && code) {
    let path = _this.$route.path;
    let title =_this.$route.meta.title;
    zwlog.onReady(function() {
      console.log("\n提交埋点数据 zwlog.record",
          "\n 目前自定义参数pagePath和pageTitle,查看代码中最后一个Ojbect是可以任意赋值的，提交什么就会后台收到什么",
          `\n code = ${code}
                \n pagePath = ${path}
                \n pageTitle = ${title} \n\n `);
      zwlog.record(code, 'CLK', {
        pagePath: path,
        pageTitle: title
      })
    })
  }
}


export default  {
  initZwlog,
  zwlogPvGlobal,
  addZwLogEvent
}