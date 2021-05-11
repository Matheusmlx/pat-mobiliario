import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'
import UrlEncodeUtils from '../utils/UrlEncodeUtils'

class ReservaApiClient {

    async buscarTodos(filtros, paginacao) {
        if (filtros.conteudo) {
            filtros.conteudo.value = new UrlEncodeUtils(filtros.conteudo.value).encode()
        }

        const url = AzSearchUrlBuilder.build('api/configuracao/reservas', filtros, paginacao)
        return axios.get(url)
    }
}

export default new ReservaApiClient
