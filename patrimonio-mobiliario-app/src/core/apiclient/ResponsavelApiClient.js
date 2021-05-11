import axios from 'axios'

class ResponsavelApiClient {

    buscarTodosResponsaveis() {
        return axios.get('api/responsavel')
    }

}

export default new ResponsavelApiClient()
