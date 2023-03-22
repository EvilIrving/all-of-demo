import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'


// 插件统一管理
Vue.prototype.$commonSetting = { chartFontSize: 12 };

// echarts
import echarts from "echarts";
Vue.prototype.$echarts = echarts;

// import VCharts from '@linjhon/v-charts';
import VCharts from 'v-charts'
Vue.use(VCharts);

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: function (h) { return h(App) }
}).$mount('#app')
