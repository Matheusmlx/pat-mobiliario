import store from '@/core/store'
import {actionTypes} from '@/core/constants'

class BuscarUsuarioLogadoListener {
    async execute() {
        return store.dispatch(actionTypes.COMUM.BUSCAR_USUARIO_LOGADO)
    }
}

export default new BuscarUsuarioLogadoListener()
