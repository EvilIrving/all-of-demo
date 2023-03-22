import Vue from 'vue'
import App from './App.vue'

import '@/assets/iconfont'

import { handelColor } from "@/assets/js/utils.js"
Vue.prototype.$handleColor = handelColor;
console.log(handelColor);
new Vue({
  render: h => h(App),
}).$mount('#app')
