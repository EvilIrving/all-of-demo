import ElementUI from 'element-ui';
import { UTable, UTableColumn } from 'umy-ui';
import VueAwesomeSwiper from 'vue-awesome-swiper';
import VCharts from '@linjhon/v-charts';
import VueViewer from 'v-viewer'
import { Slider, Tooltip, Timeline } from 'ant-design-vue';
// import VideoPlayer from 'vue-video-player';
import VueCompositionAPI from '@vue/composition-api';

// 原型链挂载
import Vue from 'vue';
import moment from 'moment';
import config from '@/config/config';
import { localData, sessionData } from './storage';

// 全量引入的样式
import '@/style/element.scss';
import 'ant-design-vue/lib/slider/style/index.css';
import 'ant-design-vue/lib/tooltip/style/index.css';
import 'ant-design-vue/lib/timeline/style/index.css';
import 'swiper/swiper-bundle.css';
import '@/style/index.scss';
import '@/style/sprite.less';
import 'viewerjs/dist/viewer.css'

// 需要引入的js
// import 'video.js/dist/video-js.css'; //videoJs的样式
// import 'videojs-flash'; //引入才能播放rtmp视屏

// 工具
import '@/utils/prototype';
import '@/utils/utils';
import '@/utils/directives';
import '@/utils/filter';
import '@/utils/rem';

const EventBus = new Vue();

/**
 * 一些插件集体引入安装
 */
export default {
  install: function(Vue) {
    // 原型链挂载
    Vue.prototype.$EventBus = EventBus;
    Vue.prototype.$moment = moment;
    Vue.prototype.$config = config;
    Vue.prototype.$localData = localData;
    Vue.prototype.$sessionData = sessionData;
    // use 安装
    Vue.use(VueCompositionAPI);
    // Vue.use(VideoPlayer);
    Vue.use(Slider).use(Tooltip).use(Timeline);
    Vue.use(ElementUI);
    Vue.use(UTable);
    Vue.use(UTableColumn);
    Vue.use(VueAwesomeSwiper);
    Vue.use(VCharts);
    Vue.use(VueViewer)
  }
};
