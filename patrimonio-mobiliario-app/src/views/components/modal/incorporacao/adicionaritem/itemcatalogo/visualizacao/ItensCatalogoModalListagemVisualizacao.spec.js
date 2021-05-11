import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import ItensCatalogoModalListagemVisualizacao from './ItensCatalogoModalListagemVisualizacao'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {mutationTypes} from '@/core/constants'

describe('ItensCatalogoModalListagemVisualizacao', () => {
    let wrapper, localVue, state, mutations, actions, errors, vuetify, $emit = jest.fn()

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    errors = {
        collect: jest.fn()
    }

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1
                }
            },
            itemcatalogo: {
                colunasItemCatalogoModalListagem:[
                    {
                        text: 'Código',
                        value: 'codigo',
                        sortable: true,
                        align: 'left',
                        width: '20%',
                        class: 'gray--text'
                    },
                    {
                        text: 'Descrição',
                        value: 'descricao',
                        sortable: true,
                        align: 'left',
                        width: '75%',
                        class: 'gray--text'
                    },
                    {
                        sortable: false,
                        value: 'adicionado',
                        align: 'left',
                        width: '5%'
                    }
                ]
            }
        }

        mutations = {
            [mutationTypes.ITEM_CATALOGO.SET_PAGINACAO_BUSCA_TODOS_ITENS_CATALOGO]: jest.fn(),
            [mutationTypes.ITEM_CATALOGO.RESETA_PAGE_CATALOGO]: jest.fn()
        }
    })

    describe('Events', () => {
        it('Deve emitir evento de paginar quando mudar a página', async () => {
            wrapper = shallowMount(ItensCatalogoModalListagemVisualizacao, {
                localVue,
                vuetify,
                propsData: {
                    paginacao: {
                        page: 1,
                        rowsPerPage: 5
                    }
                },
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks: {errors, $emit}
            })

            wrapper.vm.paginacaoInterna.page = 2

            await flushPromises()

            expect(wrapper.emitted().paginar).toBeTruthy()
        })
    })

    describe('Methods', () => {
        it('Deve trocar a página', async () => {
            wrapper = shallowMount(ItensCatalogoModalListagemVisualizacao, {
                localVue,
                vuetify,
                propsData: {
                    paginacao: {
                        page: 1,
                        rowsPerPage: 5
                    }
                },
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks: {errors, $emit}
            })

            wrapper.vm.tratarPaginacao(5)

            expect(wrapper.vm.paginacaoInterna.page).toBe(5)
        })

        it('Deve resetar página do catálogo', async () => {
            wrapper = shallowMount(ItensCatalogoModalListagemVisualizacao, {
                localVue,
                vuetify,
                propsData: {
                    paginacao: {
                        page: 1,
                        rowsPerPage: 5
                    }
                },
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks: {errors, $emit}
            })

            wrapper.vm.resetaPage()

            expect(mutations[mutationTypes.ITEM_CATALOGO.RESETA_PAGE_CATALOGO]).toHaveBeenCalled()
        })
    })
})
