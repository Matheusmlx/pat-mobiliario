import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoAdicionarPatrimonio from './BotaoAdicionarPatrimonio'

describe('BotaoAdicionarPatrimonio', () => {
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
        it('Deve emitir o evento de adicionarPatrimonio ', () => {
            wrapper = shallowMount(BotaoAdicionarPatrimonio, {
                localVue,
                store
            })
            wrapper.vm.adicionarPatrimonio()
            expect(wrapper.emitted('adicionarPatrimonio')).toBeTruthy()
        })
    })
})
