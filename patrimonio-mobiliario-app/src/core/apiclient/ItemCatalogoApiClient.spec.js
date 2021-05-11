import ItemCatalogoApiClient from './ItemCatalogoApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', ()=>({
    get(_url){
        return new Promise(resolve =>{
            verboHttp = 'get'
            url = _url
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('ItemCatalogoApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('buscarTodos',async()=>{

        const paginacao = {
            page: 1,
            rowsPerPage: 5,
            sortDesc: [false],
            sortBy: ['codigo']
        }
        const filtros = {
            conteudo: {
                value: null,
                default: null,
                label: 'Pesquisa'
            }
        }

        const {data} = await ItemCatalogoApiClient.buscarTodos(filtros, paginacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/catalogo/itens?page=1&size=5&sort=codigo&direction=ASC')
    })

    it('buscarPorId',async()=>{

        const id = 1

        const {data} = await ItemCatalogoApiClient.buscarPorId(id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/catalogo/itens/${id}`)
    })
})
