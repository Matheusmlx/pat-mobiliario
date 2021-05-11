import {mutationTypes} from '@/core/constants'

export default class LoadingScreen {
    constructor(store) {
        this.store = store
    }

    start() {
        this.store.commit(mutationTypes.LOKI.SET_GLOBAL_LOADING, true)
    }

    stop() {
        this.store.commit(mutationTypes.LOKI.SET_GLOBAL_LOADING, false)
        this.reset()
    }

    reset() {
        this.store.commit(mutationTypes.LOKI.SET_LOADING_MESSAGE, this.store.state.defaultLoadingMessage)
    }

}
