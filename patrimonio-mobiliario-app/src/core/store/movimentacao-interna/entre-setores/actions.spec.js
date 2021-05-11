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

    it('ATUALIZAR_ENTRE_SETORES', async () => {
        const entreSetores = {id: 1, motivoObservacao: 'motivo'}
        await actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.ATUALIZAR_ENTRE_SETORES]({commit}, entreSetores)
        expect(url).toEqual(`api/movimentacoes/internas/definitiva/${entreSetores.id}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, entreSetores)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('BUSCAR_ENTRE_SETORES_POR_ID', async () => {
        const entreSetoresId = 1
        await actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.BUSCAR_ENTRE_SETORES_POR_ID]({commit}, entreSetoresId)
        expect(url).toEqual(`api/movimentacoes/internas/movimentacao/${entreSetoresId}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, true)
    })

    it('FINALIZAR_ENTRE_SETORES', async() => {
        const entreSetoresId = 5

        const resposta = await actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES](context, entreSetoresId)

        expect(url).toBe(`api/movimentacoes/internas/definitiva/${entreSetoresId}/finalizar`)
        expect(resposta).toBeTruthy()
        expect(body).toBeUndefined()
    })
})
