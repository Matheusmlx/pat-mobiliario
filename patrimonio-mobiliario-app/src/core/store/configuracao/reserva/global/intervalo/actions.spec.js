import actions from './actions'
import { actionTypes, mutationTypes } from '@/core/constants'

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
            resolve({ data: mockResponseData })
        })
    },
    delete(_url) {
        return new Promise(resolve => {
            url = _url
            resolve()
        })
    }
}))

describe('Actions', () => {

    const context = jest.fn()
    const commit = jest.fn()

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
            resultadoBuscaTodosIntervalosListagem: {
                itens: [],
                totalElements: 0,
                totalPages: 0,
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: [false],
                    sortBy: ['situacao']
                }
            },
            existeEmElaboracao: false
        }
    })

    xit('Deve buscar o próximo intervalo', async () => {
        const reserva = {
            reservaId: 1
        }
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_INTERVALO](context, reserva)
        expect(url).toBe(`api/configuracao/reservas/${reserva.reservaId}/intervalos/proximoDisponivel`, reserva)
    })

    xit('Deve salvar intervalo', async () => {
        const intervalo = {
            reservaId: 1
        }
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SALVAR_INTERVALO](context, intervalo)
        expect(url).toBe(`api/configuracao/reservas/${intervalo.reservaId}/intervalos`, intervalo)
    })

    xit('Deve buscar todos intervalos da reserva e atualizar o estado', async () => {
        const reservaId = 1
        mockResponseData = {itens: [], totalElements: 0, totalPages: 0}
        await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM]({ state, commit }, reservaId)

        expect(url).toBe('api/configuracao/reservas/1/intervalos?page=1&size=10&sort=situacao&direction=ASC')
        expect(commit.mock.calls[0]).toEqual([
            mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_RESULTADO_BUSCA_TODOS_INTERVALOS,
            mockResponseData
        ])
    })

    xit('Deve excluir intervalo da reserva', async () => {
        const reservaId = 2
        const intervaloId = 1
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.REMOVER_INTERVALO_POR_ID]({ state }, {
            reservaId,
            intervaloId
        })
        expect(url).toBe(`api/configuracao/reservas/${reservaId}/intervalos/${intervaloId}`)
    })

    xit('Deve atualizar o estado existeEmElaboracao', async () => {
        const reservaId = 1
        mockResponseData = {existe: true}

        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO]({ commit }, reservaId)
        expect(url).toBe(`api/configuracao/reservas/${reservaId}/intervalos/existe/EM_ELABORACAO`)
        expect(commit).toHaveBeenCalled()
        expect(commit.mock.calls[0][1]).toEqual(mockResponseData.existe)
    })

    xit('Deve buscar pŕoximo número', async () => {
        const reservaId = 1
        const maiorNumeroFimIntervalo = 10
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_PROXIMO_NUMERO](context, {
            reservaId, maiorNumeroFimIntervalo
        })
        expect(url).toBe(`api/configuracao/reservas/${reservaId}/intervalos/proximoNumero`)
    })

    xit('Deve validar intervalo', async () => {
        const reservaId = 1
        const numeroInicio = 10
        const numeroFim = 20
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.VALIDAR_INTERVALO](context, {
            reservaId, numeroInicio, numeroFim
        })
        expect(url).toBe(`api/configuracao/reservas/${reservaId}/intervalos/validar`)
    })
})
