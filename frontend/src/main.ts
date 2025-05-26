import { createApp } from "vue"
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import {useUserStore} from "@/store/user";

import './assets/style.css'

const app = createApp(App)
app.use(createPinia())

const pinia = createPinia()
app.use(pinia)
app.use(router)

const userStore = useUserStore()

if (import.meta.env.DEV && !userStore.user) {
  // mock doctor 登录，可改成 'patient' 看不同效果
  userStore.mockLogin('doctor')
}

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus, {
  locale: zhCn,
})
app.use(router)
app.mount('#app')