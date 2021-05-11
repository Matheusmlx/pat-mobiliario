import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {

    it('deve chamar a mutation PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_INCORPORACAO_ITEM e setar os dados na state', () => {
        const state = { dadosGerais: {} }
        const incorporacaoItem = {id: 1, nome: 'teste'}

        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_INCORPORACAO_ITEM](state, incorporacaoItem)
        expect(state.dadosGerais).toEqual(incorporacaoItem)
    })

    it('deve chamar a mutation PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_INCORPORACAO e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosItensIncorporacao: {
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
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_INCORPORACAO](state, filtros)
        expect(state.resultadoBuscaTodosItensIncorporacao.filtros).toEqual(filtros)
    })

    it('deve chamar a mutation PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosItensIncorporacao: {
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
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_INCORPORACAO](state, paginacao)
        expect(state.resultadoBuscaTodosItensIncorporacao.paginacao).toEqual(paginacao)
    })

    it('deve chamar a mutation PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_INCORPORACAO_ITEM e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosItensIncorporacao: {
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
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_INCORPORACAO_ITEM](state)
        expect(state.resultadoBuscaTodosItensIncorporacao.paginacao.page).toEqual(1)
    })

    it('deve chamar a mutation PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_BUSCA_ITENS_INCORPORADOS e atualizar o state', () => {
        const state = {
            buscaRegistroItensIncorporacao: {
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
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_BUSCA_ITENS_INCORPORADOS](state)
        expect(state.buscaRegistroItensIncorporacao.paginacao.page).toEqual(1)
    })

    it('deve chamar a mutation INCORPORACAO.MODAL_INCORPORACAO_ITEM.ATIVAR_SALVAMENTO_AUTOMATICO e atualizar o state', () => {
        const state = {
            autoSave: {
                show: false
            },
        }

        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ATIVAR_SALVAMENTO_AUTOMATICO](state)
        expect(state.autoSave.show).toEqual(true)
    })

    it('deve chamar a mutation INCORPORACAO.MODAL_INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO e atualizar o state', () => {
        const state = {
            autoSave: {
                show: true
            },
        }

        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO](state)
        expect(state.autoSave.show).toEqual(false)
    })
})
