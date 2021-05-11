import EstadoConservacaoApiClient from './EstadoConservacaoApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('EstadoConservacaoApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('Deve buscar todos estados de conservação', async() => {
        const {data} = await EstadoConservacaoApiClient.buscarTodos()

        expect(data).toBeTruthy()
        expect(verboHttp).toBe('get')
        expect(url).toBe('api/patrimonios/incorporacao/itens/estadosconservacao')
        expect(mockRetornoApi).toBeTruthy()
    })
})

