import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import {actionTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ReservaDadosGeraisCadastro from './ReservaDadosGeraisCadastro'
import flushPromises from 'flush-promises'

describe('ReservaDadosGeraisCadastro', () => {

    let wrapper, actions, store, localVue, state, router, errors, $validator

    const reserva = {
        id: 1
    }

    const proximoNumeroDisponivel = {
        proximoNumero: 3
    }

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
            history: {current: {name: 'ReservaCadastro'}}
        }

        actions = {
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.SALVAR_RESERVA]: jest.fn().mockReturnValue(reserva),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_PROXIMO_NUMERO_DISPONIVEL]: jest.fn().mockReturnValue(proximoNumeroDisponivel)
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, actions})
    })

    describe('methods', () => {
        it('deve salvar a reserva e direcionar para a página de edição da reserva', async () => {
            const reserva = {
                quantidadeReservada: '',
                preenchimento: 'AUTOMATICO',
                numeroInicio: 3,
                numeroFim: null
            }
            wrapper = shallowMount(ReservaDadosGeraisCadastro, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            await flushPromises()
            await wrapper.vm.salvar()

            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.SALVAR_RESERVA]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.SALVAR_RESERVA].mock.calls[0][1]).toEqual(reserva)
            expect(router.push.mock.calls[0][0]).toEqual({name: 'ReservaEdicao', 'params': {id: 1}})
        })

        it('deve cancelar e voltar para a listagem de reservas', async () => {
            wrapper = shallowMount(ReservaDadosGeraisCadastro, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })

            wrapper.vm.cancelar()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'ReservaListagem'})
        })

        it('deve resetar a quantidade da reserva', () => {
            wrapper = shallowMount(ReservaDadosGeraisCadastro, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })

            wrapper.setData({
                reserva: {
                    quantidadeReservada: 3
                }
            })

            wrapper.vm.resetarQuantidade()

            expect(wrapper.vm.reserva.quantidadeReservada).toBe('')
        })

        it('deve calcular a quantidade quando intervalo manual e numero de inicio e fim estiverem preenchidos', () => {
            wrapper = shallowMount(ReservaDadosGeraisCadastro, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })

            wrapper.setData({
                reserva: {
                    preenchimento: 'MANUAL',
                    quantidadeReservada: '',
                    numeroInicio: 11,
                    numeroFim: 15
                }
            })

            wrapper.vm.calcularQuantidadeIntervaloManual()

            expect(wrapper.vm.reserva.quantidadeReservada).toEqual(5)
        })

        it('deve deixar quantidade em branco quando intervalo manual e numero fim menor que numero inicio', () => {
            wrapper = shallowMount(ReservaDadosGeraisCadastro, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })

            wrapper.setData({
                reserva: {
                    preenchimento: 'MANUAL',
                    quantidadeReservada: '',
                    numeroInicio: 11,
                    numeroFim: 8
                }
            })

            wrapper.vm.calcularQuantidadeIntervaloManual()

            expect(wrapper.vm.reserva.quantidadeReservada).toEqual('')
        })

        it('deve deixar quantidade em branco quando intervalo manual e numero inicio preenchido e numero fim não preenchido', () => {
            wrapper = shallowMount(ReservaDadosGeraisCadastro, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })

            wrapper.setData({
                reserva: {
                    preenchimento: 'MANUAL',
                    quantidadeReservada: '',
                    numeroInicio: 11,
                    numeroFim: ''
                }
            })

            wrapper.vm.calcularQuantidadeIntervaloManual()

            expect(wrapper.vm.reserva.quantidadeReservada).toEqual('')
        })

        it('deve deixar quantidade em branco quando intervalo manual e numero inicio não preenchido e numero fim preenchido', () => {
            wrapper = shallowMount(ReservaDadosGeraisCadastro, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })

            wrapper.setData({
                reserva: {
                    preenchimento: 'MANUAL',
                    quantidadeReservada: '',
                    numeroInicio: '',
                    numeroFim: 15
                }
            })

            wrapper.vm.calcularQuantidadeIntervaloManual()

            expect(wrapper.vm.reserva.quantidadeReservada).toEqual('')
        })

    })
})
