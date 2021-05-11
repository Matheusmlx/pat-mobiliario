import Vue from 'vue'
import '@babel/polyfill'
import '@/plugins'
import mixins from '@/views/mixins'
import filters from '@/views/filters'

class Register {

    registerAll() {
        Vue.use(mixins)
        Vue.use(filters)
    }

}

export default new Register()
