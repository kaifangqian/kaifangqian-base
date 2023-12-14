import './assets/main.css'
import 'ant-design-vue/dist/antd.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import CScrollbar from '@/components/scrollbar'

const app = createApp(App)

app.use(router)
app.use(CScrollbar);

app.mount('#app')
