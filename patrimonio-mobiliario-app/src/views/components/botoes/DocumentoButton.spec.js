import {mount} from '@vue/test-utils'
import Vuex from 'vuex'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import DocumentoButton from '@/views/components/botoes/DocumentoButton'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'
import Vue from 'vue'

describe('DocumentoButton', () => {
    let wrapper, state, localVue,store,vuetify,actions,mutations

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    const movimentacao = {
        id: 1,
        situacao: 'EM_ELABORACAO'
    }

    beforeEach(() => {

        state = {
            items: []
        }
       actions = {
           [actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_TERMO_DE_RESPONSABILDADE]:jest.fn(),
           [actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_FINALIZADA]:jest.fn(),
           [actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_EM_ELABORACAO]:jest.fn(),
           [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_TEMPORARIA_POR_ID]: jest.fn().mockReturnValue(movimentacao)
       }

       mutations = {
           [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]:jest.fn(),
           [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]:jest.fn(),
           [mutationTypes.LOKI.SET_LOADING_MESSAGE]:jest.fn()
       }

        Vue.use(Vuex)
        store = new Vuex.Store({state,mutations,actions})

        localVue = applicationTestBuilder.build()
    })

    const defaultMount = () => mount(DocumentoButton,{
        localVue,
        vuetify,
        store:new Vuex.Store({state,actions,mutations}),
        propsData: {
            movimentacaoInternaId:2,
            movimentacaoTipo:'TEMPORARIA'
        }
    })

    describe('Methods', () => {
        it('Deve  gerar um termo de Guarda e Responsabilidade', async () => {
            wrapper = defaultMount()

            wrapper.vm.tratarEventoGerarDocumento('TERMORESPONSABILIDADE')
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_TERMO_DE_RESPONSABILDADE]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_TERMO_DE_RESPONSABILDADE].mock.calls[0][1]).toEqual(2)
        })

        it('Deve gerar um memorando da Movimentação em elaboração', async () => {
            wrapper = defaultMount()

            await wrapper.vm.tratarEventoGerarDocumento('MEMORANDO')
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_EM_ELABORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_EM_ELABORACAO].mock.calls[0][1]).toEqual(2)
        })

        it('Deve gerar um memorando da Movimentação finalizada', async () => {
            actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_TEMPORARIA_POR_ID] = jest.fn().mockReturnValue({situacao: 'FINALIZADO'})

            wrapper = mount(DocumentoButton, {
                localVue,
                vuetify,
                store: new Vuex.Store({state, actions, mutations}),
                propsData: {
                    movimentacaoInternaId: 2,
                    movimentacaoTipo: 'TEMPORARIA'
                }
            })

            await wrapper.vm.tratarEventoGerarDocumento('MEMORANDO')
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_FINALIZADA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_FINALIZADA].mock.calls[0][1]).toEqual(2)
        })
    })
})
