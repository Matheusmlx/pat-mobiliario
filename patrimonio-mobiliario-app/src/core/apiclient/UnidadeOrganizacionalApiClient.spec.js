import UnidadeOrganizacionalApiClient from './UnidadeOrganizacionalApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({data: mockRetornoApi})
        })
    }
}))

describe('UnidadeOrganizacionalApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('buscarTodosOrgaos',async()=>{
        const {data} = await UnidadeOrganizacionalApiClient.buscarTodosOrgaos()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/unidades-organizacionais?sort=sigla&direction=ASC')
    })

    it('buscarTodosSetoresAlmoxarifado', async () => {
        const orgaoId = 1
        const {data} = await UnidadeOrganizacionalApiClient.buscarTodosSetoresAlmoxarifado(orgaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/unidades-organizacionais/${orgaoId}/setores-almoxarifado`)
    })

    it('buscarTodosSetoresNaoAlmoxarifado', async () => {
        const codigoHierarquiaOrgao = 1
        const {data} = await UnidadeOrganizacionalApiClient.buscarTodosSetoresNaoAlmoxarifado(codigoHierarquiaOrgao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/unidades-organizacionais/${codigoHierarquiaOrgao}/setores-nao-almoxarifado`)
    })

    it('buscarFundos', async () => {
        const orgaoId = 139
        const {data} = await UnidadeOrganizacionalApiClient.buscarFundos(orgaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/unidades-organizacionais/${orgaoId}/fundos`)
    })

    it('buscarSetoresPossuiAcesso',async()=>{
        const {data} = await UnidadeOrganizacionalApiClient.buscarSetoresPossuiAcesso()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/unidades-organizacionais/setores?sort=sigla&direction=ASC')
    })

    it('buscarTodosOrgaoPaginado', async () => {
        const filtros = {
            conteudo: {
                value: null,
                default: null,
                label: 'Pesquisa'
            }
        }
        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            descending: false,
            sortBy: ['situacao'],
            sortDesc: [false]
        }
        const {data} = await UnidadeOrganizacionalApiClient.buscarTodosOrgaosPaginado(filtros, paginacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/unidades-organizacionais/orgaos?page=1&size=10&sort=situacao&direction=ASC')
    })
})
