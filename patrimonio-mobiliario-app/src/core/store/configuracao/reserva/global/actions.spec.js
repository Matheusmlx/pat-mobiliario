import actions from './actions'
import {actionTypes} from '@/core/constants'

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
    },
    get(_url) {
        return new Promise(resolve => {

            url = _url
            resolve({data: mockResponseData})
        })
    },
    delete(_url) {
        return new Promise(resolve => {
            url = _url
            resolve({data:mockResponseData})
        })
    }
}))

describe('Actions', () => {
    const reserva = {
        id: 1,
        quantidadeReservada: '50',
        preenchimento: 'Automática'
    }

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
            todosOrgaos: {
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

    xit('Deve salvar a reserva', async () => {
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.SALVAR_RESERVA](context, reserva)
        expect(url).toBe('api/configuracao/reservas')
    })

    xit('Deve editar a reserva com sucesso e retornar a reserva editada', async () => {
        mockResponseData = {id: 1}
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.EDITAR_RESERVA](context, reserva)
        expect(url).toBe('api/configuracao/reservas/1')
        expect(returnedResponse).toEqual(mockResponseData)
    })

    xit('Deve retornar undefined quando editar a reserva com erro', async () => {
        mockResponseData = undefined
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.EDITAR_RESERVA](context, reserva)
        expect(url).toBe('api/configuracao/reservas/1')
        expect(returnedResponse).toEqual(mockResponseData)
    })

    xit('Deve buscar a reserva por id', async () => {
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID](context, reserva.id)
        expect(url).toBe('api/configuracao/reservas/1')
    })

    xit('Deve buscar o próximo número da reserva', async () => {
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_PROXIMO_NUMERO_DISPONIVEL]()
        expect(url).toBe('api/configuracao/reservas/proximoNumero')
    })

    xit('Deve buscar todos os órgãos em lista paginada', async () => {
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_TODOS_ORGAOS_PAGINADO]({state})
        expect(url).toBe('api/unidades-organizacionais/orgaos?page=1&size=10&sort=situacao&direction=ASC')
    })

    xit('Deve verificar se reserva possui numero utilizado', async () => {
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.VERIFICAR_RESERVA_POSSUI_NUMERO_UTILIZADO](context,reserva.id)
        expect(url).toBe(`api/configuracao/reservas/${reserva.id}/possuiNumerosUtilizados`)
    })

    xit('Deve deletar reserva', async () => {
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA](context,reserva.id)
        expect(url).toBe(`api/configuracao/reservas/${reserva.id}`)
    })

    xit('Deve finalizar reserva', async () => {
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA](context,reserva.id)
        expect(url).toBe(`api/configuracao/reservas/${reserva.id}/finalizar`)
    })
})
