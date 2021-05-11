import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {
    const state = {
        resultadoBuscaTodosIntervalosListagem: {
            items: [],
            totalItens: 0,
            totalPaginas: 0,
            paginacao: {
                page: 1,
                rowsPerPage: 10,
                sortDesc: [false],
                sortBy: ['situacao']
            }
        },
        existeEmElaboracao: false
    }

    xit('Deve setar os itens retornados a partir da busca dos intervalos da reserva', async () => {
        const resultadoBusca = {
            items: [{id: 1}, {id: 2}],
            totalElements: 2,
            totalPages: 1
        }

        mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_RESULTADO_BUSCA_TODOS_INTERVALOS](state, resultadoBusca)
        expect(state.resultadoBuscaTodosIntervalosListagem.itens).toEqual(resultadoBusca.items)
        expect(state.resultadoBuscaTodosIntervalosListagem.totalItens).toEqual(resultadoBusca.totalElements)
        expect(state.resultadoBuscaTodosIntervalosListagem.totalPaginas).toEqual(resultadoBusca.totalPages)
    })

    xit('Deve setar a paginacao dos intervalos da reserva ', () => {
        const paginacao = {
            page: 2,
            rowsPerPage: 10,
            sortDesc: [false],
            sortBy: ['situacao']
        }

        mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_PAGINACAO_BUSCA_TODOS_INTERVALOS](state, paginacao)
        expect(state.resultadoBuscaTodosIntervalosListagem.paginacao).toEqual(paginacao)
    })

    xit('Deve resetar paginacao', () => {
        mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO](state)
        expect(state.resultadoBuscaTodosIntervalosListagem.paginacao.page).toEqual(1)
    })

    xit('Deve atualizar o estado existeEmElaboracao', () => {
        mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_EXISTE_EM_ELABORACAO](state, true)
        expect(state.existeEmElaboracao).toBeTruthy()
    })
})
