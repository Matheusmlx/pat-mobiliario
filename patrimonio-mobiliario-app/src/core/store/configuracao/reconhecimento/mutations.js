import {mutationTypes} from '@/core/constants'

export default {
    [mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_FILTROS_BUSCA_TODOS_RECONHECIMENTOS](state, filtros) {
        state.todosReconhecimentos.filtros = filtros
    },

    [mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_PAGINACAO_BUSCA_TODOS_RECONHECIMENTOS](state, paginacao) {
        state.todosReconhecimentos.paginacao = paginacao
    },

    [mutationTypes.CONFIGURACAO.RECONHECIMENTO.RESETA_PAGE_RECONHECIMENTO](state) {
        state.todosReconhecimentos.paginacao.page = 1
    }
}
