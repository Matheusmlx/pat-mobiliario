import fusoHorarioAPiClient from './FusoHorarioApiClient'

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

describe('FusoHOrarioApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('buscarFusoHorario', async () => {
        const {data} = await fusoHorarioAPiClient.buscarFusoHorario()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/fuso-horario')
    })
})
