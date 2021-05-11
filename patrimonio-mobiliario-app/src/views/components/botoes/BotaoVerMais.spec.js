import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoVerMais from './BotaoVerMais'

describe('BotaoVerMais', () => {
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
        it('Deve emitir o evento de verMais ', () => {
            wrapper = shallowMount(BotaoVerMais, {
                localVue,
                store
            })
            wrapper.vm.verMais()
            expect(wrapper.emitted('verMais')).toBeTruthy()
        })
    })
})
