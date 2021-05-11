import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ReservaIntervaloListagem from './ReservaIntervaloListagem'

describe('ReservaIntervaloListagem', () => {
    let wrapper, mutations, actions, localVue, router, state, store

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {current: {}}
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [{name: 'Mobiliario.Configuracao', hasAccess: true}]
                }
            },
            intervalo: {
                resultadoBuscaTodosIntervalosListagem: {
                    itens: [],
                    totalItens: 0,
                    totalPaginas: 0,
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        sortDesc: [false],
                        sortBy: ['situacao']
                    }
                }
            }
        }

        mutations = {
            [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_PAGINACAO_BUSCA_TODOS_INTERVALOS]: jest.fn(),
            [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO]: jest.fn()
        }

        actions = {
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM]: jest.fn(),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.REMOVER_INTERVALO_POR_ID]: jest.fn()
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, mutations, actions})
    })

    describe('Computed', () => {
        it('deve retornar os intervalos da reserva', async () => {
            state.intervalo.resultadoBuscaTodosIntervalosListagem.itens = [{id: 1}, {id: 2}]
            wrapper = shallowMount(ReservaIntervaloListagem, {
                localVue,
                router,
                store: new Vuex.Store({state})
            })

            await flushPromises()
            expect(wrapper.vm.itens).toEqual(state.intervalo.resultadoBuscaTodosIntervalosListagem.itens)
        })

        it('deve retornar o total de páginas da lista de intervalos da reserva', async () => {
            state.intervalo.resultadoBuscaTodosIntervalosListagem.totalPaginas = 5
            wrapper = shallowMount(ReservaIntervaloListagem, {
                localVue,
                router,
                store: new Vuex.Store({state})
            })

            await flushPromises()
            expect(wrapper.vm.paginas).toEqual(state.intervalo.resultadoBuscaTodosIntervalosListagem.totalPaginas)
        })

        it('deve retornar o total de intervalos da reserva', async () => {
            state.intervalo.resultadoBuscaTodosIntervalosListagem.totalItens = 5
            wrapper = shallowMount(ReservaIntervaloListagem, {
                localVue,
                router,
                store: new Vuex.Store({state})
            })

            await flushPromises()
            expect(wrapper.vm.totalItens).toEqual(state.intervalo.resultadoBuscaTodosIntervalosListagem.totalItens)
        })

        it('deve retornar a paginação atual', async () => {
            state.intervalo.resultadoBuscaTodosIntervalosListagem.paginacao = {
                page: 10,
                rowsPerPage: 10,
                sortDesc: [false],
                sortBy: ['situacao']
            }

            wrapper = shallowMount(ReservaIntervaloListagem, {
                localVue,
                router,
                store: new Vuex.Store({state})
            })

            await flushPromises()
            expect(wrapper.vm.paginacao).toEqual(state.intervalo.resultadoBuscaTodosIntervalosListagem.paginacao)
        })
    })

    describe('Events', () => {
        it('Deve emitir o evento de paginar quando alterar a paginação', async () => {
            wrapper = shallowMount(ReservaIntervaloListagem, {
                localVue,
                router,
                store,
                stubs: {
                    ReservaIntervaloListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'paginar\', {})"></button></div>'
                    }
                }
            })

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_PAGINACAO_BUSCA_TODOS_INTERVALOS].mock.calls[0][1]).toEqual({})
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM]).toHaveBeenCalled()
        })

    })

    describe('Methods', () => {
        it('Deve emitir o método de buscar todos os intervalos', async () => {
            wrapper = shallowMount(ReservaIntervaloListagem, {
                localVue,
                router,
                store,

            })
            await flushPromises()
            wrapper.vm.buscarTodosIntervalos()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM]).toHaveBeenCalled()
        })

        it('Deve redirecionar para adicionar novo intervalo', () => {
            wrapper = shallowMount(ReservaIntervaloListagem, {
                localVue,
                router,
                store,
            })
            wrapper.setProps({
                reservaId: 1
            })

            wrapper.vm.adicionarIntervalos()

            expect(router.replace.mock.calls[0][0]).toEqual({
                name: 'ModalAdicionarIntervalos',
                params: {id: wrapper.vm.reservaId}
            })
        })

        it('Deve remover intervalo', async () => {
            wrapper = shallowMount(ReservaIntervaloListagem, {
                localVue,
                router,
                store,
            })

            wrapper.vm.tratarEventoRemoverIntervalo(1)
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.REMOVER_INTERVALO_POR_ID]).toHaveBeenCalled()
            expect(wrapper.emitted().recarregarDadosReserva).toBeTruthy()
        })
    })
})
