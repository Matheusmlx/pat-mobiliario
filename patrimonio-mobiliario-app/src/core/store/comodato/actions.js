import api from '@/core/apiclient'
import {actionTypes} from '@/core/constants'

export default {
    async [actionTypes.COMODATO.BUSCAR_COMODANTES_DINAMICAMENTE](context, conteudo) {
        const {data} = await api.comodato.buscarComodanteDinamicamente(conteudo)
        return data
    }
}
