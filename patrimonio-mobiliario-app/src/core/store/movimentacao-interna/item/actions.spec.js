import actions from './actions'
import {actionTypes} from '@/core/constants'

let url, body, mockResponseData, returnedResponse, state

jest.mock('axios', () => ({
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
    },
    post(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
}))

describe('Actions', () => {
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
            resultadoBuscaTodosItensAdicionadosMovimentacaoInterna: {
                filtros: {},
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: []
                }
            },
            resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna: {
                filtros: {
                    conteudo: {
                        default: null,
                        label: 'Pesquisa',
                        value: '123'
                    }
                },
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: [false],
                    sortBy: ['numero'],
                    defaultSortBy: ['numero']
                }
            }
        }

    })

    it('BUSCAR_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA', async () => {
        const movimentacaoInternaId = 1
        returnedResponse = await actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]({state}, movimentacaoInternaId)
        expect(url).toBe(`api/movimentacoes/internas/${movimentacaoInternaId}/itens?page=1&size=10`)
    })

    it('DELETAR_ITEM_MOVIMENTACAO_INTERNA', async () => {
        const data = {movimentacaoInternaId: 1, itemId: 2}
        returnedResponse = await actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.DELETAR_ITEM_MOVIMENTACAO_INTERNA](context, data)
        expect(url).toBe(`api/movimentacoes/internas/${data.movimentacaoInternaId}/itens/${data.itemId}`)
    })

    it('BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA', async () => {
        const movimentacaoInternaId = 1
        returnedResponse = await actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]({state}, movimentacaoInternaId)
        expect(url).toBe(`api/movimentacoes/internas/${movimentacaoInternaId}/patrimoniosParaSelecao?conteudo=123&page=1&size=10&sort=numero&direction=ASC`)
    })

    it('ADICIONAR_ITENS_MOVIMENTACAO_INTERNA', async () => {
        const movimentcao = {movimentacaoInternaId: 1}
        returnedResponse = await actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.ADICIONAR_ITENS_MOVIMENTACAO_INTERNA](context, movimentcao)
        expect(url).toBe(`api/movimentacoes/internas/${movimentcao.movimentacaoInternaId}/itens`)
    })

    it('BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA', async () => {
        const movimentacaoInternaId = 1
        returnedResponse = await actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA](context, movimentacaoInternaId)
        expect(url).toBe(`api/movimentacoes/internas/${movimentacaoInternaId}/itens/quantidade`)
    })
})
