import ReconhecimentoApiClient from './ReconhecimentoApiClient'

let url, config, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    put(_url) {
        return new Promise(resolve => {
            verboHttp = 'put'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    post(_url) {
        return new Promise(resolve => {
            verboHttp = 'post'
            url = _url
            resolve({data: mockRetornoApi})
        })
    }
}))

describe('ReconhecimentoApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })


    it('salvar', async () => {
        const reconhecimento = {nome: 'Compra', situacao: 'ATIVO', execucaoOrcamentaria: true}
        const {data} = await ReconhecimentoApiClient.inserirReconhecimento(reconhecimento)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/reconhecimentos', reconhecimento)
    })

    it('buscar', async () => {
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
        const {data} = await ReconhecimentoApiClient.buscarTodosReconhecimento(filtros, paginacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/reconhecimentos?page=1&size=10&sort=situacao&direction=ASC')
    })

    it('editar', async () => {
        const reconhecimento = {id: 1, nome: 'Compra', situacao: 'ATIVO', execucaoOrcamentaria: true}
        const {data} = await ReconhecimentoApiClient.editarReconhecimento(reconhecimento)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/reconhecimentos/${reconhecimento.id}`, reconhecimento)
    })
})
