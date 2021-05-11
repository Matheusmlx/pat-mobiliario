import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import AcoesBotoesContinuarVoltar from './AcoesBotoesContinuarVoltar'


describe('AcoesBotoesContinuarVoltar', () => {
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
            wrapper = shallowMount(AcoesBotoesContinuarVoltar, {
                localVue,
                store
            })

            await flushPromises()
            await wrapper.vm.voltar()
            expect(wrapper.emitted('voltar')).toBeTruthy()
        })

        it('Deve emitir o evento de continuar ', () => {
            wrapper = shallowMount(AcoesBotoesContinuarVoltar, {
                localVue,
                store
            })
            wrapper.vm.continuar()
            expect(wrapper.emitted('continuar')).toBeTruthy()
        })

    })
})
