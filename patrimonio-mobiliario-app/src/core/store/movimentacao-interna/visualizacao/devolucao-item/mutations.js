import {mutationTypes} from '@/core/constants'

export default {
    [mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO](state, paginacao) {
        state.resultadoBuscaTodosPatrimoniosParaDevolucao.paginacao = paginacao
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO](state) {
        state.resultadoBuscaTodosPatrimoniosParaDevolucao.paginacao.page = 1
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO](state, filtros) {
        state.resultadoBuscaTodosPatrimoniosParaDevolucao.filtros = filtros
    }
}
