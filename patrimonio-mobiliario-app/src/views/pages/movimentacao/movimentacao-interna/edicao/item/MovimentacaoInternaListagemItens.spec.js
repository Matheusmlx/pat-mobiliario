import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import MovimentacaoInternaListagemItens from './MovimentacaoInternaListagemItens'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('MovimentacaoInternaListagemItens', () => {

    let wrapper, store, localVue, router, actions, state, mutations

    beforeEach(() => {

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Movimentacoes', hasAccess: true}]
                }
            },
            movimentacaoInternaItem:{
                resultadoBuscaTodosItensAdicionadosMovimentacaoInterna: {
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        sortDesc: []
                    }
                },
                colunasTabelaItensAdicionadosMovimentacaoDistribuicao:[],
                colunasTabelaItensAdicionadosMovimentacao: [
                    {
                        value: 'patrimonioNumero',
                        sortable: false,
                        align: 'left',
                        width: '13%',
                        class: 'gray--text'
                    },
                    {
                        value: 'patrimonioDescricao',
                        sortable: false,
                        align: 'left',
                        width: '70%',
                        class: 'gray--text'
                    },
                    {
                        text: '',
                        value: 'acoes',
                        sortable: false,
                        align: 'right',
                        width: '20%',
                        class: 'gray--text'
                    },
                ]
            }
        }

        router = {
            routes: [],
            push: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'MovimentacaoInternaEdicaoItens',
                    params: { movimentacaoInternaId: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.MOVIMENTACAO_INTERNA.ITEM.DELETAR_ITEM_MOVIMENTACAO_INTERNA]: jest.fn(),
            [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DISTRIBUICAO')
        }

        mutations = {
            [mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn(),
            [mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn()
        }

        store = new Vuex.Store({actions, state, mutations})
    })

    const defaultMount = () => shallowMount(MovimentacaoInternaListagemItens, {
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
            const tratarEventoPaginar = jest.spyOn(wrapper.vm, 'tratarEventoPaginar')
            await flushPromises()

            expect(tratarEventoPaginar).toHaveBeenCalledWith(wrapper.vm.paginacaoInterna)
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA].mock.calls[0][1].page).toEqual(2)
        })
    })

    describe('methods', () => {
        it('deve chamar adicionar Itens', () => {
            wrapper = defaultMount()
            wrapper.vm.adicionarItem()

            expect(wrapper.emitted().adicionarItem).toBeTruthy()
        })

        it('existe itens deve ser true se total de itens maior que 0', () => {
            wrapper = defaultMount()
            wrapper.setData({
                totalItens: 1
            })

            expect(wrapper.vm.existeItens).toEqual(true)
        })

        it('existe itens deve ser false se total de itens igual a 0', () => {
            wrapper = defaultMount()
            wrapper.setData({
                totalItens: 0
            })

            expect(wrapper.vm.existeItens).toEqual(false)
        })

        it('deve emitir evento para buscar Itens', () => {
            wrapper = defaultMount()
            wrapper.vm.buscar()

            expect(wrapper.emitted().buscarTodosItens).toBeTruthy()
        })

        it('deve deletar item', async() => {
            wrapper = defaultMount()
            const data = {movimentacaoInternaId: router.history.current.params.movimentacaoInternaId, itemId: 2}
            wrapper.vm.tratarEventoDeletarItem(data.itemId)
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.DELETAR_ITEM_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.DELETAR_ITEM_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(data)
            expect(wrapper.emitted().buscarTodosItens).toBeTruthy()
        })

        it('deve paginar', async () => {
            wrapper = defaultMount()
            const paginacao = {
                page: 2,
                rowsPerPage: 10,
                sortDesc: []
            }
            wrapper.vm.tratarEventoPaginar(paginacao)
            await flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(paginacao)
        })

        it('Deve resetar a paginação',() => {
            wrapper = defaultMount()

            wrapper.vm.resetaPage()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
        })

        it('Deve retornar as colunas da tabela de movimentação do tipo distribuição', async() => {
            actions = {
                [actionTypes.MOVIMENTACAO_INTERNA.ITEM.DELETAR_ITEM_MOVIMENTACAO_INTERNA]: jest.fn(),
                [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DISTRIBUICAO')
            }

            store = new Vuex.Store({actions, state, mutations})

            wrapper = shallowMount(MovimentacaoInternaListagemItens, {
                store,
                router,
                localVue
            })

            const colunasTabelaItensAdicionadosMovimentacaoDistribuicao = [
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
                    width: '55%',
                    class: 'gray--text'
                },
                {
                    text: null,
                    value: 'incorporacaoCodigo',
                    sortable: false,
                    align: 'left',
                    width: '20%',
                    class: 'gray--text'
                },
                {
                    text: null,
                    value: 'acoes',
                    sortable: false,
                    align: 'right',
                    width: '10%',
                    class: 'gray--text'
                }
            ]

            const colunasTabela = await wrapper.vm.obterColunasTabela()

            expect(colunasTabela).toEqual(colunasTabelaItensAdicionadosMovimentacaoDistribuicao)
        })

        it('Deve retornar as colunas da tabela de movimentação do tipo definitiva', async() => {
            actions = {
                [actionTypes.MOVIMENTACAO_INTERNA.ITEM.DELETAR_ITEM_MOVIMENTACAO_INTERNA]: jest.fn(),
                [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DEFINITIVA')
            }

            store = new Vuex.Store({actions, state, mutations})

            wrapper = shallowMount(MovimentacaoInternaListagemItens, {
                store,
                router,
                localVue
            })

            const colunasTabelaItensAdicionadosMovimentacao = [
                {
                    text: null,
                    value: 'patrimonioNumero',
                    sortable: false,
                    align: 'left',
                    width: '13%',
                    class: 'gray--text'
                },
                {
                    text: null,
                    value: 'patrimonioDescricao',
                    sortable: false,
                    align: 'left',
                    width: '70%',
                    class: 'gray--text'
                },
                {
                    text: null,
                    value: 'acoes',
                    sortable: false,
                    align: 'right',
                    width: '20%',
                    class: 'gray--text'
                }
            ]

            const colunasTabela = await wrapper.vm.obterColunasTabela()

            expect(colunasTabela).toEqual(colunasTabelaItensAdicionadosMovimentacao)
        })
    })
})
