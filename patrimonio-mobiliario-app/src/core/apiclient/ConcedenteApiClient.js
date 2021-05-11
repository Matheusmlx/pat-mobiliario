import axios from 'axios'
import {AzSearchUrlBuilder} from '@azinformatica/loki'
import UrlEncodeUtils from '../utils/UrlEncodeUtils'

class ConcedenteApiClient {
    constructor() {
        this.urlPath = 'api/concedentes'
    }

    async buscarTodos(filtros, paginacao) {
        paginacao.descending = paginacao.sortDesc[0]
        filtros.conteudo.value = new UrlEncodeUtils(filtros.conteudo.value).encode()
        const url = AzSearchUrlBuilder.build(
            this.urlPath,
            filtros,
            paginacao
        )
        return axios.get(url)
    }

    async buscarDinamicamente(conteudo) {
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
            situacao:{
                value:'ATIVO'
            }
        }
        const url = AzSearchUrlBuilder.build(this.urlPath, filtros, paginacao)
        return axios.get(url)
    }

    async buscarId(id) {
        return axios.get(`${this.urlPath}/${id}`)
    }

    async editar(dados) {
        return axios.put(`${this.urlPath}/${dados.id}`, dados)
    }

    async salvar(dados) {
        return axios.post(this.urlPath, dados)
    }
}

export default new ConcedenteApiClient()
