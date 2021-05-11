import axios from 'axios'

class DistribuicaoApiClient {

    async atualizarMovimentacaoDistribuicao(distribuicao) {
        return axios.put(`api/movimentacoes/internas/distribuicao/${distribuicao.id}`, distribuicao)
    }

    async buscarMovimentacaoDistribuicaoPorId(distribuicaoId) {
        return axios.get(`api/movimentacoes/internas/movimentacao/${distribuicaoId}`)
    }

    async finalizarMovimentacaoDistribuicaoPorId(distribuicaoId) {
        return axios.post(`api/movimentacoes/internas/distribuicao/${distribuicaoId}/finalizar`)
    }

}

export default new DistribuicaoApiClient
