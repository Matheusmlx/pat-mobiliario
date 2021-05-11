import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import AcoesBotoesSalvarCancelar from './AcoesBotoesSalvarCancelar'


describe('AcoesBotoesSalvarCancelar', () => {
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
        it('Deve emitir o evento de cancelar ', () => {
            wrapper = shallowMount(AcoesBotoesSalvarCancelar, {
                localVue,
                store
            })
            wrapper.vm.cancelar()
            expect(wrapper.emitted('cancelar')).toBeTruthy()
        })

        it('Deve emitir o evento de salvar ', () => {
            wrapper = shallowMount(AcoesBotoesSalvarCancelar, {
                localVue,
                store
            })
            wrapper.vm.salvar()
            expect(wrapper.emitted('salvar')).toBeTruthy()
        })

    })
})
