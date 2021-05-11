import { shallowMount } from '@vue/test-utils'
import flushPromises from 'flush-promises'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ReservaVisualizacaoCabecalho from './ReservaVisualizacaoCabecalho'
import {actionTypes,mutationTypes} from '@/core/constants'
import Vuex from 'vuex'

const reserva = {
    id: 1,
    codigo: '00002',
    situacao: 'FINALIZADO',
    preenchimento: 'AUTOMATICO',
}

describe('ReservaVisualizacaoCabecalho', () => {
    let wrapper, router, localVue,actions,state,mutations

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        state = {
            numeroReservaUtilizado:true
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
        replace:jest.fn(),
        push: jest.fn(),
        history: {
            current: {
                name: 'ReservaVisualizacao',
                params: {
                    id: 1,
                },
            },
        },
    }

    const defaulShallowMount = () => shallowMount(ReservaVisualizacaoCabecalho, {
            router,
            localVue,
            propsData: {
                reserva: reserva,
            },
            store: new Vuex.Store({state,actions,mutations})
        })

    describe('Methods',() => {
        it('Deve redirecionar o usuario quando clicar no botão de adicionar Orgãos', async () => {
            wrapper  = defaulShallowMount()

            wrapper.vm.adicionarOrgaos(reserva)
            await flushPromises()

            expect(wrapper.props('reserva')).toEqual(reserva)
            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name:'ModalAdicionarIntervalos', params:{id:reserva.id}})
        })

        it('Caso algum numero da reserva esteja em uso, o atributo numeroReservaUtilizado deve ser false ', async () => {
            wrapper = defaulShallowMount()

            wrapper.vm.verificarReservaPossuiNumeroUtilizado(reserva)
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.VERIFICAR_RESERVA_POSSUI_NUMERO_UTILIZADO]).toHaveBeenCalled()
            expect(wrapper.vm.possuiNumerosUtilizados).toEqual(false)
        })

        it('Deve deletar uma reserva', async () => {
            wrapper = defaulShallowMount()

            wrapper.vm.removerReserva()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA].mock.calls[0][1]).toEqual(1)
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA]).toHaveBeenCalledTimes(1)
            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name:'ReservaListagem'})
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalledTimes(1)
        })
    })
})
