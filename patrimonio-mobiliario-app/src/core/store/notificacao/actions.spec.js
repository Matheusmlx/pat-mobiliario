import actions from './actions'
import {actionTypes} from '@/core/constants'

let url, body, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    get(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({ data: mockResponseData })
        })
    }
}))

describe('Actions', () => {
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
            notificacao:{
                items:[],
                page: 0
            }
        }
    })

    it('BUSCAR_NOTIFICACOES', async () => {
        mockResponseData = {
                items: [
                    {
                        id: 1,
                        origem: 'DEVOLUCAO_ALMOXARIFADO',
                        origemId: 1,
                        assunto: 'Devolução Almoxarifado 12345678912539687589',
                        mensagem: 'Em Processamento',
                        dataCriacao: '2020-02-10T00:00:00.000-0400',
                        visualizada: false,
                    },
                ],
                totalElements: 1,
                quantidadeNaoVisualizadas:1
            }
        returnedResponse = await actions[actionTypes.NOTIFICACAO.BUSCAR_NOTIFICACOES]({ commit, state })
        expect(url).toBe('api/notificacoes?page=0')
        expect(commit.mock.calls[0][0]).toEqual('setNotification')
        expect(commit.mock.calls[1][0]).toEqual('setNotificacaoItens')
    })

    it('BUSCAR_QUANTIDADE_NOTIFICACOES_NAO_VISUALIZADAS', async () => {
        returnedResponse = await actions[actionTypes.NOTIFICACAO.BUSCAR_QUANTIDADE_NOTIFICACOES_NAO_VISUALIZADAS]({commit})
        expect(commit.mock.calls[0][0]).toEqual('setNotification')
        expect(url).toBe('api/notificacoes/quantidade-nao-visualizadas')
    })
})
