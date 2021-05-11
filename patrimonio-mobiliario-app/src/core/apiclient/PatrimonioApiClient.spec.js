import PatrimonioApiClient from './PatrimonioApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({

    get(_url) {
        return new Promise(resolve => {
            verboHttp = 'get'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    post(_url) {
        return new Promise(resolve => {
            verboHttp = 'post'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    put(_url) {
        return new Promise(resolve => {
            verboHttp = 'put'
            url = _url
            resolve({data: mockRetornoApi})
        })
    },
    patch(_url) {
        return new Promise(resolve => {
            verboHttp = 'patch'
            url = _url
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
}))


describe('PatrimonioApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })
    it('buscarPatrimonios', async () => {
        const paginacao = {
            page: 1,
            rowsPerPage: 100,
            sortDesc: [false]
        }
        const filtros = {
            conteudo: {}
        }

        const itemIncorporacaoId = 1

        const {data} = await PatrimonioApiClient.buscarPatrimonios(filtros, paginacao, itemIncorporacaoId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/patrimonios/incorporacao/items/1/patrimonios?page=1&size=100')
    })

    it('buscarPatrimonios com situacao', async () => {
        const paginacao = {
            page: 1,
            rowsPerPage: 100,
            sortDesc: [false]
        }
        const filtros = {
            conteudo: {},
            situacao:{value:'situacao'}
        }

        const itemIncorporacaoId = 1

        const {data} = await PatrimonioApiClient.buscarPatrimonios(filtros, paginacao, itemIncorporacaoId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/patrimonios/incorporacao/items/1/patrimonios?situacao=situacao&page=1&size=100')
    })

    it('atualizarPatrimonio', async () => {

        const patrimonio = {
            id: 1,
            numero: 2881,
            valorAquisicao: 20,
            placa: 'HTE80',
            renavam: 'H23999',
            licenciamento: 'AA22TT',
            motor: 'HA2T0',
            chassi: 'ATTT23'
        }

        const {data} = await PatrimonioApiClient.atualizarPatrimonio(patrimonio)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/patrimonios/incorporacao/items/patrimonios/${patrimonio.id}`)
    })

    it('cadastrarPatrimonio', async () => {

        const patrimonio = {
            quantidade: 1,
            valorTotal: 2881,
        }

        const {data} = await PatrimonioApiClient.cadastrarPatrimonio(patrimonio)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('post')
        expect(url).toEqual('api/patrimonios/incorporacao/items/patrimonios')
    })

    it('buscarPatrimoniosListagem', async () => {
        const paginacao = {
            page: 1,
            rowsPerPage: 100,
            sortDesc: []
        }
        const filtros = {
            conteudo: {}
        }

        const {data} = await PatrimonioApiClient.buscarPatrimoniosListagem(filtros, paginacao)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual('api/patrimonios/patrimonios?page=1&size=100')
    })

    it('buscarPatrimonioPorIdFicha', async () => {
        const patrimonioId =  1

        const {data} = await PatrimonioApiClient.buscarPatrimonioPorIdFicha(patrimonioId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/patrimonio/${patrimonioId}/ficha`)
    })

    it('buscarTodasMovimentacoes', async () => {
        const patrimonioId =  1

        const {data} = await PatrimonioApiClient.buscarTodasMovimentacoes(patrimonioId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/patrimonios/${patrimonioId}/movimentacoes`)
    })

    it('buscarTodosPatrimoniosDeTodosItens', async () => {
        const paginacao = {page: 1, rowsPerPage: 100, sortDesc: []}
        const filtros = {conteudo: {}}
        const incorporacaoId = 1

        const {data} = await PatrimonioApiClient.buscarTodosPatrimoniosDeTodosItens(filtros, paginacao, incorporacaoId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/patrimonios/incorporacao/${incorporacaoId}/patrimonios?page=1&size=100`)
    })

    it('estornarPatrimonios', async () => {
        const estorno = {
            incorporacaoId: 1,
            data: '10/11/2020',
            usuario: 'admin',
            motivo: 'motivo',
            patrimoniosId: [2,8,1]
        }

        const {data} = await PatrimonioApiClient.estornarPatrimonios(estorno)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('patch')
        expect(url).toEqual('api/patrimonios/incorporacao/items/patrimonios/estornar')
    })

    it('buscarTodosPatrimoniosEstornados', async () => {
        const incorporacaoId = 3

        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            sortBy: ['numero'],
            defaultSortBy: ['numero'],
            sortDesc: [false]
        }

        const filtros = {
            conteudo: {}
        }

        const {data} = await PatrimonioApiClient.buscarTodosPatrimoniosEstornados(filtros, paginacao, incorporacaoId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/patrimonios/incorporacao/${incorporacaoId}/patrimonios/estornados?page=1&size=10&sort=numero&direction=ASC`)
    })

    it('buscarDepreciacoes', async () => {
        const patrimonioId =  1

        const {data} = await PatrimonioApiClient.buscarDepreciacoes(patrimonioId)
        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/patrimonio/${patrimonioId}/depreciacoes`)
    })
})
