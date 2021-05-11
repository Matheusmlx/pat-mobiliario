import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalDevolverPatrimonios from './ModalDevolverPatrimonios'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('ModalDevolverPatrimonios', () => {

    let wrapper, store, localVue, router, actions, state, mutations

    const items = {
        itens: [
            {
                patrimonioId: 1,
                patrimonioNumero: '1234',
                patrimonioDescricao: 'teste'
            },
            {
                patrimonioId: 2,
                patrimonioNumero: '4321',
                patrimonioDescricao: 'teste'
            },
            {
                patrimonioId: 4,
                patrimonioNumero: '4321',
                patrimonioDescricao: 'teste'
            },
            {
                patrimonioId: 5,
                patrimonioNumero: '4321',
                patrimonioDescricao: 'teste'
            },
            {
                patrimonioId: 6,
                patrimonioNumero: '4321',
                patrimonioDescricao: 'teste'
            }
        ],
        totalPages: 1,
        totalElements: 5
    }

    beforeEach(() => {

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Movimentacoes', hasAccess: true}]
                }
            },
            movimentacaoInternaVisualizacaoRegistroItem: {
                resultadoBuscaTodosPatrimoniosParaDevolucao: {
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
                        sortBy: ['numero'],
                        defaultSortBy: ['numero'],
                        sortDesc: [false]
                    }
                },
            }
        }

        router = {
            routes: [],
            push: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'ModalDevolverPatrimonios',
                    params: {movimentacaoInternaId: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO]: jest.fn().mockReturnValue(items),
            [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.DEVOLVER_ITENS_PARCIAL]: jest.fn(),
            [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.DEVOLVER_TODOS_PATRIMONIOS]: jest.fn()
        }

        mutations = {
            [mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO]: jest.fn(),
            [mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO]: jest.fn(),
            [mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
        }

        store = new Vuex.Store({actions, state, mutations})
    })

    const defaultMount = () => shallowMount(ModalDevolverPatrimonios, {
        store,
        router,
        localVue
    })

    describe('Computed', () => {
        it('Deve retornar as colunas da tabela para a listagem dos patrimônios', () => {
            wrapper = defaultMount()

           const colunasTabelaItensMovimentacao = [
                {
                    text: null,
                    value: 'patrimonioNumero',
                    sortable: false,
                    align: 'left',
                    width: '15%',
                    class: 'gray--text'
                },
                {
                    text: null,
                    value: 'patrimonioDescricao',
                    sortable: false,
                    align: 'left',
                    width: '85%',
                    class: 'gray--text'
                }
            ]

            expect(wrapper.vm.colunas).toEqual(colunasTabelaItensMovimentacao)
        })
    })

    describe('Watch', () => {
        it('Deve chamar tratarEventoPaginar ao mudar paginação interna', async () => {
            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.paginacaoInterna = {
                page: 2,
                rowsPerPage: 10,
                descending: false,
                sortBy: ['numero']
            }
            await flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO].mock.calls[0][1].page).toEqual(2)
        })
    })

    describe('mounted', () => {
        it('deve buscar itens ao entrar na página', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO].mock.calls[0][1].conteudo.value).toEqual('')
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.movimentacaoInternaId)
        })
    })


    describe('methods', () => {
        it('Deve colocar todos patrimonios no array de selecionados caso movimentacaoInterna.devolverTodos = true',  () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna.devolverTodos = true
            wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar = [2]
            wrapper.vm.patrimonios = [{patrimonioId:1},{patrimonioId:2},{patrimonioId:3}]
            wrapper.vm.selecionaTodosPatrimoniosSeNecessario()
            flushPromises()

            expect(wrapper.vm.movimentacaoInterna.patrimoniosId).toEqual([{patrimonioId:1},{patrimonioId:3}])
        })

        it('Não deve colocar todos patrimonios no array de selecionados caso movimentacaoInterna.devolverTodos = false',  () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna.devolverTodos = false
            wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar = [2]
            wrapper.vm.patrimonios = [{patrimonioId:1},{patrimonioId:2},{patrimonioId:3}]
            wrapper.vm.selecionaTodosPatrimoniosSeNecessario()
            flushPromises()

            expect(wrapper.vm.movimentacaoInterna.patrimoniosId).toEqual([])
        })

        it('Deve filtrar os patrimônios que não foram desselecionados', () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar = [1]
            wrapper.vm.patrimonios = [
                {patrimonioId: 1},
                {patrimonioId: 2},
                {patrimonioId: 3}
            ]

            const patrimonios = wrapper.vm.filtrarPatrimonios(wrapper.vm.patrimonios)

            expect(patrimonios).toEqual([{patrimonioId: 2}, {patrimonioId: 3}])
        })

        it('Deve setar movimentacaoInterna.devolverTodos = true se props.value = false(checkbox de seleção de todos patrimonios marcado)',  () => {
            wrapper = defaultMount()
            wrapper.vm.selecionaTodosPatrimoniosTodasPaginas({value:false})

            flushPromises()

            expect(wrapper.vm.movimentacaoInterna.devolverTodos).toEqual(true)
        })

        it('Deve setar movimentacaoInterna.devolverTodos = false se props.value = true(checkbox de seleção de todos patrimonios não marcado)',  () => {
            wrapper = defaultMount()
            wrapper.vm.selecionaTodosPatrimoniosTodasPaginas({value:true})
            flushPromises()

            expect(wrapper.vm.movimentacaoInterna.devolverTodos).toEqual(false)
        })

        it('Deve limpar a lista de patrimônios que foram desselecionados', () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar = [1]

            wrapper.vm.resetarPatrimoniosExcecao()

            expect(wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar).toEqual([])
        })

        it('Deve desselecionar o patrimônio se devolverTodos = false', () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna.devolverTodos = false
            wrapper.vm.movimentacaoInterna.patrimoniosId = [
                {patrimonioId: 1},
                {patrimonioId: 2},
                {patrimonioId: 3}
            ]
            wrapper.vm.desselecionarPatrimonio({patrimonioId: 2})

            expect(wrapper.vm.movimentacaoInterna.patrimoniosId).toEqual([{patrimonioId: 1}, {patrimonioId: 3}])
        })

        it('Deve desselecionar o patrimônio se devolverTodos = true', () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna.devolverTodos = true
            wrapper.vm.movimentacaoInterna.patrimoniosId = [
                {patrimonioId: 1},
                {patrimonioId: 2},
                {patrimonioId: 3}
            ]
            wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar = []
            wrapper.vm.desselecionarPatrimonio({patrimonioId: 2})

            expect(wrapper.vm.movimentacaoInterna.patrimoniosId).toEqual([{patrimonioId: 1}, {patrimonioId: 3}])
            expect(wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar).toEqual([2])
        })

        it('Deve selecionar o patrimônio se devolverTodos = false', () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna.devolverTodos = false
            wrapper.vm.movimentacaoInterna.patrimoniosId = []
            wrapper.vm.selecionarPatrimonio({patrimonioId: 1})

            expect(wrapper.vm.movimentacaoInterna.patrimoniosId).toEqual([{patrimonioId: 1}])

        })

        it('Deve selecionar o patrimônio se devolverTodos = true', () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna.devolverTodos = true
            wrapper.vm.movimentacaoInterna.patrimoniosId = []
            wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar = [1]
            wrapper.vm.selecionarPatrimonio({patrimonioId: 1})

            expect(wrapper.vm.movimentacaoInterna.patrimoniosId).toEqual([{patrimonioId: 1}])
            expect(wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar).toEqual([])
        })

        it('Deve remover filtro',  () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoRemoverFiltro('conteudo')
            flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toBe(null)
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.movimentacaoInternaId)
        })

        it('Deve realizar busca simples', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoBuscaSimples('simples')
            await flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('simples')
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.movimentacaoInternaId)
        })

        it('Deve paginar',  () => {
            wrapper = defaultMount()
            const paginacao = {sortBy: ['numero']}
            wrapper.vm.tratarEventoPaginar(paginacao)
            flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO].mock.calls[0][1]).toEqual(paginacao)
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO].mock.calls[0][1]).toEqual(1)
        })

        it('Deve resetar paginacao',  () => {
            wrapper = defaultMount()
            wrapper.vm.resetaPage()
            flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO]).toHaveBeenCalled()
        })

        it('Deve resetar sortBy',  () => {
            wrapper = defaultMount()
            const paginacao = {sortBy: [], defaultSortBy:'default'}
            wrapper.vm.resetarPaginacaoSortBy(paginacao)
            flushPromises()

            expect(state.movimentacaoInternaVisualizacaoRegistroItem.resultadoBuscaTodosPatrimoniosParaDevolucao.paginacao.sortBy[0]).toEqual(paginacao.defaultSortBy)
        })

        it('Deve fechar modal', async () => {
            wrapper = defaultMount()
            wrapper.vm.fecharModal()
            await flushPromises()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarRegistroMovimentacaoInterna', params: {movimentacaoInternaId: wrapper.vm.$route.params.movimentacaoInternaId}})
        })

        it('deve devolver Itens', async () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna = {patrimoniosId:[{patrimonioId:1}]}
            const envio = {patrimoniosId: [1]}
            await wrapper.vm.tratarEventoDevolver()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.DEVOLVER_ITENS_PARCIAL]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.DEVOLVER_ITENS_PARCIAL].mock.calls[0][1]).toEqual(envio)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1]).toEqual({message: 'Operação realizada com sucesso', type: 'success'})
            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarRegistroMovimentacaoInterna', params: {movimentacaoInternaId: wrapper.vm.$route.params.movimentacaoInternaId}})
        })

        it('Deve pegar apenas id dos patrimonios selecionados', () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna = {patrimoniosId:[{patrimonioId:1}]}

            flushPromises()
            expect(wrapper.vm.filtraIdPatrimoniosSelecionados()).toEqual([1])
        })

        it('Deve retornar quantidade de patrimonios selecionados se todosSelecionados = true', async () => {
            items.totalElementsOfAllPages = 5
            actions = {
                [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO]: jest.fn().mockReturnValue(items),
                [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.DEVOLVER_ITENS_PARCIAL]: jest.fn()
            }
            wrapper = shallowMount(ModalDevolverPatrimonios, {
                store: new Vuex.Store({actions, state, mutations}),
                router,
                localVue
            })

            await flushPromises()

            wrapper.vm.movimentacaoInterna.devolverTodos = true
            wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar = [1,2]
            wrapper.vm.calculaQuantidadePatrimoniosSelecionados()

            expect(wrapper.vm.quantidadeSelecionados).toEqual(3)
        })

        it('Deve retornar quantidade de patrimonios selecionados se todosSelecionados = false', async () => {
            items.totalElementsOfAllPages = 5
            actions = {
                [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO]: jest.fn().mockReturnValue(items),
                [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.DEVOLVER_ITENS_PARCIAL]: jest.fn()
            }

            wrapper = shallowMount(ModalDevolverPatrimonios, {
                store: new Vuex.Store({actions, state, mutations}),
                router,
                localVue
            })

            await flushPromises()

            wrapper.vm.movimentacaoInterna.todosSelecionados = false
            wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar = [1,2]
            wrapper.vm.movimentacaoInterna.patrimoniosId = [1,2,3,4]
            wrapper.vm.calculaQuantidadePatrimoniosSelecionados()

            expect(wrapper.vm.quantidadeSelecionados).toEqual(4)
        })

        it('Deve retornar filtros',  () => {
            wrapper = defaultMount()
            flushPromises()

            expect(wrapper.vm.getFiltros()).toEqual(state.movimentacaoInternaVisualizacaoRegistroItem.resultadoBuscaTodosPatrimoniosParaDevolucao.filtros)
        })

        it('Deve retornar paginacao',  () => {
            wrapper = defaultMount()
            flushPromises()

            expect(wrapper.vm.getPaginacao()).toEqual(state.movimentacaoInternaVisualizacaoRegistroItem.resultadoBuscaTodosPatrimoniosParaDevolucao.paginacao)
        })

        it('Deve retornar filtrosInternos', () => {
            wrapper = defaultMount()
            flushPromises()

            expect(wrapper.vm.getFiltrosInterno()).toEqual(wrapper.vm.filtrosInterno)
        })

        it('Deve buscar os itens a serem devolvidos', async() => {
            wrapper = defaultMount()

            await wrapper.vm.buscaTodosPatrimonios()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO]).toBeCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO].mock.calls[0][1]).toBe(wrapper.vm.$route.params.movimentacaoInternaId)
        })

    })
})
