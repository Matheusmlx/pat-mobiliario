import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoVerMenos from './BotaoVerMenos'

describe('BotaoVerMenos', () => {
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
        it('Deve emitir o evento de verMenos ', () => {
            wrapper = shallowMount(BotaoVerMenos, {
                localVue,
                store
            })
            wrapper.vm.verMenos()
            expect(wrapper.emitted('verMenos')).toBeTruthy()
        })
    })
})
