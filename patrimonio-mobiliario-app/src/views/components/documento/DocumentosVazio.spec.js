import {shallowMount} from '@vue/test-utils'
import DocumentosVazio from './DocumentosVazio'
import {mutationTypes, actionTypes} from '@/core/constants'
import Vuex from 'vuex'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

jest.mock('vue-sweetalert2')

describe('DocumentosVazio', () => {
    let wrapper, localVue, store, state, mutations, router, actions

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
            }
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'IncorporacaoDocumentosVazioEdicao',
                    params: {
                        incorporacaoId: 1,
                    },
                },
            },
        }

        store = new Vuex.Store({state})
    })

    const defaultMount = () => shallowMount(DocumentosVazio, {
        store,
        localVue,
        router
    })

    describe('Computed', () => {
        it('Deve retornar se permissão Mobiliario.Normal', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.verificaPermissaoEdicao).toEqual(true)
        })

        it('Deve retornar se permissão diferente de Mobiliario.Normal', () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            wrapper = defaultMount()
            expect(wrapper.vm.verificaPermissaoEdicao).toEqual(false)
        })
    })

    describe('Methods', () => {
        it('Deve emitir evento para criar novo documento', () => {
            wrapper = defaultMount()
            wrapper.vm.novoDocumento()

            expect(wrapper.emitted().novoDocumento).toBeTruthy()
        })
    })
})
