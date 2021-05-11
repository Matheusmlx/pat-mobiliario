import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoContinuar from './BotaoContinuar'

describe('BotaoContinuar', () => {
    let wrapper, state, store, localVue

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO'
                }
            }
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state})

        localVue = applicationTestBuilder.build()
    })

    describe('methods', () => {
        it('Deve emitir o evento de continuar ', () => {
            wrapper = shallowMount(BotaoContinuar, {
                localVue,
                store
            })
            wrapper.vm.continuar()
            expect(wrapper.emitted('continuar')).toBeTruthy()
        })
    })
})
