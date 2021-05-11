import axios from 'axios'

class ParametrosApiClient {

    async buscarParametros() {
        return axios.get('api/parametros-sistema')
    }
}

export default new ParametrosApiClient()
