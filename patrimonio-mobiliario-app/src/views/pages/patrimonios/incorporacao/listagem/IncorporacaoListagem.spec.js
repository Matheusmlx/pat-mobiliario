import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import IncorporacaoListagem from './IncorporacaoListagem'

describe('IncorporacaoListagem', () => {
    let wrapper, mutations, actions, localVue, router, state, store, stubs

    const incorporacoes = {
        items: [
            {
                id: 3,
                codigo: '4',
                quantidade: null,
                dataRecebimento: '2020-09-09T00:00:00.000-0400',
                situacao: 'EM_ELABORACAO',
                numProcesso: null,
                nota: '',
                valorNota: '1.110000',
                dataNota: '2020-09-01T00:00:00.000-0400',
                fornecedor: 'Google Brasil Internet Ltda',
                orgao: 132,
                setor: 5127,
                convenio: null,
                reconhecimento:
                    {
                        id: 1,
                        notaFiscal: null,
                        empenho: null,
                    },
                naturezaDespesa: null,
                origemConvenio: true,
                origemFundo: false,
            },
        ],
        totalElements: 1,
        totalPages: 1,
    }
    const empenhos = {
        items: [{
            numeroEmpenho: '1',
            dataEmpenho: '12/12/2020',
            valorEmpenho: 12.00,
        }],
    }

    const incorporacaoSalva = {
        id: 1,
    }

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getIncorporacaoValidado: () => jest.fn().mockReturnValue(true),
        },
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {current: {}},
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}],
                },
            },
            incorporacao: {
                resultadoBuscaTodasIncorporacoes: {
                    paginacao: {
                        page: 0, rowsPerPage: 10, sortDesc: [false], sortBy: ['situacao'], defaultSortBy: ['situacao'],
                    },
                    filtros: {
                        conteudo: {
                            value: null,
                            default: null,
                            label: 'Pesquisa',
                        },
                    },
                },
            },
        }

        stubs = {
            IncorporacaoListagemTabela: {
                template: '<div><button class="stub" @click="$emit(\'acessar\', incorporacao = {\n' +
                    '        id:3,\n' +
                    '        codigo:388381,\n' +
                    '        fornecedor: {id: 1, nome: \'Loja de Armas e Munições o Mercado S.A\'},\n' +
                    '        quantidade: 1000,\n' +
                    '        recebimento: \'2020-05-25T00:00:00\',\n' +
                    '        situacao: \'ERRO_PROCESSAMENTO\',\n' +
                    '        reconhecimento: {id: 1, empenho: null, notaFiscal: null},\n' +
                    '\n' +
                    '    })"></button></div>',
            },
            'az-search': {
                template: '<div><button class="stub" @click="$emit(\'simple-search\', \'111112345678\')"></button></div>',
            },
        }

        mutations = {
            [mutationTypes.PATRIMONIO.INCORPORACAO.SET_FILTROS_BUSCA_TODAS_INCORPORACOES]: jest.fn(),
            [mutationTypes.PATRIMONIO.INCORPORACAO.SET_PAGINACAO_BUSCA_TODAS_INCORPORACOES]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.SET_ROTA_ORIGEM]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.PATRIMONIO.INCORPORACAO.RESETA_PAGE_INCORPORACAO]: jest.fn(),
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES]: jest.fn().mockReturnValue(incorporacoes),
            [actionTypes.PATRIMONIO.INCORPORACAO.CADASTRAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacaoSalva),
            [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]: jest.fn(),
        }

        store = new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
    })

    const defaultShallowMount = () => shallowMount(IncorporacaoListagem, {
        localVue,
        router,
        store,
        stubs: stubs,

    })

    describe('Events', () => {
        it('Deve emitir quando alterar a paginação', async () => {
            wrapper = defaultShallowMount()

            wrapper.vm.tratarEventoPaginar({})

            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.SET_PAGINACAO_BUSCA_TODAS_INCORPORACOES].mock.calls[0][1]).toEqual({})
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES]).toHaveBeenCalled()
        })

        it('Deve emitir o evento de buscar simples', async () => {
            wrapper = defaultShallowMount()

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.SET_FILTROS_BUSCA_TODAS_INCORPORACOES].mock.calls[0][1].conteudo.value)
                .toEqual('111112345678')
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES]).toHaveBeenCalled()
            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('111112345678')
        })

        it('Deve emitir o evento de remover os filtros da pesquisa', async () => {
            stubs = {
                'az-search': {
                    template: '<div><button class="stub" @click="$emit(\'remove-filter\', \'conteudo\')"></button></div>',
                },
            }

            wrapper = defaultShallowMount()

            wrapper.vm.filtrosInterno.conteudo.value = 'Caixa Seguradora de Bens Social'
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual(null)
        })

        describe('tratarEventoAcessar', () => {
            it('Não deve redirecionar se situacao EM_PROCESSAMENTO', async () => {
                state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Normal'}]

                stubs = {
                    IncorporacaoListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'acessar\', incorporacao = {\n' +
                            '        id:3,\n' +
                            '        codigo:388381,\n' +
                            '        fornecedor: {id: 1, nome: \'Loja de Armas e Munições o Mercado S.A\'},\n' +
                            '        quantidade: 1000,\n' +
                            '        recebimento: \'2020-05-25T00:00:00\',\n' +
                            '        situacao: \'EM_PROCESSAMENTO\',\n' +
                            '        reconhecimento: {id: 1, empenho: null, notaFiscal: null},\n' +
                            '\n' +
                            '    })"></button></div>',
                    },
                }

                wrapper = defaultShallowMount()

                expect(router.push).not.toHaveBeenCalled()
            })

            it('Deve redirecionar para IncorporacaoDocumentosEdicao se incorporação estiver com campos obrigatorios preenchidose possuir itens', async () => {
                state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Normal'}]
                const itens = {totalElements: 3}
                actions = {
                    [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES]: jest.fn().mockReturnValue(incorporacoes),
                    [actionTypes.PATRIMONIO.INCORPORACAO.CADASTRAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacaoSalva),
                    [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                    [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]: jest.fn().mockReturnValue(itens),
                }

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaIncorproacaoDocumentos(3)

                await flushPromises()
                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'IncorporacaoDocumentosEdicao',
                    params: {incorporacaoId: 3},
                })
            })

            it('Deve redirecionar para VisualizarIncorporacaoDocumentos se incorporação estiver com campos obrigatorios preenchidose possuir itens', async () => {
                state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Consulta'}]
                const itens = {totalElements: 3}
                actions = {
                    [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES]: jest.fn().mockReturnValue(incorporacoes),
                    [actionTypes.PATRIMONIO.INCORPORACAO.CADASTRAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacaoSalva),
                    [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                    [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]: jest.fn().mockReturnValue(itens),
                }

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaIncorproacaoDocumentos(3)

                await flushPromises()

                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'VisualizarIncorporacaoDocumentos',
                    params: {incorporacaoId: 3},
                })
            })

            it('Deve redirecionar para ItensIncorporacaoListagem se incorporação estiver com campos obrigatorios preenchidose não possuir itens', async () => {
                state.loki.user.authorities = [{
                    hasAccess: true,
                    name: 'Mobiliario.Normal',
                }]

                stubs = {
                    IncorporacaoListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'acessar\', incorporacao = {\n' +
                            '        id:3,\n' +
                            '        codigo:388381,\n' +
                            '        fornecedor: {id: 1, nome: \'Loja de Armas e Munições o Mercado S.A\'},\n' +
                            '        quantidade: 1000,\n' +
                            '        recebimento: \'2020-05-25T00:00:00\',\n' +
                            '        situacao: \'EM_ELABORACAO\',\n' +
                            '        reconhecimento: {id: 1, empenho: null, notaFiscal: null},\n' +
                            '\n' +
                            '    })"></button></div>',
                    },
                }

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaItensIncorporacaoListagem(3)

                await flushPromises()
                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'ItensIncorporacaoListagem',
                    params: {incorporacaoId: 3},
                })
            })

            it('Deve redirecionar para VisualizarItensIncorporacao se incorporação estiver com campos obrigatorios preenchidos, permissao de visualização e não tiver itens', async () => {
                state.loki.user.authorities = [{
                    hasAccess: true,
                    name: 'Mobiliario.Consultor',
                }]

                stubs = {
                    IncorporacaoListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'acessar\', incorporacao = {\n' +
                            '        id:3,\n' +
                            '        codigo:388381,\n' +
                            '        fornecedor: {id: 1, nome: \'Loja de Armas e Munições o Mercado S.A\'},\n' +
                            '        quantidade: 1000,\n' +
                            '        recebimento: \'2020-05-25T00:00:00\',\n' +
                            '        situacao: \'EM_ELABORACAO\',\n' +
                            '        reconhecimento: {id: 1, empenho: null, notaFiscal: null},\n' +
                            '\n' +
                            '    })"></button></div>',
                    },
                }

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaItensIncorporacaoListagem(3)

                await flushPromises()
                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'VisualizarItensIncorporacao',
                    params: {incorporacaoId: 3},
                })
            })

            it('Deve redirecionar para EditarIncorporacao se incorporação estiver com campos obrigatorios não preenchidos', async () => {
                state.loki.user.authorities = [{
                    hasAccess: true,
                    name: 'Mobiliario.Normal',
                }]

                stubs = {
                    IncorporacaoListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'acessar\', incorporacao = {\n' +
                            '        id:3,\n' +
                            '        codigo:388381,\n' +
                            '        fornecedor: {id: 1, nome: \'Loja de Armas e Munições o Mercado S.A\'},\n' +
                            '        quantidade: 1000,\n' +
                            '        recebimento: \'2020-05-25T00:00:00\',\n' +
                            '        situacao: \'EM_ELABORACAO\',\n' +
                            '        reconhecimento: {id: 1, empenho: null, notaFiscal: null},\n' +
                            '\n' +
                            '    })"></button></div>',
                    },
                }

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaIncorporacao(3)

                await flushPromises()
                expect(router.push.mock.calls[0][0]).toEqual({name: 'EditarIncorporacao', params: {incorporacaoId: 3}})
            })

            it('Deve redirecionar para VisualizarIncorporacao se incorporação estiver com campos obrigatorios não preenchidos e permissão de visualização', async () => {
                state.loki.user.authorities = [{
                    hasAccess: true,
                    name: 'Mobiliario.Consulta',
                }]

                stubs = {
                    IncorporacaoListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'acessar\', incorporacao = {\n' +
                            '        id:3,\n' +
                            '        codigo:388381,\n' +
                            '        fornecedor: {id: 1, nome: \'Loja de Armas e Munições o Mercado S.A\'},\n' +
                            '        quantidade: 1000,\n' +
                            '        recebimento: \'2020-05-25T00:00:00\',\n' +
                            '        situacao: \'EM_ELABORACAO\',\n' +
                            '        reconhecimento: {id: 1, empenho: null, notaFiscal: null},\n' +
                            '\n' +
                            '    })"></button></div>',
                    },
                }

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaIncorporacao(3)

                await flushPromises()
                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'VisualizarIncorporacao',
                    params: {incorporacaoId: 3},
                })
            })

            it('Deve redirecionar para EditarIncorporacao se incorporação situacao ERRO_PROCESSAMENTO', async () => {
                state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Normal'}]

                stubs = {
                    IncorporacaoListagemTabela: {
                        template: '<div><button class="stub" @click="$emit(\'acessar\', incorporacao = {\n' +
                            '        id:3,\n' +
                            '        codigo:388381,\n' +
                            '        fornecedor: {id: 1, nome: \'Loja de Armas e Munições o Mercado S.A\'},\n' +
                            '        quantidade: 1000,\n' +
                            '        recebimento: \'2020-05-25T00:00:00\',\n' +
                            '        situacao: \'ERRO_PROCESSAMENTO\',\n' +
                            '        reconhecimento: {id: 1, empenho: null, notaFiscal: null},\n' +
                            '\n' +
                            '    })"></button></div>',
                    },
                }

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaIncorporacao(3)

                await flushPromises()
                expect(router.push.mock.calls[0][0]).toEqual({name: 'EditarIncorporacao', params: {incorporacaoId: 3}})
            })

            it('Deve redirecionar para VisualizarIncorporacao se incorporação situacao ERRO_PROCESSAMENTO e permissao de visualizacao', async () => {
                state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Consulta'}]

                const itemIncorpocaoMock = {
                    id: 3,
                    fornecedor: {
                        id: 1,
                        nome: 'Loja de Armas e Munições o Mercado S.A',
                    },
                    quantidade: 1000,
                    situacao: 'ERRO_PROCESSAMENTO',
                    reconhecimento: {
                        id: 1,
                        empenho: null,
                        notaFiscal: null,
                    },
                }

                wrapper = defaultShallowMount()

                wrapper.vm.tratarRedirecionamentosParaItemSituacaoDiferenteDeElaboracao(itemIncorpocaoMock)

                await flushPromises()
                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'VisualizarIncorporacao',
                    params: {incorporacaoId: 3},
                })
            })

            it('Deve redirecionar para VisualizarRegistroIncorporacao se incorporação situacao ESTORNADO', async () => {
                state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Normal'}]

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaVisualizacaoRegistro(3)

                await flushPromises()
                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'VisualizarRegistroIncorporacao',
                    params: {incorporacaoId: 3},
                })
            })

            it('Deve redirecionar para VisualizarRegistroIncorporacaoVisualizacao se incorporação situacao ESTORNADO e permissao de visualizacao', async () => {
                state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Consulta'}]

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaVisualizacaoRegistro(3)

                await flushPromises()
                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'VisualizarRegistroIncorporacaoVisualizacao',
                    params: {incorporacaoId: 3},
                })
            })

            it('Deve redirecionar para VisualizarRegistroIncorporacao se incorporação situacao PARCIALMENTE_ESTORNADO', async () => {
                state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Normal'}]

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaVisualizacaoRegistro(3)

                await flushPromises()
                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'VisualizarRegistroIncorporacao',
                    params: {incorporacaoId: 3},
                })
            })

            it('Deve redirecionar para VisualizarRegistroIncorporacaoVisualizacao se incorporação situacao PARCIALMENTE_ESTORNADO e permissao de visualizacao', async () => {
                state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Consulta'}]

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaVisualizacaoRegistro(3)

                await flushPromises()

                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'VisualizarRegistroIncorporacaoVisualizacao',
                    params: {incorporacaoId: 3},
                })
            })

            it('Deve redirecionar para VisualizarRegistroIncorporacao se incorporação situacao FINALIZADO', async () => {
                state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Normal'}]

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaVisualizacaoRegistro(3)

                await flushPromises()

                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'VisualizarRegistroIncorporacao',
                    params: {incorporacaoId: 3},
                })
            })

            it('Deve redirecionar para VisualizarRegistroIncorporacaoVisualizacao se incorporação situacao FINALIZADO e permissao de visualizacao', async () => {
                state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Consulta'}]

                wrapper = defaultShallowMount()

                wrapper.vm.verificarRotaVisualizacaoRegistro(3)

                await flushPromises()

                expect(router.push.mock.calls[0][0]).toEqual({
                    name: 'VisualizarRegistroIncorporacaoVisualizacao',
                    params: {incorporacaoId: 3},
                })
            })
        })

        it('Deve emitir o evento de inserir um novo cadastro de incorporação', async () => {
            stubs = {
                'botao-novo': {
                    template: '<div><button class="stub" @click="$emit(\'click\')"></button></div>',
                },
            }

            wrapper = defaultShallowMount()

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'EditarIncorporacao', params: {incorporacaoId: 1}})
        })
    })

    describe('Methods', () => {
        it('Deve emitir o método de buscar todas incorporações', async () => {
            wrapper = defaultShallowMount()

            await flushPromises()
            wrapper.vm.buscar()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES]).toHaveBeenCalled()
        })

        it('Deve buscar os itens de incorporação', async () => {
            wrapper = defaultShallowMount()

            wrapper.vm.buscarItensIncorporacao()

            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]).toHaveBeenCalled()
        })

        it('Deve retornar false se empenho obrigatório e algum dos empenhos não estão preenchidos', async () => {
            const empenhos = {
                items: [{
                    numeroEmpenho: '1',
                    dataEmpenho: null,
                    valorEmpenho: 12.00,
                }],
            }
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES]: jest.fn().mockReturnValue(incorporacoes),
                [actionTypes.PATRIMONIO.INCORPORACAO.CADASTRAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacaoSalva),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
            }
            wrapper = shallowMount(IncorporacaoListagem, {
                localVue,
                router,
                stubs: stubs,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}}),
            })

            const item = {
                reconhecimento: {empenho: true},
            }
            const resultado = await wrapper.vm.verificaSeCamposObrigatoriosPreenchidos(item)
            await flushPromises()

            expect(resultado).toEqual(false)
        })

        it('Deve retornar false se empenho obrigatório e não houver nenhum empenho cadastrado', async () => {
            const empenhos = {items: []}
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES]: jest.fn().mockReturnValue(incorporacoes),
                [actionTypes.PATRIMONIO.INCORPORACAO.CADASTRAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacaoSalva),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
            }
            wrapper = shallowMount(IncorporacaoListagem, {
                localVue,
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}}),
            })

            const item = {}
            const resultado = await wrapper.vm.verificaSeCamposObrigatoriosPreenchidos(item)
            await flushPromises()

            expect(resultado).toEqual(false)
        })

        it('Deve retornar true se empenho não obrigatório e não houver nenhum empenho cadastrado', async () => {
            const empenhos = {items: []}
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES]: jest.fn().mockReturnValue(incorporacoes),
                [actionTypes.PATRIMONIO.INCORPORACAO.CADASTRAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacaoSalva),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
            }
            wrapper = shallowMount(IncorporacaoListagem, {
                localVue,
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}}),
            })

            const item = {
                reconhecimento: {empenho: false},
            }
            const resultado = await wrapper.vm.verificaSeCamposObrigatoriosPreenchidos(item)
            await flushPromises()

            expect(resultado).toEqual(true)
        })

        it('Deve redirecionar para VisualizarRegistroIncorporacao', () => {
            wrapper = shallowMount(IncorporacaoListagem, {
                localVue,
                router,
                store,
            })
            flushPromises()
            wrapper.vm.verificarRotaVisualizacaoRegistro(1)
            flushPromises()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({
                name: 'VisualizarRegistroIncorporacao',
                params: {incorporacaoId: 1},
            })
        })

        it('Deve redirecionar para VisualizarRegistroIncorporacaoVisualizacao', () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            wrapper = shallowMount(IncorporacaoListagem, {
                localVue,
                router,
                store,
            })
            flushPromises()
            wrapper.vm.verificarRotaVisualizacaoRegistro(1)
            flushPromises()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({
                name: 'VisualizarRegistroIncorporacaoVisualizacao',
                params: {incorporacaoId: 1},
            })
        })

        it('Deve redirecionar para IncorporacaoDocumentosEdicao', () => {
            wrapper = shallowMount(IncorporacaoListagem, {
                localVue,
                router,
                store,
            })
            flushPromises()
            wrapper.vm.verificarRotaIncorproacaoDocumentos(1)
            flushPromises()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({
                name: 'IncorporacaoDocumentosEdicao',
                params: {incorporacaoId: 1},
            })
        })

        it('Deve redirecionar para VisualizarIncorporacaoDocumentos', () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            wrapper = shallowMount(IncorporacaoListagem, {
                localVue,
                router,
                store,
            })
            flushPromises()
            wrapper.vm.verificarRotaIncorproacaoDocumentos(1)
            flushPromises()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({
                name: 'VisualizarIncorporacaoDocumentos',
                params: {incorporacaoId: 1},
            })
        })

        it('Deve redirecionar para EditarIncorporacao', () => {
            wrapper = shallowMount(IncorporacaoListagem, {
                localVue,
                router,
                store,
            })
            flushPromises()
            wrapper.vm.verificarRotaIncorporacao(1)
            flushPromises()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'EditarIncorporacao', params: {incorporacaoId: 1}})
        })

        it('Deve redirecionar para VisualizarIncorporacao', () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            wrapper = shallowMount(IncorporacaoListagem, {
                localVue,
                router,
                store,
            })
            flushPromises()
            wrapper.vm.verificarRotaIncorporacao(1)
            flushPromises()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarIncorporacao', params: {incorporacaoId: 1}})
        })

        it('Deve retornar false quando não houver item', async () => {
            wrapper = shallowMount(IncorporacaoListagem, {
                localVue,
                router,
                store,
            })
            await flushPromises()
            expect(await wrapper.vm.verificaSeCamposObrigatoriosPreenchidos(null)).toEqual(false)
        })

        it('Deve verificar ordenação', async () => {
            wrapper = shallowMount(IncorporacaoListagem, {
                localVue,
                router,
                store,
            })
            await flushPromises()
            const paginacao = {defaultSortBy: 'abc'}
            wrapper.vm.todasIncorporacoes.paginacao.sortBy[0] = null
            wrapper.vm.verificarOrdenacao(paginacao)
            await flushPromises()

            expect(wrapper.vm.todasIncorporacoes.paginacao.sortBy[0]).toEqual('abc')
        })
    })
})
