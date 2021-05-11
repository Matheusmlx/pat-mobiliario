import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ItensTabela from './ItensTabela'
import {mutationTypes} from '@/core/constants'


describe('ItensTabela', () => {
    let wrapper, state, store, localVue, mutations

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO'
                }
            }
        }

        Vue.use(Vuex)

        mutations = {
            [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_INCORPORACAO_ITEM]: jest.fn()
        }

        store = new Vuex.Store({state, mutations})

        localVue = applicationTestBuilder.build()
    })

    describe('methods', () => {

        it('Deve emitir o evento de deletar o item selecionado', async () =>{
            wrapper = shallowMount(ItensTabela,{
                localVue,
                store,
                propsData: {
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
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                },
                sync: false,
            })
            await flushPromises()
            await wrapper.vm.tratarEventoDeletarItem(1)
            await flushPromises()
            expect(wrapper.emitted('deletar')).toBeTruthy()
            expect(wrapper.emitted().deletar[0][0]).toEqual(1)
        })

        it('Deve emitir o evento de acessar passando o registro do item da incorporação ', async () => {
            wrapper = shallowMount(ItensTabela, {
                localVue,
                store,
                propsData: {
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
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                },
                sync: false,
            })

            await flushPromises()
            await wrapper.vm.tratarEventoAcessar()
            await flushPromises()
            expect(wrapper.emitted('acessar')).toBeTruthy()
            expect(wrapper.emitted().acessar[0][0]).toEqual(wrapper.vm.$props.item)
        })

        it('Deve emitir o evento de paginar', async () => {
            wrapper = shallowMount(ItensTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
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
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                },
                sync: false,
            })

            await flushPromises()
            wrapper.vm.paginacaoInterna = {
                page: 2,
                rowsPerPage: 10,
                descending: false,
            }
            await flushPromises()
            expect(wrapper.emitted('paginar')).toBeTruthy()
        })

        it('Deve mudar paginação', async () => {
            wrapper = shallowMount(ItensTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
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
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                },
                sync: false,
            })

            await flushPromises()
            const pagina = 3
            wrapper.vm.tratarPaginacao(pagina)
            await flushPromises()
            expect(wrapper.vm.paginacaoInterna.page).toEqual(3)
        })

        it('Deve resetar paginação', async () => {
            wrapper = shallowMount(ItensTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
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
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                },
                sync: false,
            })

            await flushPromises()
            wrapper.vm.resetaPage()
            await flushPromises()
            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_INCORPORACAO_ITEM]).toHaveBeenCalled()
        })
    })
})
