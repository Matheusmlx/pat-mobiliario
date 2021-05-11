import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'
import UrlEncodeUtils from '../utils/UrlEncodeUtils'

class ConvenioApiClient {
    constructor() {
        this.urlPath = 'api/convenios'
    }

    async buscarTodos(filtros, paginacao) {
        paginacao.descending = paginacao.sortDesc[0]

        if (filtros.conteudo) {
            filtros.conteudo.value = new UrlEncodeUtils(filtros.conteudo.value).encode()
        }

        const url = AzSearchUrlBuilder.build(
            this.urlPath,
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async salvar(dados) {
        return axios.post(this.urlPath, dados)
    }

    async editar(dados) {
        return axios.put(`${this.urlPath}/${dados.id}`, dados)
    }

    async buscarId(id) {
        return axios.get(`${this.urlPath}/${id}`)
    }

    async buscarConvenioDinamicamente(conteudo) {
        const paginacao = {
            page: 1,
            sortBy: ['nome'],
            sortDesc: [false],
            rowsPerPage: 100
        }
        const filtros = {
            conteudo: {
                value: new UrlEncodeUtils(conteudo).encode()
            },
            situacao: {
                value: 'ATIVO'
            }
        }
        const url = AzSearchUrlBuilder.build(this.urlPath, filtros, paginacao)
        return axios.get(url)
    }
}

export default new ConvenioApiClient()
