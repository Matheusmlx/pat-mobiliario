import {mutationTypes} from '@/core/constants'

export default {

    [mutationTypes.NOTIFICACAO.ACRESCENTA_PAGE_NOTIFICACAO](state) {
        state.notificacao.page = state.notificacao.page + 1
    },

    [mutationTypes.NOTIFICACAO.RESETA_PAGE_NOTIFICACAO](state) {
        state.notificacao.page = 1
    },

    [mutationTypes.NOTIFICACAO.SET_NOTIFICACAO_ITENS](state, itens) {
        state.notificacao.items = itens
    },

    [mutationTypes.NOTIFICACAO.RESETA_NOTIFICACAO_ITENS](state) {
        state.notificacao.items = []
    },

    [mutationTypes.NOTIFICACAO.OCULTA_NO_NOTIFICATION_TEXT](state) {
        state.noNotificationTextAuxiliar = state.configuracaoNotificacao.noNotificationText
        state.configuracaoNotificacao.noNotificationText = ''
    },

    [mutationTypes.NOTIFICACAO.VOLTA_NO_NOTIFICATION_TEXT_PADRAO](state) {
        state.configuracaoNotificacao.noNotificationText = state.noNotificationTextAuxiliar
        state.noNotificationTextAuxiliar = ''
    },
}
