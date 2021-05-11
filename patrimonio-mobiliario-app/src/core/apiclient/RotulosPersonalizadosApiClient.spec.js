import rotulosPersonalizadosApi from './RotulosPersonalizadosApiClient'

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

describe('PatrimonioApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('deve chamar a api rotulosPersonalizados e ter o retorno esperado',async()=>{
        const { data } = await rotulosPersonalizadosApi.getAll()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/rotulosPersonalizados')
    })
})
