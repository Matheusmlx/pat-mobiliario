import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'
import UrlEncodeUtils from '../utils/UrlEncodeUtils'

class IncorporacaoApiClient {

    async buscarTodos(filtros, paginacao) {
        paginacao.descending = paginacao.sortDesc[0]
        filtros.conteudo.value = new UrlEncodeUtils(filtros.conteudo.value).encode()
        const url = AzSearchUrlBuilder.build(
            'api/patrimonios/incorporacao',
            filtros,
            paginacao
        )

        return axios.get(url)
    }

    async finalizar(incorporacaoId){
        return axios.patch(`api/patrimonios/incorporacao/finalizar/${incorporacaoId}`)
    }

    async cadastrar() {
        return axios.post('api/patrimonios/incorporacao')
    }

    async atualizar(incorporacao) {
        if (incorporacao.orgao != null && typeof incorporacao.orgao.id != 'undefined') {
            incorporacao.orgao = incorporacao.orgao.id
        }
        if (incorporacao.setor != null && typeof incorporacao.setor.id != 'undefined') {
            incorporacao.setor = incorporacao.setor.id
        }
        if (incorporacao.fornecedor != null && typeof incorporacao.fornecedor.id != 'undefined') {
            incorporacao.fornecedor = incorporacao.fornecedor.id
        }
        if (incorporacao.reconhecimento != null && typeof incorporacao.reconhecimento.id != 'undefined') {
            incorporacao.reconhecimento = incorporacao.reconhecimento.id
        }
        if (incorporacao.comodante != null && typeof incorporacao.comodante.id != 'undefined') {
            incorporacao.comodante = incorporacao.comodante.id
        }
        return axios.put(`api/patrimonios/incorporacao/${incorporacao.id}`, incorporacao)
    }

    async buscarPorId(incorporacaoId) {
        return axios.get(`api/patrimonios/incorporacao/${incorporacaoId}`)
    }

    async buscarRegistroPorId(incorporacaoId) {
        return axios.get(`api/patrimonios/incorporacao/${incorporacaoId}/visualizar`)
    }

    async deletarIncorporacao(incorporacaoId) {
        return axios.delete(`api/patrimonios/incorporacao/${incorporacaoId}`)
    }

    async reabrirIncorporacao(incorporacaoId) {
        return axios.patch(`api/patrimonios/incorporacao/reabrir/${incorporacaoId}`)
    }

    async cadastrarDocumento(documento) {
        return axios.post(`api/patrimonios/incorporacao/${documento.incorporacao}/documentos`, documento)
    }

    async atualizarDocumento(documento) {
        return axios.put(`api/patrimonios/incorporacao/${documento.incorporacao}/documentos/${documento.id}`, documento)
    }

    async buscarDocumentos(incorporacaoId) {
        return axios.get(`api/patrimonios/incorporacao/${incorporacaoId}/documentos`)
    }

    async uploadAnexoDocumento(formData){
        return axios.post('api/v1/arquivos',formData,{headers:{'Content-Type':'multipart/form-data'}})
    }

    async downloadAnexo(anexo){
        return axios.get('api/v1/arquivos',{params:{'uri':anexo}})
    }

    async buscarTipoDocumento() {
        return axios.get('api/tiposdocumentos?page=1&size=12')
    }

    async deletarDocumento(documento){
        return axios.delete(`api/patrimonios/incorporacao/${documento.incorporacao}/documentos/${documento.id}`)
    }

    async buscarSituacaoIncorporacao(incorporacaoId) {
        return axios.get(`api/patrimonios/incorporacao/${incorporacaoId}/situacao`)
    }
}

export default new IncorporacaoApiClient()
