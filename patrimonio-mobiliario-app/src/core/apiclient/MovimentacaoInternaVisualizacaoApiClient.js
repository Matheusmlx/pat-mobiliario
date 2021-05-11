import axios from 'axios'

class MovimentacaoInternaVisualizacaoApiClient {

    async atualizarMovimentacaoInterna(movimentacaoInterna) {
        return axios.put(`api/movimentacoes/internas/visualizacao/${movimentacaoInterna.id}`, movimentacaoInterna)
    }

    async buscarMovimentacaoInterna(movimentacaoInternaId) {
        return axios.get(`api/movimentacoes/internas/visualizacao/${movimentacaoInternaId}`)
    }

}

export default new MovimentacaoInternaVisualizacaoApiClient()
