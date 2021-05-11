import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import ItensModal from './ItensCatalogoModalEdicao'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('ItensModal', () => {
    let wrapper, localVue, state,router, mutations, actions, vuetify, $emit = jest.fn()

    const itensData = {
        'items':
            [
                {
                    id: 1,
                    codigo: '0001',
                    descricao: 'Items 1'
                },
                {
                    id: 2,
                    codigo: '00214',
                    descricao: 'Armario de escritório'
                },
                {
                    id: 3,
                    codigo: '005487',
                    descricao: 'Carro popular'
                }
            ],
        'paginas': 3,
        'totalItens': 5
    }

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: ['Mobiliario.Normal']
                }
            },
            itemcatalogo: {
                todosItens: {
                    filtros: {
                        conteudo: {
                            value: null,
                            default: null,
                            label: 'Pesquisa'
                        }
                    },
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                        multiSort:false,
                        mustSort:false,
                        sortDesc: [false],
                        sortBy: ['codigo'],
                        defaultSortBy: ['codigo']
                    }
                }

            },
            itemIncorporacao: {
                rotaModal:{
                    origem: 'ItensIncorporacaoListagem'
                }
            }
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'ItensCatalogoModalEdicao',
                    params: {
                        incorporacaoId: 1,
                        itemIncorporacaoId: 1
                    },
                },
            },
        }

        mutations = {
            [mutationTypes.ITEM_CATALOGO.SET_PAGINACAO_BUSCA_TODOS_ITENS_CATALOGO]: jest.fn(),
            [mutationTypes.ITEM_CATALOGO.SET_FILTROS_BUSCA_TODOS_ITENS_CATALOGO]: jest.fn(),
            [mutationTypes.ITEM_CATALOGO.RESETA_PAGE_CATALOGO]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()

        }
        actions = {
            [actionTypes.ITEM_CATALOGO.BUSCAR_TODOS_ITENS_CATALOGO]: jest.fn().mockReturnValue(itensData)
        }
    })

    const defaultMount = () => shallowMount(ItensModal, {
        localVue,
        vuetify,
        router,
        propsData: {
            modalItens: []
        },
        store: new Vuex.Store({
            state, mutations, actions
        }),
        mocks: {$emit}
    })

    describe('Events', () => {

        it('Deve emitir o evento de remover os filtros da pesquisa', async () => {
            wrapper = shallowMount(ItensModal, {
                localVue,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                stubs: {
                    'az-search': {
                        template: '<div><button class="stub" @click="$emit(\'remove-filter\', \'conteudo\')"></button></div>'
                    }
                }
            })
            wrapper.vm.filtrosInterno.conteudo.value = 'Teste'
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual(null)
        })

        it('Deve emitir o evento para verificar se pode continuar', async () => {
            wrapper = shallowMount(ItensModal, {
                localVue,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                })
            })

            await flushPromises()
            wrapper.vm.tratarEventoCamposAceitos()

            expect(wrapper.emitted().verificarContinuar).toBeTruthy()
        })


    })

    describe('Methods', () => {

        it('Deve fazer a busca com o conteudo passsado', async () => {
            wrapper = defaultMount()

            wrapper.setData({
                filtrosInterno: {
                    conteudo: {
                        value: null,
                        default: null,
                        label: 'Pesquisa'
                    }
                }
            })

            wrapper.vm.tratarEventoBuscaSimples('Teste')

            expect(wrapper.vm.filtrosInterno.conteudo.value).toBe('Teste')
            expect(wrapper.vm.buscar()).toBeTruthy()
        })

        it('Deve realizar a paginação do conteúdo', async () => {
            wrapper = defaultMount()

            wrapper.setData({
                paginacaoInterna: {
                    page: 1,
                    rowsPerPage: 5
                }
            })

            const paginacao = {
                page: 1,
                rowsPerPage: 5,
                sortDesc: [false],
                sortBy: ['codigo']
            }

            wrapper.vm.tratarEventoPaginar(paginacao)

            expect(mutations[mutationTypes.ITEM_CATALOGO.SET_PAGINACAO_BUSCA_TODOS_ITENS_CATALOGO]).toHaveBeenCalled()
            expect(wrapper.vm.buscar()).toBeTruthy()
        })

        it('Deve remover o filtro', async () => {
            wrapper = defaultMount()

            wrapper.setData({
                filtrosInterno: {
                    conteudo: {
                        value: 'Teste',
                        default: null,
                        label: 'Pesquisa'
                    }
                }
            })

            wrapper.vm.tratarEventoRemoverFiltro('conteudo')

            expect(wrapper.vm.filtrosInterno.conteudo.value).toBe(null)
            expect(wrapper.vm.buscar()).toBeTruthy()
        })

        it('Deve retornar um clone dos filtros', async() => {
            wrapper = defaultMount()

            const resultado = wrapper.vm.getFiltros()

            expect(resultado).toEqual(state.itemcatalogo.todosItens.filtros)
        })

        it('Deve retornar um clone dos filtros internos', async() => {
            wrapper = defaultMount()

            wrapper.vm.filtrosInterno = wrapper.vm.getFiltros()
            const resultado = wrapper.vm.getFiltrosInterno()

            expect(resultado).toEqual(wrapper.vm.filtrosInterno)
        })

        it('Deve resetar ordenação', async () => {
            wrapper = shallowMount(ItensModal, {
                localVue,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                })
            })

            await flushPromises()
            const paginacao= {sortBy:[],defaultSortBy:'codigo'}
            wrapper.vm.resetarPaginacaoSortBy(paginacao)

            expect(wrapper.vm.todosItens.paginacao.sortBy[0]).toEqual(paginacao.defaultSortBy)
        })

    })
})
