import axios from 'axios'
import UrlEncodeUtils from '../utils/UrlEncodeUtils'
import {AzSearchUrlBuilder} from '@azinformatica/loki'

class UnidadeOrganizacionalApiClient {

    async buscarTodosOrgaos() {
        return axios.get('api/unidades-organizacionais?sort=sigla&direction=ASC')
    }

    async buscarTodosSetoresAlmoxarifado(orgaoId) {
        return axios.get(`api/unidades-organizacionais/${orgaoId}/setores-almoxarifado`)
    }

    async buscarTodosSetoresNaoAlmoxarifado(orgaoId) {
        return axios.get(`api/unidades-organizacionais/${orgaoId}/setores-nao-almoxarifado`)
    }

    async buscarSetoresPossuiAcesso() {
        return axios.get('api/unidades-organizacionais/setores?sort=sigla&direction=ASC')
    }

    async buscarFundos(orgaoId) {
        return axios.get(`api/unidades-organizacionais/${orgaoId}/fundos`)
    }

    async buscarTodosOrgaosPaginado(filtros, paginacao) {
        if (filtros.conteudo) {
            filtros.conteudo.value = new UrlEncodeUtils(filtros.conteudo.value).encode()
        }
        const url = AzSearchUrlBuilder.build('api/unidades-organizacionais/orgaos', filtros, paginacao)
        return axios.get(url)
    }
}

export default new UnidadeOrganizacionalApiClient()
