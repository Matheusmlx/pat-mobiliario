import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import ModalIncorporacaoItem from './ModalIncorporacaoItem'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {mutationTypes,actionTypes} from '@/core/constants'

const  dadosGeraisDoItem = {
    id:1,
    idIncorporacao: 1,
    nome: 'Teste',
    codigo: '001',
    naturezaDespesa:{id:1},
    situacao: 'EM_ELABORACAO'
}

describe('ModalIncorporacaoItem.vue' , () => {
    let wrapper, localVue, state, mutations, actions, router

    const rotulosPersonalizados = {
        namespaced: true,
        getters: {
            getItemIncorporacaoValidado: () => jest.fn().mockReturnValue(true)
        }
    }

    const createDefaultStore = () => new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})

    beforeEach(() => {

        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'ModalIncorporacaoItem',
                    params: {
                        incorporacaoId: 1,
                        itemIncorporacaoId: 1
                    },
                    meta: {
                        modal: {
                            title: 'Etapa 1 - Selecione o item',
                            podeVoltar: false,
                            showModal: true,
                            visualizacao: false
                        }
                    }
                },
            },
        }


        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                }
            },
            itemIncorporacao: {
                dadosGerais: {
                    id:1,
                    idIncorporacao: 1,
                    codigo: '001',
                    quantidade: 2,
                    valorTotal: 50,
                    contaContabil: { id: 1 }
                },
                autoSave: {
                    show: false
                },
                rotaModal:{
                    origem: 'ItensIncorporacaoListagem'
                }
            }
        }

        mutations = {
            [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.CADASTRAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DELETAR_ITEM_INCORPORACAO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
            [actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]: jest.fn()
        }

    })

    describe('Events' , () => {

        it('Deve emitir o evento de salvar novo item', async () => {

            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'salvarNovoItem\', {})"></button></div>'
            }

            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.CADASTRAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
        })

        it('Deve emitir o evento de ir para a rota de editar item no passo um', async () => {
            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'editarItemPassoUm\', {})"></button></div>'
            }

            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            await flushPromises()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'DadosGeraisDoItemModal', params: {itemIncorporacaoId: 1}})
        })

        it('Deve emitir o evento de efetuar a troca de item no passo um', async () => {
            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'editarItemPassoUm\', {})"></button></div>'
            }

            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            wrapper.vm.$store.state.itemIncorporacao.dadosGerais = {
                id:1,
                idIncorporacao: 1,
                codigo: '002',
                quantidade: 2,
                valorTotal: 50,
                contaContabil: { id: 1 }
            }

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DELETAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.CADASTRAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'DadosGeraisDoItemModal', params: {itemIncorporacaoId: 1}})
        })

        it('Deve emitir o evento de avançar a visualização no passo um (ir para o passo 2)', async () => {
            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'avancarEtapaDoisVisualizar\', {})"></button></div>'
            }
            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            expect(router.replace.mock.calls[0][0]).toEqual({name: 'VisualizacaoDadosGerais', params: {incorporacaoId: 1, itemIncorporacaoId: 1}})
        })

        it('Deve emitir o evento de avançar para a etapa 3', async () => {
            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'avancarEtapaTres\', {})"></button></div>'
            }

            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            expect(actions[actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]).toHaveBeenCalled()
            await flushPromises()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'IncorporacaoItemListagemPatrimonios', params: {incorporacaoId: 1, itemIncorporacaoId: 1}})
        })

        it('Deve emitir o evento de voltar para etapa 1 e cadastrar um novo item', async () => {
            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'voltarPassoNovo\', {})"></button></div>'
            }

            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ItensCatalogoModalNovo', params: { incorporacaoId: 1}})
        })

        it('Deve emitir o evento de voltar para etapa 1 na edição de item', async () => {
            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'voltarPassoUmEdicao\', {})"></button></div>'
            }

            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ItensCatalogoModalEdicao', params: { incorporacaoId: 1, itemIncorporacaoId: 1}})
        })

        it('Deve emitir o evento de voltar para etapa 1 no modo visualização', async () => {
            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'voltarPassoUmVisualizar\', {})"></button></div>'
            }

            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ItensCatalogoModalVisualizacaoCadastrado', params: { itemIncorporacaoId: 1}})
        })

        it('Deve emitir o evento de voltar para etapa 2 no modo edição', async () => {
            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'voltarPassoDoisEdicao\', {})"></button></div>'
            }

            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')

            expect(router.replace.mock.calls[0][0]).toEqual({name: 'DadosGeraisDoItemModal', params: { itemIncorporacaoId: 1}})
        })

        it('Deve emitir o evento para fechar o modal', async () => {
            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'fechar\', {})"></button></div>'
            }

            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ItensIncorporacaoListagem', params: { incorporacaoId: 1}})
        })

    })

    describe('Methods', () =>{
        it('Deve enviar true ao verificar se pode continuar', async () => {
            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router
            })
            await flushPromises()
            wrapper.vm.tratarEventoVerificarContinuar(true)

            expect(wrapper.vm.continuar).toBeTruthy()
        })

        it('Deve enviar false ao verificar se pode continuar', async () => {
            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router
            })
            await flushPromises()
            wrapper.vm.tratarEventoVerificarContinuar(false)

            expect(wrapper.vm.continuar).toBeFalsy()
        })

        it('Deve enviar undefined ao verificar se pode continuar', async () => {
            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router
            })
            await flushPromises()
            const item = {}
            wrapper.vm.tratarEventoVerificarContinuar(item.value)

            expect(wrapper.vm.continuar).toBeTruthy()
        })

        it('Deve finalizar o item de incorporação', async () => {
            const router = {
                init: jest.fn(),
                replace: jest.fn(),
                history: {
                    current: {
                        name: 'IncorporacaoItemListagemPatrimonios',
                        params: {
                            incorporacaoId: 1,
                            itemIncorporacaoId: 1
                        },
                        meta: {
                            modal: {
                                title: 'Etapa 3 - Dados individuais dos patrimônios',
                                podeVoltar: true,
                                showModal: true,
                                visualizacao: false
                            },
                            requiresAuth: true,
                            authorities: ['Mobiliario.Normal']
                        }
                    }
                }
            }

            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'fechar\', {})"></button></div>'
            }

            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: createDefaultStore(),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()
            expect(wrapper.vm.incorporacaoItem.situacao).toEqual('FINALIZADO')
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ItensIncorporacaoListagem', params: { incorporacaoId: 1}})
        })

        it('Não deve finalizar o item de incorporação por estar incompleto', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                getters: {
                    getItemIncorporacaoValidado: () => jest.fn().mockReturnValue(false)
                }
            }
            const incorporacaoItem = { situacao: 'EM_ELABORACAO'}
            const actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(incorporacaoItem),
            }

            const ModalIncorporacaoAcoes = {
                template: '<div><button class="stub" @click="$emit(\'fechar\', {})"></button></div>'
            }

            wrapper = shallowMount(ModalIncorporacaoItem, {
                localVue,
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    },
                }),
                router,
                stubs: {
                    ModalIncorporacaoAcoes
                }
            })

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(wrapper.vm.incorporacaoItem.situacao).toEqual('EM_ELABORACAO')
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ItensIncorporacaoListagem', params: { incorporacaoId: 1}})
        })
    })
})
