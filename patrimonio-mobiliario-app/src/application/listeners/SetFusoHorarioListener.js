import moment from 'moment-timezone'
import { mutationTypes, actionTypes } from '@/core/constants'
import store from '@/core/store'

class SetFusoHorarioListener {
  async execute () {
    return new Promise((resolve) => {
      store.dispatch(actionTypes.COMUM.BUSCAR_FUSO_HORARIO).then(
        apiFusoHorarioDefault => {
          store.commit(mutationTypes.LOKI.SET_TIMEZONE, apiFusoHorarioDefault)
          moment.tz.setDefault(apiFusoHorarioDefault)
          moment.locale('pt')
        })
      resolve()
    })
  }
}

export default new SetFusoHorarioListener()
