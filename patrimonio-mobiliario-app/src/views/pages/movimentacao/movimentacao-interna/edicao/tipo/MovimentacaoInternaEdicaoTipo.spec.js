import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import MovimentacaoInternaEdicaoTipo from './MovimentacaoInternaEdicaoTipo'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('MovimentacaoInternaEdicaoTipo', () => {

    let wrapper, store, localVue, router, actions, state, mutations

    beforeEach(() => {

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Movimentacoes', hasAccess: true}]
                }
            },
            movimentacaointerna:{
                tiposMovimentacaoLinha1:[],
                tiposMovimentacaoLinha2:[]
            }
        }

        router = {
            routes: [],
            push: jest.fn(),
            replace: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'MovimentacaoInternaEdicaoTipo',
                    params: {movimentacaoInternaId: 1 }
                }
            }
        }

        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.MOVIMENTACAO_INTERNA.EDITAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue({id:2, tipo: 'DEFINITIVA'}),
            [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DISTRIBUICAO')
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        store = new Vuex.Store({actions, state, mutations})
    })

    const defaultMount = () => wrapper = shallowMount(MovimentacaoInternaEdicaoTipo, {
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
            expect(wrapper.emitted().habilitaPasso2).toBeTruthy()
        })
    })


    describe('methods', () => {
        it('deve tratar o evento continuar', () => {
            wrapper = defaultMount()
            wrapper.vm.tipoSelecionado = 'DISTRIBUICAO'
            wrapper.vm.continuar()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
        })

        it('deve selecionar o novo tipo da movimentação', async () => {
            wrapper = defaultMount()
            wrapper.vm.selecionarTipo({nome:'DISTRIBUICAO'})
            await flushPromises()

            expect(wrapper.vm.tipoSelecionado).toEqual('DISTRIBUICAO')
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.EDITAR_TIPO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.EDITAR_TIPO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual({id: router.history.current.params.movimentacaoInternaId,tipo:'DISTRIBUICAO'})
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoTipo', params: {movimentacaoInternaId: 2}})
            expect(wrapper.emitted().habilitaPasso2).toBeTruthy()
            expect(wrapper.emitted().desabilitaPasso3).toBeTruthy()
            expect(wrapper.emitted().desabilitaPasso4).toBeTruthy()
        })

        it('não deve selecionar bem se não tiver permissão', async () => {
            store.state.loki.user.authorities[0] = {name: 'Mobiliario.Consultor', hasAccess: true}
            wrapper = defaultMount()
            wrapper.vm.selecionarTipo({nome:'DISTRIBUICAO'})

            expect(wrapper.vm.tipoSelecionado).toEqual(null)
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.EDITAR_TIPO_MOVIMENTACAO_INTERNA]).not.toHaveBeenCalled()
            expect(wrapper.emitted().habilitaPasso2).not.toBeTruthy()
        })

        it('deve retornar a listagem da incorporação', async () => {
            wrapper = defaultMount()
            wrapper.vm.cancelar()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'IncorporacaoListagem'})
        })

        it('deve habilitar passo 2', async () => {
            wrapper = defaultMount()
            wrapper.vm.habilitaPasso2()

            expect(wrapper.emitted().habilitaPasso2).toBeTruthy()
        })

        it('deve desabilitar passo 3', async () => {
            wrapper = defaultMount()
            wrapper.vm.desabilitaPasso3()

            expect(wrapper.emitted().desabilitaPasso3).toBeTruthy()
        })

        it('deve desabilitar passo 4', async () => {
            wrapper = defaultMount()
            wrapper.vm.desabilitaPasso4()

            expect(wrapper.emitted().desabilitaPasso4).toBeTruthy()
        })
    })
})
