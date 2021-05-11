import axios from 'axios'

class EntreEstoquesApiClient {

    async atualizarMovimentacaoEntreEstoques(entreEstoques) {
        return axios.put(`api/movimentacoes/internas/entre-estoques/${entreEstoques.id}`, entreEstoques)
    }

    async buscarMovimentacaoEntreEstoquesPorId(entreEstoquesId) {
        return axios.get(`api/movimentacoes/internas/movimentacao/${entreEstoquesId}`)
    }

    async finalizarMovimentacaoEntreEstoques(entreEstoquesId) {
        return axios.patch(`api/movimentacoes/internas/entre-estoques/${entreEstoquesId}/finalizar`)
    }
}

export default new EntreEstoquesApiClient
