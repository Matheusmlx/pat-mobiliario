import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import {mutationTypes} from '@/core/constants'
import Vuex from 'vuex'
import {mask} from 'vue-the-mask'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ContaContabilListagemForm from './ContaContabilListagemForm'

describe('ContaContabilListagemForm', () => {
    let wrapper, localVue, $options, errors, $validator, store, directives, mutations, state

    beforeEach(() => {
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
                reset: jest.fn()
            },
            resume: jest.fn(),
            pause: jest.fn()
        }

        localVue = applicationTestBuilder.build()
        localVue.filter('valorParaPorcentagem', (data) => data)

        errors = {
            collect: jest.fn()
        }

        state = {
            contaContabil: {
                todasContasContabeis: {
                    filtros: {
                        conteudo: {
                            default: null,
                            label: 'Pesquisa',
                            value: ''
                        }
                    },
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['codigo'],
                        sortDesc: [false]
                    }
                }
            }
        }

        mutations = {
            [mutationTypes.PATRIMONIO.SET_DOCUMENTOS]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.PATRIMONIO.REMOVER_DOCUMENTOS]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
        }

        store = new Vuex.Store({mutations, state})

    })

    const defaultshallowMount = (stubs) => shallowMount(ContaContabilListagemForm, {
        localVue,
        directives,
        stubs,
        store,
        mocks: {
            $validator,
            errors,
            $options
        },
        propsData: {
            value: {vidaUtil: '0'}
        }
    })

    describe('computed', () => {
        it('deve retornar se o tipo da conta contábil é depreciável', async () => {
            const wrapper1 = defaultshallowMount()
            wrapper1.setProps({value: {tipo: 'DEPRECIAVEL'}})

            const wrapper2 = defaultshallowMount()
            wrapper2.setProps({value: {tipo: 'NAO_DEPRECIAVEL'}})

            await flushPromises()

            expect(wrapper1.vm.$options.computed.depreciavel.call(wrapper1.vm)).toBe(true)
            expect(wrapper2.vm.$options.computed.depreciavel.call(wrapper2.vm)).toBe(false)
        })
    })

    describe('watch', () => {
        it('deve desativar o modo edição quando houver alteração na lista de conta contábeis', () => {
            wrapper = defaultshallowMount()

            wrapper.setProps({
                value: {
                    codigo: '23110103',
                    descricao: 'APARELHOS, EQUIPAMENTOS E UTENSÍLIOS MÉDICOS, ODONTOLÓGICOS, LABORATORIAIS E HOSPITALARES',
                    id: 4,
                    percentualResidual: 1,
                    situacao: 'ATIVO',
                    tipo: 'DEPRECIAVEL',
                    tipoBem: 'MOVEL',
                    vidaUtil: '1'
                }
            })
            expect(wrapper.vm.modoEdicao).toEqual(false)
        })

        it('deve desativar o modo edição e habilitar as ações quando houver alguma pesquisa por filtro', async () => {
            wrapper = defaultshallowMount()

            await flushPromises()
            state.contaContabil.todasContasContabeis.filtros.conteudo.value = '103'
            await flushPromises()
            expect(wrapper.vm.modoEdicao).toEqual(false)
            expect(wrapper.emitted().habilitarAcoes).toBeTruthy()
        })

        it('deve desativar o modo edição e habilitar as ações quando houver troca da paginação', async () => {
            wrapper = defaultshallowMount()

            await flushPromises()
            state.contaContabil.todasContasContabeis.paginacao.page = 4
            await flushPromises()
            expect(wrapper.vm.modoEdicao).toEqual(false)
            expect(wrapper.emitted().habilitarAcoes).toBeTruthy()
        })
    })

    describe('methods', () => {

        it('deve setar o modo edicao para  true', async () => {
            wrapper = defaultshallowMount()

            wrapper.vm.ativarModoEdicao()
            await flushPromises()

            expect(wrapper.vm.modoEdicao).toEqual(true)
        })

        it('deve setar o modo edicao para false', async () => {
            wrapper = shallowMount(ContaContabilListagemForm, {
                localVue,
                directives,
                store,
                mocks: {
                    $validator,
                    errors,
                    $options
                },
                propsData: {
                    value: {}
                }
            })

            wrapper.vm.desativarModoEdicao()
            await flushPromises()

            expect(wrapper.vm.modoEdicao).toEqual(false)
        })

        it('deve emitir um emmited para desativar o modo edicao', async () => {
            wrapper = defaultshallowMount()

            wrapper.vm.cancelar()
            await flushPromises()

            expect(wrapper.emitted().cancelarEdicao)
        })

        it('deve emitir desativarAcoes para o componente pai', () => {
            wrapper = shallowMount(ContaContabilListagemForm, {
                localVue,
                directives,
                store,
                mocks: {
                    $validator,
                    errors,
                    $options
                },
                propsData: {
                    value: {},
                    permitirAcoes: true
                }
            })
            wrapper.vm.desativarAcoes()

            expect(wrapper.emitted().desativarAcoes).toBeTruthy()
        })

        it('deve emitir habilitarAcoes para o componente pai', () => {
            wrapper = shallowMount(ContaContabilListagemForm, {
                localVue,
                directives,
                store,
                mocks: {
                    $validator,
                    errors,
                    $options
                },
                propsData: {
                    value: {},
                    permitirAcoes: false
                }
            })
            wrapper.vm.habilitarAcoes()

            expect(wrapper.emitted().habilitarAcoes).toBeTruthy()
        })

        it('deve retornar true quando não houver erros na validação dos campos', async () => {
            wrapper = shallowMount(ContaContabilListagemForm, {
                localVue,
                directives,
                store,
                mocks: {
                    $validator,
                    errors,
                    $options
                },
                propsData: {
                    value: {
                        codigo: '23110103',
                        descricao: 'APARELHOS, EQUIPAMENTOS E UTENSÍLIOS MÉDICOS, ODONTOLÓGICOS, LABORATORIAIS E HOSPITALARES',
                        id: 4,
                        percentualResidual: 1,
                        situacao: 'ATIVO',
                        tipo: 'DEPRECIAVEL',
                        tipoBem: 'MOVEL',
                        vidaUtil: '1'
                    },
                    permitirAcoes: false
                }
            })
            await flushPromises()
            await wrapper.vm.validarDadosFormulario()

            await flushPromises()
            expect(wrapper.vm.validarDadosFormulario()).toBeTruthy
        })

        it('deve retornar false quando há erros na validação dos campos', async () => {
            $validator = {
                _base: {
                    validateAll: jest.fn().mockReturnValue(false)
                }
            }
            wrapper = shallowMount(ContaContabilListagemForm, {
                localVue,
                directives,
                store,
                mocks: {
                    $validator,
                    errors,
                    $options
                },
                propsData: {
                    value: {
                        codigo: '23110103',
                        descricao: 'APARELHOS, EQUIPAMENTOS E UTENSÍLIOS MÉDICOS, ODONTOLÓGICOS, LABORATORIAIS E HOSPITALARES',
                        id: 4,
                        percentualResidual: null,
                        situacao: 'ATIVO',
                        tipo: null,
                        tipoBem: null,
                        vidaUtil: null
                    },
                    permitirAcoes: false
                }
            })
            await flushPromises()
            await wrapper.vm.validarDadosFormulario()

            await flushPromises()
            expect(wrapper.vm.validarDadosFormulario()).toBeFalsy
        })

        it('deve emitir evento para mudar o valor do tipo da conta contábil', async() => {
            wrapper = defaultshallowMount()

            wrapper.vm.mudarTipo('DEPRECIAVEL')

            expect(wrapper.emitted().alterarTipo).toBeTruthy()
            expect(wrapper.emitted().alterarTipo[0][0]).toBe('DEPRECIAVEL')
        })

        it('deve anular valores de percentual residual e vida util ao salvar conta com tipo não depreciável', async () => {
            wrapper = shallowMount(ContaContabilListagemForm, {
                localVue,
                directives,
                store,
                mocks: {$validator, errors},
                propsData: {
                    value: {
                        codigo: '23110103',
                        descricao: 'APARELHOS, EQUIPAMENTOS E UTENSÍLIOS MÉDICOS, ODONTOLÓGICOS, LABORATORIAIS E HOSPITALARES',
                        id: 4,
                        percentualResidual: 1,
                        situacao: 'ATIVO',
                        tipo: 'NAO_DEPRECIAVEL',
                        tipoBem: 'MOVEL',
                        vidaUtil: '1'
                    },
                    permitirAcoes: false
                }
            })
            await flushPromises()

            expect(wrapper.vm.value.percentualResidual).toEqual(1)
            expect(wrapper.vm.value.vidaUtil).toEqual('1')

            await wrapper.vm.salvar()
            await flushPromises()

            expect(wrapper.vm.value.percentualResidual).toEqual(null)
            expect(wrapper.vm.value.vidaUtil).toEqual(null)
        })

        it('deve anular percentual residual se valor maior que 99,99%', () => {
            wrapper = defaultshallowMount()

            wrapper.setProps({
                value: {percentualResidual: 1}})

            expect(wrapper.vm.value.percentualResidual).toEqual(1)

            wrapper.vm.value.percentualResidual = 100
            wrapper.vm.anulaPercentualSeMaiorQuePermitido()
            expect(wrapper.vm.value.percentualResidual).toEqual(null)
        })

        it('deve setar para null caso a vida util for igual a zero', async () => {
            wrapper = defaultshallowMount()

            wrapper.setProps({
                value: {vidaUtil: '0'}})

            expect(wrapper.vm.value.vidaUtil).toEqual('0')

            wrapper.vm.salvar()
            await  flushPromises()

            expect(wrapper.vm.value.vidaUtil).toEqual(null)
            expect(wrapper.emitted().salvar)
        })

        it('deve anular percentual residual caso valor for igual a zero', async () => {
            wrapper = defaultshallowMount()

            wrapper.setProps({
                value: {percentualResidual: 0}})

            expect(wrapper.vm.value.percentualResidual).toEqual(0)

            wrapper.vm.salvar()
            await  flushPromises()
            expect(wrapper.vm.value.percentualResidual).toEqual(null)
            expect(wrapper.emitted().salvar)
        })
    })
})
