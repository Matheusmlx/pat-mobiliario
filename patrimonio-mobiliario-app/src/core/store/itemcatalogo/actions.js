import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {
    async [actionTypes.ITEM_CATALOGO.BUSCAR_TODOS_ITENS_CATALOGO]({state}) {
        const {filtros, paginacao} = state.todosItens
        const {data} = await api.itens.buscarTodos(filtros, paginacao)
        return data
    },

    async [actionTypes.ITEM_CATALOGO.BUSCAR_ITEM_CATALOGO_POR_ID]({commit}, id) {
        const {data} = await api.itens.buscarPorId(id)
        commit(mutationTypes.ITEM_CATALOGO.SET_ITEM_CATALOGO, data)
        return data
    },
}
