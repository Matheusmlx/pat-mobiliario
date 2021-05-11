import MovimentacaoInternaApiClient from './MovimentacaoInternaApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
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
    },
    delete(_url) {
        return new Promise(resolve => {
            verboHttp = 'delete'
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
}))

describe('MovimentacaoInternaApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('cadastrar movimentação interna', async () => {
        const movimentacao ={tipo: 'DISTRIBUICAO'}
        const {data} = await MovimentacaoInternaApiClient.cadastrarMovimentacaoInterna(movimentacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/movimentacoes/internas')
    })

    it('buscar tipo da movimentação interna por id', async () => {
        const movimentacaoId = 1
        const {data} = await MovimentacaoInternaApiClient.buscarTipoMovimentacaoInterna(movimentacaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacaoId}/tipo`)
    })

    it('deve remover movimentação interna', async () => {
        const movimentacao = 5
        const {data} = await MovimentacaoInternaApiClient.removerMovimentacaoInternaCompleta(movimentacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacao}/completa`)
    })

    it('deve remover movimentação interna vazia', async () => {
        const movimentacao = 5
        const {data} = await MovimentacaoInternaApiClient.removerMovimentacaoInternaVazia(movimentacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacao}/vazia`)
    })

    it('deve editar tipo da movimentação interna', async () => {
        const movimentacao = {id:5, tipo: 'DISTRIBUICAO'}
        const {data} = await MovimentacaoInternaApiClient.editarTipoMovimentacaoInterna(movimentacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacao.id}/tipo`)
    })

    it('deve buscar todas as movimentações internas', async () => {
        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            sortBy: ['codigo'],
            defaultSortBy: ['codigo'],
            sortDesc: [false]
        }
        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: 'conteúdo'
            }
        }
        const { data } = await MovimentacaoInternaApiClient.buscarTodasMovimentacoesInternas(filtros, paginacao)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/movimentacoes/internas?conteudo=conteúdo&page=1&size=10&sort=codigo&direction=ASC')
    })

    it('deve buscar situação da movimentação interna por id', async () => {
        const movimentacaoId = 2
        const {data} = await MovimentacaoInternaApiClient.buscarSituacaoMovimentacaoInterna(movimentacaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacaoId}/situacao`)
    })
})
