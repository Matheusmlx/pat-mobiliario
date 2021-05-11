import api from '@/core/apiclient'
import actionTypes from '@/core/constants/actionTypes'

export default {
    [actionTypes.CONFIGURACAO.PARAMETROS.BUSCAR_TODOS_PARAMETROS]: async ({commit}) => {
        try {
            const {data} = await api.parametros.buscarParametros()
            commit('SET_PARAMETROS', data)
        } catch (e) {
            console.error(e)
        }
    }
}
