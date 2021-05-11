import axios from 'axios'

class EntreSetoresApiClient {

    async atualizarMovimentacaoDefinitiva(definitiva) {
        return axios.put(`api/movimentacoes/internas/definitiva/${definitiva.id}`, definitiva)
    }

    async buscarMovimentacaoDefinitivaPorId(definitivaId) {
        return axios.get(`api/movimentacoes/internas/movimentacao/${definitivaId}`)
    }

    async finalizarMovimentacaoDefinitiva(definitivaId) {
        return axios.patch(`api/movimentacoes/internas/definitiva/${definitivaId}/finalizar`)
    }
}

export default new EntreSetoresApiClient
