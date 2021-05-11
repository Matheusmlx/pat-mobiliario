import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'

class ReservaIntervaloGlobalApiClient {

    async buscarTodos(reservaId, paginacao) {
        const url = AzSearchUrlBuilder.build(`api/configuracao/reservas/${reservaId}/intervalos`, {}, paginacao)
        return axios.get(url)
    }

    async excluirIntervalo(reservaId,intervaloId) {
        return axios.delete(`api/configuracao/reservas/${reservaId}/intervalos/${intervaloId}`)
    }

    async buscarIntervalo(reserva) {
        return axios.post(`api/configuracao/reservas/${reserva.reservaId}/intervalos/proximoDisponivel`, reserva)
    }

    async salvarIntervalo(intervalo) {
        return axios.post(`api/configuracao/reservas/${intervalo.reservaId}/intervalos`, intervalo)
    }

    async existeEmElaboracao(reservaId) {
        return axios.get(`api/configuracao/reservas/${reservaId}/intervalos/existe/EM_ELABORACAO`)
    }

    async buscarProximoNumero(reservaId, maiorNumeroFimIntervalo) {
        return axios.post(`api/configuracao/reservas/${reservaId}/intervalos/proximoNumero`,
          {reservaId: reservaId, maiorNumeroFimIntervalo: maiorNumeroFimIntervalo})
    }

    async validarIntervalo(reservaId, numeroInicio, numeroFim) {
        return axios.post(`api/configuracao/reservas/${reservaId}/intervalos/validar`,
          {reservaId: reservaId, numeroInicio: numeroInicio, numeroFim: numeroFim})
    }
}

export default new ReservaIntervaloGlobalApiClient
