import store from '@/core/store'
import {actionTypes} from '@/core/constants'

class BuscarProdutoListener {
    async execute() {
        return store.dispatch(actionTypes.COMUM.BUSCAR_PRODUTO_POR_NOME)
    }
}

export default new BuscarProdutoListener()
