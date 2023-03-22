// src/plugins/element-plus.ts
import { App } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import locale from 'element-plus/es/locale/lang/zh-cn'
import * as Icons from '@element-plus/icons-vue'

export default {
    install(app: App) {
        app.use(ElementPlus, {
            locale,
            size: 'small' || 'default' || 'large'
        })
        Object.keys(Icons).forEach((key: string) => {
            app.component(key, Icons[key as keyof typeof Icons]);
        })
    }
}