import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {

    async [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.ATUALIZAR_ENTRE_ESTOQUES]({commit}, entreEstoques) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, entreEstoques)
        const {data} = await api.entreEstoques.atualizarMovimentacaoEntreEstoques(entreEstoques)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.BUSCAR_ENTRE_ESTOQUES_POR_ID]({commit}, entreEstoquesId) {
        const {data} = await api.entreEstoques.buscarMovimentacaoEntreEstoquesPorId(entreEstoquesId)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, data)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES](context, entreEstoques) {
        const {data} = await api.entreEstoques.finalizarMovimentacaoEntreEstoques(entreEstoques)
        return data
    }
}
