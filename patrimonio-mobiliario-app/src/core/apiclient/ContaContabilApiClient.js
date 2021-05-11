import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'
import UrlEncodeUtils from '../utils/UrlEncodeUtils'

class ContaContabilApiClient {
    async buscarPorId(id) {
        return axios.get(`api/configuracao/contas-contabeis/${id}`)
    }

    async buscarTodos(filtros, paginacao) {
        if (filtros.conteudo) {
            filtros.conteudo.value = new UrlEncodeUtils(filtros.conteudo.value).encode()
        }

        const url = AzSearchUrlBuilder.build('api/configuracao/contas-contabeis', filtros, paginacao)
        return axios.get(url)
    }

    async salvar(dados) {
        return axios.post(`api/configuracao/contas-contabeis/${dados.id}/config-depreciacao`, dados)
    }

    async editar(dados) {
        return axios.post(`api/configuracao/contas-contabeis/${dados.id}/config-depreciacao/${dados.id}`, dados)
    }
}

export default new ContaContabilApiClient
