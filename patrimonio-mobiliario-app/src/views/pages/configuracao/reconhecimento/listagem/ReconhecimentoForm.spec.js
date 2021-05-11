import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {shallowMount} from '@vue/test-utils'
import ReconhecimentoForm from './ReconhecimentoForm'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'

describe('ReconhecimentoForm.vue', () => {

    let wrapper, localVue, $validator, store, state

    $validator = {
        _base: {
            validateAll: jest.fn().mockReturnValue(true)
        },
        resume: jest.fn(),
        pause: jest.fn()
    }

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
            getObrigatorioRotulosPersonalizados: () => jest.fn()
        }
    }

    const errors = {
        collect() {
        },
        clear: jest.fn()
    }

    beforeEach(() => {

        state = {
            reconhecimento: {
                todosReconhecimentos: {
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

        localVue = applicationTestBuilder.build()

        store = new Vuex.Store({
            state,
            modules: {
                rotulosPersonalizados
            }
        })
    })

    describe('watch', () => {

        it('deve desativar a edição quando houver alteração na lista de reconhecimentos', async () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    }
                },
                mocks: {errors, $validator}
            })
            wrapper.setProps({
                value: {
                    id: '1',
                    nome: 'COMPRAS',
                    situacao: 'ATIVO',
                    execucaoOrcamentaria: 'SIM'
                }
            })
            await flushPromises()
            expect(wrapper.vm.modoEdicao).toEqual(false)
        })

        it('deve desativar a edição e habilitar ações quando houver alteração no filtro', async () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    permitirAcoes: false
                },
                mocks: {errors, $validator}
            })
            state.reconhecimento.todosReconhecimentos.filtros.conteudo = {
                default: null,
                label: 'Pesquisa',
                value: 'COMPRAS'
            }

            wrapper.setProps({
                permitirAcoes: true
            })

            await flushPromises()

            expect(wrapper.vm.modoEdicao).toEqual(false)
            expect(wrapper.vm.permitirAcoes).toEqual(true)
        })

        it('deve desativar a edição e habilitar ações quando houver alteração na paginação', async () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    },
                    permitirAcoes: false
                },
                mocks: {errors, $validator}
            })
            state.reconhecimento.todosReconhecimentos.paginacao = {
                descending: false,
                groupBy: [],
                groupDesc: [],
                itemsPerPage: 10,
                multiSort: false,
                mustSort: false,
                page: 5,
                rowsPerPage: 10,
                sortBy: ['codigo'],
                sortDesc: [false]
            }

            wrapper.setProps({
                permitirAcoes: true
            })
            await flushPromises()

            expect(wrapper.vm.modoEdicao).toEqual(false)
            expect(wrapper.vm.permitirAcoes).toEqual(true)
        })
    })

    describe('methods', () => {

        it('deve ativar o modo de edição do reconhecimento selecionado', () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    }
                },
                mocks: {errors, $validator}
            })
            flushPromises()
            wrapper.vm.ativarModoEdicao()

            flushPromises()
            expect(wrapper.vm.modoEdicao).toEqual(true)
        })

        it('deve desativar o modo de edição do reconhecimento selecionado', () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                mocks: {errors, $validator},
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    }
                }
            })
            flushPromises()
            wrapper.vm.desativarModoEdicao()

            flushPromises()
            expect(wrapper.vm.modoEdicao).toEqual(false)
        })

        it('deve emitir o evento de inserirNovoReconhecimento caso o formulário for validado', async () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                mocks: {errors, $validator},
                propsData: {
                    value: {
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    }
                }
            })
            await flushPromises()
            await wrapper.vm.salvar()

            await flushPromises()
            expect(wrapper.emitted('inserirNovoReconhecimento')).toBeTruthy
            expect(wrapper.vm.desativarModoEdicao()).toBeTruthy
        })

        it('deve chamar o metodo de desativarModoEdicao e emitir o evento atualizarReconhecimento quando o formulário for validado e não for um reconhecimento novo', async () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                mocks: {errors, $validator},
                propsData: {
                    value: {
                        id: 1,
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM'
                    }
                }
            })
            await flushPromises()
            await wrapper.vm.salvar()

            await flushPromises()
            expect(wrapper.emitted('atualizarReconhecimento')).toBeTruthy
            expect(wrapper.vm.desativarModoEdicao()).toBeTruthy
        })

        it('não deve emitir o evento de inserirNovoReconhecimento quando há campos em brancos', async () => {
            $validator = {
                resume: jest.fn(),
                pause: jest.fn(),
                _base: {
                    validateAll: jest.fn().mockReturnValue(false)
                }
            }

            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                mocks: {errors, $validator},
                propsData: {
                    value: {
                        nome: '',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: ''
                    }
                }
            })
            await flushPromises()
            await wrapper.vm.salvar()

            await flushPromises()
            expect(wrapper.emitted('inserirNovoReconhecimento')).toBeFalsy
            expect(wrapper.vm.desativarModoEdicao()).toBeFalsy
        })

        it('deve emitir o evento de cancelar para o componente pai', async () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                mocks: {errors, $validator},
                propsData: {
                    value: {
                        nome: '',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: ''
                    }
                }
            })
            await flushPromises()
            await wrapper.vm.cancelar()

            await flushPromises()
            expect(wrapper.emitted('cancelarEdicao')).toBeTruthy
            expect(wrapper.vm.desativarModoEdicao()).toBeTruthy
        })

        it('deve retornar true quando não houver erros na validação dos campos', async () => {

            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                mocks: {errors, $validator},
                propsData: {
                    value: {
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'NAO'
                    }
                }
            })
            await flushPromises()
            await wrapper.vm.validarDadosFormulario()

            await flushPromises()
            expect(wrapper.vm.validarDadosFormulario()).toBeTruthy
        })

        it('deve retornar false quando há erros na validação dos campos', async () => {

            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                mocks: {errors, $validator},
                propsData: {
                    value: {
                        nome: '',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: ''
                    }
                }
            })
            await flushPromises()
            await wrapper.vm.validarDadosFormulario()

            await flushPromises()
            expect(wrapper.vm.validarDadosFormulario()).toBeFalsy
        })

        it('deve emitir habilitarAcoes para o componente pai', () => {

            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                mocks: {errors, $validator},
                propsData: {
                    value: {
                        nome: '',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: ''
                    }
                }
            })
            wrapper.vm.habilitarAcoes()

            expect(wrapper.emitted().habilitarAcoes).toBeTruthy()
        })

        it('deve emitir desativarAcoes para o componente pai', () => {

            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                mocks: {errors, $validator},
                propsData: {
                    value: {
                        nome: '',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: ''
                    }
                }
            })
            wrapper.vm.desativarAcoes()

            expect(wrapper.emitted().desativarAcoes).toBeTruthy()
        })
    })

    describe('Computed', () => {
        it('deve retornar string Empenho, Nota Fiscal', () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM',
                        notaFiscal: true,
                        empenho: true
                    }
                },
                mocks: {errors, $validator}
            })
            flushPromises()
            expect(wrapper.vm.camposObrigatorios).toEqual('Empenho, Nota Fiscal')
        })

        it('deve retornar string Nota Fiscal', () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM',
                        notaFiscal: true,
                        empenho: false
                    }
                },
                mocks: {errors, $validator}
            })
            flushPromises()
            expect(wrapper.vm.camposObrigatorios).toEqual('Nota Fiscal')
        })

        it('deve retornar string Empenho', () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM',
                        notaFiscal: false,
                        empenho: true
                    }
                },
                mocks: {errors, $validator}
            })
            flushPromises()
            expect(wrapper.vm.camposObrigatorios).toEqual('Empenho')
        })

        it('deve retornar string ----', () => {
            wrapper = shallowMount(ReconhecimentoForm, {
                localVue,
                store,
                propsData: {
                    value: {
                        id: '1',
                        nome: 'COMPRAS',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: 'SIM',
                        notaFiscal: false,
                        empenho: false
                    }
                },
                mocks: {errors, $validator}
            })
            flushPromises()
            expect(wrapper.vm.camposObrigatorios).toEqual('----')
        })
    })

})
