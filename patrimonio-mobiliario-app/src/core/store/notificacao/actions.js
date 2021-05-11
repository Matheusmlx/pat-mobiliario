import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'
import Notificacoes from '@/core/utils/Notificacoes'

export default {

    async [actionTypes.NOTIFICACAO.BUSCAR_NOTIFICACOES]({commit, state}) {
        const { data } = await api.notificacao.buscarNotificacoes(state.notificacao.page)
        data.items.unshift(...state.notificacao.items)
        commit(mutationTypes.LOKI.SET_NOTIFICATION, Notificacoes.retornaArrayDeNotificacoes(data))
        commit(mutationTypes.NOTIFICACAO.SET_NOTIFICACAO_ITENS,data.items)
        const dados = {
            quantidadeNaoVisualizadasAtual: data.quantidadeNaoVisualizadas,
            mostraVerMais: data.totalElements  > data.items.length
        }
        return  dados
    },

    async [actionTypes.NOTIFICACAO.BUSCAR_QUANTIDADE_NOTIFICACOES_NAO_VISUALIZADAS]({commit}) {
        const { data } = await api.notificacao.buscarQuantidadeNotificacoesNaoVisualizadas()
        commit(mutationTypes.LOKI.SET_NOTIFICATION, Notificacoes.retornaArrayDeNotificacoes(data))
        return data.quantidadeNaoVisualizadas
    }
}
