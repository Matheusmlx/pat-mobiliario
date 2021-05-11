import NaturezaDespesaApiClient from './NaturezaDespesaApiClient'

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

describe('NaturezaDespesaApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('buscarNaturezasDespesa',async()=>{
        const codigo = '00002'
        const {data} = await NaturezaDespesaApiClient.buscarNaturezasDespesa(codigo)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/natureza-despesa?itemCatalogoCodigo=${codigo}`)
    })
})
