import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'
import UrlEncodeUtils from '@/core/utils/UrlEncodeUtils'


class MovimentacaoInternaApiClient {

    async cadastrarMovimentacaoInterna(movimentacao) {
        return axios.post('api/movimentacoes/internas', movimentacao)
    }

    async buscarTipoMovimentacaoInterna(movimentacaoId) {
        return axios.get(`api/movimentacoes/internas/${movimentacaoId}/tipo`)
    }

    async removerMovimentacaoInternaCompleta(movimentacao) {
        return axios.delete(`api/movimentacoes/internas/${movimentacao}/completa`)
    }

    async removerMovimentacaoInternaVazia(movimentacaoId) {
        return axios.delete(`api/movimentacoes/internas/${movimentacaoId}/vazia`)
    }

    async editarTipoMovimentacaoInterna(movimentacao) {
        return axios.put(`api/movimentacoes/internas/${movimentacao.id}/tipo`, movimentacao)
    }

    async buscarTodasMovimentacoesInternas(filtros, paginacao) {
        paginacao.descending = paginacao.sortDesc[0]
        filtros.conteudo.value = new UrlEncodeUtils(filtros.conteudo.value).encode()
        const url = AzSearchUrlBuilder.build('api/movimentacoes/internas', filtros, paginacao)

        return axios.get(url)
    }

    async buscarSituacaoMovimentacaoInterna(movimentacaoId) {
        return axios.get(`api/movimentacoes/internas/${movimentacaoId}/situacao`)
    }
}

export default new MovimentacaoInternaApiClient
