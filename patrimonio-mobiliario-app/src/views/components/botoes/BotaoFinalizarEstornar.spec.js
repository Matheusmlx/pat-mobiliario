import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoFinalizarEstornar from './BotaoFinalizarEstornar'

describe('BotaoFinalizarEstornar', () => {
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
        it('Deve emitir o evento de finalizar ', () => {
            wrapper = shallowMount(BotaoFinalizarEstornar, {
                localVue,
                store
            })
            wrapper.vm.finalizar()
            expect(wrapper.emitted('finalizar')).toBeTruthy()
        })
    })
})
