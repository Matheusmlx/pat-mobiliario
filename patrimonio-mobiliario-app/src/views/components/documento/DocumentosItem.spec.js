import {shallowMount} from '@vue/test-utils'
import DocumentosItem from './DocumentosItem'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {mask} from 'vue-the-mask'
import flushPromises from 'flush-promises'
import {mutationTypes} from '@/core/constants'

describe('DocumentosItem', () => {
    let wrapper, localVue, errors, $validator, router, store, mutations, actions, state, directives, $t = jest.fn(),
        windowSpy

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getObrigatorioRotulosPersonalizados: () => jest.fn(),
            getDocumentoNumeroTipoObrigatorioValidado: () => jest.fn().mockReturnValue(true),
            getDocumentoTodosObrigatoriosValidado: () => jest.fn().mockReturnValue(true)
        }
    }

    localVue = applicationTestBuilder.build()

    directives = {
        mask: {
            ...mask,
            tokens: {
                ...mask.tokens,
                '*': /./,
            }
        },
    }

    errors = {
        collect: jest.fn(),
    }

    $validator = {
        _base: {
            validateAll: jest.fn().mockReturnValue(true),
            errors: {
                clear: jest.fn(),
            },
        },
    }

    mutations = {
        [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
        [mutationTypes.LOKI.ENABLE_AUTO_SAVING]: jest.fn(),
        [mutationTypes.LOKI.DISABLE_AUTO_SAVING]: jest.fn(),
        [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
        [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
        [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
    }

    const defaultMount = (stubs) => shallowMount(DocumentosItem, {
        localVue,
        router,
        directives,
        stubs,
        store: new Vuex.Store({
            state, mutations, actions,
            modules: {
                rotulosPersonalizados
            }
        }),
        mocks: {$validator, errors, $t},
    })

    const vueTextFieldStub = {
        render: () => {
        },
        methods: {
            focus: () => {
            }
        }
    }

    beforeEach(async () => {
        state = {
            regrasAnexo: '',
            nomeAnexo: '',
            deletarBotao: 'grey',
            apagar: true,
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'Mobiliario.Normal'
                        }
                    ]
                }
            },
        }
        router = {
            init: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'IncorporacaoDocumentosEdicao',
                    params: {
                        incorporacaoId: 1,
                    },
                },
            },
        }

        store = new Vuex.Store(({state}))

        windowSpy = jest.spyOn(global, 'window', 'get')
        windowSpy.mockImplementation(() => ({
            location: jest.fn(),
            getComputedStyle: jest.fn(),
            addEventListener: jest.fn(),
            removeEventListener: jest.fn(),
            $i18n: {
                locale: 'rotulosPersonalizados',
                getLocaleMessage: () => rotulosPersonalizados.i18n
            }
        }))
    })

    describe('Methods', () => {
        it('Deve enviar ao componente pai um emit de salvar se documento conter dados obrigatorios para tipo Nota Fiscal', async () => {
            wrapper = defaultMount()
            wrapper.setProps({
                tipoDocumento: [{id: 1, descricao: 'Nota Fiscal'}],
                value: {numero: '1', valor: 600, data: '05/11/2016', uriAnexo: 'anexo', tipo: 1},
                index: 1,
                permissaoEdicao: true
            })

            wrapper.vm.tratarEventoEdicaoCampo()
            await flushPromises()

            expect(wrapper.emitted().salvar).toBeTruthy()
        })

        it('Deve enviar ao componente pai um emit de salvar se documento conter dados obrigatorios para tipo Nota de empenho', async () => {
            wrapper = defaultMount()
            wrapper.setProps({
                tipoDocumento: [{id: 1, descricao: 'Nota de Empenho'}],
                value: {numero: '1', valor: 600, data: '05/11/2016', uriAnexo: 'anexo', tipo: 1},
                index: 1,
                permissaoEdicao: true
            })
            wrapper.vm.tratarEventoEdicaoCampo()
            await flushPromises()


            expect(wrapper.emitted().salvar).toBeTruthy()
        })

        it('Deve enviar ao componente pai um emit de salvar se documento conter todos dados obrigatorios para tipo diferente de nota fiscal', async () => {
            wrapper = defaultMount()
            wrapper.setProps({
                tipoDocumento: [{id: 1, descricao: 'Nota Fiscal'}],
                value: {numero: '1', valor: null, data: null, uriAnexo: 'anexo', tipo: 2},
                index: 1,
                permissaoEdicao: true
            })
            wrapper.vm.tratarEventoEdicaoCampo()
            await flushPromises()

            expect(wrapper.emitted().salvar).toBeTruthy()
        })

        it('Deve enviar ao componente pai um emit de salvar se documento conter todos dados obrigatorios para tipo diferente de nota de empenho', async () => {
            wrapper = defaultMount()
            wrapper.setProps({
                tipoDocumento: [{id: 1, descricao: 'Nota de Empenho'}],
                value: {numero: '1', valor: null, data: null, uriAnexo: 'anexo', tipo: 2},
                index: 1,
                permissaoEdicao: true
            })
            wrapper.vm.tratarEventoEdicaoCampo()
            await flushPromises()

            expect(wrapper.emitted().salvar).toBeTruthy()
        })

        it('Não deve enviar ao componente pai um emit de salvar se documento não conter dados obrigatorios para tipo Nota Fiscal', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getObrigatorioRotulosPersonalizados: () => jest.fn(),
                    getDocumentoNumeroTipoObrigatorioValidado: () => jest.fn().mockReturnValue(true),
                    getDocumentoTodosObrigatoriosValidado: () => jest.fn().mockReturnValue(false)
                }
            }
            wrapper = shallowMount(DocumentosItem, {
                localVue,
                router,
                directives,
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $t},
            })
            wrapper.setProps({
                tipoDocumento: [{id: 1, descricao: 'Nota Fiscal'}],
                value: {numero: '1', valor: 600, data: '05/11/2016', uriAnexo: 'anexo', tipo: 1},
                index: 1,
                permissaoEdicao: true
            })

            wrapper.vm.tratarEventoEdicaoCampo()
            await flushPromises()

            expect(wrapper.emitted().salvar).not.toBeTruthy()
        })

        it('Não deve enviar ao componente pai um emit de salvar se documento não conter dados obrigatorios para tipo Nota de empenho', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getObrigatorioRotulosPersonalizados: () => jest.fn(),
                    getDocumentoNumeroTipoObrigatorioValidado: () => jest.fn().mockReturnValue(true),
                    getDocumentoTodosObrigatoriosValidado: () => jest.fn().mockReturnValue(false)
                }
            }
            wrapper = shallowMount(DocumentosItem, {
                localVue,
                router,
                directives,
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $t},
            })
            wrapper.setProps({
                tipoDocumento: [{id: 1, descricao: 'Nota de Empenho'}],
                value: {numero: '1', valor: 600, data: '05/11/2016', uriAnexo: 'anexo', tipo: 1},
                index: 1,
                permissaoEdicao: true
            })
            wrapper.vm.tratarEventoEdicaoCampo()
            await flushPromises()


            expect(wrapper.emitted().salvar).not.toBeTruthy()
        })

        it('Não deve enviar ao componente pai um emit de salvar se documento não conter todos dados obrigatorios para tipo diferente de nota fiscal', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getObrigatorioRotulosPersonalizados: () => jest.fn(),
                    getDocumentoNumeroTipoObrigatorioValidado: () => jest.fn().mockReturnValue(false),
                    getDocumentoTodosObrigatoriosValidado: () => jest.fn().mockReturnValue(true)
                }
            }
            wrapper = shallowMount(DocumentosItem, {
                localVue,
                router,
                directives,
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $t},
            })
            wrapper.setProps({
                tipoDocumento: [{id: 1, descricao: 'Nota Fiscal'}],
                value: {numero: '1', valor: null, data: null, uriAnexo: 'anexo', tipo: 2},
                index: 1,
                permissaoEdicao: true
            })
            wrapper.vm.tratarEventoEdicaoCampo()
            await flushPromises()

            expect(wrapper.emitted().salvar).not.toBeTruthy()
        })

        it('Não deve enviar ao componente pai um emit de salvar se documento não conter todos dados obrigatorios para tipo diferente de nota de empenho', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getObrigatorioRotulosPersonalizados: () => jest.fn(),
                    getDocumentoNumeroTipoObrigatorioValidado: () => jest.fn().mockReturnValue(false),
                    getDocumentoTodosObrigatoriosValidado: () => jest.fn().mockReturnValue(true)
                }
            }
            wrapper = shallowMount(DocumentosItem, {
                localVue,
                router,
                directives,
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $t},
            })
            wrapper.setProps({
                tipoDocumento: [{id: 1, descricao: 'Nota de Empenho'}],
                value: {numero: '1', valor: null, data: null, uriAnexo: 'anexo', tipo: 2},
                index: 1,
                permissaoEdicao: true
            })
            wrapper.vm.tratarEventoEdicaoCampo()
            await flushPromises()

            expect(wrapper.emitted().salvar).not.toBeTruthy()
        })

        it('Não deve emitir o evento salvar caso o estado permissaoEdicao for igual a false', async () => {
            wrapper = defaultMount()
            wrapper.setProps({
                tipoDocumento: [{id: 1, descricao: 'Nota de Empenho'}],
                value: {numero: '1', valor: null, data: null, uriAnexo: 'anexo', tipo: 2},
                index: 1,
                permissaoEdicao: false
            })
            wrapper.vm.tratarEventoEdicaoCampo()

            expect(wrapper.emitted().salvar).not.toBeTruthy()
        })

        it('Deve emitir um evento, removerDocumento, ao clicar no botão de excluir', async () => {
            wrapper = wrapper = defaultMount({'v-text-field': vueTextFieldStub})
            wrapper.vm.removerDocumento()
            wrapper.vm.$emit('removerDocumento')
            await wrapper.vm.$nextTick()

            expect(wrapper.emitted().removerDocumento).toBeTruthy()
        })

        it('Não deve salvar anexo se tamanho maior que 15116247', async () => {
            wrapper = defaultMount()
            wrapper.setProps({
                value: {numero: '1', valor: null, data: null, uriAnexo: {name: 'data.png', size: 15116248}, tipo: 2}
            })

            await flushPromises()
            wrapper.vm.tratarEventoSalvarUri()
            await flushPromises()

            expect(wrapper.vm.validarTamanhoMaximo(15116248)).toEqual(true)
            expect(wrapper.emitted().salvarAnexo).toBeFalsy()
        })

        it('Não deve salvar anexo se extensão invalida', async () => {
            wrapper = defaultMount()
            wrapper.setProps({
                value: {numero: '1', valor: null, data: null, uriAnexo: {name: 'data.mgp', size: 15116248}, tipo: 2}
            })

            await flushPromises()
            wrapper.vm.tratarEventoSalvarUri()
            await flushPromises()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Extensão do arquivo inválida. Envie arquivos nos seguintes formatos .pdf .jpg .png')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('error')
            expect(wrapper.emitted().salvarAnexo).toBeFalsy()
        })

        it('Deve salvar anexo', async () => {
            wrapper = defaultMount()
            wrapper.vm.value = {numero: '1', valor: null, data: null, uriAnexo: {name: 'data.png', size: 5000}, tipo: 2}

            await flushPromises()
            wrapper.vm.tratarEventoSalvarUri()
            await flushPromises()

            expect(wrapper.emitted().salvarUri).toBeTruthy()
        })

        it('Deve anular anexo', async () => {
            wrapper = defaultMount()
            wrapper.vm.value = {numero: '1', valor: null, data: null, uriAnexo: {name: 'data.png', size: 5000}, tipo: 2}
            wrapper.vm.permissaoEdicao = true

            await flushPromises()
            wrapper.vm.anularCampoAnexo()
            await flushPromises()

            expect(wrapper.vm.value.uriAnexo).toEqual(null)
            expect(wrapper.vm.errorObrigatoriedadeAnexo).toEqual(0)
            expect(wrapper.vm.errorTamanhoExcedido).toEqual(0)
        })
    })

})

