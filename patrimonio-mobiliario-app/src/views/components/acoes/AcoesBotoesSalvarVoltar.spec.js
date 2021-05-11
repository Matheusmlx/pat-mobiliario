import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import AcoesBotoesSalvarVoltar from './AcoesBotoesSalvarVoltar'


describe('AcoesBotoesSalvarVoltar', () => {
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
            wrapper = shallowMount(AcoesBotoesSalvarVoltar, {
                localVue,
                store
            })

            await flushPromises()
            await wrapper.vm.voltar()
            expect(wrapper.emitted('voltar')).toBeTruthy()
        })

        it('Deve emitir o evento de salvar ', () => {
            wrapper = shallowMount(AcoesBotoesSalvarVoltar, {
                localVue,
                store
            })
            wrapper.vm.salvar()
            expect(wrapper.emitted('salvar')).toBeTruthy()
        })

    })
})
