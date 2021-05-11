import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import AcoesBotoesContinuarCancelar from './AcoesBotoesContinuarCancelar'


describe('AcoesBotoesContinuarCancelar', () => {
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
        it('Deve emitir o evento de cancelar ', async () => {
            wrapper = shallowMount(AcoesBotoesContinuarCancelar, {
                localVue,
                store
            })

            await flushPromises()
            await wrapper.vm.cancelar()
            expect(wrapper.emitted('cancelar')).toBeTruthy()
        })

        it('Deve emitir o evento de continuar ', () => {
            wrapper = shallowMount(AcoesBotoesContinuarCancelar, {
                localVue,
                store
            })
            wrapper.vm.continuar()
            expect(wrapper.emitted('continuar')).toBeTruthy()
        })

    })
})
