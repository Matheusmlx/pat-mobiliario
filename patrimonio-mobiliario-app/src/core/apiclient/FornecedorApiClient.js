import axios from 'axios'
import { AzSearchUrlBuilder } from '@azinformatica/loki'
import UrlEncodeUtils from '../utils/UrlEncodeUtils'

class FornecedorApiClient {
    async buscarPorCNPJ(conteudo) {
        const paginacao = {
            page: 1,
            rowsPerPage: 100
        }
        const filtros = {
            conteudo: {
                value: new UrlEncodeUtils(conteudo).encode()
            }
        }
        const url = AzSearchUrlBuilder.build('api/fornecedores', filtros, paginacao)
        return axios.get(url)
    }

    async buscarPorId(id) {
        return axios.get(`api/fornecedores/${id}`)
    }
}

export default new FornecedorApiClient()
