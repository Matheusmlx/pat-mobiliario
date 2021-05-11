import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoVoltar from './BotaoVoltar'

describe('BotaoVoltar', () => {
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
        it('Deve emitir o evento de voltar ', () => {
            wrapper = shallowMount(BotaoVoltar, {
                localVue,
                store
            })
            wrapper.vm.voltar()
            expect(wrapper.emitted('voltar')).toBeTruthy()
        })
    })
})
