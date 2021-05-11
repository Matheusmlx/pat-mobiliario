import axios from 'axios'

class RotulosPersonalizadosApiClient {

    async getAll() {
        return axios.get('api/rotulosPersonalizados')
    }
}

export default new RotulosPersonalizadosApiClient()
