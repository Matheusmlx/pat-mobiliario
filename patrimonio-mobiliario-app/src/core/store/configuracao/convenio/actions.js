import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {

    async [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_TODOS_CONVENIOS]({state}) {
        const {filtros, paginacao} = state.resultadoBuscaTodosConvenios
        const {data} = await api.convenio.buscarTodos(filtros, paginacao)
        return data
    },

    async [actionTypes.CONFIGURACAO.CONVENIO.SALVAR_CONVENIO]({commit}, dados) {
        const {data} = await api.convenio.salvar(dados)
        commit(mutationTypes.CONFIGURACAO.CONVENIO.SET_CONVENIO, data)
    },

    async [actionTypes.CONFIGURACAO.CONVENIO.EDITAR_CONVENIO]({commit}, dados) {
        const {data} = await api.convenio.editar(dados)
        commit(mutationTypes.CONFIGURACAO.CONVENIO.SET_CONVENIO, data)
    },

    async [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID](context, id) {
        const {data} = await api.convenio.buscarId(id)
        return data
    },

    async [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE](context, conteudo) {
        const {data} = await api.convenio.buscarConvenioDinamicamente(conteudo)
        return data
    },
}
