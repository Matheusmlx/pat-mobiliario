import RelatorioApiClient from './RelatorioApiClient'

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

describe('RelatorioApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('Deve realizar download do Termo de Responsabilidade', async () => {
        const movimentacaoId = 1
        const {data} = await RelatorioApiClient.baixarTermoDeResponsabilidade(movimentacaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacao/${movimentacaoId}/termo-guarda-responsabilidade`)
    })

    it('Deve gerar o memorando da Movimentação em elaboração', async () => {
        const movimentacaoId = 1
        const {data} = await RelatorioApiClient.baixarMemorandoMovimentacaoEmElaboracao(movimentacaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacao/${movimentacaoId}/memorando/em-elaboracao`)
    })

    it('Deve gerar o memorando da Movimentação finalizada', async () => {
        const movimentacaoId = 1
        const {data} = await RelatorioApiClient.baixarMemorandoMovimentacaoFinalizada(movimentacaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacao/${movimentacaoId}/memorando/finalizado`)
    })
})
