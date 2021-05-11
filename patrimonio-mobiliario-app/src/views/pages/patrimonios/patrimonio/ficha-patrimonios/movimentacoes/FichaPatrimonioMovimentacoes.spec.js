import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import {actionTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import FichaPatrimonioMovimentacoes from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/movimentacoes/FichaPatrimonioMovimentacoes'

describe('FichaPatrimonioMovimentacoes', () => {
    let wrapper, actions, localVue, router, state, store

    const movimentacoes = { items: [{
            tipo: 'INCORPORACAO',
            data: '2020-09-01T23:00:00.000-0400',
            numeroNotaLancamentoContabil: '3432532432',
            valorTotal: 100000.58,
            incorporacaoId: 1
        }]}

    const quantidadeItensAdicionados = {
        quantidadeItens: 1
    }

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getDistribuicaoValidado: () => jest.fn().mockReturnValue(true)
        }
    }

    const dadosIncorporacao = {
        numeroNotaLancamentoContabil: '123',
        valorTotal: 100000.00,
        situacao: 'FINALIZADO',
        dataFinalizacao: '2021-02-22 16:53:50.094000',
        dataCriacao: '2021-02-22 16:53:50.094000'
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {current: { name: 'FichaPatrimonioMovimentacoes', params:{patrimonioId: 1}}}
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}, {name: 'Mobiliario.Movimentacoes', hasAccess: true}]
                }
            }
        }

        actions = {
            [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(quantidadeItensAdicionados),
            [actionTypes.PATRIMONIO.BUSCAR_TODAS_MOVIMENTACOES]: jest.fn().mockReturnValue(movimentacoes)
        }

        store = new Vuex.Store({state, actions})
    })

    const defaultMount = () => shallowMount(FichaPatrimonioMovimentacoes, {
        localVue,
        router,
        store,
        propsData:{
            dadosIncorporacao: dadosIncorporacao
        }
    })

    describe('Methods', () => {
        it('deve buscar movimentações', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.buscarMovimentacoes()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODAS_MOVIMENTACOES]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODAS_MOVIMENTACOES].mock.calls[0][1]).toEqual(1)
            expect(wrapper.vm.movimentacoes).toEqual(movimentacoes.items)
        })


        it('deve fazer uma cópia da props dadosIncorporacao para incorporacao', () => {
            wrapper = defaultMount()

            wrapper.vm.preencherIncorporacao()
            expect(wrapper.vm.incorporacao).toEqual(dadosIncorporacao)
        })

        it('deve emitir o evento de buscar incorporação', () => {
            wrapper = defaultMount()
            wrapper.vm.buscarIncorporacao()

            expect(wrapper.emitted().buscarIncorporacao).toBeTruthy()
        })

        it('deve redirecionar para registro de incorporacao', async () => {
            wrapper = defaultMount()
            await flushPromises()
            const item = {incorporacaoId:1}
            wrapper.vm.redirecionaRegistroIncorporacao(item)

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarRegistroIncorporacao', params: {incorporacaoId: item.incorporacaoId}})
        })

        it('deve redirecionar para registro de incorporacao visualizacao', async () => {
            wrapper = defaultMount()
            await flushPromises()
            const item = {incorporacaoId:1}
            wrapper.vm.$store.state.loki.user.authorities[0].name = 'Consultor.Normal'
            wrapper.vm.redirecionaRegistroIncorporacao(item)

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarRegistroIncorporacaoVisualizacao', params: {incorporacaoId: item.incorporacaoId}})
        })

        it('deve mostrar apenas as 5 primeiras movimentações ao buscar', async () => {
            const movimentacoes = { items: [{incorporacaoId: 1},{incorporacaoId: 1},{incorporacaoId: 1},{incorporacaoId: 1},{incorporacaoId: 1},{incorporacaoId: 1}]}
            actions = {[actionTypes.PATRIMONIO.BUSCAR_TODAS_MOVIMENTACOES]: jest.fn().mockReturnValue(movimentacoes)}
            wrapper = shallowMount(FichaPatrimonioMovimentacoes, {
                localVue,
                router,
                propsData: {
                    dadosIncorporacao: dadosIncorporacao
                },
                store:new Vuex.Store({state, actions})
            })
            await flushPromises()
            await wrapper.vm.buscarMovimentacoes()

            expect(wrapper.vm.movimentacoes).toEqual(movimentacoes.items.slice(0,5))
            expect(wrapper.vm.mostraBotoes).toEqual(true)
            expect(wrapper.vm.mostraVerMais).toEqual(true)
        })

        it('deve mostrar todas movimentações ao buscar', async () => {
            const movimentacoes = { items: [{incorporacaoId: 1},{incorporacaoId: 1},{incorporacaoId: 1},{incorporacaoId: 1}]}
            actions = {[actionTypes.PATRIMONIO.BUSCAR_TODAS_MOVIMENTACOES]: jest.fn().mockReturnValue(movimentacoes)}
            wrapper = shallowMount(FichaPatrimonioMovimentacoes, {
                localVue,
                router,
                propsData: {
                    dadosIncorporacao: dadosIncorporacao
                },
                store:new Vuex.Store({state, actions})
            })
            await flushPromises()
            await wrapper.vm.buscarMovimentacoes()

            expect(wrapper.vm.movimentacoes).toEqual(movimentacoes.items)
            expect(wrapper.vm.mostraBotoes).toEqual(false)
        })

        it('deve mostrar todas as movimentações ao clicar em ver mais', () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacoes = []
            wrapper.vm.todasMovimentacoes = [{id:1},{id:2},{id:3},{id:4},{id:5},{id:6}]
            wrapper.vm.tratarEventoVerMais()

            expect(wrapper.vm.movimentacoes).toEqual(wrapper.vm.todasMovimentacoes)
            expect(wrapper.vm.mostraVerMais).toEqual(false)
        })

        it('deve mostrar apenas 5 movimentações ao clicar em ver menos', () => {
            wrapper = defaultMount()
            wrapper.vm.movimentacoes = []
            wrapper.vm.cincoMovimentacoes = [{id:2}]
            wrapper.vm.tratarEventoVerMenos()

            expect(wrapper.vm.movimentacoes).toEqual(wrapper.vm.cincoMovimentacoes)
            expect(wrapper.vm.mostraVerMais).toEqual(true)
        })

        it('deve retornar icone para item com tipo Distribuicao', () => {
            wrapper = defaultMount()
            const item = {tipo: 'DISTRIBUICAO'}

            expect(wrapper.vm.retornaIcone(item)).toEqual('priority_high')
        })

        it('deve retornar icone para item com tipo incorporação', () => {
            wrapper = defaultMount()
            const item = {tipo: 'INCORPORACAO', situacao: 'FINALIZADO'}

            expect(wrapper.vm.retornaIcone(item)).toEqual('done')
        })

        it('deve retornar cor para item com tipo incorporação', () => {
            wrapper = defaultMount()
            const item = {tipo: 'DISTRIBUICAO', situacao: 'FINALIZADO'}

            expect(wrapper.vm.retornaCor(item)).toEqual('primary')
        })

        it('deve retornar cor para item com tipo distribuição', () => {
            wrapper = defaultMount()
            const item = {tipo: 'DISTRIBUICAO', situacao: 'EM_ELABORACAO'}

            expect(wrapper.vm.retornaCor(item)).toEqual('orange')
        })

        it('deve retornar true para item com tipo incorporacao', () => {
            wrapper = defaultMount()
            const item = {tipo: 'INCORPORACAO'}

            expect(wrapper.vm.movimentacaoIncorporacao(item)).toEqual(true)
        })

        it('deve retornar false para item com tipo distribuicao', () => {
            wrapper = defaultMount()
            const item = {tipo: 'DISTRIBUICAO'}

            expect(wrapper.vm.movimentacaoIncorporacao(item)).toEqual(false)
        })

        it('deve retornar dataFinalizacao para item com situação finalizado', () => {
            wrapper = defaultMount()
            const item = {dataFinalizacao: '10/12/2020',situacao: 'FINALIZADO', dataCriacao:'01/01/2020'}

            expect(wrapper.vm.retornaData(item)).toEqual(item.dataFinalizacao)
        })

        it('deve retornar dataCriacao para item com situação em elaboração', () => {
            wrapper = defaultMount()
            const item = {dataFinalizacao: '10/12/2020',situacao: 'EM_ELABORACAO', dataCriacao:'01/01/2020'}

            expect(wrapper.vm.retornaData(item)).toEqual(item.dataCriacao)
        })

        it('deve redirecionar para documentos se campos obrigatorios preenchidos e houver pelo menos um item', async () => {
            wrapper = shallowMount(FichaPatrimonioMovimentacoes, {
                localVue,
                router,
                store: new Vuex.Store({actions, state, modules: {rotulosPersonalizados}})
            })

            const item = {
                id: 1,
                codigo: '0934830947',
                tipo: 'DISTRIBUICAO',
                orgao: 'AGEPAN',
                setorOrigem: 'Almoxarifado',
                setorDestino: 'Copa e Cozinha',
                situacao: 'EM_ELABORACAO'
            }

            await wrapper.vm.redirecionaRegistroMovimentacao(item)
            await flushPromises()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDocumentos', params: {movimentacaoInternaId: 1}})
        })

        it('deve redirecionar para itens se campos obrigatorios preenchidos e não houver itens', async () => {
            quantidadeItensAdicionados.quantidadeItens = 0
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_TODAS_MOVIMENTACOES]: jest.fn().mockReturnValue(movimentacoes),
                [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(quantidadeItensAdicionados)
            }
            wrapper = shallowMount(FichaPatrimonioMovimentacoes, {
                localVue,
                router,
                store: new Vuex.Store({actions, state, modules: {rotulosPersonalizados}})
            })
            const item = {
                id:1,
                codigo: '0934830947',
                tipo: 'DISTRIBUICAO',
                orgao: 'AGEPAN',
                setorOrigem: 'Almoxarifado',
                setorDestino: 'Copa e Cozinha',
                situacao: 'EM_ELABORACAO'
            }

            await wrapper.vm.redirecionaRegistroMovimentacao(item)
            await flushPromises()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoItens', params: {movimentacaoInternaId: 1}})
        })

        it('deve redirecionar para dados gerais se campos obrigatorios não preenchidos (distribuicao)', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getDistribuicaoValidado: () => jest.fn().mockReturnValue(false)
                }
            }

            wrapper = shallowMount(FichaPatrimonioMovimentacoes, {
                localVue,
                router,
                store: new Vuex.Store({actions, state, modules: {rotulosPersonalizados}})
            })

            const item = {
                id:1,
                codigo: '0934830947',
                tipo: 'DISTRIBUICAO',
                orgao: 'AGEPAN',
                setorOrigem: 'Almoxarifado',
                setorDestino: 'Copa e Cozinha',
                situacao: 'EM_ELABORACAO'
            }

            await wrapper.vm.redirecionaRegistroMovimentacao(item)
            await flushPromises()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
        })

        it('deve retornar true se todos campos obrigatorios preenchidos (distribuicao)', async () => {
            wrapper = shallowMount(FichaPatrimonioMovimentacoes, {
                localVue,
                router,
                store: new Vuex.Store({actions, state, modules: {rotulosPersonalizados}})
            })

            const item = {
                id:1,
                codigo: '0934830947',
                tipo: 'DISTRIBUICAO',
                orgao: 'AGEPAN',
                setorOrigem: 'Almoxarifado',
                setorDestino: 'Copa e Cozinha',
                situacao: 'EM_ELABORACAO'
            }

            await flushPromises()

            expect(await wrapper.vm.verificarSeCamposObrigatoriosPreenchidos(item)).toEqual(true)
        })

        it('deve retornar false se campo obrigatorio não preenchido (distribuicao)', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getDistribuicaoValidado: () => jest.fn().mockReturnValue(false)
                }
            }

            wrapper = shallowMount(FichaPatrimonioMovimentacoes, {
                localVue,
                router,
                store: new Vuex.Store({actions, state, modules: {rotulosPersonalizados}})
            })
            const item = {
                id:1,
                codigo: '0934830947',
                tipo: 'DISTRIBUICAO',
                orgao: 'AGEPAN',
                setorOrigem: 'Almoxarifado',
                setorDestino: 'Copa e Cozinha',
                situacao: 'EM_ELABORACAO'
            }
            await flushPromises()
            expect(await wrapper.vm.verificarSeCamposObrigatoriosPreenchidos(item)).toEqual(false)
        })

        it('deve retornar true se houver pelo menos um item', async () => {
            quantidadeItensAdicionados.quantidadeItens = 1

            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_TODAS_MOVIMENTACOES]: jest.fn().mockReturnValue(movimentacoes),
                [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(quantidadeItensAdicionados)
            }

            wrapper = shallowMount(FichaPatrimonioMovimentacoes, {
                localVue,
                router,
                store: new Vuex.Store({actions, state, modules: {rotulosPersonalizados}})
            })
            await flushPromises()
            const resultado = await wrapper.vm.verificarSePossuiItensNaMovimentacao(1)
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(1)
            expect(resultado).toEqual(true)
        })

        it('deve retornar false se não houver item', async () => {
            quantidadeItensAdicionados.quantidadeItens = 0

            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_TODAS_MOVIMENTACOES]: jest.fn().mockReturnValue(movimentacoes),
                [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(quantidadeItensAdicionados)
            }

            wrapper = shallowMount(FichaPatrimonioMovimentacoes, {
                localVue,
                router,
                store: new Vuex.Store({actions, state, modules: {rotulosPersonalizados}})
            })

            const resultado = await wrapper.vm.verificarSePossuiItensNaMovimentacao(1)
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(1)
            expect(resultado).toEqual(false)
        })

        it('deve redirecionar para documentos ', () => {
            wrapper = shallowMount(FichaPatrimonioMovimentacoes, {
                localVue,
                router,
                store: new Vuex.Store({actions, state, modules: {rotulosPersonalizados}})
            })

            wrapper.vm.redirecionaParaDocumentos(1)

            expect(router.push.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDocumentos', params: {movimentacaoInternaId: 1}})
        })

        it('deve redirecionar para itens ', () => {
            wrapper = defaultMount()
            wrapper.vm.redirecionaParaItens(1)

            expect(router.push.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoItens', params: {movimentacaoInternaId: 1}})
        })

        it('deve redirecionar para dados gerais ', () => {
            wrapper = defaultMount()
            wrapper.vm.redirecionaParaDadosGerais(1)

            expect(router.push.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
        })
    })
})
