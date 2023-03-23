import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store, { key } from './store';
import vant from './components/Vant';
import './style/index.scss';

const app = createApp(App);

/** 引入 */
app.use(vant);
app.use(store, key);
app.use(router);

/** 挂载实例 */
app.mount('#app');
