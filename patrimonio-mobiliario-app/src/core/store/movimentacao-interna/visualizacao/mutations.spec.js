import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {

    it('SET_PAGINACAO_BUSCA_TODOS_ITENS_VISUALIZACAO', () => {
        const state = {
            resultadoBuscaTodosItensMovimentacaoInternaVisualizacao: {
                filtros: {},
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: []
                }
            }
        }

        const paginacao = {
            page: 2,
            rowsPerPage: 10,
            sortDesc: []
        }

        mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.SET_PAGINACAO_BUSCA_TODOS_ITENS_VISUALIZACAO](state, paginacao)

        expect(state.resultadoBuscaTodosItensMovimentacaoInternaVisualizacao.paginacao).toEqual(paginacao)
    })

    it('RESETA_PAGE_BUSCA_TODOS_ITENS_VISUALIZACAO', () => {
        const state = {
            resultadoBuscaTodosItensMovimentacaoInternaVisualizacao: {
                filtros: {},
                paginacao: {
                    page: 3,
                    rowsPerPage: 10,
                    sortDesc: []
                }
            }
        }

        mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.RESETA_PAGE_BUSCA_TODOS_ITENS_VISUALIZACAO](state)

        expect(state.resultadoBuscaTodosItensMovimentacaoInternaVisualizacao.paginacao.page).toBe(1)
    })

})
