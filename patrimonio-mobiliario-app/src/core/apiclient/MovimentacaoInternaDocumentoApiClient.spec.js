import MovimentacaoInternaDocumentoApiClient from './MovimentacaoInternaDocumentoApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    post(_url) {
        return new Promise(resolve => {
            verboHttp = 'post'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    delete(_url) {
        return new Promise(resolve => {
            verboHttp = 'delete'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    put(_url) {
        return new Promise(resolve => {
            verboHttp = 'put'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
}))

describe('MovimentacaoInternaDocumentoApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it ('deve cadastrar documento', async () => {
        const documento = {movimentacao: 5}

        const {data} = await MovimentacaoInternaDocumentoApiClient.cadastrarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toBe('post')
        expect(url).toBe(`api/movimentacoes/internas/${documento.movimentacao}/documentos`)
    })

    it ('deve atualizar documento', async () => {
        const documento = {id: 3, movimentacao: 5}

        const {data} = await MovimentacaoInternaDocumentoApiClient.atualizarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toBe('put')
        expect(url).toBe(`api/movimentacoes/internas/${documento.movimentacao}/documentos/${documento.id}`)
    })

    it ('deve deletar documento', async () => {
        const documento = {id: 3, movimentacao: 5}

        const {data} = await MovimentacaoInternaDocumentoApiClient.deletarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toBe('delete')
        expect(url).toBe(`api/movimentacoes/internas/${documento.movimentacao}/documentos/${documento.id}`)
    })

    it ('deve buscar documentos de uma movimentação', async () => {
        const movimentacaoId = 8

        const {data} = await MovimentacaoInternaDocumentoApiClient.buscarDocumentos(movimentacaoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toBe('get')
        expect(url).toBe(`api/movimentacoes/internas/${movimentacaoId}/documentos`)
    })

})
