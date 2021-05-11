import axios from 'axios'

class ReservaIntervaloApiClient {

    async baixarTermoDeGuarda(reservaId,intervaloId){
        return axios.get(`api/configuracao/reservas/${reservaId}/intervalos/${intervaloId}/termoDeGuarda`, {responseType:'arraybuffer'})
    }
}

export default new ReservaIntervaloApiClient
