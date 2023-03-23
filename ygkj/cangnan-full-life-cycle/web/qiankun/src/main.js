import Vue from 'vue';
import App from './App.vue';
import router from './router';

//elementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);
Vue.config.productionTip = false;
//精灵图样式
import "./style/sprite.less";
//全局样式
import './style/index.scss';
//Vuex
import store from './store';
// VXETable
import 'xe-utils';
import VXETable from 'vxe-table';
import '@/style/vxe-table-variables.scss';
Vue.use(VXETable);
VXETable.setup({ table: { showHeaderOverflow: 'tooltip' } });

import { localData, sessionData } from './plugins/storage';
// 乾坤微应用
import start from './qiankun.js';

Vue.prototype.$localData = localData;
Vue.prototype.$sessionData = sessionData;

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#container');

start();
