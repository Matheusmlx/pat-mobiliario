import store from '@/core/store'
import {mutationTypes} from '@/core/constants'

class SetConfiguracaoNotificacaoListener {
  async execute() {

    const notificationConfig = store.state.notificacao.configuracaoNotificacao
    return new Promise((resolve) => {
      store.commit(mutationTypes.LOKI.SET_NOTIFICATION_CONFIG, notificationConfig)
      resolve()
    })
  }
}

export default new SetConfiguracaoNotificacaoListener()