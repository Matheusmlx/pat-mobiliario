import store from '@/core/store'
import {mutationTypes} from '@/core/constants'

class SetMensagemLoadingListener {
    async execute() {
        return new Promise((resolve) => {
            store.commit(mutationTypes.LOKI.SET_LOADING_MESSAGE, store.state.defaultLoadingMessage)
            resolve()
        })
    }
}

export default new SetMensagemLoadingListener()
