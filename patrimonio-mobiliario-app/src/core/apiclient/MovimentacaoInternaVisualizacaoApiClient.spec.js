import MovimentacaoInternaVisualizacaoApiClient from './MovimentacaoInternaVisualizacaoApiClient'

let url, mockRetornoApi, verboHttp

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
    }
}))

describe('MovimentacaoInternaVisualizacaoApiClient', () => {

    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('atualizarMovimentacaoInterna', async() => {
        const movimentacao = {
            id: 5,
            numeroNotaLancamentoContabil: '5555',
            dataNotaLancamentoContabil: '2021-01-14 15:36:38.186000'
        }

        const {data} = await MovimentacaoInternaVisualizacaoApiClient.atualizarMovimentacaoInterna(movimentacao)

        expect(data).toBeTruthy()
        expect(url).toBe('api/movimentacoes/internas/visualizacao/5')
        expect(verboHttp).toBe('put')
    })

    it('buscarMovimentacaoInterna', async() => {
        const {data} = await MovimentacaoInternaVisualizacaoApiClient.buscarMovimentacaoInterna(5)

        expect(data).toBeTruthy()
        expect(url).toBe('api/movimentacoes/internas/visualizacao/5')
        expect(verboHttp).toBe('get')
    })
})
