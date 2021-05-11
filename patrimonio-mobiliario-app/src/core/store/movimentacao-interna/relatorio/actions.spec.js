import {actionTypes} from '@/core/constants'
import actions from './actions'

let url, body, mockResponseData

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
    get(_url) {
        return new Promise(resolve => {
            url = _url
            resolve({data: mockResponseData})
        })
    },
    delete(_url) {
        return new Promise(resolve => {
            url = _url
            resolve()
        })
    }
}))

describe('Actions', () => {
    const commit = jest.fn()
    const context = jest.fn()

    beforeEach(() => {
        url = ''
        body = undefined
        mockResponseData = true
    })

    it('Deve realizar download do termo de responsabilidade', async () => {
        const movimentacaoId = 1
        window.URL.createObjectURL = jest.fn()
        await actions[actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_TERMO_DE_RESPONSABILDADE](context, movimentacaoId)
        expect(url).toBe(`api/movimentacao/${movimentacaoId}/termo-guarda-responsabilidade`)
    })

    it('Deve gerar o memorando em elaboração', async () => {
        const idMovimentacao = 2
        window.URL.createObjectURL = jest.fn()
        await actions[actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_EM_ELABORACAO](context, idMovimentacao)
        expect(url).toBe(`api/movimentacao/${idMovimentacao}/memorando/em-elaboracao`)
    })

    it('Deve gerar o memorando finalizada', async () => {
        const idMovimentacao = 2
        window.URL.createObjectURL = jest.fn()
        await actions[actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_FINALIZADA](context, idMovimentacao)
        expect(url).toBe(`api/movimentacao/${idMovimentacao}/memorando/finalizado`)
    })
})
