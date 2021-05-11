import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import TipoEquipamentoArmamentoTabela from './TipoEquipamentoArmamentoTabela'
import {mutationTypes} from '@/core/constants'

describe('TipoEquipamentoArmamentoTabela', () => {
    let wrapper, state, store, localVue, mutations

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
            getObrigatorioRotulosPersonalizados: () => jest.fn()
        }
    }

    const patrimonio =  {
        id: 1,
        numero: 2398472892,
        valorAquisicao: 120.28520
    }

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO'
                }
            },
            patrimonio:{
                colunasEquipamentoArmamento:[]
            }
        }
        mutations = {
            [mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO]: jest.fn(),
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, mutations, modules: rotulosPersonalizados})

        localVue = applicationTestBuilder.build()
    })

    describe('Events', () => {

        it('Deve emitir o evento de paginar', async () => {
            wrapper = shallowMount(TipoEquipamentoArmamentoTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
                        {id: 1, numero: 2398472892, valorAquisicao: 120.28520},
                        {id: 2, numero: 2398472893, valorAquisicao: 120.28520},
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
    })

    describe('Methods', () => {

        it('Deve atribuir a nova páginação', async () => {
            wrapper = shallowMount(TipoEquipamentoArmamentoTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
                        {id: 1, numero: 2398472892, valorAquisicao: 120.28520},
                        {id: 2, numero: 2398472893, valorAquisicao: 120.28520},
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
            const novaPaginacao = {
                page: 2,
                rowsPerPage: 25,
                descending: false,
            }
            await wrapper.vm.tratarPaginacao(novaPaginacao)
            await flushPromises()
            expect(wrapper.vm.$data.paginacaoInterna.page).toEqual(novaPaginacao)
        })

        it('Deve resetar a páginação', async () => {
            wrapper = shallowMount(TipoEquipamentoArmamentoTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
                        {id: 1, numero: 2398472892, valorAquisicao: 120.28520},
                        {id: 2, numero: 2398472893, valorAquisicao: 120.28520},
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
            await wrapper.vm.resetaPage()
            await flushPromises()
            expect(wrapper.vm.$props.paginacao.page).toEqual(1)
        })

    })
})
