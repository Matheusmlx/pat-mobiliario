import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {

    async [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS](context, incorporacaoId) {
        const {data} = await api.empenho.buscarEmpenhos(incorporacaoId)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.INSERIR_EMPENHO]({commit}, empenho) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const {data} = await api.empenho.salvar(empenho)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.ATUALIZAR_EMPENHO]({commit}, empenho) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const {data} = await api.empenho.atualizar(empenho)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.REMOVER_EMPENHO]({commit}, empenhoId) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        await api.empenho.remover(empenhoId)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    }
}
