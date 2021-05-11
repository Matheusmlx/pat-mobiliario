import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, body, mockResponseData, state

jest.mock('axios', () => ({
    post(_url, _body) {
        return new Promise((resolve) => {
            url = _url
            body = _body
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
    get(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    },
    delete(_url) {
        return new Promise(resolve => {
            url = _url
            resolve({data: mockResponseData})
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
            }
        }
    })

    it('CADASTRAR_MOVIMENTACAO_INTERNA', async () => {
        const movimentacao ={tipo: 'DISTRIBUICAO'}
        await actions[actionTypes.MOVIMENTACAO_INTERNA.CADASTRAR_MOVIMENTACAO_INTERNA]({commit}, movimentacao)
        expect(url).toEqual('api/movimentacoes/internas')
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, true)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('BUSCAR_TIPO_MOVIMENTACAO_INTERNA', async () => {
        const movimentacaoId = 1
        await actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA](context, movimentacaoId)
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacaoId}/tipo`)
    })

    it('REMOVER_MOVIMENTACAO_INTERNA_COMPLETA', async () => {
        const movimentacaoId = 7
        await actions[actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_COMPLETA]({commit}, movimentacaoId)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, {})

        expect(url).toEqual(`api/movimentacoes/internas/${movimentacaoId}/completa`)
    })

    it('REMOVER_MOVIMENTACAO_INTERNA_VAZIA', async () => {
        const movimentacaoId = 7
        await actions[actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_VAZIA]({commit}, movimentacaoId)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, {})

        expect(url).toEqual(`api/movimentacoes/internas/${movimentacaoId}/vazia`)
    })

    it('EDITAR_TIPO_MOVIMENTACAO_INTERNA', async () => {
        const movimentacao = {id:1, tipo: 'DISTRIBUICAO'}
        await actions[actionTypes.MOVIMENTACAO_INTERNA.EDITAR_TIPO_MOVIMENTACAO_INTERNA](context, movimentacao)
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacao.id}/tipo`)
        expect(body).toEqual(movimentacao)
    })

    it('BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA', async () => {
        const movimentacaoId = 2
        await actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA](context, movimentacaoId)
        expect(url).toEqual(`api/movimentacoes/internas/${movimentacaoId}/situacao`)
    })

})
