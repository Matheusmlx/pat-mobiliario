import {mutationTypes} from '@/core/constants'

export default {
    [mutationTypes.ITEM_CATALOGO.SET_FILTROS_BUSCA_TODOS_ITENS_CATALOGO](state, filtros){
        state.todosItens.filtros = filtros
    },

    [mutationTypes.ITEM_CATALOGO.SET_PAGINACAO_BUSCA_TODOS_ITENS_CATALOGO](state, paginacao) {
        state.todosItens.paginacao = paginacao
    },

    [mutationTypes.ITEM_CATALOGO.RESETA_PAGE_CATALOGO](state) {
        state.todosItens.paginacao.page = 1
    },

    [mutationTypes.ITEM_CATALOGO.SET_ORDENACAO_ITENS_CATALOGO](state, ordenacao) {
        state.todosItens.paginacao.sortBy = [ordenacao]
    },

    [mutationTypes.ITEM_CATALOGO.SET_ITEM_CATALOGO](state, data) {
        state.itemCatalogo = data
        return state.itemCatalogo
    }
}
