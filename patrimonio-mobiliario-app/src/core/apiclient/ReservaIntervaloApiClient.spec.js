import reservaIntervaloApiClient from './ReservaIntervaloApiClient'

let url, config, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({data: mockRetornoApi})
        })
    }
}))

describe('ReservaIntervaloApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('Deve baixar termo de guarda', async () => {
        const reservaId = 2
        const intervaloId = 1
        const {data} = await reservaIntervaloApiClient.baixarTermoDeGuarda(reservaId, intervaloId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/configuracao/reservas/${reservaId}/intervalos/${intervaloId}/termoDeGuarda`)
    })
})
