import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import AcoesBotoesFinalizarVoltar from './AcoesBotoesFinalizarVoltar'


describe('AcoesBotoesFinalizarVoltar', () => {
    let wrapper, state, store, localVue

    beforeEach(() => {
        state = {
            loki: {user: {domainId: 1, type: 'INTERNO'}}
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state})

        localVue = applicationTestBuilder.build()
    })
    const defaultMount = () => shallowMount(AcoesBotoesFinalizarVoltar, {
        localVue,
        store
    })

    describe('methods', () => {
        it('Deve emitir o evento de voltar ', async () => {
            wrapper = defaultMount()

            await flushPromises()
            await wrapper.vm.voltar()
            expect(wrapper.emitted('voltar')).toBeTruthy()
        })

        it('Deve emitir o evento de finalizar ', () => {
            const eventoFinalizar = jest.fn()

            wrapper = shallowMount(AcoesBotoesFinalizarVoltar, {
                localVue,
                store,
                listeners: {
                    finalizar: eventoFinalizar
                }
            })

            wrapper.vm.finalizar()
            expect(eventoFinalizar).toHaveBeenCalled()
        })
    })
})
