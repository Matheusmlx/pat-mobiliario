import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, body, mockResponseData, returnedResponse

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
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    },
    delete(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    }
}))

describe('Actions', () => {
    const commit = jest.fn()
    const context = jest.fn()
    let state

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

    it('BUSCAR_TODOS_EMPENHOS', async () => {
        const incorporacao = {
            id: 5
        }
        await actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS](context, incorporacao.id)
        expect(url).toBe(`api/patrimonios/incorporacao/${incorporacao.id}/empenhos`)
    })

    it('INSERIR_EMPENHO', async() => {
        const empenho = {
            numero: '135153',
            data: '2020-07-15T23:00:00.000-0400',
            valor: '356500',
            statusCampos: true
        }

        await actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.INSERIR_EMPENHO]({commit}, empenho)
        expect(url).toBe('api/patrimonios/incorporacao/empenhos')
        expect(body).toBe(empenho)
    })

    it('ATUALIZAR_EMPENHO', async() => {
        const empenho = {
            id: 3,
            numero: '135153',
            data: '2020-07-15T23:00:00.000-0400',
            valor: '356500',
            statusCampos: true
        }

        await actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.ATUALIZAR_EMPENHO]({commit}, empenho)

        expect(url).toBe(`api/patrimonios/incorporacao/empenhos/${empenho.id}`)
        expect(body).toBe(empenho)
    })

    it('REMOVER_EMPENHO', async() => {
        const empenho = {
            id: 3,
            numero: '135153',
            data: '2020-07-15T23:00:00.000-0400',
            valor: '356500',
            statusCampos: true
        }

        await actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.REMOVER_EMPENHO]({commit}, empenho.id)

        expect(url).toBe(`api/patrimonios/incorporacao/empenhos/${empenho.id}`)
    })
})
