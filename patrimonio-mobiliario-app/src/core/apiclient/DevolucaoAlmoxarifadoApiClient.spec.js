import MovimentacaoInternaDevolucaoAlmoxarifadoApiClient from './DevolucaoAlmoxarifadoApiClient'

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

describe('MovimentacaoInternaDevolucaoAlmoxarifadoApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('atualizar movimentação interna devolução almoxarifado', async () => {
        const devolucaoAlmoxarifado = {id: 1}
        const {data} = await MovimentacaoInternaDevolucaoAlmoxarifadoApiClient.atualizarMovimentacaoDevolucaoAlmoxarifado(devolucaoAlmoxarifado)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/movimentacoes/internas/devolucao-almoxarifado/${devolucaoAlmoxarifado.id}`)
    })

    it('buscar movimentação interna devolução almoxarifado por id', async () => {
        const devolucaoAlmoxarifadoId = 1
        const {data} = await MovimentacaoInternaDevolucaoAlmoxarifadoApiClient.buscarMovimentacaoDevolucaoAlmoxarifadoPorId(devolucaoAlmoxarifadoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacoes/internas/movimentacao/${devolucaoAlmoxarifadoId}`)
    })

    it('deve finalizar a movimentação do tipo devolução almoxarifado', async() => {
        const devolucaoAlmoxarifadoId = 7

        const {data} = await MovimentacaoInternaDevolucaoAlmoxarifadoApiClient.finalizarMovimentacaoDevolucaoAlmoxarifado(devolucaoAlmoxarifadoId)

        expect(verboHttp).toBe('patch')
        expect(data).toBeTruthy()
        expect(url).toBe(`api/movimentacoes/internas/devolucao-almoxarifado/${devolucaoAlmoxarifadoId}/finalizar`)
    })
})
