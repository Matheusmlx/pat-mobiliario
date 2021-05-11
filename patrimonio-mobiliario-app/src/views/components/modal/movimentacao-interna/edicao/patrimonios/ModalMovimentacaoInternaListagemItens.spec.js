import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalMovimentacaoInternaListagemItens from './ModalMovimentacaoInternaListagemItens'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('ModalMovimentacaoInternaListagemItens', () => {

    let wrapper, store, localVue, router, actions, state, mutations

    const Itens = {
        items:[
            {
                patrimonioId:1,
                patrimonioNumero: '000493',
                patrimonioDescricao: 'descrição',
                incorporacaoCodigo: '0082832'
            }
        ],
        totalPages: 1,
        totalElements: 1,
        totalElementsOfAllPages: 1
    }

    beforeEach(() => {

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Movimentacoes', hasAccess: true}]
                }
            },
            movimentacaoInternaItem: {
                resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna: {
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
            replace: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'ModalMovimentacaoInternaListagemItens',
                    params: { movimentacaoInternaId: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(Itens),
            [actionTypes.MOVIMENTACAO_INTERNA.ITEM.ADICIONAR_ITENS_MOVIMENTACAO_INTERNA]: jest.fn(),
            [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DISTRIBUICAO')
        }

        mutations = {
            [mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]: jest.fn(),
            [mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]: jest.fn(),
            [mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]: jest.fn()
        }

        store = new Vuex.Store({actions, state, mutations})
    })

    const defaultMount = () => shallowMount(ModalMovimentacaoInternaListagemItens, {
        store,
        router,
        localVue
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

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA].mock.calls[0][1].page).toEqual(2)
        })
    })

    describe('mounted', () => {
        it('deve buscar itens ao entrar na página', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA].mock.calls[0][1].conteudo.value).toEqual('')
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.movimentacaoInternaId)
        })
    })


    describe('methods', () => {
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

        it('Deve remover filtro',  () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoRemoverFiltro('conteudo')
            flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toBe(null)
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.movimentacaoInternaId)
        })

        it('Deve realizar busca simples', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoBuscaSimples('simples')
            await flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('simples')
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.movimentacaoInternaId)
        })

        it('Deve paginar',  () => {
            wrapper = defaultMount()
            const paginacao = {sortBy: ['numero']}
            wrapper.vm.tratarEventoPaginar(paginacao)
             flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(paginacao)
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(1)
        })

        it('Deve resetar paginacao',  () => {
            wrapper = defaultMount()
            wrapper.vm.resetaPage()
            flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
        })

        it('Deve resetar sortBy',  () => {
            wrapper = defaultMount()
            const paginacao = {sortBy: [], defaultSortBy:'default'}
            wrapper.vm.resetarPaginacaoSortBy(paginacao)
            flushPromises()

            expect(state.movimentacaoInternaItem.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.paginacao.sortBy[0]).toEqual(paginacao.defaultSortBy)
        })

        it('Deve fechar modal', async () => {
            wrapper = defaultMount()
            wrapper.vm.fecharModal()
            await flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoItens', params: {movimentacaoInternaId: wrapper.vm.$route.params.movimentacaoInternaId}})
        })

        it('deve adicionar Itens', async () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna = {patrimoniosId:[{patrimonioId:1}]}
            const envio = {patrimoniosId: [1]}
            await wrapper.vm.tratarEventoAdicionar()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.ADICIONAR_ITENS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.ADICIONAR_ITENS_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(envio)
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoItens', params: {movimentacaoInternaId: wrapper.vm.$route.params.movimentacaoInternaId}})
        })

        it('Deve pegar apenas id dos patrimonios selecionados', () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacaoInterna = {patrimoniosId:[{patrimonioId:1}]}

            flushPromises()
            expect(wrapper.vm.filtraIdPatrimoniosSelecionados()).toEqual([1])
        })

        it('Deve retornar quantidade de patrimonios selecionados se todosSelecionados = false', async () => {
            Itens.totalElementsOfAllPages = 5
            actions = {
                [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(Itens),
                [actionTypes.MOVIMENTACAO_INTERNA.ITEM.ADICIONAR_ITENS_MOVIMENTACAO_INTERNA]: jest.fn(),
                [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DISTRIBUICAO')
            }
            wrapper = shallowMount(ModalMovimentacaoInternaListagemItens, {
                store: new Vuex.Store({actions, state, mutations}),
                router,
                localVue
            })
            await flushPromises()
            wrapper.vm.movimentacaoInterna.todosSelecionados = false
            wrapper.vm.movimentacaoInterna.patrimoniosNaoConsiderar = [1,2]
            wrapper.vm.movimentacaoInterna.patrimoniosId = [1,2,3,4]
            wrapper.vm.calculaQuantidadePatrimoniosSelecionados()
            flushPromises()
            expect(wrapper.vm.quantidadeSelecionados).toEqual(4)
        })

        it('Deve retornar filtros',  () => {
            wrapper = defaultMount()
            flushPromises()

            expect(wrapper.vm.getFiltros()).toEqual(state.movimentacaoInternaItem.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.filtros)
        })

        it('Deve retornar paginacao',  () => {
            wrapper = defaultMount()
            flushPromises()

            expect(wrapper.vm.getPaginacao()).toEqual(state.movimentacaoInternaItem.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.paginacao)
        })

        it('Deve retornar filtrosInternos',  () => {
            wrapper = defaultMount()
            flushPromises()

            expect(wrapper.vm.getFiltrosInterno()).toEqual(wrapper.vm.filtrosInterno)
        })

        it('Deve emitir evento para que página a baixo do modal busque os itens adicionados',  () => {
            wrapper = defaultMount()
            flushPromises()
            wrapper.vm.buscarItensAdicionados()

            expect(wrapper.emitted().buscarItensAdicionados).toBeTruthy()
        })

        it('Deve retornar as colunas da tabela de movimentação do tipo definitiva', async() => {
            actions = {
                [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(Itens),
                [actionTypes.MOVIMENTACAO_INTERNA.ITEM.ADICIONAR_ITENS_MOVIMENTACAO_INTERNA]: jest.fn(),
                [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DEFINITIVA')
            }

            store = new Vuex.Store({actions, state, mutations})

            wrapper = shallowMount(ModalMovimentacaoInternaListagemItens, {
                store,
                router,
                localVue
            })

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

            const colunas = await wrapper.vm.obterColunasTabela()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]).toBeCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toBe(1)
            expect(colunas).toEqual(colunasTabelaItensMovimentacao)
        })

    })
})
