import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {

    it('deve chamar a mutation CONVENIO.SET_FILTROS_BUSCA_TODOS_CONVENIOS e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosConvenios: {
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
        mutations[mutationTypes.CONFIGURACAO.CONVENIO.SET_FILTROS_BUSCA_TODOS_CONVENIOS](state, filtros)
        expect(state.resultadoBuscaTodosConvenios.filtros).toEqual(filtros)
    })

    it('deve chamar a mutation CONVENIO.SET_PAGINACAO_BUSCA_TODOS_CONVENIOS e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosConvenios: {
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
        mutations[mutationTypes.CONFIGURACAO.CONVENIO.SET_PAGINACAO_BUSCA_TODOS_CONVENIOS](state, paginacao)
        expect(state.resultadoBuscaTodosConvenios.paginacao).toEqual(paginacao)
    })

    it('deve chamar a mutation CONVENIO.SET_SET_CONVENIO e atualizar o state', () => {
        const state = {
            dadosGerais: {
                id: 1,
                numero: '095848',
                nome: 'Caixa Seguradora de Bens Social',
                concedente: {
                    id: 1, nome: 'Bradesco Seguros'
                },
                fonteRecurso: {
                    id: 2, nome: 'Aquisição de protótipos'
                },
                situacao: 'INATIVO',
                dataHoraInicial: '2020-07-15T23:00:00.000-0400',
                dataHoraFinal: '2020-07-16T23:00:00.000-0400'
            }
        }
        const dadosGerais = {
            id: 1,
            numero: '095848',
            nome: 'Caixa Seguradora de Bens Social',
            concedente: {
                id: 1, nome: 'Bradesco Seguros'
            },
            fonteRecurso: {
                id: 2, nome: 'Aquisição de protótipos'
            },
            situacao: 'INATIVO',
            dataHoraInicial: '2020-07-15T23:00:00.000-0400',
            dataHoraFinal: '2020-07-16T23:00:00.000-0400'
        }
        mutations[mutationTypes.CONFIGURACAO.CONVENIO.SET_CONVENIO](state, dadosGerais)
        expect(state.dadosGerais).toEqual(dadosGerais)
    })

    it('deve chamar a mutation CONVENIO.RESETA_PAGE e atualizar o state', () => {
        const state = {
            resultadoBuscaTodosConvenios: {
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
        mutations[mutationTypes.CONFIGURACAO.CONVENIO.RESETA_PAGE_CONVENIO](state)
        expect(state.resultadoBuscaTodosConvenios.paginacao.page).toEqual(1)
    })

    it('deve setar ordenacao', () => {
        const state = {
            resultadoBuscaTodosConvenios: {
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
        mutations[mutationTypes.CONFIGURACAO.CONVENIO.SET_PAGINACAO_BUSCA_TODOS_CONVENIOS](state, 'cadastro')
        expect(state.resultadoBuscaTodosConvenios.paginacao).toEqual('cadastro')
    })
})
