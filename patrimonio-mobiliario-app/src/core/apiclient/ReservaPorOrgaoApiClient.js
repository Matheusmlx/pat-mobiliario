import axios from 'axios'

class ReservaPorOrgaoApiClient {

  async buscarIntervalo(reserva) {
    return axios.post('api/configuracao/reservas/proximoIntervalo',
      {orgaoId: reserva.orgaoId, quantidadeReservada: reserva.quantidadeReservada})
  }

  async buscarProximoNumero(orgaoId) {
    return axios.post('api/configuracao/reservas/proximoNumero', {orgaoId: orgaoId})
  }

  async finalizarReserva(reserva) {
    return axios.post('api/configuracao/reservas', reserva)
  }

}

export default new ReservaPorOrgaoApiClient
