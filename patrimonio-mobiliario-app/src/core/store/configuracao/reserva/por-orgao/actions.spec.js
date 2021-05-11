import parametroState from '@/core/store/configuracao/parametros/state'
parametroState.parametros.reservaPatrimonialGlobal = false
import actions from './actions'
import { actionTypes } from '@/core/constants'

let url, body, state, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    post(_url, _body) {
        return new Promise((resolve) => {
            url = _url
            body = _body
            resolve({data: mockResponseData})
        })
    },
    put(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({data: mockResponseData})
        })
    }
}))

describe('Actions', () => {

    const context = jest.fn()

    beforeEach(() => {
        url = ''
        body = undefined
        mockResponseData = true
        state = {
            loki: {
                product: {
                    id: 401,
                    name: 'patrimonio-mobiliario'
                }
            },
            parametros: {
                parametros: {
                    reservaPatrimonialGlobal: false
                }
            }
        }
    })

    it('Deve buscar o próximo intervalo', async () => {
        const buscarIntervaloEntidade = {orgaoId: 2, quantidadeReservada: 3}
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_INTERVALO_POR_ORGAO](context, buscarIntervaloEntidade)
        expect(url).toBe('api/configuracao/reservas/proximoIntervalo', buscarIntervaloEntidade)
    })

    it('Deve buscar pŕoximo número', async () => {
        const maiorNumeroFimIntervalo = 10
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_PROXIMO_NUMERO_DISPONIVEL_POR_ORGAO](context, {
            maiorNumeroFimIntervalo
        })
        expect(url).toBe('api/configuracao/reservas/proximoNumero')
    })

    it('Deve finalizar reserva', async () => {
        const reserva = {orgaoId: 1}
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.FINALIZAR_RESERVA_POR_ORGAO](context,reserva)
        expect(url).toBe('api/configuracao/reservas', reserva)
    })

})
