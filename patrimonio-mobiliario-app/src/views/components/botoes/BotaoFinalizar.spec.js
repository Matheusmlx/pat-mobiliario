import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoFinalizar from './BotaoFinalizar'
import flushPromises from 'flush-promises'

describe('BotaoFinalizar', () => {
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
        it('Deve emitir o evento de finalizar ', () => {
            const eventoFinalizar = jest.fn()

            wrapper = shallowMount(BotaoFinalizar, {
                localVue,
                store,
                listeners: {
                    finalizar: eventoFinalizar
                }
            })

            wrapper.vm.finalizar()

            expect(eventoFinalizar).toHaveBeenCalled()
        })

        it('Deve emitir o evento de finalizar e setar como false na variável loading caso o evento finalizar retorno uma exceção', async () => {
            const eventoFinalizar = jest.fn().mockRejectedValueOnce(new Error())

            wrapper = shallowMount(BotaoFinalizar, {
                localVue,
                store: new Vuex.Store({state}),
                listeners: {
                    finalizar: eventoFinalizar
                }
            })
            wrapper.vm.finalizar()
            await flushPromises()
            expect(eventoFinalizar).toHaveBeenCalled()
            expect(wrapper.vm.loading).toEqual(false)
        })
    })
})
