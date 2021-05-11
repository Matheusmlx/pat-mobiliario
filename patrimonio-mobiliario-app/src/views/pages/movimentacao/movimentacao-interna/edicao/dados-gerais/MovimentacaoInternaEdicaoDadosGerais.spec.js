import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import MovimentacaoInternaEdicaoDadosGerais from './MovimentacaoInternaEdicaoDadosGerais'
import {actionTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('MovimentacaoInternaEdicaoDadosGerais', () => {

    let wrapper, store, localVue, router, actions, state

    const tipoMovimentacao = 'DISTRIBUICAO'

    beforeEach(() => {

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Movimentacoes', hasAccess: true}]
                }
            }
        }

        router = {
            routes: [],
            push: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'MovimentacaoInternaEdicaoDadosGerais',
                    params: { movimentacaoInternaId: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()

        actions = {[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(tipoMovimentacao)}

        store = new Vuex.Store({actions, state})
    })

    const defaultMount = () => shallowMount(MovimentacaoInternaEdicaoDadosGerais, {
        store,
        router,
        localVue
    })

    describe('mounted', () => {
        it('deve buscar tipo da movimentação', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(router.history.current.params.movimentacaoInternaId)
            expect(wrapper.vm.tipoMovimentacaoInterna).toEqual(tipoMovimentacao)
        })
    })

    describe('methods', () => {
        it('deve habilitar o passo 3', () => {
            wrapper = defaultMount()
            wrapper.vm.habilitaPasso3()

            expect(wrapper.emitted().habilitaPasso3).toBeTruthy()
        })

        it('deve desabilitar o passo 3', () => {
            wrapper = defaultMount()
            wrapper.vm.desabilitaPasso3()

            expect(wrapper.emitted().desabilitaPasso3).toBeTruthy()
        })
    })
})
