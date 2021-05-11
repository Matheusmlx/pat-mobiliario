import api from '@/core/apiclient'
import {actionTypes,mutationTypes} from '@/core/constants'

export default {

    async [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_TEMPORARIA_POR_ID]({commit}, temporariaId) {
        const {data} = await api.temporaria.buscarMovimentacaoTemporariaPorId(temporariaId)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA,data)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ATUALIZAR_TEMPORARIA]({commit},temporaria) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, temporaria)
        const {data} = await api.temporaria.atualizarMovimentacaoTemporaria(temporaria)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA](context, idMovimentacao){
        const {data} = await api.temporaria.enviarMovimentacaoTemporaria(idMovimentacao)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.DEVOLVER_TODOS_PATRIMONIOS]({commit},temporariaId){
        const {data} = await api.temporaria.devolverTodosPatrimonios(temporariaId)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA,data)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_INFORMACAO_TOOLTIP](context, temporariaId) {
        const {data} = await api.temporaria.buscarInformacaoTooltip(temporariaId)
        return data
    }



}

