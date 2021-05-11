import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {
    async [actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_TODOS_CONCEDENTES]({state}) {
        const {filtros, paginacao} = state.todosConcedentes
        const {data} = await api.concedente.buscarTodos(filtros, paginacao)
        return data
    },

    async [actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_CONCEDENTES_DINAMICAMENTE](context, conteudo) {
        const { data } = await api.concedente.buscarDinamicamente(conteudo)
        return data
    },

    async [actionTypes.CONFIGURACAO.CONCEDENTE.SALVAR_CONCEDENTE]({commit}, dados) {
        const {data} = await api.concedente.salvar(dados)
        commit(mutationTypes.CONFIGURACAO.CONCEDENTE.SET_CONCEDENTE, data)
    },

    async [actionTypes.CONFIGURACAO.CONCEDENTE.EDITAR_CONCEDENTE]({commit}, dados) {
        const {data} = await api.concedente.editar(dados)
        commit(mutationTypes.CONFIGURACAO.CONCEDENTE.SET_CONCEDENTE, data)
    },

    async [actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_CONCEDENTE_POR_ID](context, id) {
        const {data} = await api.concedente.buscarId(id)
        return data
    }
}
