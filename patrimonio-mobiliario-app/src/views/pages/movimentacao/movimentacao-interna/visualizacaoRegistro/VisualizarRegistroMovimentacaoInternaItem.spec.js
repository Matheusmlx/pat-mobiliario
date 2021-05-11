import VisualizarRegistroMovimentacaoInternaItem from './VisualizarRegistroMovimentacaoInternaItem'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

let router, state, store, localVue, actions, mutations

describe('VisualizarRegistroMovimentacaoInternaItem', () => {

    const movimentacaoInternaItens = {
        items: [
            {
                patrimonioId: 5,
                patrimonioNumero: '0000000005',
                patrimonioDescricao: 'Veículo caminhão leve com as seguintes características técnicas mínimas: -zero quilômetro; -combustível a diesel',
                incorporacaoCodigo: '8',
                movimentacaoId:15
            },
            {
                patrimonioId: 6,
                patrimonioNumero: '0000000006',
                patrimonioDescricao: 'Veículo caminhão leve com as seguintes características técnicas mínimas: -zero quilômetro; -combustível a diesel',
                incorporacaoCodigo: '8',
                movimentacaoId:15
            },
            {
                patrimonioId: 7,
                patrimonioNumero: '0000000007',
                patrimonioDescricao: 'Veículo caminhão leve com as seguintes características técnicas mínimas: -zero quilômetro; -combustível a diesel',
                incorporacaoCodigo: '8',
                movimentacaoId:15
            },
            {
                patrimonioId: 8,
                patrimonioNumero: '0000000008',
                patrimonioDescricao: 'Veículo caminhão leve com as seguintes características técnicas mínimas: -zero quilômetro; -combustível a diesel',
                incorporacaoCodigo: '8',
                movimentacaoId:15
            }],
        totalPages: 1,
        totalElements: 4
    }

    const movimentacao = {
        id: 7,
        tipo: 'DISTRIBUICAO'
    }

    beforeEach(() => {
        state = {
            movimentacaoInternaVisualizacaoRegistro: {
                resultadoBuscaTodosItensMovimentacaoInternaVisualizacao: {
                    filtros: {},
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        sortDesc: []
                    }
                },
                colunasTabelaItensMovimentacao: []
            }
        }

        router = {
            routes: [],
            push: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'MovimentacaoInternaEdicaoDadosGerais',
                    params: { movimentacaoInternaId: 7}
                }
            }
        }

        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_TODOS_ITENS_ADICIONADOS_VISUALIZACAO_REGISTRO]: jest.fn().mockReturnValue(movimentacaoInternaItens)
        }

        mutations = {
            [mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.SET_PAGINACAO_BUSCA_TODOS_ITENS_VISUALIZACAO]: jest.fn(),
            [mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.RESETA_PAGE_BUSCA_TODOS_ITENS_VISUALIZACAO]: jest.fn()
        }

        store = new Vuex.Store({actions, mutations, state})

    })

    const defaultMount = () => shallowMount(VisualizarRegistroMovimentacaoInternaItem, {
        store,
        router,
        localVue,
        propsData: {movimentacao}
    })

    describe('Watch', () => {

        it('deve carregar os itens quando trocar a movimentação', async () => {
            const wrapper = defaultMount()
            await flushPromises()

            wrapper.setProps({
                movimentacao: {
                    id: 1
                }
            })
            await flushPromises()

            expect(wrapper.vm.movimentacaoInternaId).toBe(1)
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.RESETA_PAGE_BUSCA_TODOS_ITENS_VISUALIZACAO]).toBeCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_TODOS_ITENS_ADICIONADOS_VISUALIZACAO_REGISTRO]).toBeCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_TODOS_ITENS_ADICIONADOS_VISUALIZACAO_REGISTRO].mock.calls[1][1]).toEqual(1)
        })

    })

    describe('Methods', () => {
        it('deve setar o id da movimentação', async() => {
            const wrapper = defaultMount()

            expect(wrapper.vm.movimentacaoInternaId).toBe(7)
        })

        it('deve buscar patrimônios adicionados', async() => {
            const wrapper = defaultMount()

            await flushPromises()

            expect(wrapper.vm.movimentacaoInternaItens).toEqual(movimentacaoInternaItens.items)
            expect(wrapper.vm.quantidadeTotalPatrimonios).toEqual(movimentacaoInternaItens.totalElements)
            expect(wrapper.vm.paginas).toBe(movimentacaoInternaItens.totalPages)
        })

        it('deve formatar a quantidade de patrimônios', async() => {
            const wrapper = defaultMount()

            const resultado = wrapper.vm.formatarQuantidadePatrimonios(5)

            expect(resultado).toBe('05')
        })

        it('deve tratar evento de paginação', async() => {
            const wrapper = defaultMount()

            const tratarEventoPaginar = jest.spyOn(wrapper.vm, 'tratarEventoPaginar')
            const buscarMovimentacaoItens = jest.spyOn(wrapper.vm, 'buscarMovimentacaoItens')

            wrapper.vm.paginacaoInterna.page = 2

            await flushPromises()

            expect(tratarEventoPaginar).toBeCalledWith(wrapper.vm.paginacaoInterna)
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.SET_PAGINACAO_BUSCA_TODOS_ITENS_VISUALIZACAO]).toBeCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.SET_PAGINACAO_BUSCA_TODOS_ITENS_VISUALIZACAO].mock.calls[0][1]).toEqual(wrapper.vm.paginacaoInterna)
            expect(buscarMovimentacaoItens).toBeCalled()
        })

        it('deve tratar evento para resetar a página', async() => {
            const wrapper = defaultMount()

            wrapper.vm.resetaPage()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.RESETA_PAGE_BUSCA_TODOS_ITENS_VISUALIZACAO]).toBeCalled()
        })
    })

})
