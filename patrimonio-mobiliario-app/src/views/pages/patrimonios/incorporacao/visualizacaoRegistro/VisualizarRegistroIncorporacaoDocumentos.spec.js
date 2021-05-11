import {shallowMount} from '@vue/test-utils'
import VisualizarRegistroIncorporacaoDocumentos from './VisualizarRegistroIncorporacaoDocumentos'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import {mask} from 'vue-the-mask'

describe('VisualizarRegistroIncorporacaoDocumentos', () => {
    let wrapper, localVue, state, store, mutations, $validator, errors, actions, vuetify, directives, $t = jest.fn()

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
        incorporacaoId: 1
    }

    const defaultMount = (stubs) => shallowMount(VisualizarRegistroIncorporacaoDocumentos, {
        localVue,
        store,
        directives,
        vuetify,
        stubs,
        mocks: {$validator, errors, $t},
        propsData
    })

    const documentos = [
        {id: 0, numero: '141', data: '', valor: '', tipo: 1, uriAnexo: 'notafiscal.pdf'},
        {id: 1, numero: '142', data: '', valor: '', tipo: 2, uriAnexo: 'notafiscal.pdf'},
        {id: 2, numero: '143', data: '', valor: '', tipo: 2, uriAnexo: 'notafiscal.pdf'},
        {id: 3, numero: '144', data: '', valor: '', tipo: 3, uriAnexo: 'notafiscal.pdf'}
    ]

    const tipoDocumentos = {
        items: [
            {
                id: 1, descricao: 'Nota Fiscal'
            },
            {
                id: 2, descricao: 'Recibo'
            },
            {
                id: 3, descricao: 'Contrato'
            },
            {
                id: 4, descricao: 'Nota de Empenho'
            },
            {
                id: 5, descricao: 'Nota Fiscal'
            },
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

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]: jest.fn().mockReturnValue(documentos),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DELETAR_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]: jest.fn().mockReturnValue(tipoDocumentos.items),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.CADASTRAR_DOCUMENTO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD]: jest.fn()
        }
        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            ['SET_UPLOADED_FILES']: jest.fn(),
        }

        store = new Vuex.Store({mutations, state, actions})
    })

    it('Deve adicionar um novo Documento', async () => {
        wrapper = defaultMount()
        wrapper.vm.docNovo = {}
        wrapper.vm.tratarEventoCriarNovoDocumento()
        await flushPromises()

        expect(wrapper.vm.docNovo.incorporacao).toEqual(propsData.incorporacaoId)
    })

    it('Deve buscar todos documentos', async () => {
        wrapper = defaultMount()
        wrapper.vm.tratarEventoBuscarTodosDocumentos()
        await flushPromises()

        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS].mock.calls[0][1]).toEqual(propsData.incorporacaoId)
    })

    it('Deve buscar tipos documentos', async () => {
        wrapper = defaultMount()
        wrapper.vm.tratarEventoBuscarTipoDocumentos()
        await flushPromises()

        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]).toHaveBeenCalled()
    })

    it('Deve cadastrar documento e buscar documentos', async () => {
        wrapper = defaultMount()
        const documento = {id: 1}
        wrapper.vm.tratarEventoSalvarDocumento(documento)
        await flushPromises()

        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.CADASTRAR_DOCUMENTO]).toHaveBeenCalled()
        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.CADASTRAR_DOCUMENTO].mock.calls[0][1]).toEqual(documento)
        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS].mock.calls[0][1]).toEqual(propsData.incorporacaoId)
    })

    it('Deve editar documento', async () => {
        wrapper = defaultMount()
        const documento = {id: 1}
        wrapper.vm.tratarEventoEditarDocumento(documento)
        await flushPromises()

        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO]).toHaveBeenCalled()
        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO].mock.calls[0][1]).toEqual(documento)
    })

    it('Deve realizar upload de anexo', async () => {
        wrapper = defaultMount()
        const anexo = {uriAnexo: 'uri'}
        wrapper.vm.tratarEventoUploadAnexo(anexo)
        await flushPromises()

        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD]).toHaveBeenCalled()
        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD].mock.calls[0][1]).toEqual(anexo)
    })

    it('Deve remover documento', async () => {
        wrapper = defaultMount()
        const documento = {id: 1}
        wrapper.vm.tratarEventoRemoverDocumento(documento)
        await flushPromises()

        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DELETAR_DOCUMENTO]).toHaveBeenCalled()
        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DELETAR_DOCUMENTO].mock.calls[0][1]).toEqual(documento)
    })

    it('Deve resetar documento novo', async () => {
        wrapper = defaultMount()
        wrapper.vm.docNovo = {id: 1}
        wrapper.vm.tratarEventoResetarDocumentoNovo()
        await flushPromises()

        expect(wrapper.vm.docNovo).toEqual({})
    })

    it('Deve atualizar a listagem de documentos quando trocar a incorporação', async () => {
        wrapper = defaultMount()
        await flushPromises()

        const novoIncorporacaoId = 2
        wrapper.setProps({
            incorporacaoId: novoIncorporacaoId
        })
        await flushPromises()

        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
        expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS].mock.calls[0][1]).toEqual(novoIncorporacaoId)
        expect(wrapper.vm.docNovo).toEqual({})
        expect(wrapper.vm.anexoUpload).toEqual('')
    })
})

