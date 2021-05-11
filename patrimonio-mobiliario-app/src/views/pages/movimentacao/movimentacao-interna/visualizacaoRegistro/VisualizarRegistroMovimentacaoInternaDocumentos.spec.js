import {shallowMount} from '@vue/test-utils'
import VisualizarRegistroMovimentacaoInternaDocumentos from './VisualizarRegistroMovimentacaoInternaDocumentos'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import { actionTypes, mutationTypes } from '@/core/constants'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import {mask} from 'vue-the-mask'

describe('VisualizarRegistroMovimentacaoInternaDocumentos', () => {
    let wrapper, localVue, state, mutations, $validator, errors, actions, router,vuetify, directives, $t = jest.fn()

    directives = {
        mask: {
            ...mask,
            tokens: {
                ...mask.tokens,
                '*': /./,
            }
        },
    }

    const propsData = {
        movimentacaoInternaId: 1
    }

    const defaultMount = (stubs) => shallowMount(VisualizarRegistroMovimentacaoInternaDocumentos, {
        localVue,
        directives,
        router,
        vuetify,
        stubs,
        store: new Vuex.Store({ state, mutations, actions }),
        mocks: {$validator, errors, $t},
        propsData
    })

    const documentos = [
        {id:0, numero: '141', data: '', valor: '', tipo: 1, uriAnexo: 'notafiscal.pdf' },
        {id:1, numero: '142', data: '', valor: '', tipo: 2, uriAnexo: 'notafiscal.pdf' },
        {id:2, numero: '143', data: '', valor: '', tipo: 2, uriAnexo: 'notafiscal.pdf' },
        {id:3, numero: '144', data: '', valor: '', tipo: 3, uriAnexo: 'notafiscal.pdf' }
    ]

    const movimentacao = {
        id: 1
    }

    const tipoDocumentos = {
        items: [
            {
                id: 1, descricao:'Nota Fiscal'
            },
            {
                id:2, descricao:'Recibo'
            },
            {
                id:3, descricao:'Contrato'
            },
            {
                id:4, descricao:'Nota Fiscal'
            }
        ]
    }

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
                            name: 'Mobiliario.Movimentacoes'
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
                current: {
                    params: {
                        movimentacaoInternaId: movimentacao.id,
                    },
                },
            },
        }

        actions = {
            [actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(documentos),
            [actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(tipoDocumentos.items),
            [actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO_INTERNA]: jest.fn(),
            [actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.ATUALIZAR_DOCUMENTO_MOVIMENTACAO_INTERNA]: jest.fn(),
            [actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.DELETAR_DOCUMENTO_MOVIMENTACAO_INTERNA]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD]: jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            ['SET_UPLOADED_FILES']: jest.fn(),
        }
    })

    describe('Watch', () => {

        it('deve buscar os documentos quando trocar a movimentação', async () => {
            wrapper = defaultMount()
            await flushPromises()

            const novoMovimentacaoId = 2

            wrapper.setProps({
                movimentacaoInternaId: novoMovimentacaoId
            })
            await flushPromises()

            expect(wrapper.vm.docNovo).toEqual({})
            expect(wrapper.vm.anexoUpload).toEqual('')
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(novoMovimentacaoId)
        })

    })

    describe('Methods', () => {

        it('Deve adicionar um novo Documento', async() => {
            wrapper = defaultMount()
            wrapper.vm.docNovo = {}
            wrapper.vm.tratarEventoCriarNovoDocumento()
            await flushPromises()

            expect(wrapper.vm.docNovo.movimentacao).toEqual(propsData.movimentacaoInternaId)
        })

        it('Deve buscar todos documentos', async() => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoBuscarTodosDocumentos()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(propsData.movimentacaoInternaId)
        })

        it('Deve buscar tipos documentos', async() => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoBuscarTipoDocumentos()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
        })

        it('Deve cadastrar documento e buscar documentos', async() => {
            wrapper = defaultMount()
            const documento = {id:1}
            wrapper.vm.tratarEventoSalvarDocumento(documento)
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(documento)
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(propsData.movimentacaoInternaId)
        })

        it('Deve editar documento', async() => {
            wrapper = defaultMount()
            const documento = {id:1}
            wrapper.vm.tratarEventoEditarDocumento(documento)
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.ATUALIZAR_DOCUMENTO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.ATUALIZAR_DOCUMENTO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(documento)
        })

        it('Deve realizar upload de anexo', async() => {
            wrapper = defaultMount()
            const anexo = {uriAnexo:'uri'}
            wrapper.vm.tratarEventoUploadAnexo(anexo)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD].mock.calls[0][1]).toEqual(anexo)
        })

        it('Deve remover documento', async() => {
            wrapper = defaultMount()
            const documento = {id:1}
            wrapper.vm.tratarEventoRemoverDocumento(documento)
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.DELETAR_DOCUMENTO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.DELETAR_DOCUMENTO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(documento)
        })

        it('Deve resetar documento novo', async() => {
            wrapper = defaultMount()
            wrapper.vm.docNovo = {id:1}
            wrapper.vm.tratarEventoResetarDocumentoNovo()
            await flushPromises()

            expect(wrapper.vm.docNovo).toEqual({})
        })
    })
})

