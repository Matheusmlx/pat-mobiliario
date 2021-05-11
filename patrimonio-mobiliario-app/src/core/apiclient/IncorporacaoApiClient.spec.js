import IncorporacaoApiClient from './IncorporacaoApiClient'

let url, mockRetornoApi, verboHttp,body, documento={incorporacao:1,id:2}

jest.mock('axios', () => ({

    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({ data: mockRetornoApi })
        })
    },
    post(_url, _body) {
        return new Promise(resolve => {
            verboHttp = 'post'
            url = _url
            body = _body
            resolve({data: mockRetornoApi})
        })
    },
    put(_url, _body) {
        return new Promise(resolve => {
            verboHttp = 'put'
            url = _url
            body = _body
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
    patch(_url){
        return new Promise(resolve =>{
            verboHttp = 'patch'
            url = _url
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('IncorporacaoApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('finalizar',async()=>{
        const incorporacaoId = 0
        await IncorporacaoApiClient.finalizar(incorporacaoId)

        expect(verboHttp).toEqual('patch')
        expect(url).toEqual(`api/patrimonios/incorporacao/finalizar/${incorporacaoId}`)
    })

    it('buscarTodos', async () => {
        const paginacao = {
            page: 1,
            rowsPerPage: 100,
            sortDesc: []
        }
        const filtros = {
            conteudo: {}
        }
        const { data } = await IncorporacaoApiClient.buscarTodos(filtros, paginacao)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/patrimonios/incorporacao?page=1&size=100')
    })

    it('cadastrar', async () => {
        const { data } = await IncorporacaoApiClient.cadastrar()
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/patrimonios/incorporacao')
    })

    it('atualizar', async () => {
        const incorporacao = {id:0}
        const { data } = await IncorporacaoApiClient.atualizar(incorporacao)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/patrimonios/incorporacao/${incorporacao.id}`)
    })

    it('atualizar com orgão e setor', async () => {
        const incorporacao = {id:0, orgao:{id:1}, setor:{id:2}, fornecedor:{id:3}, reconhecimento:{id:4}, comodante:{id:5}}
        const { data } = await IncorporacaoApiClient.atualizar(incorporacao)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(body.orgao).toEqual(1)
        expect(body.setor).toEqual(2)
        expect(body.fornecedor).toEqual(3)
        expect(body.reconhecimento).toEqual(4)
        expect(body.comodante).toEqual(5)
        expect(url).toEqual(`api/patrimonios/incorporacao/${incorporacao.id}`)
    })

    it('buscarPorId', async () => {
        const incorporacaoId = 0
        const { data } = await IncorporacaoApiClient.buscarPorId(incorporacaoId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/patrimonios/incorporacao/${incorporacaoId}`)
    })

    it('buscarRegistroPorId', async () => {
        const incorporacaoId = 0
        const { data } = await IncorporacaoApiClient.buscarRegistroPorId(incorporacaoId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/patrimonios/incorporacao/${incorporacaoId}/visualizar`)
    })

    it('deletar', async () => {
        const incorporacaoId = 0
        const { data } = await IncorporacaoApiClient.deletarIncorporacao(incorporacaoId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual(`api/patrimonios/incorporacao/${incorporacaoId}`)
    })

    it('reabrir', async () => {
        const incorporacaoId = 7
        const { data } = await IncorporacaoApiClient.reabrirIncorporacao(incorporacaoId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('patch')
        expect(url).toEqual(`api/patrimonios/incorporacao/reabrir/${incorporacaoId}`)
    })

    it('cadastrarDocumento',async()=>{
        const {data} = await IncorporacaoApiClient.cadastrarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/patrimonios/incorporacao/${documento.incorporacao}/documentos`)
    })

    it('buscarTipoDocumento',async()=>{
        const {data} = await IncorporacaoApiClient.buscarTipoDocumento()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/tiposdocumentos?page=1&size=12')
    })

    it('buscarDocumentos',async()=>{
        const {data} = await IncorporacaoApiClient.buscarDocumentos(7)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/patrimonios/incorporacao/7/documentos')
    })

    it('atualizarDocumento',async()=>{
        const {data} = await IncorporacaoApiClient.atualizarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/patrimonios/incorporacao/${documento.incorporacao}/documentos/${documento.id}`)
    })

    it('deletarDocumento',async()=>{
        const documento = {id:1, incorporacao: 1}
        const {data} = await IncorporacaoApiClient.deletarDocumento(documento)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual(`api/patrimonios/incorporacao/${documento.incorporacao}/documentos/${documento.id}`)
    })

    it('Deve buscar a situacao da incorporação', async () => {
        const id = 1
        const {data} = await IncorporacaoApiClient.buscarSituacaoIncorporacao(id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/patrimonios/incorporacao/${id}/situacao`)
    })

    it('Deve realizar download anexo', async () => {
        const anexo = 'uri:anexo'
        const {data} = await IncorporacaoApiClient.downloadAnexo(anexo)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/v1/arquivos')
    })

    it('Deve realizar upload anexo', async () => {
        const formData = 'form'
        const {data} = await IncorporacaoApiClient.uploadAnexoDocumento(formData)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/v1/arquivos')
    })
})
