import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ModalAdicionarReservaPorOrgaoForm from './ModalAdicionarReservaPorOrgaoForm'
import flushPromises from 'flush-promises'

describe('ModalAdicionarReservaPorOrgaoForm', () => {

    let wrapper, store, localVue, router, state, $validator

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
        collect() {},
        clear: jest.fn()
    }

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

        state = {
            parametros: {
                parametros: {
                    quantidadeDigitosNumeroPatrimonio: 10
                }
            }
        }

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

        store = new Vuex.Store({state})
    })

    const defaultMount = () => shallowMount(ModalAdicionarReservaPorOrgaoForm, {
        store,
        router,
        localVue,
        mocks: {errors, $validator},
        propsData: {
            reserva: reserva,
            orgaos: orgaos.items
        },
    })

    describe('Computed', () => {
        it('Deve retornar quantidade de digitos do numero de patrimonio do state', async () => {
            wrapper = defaultMount()

            expect(wrapper.vm.quantidadeDigitosNumeroPatrimonio).toEqual(state.parametros.parametros.quantidadeDigitosNumeroPatrimonio)
        })

        it('Deve retornar de preenchimento automático', async () => {
            reserva.preenchimento = 'AUTOMATICO'
            wrapper = defaultMount()

            expect(wrapper.vm.preenchimentoAutomatico).toBeTruthy()
        })

        it('Deve retornar de preenchimento diferente de automático', async () => {
            reserva.preenchimento = 'MANUAL'
            wrapper = defaultMount()

            expect(wrapper.vm.preenchimentoAutomatico).toBeFalsy()
        })
    })

    describe('methods', () => {

        describe('verificarSeTodosCamposPreenchidos', () => {
            it('Deve liberar botão finalizar se campos obrigatórios preenchidos', async () => {
                wrapper = defaultMount()

                const camposPreenchidos = jest.spyOn(wrapper.vm, 'camposPreenchidos')
                wrapper.vm.verificarSeTodosCamposPreenchidos()
                await flushPromises()

                expect(wrapper.emitted().liberarBotaoFinalizar).toBeTruthy()
                expect(wrapper.emitted().bloquearBotaoFinalizar).toBeFalsy()
                expect(camposPreenchidos).toHaveBeenCalled()
            })

            it('Deve bloquear botão finalizar se campos obrigatórios não preenchidos', async () => {
                $validator.validateAll = jest.fn().mockReturnValue(false)
                wrapper = defaultMount()

                const camposPreenchidos = jest.spyOn(wrapper.vm, 'camposPreenchidos')
                wrapper.vm.verificarSeTodosCamposPreenchidos()
                await flushPromises()

                expect(wrapper.emitted().bloquearBotaoFinalizar).toBeTruthy()
                expect(wrapper.emitted().liberarBotaoFinalizar).toBeFalsy()
                expect(camposPreenchidos).toHaveBeenCalled()
            })
        })

        describe('calcularQuantidadeIntervaloManual', () => {
            it('Deve calcular a quantidade para o intervalo manual', async () => {
                $validator.validateAll = jest.fn().mockReturnValue(true)
                reserva.preenchimento = 'MANUAL'
                reserva.numeroInicio = 1
                reserva.numeroFim = 10
                wrapper = defaultMount()
                await flushPromises()

                const camposPreenchidos = jest.spyOn(wrapper.vm, 'camposPreenchidos')
                const verificarSeTodosCamposPreenchidos = jest.spyOn(wrapper.vm, 'verificarSeTodosCamposPreenchidos')
                wrapper.vm.calcularQuantidadeIntervaloManual()
                await flushPromises()

                expect(wrapper.vm.reserva.quantidadeReservada).toEqual(reserva.numeroFim - reserva.numeroInicio + 1)
                expect(camposPreenchidos).toHaveBeenCalled()
                expect(verificarSeTodosCamposPreenchidos).toHaveBeenCalled()
            })

            it('Não deve calcular a quantidade para o intervalo automático', async () => {
                reserva.preenchimento = 'AUTOMATICO'
                reserva.numeroInicio = 1
                reserva.numeroFim = 10
                wrapper = defaultMount()

                const camposPreenchidos = jest.spyOn(wrapper.vm, 'camposPreenchidos')
                const verificarSeTodosCamposPreenchidos = jest.spyOn(wrapper.vm, 'verificarSeTodosCamposPreenchidos')
                wrapper.vm.calcularQuantidadeIntervaloManual()
                await flushPromises()

                expect(wrapper.vm.reserva.quantidadeReservada).toEqual(reserva.quantidadeReservada)
                expect(camposPreenchidos).toHaveBeenCalled()
                expect(verificarSeTodosCamposPreenchidos).toHaveBeenCalled()
            })

            it('Não deve calcular a quantidade para o intervalo sem campos obrigatórios preenchidos', async () => {
                $validator.validateAll = jest.fn().mockReturnValue(false)
                reserva.preenchimento = 'MANUAL'
                reserva.numeroInicio = 1
                reserva.numeroFim = 10
                wrapper = defaultMount()

                const camposPreenchidos = jest.spyOn(wrapper.vm, 'camposPreenchidos')
                const verificarSeTodosCamposPreenchidos = jest.spyOn(wrapper.vm, 'verificarSeTodosCamposPreenchidos')
                const resetarQuantidade = jest.spyOn(wrapper.vm, 'resetarQuantidade')

                wrapper.vm.calcularQuantidadeIntervaloManual()
                await flushPromises()

                expect(wrapper.vm.reserva.quantidadeReservada).toEqual(reserva.quantidadeReservada)
                expect(camposPreenchidos).toHaveBeenCalled()
                expect(verificarSeTodosCamposPreenchidos).toHaveBeenCalled()
                expect(resetarQuantidade).toHaveBeenCalled()
            })
        })

        it('Deve resetar quantidade reservada', () => {
            reserva.quantidadeReservada = 100
            wrapper = defaultMount()
            wrapper.vm.resetarQuantidade()

            expect(wrapper.vm.reserva.quantidadeReservada).toEqual(null)
        })


        describe('tratarEdicao', () => {
            it('Deve resetar a reserva e buscar pŕoximo número disponível se preenchimento manual e possuir orgao', () => {
                reserva.orgaoId = 1
                reserva.preenchimento = 'MANUAL'
                wrapper = defaultMount()

                const resetarReserva = jest.spyOn(wrapper.vm, 'resetarReserva')
                wrapper.vm.tratarEdicao()

                expect(wrapper.emitted().buscarProximoNumeroDisponivel).toBeTruthy()
                expect(wrapper.emitted().buscarProximoNumeroDisponivel[0][0]).toEqual(reserva.orgaoId)
                expect(resetarReserva).toHaveBeenCalled()
            })

            it('Não deve buscar pŕoximo número disponível se preenchimento automático', () => {
                reserva.orgaoId = 1
                reserva.preenchimento = 'AUTOMATICO'
                wrapper = defaultMount()

                wrapper.vm.tratarEdicao()

                expect(wrapper.emitted().buscarProximoNumeroDisponivel).toBeFalsy()
            })

            it('Não deve buscar pŕoximo número disponível se não possuir orgao', () => {
                reserva.orgaoId = null
                reserva.preenchimento = 'MANUAL'
                wrapper = defaultMount()

                wrapper.vm.tratarEdicao()

                expect(wrapper.emitted().buscarProximoNumeroDisponivel).toBeFalsy()
            })

            it('Deve buscar intervalo se campos preenchidos e preenchimento automatico', async () => {
                $validator.validateAll = jest.fn().mockReturnValue(true)
                reserva.preenchimento = 'AUTOMATICO'
                wrapper = defaultMount()

                const buscarIntervaloEntidade = {
                    orgaoId: reserva.orgaoId,
                    quantidadeReservada: reserva.quantidadeReservada,
                }

                const verificarSeTodosCamposPreenchidos = jest.spyOn(wrapper.vm, 'verificarSeTodosCamposPreenchidos')
                const ciarEntidadeParaBuscarIntervalo = jest.spyOn(wrapper.vm, 'ciarEntidadeParaBuscarIntervalo')
                wrapper.vm.tratarEdicao()
                await flushPromises()

                expect(wrapper.emitted().buscarIntervalo).toBeTruthy()
                expect(wrapper.emitted().buscarIntervalo[0][0]).toEqual(buscarIntervaloEntidade)
                expect(verificarSeTodosCamposPreenchidos).toHaveBeenCalled()
                expect(ciarEntidadeParaBuscarIntervalo).toHaveBeenCalled()
            })

            it('Deve resetar intervalo da reserva se campos obrigatórios não preenchidos', async () => {
                $validator.validateAll = jest.fn().mockReturnValue(false)
                reserva.preenchimento = 'AUTOMATICO'
                wrapper = defaultMount()

                const resetarReserva = jest.spyOn(wrapper.vm, 'resetarReserva')
                const verificarSeTodosCamposPreenchidos = jest.spyOn(wrapper.vm, 'verificarSeTodosCamposPreenchidos')
                const ciarEntidadeParaBuscarIntervalo = jest.spyOn(wrapper.vm, 'ciarEntidadeParaBuscarIntervalo')
                wrapper.vm.tratarEdicao()
                await flushPromises()

                expect(wrapper.emitted().buscarIntervalo).toBeFalsy()
                expect(ciarEntidadeParaBuscarIntervalo).not.toHaveBeenCalled()
                expect(verificarSeTodosCamposPreenchidos).toHaveBeenCalled()
                expect(resetarReserva).toHaveBeenCalled()
            })
        })

        it('Deve buscar próximo número', async () => {
            wrapper = defaultMount()

            wrapper.vm.buscarProximoNumero()
            await flushPromises()

            expect(wrapper.emitted().buscarProximoNumeroDisponivel).toBeTruthy()
            expect(wrapper.emitted().buscarProximoNumeroDisponivel[0][0]).toEqual(reserva.orgaoId)
        })

        it('Deve buscar intervalo', async () => {
            wrapper = defaultMount()

            const buscarIntervaloEntidade = {
                orgaoId: reserva.orgaoId,
                quantidadeReservada: reserva.quantidadeReservada,
            }

            wrapper.vm.buscarIntervalo()
            await flushPromises()

            expect(wrapper.emitted().buscarIntervalo).toBeTruthy()
            expect(wrapper.emitted().buscarIntervalo[0][0]).toEqual(buscarIntervaloEntidade)
        })

        it('Deve resetar reserva', () => {
            wrapper = defaultMount()

            wrapper.vm.resetarReserva()

            expect(wrapper.emitted().resetarReserva).toBeTruthy()
        })

        it('Deve criar entidade para buscar intervalo', () => {
            wrapper = defaultMount()

            const buscarIntervaloEntidade = {
                orgaoId: reserva.orgaoId,
                quantidadeReservada: reserva.quantidadeReservada,
            }

            expect(wrapper.vm.ciarEntidadeParaBuscarIntervalo()).toEqual(buscarIntervaloEntidade)
        })

        it('Deve finalizar reserva', () => {
            wrapper = defaultMount()

            wrapper.vm.finalizarReserva()

            expect(wrapper.emitted().finalizar).toBeTruthy()
        })

        it('Deve validar o valor digitado no auto-complete', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.filtroComboAutoComplete(orgaos.items[0], 'org')).toBe(true)
        })

        it('Não deeve encontrar o valor digitado no auto-complete', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.filtroComboAutoComplete(orgaos.items[0], 'abc')).toBe(false)
        })


    })

})
