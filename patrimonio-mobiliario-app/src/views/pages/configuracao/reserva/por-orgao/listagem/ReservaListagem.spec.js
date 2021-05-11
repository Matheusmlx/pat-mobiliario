import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ReservaListagem from './ReservaListagem'

describe('ReservaListagem', () => {
    let wrapper, mutations, actions, localVue, router, state, store

    const reservas = {
        itens: [
            {
                codigo: 32434,
                dataCriacao: '2020-07-15T23:00:00.000-0400',
                quantidadeReservada: 250,
                restante: 50,
                orgaos: ['SED', 'SAD', 'AGEPAN', 'SISPAT'],
                situacao: 'PARCIAL'
            }
        ],
        totalElements: 1,
        totalPages: 1,
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
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
            reservaGeral: {
                resultadoBuscaTodasReservasListagem: {
                    filtros: {
                        conteudo: {
                            default: null,
                            label: 'Pesquisa',
                            value: ''
                        }
                    },
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
            [mutationTypes.CONFIGURACAO.RESERVA.SET_FILTROS_BUSCA_TODAS_RESERVAS]: jest.fn(),
            [mutationTypes.CONFIGURACAO.RESERVA.SET_PAGINACAO_BUSCA_TODAS_RESERVAS]: jest.fn(),
            [mutationTypes.CONFIGURACAO.RESERVA.RESETA_PAGE_RESERVA]: jest.fn()
        }

        actions = {
            [actionTypes.CONFIGURACAO.RESERVA.BUSCAR_TODAS_RESERVAS_LISTAGEM]: jest.fn().mockReturnValue(reservas)
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, mutations, actions})
    })

    describe('Events', () => {
        it('Deve emitir o evento de paginar quando alterar a paginação da reserva', async () => {
            wrapper = shallowMount(ReservaListagem, {
                localVue,
                router,
                store,
                stubs: {
                    ReservaListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'paginar\', {})"></button></div>'
                    }
                }
            })

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.SET_PAGINACAO_BUSCA_TODAS_RESERVAS].mock.calls[0][1]).toEqual({})
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.BUSCAR_TODAS_RESERVAS_LISTAGEM]).toHaveBeenCalled()
        })

        it('Deve emitir o evento de buscar simples', async () => {
            wrapper = shallowMount(ReservaListagem, {
                localVue,
                router,
                store,
                stubs: {
                    'az-search': {
                        template: '<div><button class="stub" @click="$emit(\'simple-search\', \'0001 - teste\')"></button></div>'
                    }
                }
            })

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.SET_FILTROS_BUSCA_TODAS_RESERVAS].mock.calls[0][1].conteudo.value).toEqual('0001 - teste')
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.BUSCAR_TODAS_RESERVAS_LISTAGEM]).toHaveBeenCalled()
            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('0001 - teste')
        })

        it('Deve emitir o evento de remover os filtros da pesquisa', async () => {
            wrapper = shallowMount(ReservaListagem, {
                localVue,
                router,
                store,
                stubs: {
                    'az-search': {
                        template: '<div><button class="stub" @click="$emit(\'remove-filter\', \'conteudo\')"></button></div>'
                    }
                }
            })
            wrapper.vm.filtrosInterno.conteudo.value = '0001 - teste'
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual(null)
        })

        it('Deve emitir o evento de acessar a reserva e redirecionar para visualizacao', async () => {
            wrapper = shallowMount(ReservaListagem, {
                localVue,
                router,
                store,
                stubs: {
                    ReservaListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'acessar\', reserva = {id:1, situacao:\'EM_ELABORACAO\'})"></button></div>'
                    }
                }
            })
            wrapper.find('button[class="stub"]').trigger('click')

            await flushPromises()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'ReservaVisualizacao', params: {id: 1}})
        })

    })

    describe('Methods', () => {
        it('Deve emitir o método de buscar todas as reservas', async () => {
            wrapper = shallowMount(ReservaListagem, {
                localVue,
                router,
                store,

            })
            await flushPromises()
            wrapper.vm.buscar()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.RESERVA.BUSCAR_TODAS_RESERVAS_LISTAGEM]).toHaveBeenCalled()
        })

        it('Deve redirecionar para nova reserva', () => {
            wrapper = shallowMount(ReservaListagem, {
                localVue,
                router,
                store,
            })

            wrapper.vm.redirecionarNovaReserva()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'ReservaCadastro'})
        })
    })
})
