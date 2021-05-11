import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, body, mockResponseData, state

jest.mock('axios', () => ({
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
    patch(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({data: mockResponseData})
        })
    }
}))

describe('Actions', () => {
    const commit = jest.fn()
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

    it('ATUALIZAR_ENTRE_ESTOQUES', async () => {
        const entreEstoques = {id: 1, motivoObservacao: 'motivo'}
        await actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.ATUALIZAR_ENTRE_ESTOQUES]({commit}, entreEstoques)
        expect(url).toEqual(`api/movimentacoes/internas/entre-estoques/${entreEstoques.id}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, entreEstoques)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('BUSCAR_ENTRE_ESTOQUES_POR_ID', async () => {
        const entreEstoquesId = 1
        await actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.BUSCAR_ENTRE_ESTOQUES_POR_ID]({commit}, entreEstoquesId)
        expect(url).toEqual(`api/movimentacoes/internas/movimentacao/${entreEstoquesId}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, true)
    })

    it('FINALIZAR_ENTRE_ESTOQUES', async() => {
        const entreEstoquesId = 5

        const resposta = await actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES](context, entreEstoquesId)

        expect(resposta).toBeTruthy()
        expect(url).toBe(`api/movimentacoes/internas/entre-estoques/${entreEstoquesId}/finalizar`)
        expect(body).toBe(undefined)
    })
})
