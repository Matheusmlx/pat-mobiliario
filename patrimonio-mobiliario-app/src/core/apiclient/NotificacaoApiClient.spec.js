import NotificacaoApiClient from './NotificacaoApiClient'

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

describe('NotificacaoApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('buscarNotificacoes',async()=>{
        const page = 1
        const {data} = await NotificacaoApiClient.buscarNotificacoes(page)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/notificacoes?page=${page}`)
    })

    it('buscarQuantidadeNotificacoesNaoVisualizadas',async()=>{
        const {data} = await NotificacaoApiClient.buscarQuantidadeNotificacoesNaoVisualizadas()

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/notificacoes/quantidade-nao-visualizadas')
    })
})
