import {mutationTypes} from '@/core/constants'

export default {
    [mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.SET_PAGINACAO_BUSCA_TODOS_ITENS_VISUALIZACAO](state, paginacao) {
        state.resultadoBuscaTodosItensMovimentacaoInternaVisualizacao.paginacao = paginacao
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.RESETA_PAGE_BUSCA_TODOS_ITENS_VISUALIZACAO](state) {
        state.resultadoBuscaTodosItensMovimentacaoInternaVisualizacao.paginacao.page = 1
    }
}
