import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import AcoesIconesTooltipEditarSelecionar from './AcoesIconesTooltipEditarSelecionar'


describe('AcoesIconesTooltipEditarSelecionar', () => {
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
        it('Deve emitir o evento de ativar edição', async () => {
            wrapper = shallowMount(AcoesIconesTooltipEditarSelecionar, {
                localVue,
                store
            })

            await flushPromises()
            await wrapper.vm.ativarEdicao()
            expect(wrapper.emitted('ativarEdicao')).toBeTruthy()
        })

        it('Deve emitir o evento de selecionar ', () => {
            wrapper = shallowMount(AcoesIconesTooltipEditarSelecionar, {
                localVue,
                store
            })
            wrapper.vm.selecionar()
            expect(wrapper.emitted('selecionar')).toBeTruthy()
        })

    })
})
