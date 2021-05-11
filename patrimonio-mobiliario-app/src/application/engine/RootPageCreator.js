import Vue from 'vue'
import {buildMenu} from '@azinformatica/loki'
import RootPage from '@/views/pages/RootPage.vue'
import router from '@/views/routers'
import store from '@/core/store'
import exceptionHandler from '@/core/exceptions/ExceptionHandler'
import {mutationTypes} from '@/core/constants'
import vuetify from '@/plugins/vuetify'

class RootPageCreator {
    createInstance() {
        new Vue({
            router,
            store,
            vuetify,
            render: h => h(RootPage),
            created() {
                this.$store.commit(mutationTypes.LOKI.SET_MENU_ACTIONS, buildMenu(store, router))
            },
            errorCaptured(error) {
                exceptionHandler.execute(error)
                return false
            }
        }).$mount('#app')
    }

    createBootstrapError() {
        new Vue({
            el: '#app',
            render(createElement) {
                return createElement('div', {
                    'class': 'erro-inicializacao'
                }, [
                    createElement('div', {}, [
                        createElement('i', {
                            'class': 'far fa-surprise'
                        }),
                        createElement('p', 'Ocorreu um erro na inicialização desta aplicação.')
                    ])
                ])
            }
        })
    }
}

export default new RootPageCreator()
