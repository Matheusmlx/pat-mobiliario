import actions from './actions'
import {actionTypes} from '@/core/constants'

let url, body, state, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            url = _url
            resolve({data: mockResponseData})
        })
    }
}))

describe('Actions', () => {

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
            resultadoBuscaTodasReservasListagem: {
                filtros: {
                    conteudo: {
                        default: null,
                        label: 'Pesquisa',
                        value: ''
                    }
                },
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: [false],
                    sortBy: ['situacao']
                }
            }
        }
    })

    it('Deve buscar todas reservas', async () => {
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.BUSCAR_TODAS_RESERVAS_LISTAGEM]({state})
        expect(url).toBe('api/configuracao/reservas?page=1&size=10&sort=situacao&direction=ASC')
    })
})
