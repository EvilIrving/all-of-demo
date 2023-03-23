import Vue from "vue";
import App from "./App.vue";

//路由
import router from "./router";
import './router/permission'
//Vuex
import store from "./store";
//elementUI
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
Vue.use(ElementUI);

// 需要引入的js
import 'video.js/dist/video-js.css'; //videoJs的样式
import 'videojs-flash'; //引入才能播放rtmp视屏

import VideoPlayer from "vue-video-player";
export default {
  install: function(Vue) {
    Vue.use(VideoPlayer);
  },
};
//axios
import axios from "./config/httpRequset";
Vue.prototype.$http = axios;

//echarts
import echarts from "echarts";
Vue.prototype.$echarts = echarts;


//VCharts
import VCharts from "@linjhon/v-charts";
Vue.use(VCharts);

import Video from 'video.js'
import 'video.js/dist/video-js.css'

Vue.prototype.$video = Video

//全局样式
import "./style/index.scss";

//全局组件
import LookFile from "@/components/LookFile";
import Pagination from "@/components/Pagination";
Vue.component("LookFile", LookFile);
Vue.component("Pagination", Pagination);

//公用方法
import "./utils/prototype";
import "./utils/utils";
import "./utils/directives";
import "./utils/filter";
// import './router/permission'

// 插件
import { localData, sessionData } from "../src/plugins/storage";
Vue.prototype.$localData = localData;
Vue.prototype.$sessionData = sessionData;
import moment from 'moment'
Vue.prototype.$moment = moment;
//全局变量
import config from "./config/config";
Vue.prototype.$config = config;

Vue.config.productionTip = false;

//刷新页面时，首先进入onbeforeunload 方法
// window.onbeforeunload = () => {
//   let numbers = window.localStorage.getItem("numbers");
//   const sesTime = window.sessionStorage.getItem("sesTime");
//   const localTime = window.localStorage.getItem("localTime"); //当localTime 没有值，并且sesTime === localTime，减去当前页。
//   if (localTime != "NaN" && localTime != null && sesTime === localTime) {
//     numbers = parseInt(numbers) - 1;
//     window.localStorage.setItem("numbers", numbers);
//   }
// };

window.onload = () => {
  // let time = "";
  // let numbers = window.localStorage.getItem("numbers"); //计算打开的标签页数量
  // const sesTime = window.sessionStorage.getItem("sesTime");
  // const localTime = window.localStorage.getItem("localTime"); //首次登录的时间
  // //当numbers无值或者小于0时，为numbers 赋值0
  // console.log(parseInt(numbers), '---------------', sesTime, '-----------', localTime)
  // if (numbers == "NaN" || numbers === null || parseInt(numbers) < 0) {
  //   numbers = 0;
  // }
  // //sesTime || localTime 没有值，并且numbers为0时，清除localStorage里的登录状态，退出到登录页面
  // if (
  //   (sesTime === null ||
  //     sesTime === "NaN" ||
  //     localTime === null ||
  //     localTime === "NaN") &&
  //   parseInt(numbers) === 0
  // ) {
  //   localData('clean', 'userInfo')
  //   //为localTime和sesTime赋值，记录第一个标签页的时间
  //   time = new Date().getTime();
  //   window.sessionStorage.setItem("sesTime", time);
  //   window.localStorage.setItem("localTime", time);
  //   router.push("/login");
  // }
  //当time ，localTime 有值，并且sesTime != localTime时，为sesTime赋值
  // if (
  //   !time &&
  //   localTime != "NaN" &&
  //   localTime != null &&
  //   sesTime != localTime
  // ) {
  //   window.sessionStorage.setItem("sesTime", localTime);
  // }
  // //最后保存当前是第几个标签页
  // numbers = parseInt(numbers) + 1;
  // window.localStorage.setItem("numbers", numbers);
};

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
