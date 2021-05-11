import { actionTypes } from '@/core/constants'
import api from '@/core/apiclient'

export default {

  async [actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_INTERVALO_POR_ORGAO](context, buscarIntervaloEntidade) {
    const {data} = await api.reserva.buscarIntervalo(buscarIntervaloEntidade) || {data: undefined}
    return data
  },

  async [actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_PROXIMO_NUMERO_DISPONIVEL_POR_ORGAO](context, orgaoId) {
    const {data} = await api.reserva.buscarProximoNumero(orgaoId)
    return data
  },

  async [actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.FINALIZAR_RESERVA_POR_ORGAO](context, reserva) {
    const response = await api.reserva.finalizarReserva(reserva)
    return response.status === 200
  },

}

