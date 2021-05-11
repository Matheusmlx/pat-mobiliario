import { shallowMount } from '@vue/test-utils'
import Vuex from 'vuex'
import DadosGeraisDoItemModalFormulario from './DadosGeraisDoItemModalFormulario'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {mutationTypes} from '@/core/constants'
import {mask} from 'vue-the-mask'
import flushPromises from 'flush-promises'

jest.mock('vue-i18n')

describe('DadosGeraisDoItemModalFormulario', async () => {
    let wrapper, localVue, router, state, mutations, $validator, errors, vuetify, $options, directives, $emit = jest.fn()

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
        quantidade:10,
        naturezaDespesa: {
            id:1,
            situacao:'INATIVO',
            disabled: false
        }
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
    const naturezasDespesa = [{id:1,descricao:'VEICULOS DIVERSOS',despesa:'44905248'}]

    const anoFabricacaoModelo = ['2020/2021','2020/2020']

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
            [mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO]: jest.fn()
        }
    })

    const defaultMount = (stubs) => shallowMount(DadosGeraisDoItemModalFormulario, {
        localVue,
        router,
        vuetify,
        directives,
        propsData: {
            dadosDeEntrada: dadosGeraisDoItem,
            naturezasDespesa: naturezasDespesa,
            estadosConservacao: estadosConservacao,
            anoFabricacaoModelo: anoFabricacaoModelo

        },
        stubs,
        store: new Vuex.Store({
            state, mutations,
            modules: {
                rotulosPersonalizados
            },
        }),
        mocks: { $validator, errors, $options, $emit }
    })

    describe('Methods', async () => {

        it('Deve aplicar mascara na conta contábil', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.contaContabil).toEqual('12.232.32.13 - descricao')
        })

        it('Deve setar mostrarErroSituacao como true', async () => {
            wrapper = defaultMount()

            wrapper.vm.mostrarErroSituacao()

            expect(wrapper.vm.mostraErroSituacao).toEqual(true)
        })

        it('Deve tratar campo anexo', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.tratarCampoAnexo()

            expect(wrapper.vm.nomeAnexo).toEqual('2.pdf')
        })

        it('Deve tratar campo quantidade, deve anular quantidade se quantidade igual a 0', async () => {
            wrapper =  shallowMount(DadosGeraisDoItemModalFormulario, {
                localVue,
                router,
                vuetify,
                directives,
                propsData: {
                    dadosDeEntrada: {quantidade:0}
                },
                store: new Vuex.Store({
                    state, mutations,
                    modules: {
                        rotulosPersonalizados
                    },
                }),
                mocks: { $validator, errors, $options, $emit }
            })
            await flushPromises()
            wrapper.vm.tratarCampoQuantidade()

            expect(wrapper.vm.dadosDeEntrada.quantidade).toEqual(null)
        })

        it('Deve tratar campo quantidade, deve salvar formulario de quantidade diferente de 0', async () => {
            wrapper =  defaultMount()

            await flushPromises()
            wrapper.vm.tratarCampoQuantidade()
            await flushPromises()

            expect(wrapper.vm.dadosDeEntrada.quantidade).toEqual(10)
            expect(wrapper.emitted().editarItem).toBeTruthy()
            expect(wrapper.emitted().editarItem[0][0]).toEqual(wrapper.vm.dadosDeEntrada)
        })

        it('Deve tratar campo quantidade, deve fazer quantidade igual a null se quantidade vazia', async () => {
            wrapper =  defaultMount()

            await flushPromises()
            wrapper.vm.dadosDeEntrada.quantidade = ''
            wrapper.vm.tratarCampoQuantidade()
            await flushPromises()

            expect(wrapper.vm.dadosDeEntrada.quantidade).toEqual(null)
            expect(wrapper.emitted().editarItem).toBeTruthy()
            expect(wrapper.emitted().editarItem[0][0]).toEqual(wrapper.vm.dadosDeEntrada)
        })

        it('Deve anular campo anexo', async () => {
            wrapper =  defaultMount()
            wrapper.setData({
                errorObrigatoriedadeAnexo:1,
                errorTamanhoExcedido:1
            })
            await flushPromises()
            wrapper.vm.anularCampoAnexo()

            expect(wrapper.vm.dadosDeEntrada.uriImagem).toEqual(null)
            expect(wrapper.vm.errorObrigatoriedadeAnexo).toEqual(0)
            expect(wrapper.vm.errorTamanhoExcedido).toEqual(0)
            expect(wrapper.emitted().editarItem).toBeTruthy()
            expect(wrapper.emitted().editarItem[0][0]).toEqual(wrapper.vm.dadosDeEntrada)
        })

        it('Deve salvar anexo', async () => {
            wrapper =  defaultMount()
            await flushPromises()
            wrapper.vm.tratarEventoSalvarAnexo()
            await flushPromises()

            expect(wrapper.emitted().salvarAnexo).toBeTruthy()
            expect(wrapper.emitted().salvarAnexo[0][0]).toEqual(wrapper.vm.dadosDeEntrada)
        })

        it('Deve atualizar valor inserido', async () => {
            wrapper =  defaultMount()
            await flushPromises()
            wrapper.vm.atualizarValorInserido(10000)

            expect(wrapper.vm.dadosDeEntrada.valorTotal).toEqual(10000)
        })

        it('Deve retornar se contem string no nome do item', async () => {
            wrapper =  defaultMount()
            await flushPromises()
            const item= {nome:'abc'}


            expect(wrapper.vm.filtroComboAutoComplete(item,'abc')).toEqual(true)
        })

        it('Deve retornar se contem parte da string no nome do item', async () => {
            wrapper =  defaultMount()
            await flushPromises()
            const item= {nome:'abc'}


            expect(wrapper.vm.filtroComboAutoComplete(item,'ab')).toEqual(true)
        })

        it('Deve retornar false se não contem string no nome do item', async () => {
            wrapper =  defaultMount()
            await flushPromises()
            const item= {nome:'abc'}

            expect(wrapper.vm.filtroComboAutoComplete(item,'abcd')).toEqual(false)
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
            wrapper =  shallowMount(DadosGeraisDoItemModalFormulario, {
                localVue,
                router,
                vuetify,
                directives,
                propsData: {
                    dadosDeEntrada: {}
                },
                store: new Vuex.Store({
                    state, mutations,
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

        it('Deve emitir editar item e anular conta contabil', async () => {
            wrapper = defaultMount()
            wrapper.setData({
                dadosDeEntrada:{contaContabil:1}
            })
            await flushPromises()
            wrapper.vm.salvarFormulario()
            await flushPromises()

            expect(wrapper.emitted().editarItem).toBeTruthy()
            expect(wrapper.emitted().editarItem[0][0]).toEqual(wrapper.vm.dadosDeEntrada)
        })

        it('Deve resetar a página dos patrimônios', async () => {
            wrapper = defaultMount()

            const salvarFormulario = jest.spyOn(wrapper.vm, 'salvarFormulario')

            wrapper.vm.tratarCampoQuantidade()

            expect(mutations[mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO]).toHaveBeenCalled()
            expect(salvarFormulario).toHaveBeenCalled()
        })
    })

})
