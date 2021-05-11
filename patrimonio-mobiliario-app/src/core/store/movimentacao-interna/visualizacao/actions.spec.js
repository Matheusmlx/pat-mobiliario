import actions from './actions'
import {actionTypes} from '@/core/constants'

let url, mockResponseData, body, state

jest.mock('axios', () => ({
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
    }
}))

describe('Actions', () => {

    const commit = jest.fn()
    const context = jest.fn()

    beforeEach(() => {
        mockResponseData = true

        state = {
            resultadoBuscaTodosItensMovimentacaoInternaVisualizacao: {
                filtros: {},
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: []
                }
            }
        }
    })

    it('ATUALIZAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO', async () => {
        const movimentacaoInterna = {
            id: 1,
            numeroNotaLancamentoContabil: '135354',
            dataNotaLancamentoContabil: '2021-01-14 15:36:38.186000'
        }

        const response = await actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.ATUALIZAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO]({commit}, movimentacaoInterna)

        expect(response).toBeTruthy()
        expect(body).toEqual(movimentacaoInterna)
        expect(url).toBe(`api/movimentacoes/internas/visualizacao/${movimentacaoInterna.id}`)
    })

    it('BUSCAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO', async() => {
        const movimentacaoInternaId = 8

        const response = await actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO](context, movimentacaoInternaId)

        expect(response).toBeTruthy()
        expect(url).toBe(`api/movimentacoes/internas/visualizacao/${movimentacaoInternaId}`)
    })

    it('BUSCAR_TODOS_ITENS_ADICIONADOS_VISUALIZACAO_REGISTRO', async() => {
        const movimentacaoInternaId = 14

        const response = await actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_TODOS_ITENS_ADICIONADOS_VISUALIZACAO_REGISTRO]({state}, movimentacaoInternaId)

        expect(response).toBeTruthy()
        expect(url).toBe(`api/movimentacoes/internas/${movimentacaoInternaId}/itens?page=1&size=10`)
    })

})
