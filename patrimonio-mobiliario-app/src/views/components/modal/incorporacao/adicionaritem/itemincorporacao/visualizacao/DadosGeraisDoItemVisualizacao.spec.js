import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import DadosGeraisDoItemVisualizacao from './DadosGeraisDoItemVisualizacao'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {mutationTypes,actionTypes} from '@/core/constants'

const  dadosGeraisDoItem = {
    id:1,
    idContaContabil:15,
    codigo:'0151425',
    descricao:'AUTOMOVEL MARCA VOLKSWAGEN, MOD.SATANA 2.0',
    marca:'Ford',
    modelo:'A356'
}


describe('DadosGeraisDoItemVisualizacao' , () => {
    let wrapper, localVue, state, mutations, actions, errors, vuetify,store,router

    beforeEach(() => {

        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()


        errors = {
            collect: jest.fn()
        }
        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'DadosGeraisDoItemVisualizacao',
                    params: {
                        incorporacaoId: 1,
                        itemIncorporacaoId: 1
                    },
                }
            }
        }


        state = {
            loki: {
                user: {
                    domainId: 1
                }
            },
            dadosDeEntrada:{}
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]: jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PATRIMONIO]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PATRIMONIO_BACKUP]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        store = new Vuex.Store({actions,mutations,state})

    })

    describe('Mounted' , () => {
        it('Deve buscar os itens gerais' , async () => {
            wrapper  = shallowMount(DadosGeraisDoItemVisualizacao, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })

            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO].mock.calls[0][1]).toEqual({idIncorporacao: 1, idItem: 1})
        })
    })

})
