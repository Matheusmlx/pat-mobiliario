import ConvenioApiClient from './ConvenioApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    post(_url) {
        return new Promise(resolve => {
            verboHttp = 'post'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    get(_url){
        return new Promise(resolve =>{
            verboHttp = 'get'
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
    },
}))

const convenio = {
    id: 1,
    numero: '095848',
    nome: 'Caixa Seguradora de Bens Social',
    concedente: 'Bradesco Seguros',
    fonteRecurso: 'Aquisição de protótipos',
    situacao: 'INATIVO',
    dataHoraInicial: '2020-07-15T23:00:00.000-0400',
    dataHoraFinal: '2020-07-16T23:00:00.000-0400'
}

describe('ConvenioApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })


    it('Deve salvar convênio', async () => {

        const {data} = await ConvenioApiClient.salvar(convenio)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/convenios', convenio)
    })

    it('buscarPorId', async () => {
        const id = 1
        const {data} = await ConvenioApiClient.buscarId(id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/convenios/${id}`)
    })

    it('editar', async () => {

        const {data} = await ConvenioApiClient.editar(convenio)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/convenios/${convenio.id}`)
    })

    it('buscarTodos', async () => {
        const filtros = {}
        const paginacao = {sortDesc:[true]}
        const {data} = await ConvenioApiClient.buscarTodos(filtros,paginacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/convenios?')
    })

    it('buscarTodos com filtro e paginacao', async () => {
        const filtros = {conteudo:{value:'filtro'}}
        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            sortDesc: [false],
            sortBy: ['situacao'],}
        const {data} = await ConvenioApiClient.buscarTodos(filtros,paginacao)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/convenios?conteudo=filtro&page=1&size=10&sort=situacao&direction=ASC')
    })

    it('buscarDinamicamente', async () => {
        const conteudo = 'contenudo'
        const {data} = await ConvenioApiClient.buscarConvenioDinamicamente(conteudo)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/convenios?conteudo=contenudo&situacao=ATIVO&page=1&size=100&sort=nome&direction=ASC')
    })
})
