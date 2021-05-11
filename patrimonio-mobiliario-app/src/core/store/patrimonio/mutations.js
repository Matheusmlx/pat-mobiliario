import {mutationTypes} from '@/core/constants'

export default {

    [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS]
        (state, paginacao) {
        state.resultadoBuscaTodosPatrimonios.paginacao = paginacao
    },

    [mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO]
        (state) {
        state.resultadoBuscaTodosPatrimonios.paginacao.page = 1
    },

    [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_REGISTRO]
        (state, paginacao) {
        state.resultadoBuscaTodosPatrimoniosRegistro.paginacao = paginacao
    },

    [mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_REGISTRO]
        (state) {
        state.resultadoBuscaTodosPatrimoniosRegistro.paginacao.page = 1
    },

    [mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_LISTAGEM](state, filtros) {
        state.resultadoBuscaTodosPatrimoniosListagem.filtros = filtros
    },

    [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_LISTAGEM](state, paginacao) {
        state.resultadoBuscaTodosPatrimoniosListagem.paginacao = paginacao
    },

    [mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_LISTAGEM](state) {
        state.resultadoBuscaTodosPatrimoniosListagem.paginacao.page = 1
    },

    [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS](state, paginacao) {
        state.resultadoBuscaTodosPatrimoniosDeTodosItens.paginacao = paginacao
    },

    [mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS](state, filtros) {
        state.resultadoBuscaTodosPatrimoniosDeTodosItens.filtros = filtros
    },

    [mutationTypes.PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS](state) {
        state.resultadoBuscaTodosPatrimoniosDeTodosItens.paginacao.page = 1
    },

    [mutationTypes.PATRIMONIO.SET_ESTORNO](state, estorno){
        state.estorno = estorno
    },

    [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS](state, paginacao) {
        state.resultadoBuscaTodosPatrimoniosEstornados.paginacao = paginacao
    },

    [mutationTypes.PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS](state) {
        state.resultadoBuscaTodosPatrimoniosEstornados.paginacao.page = 1
    },
}
