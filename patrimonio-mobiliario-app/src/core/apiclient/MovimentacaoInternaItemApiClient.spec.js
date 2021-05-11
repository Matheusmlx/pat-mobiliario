import MovimentacaoInternaItemApiClient from './MovimentacaoInternaItemApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    delete(_url) {
        return new Promise(resolve => {
            verboHttp = 'delete'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    post(_url){
        return new Promise(resolve =>{
            verboHttp = 'post'
            url = _url
            resolve({data:mockRetornoApi})
        })
    },
}))

describe('MovimentacaoInternaItemApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('buscarTodosItensAdicionadosMovimentacaoInterna', async () => {
        const filtros = {}
        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            sortDesc: []
        }
        const movimentacaoInternaId =  1

        const {data} = await MovimentacaoInternaItemApiClient.buscarTodosItensAdicionadosMovimentacaoInterna(filtros, paginacao, movimentacaoInternaId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacaoInternaId}/itens?page=1&size=10`)
    })

    it('deletarItemMovimentacaoInterna', async () => {
        const movimentacaoInternaId =  1
        const itemId = 2

        const {data} = await MovimentacaoInternaItemApiClient.deletarItemMovimentacaoInterna(movimentacaoInternaId, itemId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacaoInternaId}/itens/${itemId}`)
    })

    it('buscarTodosItensParaSelecaoMovimentacaoInterna', async () => {
        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: '123'
            }
        }
        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            sortBy: ['numero'],
            defaultSortBy: ['numero'],
            sortDesc: [true]
        }
        const movimentacaoInternaId =  1

        const {data} = await MovimentacaoInternaItemApiClient.buscarTodosItensParaSelecaoMovimentacaoInterna(filtros, paginacao, movimentacaoInternaId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacaoInternaId}/patrimoniosParaSelecao?conteudo=123&page=1&size=10&sort=numero&direction=DESC`)
    })

    it('adicionarItensMovimentacaoInterna', async () => {
        const movimentacao = {
            movimentacaoInternaId: 1
        }
        const {data} = await MovimentacaoInternaItemApiClient.adicionarItensMovimentacaoInterna(movimentacao)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacao.movimentacaoInternaId}/itens`)
    })

    it('buscarQuantidadeItensAdicionadosMovimentacaoInterna', async () => {
        const movimentacaoInternaId = 1
        const {data} = await MovimentacaoInternaItemApiClient.buscarQuantidadeItensAdicionadosMovimentacaoInterna(movimentacaoInternaId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacaoInternaId}/itens/quantidade`)
    })
})
