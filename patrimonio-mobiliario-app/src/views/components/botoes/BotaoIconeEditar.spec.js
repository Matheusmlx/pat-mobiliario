import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoIconeEditar from './BotaoIconeEditar'

describe('BotaoIconeEditar', () => {
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
        it('Deve emitir o evento de ativar edição ', () => {
            wrapper = shallowMount(BotaoIconeEditar, {
                localVue,
                store
            })
            wrapper.vm.ativarEdicao()
            expect(wrapper.emitted('ativarEdicao')).toBeTruthy()
        })
    })
})
