import store from '@/core/store'
import {mutationTypes} from '@/core/constants'

class SetApiArquivoListener {
    async execute() {
        return new Promise((resolve) => {
            store.commit(mutationTypes.COMUM.SET_LINK_ARQUIVO)
            resolve()
        })
    }
}

export default new SetApiArquivoListener()
