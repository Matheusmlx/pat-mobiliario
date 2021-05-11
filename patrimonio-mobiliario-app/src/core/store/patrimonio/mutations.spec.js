import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {

    it('deve chamar a mutation PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosPatrimonios: {
                paginacao: {
                    descending: false,
                    groupBy: [],
                    groupDesc: [],
                    itemsPerPage: 10,
                    multiSort: false,
                    mustSort: false,
                    page: 1,
                    rowsPerPage: 10,
                    sortBy: ['numeroPatrimonio'],
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
            sortBy: ['numeroPatrimonio'],
            sortDesc: [true]
        }
        mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS](state, paginacao)
        expect(state.resultadoBuscaTodosPatrimonios.paginacao).toEqual(paginacao)
    })

    it('deve chamar a mutation PATRIMONIO.RESETA_PAGE_PATRIMONIO e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosPatrimonios: {
                paginacao: {
                    descending: false,
                    groupBy: [],
                    groupDesc: [],
                    itemsPerPage: 10,
                    multiSort: false,
                    mustSort: false,
                    page: 10,
                    rowsPerPage: 10,
                    sortBy: ['numeroPatrimonio'],
                    sortDesc: [false]
                }
            }
        }
        mutations[mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO](state)
        expect(state.resultadoBuscaTodosPatrimonios.paginacao.page).toEqual(1)
    })

    it('deve chamar a mutation PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_REGISTRO e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosPatrimoniosRegistro: {
                paginacao: {
                    descending: false,
                    groupBy: [],
                    groupDesc: [],
                    itemsPerPage: 10,
                    multiSort: false,
                    mustSort: false,
                    page: 1,
                    rowsPerPage: 10,
                    sortBy: ['numeroPatrimonio'],
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
            sortBy: ['numeroPatrimonio'],
            sortDesc: [true]
        }
        mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_REGISTRO](state, paginacao)
        expect(state.resultadoBuscaTodosPatrimoniosRegistro.paginacao).toEqual(paginacao)
    })

    it('deve chamar a mutation PATRIMONIO.RESETA_PAGE_PATRIMONIO_REGISTRO e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosPatrimoniosRegistro: {
                paginacao: {
                    descending: false,
                    groupBy: [],
                    groupDesc: [],
                    itemsPerPage: 10,
                    multiSort: false,
                    mustSort: false,
                    page: 10,
                    rowsPerPage: 10,
                    sortBy: ['numeroPatrimonio'],
                    sortDesc: [false]
                }
            }
        }
        mutations[mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_REGISTRO](state)
        expect(state.resultadoBuscaTodosPatrimoniosRegistro.paginacao.page).toEqual(1)
    })

    it('deve chamar a mutation PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_LISTAGEM e atualizar o state', () => {

        const state = {
            resultadoBuscaTodosPatrimoniosListagem: {
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
                    page: 10,
                    rowsPerPage: 10,
                    sortBy: ['orgao'],
                    sortDesc: [false]
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
        mutations[mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_LISTAGEM](state, filtros)
        expect(state.resultadoBuscaTodosPatrimoniosListagem.filtros).toEqual(filtros)
    })

    it('deve chamar a mutation PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_LISTAGEM e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosPatrimoniosListagem: {
                paginacao: {
                    descending: false,
                    groupBy: [],
                    groupDesc: [],
                    itemsPerPage: 10,
                    multiSort: false,
                    mustSort: false,
                    page: 10,
                    rowsPerPage: 10,
                    sortBy: ['orgao'],
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
        mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_LISTAGEM](state, paginacao)
        expect(state.resultadoBuscaTodosPatrimoniosListagem.paginacao).toEqual(paginacao)
    })

    it('deve chamar a mutation PATRIMONIO.RESETA_PAGE_PATRIMONIO_LISTAGEM e atualizar o state', () => {

        const state = {
            resultadoBuscaTodosPatrimoniosListagem: {
                paginacao: {
                    descending: false,
                    groupBy: [],
                    groupDesc: [],
                    itemsPerPage: 10,
                    multiSort: false,
                    mustSort: false,
                    page: 10,
                    rowsPerPage: 10,
                    sortBy: ['orgao'],
                    sortDesc: [false]
                }
            }
        }

        mutations[mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_LISTAGEM](state)
        expect(state.resultadoBuscaTodosPatrimoniosListagem.paginacao.page).toEqual(1)
    })

    it('deve chamar a mutation PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosPatrimoniosDeTodosItens:{
                paginacao: {
                    itemsPerPage: 10,
                    page: 1,
                    rowsPerPage: 10,
                    sortBy: ['numero'],
                    defaultSortBy: ['numero'],
                    sortDesc: [false],
                }
            },
        }

        const paginacao = {
            itemsPerPage: 25,
            page: 1,
            rowsPerPage: 10,
            sortBy: ['numero'],
            defaultSortBy: ['numero'],
            sortDesc: [false],
        }
        mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS](state, paginacao)
        expect(state.resultadoBuscaTodosPatrimoniosDeTodosItens.paginacao).toEqual(paginacao)
    })

    it('deve chamar a mutation PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosPatrimoniosDeTodosItens:{
                filtros: {
                    conteudo: {
                        default: null,
                        label: 'Pesquisa',
                        value: ''
                    }
                }
            },
        }

        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: 'teste nome'
            }
        }
        mutations[mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS](state, filtros)
        expect(state.resultadoBuscaTodosPatrimoniosDeTodosItens.filtros).toEqual(filtros)
    })

    it('deve chamar a mutation PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS e atualizar o state', () => {

        const state = {
            resultadoBuscaTodosPatrimoniosDeTodosItens:{
                paginacao: {
                    itemsPerPage: 10,
                    page: 2,
                    rowsPerPage: 10,
                    sortBy: ['numero'],
                    defaultSortBy: ['numero'],
                    sortDesc: [false],
                }
            },
        }

        mutations[mutationTypes.PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS](state)
        expect(state.resultadoBuscaTodosPatrimoniosDeTodosItens.paginacao.page).toEqual(1)
    })

    it('deve chamar a mutation PATRIMONIO.SET_ESTORNO e atualizar o state', () => {
        const state = {
            estorno: {
                motivo: null
            }
        }

        const estorno = {motivo: 'motivo1'}
        mutations[mutationTypes.PATRIMONIO.SET_ESTORNO](state, estorno)
        expect(state.estorno).toEqual(estorno)
    })

    it('deve chamar a mutation PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS', () => {
        const state = {
            resultadoBuscaTodosPatrimoniosEstornados: {
                paginacao: {
                    descending: false,
                    groupBy: [],
                    groupDesc: [],
                    itemsPerPage: 10,
                    multiSort: false,
                    mustSort: false,
                    page: 1,
                    rowsPerPage: 10,
                    sortBy: ['numero'],
                    defaultSortBy: ['numero'],
                    sortDesc: [false]
                }
            }
        }

        const paginacao = {
            descending: false,
                groupBy: [],
                groupDesc: [],
                itemsPerPage: 10,
                multiSort: false,
                mustSort: false,
                page: 3,
                rowsPerPage: 15,
                sortBy: ['numero'],
                defaultSortBy: ['numero'],
                sortDesc: [false]
        }

        mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS](state, paginacao)
        expect(state.resultadoBuscaTodosPatrimoniosEstornados.paginacao.page).toEqual(3)
        expect(state.resultadoBuscaTodosPatrimoniosEstornados.paginacao.rowsPerPage).toEqual(15)
    })

    it('deve chamar a mutation PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS e atualizar o state', () => {

        const state = {
            resultadoBuscaTodosPatrimoniosEstornados: {
                paginacao: {
                    itemsPerPage: 10,
                    page: 2,
                    rowsPerPage: 10,
                    sortBy: ['numero'],
                    defaultSortBy: ['numero'],
                    sortDesc: [false],
                }
            }
        }

        mutations[mutationTypes.PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS](state)
        expect(state.resultadoBuscaTodosPatrimoniosEstornados.paginacao.page).toEqual(1)
    })
})
