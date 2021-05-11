import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {
    async [actionTypes.MOVIMENTACAO_INTERNA.CADASTRAR_MOVIMENTACAO_INTERNA]({commit}, movimentacao) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const {data} = await api.movimentacaointerna.cadastrarMovimentacaoInterna(movimentacao)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, data)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA](context, movimentacaoId) {
        const {data} = await api.movimentacaointerna.buscarTipoMovimentacaoInterna(movimentacaoId)
        return data.tipo
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_COMPLETA]({commit}, movimentacaoId) {
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, {})
        await api.movimentacaointerna.removerMovimentacaoInternaCompleta(movimentacaoId)
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_VAZIA]({commit}, movimentacaoId) {
        commit(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, {})
        await api.movimentacaointerna.removerMovimentacaoInternaVazia(movimentacaoId)
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.EDITAR_TIPO_MOVIMENTACAO_INTERNA](context, movimentacao) {
        const {data} = await api.movimentacaointerna.editarTipoMovimentacaoInterna(movimentacao)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TODAS_MOVIMENTACOES_INTERNAS]({state}) {
        const {filtros, paginacao} = state.resultadoBuscaTodasMovimentacoesInternas
        const {data} = await api.movimentacaointerna.buscarTodasMovimentacoesInternas(filtros, paginacao)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA](context, movimentacaoId) {
        const {data} = await api.movimentacaointerna.buscarSituacaoMovimentacaoInterna(movimentacaoId)
        return data.situacao
    },
}
