import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {

    async [actionTypes.PATRIMONIO.INCORPORACAO.FINALIZAR_INCORPORACAO](context, incorporacao) {
        const {data} = await api.incorporacao.finalizar(incorporacao)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES]({state}) {
        const {filtros, paginacao} = state.resultadoBuscaTodasIncorporacoes
        const {data} = await api.incorporacao.buscarTodos(filtros, paginacao)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.CADASTRAR_INCORPORACAO]({commit}, incorporacao) {
        const {data} = await api.incorporacao.cadastrar(incorporacao)
        commit(mutationTypes.PATRIMONIO.INCORPORACAO.SET_INCORPORACAO, data)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]({commit}, incorporacao) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const {data} = await api.incorporacao.atualizar(incorporacao)
        commit(mutationTypes.PATRIMONIO.INCORPORACAO.SET_INCORPORACAO, data)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]({commit}, incorporacaoId) {
        const {data} = await api.incorporacao.buscarPorId(incorporacaoId)
        commit(mutationTypes.PATRIMONIO.INCORPORACAO.SET_INCORPORACAO, data)

        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO_POR_ID]({commit}, incorporacao) {
        const {data} = await api.incorporacao.buscarRegistroPorId(incorporacao)
        commit(mutationTypes.PATRIMONIO.INCORPORACAO.SET_INCORPORACAO, data)

        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.DELETAR_INCORPORACAO](context, incorporacao) {
        await api.incorporacao.deletarIncorporacao(incorporacao)
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.REABRIR_INCORPORACAO](context, incorporacao) {
        await api.incorporacao.reabrirIncorporacao(incorporacao)
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO](context, incorporacaoId) {
        const {data} = await api.incorporacao.buscarSituacaoIncorporacao(incorporacaoId)
        return data.situacao
    },
}
