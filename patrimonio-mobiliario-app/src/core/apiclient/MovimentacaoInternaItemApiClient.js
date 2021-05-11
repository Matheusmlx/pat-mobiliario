import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'

class MovimentacaoInternaItemApiClient {

    async buscarTodosItensAdicionadosMovimentacaoInterna(filtros, paginacao, movimentacaoInternaId){
        const url = AzSearchUrlBuilder.build(
            `api/movimentacoes/internas/${movimentacaoInternaId}/itens`,
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async deletarItemMovimentacaoInterna(movimentacaoInternaId, itemId){
        return axios.delete(`api/movimentacoes/internas/${movimentacaoInternaId}/itens/${itemId}`)
    }

    async buscarTodosItensParaSelecaoMovimentacaoInterna(filtros, paginacao, movimentacaoInternaId){
       paginacao.descending = paginacao.sortDesc[0]
        const url = AzSearchUrlBuilder.build(
            `api/movimentacoes/internas/${movimentacaoInternaId}/patrimoniosParaSelecao`,
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async adicionarItensMovimentacaoInterna(movimentacao){
        return axios.post(`api/movimentacoes/internas/${movimentacao.movimentacaoInternaId}/itens`, movimentacao)
    }

    async buscarQuantidadeItensAdicionadosMovimentacaoInterna(movimentacaoInternaId){
        return axios.get(`api/movimentacoes/internas/${movimentacaoInternaId}/itens/quantidade`)
    }

}
export default new MovimentacaoInternaItemApiClient()
