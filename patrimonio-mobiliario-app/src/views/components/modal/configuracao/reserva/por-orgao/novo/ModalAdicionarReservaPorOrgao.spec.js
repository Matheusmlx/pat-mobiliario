import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalAdicionarReservaPorOrgao from './ModalAdicionarReservaPorOrgao'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('ModalAdicionarReservaPorOrgao', () => {

    let wrapper, store, localVue, router, actions, state, mutations, $validator

    const orgaos = {
        items: [
            {
                id: 1,
                descricao: 'descricao',
                nome: 'orgao',
                sigla: 'sigla'
            }
        ],
        totalPages: 1,
        totalElements: 1,
        totalElementsOfAllPages: 1
    }

    $validator = {
        validateAll: jest.fn().mockReturnValue(true),
        reset: jest.fn(),
        resume: jest.fn(),
        pause: jest.fn()
    }

    const errors = {
        collect() {
        },
        clear: jest.fn()
    }

    const intervalo = {numeroInicio: 101, numeroFim: 110}

    const proximoNumero = {proximoNumero: 11}

    const reserva = {
        id: 1,
        orgaoId: 1,
        preenchimento: 'AUTOMATICO',
        numeroInicio: 1,
        numeroFim: 10,
        quantidadeReservada: 50,
        quantidadeRestante: 30
    }

    beforeEach(() => {

        state = {}

        router = {
            routes: [],
            push: jest.fn(),
            replace: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'ReservaCadastro'
                }
            }
        }

        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
            [actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_INTERVALO_POR_ORGAO]: jest.fn().mockReturnValue(intervalo),
            [actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_PROXIMO_NUMERO_DISPONIVEL_POR_ORGAO]: jest.fn().mockReturnValue(proximoNumero),
            [actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.FINALIZAR_RESERVA_POR_ORGAO]: jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
        }

        store = new Vuex.Store({actions, mutations, state})
    })

    const defaultMount = () => shallowMount(ModalAdicionarReservaPorOrgao, {
        store,
        router,
        localVue,
        mocks: {errors, $validator},
    })

    describe('Mounted', () => {
        it('Deve buscar todos órgãos', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]).toHaveBeenCalled()
        })
    })

    describe('methods', () => {

        it('Deve selecionar o órgão se array de órgão conter apenas um',
          async () => {
              wrapper = defaultMount()
              await flushPromises()

              wrapper.vm.selecionaOrgaoSeArrayConterApenasUmOrgao(orgaos)

              expect(wrapper.vm.reserva.orgaoId).toEqual(orgaos.items[0].id)
          })

        it('Deve buscar próximo número disponível', async () => {
            wrapper = defaultMount()
            await flushPromises()

            const resetarReserva = jest.spyOn(wrapper.vm, 'resetarReserva')
            wrapper.vm.buscarProximoNumeroDisponivel(1)
            await flushPromises()

            expect(
              actions[actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_PROXIMO_NUMERO_DISPONIVEL_POR_ORGAO]).
              toHaveBeenCalled()
            expect(
              actions[actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_PROXIMO_NUMERO_DISPONIVEL_POR_ORGAO].mock.calls[0][1]).
              toEqual(1)

            expect(resetarReserva).toHaveBeenCalled()
            expect(wrapper.vm.proximoNumeroDisponivel).
              toEqual(proximoNumero.proximoNumero)
            expect(wrapper.vm.reserva.numeroInicio).
              toEqual(proximoNumero.proximoNumero)
        })

        it('Deve resetar a reserva', () => {
            wrapper = defaultMount()

            wrapper.vm.reserva = reserva
            wrapper.vm.resetarReserva()

            expect(wrapper.vm.reserva).toEqual({
                id: 1,
                orgaoId: 1,
                preenchimento: 'AUTOMATICO',
                numeroInicio: null,
                numeroFim: null,
                quantidadeReservada: null,
                quantidadeRestante: 30
            })
        })

        it('Deve buscar intervalo', async () => {
            wrapper = defaultMount()
            await flushPromises()

            const buscarIntervaloEntidade = {
                orgaoId: 1,
                quantidadeReservada: 10,
            }

            wrapper.vm.buscarIntervalo(buscarIntervaloEntidade)
            await flushPromises()

            expect(
              actions[actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_INTERVALO_POR_ORGAO]).
              toHaveBeenCalled()
            expect(
              actions[actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_INTERVALO_POR_ORGAO].mock.calls[0][1]).
              toEqual(buscarIntervaloEntidade)
            expect(wrapper.vm.reserva.numeroInicio).
              toEqual(intervalo.numeroInicio)
            expect(wrapper.vm.reserva.numeroFim).toEqual(intervalo.numeroFim)
        })

        it('Deve finalizar listagem', async () => {
            wrapper = defaultMount()
            await flushPromises()

            const mostrarNotificacaoSucessoDefault = jest.spyOn(wrapper.vm, 'mostrarNotificacaoSucessoDefault')
            const atualizarListagem = jest.spyOn(wrapper.vm, 'atualizarListagem')
            const fecharModal = jest.spyOn(wrapper.vm, 'fecharModal')

            wrapper.vm.reserva = reserva
            wrapper.vm.finalizarReserva()
            await flushPromises()

            expect(
              actions[actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.FINALIZAR_RESERVA_POR_ORGAO]).
              toHaveBeenCalled()
            expect(
              actions[actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.FINALIZAR_RESERVA_POR_ORGAO].mock.calls[0][1]).
              toEqual(reserva)

            expect(mostrarNotificacaoSucessoDefault).toHaveBeenCalled()
            expect(atualizarListagem).toHaveBeenCalled()
            expect(fecharModal).toHaveBeenCalled()
        })

        it('Deve fechar o modal', () => {
            wrapper = defaultMount()
            wrapper.vm.fecharModal()
            expect(router.push.mock.calls[0][0]).
              toEqual({ name: 'ReservaListagem' })
        })

        it('Deve atualizar listagem', () => {
            wrapper = defaultMount()
            wrapper.vm.atualizarListagem()
            expect(wrapper.emitted().atualizarListagem).toBeTruthy()
        })

        it('Deve liberar botão de finalizar', () => {
            wrapper = defaultMount()
            wrapper.vm.podeFinalizar = false
            wrapper.vm.liberarBotaoFinalizar()
            expect(wrapper.vm.podeFinalizar).toBeTruthy()
        })

        it('Deve bloquear botão de finalizar', () => {
            wrapper = defaultMount()
            wrapper.vm.podeFinalizar = true
            wrapper.vm.bloquearBotaoFinalizar()
            expect(wrapper.vm.podeFinalizar).toBeFalsy()
        })

    })

})
