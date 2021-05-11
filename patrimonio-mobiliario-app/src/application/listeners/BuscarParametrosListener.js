import store from '@/core/store'
import {actionTypes} from '@/core/constants'

class BuscarParametrosListener {
    async execute() {
        return store.dispatch(`parametros/${actionTypes.CONFIGURACAO.PARAMETROS.BUSCAR_TODOS_PARAMETROS}`)
    }
}

export default new BuscarParametrosListener()
