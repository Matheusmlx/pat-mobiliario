import ReservaPorOrgaoApiClient from './ReservaPorOrgaoApiClient'

let url, config, mockRetornoApi, verboHttp, params

jest.mock('axios', () => ({
    put(_url) {
        return new Promise(resolve => {
            verboHttp = 'put'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    post(_url, _params) {
        return new Promise(resolve => {
            verboHttp = 'post'
            url = _url
            params = _params
            resolve({data: mockRetornoApi})
        })
    }
}))

describe('ReservaIntervaloGlobalApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        config = ''
        params = ''
        mockRetornoApi = true
    })


    it('buscar próximo intervalo', async () => {
        const buscarIntervaloEntidade = {orgaoId: 2, quantidadeReservada: 3}

        const {data} = await ReservaPorOrgaoApiClient.buscarIntervalo(buscarIntervaloEntidade)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(params).toEqual(buscarIntervaloEntidade)
        expect(url).toEqual('api/configuracao/reservas/proximoIntervalo')
    })

    it('buscar próximo número', async () => {
        const orgaoId = 1
        const {data} = await ReservaPorOrgaoApiClient.buscarProximoNumero(orgaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(params).toEqual({orgaoId:orgaoId})
        expect(url).toEqual('api/configuracao/reservas/proximoNumero')
    })

    it('finalizar ', async () => {
        const reserva = {orgaoId: 1}
        const {data} = await ReservaPorOrgaoApiClient.finalizarReserva(reserva)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(params).toEqual(reserva)
        expect(url).toEqual('api/configuracao/reservas')
    })

})
