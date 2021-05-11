import axios from 'axios'

class ReservaGlobalApiClient {

    async buscarReservaPorId(reservaId) {
        return axios.get(`api/configuracao/reservas/${reservaId}`)
    }

    async salvarReserva(reserva) {
        return axios.post('api/configuracao/reservas', reserva)
    }

    async editarReserva(reserva) {
        return axios.put(`api/configuracao/reservas/${reserva.id}`, reserva)
    }

    async buscarProximoNumeroDisponivel() {
        return axios.get('api/configuracao/reservas/proximoNumero')
    }

    async finalizarReserva(reservaId) {
        return axios.put(`api/configuracao/reservas/${reservaId}/finalizar`)
    }

    async buscarNumeroReservaUtilizado(id) {
        return axios.get(`api/configuracao/reservas/${id}/possuiNumerosUtilizados`)
    }

    async deletarReserva(id) {
        return axios.delete(`api/configuracao/reservas/${id}`)
    }
}

export default new ReservaGlobalApiClient
