import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoSalvar from './BotaoSalvar'

describe('BotaoSalvar', () => {
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
        it('Deve emitir o evento de salvar ', () => {
            wrapper = shallowMount(BotaoSalvar, {
                localVue,
                store
            })
            wrapper.vm.salvar()
            expect(wrapper.emitted('salvar')).toBeTruthy()
        })
    })
})
