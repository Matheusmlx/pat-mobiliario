import api from '@/core/apiclient'
import {actionTypes} from '@/core/constants'

export default {
    async [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO]({state}, movimentacaoId) {
        const {filtros, paginacao} = state.resultadoBuscaTodosPatrimoniosParaDevolucao
        const {data} = await api.temporaria.buscarTodosItensParaDevolucao(filtros, paginacao, movimentacaoId)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.DEVOLVER_ITENS_PARCIAL](context, movimentacao) {
        const {data} = await api.temporaria.devolverItensParcial(movimentacao)
        return data
    }
}
