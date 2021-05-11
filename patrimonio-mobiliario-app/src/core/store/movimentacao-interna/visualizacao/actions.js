import {actionTypes, mutationTypes} from '@/core/constants'
import api from '@/core/apiclient'

export default {
    async [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.ATUALIZAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO]({commit}, movimentacaoInterna) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const {data} = await api.movimentacaoInternaVisualiacao.atualizarMovimentacaoInterna(movimentacaoInterna)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO](context, movimentacaoInternaId) {
        const {data} = await api.movimentacaoInternaVisualiacao.buscarMovimentacaoInterna(movimentacaoInternaId)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_TODOS_ITENS_ADICIONADOS_VISUALIZACAO_REGISTRO]({state}, movimentacaoId) {
        const {filtros, paginacao} = state.resultadoBuscaTodosItensMovimentacaoInternaVisualizacao
        const {data} = await api.movimentacaoInternaItem.buscarTodosItensAdicionadosMovimentacaoInterna(filtros, paginacao, movimentacaoId)
        return data
    }
}
