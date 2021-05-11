import {mutationTypes} from '@/core/constants'

export default {

    [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_INCORPORACAO_ITEM](state, incorporacaoItem) {
        state.dadosGerais = incorporacaoItem
        return state.dadosGerais
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_INCORPORACAO](state, filtros) {
        state.resultadoBuscaTodosItensIncorporacao.filtros = filtros
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_INCORPORACAO](state, paginacao) {
        state.resultadoBuscaTodosItensIncorporacao.paginacao = paginacao
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_PAGINACAO_BUSCA_ITENS_INCORPORADOS](state, paginacao){
        state.buscaRegistroItensIncorporacao.paginacao = paginacao
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_INCORPORACAO_ITEM](state) {
        state.resultadoBuscaTodosItensIncorporacao.paginacao.page = 1
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_BUSCA_ITENS_INCORPORADOS](state) {
        state.buscaRegistroItensIncorporacao.paginacao.page = 1
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ATIVAR_SALVAMENTO_AUTOMATICO](state) {
        state.autoSave.show = true
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO](state) {
        state.autoSave.show = false
    },
}
