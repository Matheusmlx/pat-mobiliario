import ItemIncorporacaoApiClient from './ItemIncorporacaoApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({

    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({ data: mockRetornoApi })
        })
    },
    put(_url){
        return new Promise(resolve =>{
            verboHttp = 'put'
            url = _url
            resolve({data:mockRetornoApi})
        })
    },
    post(_url){
        return new Promise(resolve =>{
            verboHttp = 'post'
            url = _url
            resolve({data:mockRetornoApi})
        })
    },
    delete(_url){
        return new Promise(resolve =>{
            verboHttp = 'delete'
            url = _url
            resolve({data:mockRetornoApi})
        })
    },
}))

describe('ItemIncorporacaoApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
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
        const id = 1

        const { data } = await ItemIncorporacaoApiClient.buscarTodos(filtros, paginacao, id)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/patrimonios/incorporacao/itens/1?page=1&size=100')
    })

    it('deletar', async () => {
        const dados = { idItem: 1, idIncorporacao: 1}
        const {data} = await ItemIncorporacaoApiClient.deletar(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('delete')
        expect(url).toEqual(`api/patrimonios/incorporacao/${dados.idIncorporacao}/item/${dados.idItem}`)
    })

    it('cadastrar', async () => {
        const dados = { idItem: 1, idIncorporacao: 1}
        const {data} = await ItemIncorporacaoApiClient.cadastrarItem(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/patrimonios/incorporacao/${dados.idIncorporacao}/item`)
    })

    it('buscarPorId', async () => {
        const dados = { idItem: 1, idIncorporacao: 1}
        const {data} = await ItemIncorporacaoApiClient.buscarItemIncorporacaoPorId(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/patrimonios/incorporacao/${dados.idIncorporacao}/item/${dados.idItem}`)
    })

    it('buscarPorId (registro/visualizacao', async () => {
        const dados = { id: 1}
        const {data} = await ItemIncorporacaoApiClient.buscarItemIncorporacaoVisualizacao(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/incorporacao/item/${dados.id}/visualizar`)
    })

    it('uploadImagem', async () => {
        const formData = {}
        const {data} = await ItemIncorporacaoApiClient.uploadImagem(formData)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/v1/arquivos')
    })

    it('downloadImagem', async () => {
        const formData = {}
        const {data} = await ItemIncorporacaoApiClient.downloadAnexo(formData)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/v1/arquivos')
    })

    it('editar', async () => {
        const dados = { id: 1, idIncorporacao: 1}
        const {data} = await ItemIncorporacaoApiClient.editarItemIncorporacao(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/patrimonios/incorporacao/${dados.idIncorporacao}/item/${dados.id}`)
    })
})
