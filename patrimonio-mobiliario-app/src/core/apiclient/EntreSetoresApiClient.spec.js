import MovimentacaoInternaDefinitivaApiClient from './EntreSetoresApiClient'

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

describe('MovimentacaoInternaDefinitivaApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('atualizar movimentação interna definitiva', async () => {
        const definitiva = {id: 1}
        const {data} = await MovimentacaoInternaDefinitivaApiClient.atualizarMovimentacaoDefinitiva(definitiva)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/movimentacoes/internas/definitiva/${definitiva.id}`)
    })

    it('buscar movimentação interna definitiva por id', async () => {
        const definitivaId = 1
        const {data} = await MovimentacaoInternaDefinitivaApiClient.buscarMovimentacaoDefinitivaPorId(definitivaId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacoes/internas/movimentacao/${definitivaId}`)
    })

    it('deve finalizar a movimentação do tipo definitiva', async() => {
        const movimentacaoDefinitivaId = 7

        const {data} = await MovimentacaoInternaDefinitivaApiClient.finalizarMovimentacaoDefinitiva(movimentacaoDefinitivaId)

        expect(verboHttp).toBe('patch')
        expect(data).toBeTruthy()
        expect(url).toBe(`api/movimentacoes/internas/definitiva/${movimentacaoDefinitivaId}/finalizar`)
    })
})
