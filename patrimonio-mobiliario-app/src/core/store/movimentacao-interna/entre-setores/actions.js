import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {

    async [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.ATUALIZAR_ENTRE_SETORES]({commit}, definitiva) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, definitiva)
        const {data} = await api.entreSetores.atualizarMovimentacaoDefinitiva(definitiva)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.BUSCAR_ENTRE_SETORES_POR_ID]({commit}, definitivaId) {
        const {data} = await api.entreSetores.buscarMovimentacaoDefinitivaPorId(definitivaId)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, data)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES](context, movimentacaoDefinitivaId) {
        const {data} = await api.entreSetores.finalizarMovimentacaoDefinitiva(movimentacaoDefinitivaId)
        return data
    }
}
