import reservaApiClient from './ReservaApiClient'

let url, config, mockRetornoApi, verboHttp

jest.mock('axios', ()=>({
    get(_url){
        return new Promise(resolve =>{
            verboHttp = 'get'
            url = _url
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('ReservaApiClient', () => {
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('buscarTodos',async () => {
        const filtros = { conteudo: {value:'filtro'} }
        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            sortDesc: [false],
            sortBy: ['situacao'],
            defaultSortBy: ['situacao']}
        const {data} = await reservaApiClient.buscarTodos(filtros,paginacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/configuracao/reservas?conteudo=filtro&page=1&size=10&sort=situacao&direction=ASC')
    })

})
