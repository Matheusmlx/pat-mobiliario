import {shallowMount} from '@vue/test-utils'
import ListagemDocumentosItemVisualizar from './ListagemDocumentosItemVisualizar'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {mask} from 'vue-the-mask'

describe('ListagemDocumentosItemVisualizar', () => {
    let wrapper, localVue, errors, $validator, router, store, mutations,actions, state, directives, $t = jest.fn(), windowSpy

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
            getObrigatorioRotulosPersonalizados: () => jest.fn(),
            getObjetoValidado: () => jest.fn().mockReturnValue(true)
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

    const defaultMount = (stubs) => shallowMount(ListagemDocumentosItemVisualizar, {
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
                            name: 'Mobiliario.Consulta'
                        }
                    ]
                }
            },
        }
        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'VisualizarIncorporacaoDocumentos',
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

        it('Deve emitir um evento, baixarAnexo, ao clicar no botão de download', async () => {
            wrapper = wrapper = defaultMount()
            wrapper.vm.value = {uriAnexo:'AA/aa'}
            wrapper.vm.tratarCampoAnexo()

            expect(wrapper.vm.nomeAnexo).toEqual('aa')

        })
    })

})

