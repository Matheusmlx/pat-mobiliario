import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {
    let state = {
        resultadoBuscaTodasIncorporacoes: {
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
                sortBy: ['situacao'],
                sortDesc: [false]
            }
        },
        buscarRegistro:false,
        situacaoIncorporacao:{}
    }

    it('deve chamar a mutation INCORPORACAO.SET_FILTROS_BUSCA_TODAS_INCORPORACOES e atualizar o state', () => {

        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: 'teste nome'
            }
        }
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.SET_FILTROS_BUSCA_TODAS_INCORPORACOES](state, filtros)
        expect(state.resultadoBuscaTodasIncorporacoes.filtros).toEqual(filtros)
    })

    it('deve chamar a mutation INCORPORACAO.SET_PAGINACAO_BUSCA_TODAS_INCORPORACOES e atualizar o state', () => {
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
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.SET_PAGINACAO_BUSCA_TODAS_INCORPORACOES](state, paginacao)
        expect(state.resultadoBuscaTodasIncorporacoes.paginacao).toEqual(paginacao)
    })

    it('deve chamar a mutation INCORPORACAO.RESETA_PAGE e atualizar o state', () => {
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.RESETA_PAGE_INCORPORACAO](state)
        expect(state.resultadoBuscaTodasIncorporacoes.paginacao.page).toEqual(1)
    })

    it('Deve chamar a mutation PATRIMONIO.INCORPORACAO.SET_SITUACAO_INCORPORACAO', () => {
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.SET_SITUACAO_INCORPORACAO](state,'EM_ELABORACAO')
        expect(state.situacaoIncorporacao).toEqual('EM_ELABORACAO')
    })

    it('deve setar incorporacao', () => {
        const incorporacao = {id:1}
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.SET_INCORPORACAO](state, incorporacao)
        expect(state.incorporacao).toEqual(incorporacao)
    })

    it('deve alterar valor de buscarRegistro', () => {
        const buscarRegistro = state.buscarRegistro
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO](state)
        expect(state.buscarRegistro).toEqual(!buscarRegistro)
    })
})
