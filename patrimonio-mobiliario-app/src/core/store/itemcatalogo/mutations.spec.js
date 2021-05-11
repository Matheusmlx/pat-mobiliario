import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {

    const state = {
        todosItens: {
            filtros: {
                conteudo: {
                    default: null,
                    label: 'Pesquisa',
                    value: ''
                }
            },
            paginacao: {
                descending: false,
                groupBy: [],
                groupDesc: [],
                itemsPerPage: 10,
                multiSort: false,
                mustSort: false,
                page: 1,
                rowsPerPage: 10,
                sortBy: ['codigo'],
                sortDesc: [false]
            }
        },
        itemCatalogo:{}
    }

    it('deve chamar a mutation ITEM_CATALOGO.SET_FILTROS_BUSCA_TODOS_ITENS e atualizar o state', () => {
        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: 'teste nome'
            }
        }
        mutations[mutationTypes.ITEM_CATALOGO.SET_FILTROS_BUSCA_TODOS_ITENS_CATALOGO](state, filtros)
        expect(state.todosItens.filtros).toEqual(filtros)
    })

    it('deve chamar a mutation ITEM_CATALOGO.SET_PAGINACAO_BUSCA_TODOS_ITENS_CATALOGO e atualizar o state', () => {
        const paginacao = {
            descending: true,
            groupBy: [],
            groupDesc: [],
            itemsPerPage: 10,
            multiSort: true,
            mustSort: true,
            page: 10,
            rowsPerPage: 10,
            sortBy: ['codigo'],
            sortDesc: [true]
        }
        mutations[mutationTypes.ITEM_CATALOGO.SET_PAGINACAO_BUSCA_TODOS_ITENS_CATALOGO](state, paginacao)
        expect(state.todosItens.paginacao).toEqual(paginacao)
    })

    it('deve chamar a mutation ITEM_CATALOGO.RESETA_PAGE e atualizar o state', () => {
        mutations[mutationTypes.ITEM_CATALOGO.RESETA_PAGE_CATALOGO](state)
        expect(state.todosItens.paginacao.page).toEqual(1)
    })

    it('deve setar ordenacao', () => {
        mutations[mutationTypes.ITEM_CATALOGO.SET_ORDENACAO_ITENS_CATALOGO](state)
        expect(state.todosItens.paginacao).toEqual(state.todosItens.paginacao)
    })

    it('deve setar catalogo', () => {
        const catalogo = {id:1}
        mutations[mutationTypes.ITEM_CATALOGO.SET_ITEM_CATALOGO](state, catalogo)
        expect(state.itemCatalogo).toEqual(catalogo)
    })
})
