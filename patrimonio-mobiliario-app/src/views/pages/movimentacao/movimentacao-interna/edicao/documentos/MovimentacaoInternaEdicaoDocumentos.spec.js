import {shallowMount} from '@vue/test-utils'
import MovimentacaoInternaEdicaoDocumentos from './MovimentacaoInternaEdicaoDocumentos'
import {mutationTypes, actionTypes} from '@/core/constants'
import Vuex from 'vuex'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('MovimentacaoInternaEdicaoDocumentos', () => {
    let wrapper, localVue, store, state, mutations, router, actions, errors, $validator, $swal

    const defaultMount = () => shallowMount(MovimentacaoInternaEdicaoDocumentos, {
        store,
        localVue,
        router,
        mocks: {
            $validator,
            errors,
            $swal
        },
    })

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        state = {
            loki: {
                user: {
                    domainId: 1,
                    domains: [],
                    type: 'INTERNO',
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'Mobiliario.Movimentacoes'
                        },
                    ]
                }
            },
            rota: {
                origem: 'Inicial'
            },
            documentos: [],
            movimentacaointerna: {
                situacaoMovimentacaoInterna: 'FINALIZADO'
            }
        }

        errors = {
            collect: jest.fn(),
        }

        $swal = () => ({
            then: jest.fn()
        })

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
                    name: 'IncorporacaoDocumentosEdicao',
                    params: {
                        movimentacaoInternaId: 1,
                    },
                },
            },
        }

        actions = {
            [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn(),
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        store = new Vuex.Store({state, mutations, actions})
    })

    describe('Mounted', () => {
        it('Deve montar MovimentacaoInternaEdicaoDocumentos', async () => {
            wrapper = defaultMount()

            await flushPromises()

            expect(wrapper.vm.movimentacaoInternaId).toBe(1)
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(state.documentos).toEqual([])
        })
    })

    describe('Computed', () => {
        it('Deve pegar situacao da movimentacao do state', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.situacaoMovimentacao).toEqual('FINALIZADO')
        })

        it('Deve retornar se situa????o da movimentacao em processamento', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.movimentacaoEmProcessamento).toEqual(false)
        })

        it('Deve retornar id da movimenta????o interna', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.movimentacaoInternaId).toEqual(1)
        })

        it('Deve retornar se permiss??o Mobiliario.Normal', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.verificaPermissaoEdicao).toEqual(true)
        })

        it('Deve retornar se permiss??o diferente de Mobiliario.Normal', () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            wrapper = defaultMount()
            expect(wrapper.vm.verificaPermissaoEdicao).toEqual(false)
        })

    })

    describe('Methods', () => {
        it('Deve voltar para a listagem de itens da movimenta????o', async () => {
            wrapper = defaultMount()

            await wrapper.vm.tratarEventoVoltar()

            expect(router.push.mock.calls[0][0]).toEqual({
                name: 'MovimentacaoInternaEdicaoItens',
                params: {movimentacaoInternaId: 1}
            })
        })

        it('Deve voltar para a listagem da incorpora????o', async () => {
            store.state.loki.user.authorities[0].name = 'Mobiliario.consulta'
            wrapper = defaultMount()

            await wrapper.vm.tratarEventoVoltar()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'IncorporacaoListagem'})
        })

        describe('Finalizar Distribui????o', () => {
            it('Deve finalizar a movimenta????o distribui????o e redirecionar para visualiza????o quando o tipo for DISTRIBUICAO', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DISTRIBUICAO'),
                    [actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO]: jest.fn().mockReturnValue({
                        id: 1,
                        situacao: 'FINALIZADO',
                        dataFinalizacao: new Date()
                    })
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()

                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO].mock.calls[0][1]).toEqual(1)
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Opera????o realizada com sucesso.')
                expect(router.replace).toHaveBeenCalled()
                expect(router.replace.mock.calls[0][0]).toEqual({
                    name: 'VisualizarRegistroMovimentacaoInterna',
                    params: {'movimentacaoInternaId': 1}
                })
            })

            it('Deve finalizar a movimenta????o distribui????o e redirecionar para listagem quando o situacao for EM_PROCESSAMENTO', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DISTRIBUICAO'),
                    [actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO]: jest.fn().mockReturnValue({
                        id: 1,
                        situacao: 'EM_PROCESSAMENTO',
                        dataFinalizacao: new Date()
                    })
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()
                const mostrarNotificacaoSucessoComConfirmacao = jest.spyOn(wrapper.vm, 'mostrarNotificacaoSucessoComConfirmacao')
                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO].mock.calls[0][1]).toEqual(1)
                expect(mostrarNotificacaoSucessoComConfirmacao).toHaveBeenCalled()
            })

            it('N??o deve redirecionar para a visualiza????o quando ocorrer erro ao finalizar a distribui????o', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DISTRIBUICAO'),
                    [actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO]: jest.fn().mockReturnValue(undefined)
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()

                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO].mock.calls[0][1]).toEqual(1)
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
                expect(router.push).not.toHaveBeenCalled()
            })

            it('Nao deve finalizar a movimenta????o distribui????o quando o tipo for diferente DISTRIBUICAO', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('OUTRO_TIPO'),
                    [actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO]: jest.fn()
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()

                wrapper.vm.tratarEventoFinalizar()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.FINALIZAR_DISTRIBUICAO]).not.toHaveBeenCalled()
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
                expect(router.push).not.toHaveBeenCalled()
            })

        })

        describe('Finalizar Movimenta????o Entre Setores', () => {
            it('Deve finalizar a movimenta????o entre setores e redirecionar para visualiza????o quando o tipo for ENTRE_SETORES', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('ENTRE_SETORES'),
                    [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES]: jest.fn().mockReturnValue({
                        id: 1,
                        situacao: 'FINALIZADO',
                        dataFinalizacao: new Date()
                    })
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()

                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES].mock.calls[0][1]).toEqual(1)
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Opera????o realizada com sucesso.')
                expect(router.replace).toHaveBeenCalled()
                expect(router.replace.mock.calls[0][0]).toEqual({
                    name: 'VisualizarRegistroMovimentacaoInterna',
                    params: {'movimentacaoInternaId': 1}
                })
            })

            it('Deve finalizar a movimenta????o entre setores e redirecionar para listagem quando o situacao for EM_PROCESSAMENTO', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('ENTRE_SETORES'),
                    [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES]: jest.fn().mockReturnValue({
                        id: 1,
                        situacao: 'EM_PROCESSAMENTO',
                        dataFinalizacao: new Date()
                    })
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()
                const mostrarNotificacaoSucessoComConfirmacao = jest.spyOn(wrapper.vm, 'mostrarNotificacaoSucessoComConfirmacao')
                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES].mock.calls[0][1]).toEqual(1)
                expect(mostrarNotificacaoSucessoComConfirmacao).toHaveBeenCalled()
            })

            it('N??o deve redirecionar para a visualiza????o quando ocorrer erro ao finalizar a movimentacao entre setores', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('ENTRE_SETORES'),
                    [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES]: jest.fn().mockReturnValue(undefined)
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()

                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.FINALIZAR_ENTRE_SETORES].mock.calls[0][1]).toEqual(1)
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
                expect(router.push).not.toHaveBeenCalled()
            })
        })

        describe('Finalizar Movimenta????o Entre Estoques', () => {
            it('Deve finalizar a movimenta????o entre estoques e redirecionar para visualiza????o quando o tipo for ENTRE_ESTOQUES', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('ENTRE_ESTOQUES'),
                    [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES]: jest.fn().mockReturnValue({
                        id: 1,
                        situacao: 'FINALIZADO',
                        dataFinalizacao: new Date()
                    })
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()

                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES].mock.calls[0][1]).toEqual(1)
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Opera????o realizada com sucesso.')
                expect(router.replace).toHaveBeenCalled()
                expect(router.replace.mock.calls[0][0]).toEqual({
                    name: 'VisualizarRegistroMovimentacaoInterna',
                    params: {'movimentacaoInternaId': 1}
                })
            })

            it('Deve finalizar a movimenta????o entre estoques e redirecionar para listagem quando o situacao for EM_PROCESSAMENTO', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('ENTRE_ESTOQUES'),
                    [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES]: jest.fn().mockReturnValue({
                        id: 1,
                        situacao: 'EM_PROCESSAMENTO',
                        dataFinalizacao: new Date()
                    })
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()
                const mostrarNotificacaoSucessoComConfirmacao = jest.spyOn(wrapper.vm, 'mostrarNotificacaoSucessoComConfirmacao')
                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES].mock.calls[0][1]).toEqual(1)
                expect(mostrarNotificacaoSucessoComConfirmacao).toHaveBeenCalled()
            })

            it('N??o deve redirecionar para a visualiza????o quando ocorrer erro ao finalizar a movimentacao entre estoques', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('ENTRE_ESTOQUES'),
                    [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES]: jest.fn().mockReturnValue(undefined)
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()

                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.FINALIZAR_ENTRE_ESTOQUES].mock.calls[0][1]).toEqual(1)
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
                expect(router.push).not.toHaveBeenCalled()
            })
        })

        describe('Finalizar Movimenta????o Devolu????o Almoxarifado', () => {
            it('Deve finalizar a movimenta????o Devolu????o Almoxarifado e redirecionar para visualiza????o quando o tipo for ENTRE_ESTOQUES', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DEVOLUCAO_ALMOXARIFADO'),
                    [actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO]: jest.fn().mockReturnValue({
                        id: 1,
                        situacao: 'FINALIZADO',
                        dataFinalizacao: new Date()
                    })
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()

                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(1)
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Opera????o realizada com sucesso.')
                expect(router.replace).toHaveBeenCalled()
                expect(router.replace.mock.calls[0][0]).toEqual({
                    name: 'VisualizarRegistroMovimentacaoInterna',
                    params: {'movimentacaoInternaId': 1}
                })
            })

            it('Deve finalizar a movimenta????o Devolu????o Almoxarifado e redirecionar para listagem quando o situacao for EM_PROCESSAMENTO', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DEVOLUCAO_ALMOXARIFADO'),
                    [actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO]: jest.fn().mockReturnValue({
                        id: 1,
                        situacao: 'EM_PROCESSAMENTO',
                        dataFinalizacao: new Date()
                    })
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()
                const mostrarNotificacaoSucessoComConfirmacao = jest.spyOn(wrapper.vm, 'mostrarNotificacaoSucessoComConfirmacao')
                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(1)
                expect(mostrarNotificacaoSucessoComConfirmacao).toHaveBeenCalled()
            })

            it('N??o deve redirecionar para a visualiza????o quando ocorrer erro ao finalizar a movimentacao Devolu????o Almoxarifado', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('DEVOLUCAO_ALMOXARIFADO'),
                    [actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO]: jest.fn().mockReturnValue(undefined)
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()

                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.FINALIZAR_DEVOLUCAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(1)
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
                expect(router.push).not.toHaveBeenCalled()
            })
        })

        describe('Enviar Movimenta????o Tempor??ria', () => {
            it('Deve enviar a movimenta????o Tempor??ria e redirecionar para visualiza????o quando o tipo for TEMPORARIA', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('TEMPORARIA'),
                    [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA]: jest.fn().mockReturnValue({
                        id: 1,
                        situacao: 'FINALIZADO',
                        dataFinalizacao: new Date()
                    })
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()

                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA].mock.calls[0][1]).toEqual(1)
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Opera????o realizada com sucesso.')
                expect(router.replace).toHaveBeenCalled()
                expect(router.replace.mock.calls[0][0]).toEqual({
                    name: 'VisualizarRegistroMovimentacaoInterna',
                    params: {'movimentacaoInternaId': 1}
                })
            })

            it('Deve enviar a movimenta????o Tempor??ria e redirecionar para listagem quando o situacao for EM_PROCESSAMENTO', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('TEMPORARIA'),
                    [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA]: jest.fn().mockReturnValue({
                        id: 1,
                        situacao: 'EM_PROCESSAMENTO',
                        dataFinalizacao: new Date()
                    })
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()
                const mostrarNotificacaoSucessoComConfirmacao = jest.spyOn(wrapper.vm, 'mostrarNotificacaoSucessoComConfirmacao')
                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA].mock.calls[0][1]).toEqual(1)
                expect(mostrarNotificacaoSucessoComConfirmacao).toHaveBeenCalled()
            })

            it('N??o deve redirecionar para a visualiza????o quando ocorrer erro ao enviar a movimentacao Tempor??ria', async () => {
                actions = {
                    ...actions,
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('TEMPORARIA'),
                    [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA]: jest.fn().mockReturnValue(undefined)
                }

                store = new Vuex.Store({state, mutations, actions})

                wrapper = defaultMount()
                await flushPromises()

                wrapper.vm.tratarEventoFinalizar()
                await flushPromises()

                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ENVIAR_TEMPORARIA].mock.calls[0][1]).toEqual(1)
                expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
                expect(router.push).not.toHaveBeenCalled()
            })
        })
    })
})
