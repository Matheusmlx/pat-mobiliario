import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'
import UrlEncodeUtils from '../utils/UrlEncodeUtils'

class ItemIncorporacaoApiClient {

    async buscarTodos(filtros, paginacao, id_incorporacao) {
        paginacao.descending = paginacao.sortDesc[0]
        filtros.conteudo.value = new UrlEncodeUtils(filtros.conteudo.value).encode()
        const url = AzSearchUrlBuilder.build(
            `api/patrimonios/incorporacao/itens/${id_incorporacao}`,
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async deletar(dados) {
        return axios.delete(`api/patrimonios/incorporacao/${dados.idIncorporacao}/item/${dados.idItem}`)
    }

    async cadastrarItem(dados) {
        return axios.post(`api/patrimonios/incorporacao/${dados.idIncorporacao}/item`, dados)
    }

    async buscarItemIncorporacaoPorId(dados){
        return axios.get(`api/patrimonios/incorporacao/${dados.idIncorporacao}/item/${dados.idItem}`)
    }

    async buscarItemIncorporacaoVisualizacao(dados){
        return axios.get(`api/incorporacao/item/${dados.id}/visualizar`)
    }

    async uploadImagem(formData){
        return axios.post('api/v1/arquivos',formData,{headers:{'Content-Type':'multipart/form-data'}})
    }

    async downloadAnexo(anexo){
        return axios.get('api/v1/arquivos',{params:{'uri':anexo}})
    }

    async editarItemIncorporacao(item){
        return axios.put(`api/patrimonios/incorporacao/${item.idIncorporacao}/item/${item.id}`, item)
    }
}

export default new ItemIncorporacaoApiClient()
