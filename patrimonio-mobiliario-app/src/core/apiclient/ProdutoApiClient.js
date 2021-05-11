import axios from 'axios'

class ProdutoApiClient {
    async buscarPorNome(produtoNome) {
        return await axios.get(`/hal/public/produtos?produtoNome=${produtoNome}`)
    }

    async buscarParametros() {
        return await axios.get('api/configuracao/parametros')
    }
}

export default new ProdutoApiClient()
