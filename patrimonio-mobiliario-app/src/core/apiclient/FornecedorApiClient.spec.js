import fornecedorApiClient from './FornecedorApiClient'

let url, config,mockRetornoApi, verbHttp

let conteudo = {}

jest.mock('axios', ()=>({
    get(_url){
        return new Promise(resolve =>{
            verbHttp = 'get'
            url = _url
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('FornecedorApiClient',()=>{
    beforeEach(()=>{
        verbHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('buscarPorCNPJ',async()=>{
        const paginacao = {
            page: 1,
            rowsPerPage: 100
        }
        const filtros = {
            conteudo: {
                value: conteudo
            }
        }

        const {data} = await fornecedorApiClient.buscarPorCNPJ(conteudo)

        expect(data).toBeTruthy()
        expect(verbHttp).toEqual('get')
        expect(url).toEqual('api/fornecedores?conteudo=[object Object]&page=1&size=100')
    })

    it('buscarPorId',async()=>{
        const id = 1
        const {data} = await fornecedorApiClient.buscarPorId(id)

        expect(data).toBeTruthy()
        expect(verbHttp).toEqual('get')
        expect(url).toEqual(`api/fornecedores/${id}`)
    })
})
