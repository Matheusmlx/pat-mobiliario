import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {
    async [actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.ATUALIZAR_DEVOLUCAO_ALMOXARIFADO]({commit}, devolucaoAlmoxarifado) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, devolucaoAlmoxarifado)
        const {data} = await api.devolucaoAlmoxarifado.atualizarMovimentacaoDevolucaoAlmoxarifado(devolucaoAlmoxarifado)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.BUSCAR_DEVOLUCAO_ALMOXARIFADO_POR_ID]({commit}, devolucaoAlmoxarifadoId) {
        const {data} = await api.devolucaoAlmoxarifado.buscarMovimentacaoDevolucaoAlmoxarifadoPorId(devolucaoAlmoxarifadoId)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, data)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO](context, devolucaoAlmoxarifadoId) {
        const {data} = await api.devolucaoAlmoxarifado.finalizarMovimentacaoDevolucaoAlmoxarifado(devolucaoAlmoxarifadoId)
        return data
    }
}
