import UsuarioApiClient from './UsuarioApiClient'

let url, config, mockRetornoApi, verboHttp

jest.mock('axios', ()=>({
    get(_url){
        return new Promise(resolve =>{
            verboHttp = 'get'
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
    put(_url){
        return new Promise(resolve =>{
            verboHttp = 'put'
            url = _url
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('UsuarioApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('atualiazarSessaoDominio',async()=>{
        const produtoId = 2
        const dominioTipoCliente='td'
        const {data} = await UsuarioApiClient.atualiazarSessaoDominio(produtoId,dominioTipoCliente)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual('/hal/usuario/sessao/atualizarSessaoMultiplosDominios?produtoId=2&dominioTipoCliente=td')
    })

    it('buscarLogado',async()=>{
        const produtoId = 1
        const {data} = await UsuarioApiClient.buscarLogado(produtoId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`/hal/usuario/sessao?produtoId=${produtoId}`)
    })

    it('editar',async()=>{
        const payload = {}
        const {data} = await UsuarioApiClient.editar()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('/hal/editarUsuario', payload)
    })
})
