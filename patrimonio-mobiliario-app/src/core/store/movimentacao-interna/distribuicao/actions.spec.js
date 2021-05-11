import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, body, mockResponseData, state

jest.mock('axios', () => ({
    post(_url, _body) {
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
    put(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({data: mockResponseData})
        })
    },
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

    it('ATUALIZAR_DISTRIBUICAO', async () => {
        const distribuicao ={id: 1, motivoObservacao: 'motivo'}
        await actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.ATUALIZAR_DISTRIBUICAO]({commit}, distribuicao)
        expect(url).toEqual(`api/movimentacoes/internas/distribuicao/${distribuicao.id}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, distribuicao)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('BUSCAR_DISTRIBUICAO_POR_ID', async () => {
        const distribuicaoId = 1
        await actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.BUSCAR_DISTRIBUICAO_POR_ID]({commit}, distribuicaoId)
        expect(url).toEqual(`api/movimentacoes/internas/movimentacao/${distribuicaoId}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, true)
    })

    it('FINALIZAR_DISTRIBUICAO', async () => {
        const distribuicaoId = 1
        mockResponseData = {id: distribuicaoId, situacao: 'FINALIZADO'}
        const response = await actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO]({commit}, distribuicaoId)
        expect(url).toEqual(`api/movimentacoes/internas/distribuicao/${distribuicaoId}/finalizar`)
        expect(response).toEqual(mockResponseData)
    })
})
