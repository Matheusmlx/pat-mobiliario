import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import AcoesBotoesFinalizarVoltarAdicionar from './AcoesBotoesFinalizarVoltarAdicionar'


describe('AcoesBotoesFinalizarVoltarAdicionar', () => {
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
        it('Deve emitir o evento de voltar ', async () => {
            wrapper = shallowMount(AcoesBotoesFinalizarVoltarAdicionar, {
                localVue,
                store
            })

            await flushPromises()
            await wrapper.vm.voltar()
            expect(wrapper.emitted('voltar')).toBeTruthy()
        })

        it('Deve emitir o evento de finalizar ', () => {
            wrapper = shallowMount(AcoesBotoesFinalizarVoltarAdicionar, {
                localVue,
                store
            })
            wrapper.vm.finalizar()
            expect(wrapper.emitted('finalizar')).toBeTruthy()
        })

        it('Deve emitir o evento de adicionar outro ', () => {
            wrapper = shallowMount(AcoesBotoesFinalizarVoltarAdicionar, {
                localVue,
                store
            })
            wrapper.vm.adicionarOutro()
            expect(wrapper.emitted('adicionarOutro')).toBeTruthy()
        })
    })
})
