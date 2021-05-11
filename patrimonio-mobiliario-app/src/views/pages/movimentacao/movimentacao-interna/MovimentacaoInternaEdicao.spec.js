import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import MovimentacaoInternaEdicao from './MovimentacaoInternaEdicao'
import {mutationTypes, actionTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('MovimentacaoInternaEdicao.vue', () => {

    let wrapper, store, localVue, router, actions, mutations, state

    const passo =
        {
            titulo: 'Tipo',
            rotaNovo: 'MovimentacaoInternaNovoTipo',
            rotaEdicao: 'MovimentacaoInternaEdicaoTipo',
            rotaEdicaoVisualizacao: 'VisualizarMovimentacaoInternaNovoTipo',
            desabilitado: false,
            numero: 1,
        }

    beforeEach(() => {

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                }
            },
            movimentacaointerna: {
                rota: {
                    origem: 'MovimentacaoInternaListagem'
                },
                movimentacaoInterna: {
                    id: 30,
                    situacao: 'EM_ELABORACAO'
                }
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

        actions = {
            [actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_COMPLETA]: jest.fn(),
            [actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_VAZIA]: jest.fn(),
            [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn(),
            [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('FINALIZADO')
        }

        mutations = {
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.COMUM.SET_EXPANDIR_MENU]: jest.fn(),
            [mutationTypes.COMUM.SET_RETRAIR_MENU]: jest.fn(),
            [mutationTypes.MOVIMENTACAO_INTERNA.SET_SITUACAO_MOVIMENTACAO_INTERNA]: jest.fn()
        }

        store = new Vuex.Store({actions, mutations, state})
    })

    const defaultMount = () => shallowMount(MovimentacaoInternaEdicao, {
        store,
        router,
        localVue
    })

    describe('methods', () => {
        it('deve retrair o menu lateral quando entrar na página', () => {
            wrapper = defaultMount()
            wrapper.vm.retrairMenu()
            expect(mutations[mutationTypes.COMUM.SET_RETRAIR_MENU]).toHaveBeenCalled()
        })

        it('deve expandir o menu lateral quando sair da página', () => {
            wrapper = defaultMount()
            wrapper.vm.expandirMenu()
            expect(mutations[mutationTypes.COMUM.SET_EXPANDIR_MENU]).toHaveBeenCalled()
        })

        it('deve tratar o evento de deletar movimentação', async () => {
            router = {
                routes: [],
                push: jest.fn(),
                replace: jest.fn(),
                init: jest.fn(),
                history: {
                    current: {
                        name: 'MovimentacaoInternaEdicaoDadosGerais',
                        params: {
                            movimentacaoInternaId: 1
                        }
                    }
                }
            }

            wrapper = shallowMount(MovimentacaoInternaEdicao, {
                store,
                router,
                localVue,
            })

            await flushPromises
            await wrapper.vm.tratarEventoDeletarMovimentacaoInterna()

            await flushPromises
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_COMPLETA]).toBeCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_COMPLETA].mock.calls[0][1]).toEqual(wrapper.vm.movimentacaoInternaId)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: state.movimentacaointerna.rota.origem})
        })

        it('deve habilitar o passo 3', () => {
            wrapper = defaultMount()
            wrapper.vm.habilitaPasso3()

            expect(wrapper.vm.passos[2].desabilitado).toEqual(false)
        })

        it('deve desabilitar o passo 3', () => {
            wrapper = defaultMount()
            wrapper.vm.desabilitaPasso3()

            expect(wrapper.vm.passos[2].desabilitado).toEqual(true)
        })

        it('deve habilitar o passo 4', () => {
            wrapper = defaultMount()
            wrapper.vm.habilitaPasso4()

            expect(wrapper.vm.passos[3].desabilitado).toEqual(false)
        })

        it('deve desabilitar o passo 4', () => {
            wrapper = defaultMount()
            wrapper.vm.desabilitaPasso4()

            expect(wrapper.vm.passos[3].desabilitado).toEqual(true)
        })

        it('deve tratar o evento de click redirecionando para a rota edição MovimentacaoInternaEdicaoTipo', () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoClick(passo)

            expect(router.replace.mock.calls[0][0]).toEqual({name:'MovimentacaoInternaEdicaoTipo', params: {movimentacaoInternaId: wrapper.vm.movimentacaoInternaId}})
        })

        it('deve tratar o evento de click redirecionando para a rota atual VisualizarMovimentacaoInternaNovoTipo', () => {
            store.state.loki.user.authorities[0] = {name: 'Mobiliario.Consultor', hasAccess: true}
            wrapper = shallowMount(MovimentacaoInternaEdicao, {
                store,
                router,
                localVue
            })

            wrapper.vm.tratarEventoClick(passo)

            expect(router.replace.mock.calls[0][0]).toEqual({name:'VisualizarMovimentacaoInternaNovoTipo', params: {movimentacaoInternaId: wrapper.vm.movimentacaoInternaId}})
        })

        it('deve pegar o id da movimentacao pelo rota e setar na variável movimentacaoInternaId', () => {
            router = {
                routes: [],
                push: jest.fn(),
                replace: jest.fn(),
                init: jest.fn(),
                history: {
                    current: {
                        name: 'MovimentacaoInternaEdicaoDadosGerais',
                        params: {
                            movimentacaoInternaId: 1
                        }
                    }
                }
            }

            wrapper = shallowMount(MovimentacaoInternaEdicao, {
                store,
                router,
                localVue
            })
            wrapper.vm.setMovimentacaoInternaId()

            expect(wrapper.vm.movimentacaoInternaId).toEqual(1)
        })

        it('deve controlar o acesso dos próximos passos na rota MovimentacaoInternaNovoTipo', () => {
            wrapper = defaultMount()
            wrapper.vm.contralaAcessoAosPassosEmCadaRota()

            expect(wrapper.vm.passos[0].desabilitado).toEqual(false)
            expect(wrapper.vm.passos[1].desabilitado).toEqual(true)
            expect(wrapper.vm.passos[2].desabilitado).toEqual(true)
            expect(wrapper.vm.passos[3].desabilitado).toEqual(true)
        })

        it('deve controlar o acesso dos próximos passos na rota MovimentacaoInternaEdicaoDadosGerais', () => {
            router = {
                routes: [],
                push: jest.fn(),
                replace: jest.fn(),
                init: jest.fn(),
                history: {
                    current: {
                        name: 'MovimentacaoInternaEdicaoDadosGerais',
                        params: {
                            movimentacaoInternaId: 1
                        }
                    }
                }
            }
            wrapper = shallowMount(MovimentacaoInternaEdicao, {
                store,
                router,
                localVue
            })
            wrapper.vm.contralaAcessoAosPassosEmCadaRota()

            expect(wrapper.vm.passos[0].desabilitado).toEqual(false)
            expect(wrapper.vm.passos[1].desabilitado).toEqual(false)
            expect(wrapper.vm.passos[2].desabilitado).toEqual(true)
            expect(wrapper.vm.passos[3].desabilitado).toEqual(true)
        })

        it('deve controlar o acesso dos próximos passos na rota MovimentacaoInternaEdicaoItens', () => {
            router = {
                routes: [],
                push: jest.fn(),
                replace: jest.fn(),
                init: jest.fn(),
                history: {
                    current: {
                        name: 'MovimentacaoInternaEdicaoItens',
                        params: {
                            movimentacaoInternaId: 1
                        }
                    }
                }
            }
            wrapper = shallowMount(MovimentacaoInternaEdicao, {
                store,
                router,
                localVue
            })
            wrapper.vm.contralaAcessoAosPassosEmCadaRota()

            expect(wrapper.vm.passos[2].desabilitado).toEqual(false)
            expect(wrapper.vm.passos[3].desabilitado).toEqual(true)
        })

        it('deve controlar o acesso dos próximos passos na rota MovimentacaoInternaEdicaoDocumentos', () => {
            router = {
                routes: [],
                push: jest.fn(),
                replace: jest.fn(),
                init: jest.fn(),
                history: {
                    current: {
                        name: 'MovimentacaoInternaEdicaoDocumentos',
                        params: {
                            movimentacaoInternaId: 1
                        }
                    }
                }
            }
            wrapper = shallowMount(MovimentacaoInternaEdicao, {
                store,
                router,
                localVue
            })
            wrapper.vm.contralaAcessoAosPassosEmCadaRota()

            expect(wrapper.vm.passos[0].desabilitado).toEqual(false)
            expect(wrapper.vm.passos[1].desabilitado).toEqual(false)
            expect(wrapper.vm.passos[2].desabilitado).toEqual(false)
            expect(wrapper.vm.passos[3].desabilitado).toEqual(false)
        })

        it('deve desabilitar passo 2', () => {
            wrapper = defaultMount()
            wrapper.vm.desabilitaPasso2()

            expect(wrapper.vm.passos[1].desabilitado).toEqual(true)
        })

        it('deve habilitar passo 2', () => {
            wrapper = defaultMount()
            wrapper.vm.habilitaPasso2()

            expect(wrapper.vm.passos[1].desabilitado).toEqual(false)
        })
    })

    describe('Destroyed', () => {
        it('Deve expandir a barra lateral ao componente ser destruido', () => {
            wrapper = defaultMount()
            const removerMovimentacaoVazia = jest.spyOn(wrapper.vm, 'removerMovimentacaoVazia')
            wrapper.destroy()

            expect(mutations[mutationTypes.COMUM.SET_EXPANDIR_MENU]).toHaveBeenCalled()
            expect(removerMovimentacaoVazia).toHaveBeenCalled()
        })

        it('Deve remover a movimentação vazia ao ser destruído', () => {
            state.movimentacaointerna.movimentacaoInterna = {
                id: 1,
                situacao: 'EM_ELABORACAO'
            }

            wrapper = shallowMount(MovimentacaoInternaEdicao, {
                localVue,
                router,
                store
            })

            wrapper.vm.removerMovimentacaoVazia()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_VAZIA]).toHaveBeenCalled()
        })

        it('Não deve remover a movimentação ao ser destruído e a movimentação tiver ao menos um campo preenchido', () => {
            state.movimentacaointerna.movimentacaoInterna ={
                id: 1,
                situacao: 'EM_ELABORACAO',
                motivoObservacao: 'Teste'
            }

            wrapper = shallowMount(MovimentacaoInternaEdicao, {
                localVue,
                router,
                store
            })

            wrapper.vm.removerMovimentacaoVazia()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_VAZIA]).not.toHaveBeenCalled()
        })

        it('Não deve remover a movimentação ao ser destruído e a movimentação não estiver EM_ELABORACAO', () => {
            state.movimentacaointerna.movimentacaoInterna = {id: 1, situacao: 'FINALIZADO'}

            wrapper = shallowMount(MovimentacaoInternaEdicao, {
                localVue,
                router,
                store
            })

            wrapper.vm.removerMovimentacaoVazia()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_VAZIA]).not.toHaveBeenCalled()
        })

        it('Não deve remover a movimentação ao ser destruído quando movimentação não existir', () => {
            state.movimentacaointerna.movimentacaoInterna ={movimentacaoInterna: undefined}

            wrapper = shallowMount(MovimentacaoInternaEdicao, {
                localVue,
                router,
                store
            })

            wrapper.vm.removerMovimentacaoVazia()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_VAZIA]).not.toHaveBeenCalled()
        })

        it('Deve buscar situação da movimentação e redirecionar se FINALIZADO',
          async () => {
              router.history.current = {
                  name: 'MovimentacaoInternaEdicaoDadosGerais',
                  params: { movimentacaoInternaId: 1 },
              }
              wrapper = defaultMount()
              await flushPromises()

              expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
              expect(router.replace).toHaveBeenCalled()
              expect(router.replace.mock.calls[0][0]).toEqual({ name: 'MovimentacaoInternaListagem' })
              expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
              expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1]).toEqual({
                    message: 'Não é possível editar esta movimentação',
                    type: 'error',
                })
          })

        it('Deve buscar situação da movimentação e redirecionar se EM_PROCESSAMENTO', async () => {
            router.history.current = {
                name: 'MovimentacaoInternaEdicaoDadosGerais',
                params: { movimentacaoInternaId: 1 },
            }
            actions = {
                [actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_COMPLETA]: jest.fn(),
                [actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_VAZIA]: jest.fn(),
                [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn(),
                [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('EM_PROCESSAMENTO')
            }
            wrapper = wrapper = shallowMount(MovimentacaoInternaEdicao, {
                localVue,
                router,
                store: new Vuex.Store({actions, mutations, state})
            })
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({ name: 'MovimentacaoInternaListagem' })
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1]).toEqual({
                message: 'Não é possível editar esta movimentação',
                type: 'error'
            })
        })

        it('Deve buscar situação da movimentação e não redirecionar se EM_ELABORACAO', async () => {
            router.history.current = {
                name: 'MovimentacaoInternaEdicaoDadosGerais',
                params: { movimentacaoInternaId: 1 },
            }
            actions = {
                [actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_COMPLETA]: jest.fn(),
                [actionTypes.MOVIMENTACAO_INTERNA.REMOVER_MOVIMENTACAO_INTERNA_VAZIA]: jest.fn(),
                [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA]: jest.fn(),
                [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('EM_ELABORACAO')
            }
            wrapper = wrapper = shallowMount(MovimentacaoInternaEdicao, {
                localVue,
                router,
                store: new Vuex.Store({actions, mutations, state})
            })
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(router.replace).not.toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
        })
    })
})
