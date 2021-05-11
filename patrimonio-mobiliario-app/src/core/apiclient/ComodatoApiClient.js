import UrlEncodeUtils from '@/core/utils/UrlEncodeUtils'
import {AzSearchUrlBuilder} from '@azinformatica/loki'
import axios from 'axios'

class ComodatoApiClient {
    constructor() {
        this.url = 'api/comodantes'
    }

    async buscarComodanteDinamicamente(conteudo) {
        const paginacao = {
            page: 1,
            rowsPerPage: 100
        }
        const filtros = {
            conteudo: {
                value: new UrlEncodeUtils(conteudo).encode()
            }
        }
        const url = AzSearchUrlBuilder.build(this.url, filtros, paginacao)
        return axios.get(url)
    }
}

export default new ComodatoApiClient()
