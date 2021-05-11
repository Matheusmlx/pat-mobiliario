import { shallowMount } from '@vue/test-utils'
import flushPromises from 'flush-promises'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ReservaIntervaloListagemCabecalho from './ReservaIntervaloListagemCabecalho'
import {actionTypes,mutationTypes} from '@/core/constants'
import Vuex from 'vuex'

const reservaMock = {
    id: 1,
    codigo: '00002',
    situacao: 'FINALIZADO',
    preenchimento: 'AUTOMATICO',
}

describe('ReservaIntervaloListagemCabecalho', () => {
    let wrapper, router, localVue,actions,state,mutations

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        state = {
            possuiNumerosUtilizados:true
        }

        actions={
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.VERIFICAR_RESERVA_POSSUI_NUMERO_UTILIZADO]:jest.fn().mockReturnValue(false),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA]:jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.SHOW_ALERT]:jest.fn()
        }
    })

    router = {
        init: jest.fn(),
        push: jest.fn(),
        replace: jest.fn(),
        history: {
            current: {
                name: 'ReservaVisualizacao',
                params: {
                    id: 1,
                },
            },
        },
    }

    const defaulShallowMount = () => shallowMount(ReservaIntervaloListagemCabecalho, {
            router,
            localVue,
            propsData: {
                reserva: reservaMock,
            },
            store: new Vuex.Store({state,actions,mutations})
        })

    describe('Methods',() => {

        it('Caso algum numero da reserva esteja em uso, o atributo numeroReservaUtilizado deve ser false ', async () => {
            wrapper = defaulShallowMount()

            await wrapper.vm.verificarReservaPossuiNumeroUtilizado()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.VERIFICAR_RESERVA_POSSUI_NUMERO_UTILIZADO]).toHaveBeenCalled()
            expect(wrapper.vm.possuiNumerosUtilizados).toEqual(false)
        })

        it('Deve remover uma reserva', async () => {
            wrapper = defaulShallowMount()

            wrapper.vm.removerReserva()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA]).toHaveBeenCalledTimes(1)
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA].mock.calls[0][1]).toEqual(1)
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'ReservaListagem'})
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalledTimes(1)
        })
    })
})
