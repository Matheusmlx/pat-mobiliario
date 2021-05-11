import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, body, mockResponseData, returnedResponse

const convenio = {
    id: 1,
    numero: '095848',
    nome: 'Caixa Seguradora de Bens Social',
    concedente: {
        id: 1, nome: 'Bradesco Seguros'
    },
    fonteRecurso: {
        id: 2, nome: 'Aquisição de protótipos'
    },
    situacao: 'INATIVO',
    dataInicioPeriodo: '2020-07-15T23:00:00.000-0400',
    dataFimPeriodo: '2020-07-16T23:00:00.000-0400'
}

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
    const commit = jest.fn()
    const context = jest.fn()
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
            resultadoBuscaTodosConvenios: {
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
                    sortBy: ['situacao']
                }
            },
        }
    })

    it('BUSCAR_TODOS_CONVENIOS', async () => {
        returnedResponse = await actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_TODOS_CONVENIOS]({state})
        expect(url).toBe('api/convenios?page=1&size=10&sort=situacao&direction=ASC')
    })

    it('SALVAR_CONVENIO', async () => {
        await actions[actionTypes.CONFIGURACAO.CONVENIO.SALVAR_CONVENIO]({commit}, convenio)
        expect(url).toEqual('api/convenios')
        expect(commit).toHaveBeenCalledWith(mutationTypes.CONFIGURACAO.CONVENIO.SET_CONVENIO, true)
    })

    it('EDITAR_CONVENIO', async () => {
        const dados = {
            id:1
        }
        await actions[actionTypes.CONFIGURACAO.CONVENIO.EDITAR_CONVENIO]({commit}, dados)
        expect(url).toEqual(`api/convenios/${dados.id}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.CONFIGURACAO.CONVENIO.SET_CONVENIO, true)
    })

    it('BUSCAR_CONVENIO_POR_ID', async () => {
        const id = 1
        await actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]({commit}, id)
        expect(url).toEqual(`api/convenios/${id}`)
    })

    it('BUSCAR_CONVENIO_DINAMICAMENTE', async () => {
        const conteudo = 'convenio'
        returnedResponse = await actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE](context,conteudo)
        expect(url).toBe('api/convenios?conteudo=convenio&situacao=ATIVO&page=1&size=100&sort=nome&direction=ASC')
    })
})
