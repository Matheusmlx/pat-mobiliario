import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {
    async [actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.ATUALIZAR_DISTRIBUICAO]({commit}, distribuicao) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, distribuicao)
        const {data} = await api.distribuicao.atualizarMovimentacaoDistribuicao(distribuicao)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.BUSCAR_DISTRIBUICAO_POR_ID]({commit}, distribuicaoId) {
        const {data} = await api.distribuicao.buscarMovimentacaoDistribuicaoPorId(distribuicaoId)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, data)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO](context, distribuicaoId) {
        const {data} = await api.distribuicao.finalizarMovimentacaoDistribuicaoPorId(distribuicaoId)
        return data
    }
}
