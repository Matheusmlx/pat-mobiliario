import actions from './actions'
import {actionTypes} from '@/core/constants'

let url, body, mockResponseData, returnedResponse

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
    post(_url, _body) {
        return new Promise((resolve) => {
            url = _url
            body = _body
            resolve({data: mockResponseData})
        })
    }
}))

describe('Actions', () => {
    let state
    const commit = jest.fn()
    const context = jest.fn()

    beforeEach(() => {
        url = ''
        mockResponseData = true

        state = {}
    })

    it('Deve buscar todos os concedentes', async () => {
        state = {
            concedentes: [],
            todosConcedentes: {
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
                    },
                    situacao:{
                        value:null
                    }
                }
            }
        }

        returnedResponse = await actions[
            actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_TODOS_CONCEDENTES
            ]({state})

        expect(url).toBe('api/concedentes?page=1&size=10&sort=situacao&direction=DESC')
        expect(returnedResponse).toBeTruthy()
    })

    it('Deve buscar concedentes dinamicamente', async () => {
        const cnpj = '09087060120'
        returnedResponse = await actions[actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_CONCEDENTES_DINAMICAMENTE](context,cnpj)
        expect(url).toBe(`api/concedentes?conteudo=${cnpj}&situacao=ATIVO&page=1&size=100&sort=nome&direction=ASC`)
    })

    it('Deve buscar o concedente pelo id', async () => {
        const id = 1
        returnedResponse = await actions[actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_CONCEDENTE_POR_ID]({context}, id)
        expect(url).toEqual(`api/concedentes/${id}`)
        expect(returnedResponse).toBeTruthy()
    })

    it('Deve salvar um concedente', async() => {
        const concedente = {
            cpf: '17673923033',
            nome: 'Teste',
            situacao: 'ATIVO'
        }

        await actions[actionTypes.CONFIGURACAO.CONCEDENTE.SALVAR_CONCEDENTE]({commit}, concedente)

        expect(url).toBe('api/concedentes')
        expect(body).toBe(concedente)
    })

    it('Deve editar o concedente', async() => {
        const concedente = {
            id: 1,
            cpf: '17673923033',
            nome: 'Teste',
            situacao: 'ATIVO'
        }

        await actions[actionTypes.CONFIGURACAO.CONCEDENTE.EDITAR_CONCEDENTE]({commit}, concedente)

        expect(url).toBe('api/concedentes/1')
        expect(body).toBe(concedente)
    })
})
