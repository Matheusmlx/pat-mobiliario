import EmpenhoApiClient from './EmpenhoApiClient'

let url, body, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    post(_url, _body) {
        return new Promise(resolve => {
            verboHttp = 'post'
            url = _url
            body = _body
            resolve({data: mockRetornoApi})
        })
    },
    delete(_url) {
        return new Promise(resolve => {
            verboHttp = 'delete'
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
    put(_url, _body){
        return new Promise(resolve =>{
            verboHttp = 'put'
            url = _url
            body = _body
            resolve({data:mockRetornoApi})
        })
    },
}))

const empenho = {
    numero: '956345',
    data: '2020-07-15T23:00:00.000-0400',
    valor: '9500.50',
    incorporacaoId: 2
}

describe('EmpenhoApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        body = ''
        mockRetornoApi = true
    })

    it('Deve salvar o empenho', async() => {
        const {data} = await EmpenhoApiClient.salvar(empenho)

        expect(data).toBeTruthy()
        expect(verboHttp).toBe('post')
        expect(url).toBe('api/patrimonios/incorporacao/empenhos')
        expect(body).toEqual(empenho)
    })

    it('Deve buscar todos empenhos', async() => {
        const incorporacao = {
            id: 5
        }

        const { data } = await EmpenhoApiClient.buscarEmpenhos(incorporacao.id)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/patrimonios/incorporacao/5/empenhos')
    })

    it('Deve excluir empenho', async() => {
        const empenho = {
            id: 12,
            numero: '956345',
            data: '2020-07-15T23:00:00.000-0400',
            valor: '9500.50',
            incorporacaoId: 2
        }

        await EmpenhoApiClient.remover(empenho.id)

        expect(verboHttp).toEqual('delete')
        expect(url).toEqual('api/patrimonios/incorporacao/empenhos/12')
    })

    it('Deve editar empenho', async() => {
        const empenho = {
            id: 12,
            numero: '956345',
            data: '2020-07-15T23:00:00.000-0400',
            valor: '9500.50',
            incorporacaoId: 2
        }

        const {data} = await EmpenhoApiClient.atualizar(empenho)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual('api/patrimonios/incorporacao/empenhos/12')
        expect(body).toEqual(empenho)
    })
})
