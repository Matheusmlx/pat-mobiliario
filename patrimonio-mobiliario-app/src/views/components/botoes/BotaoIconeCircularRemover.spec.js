import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoIconeCircularRemover from './BotaoIconeCircularRemover'

describe('BotaoIconeCircularRemover', () => {
    let wrapper, state, store, localVue

    beforeEach(() => {
        state = {
            loki: {
                user: {domainId: 1, type: 'INTERNO'}
            }
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state})

        localVue = applicationTestBuilder.build()
    })

    describe('methods', () => {
        it('Deve emitir o evento de remover ', () => {
            wrapper = shallowMount(BotaoIconeCircularRemover, {
                localVue,
                store
            })
            wrapper.vm.remover()
            expect(wrapper.emitted('remover')).toBeTruthy()
        })
    })
})
