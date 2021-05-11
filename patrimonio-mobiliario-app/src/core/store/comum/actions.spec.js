import actions from './actions'
import {actionTypes, mutationTypes} from '@/core/constants'

let url, body, mockResponseData, returnedResponse

jest.mock('axios', () => ({
    post(_url, _body) {
        return new Promise((resolve) => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
    put(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
    get(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({ data: mockResponseData })
        })
    }
}))

describe('Actions', () => {
    const commit = jest.fn()
    const context = jest.fn()
    let state

    beforeEach(() => {
        url = ''
        body = undefined
        mockResponseData = true
        state = {
            loki: {
                product: {
                    id: 401,
                    name: 'patrimonio-mobiliario'
                }
            },
            comum:{
                notificacoes: {
                    items:[],
                    page: 0
                }
            }
        }
    })

    it('BUSCAR_PRODUTO_POR_NOME', async () => {
        const packageJson = { name: 'patrimonio-mobiliario' }
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_PRODUTO_POR_NOME]({ commit }, packageJson)
        expect(url).toBe('/hal/public/produtos?produtoNome=patrimonio-mobiliario')
        expect(commit).toHaveBeenCalledWith(mutationTypes.COMUM.SET_PRODUTO, true)
    })

    it('BUSCAR_USUARIO_LOGADO', async () => {
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_USUARIO_LOGADO]({ commit, state })
        expect(url).toBe(`/hal/usuario/sessao?produtoId=${state.loki.product.id}`)
        expect(commit).toHaveBeenCalledWith(mutationTypes.COMUM.SET_USUARIO_LOGADO, true)
    })

    it('BUSCAR_LINK_EDITAR_USUARIO', async () => {
        const payload = { 'produto': 'patrimonio-mobiliario', 'uri': 'http://localhost/' }

        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_LINK_EDITAR_USUARIO]({ state })
        expect(url).toBe('/hal/editarUsuario')
        expect(body).toEqual(payload)
        expect(returnedResponse).toBeTruthy()
    })

    it('BUSCAR_FORNECEDORES', async () => {
        const cnpj = '09087060120'
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_FORNECEDORES](context,cnpj)
        expect(url).toBe(`api/fornecedores?conteudo=${cnpj}&page=1&size=100`)
    })

    it('BUSCAR_FORNECEDOR_POR_ID', async () => {
        const id = 5
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID](context,id)
        expect(url).toBe(`api/fornecedores/${id}`)
    })

    it('BUSCAR_SETORES_ALMOXARIFADO', async () => {
        const orgaoId = 5
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO](context,orgaoId)
        expect(url).toBe(`api/unidades-organizacionais/${orgaoId}/setores-almoxarifado`)
    })

    it('BUSCAR_SETORES_NAO_ALMOXARIFADO', async () => {
        const orgaoId = 2
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO](context,orgaoId)
        expect(url).toBe(`api/unidades-organizacionais/${orgaoId}/setores-nao-almoxarifado`)
    })

    it('BUSCAR_SETORES_POSSUI_ACESSO', async () => {
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_SETORES_POSSUI_ACESSO]()
        expect(url).toBe('api/unidades-organizacionais/setores?sort=sigla&direction=ASC')
    })

    it('BUSCAR_TODOS_ORGAOS', async () => {
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]()
        expect(url).toBe('api/unidades-organizacionais?sort=sigla&direction=ASC')
    })

    it('BUSCAR_NATUREZAS_DESPESA', async () => {
        const codigoItem = '00002'
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_NATUREZAS_DESPESA](context,codigoItem)
        expect(url).toBe(`api/natureza-despesa?itemCatalogoCodigo=${codigoItem}`)
    })

    it('BUSCAR_FUNDOS', async () => {
        const orgaoId = 239
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_FUNDOS](context,orgaoId)
        expect(url).toBe(`api/unidades-organizacionais/${orgaoId}/fundos`)
    })

    it('BUSCAR_FUSO_HORARIO', async () => {
        returnedResponse = await actions[actionTypes.COMUM.BUSCAR_FUSO_HORARIO]()
        expect(url).toBe('api/fuso-horario')
    })
})
