import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ReservaDadosGeraisEdicao from './ReservaDadosGeraisEdicao'
import flushPromises from 'flush-promises'

describe('ReservaDadosGeraisEdicao', () => {

    let wrapper, actions, mutations, store, localVue, state, router, errors, $validator

    const reserva = {
        id: 1,
        quantidadeReservada: '10',
        preenchimento: 'AUTOMATICO',
        numeroInicio: 1,
        numeroFim: 19
    }

    const proximoNumeroDisponivel = 20

    beforeEach(async () => {
        localVue = applicationTestBuilder.build()

        errors = {
            collect: jest.fn(),
            clear: jest.fn()
        }

        state = {
            parametros: {
                parametros: {
                    quantidadeDigitosNumeroPatrimonio: 10
                }
            }
        }

        $validator = {
            validateAll: jest.fn().mockReturnValue(true),
            errors: {
                clear: jest.fn()
            }
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {current: {name: 'ReservaEdicao', params: {id: 1}}}
        }

        actions = {
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.EDITAR_RESERVA]: jest.fn().mockReturnValue(reserva),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.VERIFICAR_RESERVA_POSSUI_NUMERO_UTILIZADO]:jest.fn().mockReturnValue(false),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_PROXIMO_NUMERO_DISPONIVEL]: jest.fn().mockReturnValue(proximoNumeroDisponivel),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA]:jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, actions, mutations})
    })

    describe('methods', () => {
        it('deve editar a reserva', async () => {
            const reserva = {
                id: 1,
                quantidadeReservada: 10,
                preenchimento: 'AUTOMATICO',
                numeroInicio: 1,
                numeroFim: 19
            }
            wrapper = shallowMount(ReservaDadosGeraisEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            wrapper.setProps({
                reserva
            })

            await flushPromises()
            expect(wrapper.vm.reservaEdicao).toEqual(reserva)

            await wrapper.vm.editar()

            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.EDITAR_RESERVA]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.EDITAR_RESERVA].mock.calls[0][1]).toEqual(reserva)
            expect(wrapper.emitted().recarregarDadosReserva).toBeTruthy()
        })

        it('deve resetar os dados da reserva quando trocar o tipo de preenchimento', async () => {
            const reserva = {
                id: 1,
                quantidadeReservada: 10,
                preenchimento: 'AUTOMATICO',
                numeroInicio: 1,
                numeroFim: 19
            }
            const reservaAlterada = {
                ...reserva,
                numeroInicio: 10,
                numeroFim: 37,
                quantidadeReservada: 38
            }
            wrapper = shallowMount(ReservaDadosGeraisEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            wrapper.setProps({
                reserva
            })
            await flushPromises()

            wrapper.setData({
                reservaEdicao: reservaAlterada
            })
            expect(wrapper.vm.reservaEdicao).toEqual(reservaAlterada)

            wrapper.vm.resetarReserva()
            expect(wrapper.vm.reservaEdicao).toEqual(reserva)
        })

        it('deve calcular a quantidade reservada quando preenchimento manual e numero inicio e fim estiverem preenchidos', async () => {
            const reserva = {
                id: 1,
                quantidadeReservada: 20,
                preenchimento: 'MANUAL',
                numeroInicio: 1,
                numeroFim: 19
            }

            const reservaAlterada = {
                id: 1,
                quantidadeReservada: '',
                preenchimento: 'MANUAL',
                numeroInicio: 20,
                numeroFim: 59
            }

            wrapper = shallowMount(ReservaDadosGeraisEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            wrapper.setProps({
                reserva
            })
            await flushPromises()
            expect(wrapper.vm.reservaEdicao.quantidadeReservada).toEqual(20)

            wrapper.setData({
                reservaEdicao: reservaAlterada
            })
            expect(wrapper.vm.reservaEdicao).toEqual(reservaAlterada)

            wrapper.vm.calcularQuantidadeIntervaloManual()
            expect(wrapper.vm.reservaEdicao.quantidadeReservada).toEqual(40)
        })

        it('deve deixar quantidade em branco quando intervalo manual e numero fim menor que numero inicio', async () => {
            const reserva = {
                id: 1,
                quantidadeReservada: 20,
                preenchimento: 'MANUAL',
                numeroInicio: 1,
                numeroFim: 19
            }

            const reservaAlterada = {
                id: 1,
                quantidadeReservada: '',
                preenchimento: 'MANUAL',
                numeroInicio: 20,
                numeroFim: 19
            }

            wrapper = shallowMount(ReservaDadosGeraisEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            wrapper.setProps({
                reserva
            })
            await flushPromises()
            expect(wrapper.vm.reservaEdicao.quantidadeReservada).toEqual(20)

            wrapper.setData({
                reservaEdicao: reservaAlterada
            })
            expect(wrapper.vm.reservaEdicao).toEqual(reservaAlterada)

            wrapper.vm.calcularQuantidadeIntervaloManual()
            expect(wrapper.vm.reservaEdicao.quantidadeReservada).toEqual('')
        })

        it('deve deixar quantidade em branco quando intervalo manual e numero inicio preenchido e numero fim não preenchido', async () => {
            const reserva = {
                id: 1,
                quantidadeReservada: 20,
                preenchimento: 'MANUAL',
                numeroInicio: 1,
                numeroFim: 19
            }

            const reservaAlterada = {
                id: 1,
                quantidadeReservada: '',
                preenchimento: 'MANUAL',
                numeroInicio: 20,
                numeroFim: null
            }

            wrapper = shallowMount(ReservaDadosGeraisEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            wrapper.setProps({
                reserva
            })
            await flushPromises()
            expect(wrapper.vm.reservaEdicao.quantidadeReservada).toEqual(20)

            wrapper.setData({
                reservaEdicao: reservaAlterada
            })
            expect(wrapper.vm.reservaEdicao).toEqual(reservaAlterada)

            wrapper.vm.calcularQuantidadeIntervaloManual()
            expect(wrapper.vm.reservaEdicao.quantidadeReservada).toEqual('')
        })

        it('deve deixar quantidade em branco quando intervalo manual e numero inicio não preenchido e numero fim preenchido', async () => {
            const reserva = {
                id: 1,
                quantidadeReservada: 20,
                preenchimento: 'MANUAL',
                numeroInicio: 1,
                numeroFim: 19
            }

            const reservaAlterada = {
                id: 1,
                quantidadeReservada: '',
                preenchimento: 'MANUAL',
                numeroInicio: null,
                numeroFim: 19
            }

            wrapper = shallowMount(ReservaDadosGeraisEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            wrapper.setProps({
                reserva
            })
            await flushPromises()
            expect(wrapper.vm.reservaEdicao.quantidadeReservada).toEqual(20)

            wrapper.setData({
                reservaEdicao: reservaAlterada
            })
            expect(wrapper.vm.reservaEdicao).toEqual(reservaAlterada)

            wrapper.vm.calcularQuantidadeIntervaloManual()
            expect(wrapper.vm.reservaEdicao.quantidadeReservada).toEqual('')
        })

        it('Deve remover uma reserva', async () => {
            wrapper = shallowMount(ReservaDadosGeraisEdicao, {
                store,
                router,
                localVue,
                propsData:{
                    reserva:reserva
                },
                mocks:{$validator, errors}
            })

            await wrapper.vm.removerReserva()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA]).toHaveBeenCalledTimes(1)
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA].mock.calls[0][1]).toEqual(1)
            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name:'ReservaListagem'})
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalledTimes(1)
        })

    })
})
