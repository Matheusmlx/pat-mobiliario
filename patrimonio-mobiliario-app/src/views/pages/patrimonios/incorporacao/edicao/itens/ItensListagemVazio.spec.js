import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ItensListagemVazio from './ItensListagemVazio'

describe('ItensListagemVazio', () => {
    let wrapper, state, store, localVue

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                }
            }
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state})

        localVue = applicationTestBuilder.build()
    })

    describe('methods', () => {

        it('Deve emitir o evento de adicionar um novo item de incorporação', async () =>{
            wrapper = shallowMount(ItensListagemVazio, {
                localVue,
                store
            })
            await flushPromises()
            await wrapper.vm.adicionarItem()

            expect(wrapper.emitted('adicionarItem')).toBeTruthy()
        })

    })
})
