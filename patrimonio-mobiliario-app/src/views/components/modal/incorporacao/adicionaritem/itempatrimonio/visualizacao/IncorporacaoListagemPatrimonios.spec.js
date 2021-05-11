import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import IncorporacaoListagemPatrimonios from './IncorporacaoListagemPatrimonios'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('IncorporacaoListagemPatrimonios', () => {
    let wrapper, actions, state, mutations, store, localVue, router

    const patrimoniosData =  {
        'items':
            [
                {
                    id: 1,
                    numero: 2398472892,
                    valorUnitario: 120.28520
                },
                {
                    id: 2,
                    numero: 2398472892,
                    valorUnitario: 120.28520
                },
                {
                    id: 3,
                    numero: 2398472892,
                    valorUnitario: 120.28520
                },
                {
                    id: 4,
                    numero: 2398472892,
                    valorUnitario: 120.28520
                }
            ],
        'paginas': 2,
        'totalElements': 3
    }

    const patrimonioItem = {
        id: 1,
        numero: 2398472892,
        valorUnitario: 120.28520
    }

    const incorporacaoItem = {
        id: 3,
        codigo: '0001285-2',
        quantidade: 20,
        descricao: 'Conjunto escolar, mod.MS, composto de carteira e cadeira: ' +
            'CARTEIRA: em aço, parede reforçada espessura de 1,50mm',
        numeracaoPatrimonial: 'Automática',
        tipoBem: 'MOVEL'
    }

    beforeEach(() => {

        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {
                    name: 'IncorporacaoItemListagemPatrimonios',
                    params: {
                        incorporacaoId: 1,
                        itemIncorporacaoId: 1
                    },
                },
            },
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO'
                }
            },
            itemIncorporacao: {
                rotaModal:{
                    origem: 'ItensIncorporacaoListagem'
                }
            },
            patrimonio: {
                resultadoBuscaTodosPatrimonios: {
                    paginacao: {
                        page: 1, rowsPerPage: 10, sortDesc: [false], sortBy: ['situacao'], defaultSort: ['situacao']
                    }
                }
            }
        }

        mutations = {
            [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS]: jest.fn(),
            [mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO]: jest.fn(),
            [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn()
        }

        actions = {
            [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]: jest.fn().mockReturnValue(patrimoniosData),
            [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(incorporacaoItem)
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, mutations, actions})
    })

    describe('Events', () => {

        it('Deve emitir o evento de paginar quando alterar a paginação', async () => {
            wrapper = shallowMount(IncorporacaoListagemPatrimonios, {
                localVue,
                store,
                router,
                stubs: {
                    TipoMovelTabela: {
                        template: '<div><button class="stub" @click="$emit(\'paginar\', {' +
                                'page: 1,\n' +
                            'rowsPerPage: 10,\n' +
                            'sortDesc: [false],\n' +
                            'sortBy: [\'situacao\'],\n' +
                            'defaultSort: [\'situacao\']' +
                            '})"></button></div>'
                    }
                }
            })

            const resetarPaginacaoSortBy = jest.spyOn(wrapper.vm, 'resetarPaginacaoSortBy')

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            expect(mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS].mock.calls[0][1]).toEqual(state.patrimonio.resultadoBuscaTodosPatrimonios.paginacao)
            expect(resetarPaginacaoSortBy).toHaveBeenCalledWith(state.patrimonio.resultadoBuscaTodosPatrimonios.paginacao)
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]).toHaveBeenCalled()
        })


    })

    describe('Methods', () => {

        it('Deve buscar o item de incorporacao pelo id da rota', async () =>{
            wrapper = shallowMount(IncorporacaoListagemPatrimonios, {
                localVue,
                store,
                router
            })
            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
        })

        it('Deve emitir o método de buscar todos os patrimônios da incorporação', async () => {
            wrapper = shallowMount(IncorporacaoListagemPatrimonios, {
                localVue,
                router,
                store,
            })
            await flushPromises()
            wrapper.vm.buscaTodosPatrimonios()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]).toHaveBeenCalled()
        })

        it('Deve chamar a action de atualizar patrimônio ao chamar o método de atualizar', () => {
            wrapper = shallowMount(IncorporacaoListagemPatrimonios, {
                localVue,
                router,
                store
            })

            wrapper.vm.atualizarPatrimonioItem(patrimonioItem)

            expect(actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]).toHaveBeenCalled()
        })

        it('Deve chamar o setItemIncorporacaoId após atualizar a rota', async () => {
            wrapper = shallowMount(IncorporacaoListagemPatrimonios, {
                localVue,
                router,
                store
            })
            await flushPromises()
            wrapper.vm.$router.history.current.params.itemIncorporacaoId = 2
            wrapper.vm.setItemIncorporacaoId()

            expect(wrapper.vm.$route.params.itemIncorporacaoId).toEqual(2)
            expect(wrapper.vm.$data.itemIncorporacaoId).toEqual(2)
        })

        it('Deve chamar o buscarItemDeIncorporacao após atualizar a rota', async () => {
            wrapper = shallowMount(IncorporacaoListagemPatrimonios, {
                localVue,
                router,
                store
            })
            await flushPromises()
            wrapper.vm.$router.history.current.params.itemIncorporacaoId = 3

            expect(wrapper.vm.$route.params.itemIncorporacaoId).toEqual(3)
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
            expect(wrapper.vm.incorporacaoItem.id).toEqual(3)
        })
    })
})
