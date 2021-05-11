import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'
import UrlEncodeUtils from '../utils/UrlEncodeUtils'

class PatrimonioApiClient {

    async buscarPatrimonios(filtros, paginacao, itemIncorporacaoId) {
        paginacao.descending = paginacao.sortDesc[0]

        if (filtros && filtros.situacao) {
            filtros.situacao.value = new UrlEncodeUtils(filtros.situacao.value).encode()
        }

        const url = AzSearchUrlBuilder.build(
            `api/patrimonios/incorporacao/items/${itemIncorporacaoId}/patrimonios`,
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async buscarPatrimoniosListagem(filtros, paginacao) {
        paginacao.descending = paginacao.sortDesc[0]
        filtros.conteudo.value = new UrlEncodeUtils(filtros.conteudo.value).encode()
        const url = AzSearchUrlBuilder.build(
            'api/patrimonios/patrimonios',
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async buscarTodosPatrimoniosEstornados(filtros, paginacao, incorporacaoId) {
        paginacao.descending = paginacao.sortDesc[0]
        const url = AzSearchUrlBuilder.build(
            `api/patrimonios/incorporacao/${incorporacaoId}/patrimonios/estornados`,
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async atualizarPatrimonio(dados) {
        return axios.put(`api/patrimonios/incorporacao/items/patrimonios/${dados.id}`, dados)
    }

    async cadastrarPatrimonio(dados) {
        return axios.post('api/patrimonios/incorporacao/items/patrimonios', dados)
    }

    async buscarPatrimonioPorIdFicha(patrimonioId){
        return axios.get(`api/patrimonio/${patrimonioId}/ficha`)
    }

    async buscarTodasMovimentacoes(patrimonioId){
        return axios.get(`api/patrimonios/${patrimonioId}/movimentacoes`)
    }

    async buscarTodosPatrimoniosDeTodosItens(filtros, paginacao, incorporacaoId){
        paginacao.descending = paginacao.sortDesc[0]
        const url = AzSearchUrlBuilder.build(
            `api/patrimonios/incorporacao/${incorporacaoId}/patrimonios`,
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async estornarPatrimonios(dados) {
        return axios.patch('api/patrimonios/incorporacao/items/patrimonios/estornar', dados)
    }

    async buscarDepreciacoes(patrimonioId){
        return axios.get(`api/patrimonio/${patrimonioId}/depreciacoes`)
    }

}
export default new PatrimonioApiClient()
