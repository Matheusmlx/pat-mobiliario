import {mutationTypes} from '@/core/constants'

export default {
    [mutationTypes.CONFIGURACAO.CONCEDENTE.SET_FILTROS_BUSCA_TODOS_CONCEDENTES](state, filtros){
        state.todosConcedentes.filtros = filtros
    },

    [mutationTypes.CONFIGURACAO.CONCEDENTE.SET_PAGINACAO_BUSCA_TODOS_CONCEDENTES](state, paginacao) {
        state.todosConcedentes.paginacao = paginacao
    },

    [mutationTypes.CONFIGURACAO.CONCEDENTE.SET_CONCEDENTE](state, dadosGerais) {
        state.concedentes = dadosGerais
    },

    [mutationTypes.CONFIGURACAO.CONCEDENTE.RESETA_PAGE_CONCEDENTE](state) {
        state.todosConcedentes.paginacao.page = 1
    }
}
