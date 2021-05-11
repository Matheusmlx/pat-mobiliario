import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {
    const state = {
        contasContabeis: {},
        todasContasContabeis: {
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
                rowsPerPage: 5,
                sortBy: ['codigo'],
                sortDesc: [false]
            }
        }
    }
    it('Deve setar a conta contabil vinda da api', () => {

        const responseApi = {
            items: {
                id: 1,
                codigo: 123456,
                descricao: 'MÁQUINAS,FERRAMENTAS',
                situacao: 'ATIVO'
            }

        }
        mutations[mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_CONTA_CONTABIL](state, responseApi)
        expect(state.contasContabeis).toEqual(responseApi.items)

    })

    it('Deve setar os filtros de todas as contasContabeis', () => {
        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: 'teste'
            }
        }

        mutations[mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_FILTROS_BUSCA_TODAS_CONTAS_CONTABEIS](state, filtros)
        expect(state.todasContasContabeis.filtros).toEqual(filtros)
    })
    it('Deve setar a paginacao ', () => {
        const paginacao = {
            descending: false,
            groupBy: [],
            groupDesc: [],
            itemsPerPage: 10,
            multiSort: false,
            mustSort: false,
            page: 1,
            rowsPerPage: 5,
            sortBy: ['codigo'],
            sortDesc: [false]
        }

        mutations[mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_PAGINACAO_BUSCA_TODAS_CONTAS_CONTABEIS](state,paginacao)
        expect(state.todasContasContabeis.paginacao).toEqual(paginacao)
    })

    it('Deve limpar o filtro atual pelo filtroPadrao',()=>{
        const filtros = {
            conteudo: '',
            objeto: {
                value: null,
                default: null,
                label: 'Pesquisa'
            },
            categoria: {
                value: null,
                default: null,
                label: 'Categoria'
            }
        }

        mutations[mutationTypes.CONFIGURACAO.CONTA_CONTABIL.SET_LIMPAR_FILTROS_BUSCA_CONTA](state)
        expect(state.todasContasContabeis.filtros).toEqual(filtros)
    })

    it('Deve resetar paginacao',()=>{
        mutations[mutationTypes.CONFIGURACAO.CONTA_CONTABIL.RESETA_PAGE_CONTA_CONTABIL](state)
        expect(state.todasContasContabeis.paginacao.page).toEqual(1)
    })
})
