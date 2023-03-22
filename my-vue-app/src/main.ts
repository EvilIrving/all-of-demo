// main.ts
import { createApp } from 'vue'
import App from './App.vue'
// 在mian.ts中引入样式
import 'normalize.css/normalize.css'
import 'nprogress/nprogress.css'
// 引入动画库
import "animate.css"

import router from './router'
import ElementPlus from './plugins/element-plus'

const app = createApp(App)

app.use(router)
    .use(ElementPlus)
    .mount('#app')