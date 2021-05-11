import actions from './actions'
import {actionTypes, mutationTypes } from '@/core/constants'

let url, body, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    post(_url, _body) {
        return new Promise(resolve => {
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
            resultadoBuscaTodosPatrimonios: {
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: [false],
                    sortBy: ['numero']
                }
            },
            resultadoBuscaTodosPatrimoniosRegistro: {
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: [false],
                    sortBy: ['numero']
                }
            },
            resultadoBuscaTodosPatrimoniosEstornados: {
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: [false],
                    sortBy: ['numero'],
                    defaultSortBy: ['numero']
                }
            },
            resultadoBuscaTodosPatrimoniosListagem: {
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
                    sortBy: ['orgao'],
                    defaultSortBy: ['orgao']
                }
            },
            resultadoBuscaTodosPatrimoniosDeTodosItens:{
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: [false],
                    sortBy: ['numero'],
                    defaultSortBy: ['numero']
                },
                filtros: {
                    conteudo: {
                        default: null,
                        label: 'Pesquisa',
                        value: ''
                    }
                }
            },
        }
    })

    it('CADASTRAR_PATRIMONIO', async () => {
        const dados = {
            quantidade:1,
            valorTotal: 2881,
        }
        await actions[actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]({commit}, dados)
        expect(url).toEqual('api/patrimonios/incorporacao/items/patrimonios')
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('ATUALIZAR_PATRIMONIO', async () => {
        const dados = {
            id:1,
            numero: 2881,
            valorAquisicao: 20,
            placa: 'HTE80',
            renavam: 'H23999',
            licenciamento: 'AA22TT',
            motor: 'HA2T0',
            chassi: 'ATTT23'
        }
        await actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]({commit}, dados)
        expect(url).toEqual(`api/patrimonios/incorporacao/items/patrimonios/${dados.id}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('BUSCAR_TODOS_PATRIMONIOS', async () => {
        const itemIncorporacaoId = 1
        returnedResponse = await actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]({state},itemIncorporacaoId)
        expect(url).toBe('api/patrimonios/incorporacao/items/1/patrimonios?page=1&size=10&sort=numero&direction=ASC')
    })

    it('BUSCAR_TODOS_PATRIMONIOS_REGISTRO', async () => {
        const itemIncorporacaoId = 1

        returnedResponse = await actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_REGISTRO]({state},itemIncorporacaoId)
        expect(url).toBe('api/patrimonios/incorporacao/items/1/patrimonios?page=1&size=10&sort=numero&direction=ASC')
    })

    it('BUSCAR_TODOS_PATRIMONIOS_LISTAGEM', async () => {
        returnedResponse = await actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_LISTAGEM]({state})
        expect(url).toBe('api/patrimonios/patrimonios?page=1&size=10&sort=orgao&direction=ASC')
    })

    it('BUSCAR_PATRIMONIO_POR_ID_FICHA', async () => {
        const patrimonioId = 1
        returnedResponse = await actions[actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID_FICHA](context, patrimonioId)
        expect(url).toBe(`api/patrimonio/${patrimonioId}/ficha`)
    })

    it('BUSCAR_TODAS_MOVIMENTACOES', async () => {
        const patrimonioId = 1
        returnedResponse = await actions[actionTypes.PATRIMONIO.BUSCAR_TODAS_MOVIMENTACOES](context, patrimonioId)
        expect(url).toBe(`api/patrimonios/${patrimonioId}/movimentacoes`)
    })

    it('BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS', async () => {
        const incorporacaoId = 1
        returnedResponse = await actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS]({state},incorporacaoId)
        expect(url).toBe(`api/patrimonios/incorporacao/${incorporacaoId}/patrimonios?page=1&size=10&sort=numero&direction=ASC`)
    })

    it('ESTORNAR_PATRIMONIOS', async () => {
        const estorno = {
            incorporacaoId: 1,
            data: '10/11/2020',
            usuario: 'admin',
            motivo: 'motivo',
            patrimoniosId: [2,8,1]
        }
        await actions[actionTypes.PATRIMONIO.ESTORNAR_PATRIMONIOS](context, estorno)
        expect(url).toEqual('api/patrimonios/incorporacao/items/patrimonios/estornar')
    })

    it('BUSCAR_TODOS_PATRIMONIOS_ESTORNADOS', async () => {
        const incorporacaoId = 7
        returnedResponse = await actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_ESTORNADOS]({state}, incorporacaoId)
        expect(url).toBe(`api/patrimonios/incorporacao/${incorporacaoId}/patrimonios/estornados?page=1&size=10&sort=numero&direction=ASC`)
    })

    it('BUSCAR_DEPRECIACOES', async () => {
        const patrimonioId = 1
        returnedResponse = await actions[actionTypes.PATRIMONIO.BUSCAR_DEPRECIACOES](context, patrimonioId)
        expect(url).toBe(`api/patrimonio/${patrimonioId}/depreciacoes`)
    })
})
