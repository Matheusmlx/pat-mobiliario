import {actionTypes, mutationTypes} from '@/core/constants'
import actions from './actions'

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

    it('Deve cadastrar um novo Documento', async () => {
        const documento = {movimentacao: 1}
        await actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO_INTERNA]({commit}, documento)

        expect(body).toBe(documento)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(url).toBe(`api/movimentacoes/internas/${documento.movimentacao}/documentos`, documento)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('Deve buscar tipos de documento', async () => {
        await actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO_MOVIMENTACAO_INTERNA](context)
        expect(url).toBe('api/tiposdocumentos?page=1&size=12')
    })

    it('Deve buscar os documentos relacionados ao id da movimentação', async () => {
        const movimentacaoId = 2
        await actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO_INTERNA]({commit}, movimentacaoId)

        expect(url).toBe(`api/movimentacoes/internas/${movimentacaoId}/documentos`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.SET_DOCUMENTOS_MOVIMENTACAO_INTERNA, returnedResponse)
    })

    it('Deve atualizar os Documentos ', async () => {
        const documento = {id: 1, movimentacao: 3}
        await actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.ATUALIZAR_DOCUMENTO_MOVIMENTACAO_INTERNA]({commit}, documento)

        expect(body).toBe(documento)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(url).toBe(`api/movimentacoes/internas/${documento.movimentacao}/documentos/${documento.id}`, documento)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('Deve deletar documento', async () => {
        const documento = {
            id: 1,
            movimentacao: 5
        }

        await actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.DELETAR_DOCUMENTO_MOVIMENTACAO_INTERNA]({commit}, documento)

        expect(url).toBe(`api/movimentacoes/internas/${documento.movimentacao}/documentos/${documento.id}`)
    })

})
