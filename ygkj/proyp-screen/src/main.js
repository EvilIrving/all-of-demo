/*
 * @Author: wqn
 * @Date: 2022-11-14 09:45:28
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-17 10:09:57
 */
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import 'normalize.css/normalize.css'
import 'style/index.scss' // 全局样式
import 'utils/index.js' // 公用方法(注册vue全局变量$util、$config、$olMapUtil、$localData、$sessionData，另外还包含 原型方法 和 vue自定义指令)
import 'components/index.js' // 全局组件
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import * as echarts from "echarts";
import 'echarts-gl';
Vue.use(ElementUI)
Vue.prototype.$echarts = echarts;
Vue.config.productionTip = false

//HighchartsVue
import Highcharts from 'highcharts'
import highcharts3d from 'highcharts/highcharts-3d'
import cylinder from 'highcharts/modules/cylinder'
import funnel3d from 'highcharts/modules/funnel3d'
highcharts3d(Highcharts)
funnel3d(Highcharts);
cylinder(Highcharts);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
