import {shallowMount} from '@vue/test-utils'
import VisualizarRegistroIncorporacaoDocumentosVisualizar from './VisualizarRegistroIncorporacaoDocumentosVisualizar'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import { actionTypes } from '@/core/constants'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import {mask} from 'vue-the-mask'

describe('VisualizarRegistroIncorporacaoDocumentosVisualizar', () => {
    let wrapper, localVue, store, state, $validator, errors, actions, router,vuetify, directives, $t = jest.fn()

    directives = {
        mask: {
            ...mask,
            tokens: {
                ...mask.tokens,
                '*': /./,
            }
        },
    }

    const defaultMount = (stubs) => shallowMount(VisualizarRegistroIncorporacaoDocumentosVisualizar, {
        localVue,
        directives,
        router,
        vuetify,
        stubs,
        store: new Vuex.Store({
            state, actions,
            modules: {
                rotulosPersonalizados
            }
        }),
        mocks: {$validator, errors, $t},
    })

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
        }
    }

    const documentos = [
        {id:0, numero: '141', data: '', valor: '', tipo: 1, uriAnexo: 'notafiscal.pdf' },
        {id:1, numero: '142', data: '', valor: '', tipo: 2, uriAnexo: 'notafiscal.pdf' },
        {id:2, numero: '143', data: '', valor: '', tipo: 2, uriAnexo: 'notafiscal.pdf' },
        {id:3, numero: '144', data: '', valor: '', tipo: 3, uriAnexo: 'notafiscal.pdf' }
    ]

    const incorporacao = {
        id: 1,
        nome: 'teste',
        descricao: null,
        situacao: null,
        reconhecimento: null,
        dataAquisicao: null,
        dataNL: null,
        numeroNL: null,
        fornecedor: null,
        dataVencimento: null,
        tipo: 'DIREITOS_AUTORAIS',
        estado: null,
        orgao: 3,
        setor: 4,
    }

    const tipoDocumentos = {
        items:[
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
                id:4,descricao:'Nota de Lançamento'
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
                current: {
                    name: 'MenuDetalhe',
                    params: {
                        incorporacaoId: incorporacao.id,
                    },
                },
            },
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]: jest.fn().mockReturnValue(documentos),
            [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]: jest.fn().mockReturnValue(tipoDocumentos.items),
        }

        store = new Vuex.Store({ state, actions })
    })

    describe('Mounted', () => {
        it('Deve montar VisualizarRegistroIncorporacaoVisualizar', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.incorporacaoId)
        })
    })

})

