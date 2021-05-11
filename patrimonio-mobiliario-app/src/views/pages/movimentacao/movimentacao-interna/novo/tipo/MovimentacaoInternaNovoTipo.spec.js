import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import MovimentacaoInternaNovoTipo from './MovimentacaoInternaNovoTipo'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('MovimentacaoInternaNovoTipo', () => {

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
                    name: 'MovimentacaoInternaNovoTipo'
                }
            }
        }

        localVue = applicationTestBuilder.build()

        actions = {[actionTypes.MOVIMENTACAO_INTERNA.CADASTRAR_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue({id:1})
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        store = new Vuex.Store({actions, state, mutations})
    })

    const defaultMount = () => shallowMount(MovimentacaoInternaNovoTipo, {
        store,
        router,
        localVue
    })

    describe('methods', () => {
        it('deve tratar o evento continuar', () => {
            wrapper = defaultMount()
            wrapper.vm.tipoSelecionado = 'DISTRIBUICAO'
            wrapper.vm.movimentacao = {id:1}
            wrapper.vm.continuar()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
        })

        it('deve selecionar bem', async () => {
            wrapper = defaultMount()
            wrapper.vm.selecionarTipo({nome:'DISTRIBUICAO'})
            await flushPromises()

            expect(wrapper.vm.tipoSelecionado).toEqual('DISTRIBUICAO')
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.CADASTRAR_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.CADASTRAR_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual({tipo:'DISTRIBUICAO'})
            expect(wrapper.emitted().habilitaPasso2).toBeTruthy()
            expect(wrapper.emitted().habilitaPasso2[0][0]).toEqual(wrapper.vm.movimentacao.id)
        })

        it('não deve selecionar bem se não tiver permissão', async () => {
            store.state.loki.user.authorities[0] = {name: 'Mobiliario.Consultor', hasAccess: true}
            wrapper = defaultMount()
            wrapper.vm.selecionarTipo({nome:'DISTRIBUICAO'})

            expect(wrapper.vm.tipoSelecionado).toEqual(null)
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.CADASTRAR_MOVIMENTACAO_INTERNA]).not.toHaveBeenCalled()
            expect(wrapper.emitted().habilitaPasso2).not.toBeTruthy()
        })

        it('deve retornar a listagem da incorporação', async () => {
            wrapper = defaultMount()
            wrapper.vm.cancelar()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'IncorporacaoListagem'})
        })

        it('deve habilitar passo 2 passando id para a rota', async () => {
            wrapper = defaultMount()
            wrapper.vm.habilitaPasso2(1)

            expect(wrapper.emitted().habilitaPasso2).toBeTruthy()
            expect(wrapper.emitted().habilitaPasso2[0][0]).toEqual(1)
        })
    })
})
