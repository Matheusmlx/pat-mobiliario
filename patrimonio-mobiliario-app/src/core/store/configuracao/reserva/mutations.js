import {mutationTypes} from '@/core/constants'

export default {

    [mutationTypes.CONFIGURACAO.RESERVA.SET_FILTROS_BUSCA_TODAS_RESERVAS](state, filtros) {
        state.resultadoBuscaTodasReservasListagem.filtros = filtros
    },

    [mutationTypes.CONFIGURACAO.RESERVA.SET_PAGINACAO_BUSCA_TODAS_RESERVAS](state, paginacao) {
        state.resultadoBuscaTodasReservasListagem.paginacao = paginacao
    },

    [mutationTypes.CONFIGURACAO.RESERVA.RESETA_PAGE_RESERVA](state) {
        state.resultadoBuscaTodasReservasListagem.paginacao.page = 1
    }
}
