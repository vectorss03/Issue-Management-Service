import { createApp } from 'vue'
import App from './App.vue'
import axios from "axios";
import VueAxios from 'vue-axios'
import router from './router'
import './main.css'

const app = createApp(App)
app.use(router).use(VueAxios, axios).provide('axios', app.config.globalProperties.axios).mount('#app')
