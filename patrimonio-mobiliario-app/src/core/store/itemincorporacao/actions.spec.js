import actions from './actions'
import {actionTypes} from '@/core/constants'

let url, body, mockResponseData, returnedResponse

jest.mock('axios', () => ({

    delete(_url) {
        return new Promise((resolve) => {
            url = _url
            resolve({data: mockResponseData})
        })
    },
    get(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({data: mockResponseData})
        })
    },
    put(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
    post(_url, _body) {
        return new Promise(resolve => {
            url = _url
            body = _body
            resolve({ data: mockResponseData })
        })
    },
}))

describe('Actions', () => {
    const context = jest.fn()
    const commit = jest.fn()
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
            resultadoBuscaTodosItensIncorporacao: {
                filtros: {
                    conteudo: {
                        default: null,
                        label: 'Pesquisa',
                        value: ''
                    }
                },
                paginacao: {
                    page: 1,
                    rowsPerPage: 10,
                    sortBy: ['codigo'],
                    sortDesc: [false]
                }
            },
        }
    })

    it('busca item incorporacao por id', async () => {
        const dados = { idIncorporacao: 5, idItem: 5 }
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO](context, dados)
        expect(url).toBe(`api/patrimonios/incorporacao/${dados.idIncorporacao}/item/${dados.idItem}`)
    })

    it('busca item incorporacao por id (registro/visualização)', async () => {
        const dados = {  id: 5 }
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO_REGISTRO](context, dados)
        expect(url).toBe(`api/incorporacao/item/${dados.id}/visualizar`)
    })

    it('BUSCAR_TODOS_ITENS_INCORPORACAO', async () => {
        const id_incorporacao = 1
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]({state}, id_incorporacao)
        expect(url).toBe('api/patrimonios/incorporacao/itens/1?page=1&size=10&sort=codigo&direction=ASC')
    })

    it('DELETAR_ITEM_INCORPORACAO', async () => {
        const dados = { idIncorporacao: 5, idItem: 5 }
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DELETAR_ITEM_INCORPORACAO](context, dados)
        expect(url).toBe(`api/patrimonios/incorporacao/${dados.idIncorporacao}/item/${dados.idItem}`)
    })

    it('editar item incorporacao', async () => {
        const dados = { idIncorporacao: 5, id: 5 }
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]({commit}, dados)
        expect(url).toBe(`api/patrimonios/incorporacao/${dados.idIncorporacao}/item/${dados.id}`)
    })

    it('upload imagem', async () => {
        const imagem = { uriImagem: 'uri' }
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.UPLOAD]({commit}, imagem)
        expect(url).toBe('api/v1/arquivos')
    })

    it('download imagem', async () => {
        const imagem = { uriImagem: 'uri' }
        window.URL.createObjectURL = jest.fn()
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.DOWNLOAD]({commit}, imagem)
        expect(url).toBe('api/v1/arquivos')
    })

    it('cadastrar item incorporacao', async () => {
        const dados = { idIncorporacao: 5, id: 5 }
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.CADASTRAR_ITEM_INCORPORACAO]({commit}, dados)
        expect(url).toBe(`api/patrimonios/incorporacao/${dados.idIncorporacao}/item`)
    })

    it('BUSCAR_ESTADOS_CONSERVACAO', async () => {
        returnedResponse = await actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO]()
        expect(url).toBe('api/patrimonios/incorporacao/itens/estadosconservacao')
    })
})
