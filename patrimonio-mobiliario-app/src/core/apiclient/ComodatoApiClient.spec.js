import comodatoApiClient from './ComodatoApiClient'

let url, body, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    get(_url){
        return new Promise(resolve =>{
            verboHttp = 'get'
            url = _url
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('ComodanteApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('buscar comodantes dinamicamente',async()=>{
        const conteudo = 'Gilbertino'

        const {data} = await comodatoApiClient.buscarComodanteDinamicamente(conteudo)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/comodantes?conteudo=${conteudo}&page=1&size=100`)
    })
})
