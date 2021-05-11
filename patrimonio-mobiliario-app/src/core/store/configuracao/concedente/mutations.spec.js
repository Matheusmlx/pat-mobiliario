import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {

    it('deve chamar a mutation CONCEDENTE.SET_FILTROS_BUSCA_TODOS_CONCEDENTES e atualizar o state', () => {
        const state = {
            todosConcedentes: {
                filtros: {
                    conteudo: {
                        default: null,
                        label: 'Pesquisa',
                        value: ''
                    }
                }
            }
        }
        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: 'teste nome'
            }
        }
        mutations[mutationTypes.CONFIGURACAO.CONCEDENTE.SET_FILTROS_BUSCA_TODOS_CONCEDENTES](state, filtros)
        expect(state.todosConcedentes.filtros).toEqual(filtros)
    })

    it('deve chamar a mutation CONCEDENTE.SET_PAGINACAO_BUSCA_TODOS_CONCEDENTES e atualizar o state', () => {
        const state = {
            todosConcedentes: {
                paginacao: {
                    descending: false,
                    groupBy: [],
                    groupDesc: [],
                    itemsPerPage: 10,
                    multiSort: false,
                    mustSort: false,
                    page: 1,
                    rowsPerPage: 10,
                    sortBy: ['situacao'],
                    sortDesc: [false]
                }
            }
        }
        const paginacao = {
            descending: true,
            groupBy: [],
            groupDesc: [],
            itemsPerPage: 10,
            multiSort: true,
            mustSort: true,
            page: 10,
            rowsPerPage: 10,
            sortBy: ['nome'],
            sortDesc: [true]
        }
        mutations[mutationTypes.CONFIGURACAO.CONCEDENTE.SET_PAGINACAO_BUSCA_TODOS_CONCEDENTES](state, paginacao)
        expect(state.todosConcedentes.paginacao).toEqual(paginacao)
    })

    it('deve chamar a mutation CONCEDENTE.RESETA_PAGE e atualizar o state', () => {
        const state = {
            todosConcedentes: {
                paginacao: {
                    descending: false,
                    groupBy: [],
                    groupDesc: [],
                    itemsPerPage: 10,
                    multiSort: false,
                    mustSort: false,
                    page: 10,
                    rowsPerPage: 10,
                    sortBy: ['situacao'],
                    sortDesc: [false]
                }
            }
        }
        mutations[mutationTypes.CONFIGURACAO.CONCEDENTE.RESETA_PAGE_CONCEDENTE](state)
        expect(state.todosConcedentes.paginacao.page).toEqual(1)
    })

    it('deve setar ordenacao', () => {
        const state = {
            todosConcedentes: {
                paginacao: {
                    descending: false,
                    groupBy: [],
                    groupDesc: [],
                    itemsPerPage: 10,
                    multiSort: false,
                    mustSort: false,
                    page: 1,
                    rowsPerPage: 10,
                    sortBy: ['situacao'],
                    sortDesc: [false]
                }
            }
        }
        mutations[mutationTypes.CONFIGURACAO.CONCEDENTE.SET_PAGINACAO_BUSCA_TODOS_CONCEDENTES](state, 'cadastro')
        expect(state.todosConcedentes.paginacao).toEqual('cadastro')
    })

    it('deve setar concedente', () => {
        const state = {concedentes: {}}
        mutations[mutationTypes.CONFIGURACAO.CONCEDENTE.SET_CONCEDENTE](state, 'cadastro')
        expect(state.concedentes).toEqual('cadastro')
    })
})
