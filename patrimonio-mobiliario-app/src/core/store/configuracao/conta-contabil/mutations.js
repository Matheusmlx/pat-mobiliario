import {mutationTypes} from '@/core/constants'

export default {

    [mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_CONTA_CONTABIL](state, responseApi) {
        state.contasContabeis = responseApi.items
    },
    [mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_FILTROS_BUSCA_TODAS_CONTAS_CONTABEIS](state, filtros) {
        state.todasContasContabeis.filtros = filtros
    },
    [mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_PAGINACAO_BUSCA_TODAS_CONTAS_CONTABEIS](state, paginacao) {
        state.todasContasContabeis.paginacao = paginacao
    },
    [mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_LIMPAR_FILTROS_BUSCA_CONTA](state) {
        state.todasContasContabeis.filtros = {
            conteudo: '',
            objeto: {
                value: null,
                default: null,
                label: 'Pesquisa'
            },
            categoria: {
                value: null,
                default: null,
                label: 'Categoria'
            }
        }
    },
    [mutationTypes.CONFIGURACAO.CONTA_CONTABIL.RESETA_PAGE_CONTA_CONTABIL](state) {
        state.todasContasContabeis.paginacao.page = 1
    }

}
