import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'
import UrlEncodeUtils from '../utils/UrlEncodeUtils'

class ReconhecimentoApiClient {

    async editarReconhecimento(reconhecimento) {
        return axios.put(`api/reconhecimentos/${reconhecimento.id}`, reconhecimento)
    }

    async buscarTodosReconhecimento(filtros, paginacao) {
        paginacao.descending = paginacao.sortDesc[0]
        if (filtros.conteudo) {
            filtros.conteudo.value = new UrlEncodeUtils(filtros.conteudo.value).encode()
        }
        const url = AzSearchUrlBuilder.build('api/reconhecimentos', filtros, paginacao)
        return axios.get(url)
    }

    async inserirReconhecimento(reconhecimento) {
        return axios.post('api/reconhecimentos', reconhecimento)
    }
}

export default new ReconhecimentoApiClient()
