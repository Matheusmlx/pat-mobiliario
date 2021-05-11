import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import ModalItensRegistroIncorporacaoDados from './ModalItensRegistroIncorporacaoDados'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('ModalItensRegistroIncorporacaoDados', () => {
    let wrapper, actions, localVue, vuetify, errors, router, state, mutations, $validator, $t = jest.fn()

    const itemIncorporacao = {
        id: 1,
        estadoConservacao: 1,
        contaContabil: {
            id: 1,
            codigo: '123110803',
            descricao: 'BENS MÓVEIS A REPARAR'
        }

    }

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getObrigatorioRotulosPersonalizados: () => jest.fn(),
        }
    }

    const defaultMount = (stubs) => shallowMount(ModalItensRegistroIncorporacaoDados, {
        localVue,
        router,
        vuetify,
        stubs,
        store: new Vuex.Store({
            state, mutations, actions,
            modules: {
                rotulosPersonalizados
            }
        }),
        propsData: { value: itemIncorporacao },
        mocks: {$validator, errors, $t},
    })

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        state = {
            loki: {
                user: {
                    domainId: 1
                },
                uploadedFiles: []
            },
        }

        errors = {
            collect: jest.fn()
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'VisualizarRegistroIncorporacao',
                    params: {
                        id: 1
                    }
                }
            }
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true)
            }
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(itemIncorporacao),
        }

    })

    describe('Methods', () => {

        it('Deve editar o item de incorporação', async () => {
            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.salvarFormulario()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
        })

    })
})
