import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, body, mockResponseData, state

jest.mock('axios', () => ({
    patch(_url, _body) {
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

    it('ATUALIZAR_DEVOLUCAO_ALMOXARIFADO', async () => {
        const devolucaoAlmoxarifado ={id: 1, motivoObservacao: 'motivo'}
        await actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.ATUALIZAR_DEVOLUCAO_ALMOXARIFADO]({commit}, devolucaoAlmoxarifado)
        expect(url).toEqual(`api/movimentacoes/internas/devolucao-almoxarifado/${devolucaoAlmoxarifado.id}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, devolucaoAlmoxarifado)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('BUSCAR_DEVOLUCAO_ALMOXARIFADO_POR_ID', async () => {
        const devolucaoAlmoxarifadoId = 1
        await actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.BUSCAR_DEVOLUCAO_ALMOXARIFADO_POR_ID]({commit}, devolucaoAlmoxarifadoId)
        expect(url).toEqual(`api/movimentacoes/internas/movimentacao/${devolucaoAlmoxarifadoId}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA, true)
    })


    it('FINALIZAR_DEVOLUCAO_ALMOXARIFADO', async () => {
        const devolucaoAlmoxarifadoId = 1
        mockResponseData = {id: devolucaoAlmoxarifadoId, situacao: 'FINALIZADO'}
        const response = await actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO]({commit}, devolucaoAlmoxarifadoId)
        expect(url).toEqual(`api/movimentacoes/internas/devolucao-almoxarifado/${devolucaoAlmoxarifadoId}/finalizar`)
        expect(response).toEqual(mockResponseData)
    })
})
