import reservaGlobalApiClient from './ReservaGlobalApiClient'

let url, config, mockRetornoApi, verboHttp

jest.mock('axios', ()=>({
    get(_url){
        return new Promise(resolve =>{
            verboHttp = 'get'
            url = _url
            resolve({data:mockRetornoApi})
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
            resolve({data:mockRetornoApi})
        })
    }
}))

const reserva = {
    id: 1,
    quantidadeReservada: '50',
    preenchimento: 'Automática'
}

describe('ReservaApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    xit('buscar por id', async () => {
        const {data} = await reservaGlobalApiClient.buscarReservaPorId(reserva.id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/configuracao/reservas/1')
    })

    xit('salvar', async () => {
        const {data} = await reservaGlobalApiClient.salvarReserva(reserva)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/configuracao/reservas', reserva)
    })

    xit('editar', async () => {
        const {data} = await reservaGlobalApiClient.editarReserva(reserva)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual('api/configuracao/reservas/1', reserva)
    })

    xit('buscar próximo número disponível', async () => {
        const {data} = await reservaGlobalApiClient.buscarProximoNumeroDisponivel()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/configuracao/reservas/proximoNumero')
    })

    xit('finalizar reserva global', async () => {
        const {data} = await reservaGlobalApiClient.finalizarReserva(reserva.id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/configuracao/reservas/${reserva.id}/finalizar`)
    })

    xit('Verificar se reserva possui numeros utilizados', async () => {
        let idReserva = 1
        const {data} = await reservaGlobalApiClient.buscarNumeroReservaUtilizado(idReserva)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/configuracao/reservas/${idReserva}/possuiNumerosUtilizados`)
    })

    xit('Remover reserva', async () =>{
        const {data} = await reservaGlobalApiClient.deletarReserva(reserva.id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual(`api/configuracao/reservas/${reserva.id}`)
    })
})
