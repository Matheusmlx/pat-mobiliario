import axios from 'axios'

class DevolucaoAlmoxarifadoApiClient {

    async atualizarMovimentacaoDevolucaoAlmoxarifado(devolucaoAlmoxarifado) {
        return axios.put(`api/movimentacoes/internas/devolucao-almoxarifado/${devolucaoAlmoxarifado.id}`, devolucaoAlmoxarifado)
    }

    async buscarMovimentacaoDevolucaoAlmoxarifadoPorId(devolucaoAlmoxarifadoId) {
        return axios.get(`api/movimentacoes/internas/movimentacao/${devolucaoAlmoxarifadoId}`)
    }

    async finalizarMovimentacaoDevolucaoAlmoxarifado(devolucaoAlmoxarifadoId) {
        return axios.patch(`api/movimentacoes/internas/devolucao-almoxarifado/${devolucaoAlmoxarifadoId}/finalizar`)
    }
}

export default new DevolucaoAlmoxarifadoApiClient
