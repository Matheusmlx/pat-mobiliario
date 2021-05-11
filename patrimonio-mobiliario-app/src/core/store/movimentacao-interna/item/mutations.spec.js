import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {

    it('SET_PAGINACAO_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA', () => {
        const state = {
            resultadoBuscaTodosItensAdicionadosMovimentacaoInterna: {
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    itemsPerPage: 10,
                    sortBy: ['numero'],
                    defaultSortBy: ['numero'],
                    sortDesc: [false]
                }
            }
        }
        const paginacao = {
            page: 2,
            rowsPerPage: 10,
            itemsPerPage: 20,
            sortBy: ['numero'],
            defaultSortBy: ['numero'],
            sortDesc: [false]
        }
        mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA](state, paginacao)
        expect(state.resultadoBuscaTodosItensAdicionadosMovimentacaoInterna.paginacao).toEqual(paginacao)
    })

    it('RESETA_PAGE_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA', () => {
        const state = {
            resultadoBuscaTodosItensAdicionadosMovimentacaoInterna: {
                paginacao: {
                    page: 20,
                    rowsPerPage: 10,
                    itemsPerPage: 10,
                    sortBy: ['numero'],
                    defaultSortBy: ['numero'],
                    sortDesc: [false]
                }
            }
        }
        mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA](state)
        expect(state.resultadoBuscaTodosItensAdicionadosMovimentacaoInterna.paginacao.page).toEqual(1)
    })

    it('SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA', () => {
        const state = {
            resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna: {
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortBy: ['numero'],
                    defaultSortBy: ['numero'],
                    sortDesc: [false]
                }
            }
        }
        const paginacao = {
            page: 2,
            rowsPerPage: 15,
            itemsPerPage: 20,
            sortBy: ['numero'],
            defaultSortBy: ['numero'],
            sortDesc: [false]
        }
        mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA](state, paginacao)
        expect(state.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.paginacao).toEqual(paginacao)
    })

    it('RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA', () => {
        const state = {
            resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna: {
                paginacao: {
                    page: 2,
                    rowsPerPage: 10,
                    sortBy: ['numero'],
                    defaultSortBy: ['numero'],
                    sortDesc: [false]
                }
            }
        }
        mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA](state)
        expect(state.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.paginacao.page).toEqual(1)
    })

    it('SET_FILTROS_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA', () => {
        const state = {
            resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna: {
                filtros: {
                    conteudo: {
                        default: null,
                        label: 'Pesquisa',
                        value: ''
                    }
                },
            }
        }
        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: '1234'
            }
        }
        mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA](state, filtros)
        expect(state.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.filtros).toEqual(filtros)
    })
})
