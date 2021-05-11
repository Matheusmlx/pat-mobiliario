import {mutationTypes} from '@/core/constants'

export default {

    [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_RESULTADO_BUSCA_TODOS_INTERVALOS](state, resultado) {
        state.resultadoBuscaTodosIntervalosListagem.itens = resultado.items
        state.resultadoBuscaTodosIntervalosListagem.totalItens = resultado.totalElements
        state.resultadoBuscaTodosIntervalosListagem.totalPaginas = resultado.totalPages
    },

    [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_PAGINACAO_BUSCA_TODOS_INTERVALOS](state, paginacao) {
        state.resultadoBuscaTodosIntervalosListagem.paginacao = paginacao
    },

    [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO](state) {
        state.resultadoBuscaTodosIntervalosListagem.paginacao.page = 1
    },

    [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_EXISTE_EM_ELABORACAO](state, existeEmElaboracao) {
        state.existeEmElaboracao = existeEmElaboracao
    },

}
