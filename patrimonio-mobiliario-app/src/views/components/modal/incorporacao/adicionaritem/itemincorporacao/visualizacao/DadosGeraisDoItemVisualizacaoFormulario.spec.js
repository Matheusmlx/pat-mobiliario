import { shallowMount } from '@vue/test-utils'
import Vuex from 'vuex'
import DadosGeraisDoItemVisualizacaoFormulario from './DadosGeraisDoItemVisualizacaoFormulario'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {mutationTypes, actionTypes} from '@/core/constants'
import {mask} from 'vue-the-mask'
import flushPromises from 'flush-promises'

jest.mock('vue-i18n')

describe('DadosGeraisDoItemVisualizacaoFormulario', async () => {
    let wrapper, localVue, router, state, mutations, $validator, actions, errors, vuetify, $options, directives, $emit = jest.fn()

    const rotulosPersonalizados = {
        namespaced: true,
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
            getObrigatorioRotulosPersonalizados: () => jest.fn(),
            getItemIncorporacaoValidado: () => jest.fn().mockReturnValue(true)
        }
    }

    const  dadosGeraisDoItem = {
        id:1,
        idContaContabil:15,
        contaContabil: {codigo:'122323213', descricao:'descricao'},
        codigo:'0151425',
        descricao:'AUTOMOVEL MARCA VOLKSWAGEN, MOD.SATANA 2.0',
        marca:'Ford',
        modelo:'A356',
        uriImagem:'1/2.pdf',
        quantidade:'10'
    }

    const estadosConservacao = {
        estadosConservacao: [
            {
                id: 1,
                nome: 'Ótimo',
                prioridade: 1
            },
            {
                id: 2,
                nome: 'Bom',
                prioridade: 2
            },
            {
                id: 3,
                nome: 'Regular',
                prioridade: 3
            }
        ]
    }

    beforeEach(async () => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        directives = {
            mask: {
                ...mask,
                tokens: {
                    ...mask.tokens,
                    '*': /./,
                }
            },
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            },
        }
        errors = {
            collect: jest.fn(),
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'DadosGeraisDoItemModal',
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
                    domainId: 1
                }
            },
            estadosDeConservacao: 'INSERVIVEL',
            categoriaVeiculo: 'EMBARCACOES',
            combustivelVeiculo: 'GASOLINA',
            mostraErroSituacao: false,
            errorObrigatoriedadeAnexo: 0,
            errorTamanhoExcedido: 0,
            nomeAnexo: ''
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO]: jest.fn()
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO]: jest.fn().mockReturnValue(estadosConservacao),
            [actionTypes.COMUM.BUSCAR_NATUREZAS_DESPESA]: jest.fn().mockReturnValue({items:[{id:1,descricao:'VEICULOS DIVERSOS',despesa:'44905248'}]})
        }
    })

    const defaultMount = (stubs) => shallowMount(DadosGeraisDoItemVisualizacaoFormulario, {
        localVue,
        router,
        vuetify,
        directives,
        propsData: {
            dadosDeEntrada: {}
        },
        stubs,
        store: new Vuex.Store({
            state, mutations, actions,
            modules: {
                rotulosPersonalizados
            },
        }),
        mocks: { $validator, errors, $options, $emit }
    })

    describe('Methods', async () => {

        it('Deve buscar todos estados de conservação', async () => {
            const resultado = estadosConservacao

            wrapper = defaultMount()

            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
            expect(wrapper.vm.estadosConservacao).toEqual(resultado.estadosConservacao)
        })

        it('Deve tratar campo anexo', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.tratarCampoAnexo()

            expect(wrapper.vm.nomeAnexo).toEqual('2.pdf')
        })

        it('Deve emitir mensagem se não houver natureza de despesa', async () => {
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO]: jest.fn().mockReturnValue(estadosConservacao),
                [actionTypes.COMUM.BUSCAR_NATUREZAS_DESPESA]: jest.fn().mockReturnValue({items:[]})
            }
            wrapper =  shallowMount(DadosGeraisDoItemVisualizacaoFormulario, {
                localVue,
                router,
                vuetify,
                directives,
                propsData: {
                    dadosDeEntrada: {}
                },
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    },
                }),
                mocks: { $validator, errors, $options, $emit }
            })
            await flushPromises()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Item sem conta contábil, natureza de despesa ou tipo ativos. Escolha outro item')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

    })

    describe('Events', () => {

        it('Deve emitir bloquear passo 3 = false se todos os campos obrigatórios preenchidos', async () => {
            wrapper = defaultMount()
            await flushPromises()


            expect(wrapper.emitted().bloquearPasso3).toBeTruthy()
            expect(wrapper.emitted().bloquearPasso3[0][0]).toEqual(false)
        })

        it('Deve emitir bloquear passo 3 = true se campos obrigatórios não preenchidos', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                getters: {
                    getNomeRotulosPersonalizados: () => jest.fn(),
                    getObrigatorioRotulosPersonalizados: () => jest.fn(),
                    getItemIncorporacaoValidado: () => jest.fn().mockReturnValue(false)
                }
            }
            wrapper =  shallowMount(DadosGeraisDoItemVisualizacaoFormulario, {
                localVue,
                router,
                vuetify,
                directives,
                propsData: {
                    dadosDeEntrada: {}
                },
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    },
                }),
                mocks: { $validator, errors, $options, $emit }
            })
            await flushPromises()

            expect(wrapper.emitted().bloquearPasso3).toBeTruthy()
            expect(wrapper.emitted().bloquearPasso3[0][0]).toEqual(true)
        })

        it('Deve aplicar mascara na conta contábil', async () => {
            wrapper = defaultMount()

            await flushPromises()

            expect(wrapper.vm.contaContabil).toEqual('12.232.32.13 - descricao')
        })

    })

})
