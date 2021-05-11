import reservaIntervaloGlobalApiClient from './ReservaIntervaloGlobalApiClient'

let url, config, mockRetornoApi, verboHttp

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
    }
}))

describe('ReservaIntervaloGlobalApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    xit('Deve buscar todos intervalos da reserva', async () => {
        const reservaId = 1
        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            sortDesc: [false],
            sortBy: ['situacao'],
            defaultSortBy: ['situacao']
        }
        const {data} = await reservaIntervaloGlobalApiClient.buscarTodos(reservaId, paginacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/configuracao/reservas/1/intervalos?page=1&size=10&sort=situacao&direction=ASC')
    })

    xit('Deve exluir intervalo da reserva', async () => {
        const reservaId = 2
        const intervaloId = 1
        const {data} = await reservaIntervaloGlobalApiClient.excluirIntervalo(reservaId, intervaloId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual(`api/configuracao/reservas/${reservaId}/intervalos/${intervaloId}`)
    })

    xit('buscar próximo intervalo', async () => {
        const reserva = {
            reservaId: 1
        }
        const {data} = await reservaIntervaloGlobalApiClient.buscarIntervalo(reserva)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/configuracao/reservas/${reserva.reservaId}/intervalos/proximoDisponivel`, reserva)
    })

    xit('salvar intervalo', async () => {
        const intervalo = {
            reservaId: 1
        }
        const {data} = await reservaIntervaloGlobalApiClient.salvarIntervalo(intervalo)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/configuracao/reservas/${intervalo.reservaId}/intervalos`, intervalo)
    })

    xit('existe em elaboração', async () => {
        const reservaId = 1
        const {data} = await reservaIntervaloGlobalApiClient.existeEmElaboracao(reservaId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/configuracao/reservas/${reservaId}/intervalos/existe/EM_ELABORACAO`)
    })

    xit('buscar próximo número', async () => {
        const reservaId = 1
        const maiorNumeroFimIntervalo = 10
        const {data} = await reservaIntervaloGlobalApiClient.buscarProximoNumero(reservaId, maiorNumeroFimIntervalo)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/configuracao/reservas/${reservaId}/intervalos/proximoNumero`)
    })

    xit('validar intervalo', async () => {
        const reservaId = 1
        const numeroInicio = 10
        const numeroFim = 20
        const {data} = await reservaIntervaloGlobalApiClient.validarIntervalo(reservaId, numeroInicio, numeroFim)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/configuracao/reservas/${reservaId}/intervalos/validar`)
    })
})
