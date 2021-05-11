import MovimentacaoInternaDistribuicaoApiClient from './DistribuicaoApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    post(_url) {
        return new Promise(resolve => {
            verboHttp = 'post'
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
    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
}))

describe('MovimentacaoInternaDistribuicaoApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('atualizar movimentação interna distribuição', async () => {
        const distribuicao ={id: 1}
        const {data} = await MovimentacaoInternaDistribuicaoApiClient.atualizarMovimentacaoDistribuicao(distribuicao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/movimentacoes/internas/distribuicao/${distribuicao.id}`)
    })

    it('buscar movimentação interna distribuição por id', async () => {
        const distribuicaoId = 1
        const {data} = await MovimentacaoInternaDistribuicaoApiClient.buscarMovimentacaoDistribuicaoPorId(distribuicaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacoes/internas/movimentacao/${distribuicaoId}`)
    })

    it('finalizar movimentação interna distribuição', async () => {
        const distribuicaoId = 1
        const {data} = await MovimentacaoInternaDistribuicaoApiClient.finalizarMovimentacaoDistribuicaoPorId(distribuicaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/movimentacoes/internas/distribuicao/${distribuicaoId}/finalizar`)
    })
})
