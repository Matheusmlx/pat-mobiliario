import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ConvenioListagemTabela from './ConvenioListagemTabela'
import {mutationTypes} from '@/core/constants'


describe('ConvenioListagemTabela', () => {
    let wrapper, state, store, localVue, mutations

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
        }
    }

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO'
                }
            }
        }

        mutations = {
            [mutationTypes.CONFIGURACAO.CONVENIO.RESETA_PAGE_CONVENIO]: jest.fn()
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, modules: rotulosPersonalizados, mutations})

        localVue = applicationTestBuilder.build()
    })

    describe('methods', () => {
        it('Deve emitir o evento de acessar passando o convênio ', async () => {
            wrapper = shallowMount(ConvenioListagemTabela, {
                localVue,
                store,
                propsData: {
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
            wrapper = shallowMount(ConvenioListagemTabela, {
                localVue,
                store,
                propsData: {
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
            wrapper = shallowMount(ConvenioListagemTabela, {
                localVue,
                store,
                propsData: {
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

        it('Deve resetar pagina', async () => {
            wrapper = shallowMount(ConvenioListagemTabela, {
                localVue,
                store,
                propsData: {
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
            expect(mutations[mutationTypes.CONFIGURACAO.CONVENIO.RESETA_PAGE_CONVENIO]).toHaveBeenCalled()
        })
    })
})
