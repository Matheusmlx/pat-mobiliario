import {mutationTypes} from '@/core/constants'

export default {

    [mutationTypes.CONFIGURACAO.CONVENIO.SET_FILTROS_BUSCA_TODOS_CONVENIOS](state, filtros) {
        state.resultadoBuscaTodosConvenios.filtros = filtros
    },

    [mutationTypes.CONFIGURACAO.CONVENIO.SET_PAGINACAO_BUSCA_TODOS_CONVENIOS](state, paginacao) {
        state.resultadoBuscaTodosConvenios.paginacao = paginacao
    },

    [mutationTypes.CONFIGURACAO.CONVENIO.SET_CONVENIO](state, dadosGerais) {
        state.dadosGerais = dadosGerais
    },

    [mutationTypes.CONFIGURACAO.CONVENIO.RESETA_PAGE_CONVENIO](state) {
        state.resultadoBuscaTodosConvenios.paginacao.page = 1
    }
}
