import {createLocalVue} from '@vue/test-utils'
import Vue from 'vue'
import Vuex from 'vuex'
import Vuetify from 'vuetify'
import VueRouter from 'vue-router'
import VueTheMask from 'vue-the-mask'
import loki from '@azinformatica/loki'
import moment from 'moment-timezone'
import {formatosDefault} from '@/core/constants'
import mixins from '@/views/mixins'

Vue.config.silent = true
Vue.config.productionTip = false

class ApplicationTestBuilder {

    constructor() {
        this.store = {}
        this.router = {}
        this.vuetify = {}
        this.i18n = {}
    }

    build(store, router,i18n) {
        this._addElemWithDataAppToBody()
        this._stubRequestAnimationFrame()

        return this._createVueTestInstance(store, router,i18n)
    }

    getStore() {
        return this.store
    }

    getRouter() {
        return this.store
    }

    getVuetify() {
        return this.vuetify
    }

    _addElemWithDataAppToBody() {
        const app = document.createElement('div')
        app.setAttribute('data-app', true)
        document.body.append(app)
    }

    _createLocalVueStore(store) {

        if (store) {
            this.store = new Vuex.Store(store)
        }
        this.store = new Vuex.Store({})
    }

    _createLocalVueRouter(router) {
        if (router) {
            this.router = router
        }
        this.router = new VueRouter({ routes: [] })
    }

    _createLocalVueI18n(i18n){
        if(i18n){
            this.i18n = i18n
        }
    }

    _createLocalVuetify(vuetify) {
        if (vuetify) {
            this.vuetify = vuetify
        }
        this.vuetify = new Vuetify({})
    }

    _createVueTestInstance(store, router, vuetify,i18n) {
        const localVue = createLocalVue()
        localVue.use(Vuex)

        this._createLocalVueStore(store)
        this._createLocalVueRouter(router)
        this._createLocalVuetify(vuetify)
        this._createLocalVueI18n(i18n)

        Vue.use(Vuetify)
        localVue.use(loki, { store: this.store, router: this.router })
        localVue.use(VueTheMask)
        localVue.use(Vuetify)
        localVue.use(mixins)
        localVue.use(VueRouter)

        this._mockDirectives(localVue)
        this._mockFilters(localVue)

        return localVue
    }

    _mockDirectives(localVue) {
        localVue.directive('az-auth', {
            inserted(el) {
                el.style.visibility = 'visible'
            }
        })

        localVue.directive('validate', {
            inserted() {
                return true
            }
        })
    }

    _mockFilters(localVue) {
        localVue.filter('az-date', (date, format) => {
            let dateFormat = formatosDefault.DATA
            if (format) {
                dateFormat = format
            }
            if (date) {
                return moment(date).tz('America/Sao_Paulo').format(dateFormat)
            }
            return '-'
        })
    }

    _stubRequestAnimationFrame() {
        global.requestAnimationFrame = cb => cb()
        window.requestAnimationFrame = cb => cb()
    }

}

export default new ApplicationTestBuilder()
