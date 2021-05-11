import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import MovimentacaoInternaListagem from './MovimentacaoInternaListagem'

describe('MovimentacaoInternaListagem', () => {
    let wrapper, mutations, actions, localVue, router, state

    const movimentacoes = {
        items:[
            {
                id:1,
                codigo: '0934830947',
                tipo: 'DISTRIBUICAO',
                orgao: 'AGEPAN',
                setorOrigem: 'Almoxarifado',
                setorDestino: 'Copa e Cozinha',
                situacao: 'EM_ELABORACAO'
            }
        ],
        totalPages: 1,
        totalElements: 1
    }

    const quantidadeItensAdicionados = {
        quantidadeItens: 1
    }

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getDistribuicaoValidado: () => jest.fn().mockReturnValue(true),
            getMovimentacaoInternaValidado: () => jest.fn().mockReturnValue(true),
            getTemporariaValidado: () => jest.fn().mockReturnValue(true)
        }
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {current: {}}
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [{name: 'Mobiliario.Movimentacoes', hasAccess: true}]
                }
            },
            movimentacaointerna: {
                resultadoBuscaTodasMovimentacoesInternas: {
                    filtros: {
                        conteudo: {
                            default: null,
                            label: 'Pesquisa',
                            value: ''
                        }
                    },
                    paginacao: {
                        page: 2,
                        rowsPerPage: 10,
                        sortBy: ['codigo'],
                        defaultSortBy: ['codigo'],
                        sortDesc: [false]
                    }
                },
            }
        }

        mutations = {
            [mutationTypes.MOVIMENTACAO_INTERNA.RESETA_PAGE_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]: jest.fn(),
            [mutationTypes.MOVIMENTACAO_INTERNA.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]: jest.fn(),
            [mutationTypes.MOVIMENTACAO_INTERNA.SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]: jest.fn()
        }

        actions = {
            [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TODAS_MOVIMENTACOES_INTERNAS]: jest.fn().mockReturnValue(movimentacoes),
            [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(quantidadeItensAdicionados)
        }
        Vue.use(Vuex)
    })

    const defaultMount = (stubs) => shallowMount(MovimentacaoInternaListagem, {
        localVue,
        router,
        store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}}),
        stubs
    })

    describe('Methods', () => {

        describe('tratarEventoAcessar', () => {

            it('não Deve redirecionar se situação da movimentacao = EM_PROCESSAMENTO', async () => {
                wrapper = defaultMount()
                await flushPromises()
                const item = {
                    id:1,
                    situacao: 'EM_PROCESSAMENTO'
                }
                wrapper.vm.tratarEventoAcessar(item)

                expect(router.replace).not.toHaveBeenCalled()
            })

            it('Deve redirecionar para MovimentacaoInternaEdicaoDocumentos se campos obrigatorios preenchidos e houver pelo menos um item', async () => {
                wrapper = defaultMount()
                const item = {
                    id:1,
                    codigo: '0934830947',
                    tipo: 'DISTRIBUICAO',
                    orgao: 'AGEPAN',
                    setorOrigem: 'Almoxarifado',
                    setorDestino: 'Copa e Cozinha',
                    situacao: 'EM_ELABORACAO'
                }
                wrapper.vm.tratarEventoAcessar(item)
                await flushPromises()

                expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDocumentos', params: {movimentacaoInternaId: 1}})
            })

            it('Deve redirecionar para MovimentacaoInternaEdicaoItens se campos obrigatorios preenchidos e não houver itens', async () => {
                quantidadeItensAdicionados.quantidadeItens = 0
                actions = {
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TODAS_MOVIMENTACOES_INTERNAS]: jest.fn().mockReturnValue(movimentacoes),
                    [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(quantidadeItensAdicionados)
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
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
                wrapper.vm.tratarEventoAcessar(item)
                await flushPromises()

                expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoItens', params: {movimentacaoInternaId: 1}})
            })

            it('Deve redirecionar para MovimentacaoInternaEdicaoDadosGerais se campos obrigatorios não preenchidos (DISTRIBUICAO)', async () => {
                const rotulosPersonalizados = {
                    namespaced: true,
                    actions: {getAllRotulosPersonlizados: jest.fn()},
                    getters: {
                        getDistribuicaoValidado: () => jest.fn().mockReturnValue(false)
                    }
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
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
                wrapper.vm.tratarEventoAcessar(item)
                await flushPromises()

                expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
            })

            it('Deve redirecionar para MovimentacaoInternaEdicaoDadosGerais se campos obrigatorios não preenchidos (ENTRE_ESTOQUES)', async () => {
                const rotulosPersonalizados = {
                    namespaced: true,
                    actions: {getAllRotulosPersonlizados: jest.fn()},
                    getters: {
                        getMovimentacaoInternaValidado: () => jest.fn().mockReturnValue(false)
                    }
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
                })
                const item = {
                    id:1,
                    codigo: '0934830947',
                    tipo: 'ENTRE_ESTOQUES',
                    orgao: 'AGEPAN',
                    setorOrigem: 'Almoxarifado',
                    setorDestino: 'Copa e Cozinha',
                    situacao: 'EM_ELABORACAO'
                }
                wrapper.vm.tratarEventoAcessar(item)
                await flushPromises()

                expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
            })

            it('Deve redirecionar para MovimentacaoInternaEdicaoDadosGerais se campos obrigatorios não preenchidos (ENTRE_SETORES)', async () => {
                const rotulosPersonalizados = {
                    namespaced: true,
                    actions: {getAllRotulosPersonlizados: jest.fn()},
                    getters: {
                        getMovimentacaoInternaValidado: () => jest.fn().mockReturnValue(false)
                    }
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
                })
                const item = {
                    id:1,
                    codigo: '0934830947',
                    tipo: 'ENTRE_SETORES',
                    orgao: 'AGEPAN',
                    setorOrigem: 'Almoxarifado',
                    setorDestino: 'Copa e Cozinha',
                    situacao: 'EM_ELABORACAO'
                }
                wrapper.vm.tratarEventoAcessar(item)
                await flushPromises()

                expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
            })

            it('Deve redirecionar para MovimentacaoInternaEdicaoDadosGerais se campos obrigatorios não preenchidos (DEVOLUCAO_ALMOXARIFADO)', async () => {
                const rotulosPersonalizados = {
                    namespaced: true,
                    actions: {getAllRotulosPersonlizados: jest.fn()},
                    getters: {
                        getMovimentacaoInternaValidado: () => jest.fn().mockReturnValue(false)
                    }
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
                })
                const item = {
                    id:1,
                    codigo: '0934830947',
                    tipo: 'DEVOLUCAO_ALMOXARIFADO',
                    orgao: 'AGEPAN',
                    setorOrigem: 'Almoxarifado',
                    setorDestino: 'Copa e Cozinha',
                    situacao: 'EM_ELABORACAO'
                }
                wrapper.vm.tratarEventoAcessar(item)
                await flushPromises()

                expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
            })

            it('Deve redirecionar para MovimentacaoInternaEdicaoDadosGerais se campos obrigatorios não preenchidos (TEMPORARIA)', async () => {
                const rotulosPersonalizados = {
                    namespaced: true,
                    actions: {getAllRotulosPersonlizados: jest.fn()},
                    getters: {
                        getTemporariaValidado: () => jest.fn().mockReturnValue(false)
                    }
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
                })
                const item = {
                    id:1,
                    codigo: '0934830947',
                    tipo: 'TEMPORARIA',
                    orgao: 'AGEPAN',
                    setorOrigem: 'Almoxarifado',
                    setorDestino: 'Copa e Cozinha',
                    situacao: 'EM_ELABORACAO'
                }
                wrapper.vm.tratarEventoAcessar(item)
                await flushPromises()

                expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
            })

            it('Deve redirecionar para MovimentacaoInternaEdicaoDadosGerais se situação da movimentacao = ERRO_PROCESSAMENTO', async () => {
                wrapper = defaultMount()
                await flushPromises()
                const item = {
                    id:1,
                    situacao: 'ERRO_PROCESSAMENTO'
                }
                wrapper.vm.tratarEventoAcessar(item)

                expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
            })

            it('Deve redirecionar para VisualizarRegistroMovimentacaoInterna se situação da movimentacao = FINALIZADO', async () => {
                wrapper = defaultMount()
                await flushPromises()
                const item = {
                    id:1,
                    situacao: 'FINALIZADO'
                }
                wrapper.vm.tratarEventoAcessar(item)

                expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarRegistroMovimentacaoInterna', params: {movimentacaoInternaId: 1}})
            })

            it('Deve redirecionar para VisualizarRegistroMovimentacaoInterna se situação da movimentacao = AGUARDANDO_DEVOLUCAO', async () => {
                wrapper = defaultMount()
                await flushPromises()
                const item = {
                    id:1,
                    situacao: 'AGUARDANDO_DEVOLUCAO'
                }
                wrapper.vm.tratarEventoAcessar(item)

                expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarRegistroMovimentacaoInterna', params: {movimentacaoInternaId: 1}})
            })

            it('Deve redirecionar para VisualizarRegistroMovimentacaoInterna se situação da movimentacao = DEVOLVIDO', async () => {
                wrapper = defaultMount()
                await flushPromises()
                const item = {
                    id:1,
                    situacao: 'DEVOLVIDO'
                }
                wrapper.vm.tratarEventoAcessar(item)

                expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarRegistroMovimentacaoInterna', params: {movimentacaoInternaId: 1}})
            })

            it('Deve redirecionar para VisualizarRegistroMovimentacaoInterna se situação da movimentacao = DEVOLVIDO_PARCIAL', async () => {
                wrapper = defaultMount()
                await flushPromises()
                const item = {
                    id:1,
                    situacao: 'DEVOLVIDO_PARCIAL'
                }
                wrapper.vm.tratarEventoAcessar(item)

                expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarRegistroMovimentacaoInterna', params: {movimentacaoInternaId: 1}})
            })
        })

        describe('verificarSeCamposObrigatoriosPreenchidos', () => {
            it('Deve retornar true se todos campos obrigatorios preenchidos (DISTRIBUICAO)', async () => {
                wrapper = defaultMount()
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

            it('Deve retornar false se campo obrigatorio não preenchido (DISTRIBUICAO)', async () => {
                const rotulosPersonalizados = {
                    namespaced: true,
                    actions: {getAllRotulosPersonlizados: jest.fn()},
                    getters: {
                        getDistribuicaoValidado: () => jest.fn().mockReturnValue(false)
                    }
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
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

            it('Deve retornar false se campo obrigatorio não preenchido (ENTRE_ESTOQUES)', async () => {
                const rotulosPersonalizados = {
                    namespaced: true,
                    actions: {getAllRotulosPersonlizados: jest.fn()},
                    getters: {
                        getMovimentacaoInternaValidado: () => jest.fn().mockReturnValue(false)
                    }
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
                })
                const item = {
                    id:1,
                    codigo: '0934830947',
                    tipo: 'ENTRE_ESTOQUES',
                    orgao: 'AGEPAN',
                    setorOrigem: 'Almoxarifado',
                    setorDestino: 'Copa e Cozinha',
                    situacao: 'EM_ELABORACAO'
                }
                await flushPromises()
                expect(await wrapper.vm.verificarSeCamposObrigatoriosPreenchidos(item)).toEqual(false)
            })

            it('Deve retornar false se campo obrigatorio não preenchido (DEVOLUCAO_ALMOXARIFADO)', async () => {
                const rotulosPersonalizados = {
                    namespaced: true,
                    actions: {getAllRotulosPersonlizados: jest.fn()},
                    getters: {
                        getMovimentacaoInternaValidado: () => jest.fn().mockReturnValue(false)
                    }
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
                })
                const item = {
                    id:1,
                    codigo: '0934830947',
                    tipo: 'DEVOLUCAO_ALMOXARIFADO',
                    orgao: 'AGEPAN',
                    setorOrigem: 'Almoxarifado',
                    setorDestino: 'Copa e Cozinha',
                    situacao: 'EM_ELABORACAO'
                }
                await flushPromises()
                expect(await wrapper.vm.verificarSeCamposObrigatoriosPreenchidos(item)).toEqual(false)
            })

            it('Deve retornar false se campo obrigatorio não preenchido (TEMPORARIA)', async () => {
                const rotulosPersonalizados = {
                    namespaced: true,
                    actions: {getAllRotulosPersonlizados: jest.fn()},
                    getters: {
                        getTemporariaValidado: () => jest.fn().mockReturnValue(false)
                    }
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
                })
                const item = {
                    id:1,
                    codigo: '0934830947',
                    tipo: 'TEMPORARIA',
                    orgao: 'AGEPAN',
                    setorOrigem: 'Almoxarifado',
                    setorDestino: 'Copa e Cozinha',
                    situacao: 'EM_ELABORACAO'
                }
                await flushPromises()
                expect(await wrapper.vm.verificarSeCamposObrigatoriosPreenchidos(item)).toEqual(false)
            })

            it('Deve retornar false se tipo da movimentação diferente de (DISTRIBUICAO,ENTRE_ESTOQUES,ENTRE_SETORES,DEVOLUCAO_ALMOXARIFADO,TEMPORARIA)', async () => {
                wrapper = defaultMount()
                const item = {
                    id:1,
                    codigo: '0934830947',
                    tipo: 'TEMP',
                    orgao: 'AGEPAN',
                    setorOrigem: 'Almoxarifado',
                    setorDestino: 'Copa e Cozinha',
                    situacao: 'EM_ELABORACAO'
                }
                await flushPromises()
                expect(await wrapper.vm.verificarSeCamposObrigatoriosPreenchidos(item)).toEqual(false)
            })
        })

        describe('verificarSePossuiItensNaMovimentacao', () => {
            it('Deve retornar true se houver pelo menos um item', async () => {
                quantidadeItensAdicionados.quantidadeItens = 1
                actions = {
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TODAS_MOVIMENTACOES_INTERNAS]: jest.fn().mockReturnValue(movimentacoes),
                    [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(quantidadeItensAdicionados)
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
                })
                await flushPromises()
                const resultado = await wrapper.vm.verificarSePossuiItensNaMovimentacao(1)
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(1)
                expect(resultado).toEqual(true)
            })

            it('Deve retornar false se não houver item', async () => {
                quantidadeItensAdicionados.quantidadeItens = 0
                actions = {
                    [actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TODAS_MOVIMENTACOES_INTERNAS]: jest.fn().mockReturnValue(movimentacoes),
                    [actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(quantidadeItensAdicionados)
                }
                wrapper = shallowMount(MovimentacaoInternaListagem, {
                    localVue,
                    router,
                    store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
                })
                await flushPromises()
                const resultado = await wrapper.vm.verificarSePossuiItensNaMovimentacao(1)
                await flushPromises()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
                expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(1)
                expect(resultado).toEqual(false)
            })
        })

        it('Deve redirecionar para visualização de registro da movimentação interna ', () => {
            wrapper = defaultMount()
            wrapper.vm.redirecionaParaVisualizacaoMovimentacao(1)

            expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarRegistroMovimentacaoInterna', params: {movimentacaoInternaId: 1}})
        })

        it('Deve redirecionar para documentos ', () => {
            wrapper = defaultMount()
            wrapper.vm.redirecionaParaDocumentos(1)

            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDocumentos', params: {movimentacaoInternaId: 1}})
        })

        it('Deve redirecionar para itens ', () => {
            wrapper = defaultMount()
            wrapper.vm.redirecionaParaItens(1)

            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoItens', params: {movimentacaoInternaId: 1}})
        })

        it('Deve redirecionar para dados gerais ', () => {
            wrapper = defaultMount()
            wrapper.vm.redirecionaParaDadosGerais(1)

            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
        })

        it('Deve redirecionar para cadastro de movimentação', () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoNovaMovimentacaoInterna()

            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaNovoTipo'})
        })

        it('Deve remover filtro',  () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoRemoverFiltro('conteudo')
            flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toBe(null)
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TODAS_MOVIMENTACOES_INTERNAS]).toHaveBeenCalled()
        })

        it('Deve realizar busca simples', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoBuscaSimples('simples')
            await flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('simples')
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.RESETA_PAGE_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TODAS_MOVIMENTACOES_INTERNAS]).toHaveBeenCalled()
        })

        it('Deve paginar',  () => {
            wrapper = defaultMount()
            const paginacao = {sortBy: ['codigo']}
            wrapper.vm.tratarEventoPaginar(paginacao)
            flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]).toHaveBeenCalled()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES_INTERNAS].mock.calls[0][1]).toEqual(paginacao)
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TODAS_MOVIMENTACOES_INTERNAS]).toHaveBeenCalled()
        })

        it('Deve resetar sortBy',  () => {
            wrapper = defaultMount()
            const paginacao = {sortBy: [], defaultSortBy:'default'}
            wrapper.vm.resetarPaginacaoSortBy(paginacao)
            flushPromises()

            expect(state.movimentacaointerna.resultadoBuscaTodasMovimentacoesInternas.paginacao.sortBy[0]).toEqual(paginacao.defaultSortBy)
        })

        it('Deve retornar um clone dos filtros', async() => {
            wrapper = defaultMount()

            const resultado = wrapper.vm.getFiltros()

            expect(resultado).toEqual(state.movimentacaointerna.resultadoBuscaTodasMovimentacoesInternas.filtros)
        })

        it('Deve retornar um clone dos filtros internos', async() => {
            wrapper = defaultMount()

            wrapper.vm.filtrosInterno = wrapper.vm.getFiltros()
            const resultado = wrapper.vm.getFiltrosInterno()

            expect(resultado).toEqual(wrapper.vm.filtrosInterno)
        })

        it('Deve resetar paginacao',  () => {
            wrapper = defaultMount()
            wrapper.vm.resetaPage()
            flushPromises()

            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.RESETA_PAGE_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]).toHaveBeenCalled()
        })
    })
})
