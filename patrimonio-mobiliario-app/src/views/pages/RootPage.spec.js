import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import RootPage from './RootPage'
import flushPromises from 'flush-promises'
import {mutationTypes, actionTypes} from '@/core/constants'

describe('RootPage', () => {
    let wrapper, localVue, vuetify, state, mutations, actions, store, router

    const quantidadeNotificacoesNaoVisualizadas = 6

    const retornoBuscaNotificacoes = {
        mostraVerMais: true,
        quantidadeNaoVisualizadasAtual: 2
    }

    const rotulosPersonalizadosModule = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
            getObrigatorioRotulosPersonalizados: () => jest.fn()
        }
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'IncorporacaoDocumentosEdicao',
                    params: {
                        incorporacaoId: 1,
                    },
                    meta: {
                        authorities: ['Mobiliario.Normal']
                    }
                },
            }
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                }
            },
            notificacao: {
                page: 1,
                items: [],
            }
        }

        mutations = {
            [mutationTypes.NOTIFICACAO.ACRESCENTA_PAGE_NOTIFICACAO]: jest.fn(),
            [mutationTypes.NOTIFICACAO.RESETA_PAGE_NOTIFICACAO]: jest.fn(),
            [mutationTypes.NOTIFICACAO.RESETA_NOTIFICACAO_ITENS]: jest.fn(),
            [mutationTypes.COMUM.SET_RETRAIR_MENU]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        actions = {
            [actionTypes.NOTIFICACAO.BUSCAR_NOTIFICACOES]: jest.fn().mockReturnValue(retornoBuscaNotificacoes),
            [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]: jest.fn().mockReturnValue('FINALIZADO'),
            [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('FINALIZADO'),
            [actionTypes.NOTIFICACAO.BUSCAR_QUANTIDADE_NOTIFICACOES_NAO_VISUALIZADAS]: jest.fn().mockReturnValue(quantidadeNotificacoesNaoVisualizadas),
            [actionTypes.CONFIGURACAO.ROTULOS_PERSONALIZADOS.GET_ALL_ROTULOS_PERSONALIZADOS]: jest.fn()
        }

        store = new Vuex.Store({
            state,
            mutations,
            actions,
            modules: {rotulosPersonalizados: rotulosPersonalizadosModule}
        })
    })

    const defaultMount = () => shallowMount(RootPage, {
        localVue,
        vuetify,
        store,
        router
    })

    describe('Mounted', () => {
        it('Deve Montar RootPage', async () => {
            wrapper = await defaultMount()
            await flushPromises()

            expect(rotulosPersonalizadosModule.actions[actionTypes.CONFIGURACAO.ROTULOS_PERSONALIZADOS.GET_ALL_ROTULOS_PERSONALIZADOS]).toHaveBeenCalled()
            expect(actions[actionTypes.NOTIFICACAO.BUSCAR_QUANTIDADE_NOTIFICACOES_NAO_VISUALIZADAS]).toHaveBeenCalled()
            expect(wrapper.vm.quantidadeNaoVisualizadasAtual).toEqual(quantidadeNotificacoesNaoVisualizadas)
        })
    })

    describe('Computed', () => {
        it('Deve retornar que usuário tem permissão de acesso', async () => {
            wrapper = await defaultMount()
            await flushPromises()

            expect(wrapper.vm.retornaPermissao).toEqual(true)
        })

        it('Deve retornar que usuário não tem permissão de acesso', async () => {
            router.history.current.meta.authorities = ['Mobiliario.Consulta']
            wrapper = await defaultMount()
            await flushPromises()

            expect(wrapper.vm.retornaPermissao).toEqual(false)
        })
    })



    describe('Methods', () => {
        it('Deve redirecionar se usuário não possuir permissão de acesso', async () => {
            router.history.current.meta.authorities = ['Mobiliario.Consulta']
            wrapper = await defaultMount()
            await flushPromises()
            wrapper.vm.redirecionaSeAcessoNegado()

            expect(wrapper.vm.retornaPermissao).toEqual(false)
            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name:'AcessoNegado'})
        })

        it('Não deve redirecionar se usuário possuir permissão de acesso', async () => {
            wrapper = await defaultMount()
            await flushPromises()
            wrapper.vm.redirecionaSeAcessoNegado()

            expect(wrapper.vm.retornaPermissao).toEqual(true)
            expect(router.push).not.toHaveBeenCalled()
        })

        it('Deve setar notificacaoFechada como false ao abrir notificação', async () => {
            wrapper = await defaultMount()
            await flushPromises()
            wrapper.vm.tratarEventoAbrirNotificacoes()

            expect(wrapper.vm.notificacaoFechada).toEqual(false)
        })

        it('Deve setar notificacaoFechada como true, resetar paginação e itens da notificação ao fechar notificação', async () => {
            wrapper = await defaultMount()
            await flushPromises()
            wrapper.vm.tratarEventoFecharNotificacoes()

            expect(wrapper.vm.notificacaoFechada).toEqual(true)
            expect(mutations[mutationTypes.NOTIFICACAO.RESETA_PAGE_NOTIFICACAO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.NOTIFICACAO.RESETA_NOTIFICACAO_ITENS]).toHaveBeenCalled()
        })

        it('Deve acrescentar page e buscar notificações ', async () => {
            wrapper = await defaultMount()
            await flushPromises()
            await wrapper.vm.tratarEventoPaginarNotificacoes()
            await flushPromises()

            expect(mutations[mutationTypes.NOTIFICACAO.ACRESCENTA_PAGE_NOTIFICACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.NOTIFICACAO.BUSCAR_NOTIFICACOES]).toHaveBeenCalled()
            expect(wrapper.vm.mostraVerMais).toEqual(retornoBuscaNotificacoes.mostraVerMais)
            expect(wrapper.vm.quantidadeNaoVisualizadasAtual).toEqual(retornoBuscaNotificacoes.quantidadeNaoVisualizadasAtual)
        })

        it('Deve atualizar quantidade de notificações não visualizadas', async () => {
            wrapper = await defaultMount()
            await flushPromises()
            await wrapper.vm.tratarEventoAtualizarNotificacoes()
            await flushPromises()

            expect(actions[actionTypes.NOTIFICACAO.BUSCAR_QUANTIDADE_NOTIFICACOES_NAO_VISUALIZADAS]).toHaveBeenCalled()
            expect(wrapper.vm.quantidadeNaoVisualizadasAtual).toEqual(quantidadeNotificacoesNaoVisualizadas)
        })

        it('Deve buscar notificacoes', async () => {
            wrapper = await defaultMount()
            await flushPromises()
            wrapper.vm.notificacaoFechada = true
            await wrapper.vm.tratarEventoNotificacoesLidas()
            await flushPromises()

            expect(actions[actionTypes.NOTIFICACAO.BUSCAR_NOTIFICACOES]).toHaveBeenCalled()
            expect(wrapper.vm.mostraVerMais).toEqual(retornoBuscaNotificacoes.mostraVerMais)
            expect(wrapper.vm.quantidadeNaoVisualizadasAtual).toEqual(retornoBuscaNotificacoes.quantidadeNaoVisualizadasAtual)
        })

        it('Não deve buscar notificacoes', async () => {
            wrapper = await defaultMount()
            await flushPromises()
            wrapper.vm.notificacaoFechada = false
            await wrapper.vm.tratarEventoNotificacoesLidas()
            await flushPromises()

            expect(actions[actionTypes.NOTIFICACAO.BUSCAR_NOTIFICACOES]).not.toHaveBeenCalled()
        })

        it('Deve redirecionar para a visualização de registro da incorporação ao acessar notificação incorporação finalizada', async () => {
            wrapper = await defaultMount()
            await flushPromises()
            const item = {
                origem: 'INCORPORACAO',
                origemId: 2
            }
            await wrapper.vm.tratarEventoAcessarNotificacao(item)

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]).not.toHaveBeenCalled()
            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({ name: 'VisualizarRegistroIncorporacao', params: { incorporacaoId: item.origemId } })
        })

        it('Deve redirecionar para a visualização de registro da movimentação interna ao acessar notificação distribuição finalizada', async () => {
            wrapper = await defaultMount()
            await flushPromises()
            const item = {
                origem: 'DISTRIBUICAO',
                origemId: 3
            }
            await wrapper.vm.tratarEventoAcessarNotificacao(item)

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]).not.toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({ name: 'VisualizarRegistroMovimentacaoInterna', params: { movimentacaoInternaId: item.origemId } })
        })

        it('Não deve redirecionar para a visualização de registro da incorporação ao acessar notificação incorporação não finalizada', async () => {
            actions = {
                [actionTypes.NOTIFICACAO.BUSCAR_NOTIFICACOES]: jest.fn().mockReturnValue(retornoBuscaNotificacoes),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]: jest.fn().mockReturnValue('EM_PROCESSAMENTO'),
                [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('FINALIZADO'),
                [actionTypes.NOTIFICACAO.BUSCAR_QUANTIDADE_NOTIFICACOES_NAO_VISUALIZADAS]: jest.fn().mockReturnValue(quantidadeNotificacoesNaoVisualizadas),
                [actionTypes.CONFIGURACAO.ROTULOS_PERSONALIZADOS.GET_ALL_ROTULOS_PERSONALIZADOS]: jest.fn()
            }
            wrapper = await shallowMount(RootPage, {
                localVue,
                vuetify,
                store: new Vuex.Store({state, mutations, actions}),
                router
            })
            await flushPromises()
            const item = {
                origem: 'INCORPORACAO',
                origemId: 4
            }
            await wrapper.vm.tratarEventoAcessarNotificacao(item)

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]).not.toHaveBeenCalled()
            expect(router.push).not.toHaveBeenCalled()
        })

        it('Não deve redirecionar para a visualização de registro da movimentação interna ao acessar notificação distribuição não finalizada', async () => {
            actions = {
                [actionTypes.NOTIFICACAO.BUSCAR_NOTIFICACOES]: jest.fn().mockReturnValue(retornoBuscaNotificacoes),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]: jest.fn().mockReturnValue('FINALIZADO'),
                [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue('EM_PROCESSAMENTO'),
                [actionTypes.NOTIFICACAO.BUSCAR_QUANTIDADE_NOTIFICACOES_NAO_VISUALIZADAS]: jest.fn().mockReturnValue(quantidadeNotificacoesNaoVisualizadas),
                [actionTypes.CONFIGURACAO.ROTULOS_PERSONALIZADOS.GET_ALL_ROTULOS_PERSONALIZADOS]: jest.fn()
            }
            wrapper = await shallowMount(RootPage, {
                localVue,
                vuetify,
                store: new Vuex.Store({state, mutations, actions}),
                router
            })
            await flushPromises()
            const item = {
                origem: 'DISTRIBUICAO',
                origemId: 3
            }
            await wrapper.vm.tratarEventoAcessarNotificacao(item)

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]).not.toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(router.push).not.toHaveBeenCalled()
        })

        it('Deve buscar situação incorporação', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(await wrapper.vm.buscarSituacaoDaIncorporacao(3)).toEqual('FINALIZADO')
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO].mock.calls[0][1]).toEqual(3)
        })

        it('Deve buscar situação movimentação interna', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(await wrapper.vm.buscarSituacaoDaMovimentacaoInterna(3)).toEqual('FINALIZADO')
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(3)
        })

        it('Deve redirecionar para VisualizarRegistroIncorporacao se finalizado', async () => {
            wrapper = defaultMount()
            await flushPromises()

            await wrapper.vm.redirecionarIncorporacaoSeFinalizado('FINALIZADO',3)

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({ name: 'VisualizarRegistroIncorporacao', params: { incorporacaoId: 3 } })
        })

        it('Não deve redirecionar para VisualizarRegistroIncorporacao se não finalizado', async () => {
            wrapper = defaultMount()
            await flushPromises()

            await wrapper.vm.redirecionarIncorporacaoSeFinalizado('EM_PROCESSAMENTO',3)

            expect(router.push).not.toHaveBeenCalled()
        })

        it('Deve redirecionar para VisualizarRegistroMovimentacaoInterna se finalizado', async () => {
            wrapper = defaultMount()
            await flushPromises()

            await wrapper.vm.redirecionarMovimentacaoInternaSeFinalizado('FINALIZADO',3)

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({ name: 'VisualizarRegistroMovimentacaoInterna', params: { movimentacaoInternaId: 3 } })
        })

        it('Não deve redirecionar para VisualizarRegistroMovimentacaoInterna se não finalizado', async () => {
            wrapper = defaultMount()
            await flushPromises()

            await wrapper.vm.redirecionarMovimentacaoInternaSeFinalizado('EM_PROCESSAMENTO',3)

            expect(router.push).not.toHaveBeenCalled()
        })
    })
})
