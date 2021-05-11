import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalEstornarPatrimonios from './ModalEstornarPatrimonios'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('ModalEstornarPatrimonios', () => {

    let localVue, router, vuetify, state

    const defaultMount = () => shallowMount(ModalEstornarPatrimonios, {
        vuetify,
        localVue,
        router,
        store: new Vuex.Store({state})
    })

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {
                    name: 'ModalEstornarPatrimoniosListagem',
                    params: {incorporacaoId: 7}
                }
            }
        }
        state = {
            patrimonio:{
                estorno: {
                    motivo: false
                }
            }
        }
    })

    describe('Methods', () => {
        it('Deve redirecionar para o modal de motivo caso ocorra uma atualização na página', () => {
            defaultMount()
            flushPromises()
            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name:'ModalEstornarPatrimoniosMotivo'})
        })
    })
})
