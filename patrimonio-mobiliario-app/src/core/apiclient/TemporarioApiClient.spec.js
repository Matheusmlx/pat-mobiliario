import MovimentacaoInternaTemporariaApiClient from './TemporarioApiClient'

let url, mockRetornoApi, verboHttp

jest.mock('axios', () => ({
    put(_url) {
        return new Promise(resolve => {
            verboHttp = 'put'
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
    patch(_url) {
        return new Promise(resolve => {
            verboHttp = 'patch'
            url = _url
            resolve({data: mockRetornoApi})
        })
    }
}))

describe('MovimentacaoInternaTemporariaApiClient', () => {
    beforeEach(() => {
        verboHttp = ''
        url = ''
        mockRetornoApi = true
    })

    it('buscarPorId', async () => {
        const temporariaId = 1
        const {data} = await MovimentacaoInternaTemporariaApiClient.buscarMovimentacaoTemporariaPorId(temporariaId)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('get')
        expect(url).toEqual(`api/movimentacoes/internas/movimentacao/${temporariaId}`)
    })

    it('atualizar movimentação interna temporaria', async () => {
        const temporaria = {id: 1}
        const {data} = await MovimentacaoInternaTemporariaApiClient.atualizarMovimentacaoTemporaria(temporaria)

        expect(data).toBeTruthy()
        expect(verboHttp).toEqual('put')
        expect(url).toEqual(`api/movimentacoes/internas/temporaria/${temporaria.id}`)
    })

    it('Deve Enviar a movimentação do tipo Temporaria', async () => {
        const idMovimentacao = 2

        const {data} = await MovimentacaoInternaTemporariaApiClient.enviarMovimentacaoTemporaria(idMovimentacao)

        expect(verboHttp).toBe('patch')
        expect(data).toBeTruthy()
        expect(url).toBe(`api/movimentacoes/internas/temporaria/${idMovimentacao}/enviar`)
    })

    it('Deve enviar todos patrimonios da movimentação temporaria', async () => {
        const idMovimentacao = 1

        const {data} = await MovimentacaoInternaTemporariaApiClient.devolverTodosPatrimonios(idMovimentacao)
        expect(verboHttp).toBe('put')
        expect(data).toBeTruthy()
        expect(url).toBe(`api/movimentacoes/internas/temporaria/${idMovimentacao}/devolver-todos`)

    })

    it('Deve buscar as informações para o tooltip de devolver em', async () => {
        const temporariaId = 2

        const {data} = await MovimentacaoInternaTemporariaApiClient.buscarInformacaoTooltip(temporariaId)

        expect(verboHttp).toBe('get')
        expect(data).toBeTruthy()
        expect(url).toBe(`api/movimentacoes/internas/temporaria/${temporariaId}/visualizacao`)
    })

    it('Deve chamar serviço para devolução parcial dos itens', async() => {
        const temporaria = {
            id: 5,
            patrimoniosId: []
        }

        const {data} = await MovimentacaoInternaTemporariaApiClient.devolverItensParcial(temporaria)

        expect(verboHttp).toBe('put')
        expect(data).toBeTruthy()
        expect(url).toBe(`api/movimentacoes/internas/temporaria/${temporaria.id}/devolver-parcial?patrimoniosId=${temporaria.patrimoniosId}`)
    })

    it('Deve chamar o serviço para a busca de todos os itens a serem devolvidos', async() => {
        const temporariaId = 3
        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: '04'
            }
        }
        const paginacao = {
            page: 1,
            rowsPerPage: 10,
            sortBy: ['numero'],
            defaultSortBy: ['numero'],
            sortDesc: [false]
        }

        const {data} = await MovimentacaoInternaTemporariaApiClient.buscarTodosItensParaDevolucao(filtros, paginacao, temporariaId)

        expect(verboHttp).toBe('get')
        expect(data).toBeTruthy()
        expect(url).toBe(`api/movimentacoes/internas/temporaria/${temporariaId}/devolver-patrimonios?conteudo=04&page=1&size=10&sort=numero&direction=ASC`)
    })
})
