import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import TDesign from 'tdesign-vue';
import { Icon } from 'tdesign-icons-vue';

Vue.component('icon', Icon)

// 引入组件库的少量全局样式变量
import 'tdesign-vue/es/style/index.css';

Vue.use(TDesign);
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: function (h) { return h(App) }
}).$mount('#app')
