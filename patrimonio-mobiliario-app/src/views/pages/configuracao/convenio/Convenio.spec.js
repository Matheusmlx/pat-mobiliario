import {mount} from '@vue/test-utils'
import Vuex from 'vuex'
import Convenio from './Convenio'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('Convenio', () => {
    let wrapper, actions, localVue, router, store, state, mutations

    const resumoConvenio = {
        situacao: 'ATIVO'
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        state = {
            loki: {
                user: {
                    domainId: 1
                }
            },
            convenio: {
                resumoConvenio: {},
                rota: {
                    origem: 'ConvenioListagem'
                }
            },
        }

        actions = {
            [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_RESUMO_DO_CONVENIO]: jest.fn().mockReturnValue(resumoConvenio)
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.COMUM.SET_EXPANDIR_MENU]: jest.fn(),
            [mutationTypes.COMUM.SET_RETRAIR_MENU]: jest.fn()
        }

        store = new Vuex.Store({state, mutations})

        router = {
            routes: [],
            push: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'Convenio',
                    params: {
                        id: 8
                    }
                }
            }
        }
    })


    describe('mounted', () => {
        it('Deve retrair o menu lateral quando entrar na página', () => {
            wrapper = mount(Convenio, {
                render: null,
                store,
                router,
                localVue,
            })

            expect(mutations[mutationTypes.COMUM.SET_RETRAIR_MENU]).toHaveBeenCalled()
        })
    })

    describe('Destroyed', () => {
        it('Deve voltar a expandir o menu lateral quando sair da página', () => {
            wrapper = mount(Convenio, {
                render: null,
                store,
                router,
                localVue,
                destroyed() {
                }
            }).destroy()

            expect(mutations[mutationTypes.COMUM.SET_EXPANDIR_MENU]).toHaveBeenCalled()
        })
    })
})
