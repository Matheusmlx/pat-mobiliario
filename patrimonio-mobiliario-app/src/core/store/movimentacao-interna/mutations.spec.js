import mutations from './mutations'
import {mutationTypes} from '@/core/constants'


const state = {
    movimentacaoInterna: {},
    resultadoBuscaTodasMovimentacoesInternas: {
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
            defaultSortBy: ['codigo'],
            sortDesc: [false]
        }
    },
    situacaoMovimentacaoInterna: ''
}

describe('Mutations', () => {

    it('deve chamar a mutation MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA e atualizar o state', () => {
        const movimentacao = {motivoObservacao: 'motivo'}
        mutations[mutationTypes.MOVIMENTACAO_INTERNA.SET_MOVIMENTACAO_INTERNA](state, movimentacao)
        expect(state.movimentacaoInterna).toEqual(movimentacao)
    })

    it('RESETA_PAGE_BUSCA_TODAS_MOVIMENTACOES_INTERNAS', () => {
        state.resultadoBuscaTodasMovimentacoesInternas.paginacao.page = 2
        mutations[mutationTypes.MOVIMENTACAO_INTERNA.RESETA_PAGE_BUSCA_TODAS_MOVIMENTACOES_INTERNAS](state)
        expect(state.resultadoBuscaTodasMovimentacoesInternas.paginacao.page).toEqual(1)
    })

    it('SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES_INTERNAS', () => {
        const paginacao = {
            page: 2,
            rowsPerPage: 15,
            itemsPerPage: 20,
            sortBy: ['codigo'],
            defaultSortBy: ['codigo'],
            sortDesc: [false]
        }
        mutations[mutationTypes.MOVIMENTACAO_INTERNA.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES_INTERNAS](state, paginacao)
        expect(state.resultadoBuscaTodasMovimentacoesInternas.paginacao).toEqual(paginacao)
    })

    it('SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES_INTERNAS', () => {
        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: 'conteúdo'
            }
        }
        mutations[mutationTypes.MOVIMENTACAO_INTERNA.SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES_INTERNAS](state, filtros)
        expect(state.resultadoBuscaTodasMovimentacoesInternas.filtros).toEqual(filtros)
    })

    it('SET_SITUACAO_MOVIMENTACAO_INTERNA', () => {
        const situacao = 'FINALIZADO'
        mutations[mutationTypes.MOVIMENTACAO_INTERNA.SET_SITUACAO_MOVIMENTACAO_INTERNA](state, situacao)
        expect(state.situacaoMovimentacaoInterna).toEqual(situacao)
    })
})
