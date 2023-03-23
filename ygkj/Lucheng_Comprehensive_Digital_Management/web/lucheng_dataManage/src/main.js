/*
 * @Author: dyn
 * @Date: 2022-02-17 15:43:02
 * @LastEditTime: 2022-02-22 10:52:24
 * @LastEditors: dyn
 * @Description: Modify here please
 * @FilePath: \project_manage_web\src\main.js
 */
import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './styles/element-variables.scss'

// 插件
import { localData, sessionData } from "../src/plugins/storage";
Vue.prototype.$localData = localData;
Vue.prototype.$sessionData = sessionData;
import moment from 'moment'

//精灵图样式
import "@/styles/sprite.less";

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import './icons' // icon
// import './permission' // permission control
import "./libs/rem";

// 权限指令
// import permission from "@/directive/permission/index";
import '@/libs/validate.form'
import * as filters from './filters' // global filters

Vue.prototype.$moment = moment;
// Vue.use(permission)
Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})


// register global utility filters
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
