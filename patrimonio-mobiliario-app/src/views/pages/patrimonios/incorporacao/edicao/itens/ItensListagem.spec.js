import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ItensListagem from './ItensListagem'

describe('ItensListagem', () => {
    let wrapper, mutations, actions, localVue, router, state

    const rotulosPersonalizados = {
        namespaced: true,
        getters: {
            getItemIncorporacaoValidado: () => jest.fn(),
        }
    }

    const incorporacaoItens = {
        items: [
            {
                id: 1,
                imagem: 'imagem',
                codigo: '0001285-1',
                descricao: 'Descrição Automóvel para testes',
                quantidade: 120,
                valorUnitario: 120285.20,
                valorTotal: 1002285.20
            },
        ],
        totalElements: 1,
        totalPages: 1,
    }

    const itemIncorporacao = {
            id: 1,
            imagem: 'imagem',
            codigo: '0001285-1',
            descricao: 'Descrição Automóvel para testes',
            quantidade: 120,
            valorUnitario: 120285.20,
            valorTotal: 1002285.20
        }

    const createDefaultStore = () => new Vuex.Store({state, mutations, actions, modules: {rotulosPersonalizados}})

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {
                    name: 'IncorporacaoItensEdicao',
                    params: {
                        incorporacaoId: itemIncorporacao.id,
                    },
                },
            },
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [{
                        hasAccess: true,
                        name: 'Mobiliario.Normal',
                        produtoId: 410
                    }]
                }
            },
            itemIncorporacao: {
                item:{
                    id_incorporacao: 1,
                    id: null,
                    codigo: null,
                    quantidade: null,
                    descricao: null,
                    numeracaoPatrimonial: null,
                    tipo: null
                },
                resultadoBuscaTodosItensIncorporacao: {
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
                        defaultSortBy: ['codigo'],
                        sortDesc: [false]
                    }
                },
            }
        }

        mutations = {
            [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_INCORPORACAO]: jest.fn(),
            [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_INCORPORACAO]: jest.fn(),
            [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_INCORPORACAO_ITEM]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.SET_ROTA_ORIGEM]: jest.fn()
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]: jest.fn().mockReturnValue(incorporacaoItens),
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DELETAR_ITEM_INCORPORACAO]: jest.fn(),
            [actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]: jest.fn()
        }
        Vue.use(Vuex)
    })

    describe('Events', () => {

        it('Deve emitir o evento de paginar quando alterar a paginação', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore(),
                stubs: {
                    ItensTabela: {
                        template: '<div><button class="stub" @click="$emit(\'paginar\', {sortBy: [\'codigo\']})"></button></div>'
                    }
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_INCORPORACAO].mock.calls[0][1]).toEqual({'sortBy': ['codigo']})
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]).toHaveBeenCalled()
        })

        it('Deve emitir o evento de buscar simples', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore(),
                stubs: {
                    'az-search': {
                        template: '<div><button class="stub" @click="$emit(\'simple-search\', \'teste busca\')"></button></div>'
                    }
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_INCORPORACAO].mock.calls[0][1].conteudo.value).toEqual('teste busca')
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]).toHaveBeenCalled()
            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('teste busca')
        })

        it('Deve emitir o evento de remover os filtros da pesquisa', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore(),
                stubs: {
                    'az-search': {
                        template: '<div><button class="stub" @click="$emit(\'remove-filter\', \'conteudo\')"></button></div>'
                    }
                }
            })
            await flushPromises()
            wrapper.vm.filtrosInterno.conteudo.value = 'Teste remover filtro'
            wrapper.find('button[class="stub"]').trigger('click')

            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual(null)
        })

        it('Deve emitir o evento de acessar edição do item quando campos obrigatórios não preenchidos', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                getters: {
                    getItemIncorporacaoValidado: () => jest.fn().mockReturnValue(false),
                }
            }

            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: new Vuex.Store({state, mutations, actions,modules: {rotulosPersonalizados}}),
                stubs: {
                    ItensTabela: {
                        template: '<div><button class="stub" @click="$emit(\'acessar\', incorporacaoItem = {\n' +
                            '        id:1,\n' +
                            '        imagem: \'imagem\',\n' +
                            '        codigo: \'0001285-1\',\n' +
                            '        descricao: \'Descrição Automóvel para testes\',\n' +
                            '        quantidade: 120,\n' +
                            '        valorUnitario: 120285.20,\n' +
                            '        valorTotal: 1002285.20,\n' +
                            '\n' +
                            '    })"></button></div>'
                    }
                }
            })
            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            expect(router.push.mock.calls[0][0]).toEqual({name: 'DadosGeraisDoItemModal', params: { incorporacaoId: 1, itemIncorporacaoId: 1 }})
        })

        it('Deve emitir o evento de acessar edição dos patrimonios e chamar o cadastro de patrimonios quando campos obrigatórios preenchidos', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                getters: {
                    getItemIncorporacaoValidado: () => jest.fn().mockReturnValue(true),
                }
            }

            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: new Vuex.Store({state, mutations, actions,modules: {rotulosPersonalizados}}),
                stubs: {
                    ItensTabela: {
                        template: '<div><button class="stub" @click="$emit(\'acessar\', incorporacaoItem = {\n' +
                            '        id:1,\n' +
                            '        uriImagem: \'imagem\',\n' +
                            '        quantidade: 120,\n' +
                            '        valorTotal: 1002285.20,\n' +
                            '        numeracaoPatrimonial: \'AUTOMATICA\',\n' +
                            '        naturezaDespesa: 1,\n' +
                            '        contaContabil: 1,\n' +
                            '        estadoConservacao: 1,\n' +
                            '        numeracaoPatrimonial: \'VEICULO\',\n' +
                            '\n' +
                            '    })"></button></div>'
                    }
                }
            })
            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'IncorporacaoItemListagemPatrimonios', params: { incorporacaoId: 1, itemIncorporacaoId: 1 }})
        })

        it('Deve emitir o evento de deletar o item de incorporação', async () =>{
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore(),
                stubs: {
                    ItensTabela: {
                        template: '<div><button class="stub" @click="$emit(\'deletar\', id = 1)"></button></div>'
                    }
                }
            })
            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DELETAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
        })

        it('Deve emitir evento de habilitar passo 3 quando total de elementos maior que 0', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            await flushPromises()

            expect(expect(wrapper.emitted().habilitaPasso3).toBeTruthy())
        })

        it('Deve emitir evento de desabilitaPasso3quando total de elementos menor ou igual que 0', async () => {
            const incorporacaoItens = {items: [], totalElements: 0, totalPages: 1,}
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]: jest.fn().mockReturnValue(incorporacaoItens),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DELETAR_ITEM_INCORPORACAO]: jest.fn(),
                [actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]: jest.fn()
            }
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store:new Vuex.Store({state, mutations, actions,modules: {rotulosPersonalizados}}),
            })
            await flushPromises()

            expect(expect(wrapper.emitted().desabilitaPasso3).toBeTruthy())
        })

    })

    describe('Methods', () => {

        it('Deve emitir o método de buscar todos os itens de incorporações', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            await flushPromises()
            wrapper.vm.buscar()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]).toHaveBeenCalled()
        })

        it('Deve emitir o método de abrir o modal para inserir um novo item de incorporação', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            wrapper.vm.adicionarItem()
            await flushPromises()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'ItensCatalogoModalNovo', params: { incorporacaoId: 1 }})
        })

        it('Deve emitir o método de voltar para o passo 1', () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            wrapper.vm.voltarPasso1()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'EditarIncorporacao', params: { incorporacaoId: 1 }})
        })

        it('Deve redirecionar para documentos', () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            wrapper.vm.tratarEventoContinuar()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'IncorporacaoDocumentosEdicao', params: { incorporacaoId: 1 }})
        })

        it('existeItens deve ser true quando houver itens', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            await flushPromises()

            wrapper.vm.totalItens = 1
            wrapper.vm.filtrosInterno.conteudo.value= ''

            expect(wrapper.vm.existeItens).toEqual(true)
        })

        it('existeItens deve ser true quando houver itens e conteudo', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            await flushPromises()

            wrapper.vm.totalItens = 1
            wrapper.vm.filtrosInterno.conteudo.value= 'abc'

            expect(wrapper.vm.existeItens).toEqual(true)
        })

        it('existeItens deve ser true quando não houver itens mas houver conteúdo na pesquisa', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            await flushPromises()

            wrapper.vm.totalItens = 0
            wrapper.vm.filtrosInterno.conteudo.value= 'abc'

            expect(wrapper.vm.existeItens).toEqual(true)
        })

        it('existeItens deve ser false quando não houver itens e não houver conteúdo na pesquisa', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            await flushPromises()

            wrapper.vm.totalItens = 0
            wrapper.vm.filtrosInterno.conteudo.value= ''

            expect(wrapper.vm.existeItens).toEqual(false)
        })

        it('Deve mudar paginação', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            await flushPromises()
            const paginacao= {sortBy:[],defaultSortBy:'codigo'}
            wrapper.vm.resetarPaginacaoSortBy(paginacao)

            expect(wrapper.vm.todosItensIncorporacao.paginacao.sortBy[0]).toEqual(paginacao.defaultSortBy)
        })

        it('Deve voltar para passo 1 na visualização', async () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            await flushPromises()
            wrapper.vm.voltarPasso1()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[1][0]).toEqual({name: 'VisualizarIncorporacao', params: {incorporacaoId: 1}})

        })

        it('Deve redirecionar para ItensCatalogoModalVisualizacao', async () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            await flushPromises()
            wrapper.vm.adicionarItem()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[1][0]).toEqual({name: 'ItensCatalogoModalVisualizacao', params: {incorporacaoId: 1}})
        })

        it('Deve redirecionar para IncorporacaoItemListagemPatrimoniosVisualizacao', async () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            const rotulosPersonalizados = {
                namespaced: true,
                getters: {
                    getItemIncorporacaoValidado: () => jest.fn().mockReturnValue(true),
                }
            }
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: new Vuex.Store({state, mutations, actions,modules: {rotulosPersonalizados}}),
            })
            await flushPromises()
            wrapper.vm.tratarEventoAcessar({tipoBem:'VEICULO'})

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[1][0]).toEqual({name: 'IncorporacaoItemListagemPatrimoniosVisualizacao', params: {incorporacaoId: 1}})
        })

        it('Deve redirecionar para VisualizacaoDadosGerais', async () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            const rotulosPersonalizados = {
                namespaced: true,
                getters: {
                    getItemIncorporacaoValidado: () => jest.fn().mockReturnValue(false),
                }
            }
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: new Vuex.Store({state, mutations, actions,modules: {rotulosPersonalizados}}),
            })
            await flushPromises()
            wrapper.vm.tratarEventoAcessar({tipoBem:'VEICULO'})

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[1][0]).toEqual({name: 'VisualizacaoDadosGerais', params: {incorporacaoId: 1}})
        })

        it('Deve redirecionar para VisualizarIncorporacaoDocumentos', async () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            await flushPromises()
            wrapper.vm.tratarEventoContinuar()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[1][0]).toEqual({name: 'VisualizarIncorporacaoDocumentos', params: {incorporacaoId: 1}})
        })

        it('Deve redirecionar para IncorporacaoDocumentosEdicao', async () => {
            wrapper = shallowMount(ItensListagem, {
                localVue,
                router,
                store: createDefaultStore()
            })
            await flushPromises()
            wrapper.vm.tratarEventoContinuar()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'IncorporacaoDocumentosEdicao', params: {incorporacaoId: 1}})
        })
    })
})
