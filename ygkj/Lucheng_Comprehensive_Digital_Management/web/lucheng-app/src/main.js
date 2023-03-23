import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
Vue.config.productionTip = false;

// 全局样式
import "./style/index.scss";
//px转rem
import "amfe-flexible";
import noData from '@/components/noData.vue'
import ViewPage from '@/components/ViewPage.vue'
Vue.component('no-data', noData)
Vue.component('view-page', ViewPage)
//elementUI
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
Vue.use(ElementUI);

// vant
import "vant/lib/index.css";
import {
  Button,
  Icon,
  Popup,
  Toast,
  Badge,
  Form,
  Field,
  DatetimePicker,
  Uploader,
  Picker,
  Step,
  Steps,
  Tab,
  Tabs,
  Loading,
  Col,
  Row,
  DropdownMenu,
  DropdownItem,
  Search,
  Grid,
  GridItem,
  List,
  Cell,
  Rate,
  CellGroup,
  Calendar,
  Image as VanImage,
  ImagePreview,
  Checkbox,
  CheckboxGroup,
  NavBar,
  Circle,
} from "vant";
Vue.use(Button)
  .use(Checkbox)
  .use(Badge)
  .use(NavBar)
  .use(CheckboxGroup)
  .use(Icon)
  .use(Popup)
  .use(Form)
  .use(Field)
  .use(DatetimePicker)
  .use(Uploader)
  .use(Picker)
  .use(Step)
  .use(Tabs)
  .use(Tab)
  .use(Loading)
  .use(Steps)
  .use(Col)
  .use(Row)
  .use(Search)
  .use(DropdownMenu)
  .use(DropdownItem)
  .use(List)
  .use(Cell)
  .use(CellGroup)
  .use(Grid)
  .use(GridItem)
  .use(DropdownMenu)
  .use(DropdownItem)
  .use(Rate)
  .use(VanImage)
  .use(Cell)
  .use(Calendar)
  .use(Circle)
  .use(ImagePreview);

Vue.prototype.$toast = Toast;
Vue.prototype.$imagePreview = ImagePreview;

// axios
import axios from "./config/httpRequset";
Vue.prototype.$http = axios;

//echarts
import echarts from "echarts";
Vue.prototype.$echarts = echarts;

// 公用方法
import "./utils/prototype";
import "./utils/utils";
import "./utils/directives";
import "./utils/filter";

// 缓存
import { localData, sessionData } from "../src/plugins/storage";
Vue.prototype.$localData = localData;
Vue.prototype.$sessionData = sessionData;

// 全局变量
import config from "./config/config";
Vue.prototype.$config = config;

import zwUtil from "@/utils/zwUtil";
Vue.prototype.addZwlog = function (this1) {
  console.log(this1,'this')
  zwUtil.zwlogPvGlobal({
    _this: this1,
    loadTime: new Date()
  })
  // 相应时间
  zwUtil.zwlogPvGlobal({
    _this: this1,
    responseTime: new Date()
  })
}

var vm = new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");

router.afterEach((to, from) => {
  vm.$el.scrollTop = 0;
});

