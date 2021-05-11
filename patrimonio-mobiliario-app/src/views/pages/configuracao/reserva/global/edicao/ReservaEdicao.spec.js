import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ReservaEdicao from './ReservaEdicao'
import flushPromises from 'flush-promises'

describe('ReservaEdicao', () => {

    let wrapper, actions, mutations, store, localVue, state, router, errors, $validator, $swal

    const reserva = {
        id: 1,
        quantidadeReservada: 10,
        quantidadeRestante: 9,
        preenchimento: 'AUTOMATICO',
        numeroInicio: 1,
        numeroFim: 19
    }

    beforeEach(async () => {
        localVue = applicationTestBuilder.build()

        errors = {
            collect: jest.fn(),
            clear: jest.fn()
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn()
                }
            }
        }

        $swal = () => ({
            then: jest.fn()
        })

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {current: {name: 'ReservaEdicao', params: {id: '1'}}}
        }

        state = {
            intervalo: {
                existeEmElaboracao: false
            }
        }

        actions = {
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]: jest.fn().mockReturnValue(reserva),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA]: jest.fn().mockReturnValue(true),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO]: jest.fn().mockReturnValue(false),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM]: jest.fn()
        }

        mutations = {
            [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO]: jest.fn()
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, mutations, actions})
    })

    describe('mounted', () => {
        beforeEach(() => {
            wrapper = shallowMount(ReservaEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
        })

        it('deve buscar reserva pelo id da rota', async () => {
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID].mock.calls[0][1]).toBe(router.history.current.params.id)
        })

        it('deve verificar existência de intervalos em elaboração', async () => {
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO].mock.calls[0][1]).toBe(wrapper.vm.reservaId)
        })
    })

    describe('methods', () => {

        it('Deve buscar a reserva por id', async () => {
            wrapper = shallowMount(ReservaEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            await flushPromises()
            await wrapper.vm.buscarReserva()

            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID].mock.calls[0][1]).toEqual(wrapper.vm.reservaId)
        })

        it('Deve existir intervalos', async () => {
            wrapper = shallowMount(ReservaEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            await flushPromises()
            await wrapper.vm.buscarReserva()

            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]).toHaveBeenCalled()
            expect(wrapper.vm.existeIntervalos).toEqual(true)
        })

        it('Não deve existir intervalos', async () => {
            reserva.quantidadeRestante = 10
            actions = {
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]: jest.fn().mockReturnValue(reserva),
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO]: jest.fn().mockReturnValue(false)
            }
            wrapper = shallowMount(ReservaEdicao, {
                store: new Vuex.Store({state, actions}),
                router,
                localVue,
                mocks: {$validator, errors}
            })
            await flushPromises()
            await wrapper.vm.buscarReserva()

            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]).toHaveBeenCalled()
            expect(wrapper.vm.existeIntervalos).toEqual(false)
        })

        it('Deve finalizar a reserva e redirecionar para listagem se reserva sem quantidade restante', async () => {
            reserva.quantidadeRestante = 0
            actions = {
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]: jest.fn().mockReturnValue(reserva),
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA]: jest.fn().mockReturnValue(true),
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO]: jest.fn().mockReturnValue(false),
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM]: jest.fn()
            }
            wrapper = shallowMount(ReservaEdicao, {
                store: new Vuex.Store({state, mutations, actions}),
                router,
                localVue,
                mocks: {$validator, errors}
            })
            await flushPromises()
            wrapper.vm.tratarEventoFinalizar()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA].mock.calls[0][1]).toBe(wrapper.vm.reservaId)
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ReservaVisualizacao'})
        })

        it('Deve chamar alerta de reserva parcial,finalizar a reserva e redirecionar para listagem se reserva com quantidade restante', async () => {
            reserva.quantidadeRestante = 1
            actions = {
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]: jest.fn().mockReturnValue(reserva),
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA]: jest.fn().mockReturnValue(true),
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO]: jest.fn().mockReturnValue(false),
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM]: jest.fn()
            }
            wrapper = shallowMount(ReservaEdicao, {
                store: new Vuex.Store({state, mutations, actions}),
                router,
                localVue,
                mocks: {$validator, errors, $swal}
            })
            await flushPromises()
            const mostrarConfirmacao = jest.spyOn(wrapper.vm, 'mostrarConfirmacao')
            wrapper.vm.tratarEventoFinalizar()
            await flushPromises()

            expect(mostrarConfirmacao).toHaveBeenCalled()
        })

        it('Deve finalizar a reserva com sucesso e redirecionar para listagem', async () => {
            wrapper = shallowMount(ReservaEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            await flushPromises()

            wrapper.vm.finalizar()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA].mock.calls[0][1]).toBe(wrapper.vm.reservaId)
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ReservaVisualizacao'})
        })

        it('Não deve redirecionar para listagem quando ocorrer erro ao finalizar a reserva', async () => {
            actions = {
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]: jest.fn().mockReturnValue(reserva),
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO]: jest.fn().mockReturnValue(true),
                [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA]: jest.fn().mockReturnValue(false)
            }

            wrapper = shallowMount(ReservaEdicao, {
                store: new Vuex.Store({state, actions}),
                router,
                localVue,
                mocks: {$validator, errors}
            })
            await flushPromises()

            wrapper.vm.finalizar()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA].mock.calls[0][1]).toBe(wrapper.vm.reservaId)
            expect(router.replace).not.toHaveBeenCalled()
        })

        it('Deve redirecionar para listagem', async () => {
            wrapper = shallowMount(ReservaEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            await flushPromises()

            wrapper.vm.voltarParaListagem()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ReservaListagem'})
        })

        it('Deve recarregar os dados da reserva', async () => {
            wrapper = shallowMount(ReservaEdicao, {
                store,
                router,
                localVue,
                mocks: {$validator, errors}
            })
            await flushPromises()
            await wrapper.vm.recarregarDadosReserva()

            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID].mock.calls[0][1]).toEqual(wrapper.vm.reservaId)
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO].mock.calls[0][1]).toBe(wrapper.vm.reservaId)
            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM].mock.calls[0][1]).toEqual(wrapper.vm.reservaId)
        })
    })
})
