import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, body, mockResponseData, returnedResponse

const incorporacao = {
    id: 1,
    codigo: 1,
    reconhecimento: 'teste',
    numProcesso: '',
    fornecedor: '',
    numeroNota: '',
    dataNota: '',
    valorNota: '',
    orgao: null,
    empenho: '',
    setor: null,
    dataRecebimento: '',
    convenio: '',
    fundo: '',
    situacao: 'EM_ELABORACAO',
}

const incorporacaoId = 1

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
    },
    delete(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    },
    patch(_url) {
        return new Promise(resolve => {
            url = _url
            resolve({data: mockResponseData})
        })
    },
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
            resultadoBuscaTodasIncorporacoes: {
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

    it('Deve finalizar a incorporação', async () => {
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.FINALIZAR_INCORPORACAO](context, incorporacaoId)
        expect(url).toBe(`api/patrimonios/incorporacao/finalizar/${incorporacaoId}`)
    })

    it('BUSCAR_TODAS_INCORPORACOES', async () => {
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES]({state})
        expect(url).toBe('api/patrimonios/incorporacao?page=1&size=10&sort=situacao&direction=ASC')
    })

    it('CADASTRAR_INCORPORACAO', async () => {
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.CADASTRAR_INCORPORACAO]({commit})
        expect(url).toBe('api/patrimonios/incorporacao')
        expect(commit).toHaveBeenCalledWith(mutationTypes.PATRIMONIO.INCORPORACAO.SET_INCORPORACAO, true)
    })

    it('ATUALIZAR_INCORPORACAO', async () => {
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]({commit}, incorporacao)
        expect(url).toBe(`api/patrimonios/incorporacao/${incorporacao.id}`, incorporacao)
        expect(commit).toHaveBeenCalledWith(mutationTypes.PATRIMONIO.INCORPORACAO.SET_INCORPORACAO, true)
    })

    it('BUSCAR_INCORPORACAO_POR_ID', async () => {
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]({commit}, incorporacaoId)
        expect(url).toBe(`api/patrimonios/incorporacao/${incorporacaoId}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.PATRIMONIO.INCORPORACAO.SET_INCORPORACAO, true)
    })

    it('BUSCAR_REGISTRO_INCORPORACAO_POR_ID', async () => {
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO_POR_ID]({commit}, incorporacaoId)
        expect(url).toBe(`api/patrimonios/incorporacao/${incorporacaoId}/visualizar`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.PATRIMONIO.INCORPORACAO.SET_INCORPORACAO, true)
    })

    it('DELETAR_INCORPORACAO', async () => {
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.DELETAR_INCORPORACAO](context, incorporacaoId)
        expect(url).toBe(`api/patrimonios/incorporacao/${incorporacaoId}`)
    })

    it('BUSCAR_SITUACAO_INCORPORACAO', async () => {
        const incorporacaoId = 2
        await actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO](context, incorporacaoId)
        expect(url).toEqual(`api/patrimonios/incorporacao/${incorporacaoId}/situacao`)
    })
})
