import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoDevolverParcial from './BotaoDevolverParcial'

describe('BotaoDevolverParcial', () => {
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
        it('Deve emitir o evento de devolver parcial ', () => {
            wrapper = shallowMount(BotaoDevolverParcial, {
                localVue,
                store
            })
            wrapper.vm.devolverParcial()
            expect(wrapper.emitted('devolverParcial')).toBeTruthy()
        })
    })
})
