import {actionTypes} from '@/core/constants'
import api from '@/core/apiclient'

export default {

    async [actionTypes.CONFIGURACAO.RECONHECIMENTO.EDITAR_RECONHECIMENTO](context, reconhecimento) {
        return api.reconhecimento.editarReconhecimento(reconhecimento)
    },

    async [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_TODOS_RECONHECIMENTOS]({state}) {
        const {filtros, paginacao} = state.todosReconhecimentos
        const {data} = await api.reconhecimento.buscarTodosReconhecimento(filtros, paginacao)
        return data
    },

    async [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]() {
        const filtros = {
            situacao:{
                value:'ATIVO'
            }
        }
        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            descending: false,
            sortBy: ['nome'],
            sortDesc:[false]
        }
        const {data} = await api.reconhecimento.buscarTodosReconhecimento(filtros, paginacao)
        return data
    },

    async [actionTypes.CONFIGURACAO.RECONHECIMENTO.INSERIR_RECONHECIMENTO](context, reconhecimento) {
        return api.reconhecimento.inserirReconhecimento(reconhecimento)
    }
}
