import Vue from 'vue'
import App from './App.vue'

import * as echarts from 'echarts'
Vue.prototype.$echarts = echarts

//elementUI
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
Vue.use(ElementUI);

// import { Empty } from "vant";
// Vue.use(Empty);

import './style/index.scss'

Vue.config.productionTip = false

import router from './router'
// flexible
import "./plugins/flexible";

// echarts-resize
import "./plugins/echarts-resize";
new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
