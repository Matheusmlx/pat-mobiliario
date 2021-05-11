import actions from './actions'
import {actionTypes} from '@/core/constants'

let url, body, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    post(_url, _body) {
        return new Promise((resolve) => {
            url = _url
            body = _body
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
    get(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    }
}))

describe('Actions', () => {
    const context = jest.fn()
    const commit = jest.fn()
    let state

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
            todosReconhecimentos: {
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc:[false],
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
    })

    it('BUSCAR_TODOS_RECONHECIMENTOS', async () => {
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_TODOS_RECONHECIMENTOS]({state})
        expect(url).toBe('api/reconhecimentos?page=1&size=10&sort=situacao&direction=ASC')
    })

    it('BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE', async () => {
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]({context})
        expect(url).toBe('api/reconhecimentos?situacao=ATIVO&page=1&size=10&sort=nome&direction=ASC')
    })

    it('INSERIR_RECONHECIMENTO', async () => {
        const reconhecimento = {nome: 'Compra', situacao: 'ATIVO', execucaoOrcamentaria: true}
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RECONHECIMENTO.INSERIR_RECONHECIMENTO]({commit}, reconhecimento)
        expect(url).toBe('api/reconhecimentos', reconhecimento)
    })

    it('EDITAR_RECONHECIMENTO', async () => {
        const reconhecimento = {nome: 'Compra', id: 1, situacao: 'ATIVO', execucaoOrcamentaria: true}
        returnedResponse = await actions[actionTypes.CONFIGURACAO.RECONHECIMENTO.EDITAR_RECONHECIMENTO]({commit}, reconhecimento)
        expect(url).toBe(`api/reconhecimentos/${reconhecimento.id}`, reconhecimento)
    })
})
