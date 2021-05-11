import Vue from 'vue'
import loki from '@azinformatica/loki'
import store from '@/core/store'
import router from '@/views/routers'

Vue.use(loki, {store, router})
