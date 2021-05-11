import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {
    const state = {
        todosOrgaos: {
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

    xit('Deve setar os filtros para buscar os órgão da reserva', () => {
        mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.SET_FILTRO_BUSCA_ORGAOS_RESERVA](state)
        expect(state.todosOrgaos.paginacao.page).toEqual(1)
    })

    xit('Deve setar a paginacao dos órgãos da reserva ', () => {
        const paginacao = {
            page: 2,
            rowsPerPage: 10,
            sortDesc: [false],
            sortBy: ['situacao']
        }

        mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.SET_PAGINACAO_BUSCA_ORGAOS_RESERVA](state, paginacao)
        expect(state.todosOrgaos.paginacao).toEqual(paginacao)
    })

    xit('Deve resetar paginacao dos órgãos da reserva',()=>{
        mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.RESETA_PAGE_ORGAOS_RESERVA](state)
        expect(state.todosOrgaos.paginacao.page).toEqual(1)
    })
})
