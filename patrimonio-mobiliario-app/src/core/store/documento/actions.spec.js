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
            loki: {},
            documentoBackup: {},
            documentos: []
        }
    })

    it('Deve cadastrar um novo Documento', async () => {
        const documento = {incorporacao: 1}
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.CADASTRAR_DOCUMENTO]({commit}, documento)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(url).toBe(`api/patrimonios/incorporacao/${documento.incorporacao}/documentos`, documento)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('Deve realizar upload de documento', async () => {
        const idIncorporacao = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD](context, idIncorporacao)
        expect(url).toBe('api/v1/arquivos')
    })

    it('Deve realizar download de documento', async () => {
        const anexo = 5
        window.URL.createObjectURL = jest.fn()

        await actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_DOWNLOAD](context, anexo)
        expect(url).toBe('api/v1/arquivos')
    })

    it('Deve buscar tipos de documento', async () => {
        const idIncorproacao = 5
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO](context, idIncorproacao)
        expect(url).toBe('api/tiposdocumentos?page=1&size=12')
    })

    it('Deve buscar os documentos relacionados ao id da incorporação', async () => {
        const idIncorporacao = 2
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]({commit}, idIncorporacao)
        expect(url).toBe(`api/patrimonios/incorporacao/${idIncorporacao}/documentos`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.SET_DOCUMENTOS, returnedResponse)
    })

    it('Deve atualizar os Documentos ', async () => {
        const documento = {id: 1, incorporacao: 3}
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO]({commit}, documento)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        expect(url).toBe(`api/patrimonios/incorporacao/${documento.incorporacao}/documentos/${documento.id}`, documento)
        expect(commit).toHaveBeenCalledWith(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
    })

    it('Deve deletar documento', async () => {
        const documento = {
            id: 1,
            incorporacao: 5
        }
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DELETAR_DOCUMENTO]({state}, documento)
        expect(url).toBe(`api/patrimonios/incorporacao/${documento.incorporacao}/documentos/${documento.id}`)
    })

})
