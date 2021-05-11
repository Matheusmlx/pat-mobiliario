import api from '@/core/apiclient'
import {actionTypes} from '@/core/constants'

export default {

    async [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]({state}, movimentacaoId) {
        const {filtros, paginacao} = state.resultadoBuscaTodosItensAdicionadosMovimentacaoInterna
        const {data} = await api.movimentacaoInternaItem.buscarTodosItensAdicionadosMovimentacaoInterna(filtros, paginacao, movimentacaoId)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.ITEM.DELETAR_ITEM_MOVIMENTACAO_INTERNA](context, data) {
        await api.movimentacaoInternaItem.deletarItemMovimentacaoInterna(data.movimentacaoInternaId, data.itemId)
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]({state}, movimentacaoId) {
        const {filtros, paginacao} = state.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna
        const {data} = await api.movimentacaoInternaItem.buscarTodosItensParaSelecaoMovimentacaoInterna(filtros, paginacao, movimentacaoId)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.ITEM.ADICIONAR_ITENS_MOVIMENTACAO_INTERNA](context, movimentacao) {
        await api.movimentacaoInternaItem.adicionarItensMovimentacaoInterna(movimentacao)
    },


    async [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA](context, movimentacaoId) {
        const {data} = await api.movimentacaoInternaItem.buscarQuantidadeItensAdicionadosMovimentacaoInterna(movimentacaoId)
        return data
    },
}
