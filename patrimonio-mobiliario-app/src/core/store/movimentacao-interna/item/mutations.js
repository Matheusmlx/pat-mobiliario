import {mutationTypes} from '@/core/constants'

export default {
    [mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]
        (state) {
        state.resultadoBuscaTodosItensAdicionadosMovimentacaoInterna.paginacao.page = 1
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]
        (state, paginacao) {
        state.resultadoBuscaTodosItensAdicionadosMovimentacaoInterna.paginacao = paginacao
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]
        (state) {
        state.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.paginacao.page = 1
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]
        (state, paginacao) {
        state.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.paginacao = paginacao
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]
        (state, filtros) {
        state.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.filtros = filtros
    },
}
