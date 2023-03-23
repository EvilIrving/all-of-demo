import Vue from 'vue';
import App from './App.vue';
import "./public-path";
import actions from './actions';
import router from './router';
import store from './store';
import Plugins from '@/plugins';

// 插件统一管理
Vue.prototype.$commonSetting = { chartFontSize: 12 };
Vue.config.productionTip = false;
Vue.use(Plugins);

// 微前端管理
let instance = null;
function render(props = {}) {
  const { container, store: microStore } = props;
  //接收主应用的store并监听
  Vue.observable(microStore);
  Vue.prototype.$microStore = microStore;
  instance = new Vue({
    router,
    store,
    render: (h) => h(App)
  }).$mount(container ? container.querySelector('#app') : '#app');
}

if (!window.__POWERED_BY_QIANKUN__) {
  render();
} else {
  // eslint-disable-next-line no-undef,camelcase
  __webpack_public_path__ = window.__INJECTED_PUBLIC_PATH_BY_QIANKUN__;
}

export async function bootstrap() {
  console.log('[vue] vue app bootstraped');
}

export async function mount(props) {
  actions.setActions(props);
  // console.log("[vue] props from main framework", props);
  render(props);
}

export async function unmount() {
  instance.$destroy();
  instance.$el.innerHTML = '';
  instance = null;
}
