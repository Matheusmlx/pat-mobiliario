import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {
    const state = {
        todosReconhecimentos: {
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
        }
    }

    it('deve chamar a mutation RECONHECIMENTO.SET_FILTROS_BUSCA_TODOS_RECONHECIMENTOS e atualizar o state', () => {
        mutations[mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_FILTROS_BUSCA_TODOS_RECONHECIMENTOS](state, state.filtros)
        expect(state.todosReconhecimentos.filtros).toEqual(state.filtros)
    })

    it('deve chamar a mutation RECONHECIMENTO.SET_PAGINACAO_BUSCA_TODOS_RECONHECIMENTOS e atualizar o state', () => {
        mutations[mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_PAGINACAO_BUSCA_TODOS_RECONHECIMENTOS](state, state.paginacao)
        expect(state.todosReconhecimentos.paginacao).toEqual(state.paginacao)
    })

    it('deve setar paginacao', () => {
        const paginacao= {
            descending: false,
                groupBy: [],
                groupDesc: [],
                itemsPerPage: 10,
                multiSort: false,
                mustSort: false,
                page: 2,
                rowsPerPage: 10,
                sortBy: ['codigo'],
                sortDesc: [false]
        }
        mutations[mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_PAGINACAO_BUSCA_TODOS_RECONHECIMENTOS](state, paginacao)
        expect(state.todosReconhecimentos.paginacao).toEqual(paginacao)
    })

    it('Deve resetar paginacao',()=>{
        mutations[mutationTypes.CONFIGURACAO.RECONHECIMENTO.RESETA_PAGE_RECONHECIMENTO](state)
        expect(state.todosReconhecimentos.paginacao.page).toEqual(1)
    })

})
