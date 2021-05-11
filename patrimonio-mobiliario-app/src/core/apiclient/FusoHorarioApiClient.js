import axios from 'axios'

class FusoHorarioApiClient {

    async buscarFusoHorario() {
        return axios.get('api/fuso-horario')
    }
}

export default new FusoHorarioApiClient()
