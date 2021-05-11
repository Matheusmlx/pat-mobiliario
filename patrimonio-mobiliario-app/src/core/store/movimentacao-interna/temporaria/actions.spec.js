import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, body, mockResponseData, state

jest.mock('axios', () => ({
    get(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    },
    put(_url, _body) {
        return new Promise((resolve) => {
            url = _url
            body = _body
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

    it('BUSCAR_TEMPORARIA_POR_ID', async () => {
        const temporariaId = 1
        await actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_TEMPORARIA_POR_ID]({commit}, temporariaId)
        expect(url).toEqual(`api/movimentacoes/internas/movimentacao/${temporariaId}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, true)
    })

    it('EDITAR_TEMPORARIA', async () => {
        const temporaria = {id: 1, orgaosOrigem: 2, setorOrigem: 2, setorDestino: 1}
        await actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ATUALIZAR_TEMPORARIA]({commit}, temporaria)
        expect(url).toEqual(`api/movimentacoes/internas/temporaria/${temporaria.id}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, temporaria)
    })

    it('ENVIAR_TEMPORARIA', async () => {
        const idMovimentacao = 2
        const resposta = await actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA]({commit}, idMovimentacao)
        expect(url).toBe(`api/movimentacoes/internas/temporaria/${idMovimentacao}/enviar`)
        expect(resposta).toBeTruthy()
        expect(body).toBeUndefined()
    })

    it('DEVOLVER_TODOS_PATRIMONIOS', async () => {
        const idMovimentacao = 2
        const resposta = await actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.DEVOLVER_TODOS_PATRIMONIOS]({commit}, idMovimentacao)
        expect(url).toBe(`api/movimentacoes/internas/temporaria/${idMovimentacao}/devolver-todos`)
        expect(resposta).toBeTruthy()
        expect(body).toBeUndefined()
    })

    it('BUSCAR_INFORMACAO_TOOLTIP', async () => {
        const temporariaId = 1
        const resposta = await actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_INFORMACAO_TOOLTIP]({commit}, temporariaId)
        expect(url).toBe(`api/movimentacoes/internas/temporaria/${temporariaId}/visualizacao`)
        expect(resposta).toBeTruthy()
        expect(body).toBeUndefined()
    })
})
