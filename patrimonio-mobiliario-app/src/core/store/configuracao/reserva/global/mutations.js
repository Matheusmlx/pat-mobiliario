import {mutationTypes} from '@/core/constants'

export default {

    [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.SET_FILTRO_BUSCA_ORGAOS_RESERVA](state, filtros) {
        state.todosOrgaos.filtros = filtros
    },

    [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.SET_PAGINACAO_BUSCA_ORGAOS_RESERVA](state, paginacao) {
        state.todosOrgaos.paginacao = paginacao
    },

    [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.RESETA_PAGE_ORGAOS_RESERVA](state) {
        state.todosOrgaos.paginacao.page = 1
    }
}
