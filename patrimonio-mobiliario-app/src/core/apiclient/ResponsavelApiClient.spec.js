import ResponsavelApiClient from './ResponsavelApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({data: mockRetornoApi})
        })
    }
}))

describe('ResponsavelApiClient', () => {

    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('buscarTodosResponsaveis', async() => {
        const {data} = await ResponsavelApiClient.buscarTodosResponsaveis()

        expect(data).toBeTruthy()
        expect(url).toBe('api/responsavel')
        expect(verboHttp).toBe('get')
    })
})
