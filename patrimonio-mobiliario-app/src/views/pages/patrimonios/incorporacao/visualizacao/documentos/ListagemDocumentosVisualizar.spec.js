import {shallowMount} from '@vue/test-utils'
import ListagemDocumentosVisualizar from './ListagemDocumentosVisualizar'
import {actionTypes} from '@/core/constants'
import Vuex from 'vuex'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('ListagemDocumentosVisualizar', () => {
    let wrapper, localVue, store, state, router, actions, errors, $validator

    const documentos = [
        {
            id: 1,
            numero: '2342-2',
            data: null,
            valor: 200.00,
            idTipoDocumento: 1,
            anexo: 'testedelete.jpg',
            idIncorporacao: 7
        },
        {
            id: 2,
            numero: '3243-1',
            data: new Date('2020-10-18T12:00:00'),
            valor: 170.50,
            idTipoDocumento: 2,
            anexo: 'anjocaido.jpg',
            idIncorporacao: 7
        },
        {
            id: 3,
            numero: '5221-2',
            data: new Date('2020-10-13T12:00:00'),
            valor: 150.30,
            idTipoDocumento: 3,
            anexo: 'teste.jpg',
            idIncorporacao: 7
        },
        {
            id: 4,
            numero: '1354-1',
            data: new Date('2020-10-12T12:00:00'),
            valor: 120.15,
            idTipoDocumento: 2,
            anexo: 'ave.jpg',
            idIncorporacao: 7
        },
    ]

    const tipoDocumentos = [
            {id: 1, descricao: 'Nota Fiscal', permiteAnexo: false, identificacaoDocumento: '001'},
            {id: 2, descricao: 'Contrato', permiteAnexo: true, identificacaoDocumento: '002'},
            {id: 3, descricao: 'Nota Fiscal', permiteAnexo: true, identificacaoDocumento: '003'}
        ]

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
                            name: 'Mobiliario.Consulta'
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

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_DOWNLOAD]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]: jest.fn().mockReturnValue(tipoDocumentos),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]: jest.fn().mockReturnValue(documentos),
        }

        store = new Vuex.Store({state, actions})


    })

    describe('Mounted', () => {

        it('Deve montar listagemDocumentos', async () => {
            wrapper = shallowMount(ListagemDocumentosVisualizar, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors
                },
            })
            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.incorporacaoId)

        })
    })

    describe('Methods', () => {

        it('Deve baixar o anexo', async () => {
            wrapper = shallowMount(ListagemDocumentosVisualizar, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors
                },
            })
            wrapper.vm.baixarAnexo('')
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_DOWNLOAD]).toHaveBeenCalled()
        })

        it('Deve redirecionar para VisualizarItensIncorporacao', async () => {
            wrapper = shallowMount(ListagemDocumentosVisualizar, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors
                },
            })
            wrapper.vm.tratarEventoVoltar()

            expect(router.push.mock.calls[0][0]).toEqual({
                name: 'VisualizarItensIncorporacao',
                params: {incorporacaoId: 1}
            })
        })

        it('Deve redirecionar para ItensIncorporacaoListagem', async () => {
            state.loki.user.authorities = [{
                hasAccess: true,
                name: 'Mobiliario.Normal'
            }]
            wrapper = shallowMount(ListagemDocumentosVisualizar, {
                store,
                localVue,
                router,
                mocks: {
                    $validator,
                    errors
                },
            })

            wrapper.vm.tratarEventoVoltar()

            expect(router.push.mock.calls[0][0]).toEqual({
                name: 'ItensIncorporacaoListagem',
                params: {incorporacaoId: 1}
            })
        })

    })
})
