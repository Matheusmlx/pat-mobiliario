import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    get(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    }
}))

describe('Actions', () => {
    const commit = jest.fn()
    let state

    beforeEach(() => {
        url = ''
        mockResponseData = true
        state = {}
    })

    it('Deve buscar todos os itens', async () => {
        state = {
            itens: [],
            todosItens: {
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: [true],
                    sortBy: ['situacao']
                },
                filtros: {
                    conteudo: {
                        value: null,
                        default: null,
                        label: 'Pesquisa'
                    }
                }
            }
        }

        returnedResponse = await actions[actionTypes.ITEM_CATALOGO.BUSCAR_TODOS_ITENS_CATALOGO]({state})

        expect(url).toBe('api/catalogo/itens?page=1&size=10&sort=situacao&direction=DESC')
        expect(returnedResponse).toBeTruthy()
    })

    it('Deve buscar item por id', async () => {
        const id = 1
        returnedResponse = await actions[actionTypes.ITEM_CATALOGO.BUSCAR_ITEM_CATALOGO_POR_ID]({commit},id)

        expect(url).toBe(`api/catalogo/itens/${id}`)
        expect(returnedResponse).toBeTruthy()
        expect(commit).toHaveBeenCalledWith(mutationTypes.ITEM_CATALOGO.SET_ITEM_CATALOGO,true)
    })
})
