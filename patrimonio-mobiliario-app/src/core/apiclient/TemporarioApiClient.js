import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'

class TemporarioApiClient {
    async buscarMovimentacaoTemporariaPorId(temporariaId) {
        return axios.get(`api/movimentacoes/internas/movimentacao/${temporariaId}`)
    }

    async atualizarMovimentacaoTemporaria(temporaria) {
        return axios.put(`api/movimentacoes/internas/temporaria/${temporaria.id}`, temporaria)
    }

    async enviarMovimentacaoTemporaria(idMovimentacao) {
        return axios.patch(`api/movimentacoes/internas/temporaria/${idMovimentacao}/enviar`)
    }

    async devolverTodosPatrimonios(temporariaId) {
        return axios.put(`api/movimentacoes/internas/temporaria/${temporariaId}/devolver-todos`)
    }

    async buscarInformacaoTooltip(temporariaId) {
        return axios.get(`api/movimentacoes/internas/temporaria/${temporariaId}/visualizacao`)
    }

    async devolverItensParcial(temporaria) {
        return axios.put(`api/movimentacoes/internas/temporaria/${temporaria.id}/devolver-parcial?patrimoniosId=${temporaria.patrimoniosId}`)
    }

    async buscarTodosItensParaDevolucao(filtros, paginacao, temporariaId) {
        const url = AzSearchUrlBuilder.build(
            `api/movimentacoes/internas/temporaria/${temporariaId}/devolver-patrimonios`,
            filtros,
            paginacao
        )
        return axios.get(url)
    }
}

export default new TemporarioApiClient
