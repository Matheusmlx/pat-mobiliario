import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoAdicionarOutro from './BotaoAdicionarOutro'

describe('BotaoAdicionarOutro', () => {
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
        it('Deve emitir o evento de adicionar outro ', () => {
            wrapper = shallowMount(BotaoAdicionarOutro, {
                localVue,
                store
            })
            wrapper.vm.adicionarOutro()
            expect(wrapper.emitted('adicionarOutro')).toBeTruthy()
        })
    })
})
