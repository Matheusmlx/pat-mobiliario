import {mutationTypes} from '@/core/constants'

export default {
    [mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA](state, movimentacao) {
        state.movimentacaoInterna = movimentacao
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.RESETA_PAGE_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]
        (state) {
        state.resultadoBuscaTodasMovimentacoesInternas.paginacao.page = 1
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]
        (state, paginacao) {
        state.resultadoBuscaTodasMovimentacoesInternas.paginacao = paginacao
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]
        (state, filtros) {
        state.resultadoBuscaTodasMovimentacoesInternas.filtros = filtros
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.SET_SITUACAO_MOVIMENTACAO_INTERNA] (state, situacao) {
        state.situacaoMovimentacaoInterna = situacao
    },
}
