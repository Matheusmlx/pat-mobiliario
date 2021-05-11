import {actionTypes, mutationTypes} from '@/core/constants'
import api from '@/core/apiclient'

export default {
    async [actionTypes.CONFIGURACAO.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS]({ state,commit }) {
        const {filtros,paginacao} = state.todasContasContabeis
        const { data } = await api.contaContabil.buscarTodos(filtros, paginacao)
        commit(mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_CONTA_CONTABIL,data)
        return data
    },
    async [actionTypes.CONFIGURACAO.CONTA_CONTABIL.EDITAR_CONTA_CONTABIL]({ commit }, contaContabil) {
        const { data } =  api.contaContabil.editar(contaContabil)
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        commit(mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_CONTA_CONTABIL, contaContabil)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.CONFIGURACAO.CONTA_CONTABIL.BUSCAR_CONTA_CONTABIL_POR_ID]({ commit }, id) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const { data } = await api.contaContabil.buscarPorId(id)
        commit(mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_CONTA_CONTABIL, data)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },
    async [actionTypes.CONFIGURACAO.CONTA_CONTABIL.SALVAR_CONTA_CONTABIL]({ commit }, contaContabil) {
        const { data } = await api.contaContabil.salvar(contaContabil)
        commit(mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_CONTA_CONTABIL, data)
    },
}
