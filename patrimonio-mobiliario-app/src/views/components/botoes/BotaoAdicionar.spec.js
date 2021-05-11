import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoAdicionar from './BotaoAdicionar'

describe('BotaoAdicionar', () => {
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
        it('Deve emitir o evento de adicionar ', () => {
            wrapper = shallowMount(BotaoAdicionar, {
                localVue,
                store
            })
            wrapper.vm.adicionar()
            expect(wrapper.emitted('adicionar')).toBeTruthy()
        })
    })
})
