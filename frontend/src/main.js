import { createApp } from 'vue'
import App from './App.vue'
import axios from "axios";
import VueAxios from 'vue-axios'
import router from './router'
import './main.css'
import store from "@/vuex/store";

const app = createApp(App)
app.config.globalProperties.store = store
app.use(router)
    .use(VueAxios, axios).provide('axios', app.config.globalProperties.axios)
    .use(store)
    .mount('#app')
