import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, body, state, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    post(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
    put(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
    get(_url) {
        return new Promise(resolve => {
            url = _url
            resolve({ data: mockResponseData })
        })
    },
    delete(_url) {
        return new Promise(resolve => {
            url = _url
            resolve()
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

    it('Deve buscar todas contas Contabeis', async () => {
        const state = {
            todasContasContabeis: {
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortDesc: [false],
                    sortBy: [''],
                },
                filtros: {
                    conteudo: {
                        value: null,
                        default: null,
                        label: 'Pesquisa',
                    },
                },
            },
        }
        returnedResponse = await actions[actionTypes.CONFIGURACAO.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS]({ state,commit })
        expect(url).toBe('api/configuracao/contas-contabeis?page=1&size=10&sort=&direction=ASC')
    })

    it('deve editar conta contabil', async () => {
        const contaContabil = {
            'id': 1, 'idConfigAmortizacao': 2, 'metodo': 'QUOTAS_CONSTANTES', 'percentualResidual': '15', 'sistema': 'INTANGIVEL', 'tipo': 'DEPRECIAVEL', 'vidaUtil': '45'
        }

        returnedResponse = await actions[actionTypes.CONFIGURACAO.CONTA_CONTABIL.EDITAR_CONTA_CONTABIL]({ commit }, contaContabil)

        expect(url).toBe(`api/configuracao/contas-contabeis/${contaContabil.id}/config-depreciacao/${contaContabil.id}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_CONTA_CONTABIL, contaContabil)
    })

    it('deve buscar buscar conta contabil por id', async () => {
        const idContaContabil = 1

        returnedResponse = await actions[actionTypes.CONFIGURACAO.CONTA_CONTABIL.BUSCAR_CONTA_CONTABIL_POR_ID]({ commit }, idContaContabil)
        expect(url).toBe(`api/configuracao/contas-contabeis/${idContaContabil}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_CONTA_CONTABIL, true)
        expect(returnedResponse).toBeTruthy()
    })

    it('deve salvar conta contabil', async () => {
        const contaContabil = {
            id: 1,
            tipo: 'DEPRECIAVEL',
            metodo: 'QUOTAS_CONSTANTES',
            percentualResidual: '15',
            vidaUtil: '45',
            sistema: 'INTANGIVEL',
        }

        returnedResponse = await actions[actionTypes.CONFIGURACAO.CONTA_CONTABIL.SALVAR_CONTA_CONTABIL]({ commit }, contaContabil)

        expect(url).toBe(`api/configuracao/contas-contabeis/${contaContabil.id}/config-depreciacao`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_CONTA_CONTABIL, true)
    })






})
