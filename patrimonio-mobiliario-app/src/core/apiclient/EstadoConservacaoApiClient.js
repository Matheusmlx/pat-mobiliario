import axios from 'axios'

class EstadoConservacaoApiClient {

    async buscarTodos() {
        return axios.get('api/patrimonios/incorporacao/itens/estadosconservacao')
    }

}

export default new EstadoConservacaoApiClient()
