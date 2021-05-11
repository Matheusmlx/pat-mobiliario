import {shallowMount} from '@vue/test-utils'
import Documentos from './Documentos'
import {mutationTypes, actionTypes} from '@/core/constants'
import Vuex from 'vuex'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

jest.mock('vue-sweetalert2')

describe('Documentos', () => {
    let wrapper, localVue, store, state, mutations, router, actions, errors, $validator, $swal

    const documentos = [
        {
            id: 1,
            numero: '2342-2',
            data: null,
            valor: 200.00,
            tipo: 1,
            uriAnexo: 'testedelete.jpg',
            incorporacao: 7
        },
        {
            id: 2,
            numero: '3243-1',
            data: new Date('2020-10-18T12:00:00'),
            valor: 170.50,
            tipo: 2,
            uriAnexo: 'anjocaido.jpg',
            incorporacao: 7
        },
        {
            id: 3,
            numero: '5221-2',
            data: new Date('2020-10-13T12:00:00'),
            valor: 150.30,
            tipo: 3,
            uriAnexo: 'teste.jpg',
            incorporacao: 7
        },
        {
            id: 4,
            numero: '1354-1',
            data: new Date('2020-10-12T12:00:00'),
            valor: 120.15,
            tipo: 2,
            uriAnexo: 'ave.jpg',
            incorporacao: 7
        },
    ]

    const tipoDocumentos = [
        {id: 1, descricao: 'Nota Fiscal', permiteAnexo: false, identificacaoDocumento: '001'},
        {id: 2, descricao: 'Nota de Empenho', permiteAnexo: true, identificacaoDocumento: '002'},
        {id: 3, descricao: 'Produto', permiteAnexo: true, identificacaoDocumento: '003'},
        {id: 4, descricao: 'Nota Fiscal', permiteAnexo: true, identificacaoDocumento: '003'}
    ]

    const anexoRetorno = {uriAnexo: 'anexo', numero: 1234, tipo: 3}

    const documentoData = {
        numero: 141,
        data: ' ',
        incorporacao: 48,
        valor: '0',
        tipo: 'Nota Fiscal',
        uriAnexo: 'notafiscal.pdf'
    }

    const documentoEdit = {
        id: 1,
        numero: 141,
        data: ' ',
        incorporacao: 48,
        valor: '0',
        tipo: 'Nota Fiscal',
        uriAnexo: 'notafiscal.pdf'
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        state = {
            documentos: {},
            items: [],
            loki: {
                user: {
                    domainId: 1,
                    domains: [],
                    type: 'INTERNO',
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'Mobiliario.Normal'
                        },
                    ]
                }
            },
            documentoVazio: true,
            showDesfazer: false,
            rota: {
                origem: 'Inicial'
            },
            documento: {
                documentos: [{}],
                documentoBackup: {}
            },
            incorporacao: {
                situacaoIncorporacao: {}
            }
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

        $swal = () => ({
            then: jest.fn()
        })

        router = {
            init: jest.fn(),
            push: jest.fn(),
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

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.CADASTRAR_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_DOWNLOAD]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD]: jest.fn().mockReturnValue(anexoRetorno),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DELETAR_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]: jest.fn().mockReturnValue(tipoDocumentos),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]: jest.fn().mockReturnValue(documentos)
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.REMOVER_DOCUMENTOS]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
        }

        store = new Vuex.Store({state, mutations, actions})
    })

    const defaultMount = () => shallowMount(Documentos, {
        store,
        localVue,
        router,
        mocks: {$validator, errors, $swal},
        propsData: {
            actions: {
                cadastrarDocumento: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.CADASTRAR_DOCUMENTO,
                atualizarDocumento: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO,
                enviarDocumento: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD,
                deletarDocumento: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DELETAR_DOCUMENTO,
                buscarTipoDocumento: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO,
                buscarDocumentos: actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS,
            },
            mutations: {
                removerDocumentos: mutationTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.REMOVER_DOCUMENTOS
            },
            permissaoEdicao: true,
            donoDocumento: 'incorporacao',
            donoDocumentoId: 1
        }
    })

    describe('Mounted', () => {
        it('Deve montar Documentos', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.incorporacaoId)
        })
    })

    describe('Methods', () => {
        it('Deve buscar os Documentos', async () => {
            wrapper = defaultMount()
            wrapper.vm.buscarDocumentosDono()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        })

        it('Deve salvar o anexo', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.tratarEventoSalvarUri(anexoRetorno, 0)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD]).toHaveBeenCalled()
        })

        it('Deve cadastrar um Documento', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoSalvar(documentoData)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.CADASTRAR_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        })

        it('Deve atualizar um Documento', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoSalvar(documentoEdit)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        })

        it('Deve excluir um Documento sem id', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoRemoverDocumento(documentoData)
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.REMOVER_DOCUMENTOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        })
        it('Deve excluir um Documento com id', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoRemoverDocumento(documentoEdit)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DELETAR_DOCUMENTO].mock.calls[0][1]).toEqual(documentoEdit)
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        })

        it('Deve Finalizar a edição do documento antes de criar um novo', () => {
            wrapper = defaultMount()
            wrapper.vm.documentoVazio = false
            wrapper.vm.novoDocumento()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize o cadastro para cadastrar novos documentos')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

        it('Deve Validar cadastro finalizado', () => {
            wrapper = defaultMount()
            wrapper.vm.documentoVazio = false

            expect(wrapper.vm.validarCadastrosFinalizados()).toEqual(false)
        })

        it('Deve criar um novo documento', async () => {
            wrapper = defaultMount()
            wrapper.vm.documentoVazio = true
            wrapper.vm.novoDocumento()

            expect(wrapper.vm.documentos[0].incorporacao).toEqual(1)
        })

        it('Não deve criar um novo documento', async () => {
            wrapper = defaultMount()
            wrapper.vm.documentoVazio = false
            wrapper.vm.novoDocumento()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Finalize o cadastro para cadastrar novos documentos')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

        it('Deve emitir mensagem de quantidade de cadastro de documentos excedida', async () => {
            wrapper = defaultMount()
            wrapper.vm.documentoVazio = true
            wrapper.vm.documentos = Array(30).fill({id: 1})
            wrapper.vm.novoDocumento()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('A quantidade máxima de documentos cadastrados foi atingida.')
        })
    })
})
