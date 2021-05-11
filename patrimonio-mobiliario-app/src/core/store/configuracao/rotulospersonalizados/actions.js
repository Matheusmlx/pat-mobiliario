import api from '@/core/apiclient'
import actionTypes from '@/core/constants/actionTypes'

export default {
    [actionTypes.CONFIGURACAO.ROTULOS_PERSONALIZADOS.GET_ALL_ROTULOS_PERSONALIZADOS]: async ({commit}) => {
        try {
            const {data} = await api.rotulosPersonalizados.getAll()
            commit('SET_ROTULOS_PERSONALIZADOS', data.rotulosPersonalizados.i18n)
        } catch (e) {
            console.error(e)
        }
    }
}
