import Vue from 'vue'
import {mutationTypes, produto} from '@/core/constants'
import FileManagerUtils from '@/core/utils/FileManagerUtils'

export default {

    [mutationTypes.COMUM.SET_EXPANDIR_MENU](state) {
        Vue.set(state.loki, 'asideClosed', false)
    },

    [mutationTypes.COMUM.SET_LINK_ARQUIVO](state) {
        Vue.set(state.loki.file, 'api', '/patrimonio-mobiliario/api/v1/arquivos')
    },

    [mutationTypes.COMUM.SET_MENU_AVATAR](state) {
        const actions = {
            1: { title: 'Perfil', icon: 'person', path: '/perfil' }
        }
        Vue.set(state.loki, 'avatarActions', actions)
    },

    [mutationTypes.COMUM.SET_PRODUTO](state, data) {
        state.loki.product.id = data.id
        state.loki.product.name = data.nome
        state.loki.product.mainLogo = FileManagerUtils.createUrl(data.atributosExtendidos.logoMenu)
        state.loki.product.symbolLogo = FileManagerUtils.createUrl(data.atributosExtendidos.logoMenuRetraido)
        state.loki.product.logoMobile = FileManagerUtils.createUrl(data.atributosExtendidos.logoMobile)
        state.loki.product.version = produto.VERSAO
        state.loki.product.copywrite = produto.COPYRIGHT
        state.loki.product.logoutUrl = produto.LOGOUT_URL
    },

    [mutationTypes.COMUM.SET_RETRAIR_MENU](state) {
        Vue.set(state.loki, 'asideClosed', true)
    },

    [mutationTypes.COMUM.SET_USUARIO_LOGADO](state, usuario) {
        const user = {
            id: usuario.userId,
            name: usuario.userName,
            fullName: usuario.name,
            photo: FileManagerUtils.createThumbnailUrl(usuario.imageUrl),
            type: usuario.tipoUsuario,
            domains: usuario.domains,
            domainId: usuario.domainId,
            domainName: usuario.domainName,
            domainType: usuario.domainType,
            authorities: usuario.authorities
        }
        Vue.set(state.loki, 'user', user)
    },

    [mutationTypes.COMUM.SET_PARAMETROS](state, parametros) {
        state.parametros = parametros
    },

    [mutationTypes.COMUM.MOSTRAR_FALHA_SALVAMENTO_AUTOMATICO] () {
        this.commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        this.commit(mutationTypes.LOKI.SET_AUTO_SAVED_MESSAGE, 'Última atualização não salva')
        this.commit(mutationTypes.LOKI.SET_SAVING_MESSAGE, '${date} atrás')
    },

    [mutationTypes.LOKI.ENABLE_AUTO_SAVING] () {
        this.commit(mutationTypes.LOKI.SET_AUTO_SAVED_MESSAGE, '<i class="fas fa-check mr-1"></i> Salvo Automaticamente')
        this.commit(mutationTypes.LOKI.SET_SAVING_MESSAGE, 'Salvo ${date} atrás')
    }
}
