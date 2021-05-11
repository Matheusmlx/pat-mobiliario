import actions from './actions'
import {actionTypes} from '@/core/constants'

let url, mockResponseData, state

jest.mock('axios', () => ({
    get(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    },
    put(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    }
}))

describe('Actions', () => {
    const context = jest.fn()

    beforeEach(() => {
        url = ''
        mockResponseData = true
        state = {
            resultadoBuscaTodosPatrimoniosParaDevolucao: {
                filtros: {
                    conteudo: {
                        default: null,
                        label: 'Pesquisa',
                        value: ''
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

    it('BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO', async () => {
        const temporariaId = 1
        const data = await actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO]({state}, temporariaId)

        expect(data).toBeTruthy()
        expect(url).toEqual(`api/movimentacoes/internas/temporaria/${temporariaId}/devolver-patrimonios?page=1&size=10&sort=numero&direction=ASC`)
    })

    it('DEVOLVER_ITENS_PARCIAL', async () => {
        const temporaria = {id: 1, patrimoniosId:[7]}
        const data = await actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.DEVOLVER_ITENS_PARCIAL](context, temporaria)

        expect(data).toBeTruthy()
        expect(url).toEqual(`api/movimentacoes/internas/temporaria/${temporaria.id}/devolver-parcial?patrimoniosId=${temporaria.patrimoniosId}`)
    })

})
