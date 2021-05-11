import ProdutoApiClient from './ProdutoApiClient'

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

describe('ProdutoApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('buscarPorNome',async()=>{
        const produtoNome = 'skype'
        const {data} = await ProdutoApiClient.buscarPorNome(produtoNome)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`/hal/public/produtos?produtoNome=${produtoNome}`)
    })

    it('buscarParametros',async()=>{
        const {data} = await ProdutoApiClient.buscarParametros()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/configuracao/parametros')
    })
})
