import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import Reconhecimento from './Reconhecimento'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('Reconhecimento.vue', () => {

    let wrapper, actions, store, localVue, mutations, state

    const reconhecimento = {nome: 'Compra', id: 1, situacao: 'ATIVO', execucaoOrcamentaria: 'SIM'}

    const reconhecimentos = {
        items: [
            {
                nome: 'Compra', id: 1, situacao: 'ATIVO', execucaoOrcamentaria: 'SIM', notaFiscal: false, empenho: true
            },
            {
                nome: 'Contrato', id: 2, situacao: 'INATIVO', execucaoOrcamentaria: 'NAO', notaFiscal: false, empenho: false
            },
            {
                nome: 'Doação', id: 3, situacao: 'ATIVO', execucaoOrcamentaria: 'SIM', notaFiscal: true, empenho: true
            },
            {
                nome: 'Compra', id: 4, situacao: 'INATIVO', execucaoOrcamentaria: 'NAO', notaFiscal: true, empenho: false
            }
        ],
        totalPages: 1,
        totalElements: 4
    }

    beforeEach(() => {

        state = {
            reconhecimento: {
                todosReconhecimentos: {
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
                        sortBy: ['codigo'],
                        sortDesc: [false]
                    }
                }
            }
        }

        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.CONFIGURACAO.RECONHECIMENTO.EDITAR_RECONHECIMENTO]: jest.fn().mockReturnValue(reconhecimento),
            [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_TODOS_RECONHECIMENTOS]: jest.fn().mockReturnValue(reconhecimentos),
            [actionTypes.CONFIGURACAO.RECONHECIMENTO.INSERIR_RECONHECIMENTO]: jest.fn().mockReturnValue(reconhecimentos.items)
        }

        mutations = {
            [mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_FILTROS_BUSCA_TODOS_RECONHECIMENTOS]: jest.fn(),
            [mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_PAGINACAO_BUSCA_TODOS_RECONHECIMENTOS]: jest.fn(),
            [mutationTypes.CONFIGURACAO.RECONHECIMENTO.RESETA_PAGE_RECONHECIMENTO]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
        }

        store = new Vuex.Store({actions, mutations, state})
    })

    describe('methods', () => {

        it('deve atualizar o reconhecimento', async () => {
            const mensagem =  {message: 'Atenção: alterar os dados do reconhecimento não afeta incorporações finalizadas anteriormente!', type: 'warning'}
            wrapper = shallowMount(Reconhecimento, {
                store,
                localVue
            })
            await flushPromises()
            await wrapper.vm.atualizarReconhecimento(reconhecimento)

            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.RECONHECIMENTO.EDITAR_RECONHECIMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RECONHECIMENTO.EDITAR_RECONHECIMENTO].mock.calls[0][1]).toEqual(reconhecimento)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1]).toEqual(mensagem)
        })

        it('deve buscar todos os reconhecimentos', async () => {
            wrapper = shallowMount(Reconhecimento, {
                store,
                localVue
            })
            await flushPromises()
            await wrapper.vm.buscarReconhecimentos()

            await flushPromises()
            expect(mutations[mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_FILTROS_BUSCA_TODOS_RECONHECIMENTOS]).toHaveBeenCalled()
            expect(mutations[mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_FILTROS_BUSCA_TODOS_RECONHECIMENTOS].mock.calls[0][1]).toEqual(wrapper.vm.buscarFiltrosInterno())
            expect(mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE].mock.calls[0][1]).toEqual('Carregando ...')
            expect(actions[actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_TODOS_RECONHECIMENTOS]).toHaveBeenCalled()
            expect(wrapper.vm.itens).toEqual(reconhecimentos.items)
        })

        it('deve buscar todos os filtros do reconhecimento da state e clonar na variável filtrosInterno', async () => {
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            await flushPromises()
            await wrapper.vm.buscarFiltros()

            await flushPromises()
            expect(wrapper.vm.filtrosInterno).toEqual(state.reconhecimento.todosReconhecimentos.filtros)
        })

        it('deve retornar um clone do filtrosInterno', async () => {
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            await flushPromises()
            await wrapper.vm.buscarFiltrosInterno()

            await flushPromises()
            expect(wrapper.vm.buscarFiltrosInterno()).toEqual(wrapper.vm.filtrosInterno)
        })

        it('deve cancelar a edição atual e chamar a action de buscar todos os reconhecimentos', async () => {
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            await flushPromises()
            await wrapper.vm.cancelarEdicao()

            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_TODOS_RECONHECIMENTOS]).toHaveBeenCalled()
        })

        it('deve chamar o inserir reconhecimento', async () => {
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            await flushPromises()
            await wrapper.vm.inserirNovoReconhecimento()

            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.RECONHECIMENTO.INSERIR_RECONHECIMENTO]).toHaveBeenCalled()
            expect(wrapper.vm.itens).toEqual(reconhecimentos.items)
        })

        it('deve preparar para inserir um novo reconhecimento', async() => {
            const novoReconhecimento = {execucaoOrcamentaria: null, nome: '', situacao: 'ATIVO', notaFiscal: false, empenho: false}
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            await flushPromises()
            wrapper.vm.prepararParaInserirNovoReconhecimento()

            await flushPromises()

            expect(wrapper.vm.itens[0]).toEqual(novoReconhecimento)
            expect(wrapper.vm.totalItens).toEqual(4)
            expect(wrapper.vm.paginas).toEqual(1)
        })

        it('deve preparar para inserir um novo reconhecimento com paginas 0 e total itens 0', async() => {
            reconhecimentos.totalElements = 0
            reconhecimentos.totalPages = 0
            actions = {
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.EDITAR_RECONHECIMENTO]: jest.fn(),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_TODOS_RECONHECIMENTOS]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.INSERIR_RECONHECIMENTO]: jest.fn().mockReturnValue(reconhecimentos.items)
            }
            const novoReconhecimento = {execucaoOrcamentaria: null, nome: '', situacao: 'ATIVO', notaFiscal: false, empenho: false}
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store: new Vuex.Store({actions, mutations, state})
            })
            await flushPromises()
            wrapper.vm.prepararParaInserirNovoReconhecimento()

            await flushPromises()

            expect(wrapper.vm.itens[0]).toEqual(novoReconhecimento)
            expect(wrapper.vm.totalItens).toEqual(1)
            expect(wrapper.vm.paginas).toEqual(1)
        })

        it('não deve preparar para inserir um novo reconhecimento caso já exista um novo reconhecimento em processo de inserção', async () => {
            const novoReconhecimento = [{execucaoOrcamentaria: '', nome: '', situacao: 'ATIVO'}]
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            wrapper.setData({
                itens: [{nome: '', situacao: 'ATIVO', execucaoOrcamentaria: ''}],
                paginas: 1,
                totalItens: 1
            })
            await flushPromises()
            wrapper.vm.prepararParaInserirNovoReconhecimento()

            await flushPromises()

            expect(wrapper.vm.itens).toEqual(novoReconhecimento)
            expect(wrapper.vm.totalItens).toEqual(1)
            expect(wrapper.vm.paginas).toEqual(1)
        })

        it('deve tratar o evento de busca simples', async () => {
            const valor = 'teste'
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            await flushPromises()
            await wrapper.vm.tratarEventoBuscaSimples(valor)

            await flushPromises()
            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual(valor)
            expect(wrapper.vm.buscarReconhecimentos()).toBeTruthy()
        })

        it('deve tratar o evento de paginar', async () => {
            const paginacao = {sortBy: [], defaultSortBy: 'situacao'}
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            await flushPromises()
            await wrapper.vm.tratarEventoPaginar(paginacao)

            await flushPromises()
            expect(mutations[mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_PAGINACAO_BUSCA_TODOS_RECONHECIMENTOS]).toHaveBeenCalled()
            expect(mutations[mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_PAGINACAO_BUSCA_TODOS_RECONHECIMENTOS].mock.calls[0][1]).toEqual(paginacao)
            expect(wrapper.vm.buscarReconhecimentos()).toBeTruthy()
        })

        it('deve tratar o evento de remover filtro', async () => {
            const propriedade = {conteudo: {default: null, label: 'Pesquisa', value: ''}}
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            await flushPromises()
            await wrapper.vm.tratarEventoRemoverFiltro(propriedade)

            await flushPromises()
            expect(wrapper.vm.filtrosInterno).toEqual(propriedade)
            expect(wrapper.vm.buscarReconhecimentos()).toBeTruthy()
        })

        it('deve resetar o sortBy da paginacao caso esteja vazio', async () => {
            const paginacao = {sortBy: [], defaultSortBy: 'situacao'}
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            await flushPromises()
            await wrapper.vm.resetarPaginacaoSortBy(paginacao)

            await flushPromises()
            expect(state.reconhecimento.todosReconhecimentos.paginacao.sortBy[0]).toEqual('situacao')
        })

        it('não deve resetar o sortBy da paginacao pois há filtros ativado', async () => {
            const paginacao = {sortBy: [], defaultSortBy: 'situacao'}
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            state.reconhecimento.todosReconhecimentos.paginacao.sortBy[0] = 'situacao'
            await flushPromises()
            await wrapper.vm.resetarPaginacaoSortBy(paginacao)

            await flushPromises()
            expect(state.reconhecimento.todosReconhecimentos.paginacao.sortBy[0]).toEqual('situacao')
        })


        it('deve desabilitar botao novo', async () => {
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            await flushPromises()
            await wrapper.vm.desabilitaBotaoNovo()

            await flushPromises()
            expect(wrapper.vm.botaoNovoDesabilitado).toEqual(true)
        })

        it('deve habilitar botao novo', async () => {
            wrapper = shallowMount(Reconhecimento, {
                localVue,
                store
            })
            await flushPromises()
            await wrapper.vm.habilitaBotaoNovo()

            await flushPromises()
            expect(wrapper.vm.botaoNovoDesabilitado).toEqual(false)
        })
    })
})
