import Vue from "vue";
import App from "./App.vue";

// 路由
import router from "./router";

// Vuex
import store from "./store";

// elementUI
import 'element-ui/lib/theme-chalk/base.css';
import {
  Button,
  Progress,
  Loading,
  CheckboxGroup,
  Checkbox,
  RadioGroup,
  Radio,
  Select,
  Option,
  Table,
  TableColumn,
  Message,
  Alert,
  Carousel,
  CarouselItem,
  DatePicker,
  Switch,
  Dialog,
  Input,
  Popover,
  Tabs,
  Card,
  TabPane,
  Drawer,
  Timeline,
  TimelineItem,
  Dropdown,
  DropdownMenu,
  DropdownItem,
  Cascader,
  CascaderPanel
} from "element-ui";
Vue.use(Button);
Vue.use(Progress);
Vue.use(Loading);
Vue.use(CheckboxGroup);
Vue.use(Checkbox);
Vue.use(Radio);
Vue.use(RadioGroup);
Vue.use(Select);
Vue.use(Option);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(Carousel);
Vue.use(CarouselItem);
Vue.use(DatePicker);
Vue.use(Switch);
Vue.use(Dialog);
Vue.use(Input);
Vue.use(Popover);
Vue.use(Tabs);
Vue.use(TabPane);
Vue.use(Drawer);
Vue.use(Timeline);
Vue.use(TimelineItem);
Vue.use(Dropdown);
Vue.use(DropdownMenu);
Vue.use(DropdownItem);
Vue.use(Cascader);
Vue.use(CascaderPanel);
Vue.use(Card);





import 'viewerjs/dist/viewer.css'
import VueViewer from 'v-viewer'
Vue.use(VueViewer)


Vue.prototype.$message = Message;
Vue.prototype.$alert = Alert;

// axios
import axios from "./config/httpRequset";
Vue.prototype.$http = axios;

// echarts
import echarts from "echarts";
Vue.prototype.$echarts = echarts;

//HighchartsVue
import Highcharts from 'highcharts'
import highcharts3d from 'highcharts/highcharts-3d'
import cylinder from 'highcharts/modules/cylinder'
import funnel3d from 'highcharts/modules/funnel3d'
highcharts3d(Highcharts)
funnel3d(Highcharts);
cylinder(Highcharts);


// 全局样式
import "./style/index.scss";

// 全局组件

// 公用方法
import "./utils/prototype";
import "./utils/utils";
import "./utils/directives";
import "./utils/filter";

// flexible
import "./plugins/flexible";

// echarts-resize
import "./plugins/echarts-resize";

// 缓存
import { localData, sessionData } from "../src/plugins/storage";
Vue.prototype.$localData = localData;
Vue.prototype.$sessionData = sessionData;

import { clickLog } from "@/api/api_reservoir";
// document.body.addEventListener('click', function handle(event) {
//   // 页面任何click事件发生首先会执行该函数，并且将event传给你了
//   // 你甚至可以阻止后面的事件
//   if (event.srcElement.baseURI.indexOf('skzgScreen') !== -1) {
//     clickLog({
//       title: "智慧水库-" + event.target.innerHTML
//     });
//   }
// }, true);




// 全局变量
import config from "./config/config";
Vue.prototype.$config = config;

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
