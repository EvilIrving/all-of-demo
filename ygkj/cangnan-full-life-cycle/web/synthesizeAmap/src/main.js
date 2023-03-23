import Vue from "vue";
import App from "./App.vue";
import "./public-path";
import actions from "./actions";

import { Slider, Tooltip } from "ant-design-vue";
import "ant-design-vue/dist/antd.css";
Vue.use(Slider).use(Tooltip);

/**
 * EventBus
 * @desc 发送消息 EventBus.$emit(channel: string, callback(payload1,…)) 监听接收消息 EventBus.$on(channel: string, callback(payload1,…))
 */
Vue.prototype.$EventBus = new Vue();

//elementUI
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
Vue.use(ElementUI);

//UmyUi
import { UTable, UTableColumn } from "umy-ui";
import "umy-ui/lib/theme-chalk/index.css"; // 引入样式
Vue.use(UTable);
Vue.use(UTableColumn);

//路由
import router from "./router";

//Vuex
import store from "./store";

//走拦截器的全局axios
import { axios } from "./config/httpRequset";
Vue.prototype.$http = axios;

// echarts
import echarts from "echarts";
Vue.prototype.$echarts = echarts;

import dataV from "@jiaminghi/data-view";
Vue.use(dataV);

//swiper
import VueAwesomeSwiper from "vue-awesome-swiper";
import "swiper/swiper-bundle.css";
Vue.use(VueAwesomeSwiper);

//moment
import moment from "moment";

//加密 crypto-js
import crypto from "crypto-js";
Vue.prototype.$crypto = crypto;

//全局变量config
import config from "./config/config";
Vue.prototype.$config = config;

//精灵图样式
import "./style/sprite.less";

//图表  也可以组件中按需引入
import VCharts from "v-charts";
Vue.use(VCharts);

//全局样式
import "./style/index.scss";

//公用方法
import "./utils/prototype";
import "./utils/utils";
import "./utils/directives";
import "./utils/filter";
import "./utils/rem";

// 插件
import { localData, sessionData } from "../src/plugins/storage";
Vue.prototype.$localData = localData;
Vue.prototype.$sessionData = sessionData;
Vue.prototype.$moment = moment;

Vue.config.productionTip = false;

let instance = null;
function render(props = {}) {
	const { container } = props;

	instance = new Vue({
		router,
		store,
		render: (h) => h(App),
	}).$mount(container ? container.querySelector("#app") : "#app");
}

if (!window.__POWERED_BY_QIANKUN__) {
	render();
} else {
	// eslint-disable-next-line no-undef,camelcase
	__webpack_public_path__ = window.__INJECTED_PUBLIC_PATH_BY_QIANKUN__;
}

export async function bootstrap() {
	console.log("[vue] vue app bootstraped");
}

export async function mount(props) {
	actions.setActions(props);
	// console.log("[vue] props from main framework", props);
	render(props);
}

export async function unmount() {
	instance.$destroy();
	instance.$el.innerHTML = "";
	instance = null;
}


// new Vue({
// 	router,
// 	store,
// 	render: (h) => h(App),
// }).$mount("#app");
