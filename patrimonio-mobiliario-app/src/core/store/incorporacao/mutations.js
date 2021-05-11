import {mutationTypes} from '@/core/constants'

export default {

    [mutationTypes.PATRIMONIO.INCORPORACAO.SET_FILTROS_BUSCA_TODAS_INCORPORACOES](state, filtros) {
        state.resultadoBuscaTodasIncorporacoes.filtros = filtros
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.SET_PAGINACAO_BUSCA_TODAS_INCORPORACOES](state, paginacao) {
        state.resultadoBuscaTodasIncorporacoes.paginacao = paginacao
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.RESETA_PAGE_INCORPORACAO](state) {
        state.resultadoBuscaTodasIncorporacoes.paginacao.page = 1
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.SET_INCORPORACAO](state, incorporacao) {
        state.incorporacao = incorporacao
        return state.incorporacao
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.SET_SITUACAO_INCORPORACAO](state, situacaoIncorporacao) {
      state.situacaoIncorporacao = situacaoIncorporacao
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO](state) {
        state.buscarRegistro = !state.buscarRegistro
    }
}
