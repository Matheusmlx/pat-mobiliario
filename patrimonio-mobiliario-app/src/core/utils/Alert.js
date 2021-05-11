import {mutationTypes} from '@/core/constants'

export default class Alert {

    constructor(store) {
        this.store = store
    }

    showError(key) {
        if (!key) {
            key = 'Não foi possível realizar a operação!'
        }
        this.store.commit(mutationTypes.LOKI.SHOW_ALERT, { message: key, type: 'error' })
    }

    showInfo(key) {
        if (!key) {
            key = 'Operação realizada com sucesso!'
        }
        this.store.commit(mutationTypes.LOKI.SHOW_ALERT, { message: key, type: 'success' })
    }

}
