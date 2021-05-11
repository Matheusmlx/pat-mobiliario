import {shallowMount} from '@vue/test-utils'
import PanelDocumentos from './PanelDocumentos'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import { actionTypes, mutationTypes } from '@/core/constants'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import {mask} from 'vue-the-mask'

describe('PanelDocumentos', () => {
    let wrapper, localVue, store, state, mutations, $validator, errors, actions, router,vuetify, directives, $t = jest.fn()

    directives = {
        mask: {
            ...mask,
            tokens: {
                ...mask.tokens,
                '*': /./,
            }
        },
    }

    const defaultMount = (stubs) => shallowMount(PanelDocumentos, {
        localVue,
        directives,
        router,
        vuetify,
        stubs,
        store: new Vuex.Store({
            state, mutations, actions,
            modules: { rotulosPersonalizados }
        }),
        mocks: {$validator, errors, $t},
        propsData: {
            anexoUpload: {},
            documentos: documentos,
            tiposDocumento: tipoDocumentos,
            docNovo: {}

        },
    })

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
            getObrigatorioRotulosPersonalizados: () => jest.fn(),
            getDocumentoTodosObrigatoriosValidado: () => jest.fn().mockReturnValue(true),
            getDocumentoNumeroTipoObrigatorioValidado: () => jest.fn().mockReturnValue(true)
        }
    }

    const documentos = [
        {id:0, numero: '141', data: '', valor: '', tipo: 1, uriAnexo: 'notafiscal.pdf' },
        {id:1, numero: '142', data: '', valor: '', tipo: 2, uriAnexo: 'notafiscal.pdf' },
        {id:2, numero: '143', data: '', valor: '', tipo: 2, uriAnexo: 'notafiscal.pdf' },
        {id:3, numero: '144', data: '', valor: '', tipo: 3, uriAnexo: 'notafiscal.pdf' }]

    const tipoDocumentos = [
            {
                id:1,descricao:'Nota Fiscal'
            },
            {
                id:2,descricao:'Recibo'
            },
            {
                id:3,descricao:'Contrato'
            },
            {
                id:4,descricao:'Nota de Empenho'
            },
            {
                id:5,descricao:'Nota Fiscal'
            },
        ]

    const anexoRetorno = {uriAnexo: 'anexo'}

    const dado = { numero: '141', data: ' ', incorporacao:48, valor: '698', tipo: 1, uriAnexo: 'notafiscal.pdf' }

    const dadoComId = {id:1, numero: '141', data: ' ', incorporacao:48, valor: '698', tipo: 1, uriAnexo: 'notafiscal.pdf' }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        errors = {
            collect: jest.fn()
        }
        state = {
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
            documentos: [{}]
        }
        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true)
            }
        }
        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {},
            },
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            ['SET_UPLOADED_FILES']: jest.fn(),
        }

        store = new Vuex.Store({ mutations, state })
    })

    describe('Mounted', () => {
        it('Deve montar VisualizarRegistroIncorporacaoDocumentos', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.emitted().buscarTipoDocumentos).toBeTruthy()
            expect(wrapper.emitted().buscarTodosDocumentos).toBeTruthy()
        })
    })

    describe('computed', () => {
        it('Deve retornar estaEditando true', () => {
            wrapper = defaultMount()

            wrapper.vm.editando = [{id:1}]
            expect(wrapper.vm.estaEditando).toEqual(true)
        })

        it('Deve retornar estaEditando false', () => {
            wrapper = defaultMount()

            wrapper.vm.editando = []
            expect(wrapper.vm.estaEditando).toEqual(false)
        })
    })

    describe('Methods', () => {

        it('Deve modificar documentoObrigatorioId', () => {
            wrapper = defaultMount()
            const id = 1
            expect(wrapper.vm.documentoObrigatorioId).toEqual(null)
            wrapper.vm.setaIdObrigatorio(id)
            expect(wrapper.vm.documentoObrigatorioId).toEqual(1)
        })

        it('Deve fazer valorDataObrigatorio true para documento do tipo Nota Fiscal(1)', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.valorDataObrigatorio).toEqual(false)
            wrapper.vm.verificarCamposObrigatorios({tipo:1})
            expect(wrapper.vm.valorDataObrigatorio).toEqual(true)
        })

        it('Deve fazer valorDataObrigatorio true para documento do tipo Nota de Empenho(4)', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.valorDataObrigatorio).toEqual(false)
            wrapper.vm.verificarCamposObrigatorios({tipo:4})
            expect(wrapper.vm.valorDataObrigatorio).toEqual(true)
        })

        it('Deve fazer valorDataObrigatorio false para documento do tipo diferente de Nota Fiscal(1) e Nota de Empenho(4)', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.valorDataObrigatorio).toEqual(false)
            wrapper.vm.verificarCamposObrigatorios({tipo:2})
            expect(wrapper.vm.valorDataObrigatorio).toEqual(false)
        })

        it('Deve salvar o nome do componente que está editando', () => {
            wrapper = defaultMount()
            const doc = {id:1}
            const verificarCamposObrigatorios = jest.spyOn(wrapper.vm, 'verificarCamposObrigatorios')
            const verificaObrigatoriedadeLabels = jest.spyOn(wrapper.vm, 'verificaObrigatoriedadeLabels')
            wrapper.vm.setaEditando('Numero', doc)

            expect(wrapper.vm.editando).toEqual(['Numero'])
            expect(verificarCamposObrigatorios).toHaveBeenCalledWith(doc)
            expect(verificaObrigatoriedadeLabels).toHaveBeenCalled()
        })

        it('Deve retirar o nome do componente que está editando', () => {
            wrapper = defaultMount()
            wrapper.vm.editando = ['Numero']
            const verificaObrigatoriedadeLabels = jest.spyOn(wrapper.vm, 'verificaObrigatoriedadeLabels')
            wrapper.vm.retiraEditando('Numero')

            expect(wrapper.vm.editando).toEqual([])
            expect(verificaObrigatoriedadeLabels).toHaveBeenCalled()
        })

        it('Deve salvar o tipo do documento', async () => {
            wrapper = defaultMount()
            await wrapper.vm.tratarEventoSalvar(dadoComId)
            await flushPromises()

            expect(wrapper.emitted().editarDocumento).toBeTruthy()
            expect(wrapper.emitted().editarDocumento[0][0]).toEqual(dadoComId)
        })

        it('Deve adicionar um novo Documento', async() => {
            wrapper = defaultMount()
            wrapper.vm.editando = []
            wrapper.vm.docNovo= {}
            const verificaObrigatoriedadeLabels = jest.spyOn(wrapper.vm, 'verificaObrigatoriedadeLabels')
            wrapper.vm.novoDocumento()
            await flushPromises()

            expect(wrapper.vm.validateRequeridAzMoney).toEqual(0)
            expect(wrapper.vm.valorDataObrigatorio).toEqual(false)
            expect(wrapper.vm.adicionando).toEqual(true)
            expect(wrapper.emitted().criarNovoDocumento).toBeTruthy()
            expect(verificaObrigatoriedadeLabels).toHaveBeenCalled()
        })

        it('Deve falhar ao adicionar um novo Documento se estiver editando', async() => {
            wrapper = defaultMount()
            wrapper.vm.editando = ['numero']
            wrapper.vm.docNovo= {}
            const verificaObrigatoriedadeLabels = jest.spyOn(wrapper.vm, 'verificaObrigatoriedadeLabels')
            wrapper.vm.novoDocumento()
            await flushPromises()

            expect(wrapper.vm.adicionando).toEqual(false)
            expect(verificaObrigatoriedadeLabels).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize a edição do documento antes de criar um novo')
        })

        it('Deve falhar ao adicionar um novo Documento se estiver adicionando um novo documento', async() => {
            wrapper = defaultMount()
            wrapper.vm.editando = []
            wrapper.vm.docNovo= {id:1}
            const verificaObrigatoriedadeLabels = jest.spyOn(wrapper.vm, 'verificaObrigatoriedadeLabels')
            wrapper.vm.novoDocumento()
            await flushPromises()

            expect(wrapper.vm.adicionando).toEqual(false)
            expect(verificaObrigatoriedadeLabels).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize o cadastro para cadastrar novos documentos')
        })

        it('Deve retornar true se documento vazio',() => {
            wrapper = defaultMount()
            const docNovo= {}

            expect(wrapper.vm.validarCadastrosFinalizados(docNovo)).toEqual(true)
        })

        it('Deve retornar false se documento estiver preenchido',() => {
            wrapper = defaultMount()
            const docNovo= {id:1}

            expect(wrapper.vm.validarCadastrosFinalizados(docNovo)).toEqual(false)
        })

        it('Deve retornar true se estiver editando',() => {
            wrapper = defaultMount()
            wrapper.vm.editando = ['numero']

            expect(wrapper.vm.validaSeEstaEditando()).toEqual(true)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize a edição do documento antes de criar um novo')
        })

        it('Deve retornar false se não estiver editando',() => {
            wrapper = defaultMount()
            wrapper.vm.editando = []

            expect(wrapper.vm.validaSeEstaEditando()).toEqual(false)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
        })

        it('Deve mostrar mensagem de aviso (Finalize o cadastro para realizar a edição)', async() => {
            wrapper = defaultMount()
            wrapper.vm.estaAdicionando()
            await flushPromises()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize o cadastro para realizar a edição')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

        it('Deve apresentar obrigatoriedade dos labels para true exceto uriAnexo se estaEditando true, adicionando false e valorDataObrigatorio true', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
            wrapper.vm.valorDataObrigatorio = true
            wrapper.vm.editando = ['numero']
            wrapper.vm.adicionando = false
            wrapper.vm.verificaObrigatoriedadeLabels()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve apresentar obrigatoriedade dos labels para true exceto uriAnexo se estaEditando false, adicionando true e valorDataObrigatorio true', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
            wrapper.vm.valorDataObrigatorio = true
            wrapper.vm.editando = []
            wrapper.vm.adicionando = true
            wrapper.vm.verificaObrigatoriedadeLabels()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve apresentar obrigatoriedade dos labels tipo e numero para true se estaEditando false, adicionando true e valorDataObrigatorio false', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
            wrapper.vm.valorDataObrigatorio = false
            wrapper.vm.editando = []
            wrapper.vm.adicionando = true
            wrapper.vm.verificaObrigatoriedadeLabels()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve apresentar obrigatoriedade dos labels tipo e numero para true se estaEditando true, adicionando false e valorDataObrigatorio false', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
            wrapper.vm.valorDataObrigatorio = false
            wrapper.vm.editando = ['numero']
            wrapper.vm.adicionando = false
            wrapper.vm.verificaObrigatoriedadeLabels()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve esconder obrigatoriedade dos labels se estaEditando false e adicionando false', () => {
            wrapper = defaultMount()
            wrapper.vm.documentosHeaders[0].obrigatorio = true
            wrapper.vm.documentosHeaders[1].obrigatorio = true
            wrapper.vm.documentosHeaders[2].obrigatorio = true
            wrapper.vm.documentosHeaders[3].obrigatorio = true
            wrapper.vm.documentosHeaders[4].obrigatorio = true
            wrapper.vm.editando = []
            wrapper.vm.adicionando = false
            wrapper.vm.verificaObrigatoriedadeLabels()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve apresentar obrigatoriedade dos labels para true se estaEditando true, adicionando false, valorDataObrigatorio false e obrigatoriedade personalizada true', () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getNomeRotulosPersonalizados: () => jest.fn(),
                    getObrigatorioRotulosPersonalizados: () => jest.fn().mockReturnValue(true),
                    getDocumentoTodosObrigatoriosValidado: () => jest.fn().mockReturnValue(true),
                    getDocumentoNumeroTipoObrigatorioValidado: () => jest.fn().mockReturnValue(true)
                }
            }
            wrapper = shallowMount(PanelDocumentos, {
                localVue,
                directives,
                router,
                vuetify,
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $t},
            })
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
            wrapper.vm.valorDataObrigatorio = false
            wrapper.vm.editando = ['numero']
            wrapper.vm.adicionando = false
            wrapper.vm.verificaObrigatoriedadeLabels()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(true)
        })

        it('Deve esconder obrigatoriedade dos labels', () => {
            wrapper = defaultMount()
            wrapper.vm.documentosHeaders[0].obrigatorio = true
            wrapper.vm.documentosHeaders[1].obrigatorio = true
            wrapper.vm.documentosHeaders[2].obrigatorio = true
            wrapper.vm.documentosHeaders[3].obrigatorio = true
            wrapper.vm.documentosHeaders[4].obrigatorio = true
            wrapper.vm.escondeObrigatoriedadeLabels()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve apresentar obrigatoriedade dos labels exceto uriAnexo', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
            wrapper.vm.apresentaObrigatoriedadeLabelsExcetoUriAnexo()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve apresentar obrigatoriedade dos labels numero e tipo', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
            wrapper.vm.apresentaObrigatoriedadeLabelsNumeroTipo()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve apresentar obrigatoriedade do label data se obrigatoriedade configuravel true', () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getNomeRotulosPersonalizados: () => jest.fn(),
                    getObrigatorioRotulosPersonalizados: () => jest.fn().mockReturnValue(true),
                    getDocumentoTodosObrigatoriosValidado: () => jest.fn().mockReturnValue(true),
                    getDocumentoNumeroTipoObrigatorioValidado: () => jest.fn().mockReturnValue(true)
                }
            }
            wrapper = shallowMount(PanelDocumentos, {
                localVue,
                directives,
                router,
                vuetify,
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $t},
            })
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
            wrapper.vm.verificaObrigatoriedadeLabelData()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve ocultar obrigatoriedade do label data se obrigatoriedade configuravel false ', () => {
            wrapper = defaultMount()
            wrapper.vm.documentosHeaders[1].obrigatorio = true
            wrapper.vm.verificaObrigatoriedadeLabelData()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve apresentar obrigatoriedade do label valor se obrigatoriedade configuravel true', () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getNomeRotulosPersonalizados: () => jest.fn(),
                    getObrigatorioRotulosPersonalizados: () => jest.fn().mockReturnValue(true),
                    getDocumentoTodosObrigatoriosValidado: () => jest.fn().mockReturnValue(true),
                    getDocumentoNumeroTipoObrigatorioValidado: () => jest.fn().mockReturnValue(true)
                }
            }
            wrapper = shallowMount(PanelDocumentos, {
                localVue,
                directives,
                router,
                vuetify,
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $t},
            })
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
            wrapper.vm.verificaObrigatoriedadeLabelValor()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(true)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve ocultar obrigatoriedade do label valor se obrigatoriedade configuravel false ', () => {
            wrapper = defaultMount()
            wrapper.vm.documentosHeaders[2].obrigatorio = true
            wrapper.vm.verificaObrigatoriedadeLabelValor()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve apresentar obrigatoriedade do label uriAnexo se obrigatoriedade configuravel true', () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getNomeRotulosPersonalizados: () => jest.fn(),
                    getObrigatorioRotulosPersonalizados: () => jest.fn().mockReturnValue(true),
                    getDocumentoTodosObrigatoriosValidado: () => jest.fn().mockReturnValue(true),
                    getDocumentoNumeroTipoObrigatorioValidado: () => jest.fn().mockReturnValue(true)
                }
            }
            wrapper = shallowMount(PanelDocumentos, {
                localVue,
                directives,
                router,
                vuetify,
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $t},
            })
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
            wrapper.vm.verificaObrigatoriedadeLabelUriAnexo()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(true)
        })

        it('Deve ocultar obrigatoriedade do label uriAnexo se obrigatoriedade configuravel false ', () => {
            wrapper = defaultMount()
            wrapper.vm.documentosHeaders[4].obrigatorio = true
            wrapper.vm.verificaObrigatoriedadeLabelUriAnexo()
            expect(wrapper.vm.documentosHeaders[0].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[1].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[2].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[3].obrigatorio).toEqual(false)
            expect(wrapper.vm.documentosHeaders[4].obrigatorio).toEqual(false)
        })

        it('Deve cadastrar um Documento', async () => {
            wrapper = defaultMount()
            const verificaObrigatoriedadeLabels = jest.spyOn(wrapper.vm, 'verificaObrigatoriedadeLabels')
            wrapper.vm.tratarEventoSalvar(dado)
            await flushPromises()
            expect(wrapper.vm.docNovo).toEqual({})
            expect(wrapper.vm.adicionando).toEqual(false)
            expect(wrapper.emitted().salvarDocumento).toBeTruthy()
            expect(verificaObrigatoriedadeLabels).toHaveBeenCalled()
        })

        it('Deve atualizar um Documento', async () => {
            wrapper = defaultMount()
            const verificaObrigatoriedadeLabels = jest.spyOn(wrapper.vm, 'verificaObrigatoriedadeLabels')
            wrapper.vm.tratarEventoSalvar(dadoComId)
            await flushPromises()

            expect(wrapper.emitted().editarDocumento).toBeTruthy()
            expect(verificaObrigatoriedadeLabels).toHaveBeenCalled()
        })

        it('Deve chamar getDocumentoTodosObrigatoriosValidado se valorDataObrigatorio true ', () => {
            wrapper = defaultMount()
            wrapper.vm.valorDataObrigatorio = true
            const getDocumentoTodosObrigatoriosValidado = jest.spyOn(wrapper.vm, 'getDocumentoTodosObrigatoriosValidado')
            const getDocumentoNumeroTipoObrigatorioValidado = jest.spyOn(wrapper.vm, 'getDocumentoNumeroTipoObrigatorioValidado')
            wrapper.vm.verificarValidacaoCamposObrigatorios({})
            expect(getDocumentoTodosObrigatoriosValidado).toHaveBeenCalled()
            expect(getDocumentoNumeroTipoObrigatorioValidado).not.toHaveBeenCalled()
        })

        it('Deve chamar getDocumentoNumeroTipoObrigatorioValidado se valorDataObrigatorio false ', () => {
            wrapper = defaultMount()
            wrapper.vm.valorDataObrigatorio = false
            const getDocumentoTodosObrigatoriosValidado = jest.spyOn(wrapper.vm, 'getDocumentoTodosObrigatoriosValidado')
            const getDocumentoNumeroTipoObrigatorioValidado = jest.spyOn(wrapper.vm, 'getDocumentoNumeroTipoObrigatorioValidado')
            wrapper.vm.verificarValidacaoCamposObrigatorios({})
            expect(getDocumentoTodosObrigatoriosValidado).not.toHaveBeenCalled()
            expect(getDocumentoNumeroTipoObrigatorioValidado).toHaveBeenCalled()
        })

        it('Deve deletar um documento e retirar de editando', async () => {
            wrapper = defaultMount()
            const verificaObrigatoriedadeLabels = jest.spyOn(wrapper.vm, 'verificaObrigatoriedadeLabels')
            const retiraEditando = jest.spyOn(wrapper.vm, 'retiraEditando')
            const doc = {id:1, nome: 'doc'}
            wrapper.vm.editando = [doc]
            wrapper.vm.tratarEventoDeletarDocumento(dadoComId)
            await flushPromises()

            expect(wrapper.emitted().removerDocumento).toBeTruthy()
            expect(verificaObrigatoriedadeLabels).toHaveBeenCalled()
            expect(retiraEditando).toHaveBeenCalledWith(doc)
            expect(wrapper.vm.editando).toEqual([])
        })

        it('Deve deletar um documento e não retirar de editando', async () => {
            wrapper = defaultMount()
            const verificaObrigatoriedadeLabels = jest.spyOn(wrapper.vm, 'verificaObrigatoriedadeLabels')
            const retiraEditando = jest.spyOn(wrapper.vm, 'retiraEditando')
            const doc = {id:2, nome: 'doc'}
            wrapper.vm.editando = [doc]
            wrapper.vm.tratarEventoDeletarDocumento(dadoComId)
            await flushPromises()

            expect(wrapper.emitted().removerDocumento).toBeTruthy()
            expect(verificaObrigatoriedadeLabels).toHaveBeenCalled()
            expect(retiraEditando).not.toHaveBeenCalledWith(doc)
            expect(wrapper.vm.editando).toEqual([doc])
        })

        it('Deve deletar um documento novo', async () => {
            wrapper = defaultMount()
            const verificaObrigatoriedadeLabels = jest.spyOn(wrapper.vm, 'verificaObrigatoriedadeLabels')
            wrapper.vm.adicionando = true
            wrapper.vm.docNovo = {id:1}
            wrapper.vm.tratarEventoDeletarDocumentoNovo()
            await flushPromises()

            expect(wrapper.vm.adicionando).toEqual(false)
            expect(wrapper.emitted().resetarDocumentoNovo).toBeTruthy()
            expect(verificaObrigatoriedadeLabels).toHaveBeenCalled()
        })

        it('Deve salvar o anexo', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoSalvarAnexo(anexoRetorno)
            await flushPromises()

            expect(wrapper.emitted().uploadAnexo).toBeTruthy()
        })

        it('Deve Finalizar a edição do documento antes de criar um novo', async() => {
            wrapper = defaultMount()
            wrapper.vm.setaEditando('Numero', {id:1})
            wrapper.vm.novoDocumento()
            await flushPromises()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize a edição do documento antes de criar um novo')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })
    })
})

