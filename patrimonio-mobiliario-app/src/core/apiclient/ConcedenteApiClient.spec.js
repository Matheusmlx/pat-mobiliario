import concedenteApiClient from './ConcedenteApiClient'

let url, body, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    get(_url){
        return new Promise(resolve =>{
            verboHttp = 'get'
            url = _url
            resolve({data:mockRetornoApi})
        })
    },
    put(_url, _body){
        return new Promise(resolve => {
            verboHttp = 'put'
            url = _url
            body = _body
            resolve({data:mockRetornoApi})
        })
    },
    post(_url, _body){
        return new Promise(resolve => {
            verboHttp = 'post'
            url = _url
            body = _body
            resolve({data:mockRetornoApi})
        })
    }
}))

describe('ConvenioApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('Deve buscar todos',async()=>{
        const paginacao = {
            page: 1,
            rowsPerPage: 5,
            sortDesc: [false],
            sortBy: ['nome']
        }
        const filtros = {
            conteudo: {
                value: null,
                default: null,
                label: 'Pesquisa'
            }
        }
        const {data} = await concedenteApiClient.buscarTodos(filtros, paginacao)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/concedentes?page=1&size=5&sort=nome&direction=ASC')
    })

    it('buscar dinamicamente',async()=>{
        const conteudo = '0004564839'

        const {data} = await concedenteApiClient.buscarDinamicamente(conteudo)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/concedentes?conteudo=${conteudo}&situacao=ATIVO&page=1&size=100&sort=nome&direction=ASC`)
    })

    it('buscarPorId', async () => {
        const id = 1
        const {data} = await concedenteApiClient.buscarId(id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/concedentes/${id}`)
    })

    it('editar', async () => {
        const dados = {
            id: 1,
            cpf: '20464142091',
            situacao: 'ATIVO'
        }
        const {data} = await concedenteApiClient.editar(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/concedentes/${dados.id}`, dados)
        expect(body).toEqual(dados)
    })

    it('salvar', async () => {
        const dados = {
            id: 1,
            cpf: '20464142091',
            situacao: 'ATIVO'
        }
        const {data} = await concedenteApiClient.salvar(dados)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/concedentes', dados)
        expect(body).toEqual(dados)
    })
})
