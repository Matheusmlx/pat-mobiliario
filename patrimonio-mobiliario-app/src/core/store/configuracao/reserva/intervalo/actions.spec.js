import actions from './actions'
import { actionTypes } from '@/core/constants'

let url, body, config, state, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    get(_url, _config) {
        return new Promise(resolve => {
            url = _url
            config = _config
            resolve({ data: mockResponseData })
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
            }
        }
    })

    it('Deve baixar termo de guarda', async () => {
        const reservaId = 2
        const reservaIntervaloId = 1
        window.URL.createObjectURL = jest.fn()

        await actions[actionTypes.CONFIGURACAO.RESERVA.INTERVALO.BAIXAR_TERMO_DE_GUARDA](context, {
            reservaId,
            reservaIntervaloId
        })
        expect(config).toEqual({responseType: 'arraybuffer'})
        expect(url).toBe(`api/configuracao/reservas/${reservaId}/intervalos/${reservaIntervaloId}/termoDeGuarda`)
    })
})
