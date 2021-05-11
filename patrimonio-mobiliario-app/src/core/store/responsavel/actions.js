import {actionTypes} from '@/core/constants'
import api from '@/core/apiclient'

export default {

    async [actionTypes.RESPONSAVEL.BUSCAR_TODOS_RESPONSAVEIS]() {
        const {data} = await api.responsavel.buscarTodosResponsaveis()
        return data
    }

}
