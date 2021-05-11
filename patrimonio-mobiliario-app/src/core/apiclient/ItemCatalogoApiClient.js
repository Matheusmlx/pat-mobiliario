import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'
import UrlEncodeUtils from '../utils/UrlEncodeUtils'

class ItemCatalogoApiClient {

    async buscarTodos(filtros, paginacao) {
        paginacao.descending = paginacao.sortDesc[0]
        filtros.conteudo.value = new UrlEncodeUtils(filtros.conteudo.value).encode()
        const url = AzSearchUrlBuilder.build(
            'api/catalogo/itens',
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async buscarPorId(id) {
        return axios.get(`api/catalogo/itens/${id}`)
    }
}

export default new ItemCatalogoApiClient()
