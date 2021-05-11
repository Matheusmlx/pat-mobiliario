import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalAdicionarIntervalosForm from './ModalAdicionarIntervalosForm'
import flushPromises from 'flush-promises'
import { actionTypes } from '@/core/constants'

describe('ModalAdicionarIntervalosForm', () => {

    let wrapper, store, localVue, router, state, $validator, actions

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

    const reservaIntervalo = {
        reservaId: 1,
        orgaoId: 1,
        descricao: 'descricao',
        quantidadeReservada: null,
        numeroInicio: '',
        numeroFim: '',
        preenchimento: 'AUTOMATICO',
        selecionado: false,
    }

    beforeEach(() => {

        router = {
            routes: [],
            push: jest.fn(),
            replace: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'ModalAdicionarIntervalos',
                    params: {id: 1}
                }
            }
        }

        state = {
            parametros: {
                parametros: {
                    quantidadeDigitosNumeroPatrimonio: 10
                }
            }
        }

        actions = {
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_PROXIMO_NUMERO]: jest.fn().mockReturnValue({proximoNumero: 20}),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.VALIDAR_INTERVALO]: jest.fn()
        }

        localVue = applicationTestBuilder.build()

        store = new Vuex.Store({actions,state})
    })

    const defaultMount = () => shallowMount(ModalAdicionarIntervalosForm, {
        store,
        router,
        localVue,
        mocks: {errors, $validator},
        propsData: {
            reservaIntervalo: reservaIntervalo,
            maiorNumeroFimIntervalo: 10
        }
    })

    describe('computed', () => {
        it('deve verificar se preenchimento manual', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.preenchimentoManual).toEqual(false)
        })

        it('retornar se preenchimento manual', () => {
            reservaIntervalo.preenchimento = 'MANUAL'
            wrapper = shallowMount(ModalAdicionarIntervalosForm, {
                store,
                router,
                localVue,
                mocks: {errors, $validator},
                propsData: {
                    reservaIntervalo: reservaIntervalo,
                    maiorNumeroFimIntervalo: 10
                }
            })

            expect(wrapper.vm.preenchimentoManual).toEqual(true)
        })

        it('deve retornar quantidade de digitos do patrimonio', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.quantidadeDigitosNumeroPatrimonio).toEqual(10)
        })

        it('deve retornar placeholder de quantidade Ex: 00', () => {
            reservaIntervalo.preenchimento = 'AUTOMATICO'
            wrapper = shallowMount(ModalAdicionarIntervalosForm, {
                store,
                router,
                localVue,
                mocks: {errors, $validator},
                propsData: {
                    reservaIntervalo: reservaIntervalo,
                    maiorNumeroFimIntervalo: 10
                }
            })

            expect(wrapper.vm.placeholderQuantidade).toEqual('Ex: 00')
        })

        it('deve retornar placeholder de quantidade -', () => {
            reservaIntervalo.preenchimento = 'MANUAL'
            wrapper = shallowMount(ModalAdicionarIntervalosForm, {
                store,
                router,
                localVue,
                mocks: {errors, $validator},
                propsData: {
                    reservaIntervalo: reservaIntervalo,
                    maiorNumeroFimIntervalo: 10
                }
            })

            expect(wrapper.vm.placeholderQuantidade).toEqual(' -')
        })

        it('deve retornar placeholder de quantidade 3', () => {
            reservaIntervalo.preenchimento = 'MANUAL'
            reservaIntervalo.quantidadeReservada = 3
            wrapper = shallowMount(ModalAdicionarIntervalosForm, {
                store,
                router,
                localVue,
                mocks: {errors, $validator},
                propsData: {
                    reservaIntervalo: reservaIntervalo,
                    maiorNumeroFimIntervalo: 10
                }
            })

            expect(wrapper.vm.placeholderQuantidade).toEqual('3')
        })

    })

    describe('methods', () => {

        it('deve deselecionar reserva intervalo', () => {
            wrapper = defaultMount()

            wrapper.vm.tratarSelecaoReservaIntervalo()

            expect(wrapper.emitted().atualizarMaiorNumeroFimIntervalo).toBeTruthy()
            expect(wrapper.emitted().atualizarMaiorNumeroFimIntervalo[0][0]).toEqual(null)

            expect(wrapper.emitted().deselecionaReservaIntervalo).toBeTruthy()
            expect(wrapper.emitted().deselecionaReservaIntervalo[0][0]).toEqual(reservaIntervalo)
        })

        it('deve selecionar reserva intervalo', () => {
            reservaIntervalo.selecionado = true
            wrapper = shallowMount(ModalAdicionarIntervalosForm, {
                store,
                router,
                localVue,
                mocks: {errors, $validator},
                propsData: {
                    reservaIntervalo: reservaIntervalo,
                    maiorNumeroFimIntervalo: 10
                }
            })

            wrapper.vm.tratarSelecaoReservaIntervalo()

            expect(wrapper.emitted().selecionaReservaIntervalo).toBeTruthy()
            expect(wrapper.emitted().selecionaReservaIntervalo[0][0]).toEqual(reservaIntervalo)
        })

        it('deve buscar proximo número disponível', async () => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.buscarProximoNumeroDisponivelDoIntervalo()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_PROXIMO_NUMERO]).toHaveBeenCalled()
            expect(wrapper.vm.proximoNumeroDisponivel).toEqual(20)
            expect(wrapper.vm.reservaIntervalo.numeroInicio).toEqual(20)
        })

        it('deve tratar edição quantidade', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.tratarEdicaoQuantidade()
            await flushPromises()
            expect(wrapper.emitted().selecionaReservaIntervalo).toBeTruthy()
            expect(wrapper.emitted().selecionaReservaIntervalo[0][0]).toEqual(reservaIntervalo)

            expect(wrapper.emitted().buscarProximoIntervalo).toBeTruthy()
            expect(wrapper.emitted().buscarProximoIntervalo[0][0]).toEqual(reservaIntervalo)
        })

        it('deve tratar intervalo', async () => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.tratarIntervalo()
            await flushPromises()

            expect(wrapper.emitted().atualizarMaiorNumeroFimIntervalo).toBeTruthy()
            expect(wrapper.emitted().atualizarMaiorNumeroFimIntervalo[0][0]).toEqual(reservaIntervalo.numeroFim)
        })

        it('deve calcular quantidade', () => {
            reservaIntervalo.preenchimento = 'MANUAL'
            reservaIntervalo.numeroInicio = 1
            reservaIntervalo.numeroFim = 10
            wrapper = shallowMount(ModalAdicionarIntervalosForm, {
                store,
                router,
                localVue,
                mocks: {errors, $validator},
                propsData: {
                    reservaIntervalo: reservaIntervalo,
                    maiorNumeroFimIntervalo: 10
                }
            })

            wrapper.vm.calcularQuantidadeIntervaloManual()

            expect(wrapper.emitted().selecionaReservaIntervalo).toBeTruthy()
            expect(wrapper.emitted().selecionaReservaIntervalo[0][0]).toEqual(reservaIntervalo)

            expect(wrapper.vm.reservaIntervalo.quantidadeReservada).toEqual(10)
        })

        it('deve validar intervalo', async () => {
            reservaIntervalo.preenchimento = 'MANUAL'
            reservaIntervalo.numeroInicio = 1
            reservaIntervalo.numeroFim = 10
            wrapper = shallowMount(ModalAdicionarIntervalosForm, {
                store,
                router,
                localVue,
                mocks: {errors, $validator},
                propsData: {
                    reservaIntervalo: reservaIntervalo,
                    maiorNumeroFimIntervalo: 10
                }
            })
            await flushPromises()

            wrapper.vm.validarIntervaloReservaIntervalo()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.VALIDAR_INTERVALO]).toHaveBeenCalled()

            expect(wrapper.vm.maiorNumeroFimIntervalo).toEqual(10)
        })

    })
})
