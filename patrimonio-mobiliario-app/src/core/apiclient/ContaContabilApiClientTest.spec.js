import contaContabilApiClient from './ContaContabilApiClient'

let url, config, mockRetornoApi, verboHttp

let dados = {codigo:"124110100",id:1,idConfiguAmortizacao:undefined,metodo:"QUOTAS_CONSTANTES",nome:"BENS INTANGIVEIS>SOFTWARE",tipo:"AMORTIZAVEL"}

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
    }
}))


describe('ContaContabilApiClient',()=>{
    beforeEach(()=>{
        verboHttp = ''
        url = ''
        config = ''
        mockRetornoApi = true
    })

    it('buscarPorId',async()=>{
        const id = 1
        const {data} = await contaContabilApiClient.buscarPorId(id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/configuracao/contas-contabeis/${id}`)
    })

    it('buscarTodos',async()=>{
        const filtros = {}
        const paginacao = {}
        const {data} = await contaContabilApiClient.buscarTodos(filtros,paginacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/configuracao/contas-contabeis')

    })

    it('buscarTodos com filtro e paginacao',async()=>{
        const filtros = {conteudo:{value:'filtro'}}
        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            sortDesc: [false],
            sortBy: ['codigo']}
        const {data} = await contaContabilApiClient.buscarTodos(filtros,paginacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/configuracao/contas-contabeis?conteudo=filtro&page=1&size=10&sort=codigo&direction=ASC')
    })

    it('salvar',async()=>{

        const{data} = await contaContabilApiClient.salvar(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/configuracao/contas-contabeis/${dados.id}/config-depreciacao`,dados)

    })
    it('editar',async()=>{
        const{data} = await contaContabilApiClient.editar(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual(`api/configuracao/contas-contabeis/${dados.id}/config-depreciacao/${dados.id}`,dados)
    })
})
