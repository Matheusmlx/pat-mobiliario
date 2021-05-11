import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import PatrimoniosListagem from './PatrimoniosListagem'

describe('PatrimoniosListagem', () => {
    let wrapper, mutations, actions, localVue, router, state, store

    const patrimonios = {
        itens: [
            {
                id: 1,
                descricao: '9999 - teste',
                orgao: '3333 - orgao',
                setor: 'CCP - Copa e Cozinha',
                ultimaMovimentacao: '2020-05-25T00:00:00',
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
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                }
            },
            patrimonio: {
                resultadoBuscaTodosPatrimoniosListagem: {
                    paginacao: {
                        page: 0, rowsPerPage: 10, sortDesc: [false], sortBy: ['orgao'], defaultSortBy: ['orgao']
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
            [mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_LISTAGEM]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_LISTAGEM]: jest.fn(),
            [mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_LISTAGEM]: jest.fn(),
        }

        actions = {
            [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_LISTAGEM]: jest.fn().mockReturnValue(patrimonios)
        }
        Vue.use(Vuex)
        store = new Vuex.Store({state, mutations, actions})
    })

    describe('Events', () => {
        it('Deve emitir o evento de paginar quando alterar a paginação', async () => {
            wrapper = shallowMount(PatrimoniosListagem, {
                localVue,
                router,
                store,
                stubs: {
                    PatrimoniosListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'paginar\', {})"></button></div>'
                    }
                }
            })

            wrapper.vm.verificarOrdenacao = jest.fn()

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_LISTAGEM].mock.calls[0][1]).toEqual({})
            expect(wrapper.vm.verificarOrdenacao).toHaveBeenCalledWith({})
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_LISTAGEM]).toHaveBeenCalled()
        })

        it('Deve emitir o evento de buscar simples', async () => {
            wrapper = shallowMount(PatrimoniosListagem, {
                localVue,
                router,
                store,
                stubs: {
                    'az-search': {
                        template: '<div><button class="stub" @click="$emit(\'simple-search\', \'0001 - teste\')"></button></div>'
                    }
                }
            })

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_LISTAGEM].mock.calls[0][1].conteudo.value)
                .toEqual('0001 - teste')
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_LISTAGEM]).toHaveBeenCalled()
            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('0001 - teste')
        })

        it('Deve emitir o evento de remover os filtros da pesquisa', async () => {
            wrapper = shallowMount(PatrimoniosListagem, {
                localVue,
                router,
                store,
                stubs: {
                    'az-search': {
                        template: '<div><button class="stub" @click="$emit(\'remove-filter\', \'conteudo\')"></button></div>'
                    }
                }
            })
            wrapper.vm.filtrosInterno.conteudo.value = '0001 - teste'
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual(null)
        })

        it('Deve emitir o evento de acessar o patrimônio', async () => {
            wrapper = shallowMount(PatrimoniosListagem, {
                localVue,
                router,
                store,
                stubs: {
                    PatrimoniosListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'acessar\', patrimonio = {\n' +
                                '        id:1,\n' +
                            '        descricao:\'0001 - teste\',\n' +
                            '        orgao: \'3333 - orgao\',\n' +
                            '        situacao: \'ATIVO\',\n' +
                            '\n' +
                            '    })"></button></div>'
                    }
                }
            })
            wrapper.find('button[class="stub"]').trigger('click')

            await flushPromises()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'FichaPatrimonioDadosGerais', params: {patrimonioId: 1}})
        })

    })

    describe('Methods', () => {

        it('Deve emitir o método de buscar todos patrimônios', async () => {
            wrapper = shallowMount(PatrimoniosListagem, {
                localVue,
                router,
                store,

            })
            await flushPromises()
            wrapper.vm.buscar()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_LISTAGEM]).toHaveBeenCalled()
        })

        it('Deve verificar ordenação', async () => {
            wrapper = shallowMount(PatrimoniosListagem, {
                localVue,
                router,
                store,

            })
            await flushPromises()
            const paginacao ={defaultSortBy: 'abc'}
            wrapper.vm.todosPatrimonios.paginacao.sortBy[0] = null
            wrapper.vm.verificarOrdenacao(paginacao)
            await flushPromises()

            expect(wrapper.vm.todosPatrimonios.paginacao.sortBy[0]).toEqual('abc')
        })
    })
})
