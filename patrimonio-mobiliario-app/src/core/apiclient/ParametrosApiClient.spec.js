import parametrosApiClient from './ParametrosApiClient'

let url, config,mockRetornoApi, verbHttp

jest.mock('axios', ()=>({
    get(_url){
        return new Promise(resolve =>{
            verbHttp = 'get'
            url = _url
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('ParametrosApiClient',()=>{
    beforeEach(()=>{
        verbHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('Deve buscar parâmetros',async()=>{
        const {data} = await parametrosApiClient.buscarParametros()

        expect(data).toBeTruthy()
        expect(verbHttp).toEqual('get')
        expect(url).toEqual('api/parametros-sistema')
    })
})
