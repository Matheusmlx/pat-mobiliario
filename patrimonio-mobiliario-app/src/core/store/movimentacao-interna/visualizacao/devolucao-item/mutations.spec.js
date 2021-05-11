import mutations from './mutations'
import {mutationTypes} from '@/core/constants'


const state = {
    resultadoBuscaTodosPatrimoniosParaDevolucao: {
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
            sortBy: ['numero'],
            defaultSortBy: ['numero'],
            sortDesc: [false]
        }
    }
}

describe('Mutations', () => {

    it('deve chamar a mutation SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO e atualizar o state', () => {
        const paginacao = {
            page: 2,
            rowsPerPage: 10,
            sortBy: ['numero'],
            defaultSortBy: ['numero'],
            sortDesc: [false]
        }

        mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO](state, paginacao)
        expect(state.resultadoBuscaTodosPatrimoniosParaDevolucao.paginacao).toEqual(paginacao)
    })

    it('RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO', () => {
        state.resultadoBuscaTodosPatrimoniosParaDevolucao.paginacao.page = 2
        mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO](state)
        expect(state.resultadoBuscaTodosPatrimoniosParaDevolucao.paginacao.page).toEqual(1)
    })

    it('SET_FILTROS_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO', () => {
        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: '045'
            }
        }

        mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO](state, filtros)
        expect(state.resultadoBuscaTodosPatrimoniosParaDevolucao.filtros).toEqual(filtros)
    })

})
