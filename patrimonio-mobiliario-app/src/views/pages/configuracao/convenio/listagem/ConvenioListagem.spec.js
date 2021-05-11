import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ConvenioListagem from './ConvenioListagem'

describe('ConvenioListagem', () => {
    let wrapper, mutations, actions, localVue, router, state, store

    const convenios = {
        itens: [
            {
                id: 1,
                numero: '095848',
                nome: 'Caixa Seguradora de Bens Social',
                concedente: {
                    id: 5,
                    nome: 'Caixa Economica Federal'
                },
                fonteRecurso: {
                    id: 4,
                    nome: 'Aquisição de Material Permanente'
                },
                situacao: 'ATIVO'
            },
        ],
        totalElements: 1,
        totalPages: 1,
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {current: {}}
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                }
            },
            convenio: {
                resultadoBuscaTodosConvenios: {
                    paginacao: {
                        page: 0, rowsPerPage: 10, sortDesc: [false], sortBy: ['situacao'], defaultSortBy: ['situacao']
                    },
                    filtros: {
                        conteudo: {
                            value: null,
                            default: null,
                            label: 'Pesquisa'
                        },
                    }
                }
            }
        }

        mutations = {
            [mutationTypes.CONFIGURACAO.CONVENIO.SET_FILTROS_BUSCA_TODOS_CONVENIOS]: jest.fn(),
            [mutationTypes.CONFIGURACAO.CONVENIO.SET_PAGINACAO_BUSCA_TODOS_CONVENIOS]: jest.fn(),
            [mutationTypes.CONFIGURACAO.CONVENIO.RESETA_PAGE_CONVENIO]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.SET_ROTA_ORIGEM]: jest.fn()
        }

        actions = {
            [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_TODOS_CONVENIOS]: jest.fn().mockReturnValue(convenios)
        }
        Vue.use(Vuex)
        store = new Vuex.Store({state, mutations, actions})
    })

    describe('Events', () => {
        it('Deve emitir o evento de paginar quando alterar a paginação', async () => {
            wrapper = shallowMount(ConvenioListagem, {
                localVue,
                router,
                store,
                stubs: {
                    ConvenioListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'paginar\', {})"></button></div>'
                    }
                }
            })

            wrapper.vm.verificarOrdenacao = jest.fn()

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.CONFIGURACAO.CONVENIO.SET_PAGINACAO_BUSCA_TODOS_CONVENIOS].mock.calls[0][1]).toEqual({})
            expect(wrapper.vm.verificarOrdenacao).toHaveBeenCalledWith({})
            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_TODOS_CONVENIOS]).toHaveBeenCalled()
        })

        it('Deve emitir o evento de buscar simples', async () => {
            wrapper = shallowMount(ConvenioListagem, {
                localVue,
                router,
                store,
                stubs: {
                    'az-search': {
                        template: '<div><button class="stub" @click="$emit(\'simple-search\', \'Caixa Seguradora de Bens Social\')"></button></div>'
                    }
                }
            })

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.CONFIGURACAO.CONVENIO.SET_FILTROS_BUSCA_TODOS_CONVENIOS].mock.calls[0][1].conteudo.value)
                .toEqual('Caixa Seguradora de Bens Social')
            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_TODOS_CONVENIOS]).toHaveBeenCalled()
            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('Caixa Seguradora de Bens Social')
        })

        it('Deve emitir o evento de remover os filtros da pesquisa', async () => {
            wrapper = shallowMount(ConvenioListagem, {
                localVue,
                router,
                store,
                stubs: {
                    'az-search': {
                        template: '<div><button class="stub" @click="$emit(\'remove-filter\', \'conteudo\')"></button></div>'
                    }
                }
            })
            wrapper.vm.filtrosInterno.conteudo.value = 'Caixa Seguradora de Bens Social'
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual(null)
        })

        it('Deve emitir o evento de acessar o convenio', async () => {
            wrapper = shallowMount(ConvenioListagem, {
                localVue,
                router,
                store,
                stubs: {
                    ConvenioListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'acessar\', convenio = {\n' +
                                '        id:0,\n' +
                            '        numero:\'123231\',\n' +
                            '        nome: \'Convenio 1\',\n' +
                            '        situacao: \'ATIVO\',\n' +
                            '\n' +
                            '    })"></button></div>'
                    }
                }
            })
            wrapper.find('button[class="stub"]').trigger('click')

            await flushPromises()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'ConvenioEdicao', params: {id: 0}})
        })

        it('Deve emitir o evento de inserir um novo cadastro de convênio', async () => {
            wrapper = shallowMount(ConvenioListagem, {
                localVue,
                router,
                store,
                stubs: {
                    'botao-novo': {
                        template: '<div><button class="stub" @click="$emit(\'click\')"></button></div>'
                    }
                }
            })
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'ConvenioCadastro'})
        })
    })

    describe('Methods', () => {

        it('Deve emitir o método de buscar todos convênios', async () => {
            wrapper = shallowMount(ConvenioListagem, {
                localVue,
                router,
                store,

            })
            await flushPromises()
            wrapper.vm.buscar()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_TODOS_CONVENIOS]).toHaveBeenCalled()
        })

        it('Deve verificar ordenação', async () => {
            wrapper = shallowMount(ConvenioListagem, {
                localVue,
                router,
                store,

            })
            await flushPromises()
            const paginacao ={defaultSortBy: 'abc'}
            wrapper.vm.todosConvenios.paginacao.sortBy[0] = null
            wrapper.vm.verificarOrdenacao(paginacao)
            await flushPromises()

            expect(wrapper.vm.todosConvenios.paginacao.sortBy[0]).toEqual('abc')
        })
    })
})
