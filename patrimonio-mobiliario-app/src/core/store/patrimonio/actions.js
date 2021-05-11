import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {

    async [actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]({commit}, dados) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ATIVAR_SALVAMENTO_AUTOMATICO)
        await api.patrimonio.cadastrarPatrimonio(dados)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    },

    async [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]({commit}, dados) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ATIVAR_SALVAMENTO_AUTOMATICO)
        const {data} = await api.patrimonio.atualizarPatrimonio(dados)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]({state}, itemIncorporacaoId) {
        const {filtros, paginacao} = state.resultadoBuscaTodosPatrimonios
        const {data} = await api.patrimonio.buscarPatrimonios(filtros, paginacao, itemIncorporacaoId)
        return data
    },

    async [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_REGISTRO]({state}, itemIncorporacaoId) {
        const {filtros, paginacao} = state.resultadoBuscaTodosPatrimoniosRegistro
        const {data} = await api.patrimonio.buscarPatrimonios(filtros, paginacao, itemIncorporacaoId)
        return data
    },

    async [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_LISTAGEM]({state}) {
        const {filtros, paginacao} = state.resultadoBuscaTodosPatrimoniosListagem
        const {data} = await api.patrimonio.buscarPatrimoniosListagem(filtros, paginacao)
        return data
    },

    async [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID_FICHA](context, patrimonioId) {
        const {data} = await api.patrimonio.buscarPatrimonioPorIdFicha(patrimonioId)
        return data
    },

    async [actionTypes.PATRIMONIO.BUSCAR_TODAS_MOVIMENTACOES](context, patrimonioId) {
        const {data} = await api.patrimonio.buscarTodasMovimentacoes(patrimonioId)
        return data
    },

    async [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS]({state}, incorporacaoId) {
        const {filtros, paginacao} = state.resultadoBuscaTodosPatrimoniosDeTodosItens
        const {data} = await api.patrimonio.buscarTodosPatrimoniosDeTodosItens(filtros, paginacao, incorporacaoId)
        return data
    },

    async [actionTypes.PATRIMONIO.ESTORNAR_PATRIMONIOS](context, estorno) {
        await api.patrimonio.estornarPatrimonios(estorno)
    },

    async [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_ESTORNADOS]({state}, idIncorporacao) {
        const {filtros, paginacao} = state.resultadoBuscaTodosPatrimoniosEstornados
        const {data} = await api.patrimonio.buscarTodosPatrimoniosEstornados(filtros, paginacao, idIncorporacao)
        return data
    },

    async [actionTypes.PATRIMONIO.BUSCAR_DEPRECIACOES](context, patrimonioId) {
        const {data} = await api.patrimonio.buscarDepreciacoes(patrimonioId)
        return data.items
    },
}
