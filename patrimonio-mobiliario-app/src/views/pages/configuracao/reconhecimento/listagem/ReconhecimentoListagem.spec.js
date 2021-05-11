import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {shallowMount} from '@vue/test-utils'
import ReconhecimentoListagem from './ReconhecimentoListagem'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import {mutationTypes} from '@/core/constants'

describe('ReconhecimentoListagem.vue', () => {

    let wrapper, localVue, store, mutations

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getObrigatorioRotulosPersonalizados: () => jest.fn()
        }
    }

    beforeEach(() => {

        localVue = applicationTestBuilder.build()

        mutations= {
            [mutationTypes.CONFIGURACAO.RECONHECIMENTO.RESETA_PAGE_RECONHECIMENTO]: jest.fn()
        }

        store = new Vuex.Store({mutations,
            modules: {
                rotulosPersonalizados
            }
        })


    })

    describe('watch', () => {

        it('Deve emitir o evento de paginar', async () => {
            wrapper = shallowMount(ReconhecimentoListagem, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    paginas: 1,
                    totalItens: 1,
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['situacao'],
                        sortDesc: [false]
                    }
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
    })

    describe('methods', () => {

        it('deve emitir o evento cancelarEdicao para o componente pai', () => {
            wrapper = shallowMount(ReconhecimentoListagem, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    paginas: 1,
                    totalItens: 1,
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['situacao'],
                        sortDesc: [false]
                    }
                }
            })
            flushPromises()
            wrapper.vm.cancelarEdicao()

            flushPromises()
            expect(wrapper.emitted('cancelarEdicao')).toBeTruthy
        })

        it('deve emitir o evento inserirNovoReconhecimento para o componente pai', () => {
            wrapper = shallowMount(ReconhecimentoListagem, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    paginas: 1,
                    totalItens: 1,
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['situacao'],
                        sortDesc: [false]
                    }
                }
            })
            flushPromises()
            wrapper.vm.inserirNovoReconhecimento()

            flushPromises()
            expect(wrapper.emitted('inserirNovoReconhecimento')).toBeTruthy
        })

        it('deve emitir o evento atualizarReconhecimento para o componente pai', () => {
            wrapper = shallowMount(ReconhecimentoListagem, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    paginas: 1,
                    totalItens: 1,
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['situacao'],
                        sortDesc: [false]
                    }
                }
            })
            flushPromises()
            wrapper.vm.atualizarReconhecimento()

            flushPromises()
            expect(wrapper.emitted('atualizarReconhecimento')).toBeTruthy
        })

        it('deve tratar o evento de paginação', () => {
            const pagina = 10
            wrapper = shallowMount(ReconhecimentoListagem, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    paginas: 1,
                    totalItens: 1,
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['situacao'],
                        sortDesc: [false]
                    }
                }
            })
            flushPromises()
            wrapper.vm.tratarPaginacao(pagina)

            flushPromises()
            expect(wrapper.vm.paginacaoInterna.page).toEqual(pagina)
        })

        it('deve desativar as ações dos ícones de editar', () => {
            wrapper = shallowMount(ReconhecimentoListagem, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    paginas: 1,
                    totalItens: 1,
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['situacao'],
                        sortDesc: [false]
                    }
                }
            })
            wrapper.vm.desativarAcoes()

            expect(wrapper.vm.permitirAcoes).toEqual(false)
        })
        it('deve habilitar as ações dos ícones de editar', () => {
            wrapper = shallowMount(ReconhecimentoListagem, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    paginas: 1,
                    totalItens: 1,
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['situacao'],
                        sortDesc: [false]
                    }
                }
            })
            wrapper.vm.habilitarAcoes()

            expect(wrapper.vm.permitirAcoes).toEqual(true)
        })


        it('deve desabilitar botao novo', async () => {
            wrapper = shallowMount(ReconhecimentoListagem, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    paginas: 1,
                    totalItens: 1,
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['situacao'],
                        sortDesc: [false]
                    }
                }
            })
            await flushPromises()
            await wrapper.vm.desabilitaBotaoNovo()

            await flushPromises()
            expect(wrapper.emitted().desabilitaBotaoNovo).toBeTruthy()
        })

        it('deve habilitar botao novo', async () => {
            wrapper = shallowMount(ReconhecimentoListagem, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    paginas: 1,
                    totalItens: 1,
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['situacao'],
                        sortDesc: [false]
                    }
                }
            })
            await flushPromises()
            await wrapper.vm.habilitaBotaoNovo()

            await flushPromises()
            expect(wrapper.emitted().habilitaBotaoNovo).toBeTruthy()
        })

        it('deve resetar a pagina', async () => {
            wrapper = shallowMount(ReconhecimentoListagem, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    paginas: 1,
                    totalItens: 1,
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['situacao'],
                        sortDesc: [false]
                    }
                }
            })
            await flushPromises()
            await wrapper.vm.resetaPage()

            await flushPromises()
            expect(mutations[mutationTypes.CONFIGURACAO.RECONHECIMENTO.RESETA_PAGE_RECONHECIMENTO]).toHaveBeenCalled()
        })
    })
})
