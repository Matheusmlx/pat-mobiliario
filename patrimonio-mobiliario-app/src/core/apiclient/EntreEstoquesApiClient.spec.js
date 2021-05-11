import MovimentacaoInternaEntreEstoquesApiClient from './EntreEstoquesApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
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
    patch(_url) {
        return new Promise(resolve => {
            verboHttp = 'patch'
            url = _url
            resolve({data: mockRetornoApi})
        })
    }
}))

describe('MovimentacaoInternaEntreEstoquesApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('atualizar movimentação interna entre estoques', async () => {
        const entreEstoques = {id: 1}
        const {data} = await MovimentacaoInternaEntreEstoquesApiClient.atualizarMovimentacaoEntreEstoques(entreEstoques)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/movimentacoes/internas/entre-estoques/${entreEstoques.id}`)
    })

    it('buscar movimentação interna entreEstoques por id', async () => {
        const entreEstoquesId = 1
        const {data} = await MovimentacaoInternaEntreEstoquesApiClient.buscarMovimentacaoEntreEstoquesPorId(entreEstoquesId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacoes/internas/movimentacao/${entreEstoquesId}`)
    })

    it('deve finalizar movimentação interna do tipo entre estoques', async() => {
        const entreEstoquesId = 2
        const {data} = await MovimentacaoInternaEntreEstoquesApiClient.finalizarMovimentacaoEntreEstoques(entreEstoquesId)

        expect(data).toBeTruthy()
        expect(verboHttp).toBe('patch')
        expect(url).toBe(`api/movimentacoes/internas/entre-estoques/${entreEstoquesId}/finalizar`)
    })
})
