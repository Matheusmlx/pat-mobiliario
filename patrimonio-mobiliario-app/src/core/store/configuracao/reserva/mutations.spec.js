import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {
    const state = {
        resultadoBuscaTodasReservasListagem: {
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
                sortDesc: [false],
                sortBy: ['situacao']
            }
        }
    }


    it('Deve setar os filtros de todas as reservas', () => {
        const filtros = {
            conteudo: {
                default: null,
                label: 'Pesquisa',
                value: 'teste'
            }
        }

        mutations[mutationTypes.CONFIGURACAO.RESERVA.SET_FILTROS_BUSCA_TODAS_RESERVAS](state, filtros)
        expect(state.resultadoBuscaTodasReservasListagem.filtros).toEqual(filtros)
    })

    it('Deve setar a paginacao da reserva ', () => {
        const paginacao = {
            page: 2,
            rowsPerPage: 10,
            sortDesc: [false],
            sortBy: ['situacao']
        }

        mutations[mutationTypes.CONFIGURACAO.RESERVA.SET_PAGINACAO_BUSCA_TODAS_RESERVAS](state,paginacao)
        expect(state.resultadoBuscaTodasReservasListagem.paginacao).toEqual(paginacao)
    })

    it('Deve resetar paginacao',()=>{
        mutations[mutationTypes.CONFIGURACAO.RESERVA.RESETA_PAGE_RESERVA](state)
        expect(state.resultadoBuscaTodasReservasListagem.paginacao.page).toEqual(1)
    })
})
