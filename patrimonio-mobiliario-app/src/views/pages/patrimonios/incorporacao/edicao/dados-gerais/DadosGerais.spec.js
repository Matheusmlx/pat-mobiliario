import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {mount} from '@vue/test-utils'
import DadosGerais from './DadosGerais'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('DadosGerais.vue', () => {

    let wrapper, actions, localVue, mutations, state, router, vuetify, errors, $validator

    jest.setTimeout(10000)

    const setores = {
        items: [{id: 1, nome: 'Divisão de Patrimônio e Almoxarifado'}]
    }

    const fundos = {
        items: [
            {id: 1, nome: 'fundo1'},
            {id: 2, nome: 'fundo2'}
        ]
    }

    const orgaos = {
        items: [
            {
                id: 1,
                nome: 'Defensoria Pública Geral do Estado'
            },
            {
                id: 2,
                nome: 'Agência Estadual de Metrologia'
            }
        ],
        totalItems: 2
    }

    const fornecedores = {
        items: [
            {
                id: 1,
                nome: 'Nubank S.A',
                cpfCnpj: '46118371000173'
            },
            {
                id: 2,
                nome: 'Fortaleza L.T.D.A',
                cpfCnpj: ''
            }
        ],
        totalItems: 1
    }

    const incorporacao = {
        id: 1,
        reconhecimento: {
            id: 5,
            nome: 'Reconhecimento',
            situacao: 'INATIVO',
            execucaoOrcamentaria: true,
            notaFiscal: true,
            empenho: true
        },
        numProcesso: '',
        fornecedor: {id: 5, nome: 'Fornecedor', cpfCnpj: '18236120000158', situacao: 'INATIVO'},
        numeroNota: '',
        dataNota: '',
        valorNota: '',
        orgao: {id: 139, descricao: 'Orgao', situacao: 'INATIVO'},
        empenho: '',
        setor: {id: 4233, descricao: 'Setor', situacao: 'INATIVO'},
        dataRecebimento: '',
        convenio: 1,
        fundo: '',
        situacao: '',
    }

    const convenio = {
        id: 1,
        numero: '095848',
        nome: 'Caixa Seguradora de Bens Social',
        concedente: 'Bradesco Seguros',
        fonteRecurso: 'Aquisição de protótipos',
        situacao: 'INATIVO',
        dataHoraInicial: '2020-07-15T23:00:00.000-0400',
        dataHoraFinal: '2020-07-16T23:00:00.000-0400'
    }

    const convenioData = {
        'items':
            [
                {
                    id: 1,
                    numero: '095848',
                    nome: 'Caixa Seguradora de Bens Social',
                    concedente: 'Bradesco Seguros',
                    fonteRecurso: 'Aquisição de protótipos',
                    situacao: 'INATIVO',
                    dataHoraInicial: '2020-07-15T23:00:00.000-0400',
                    dataHoraFinal: '2020-07-16T23:00:00.000-0400'
                },
                {
                    id: 1,
                    numero: '045678',
                    nome: 'AZ Tecnologia',
                    concedente: 'Bradesco Seguros',
                    fonteRecurso: 'Aquisição de protótipos',
                    situacao: 'ATIVO',
                    dataHoraInicial: '2020-07-15T23:00:00.000-0400',
                    dataHoraFinal: '2020-07-16T23:00:00.000-0400'
                }
            ],
        'paginas': 1,
        'totalItens': 2
    }

    const reconhecimentos = {
        items: [
            {
                nome: 'Compra',
                id: 1,
                situacao: 'ATIVO',
                execucaoOrcamentaria: 'SIM',
                notaFiscal: true,
                empenho: false
            },
            {
                nome: 'Contrato',
                id: 2,
                situacao: 'INATIVO',
                execucaoOrcamentaria: 'NAO',
                notaFiscal: false,
                empenho: false
            },
            {
                nome: 'Doação',
                id: 3,
                situacao: 'ATIVO',
                execucaoOrcamentaria: 'SIM',
                notaFiscal: true,
                empenho: true
            },
            {
                nome: 'Compra',
                id: 4,
                situacao: 'INATIVO',
                execucaoOrcamentaria: 'NAO',
                notaFiscal: false,
                empenho: true
            }
        ]
    }

    const empenhos = {
        items: [
            {id: 0, numero: '1234', data: '2020-07-15T23:00:00.000-0400', valor: 100, incorporacaoId: 1}
        ]
    }

    const comodantes = {
        items: [
            {
                id: 1,
                nome: 'Alan'
            },
            {
                id: 2,
                nome: 'José'
            }
        ]
    }

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
            getObrigatorioRotulosPersonalizados: () => jest.fn(),
            getTooltipRotulosPersonalizados: () => jest.fn(),
            getIncorporacaoValidado: () => jest.fn().mockReturnValue(true)
        }
    }

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    router = {
        routes: [],
        replace: jest.fn(),
        init: jest.fn(),
        history: {
            current: {
                path: '/incorporacao/:id/editarIncorporacao',
                params: {
                    id: incorporacao.id,
                    incorporacaoId: 1
                }
            }
        }
    }

    const moment = () => ({
        format: jest.fn(),
        tz: jest.fn().mockReturnValue({
            format: jest.fn().mockReturnValue('-04:00')
        }),
        utcOffset: jest.fn().mockReturnValue({
            format: (dateTime) => dateTime
        })
    })

    beforeEach(async () => {
        state = {
            reconhecimento: {
                todosReconhecimentos: {
                    filtros: {
                        conteudo: {
                            default: null,
                            label: 'Pesquisa',
                            value: ''
                        }
                    },
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['codigo'],
                        sortDesc: [false]
                    }
                }
            },
            incorporacao: {
                resultadoBuscaTodasIncorporacoes: {
                    incorporacao: {
                        tipo: null,
                    },
                    filtros: {
                        conteudo: {
                            default: null,
                            label: 'Pesquisa',
                            value: ''
                        }
                    },
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['situacao'],
                        sortDesc: [false]
                    }
                },
                rota: {
                    origem: 'IncorporacaoListagem'
                }
            },
            loki: {
                user: {
                    authorities: [{name: 'Mobiliario.Retroativo', hasAccess: true}, {
                        name: 'Mobiliario.Normal',
                        hasAccess: true
                    }]
                }
            }
        }

        errors = {
            collect: jest.fn(),
            clear: jest.fn()
        }

        $validator = {
            validateAll: jest.fn().mockReturnValue(true),
            errors: {
                items: []
            }
        }


        actions = {
            [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
            [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
            [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
            [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
            [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
            [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
            [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
            [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
            [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
            [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
            [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos),
            [actionTypes.COMODATO.BUSCAR_COMODANTES_DINAMICAMENTE]: jest.fn().mockReturnValue(comodantes),
            [actionTypes.PATRIMONIO.INCORPORACAO.DELETAR_INCORPORACAO]: jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_AUTO_SAVING]: jest.fn()
        }
    })

    const defaultMount = () => mount(DadosGerais, {
        localVue,
        vuetify,
        mocks: {$validator, errors, moment},
        router,
        store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
    })

    describe('Mounted', () => {

        it('Deve montar dados gerais', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID].mock.calls[0][1]).toEqual(incorporacao.convenio)
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_FUNDOS]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_FUNDOS].mock.calls[0][1]).toEqual(incorporacao.orgao.id)
            expect(actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()

        })

        it('Deve Colocar comodante retornado na busca da incorporação por id no array de comodantes', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.dadosDeEntrada.comodante = {id: 3, nome: 'Irineu'}
            wrapper.vm.comodantes = []
            wrapper.vm.trataCampoComodante()

            expect(wrapper.vm.comodantes[0]).toEqual(wrapper.vm.dadosDeEntrada.comodante)
        })
    })

    describe('computed', () => {

        it('deve retornar true quando o usuário logado tem permissão retroativo', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.verificaPermissaoRetroativa).toEqual(true)
        })

        it('deve retornar false quando o usuário logado não tem permissão retroativo', async () => {
            state.loki.user.authorities = [{name: 'Mobiliario.Retroativo', hasAccess: false}]
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.verificaPermissaoRetroativa).toEqual(false)
        })

        it('deve retornar data com primeiro dia do mes atual e ano atual.', async () => {
            wrapper = defaultMount()
            await flushPromises()

            const date = new Date()
            expect(wrapper.vm.dataPermitida).toEqual(new Date(date.getFullYear(), date.getMonth(), 1))
        })

        it('podeContinuar deve ser false se empenhos obrigatórios e pelo menos um dos empenhos não estiver com campo valorEmpenho preenchido', async () => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.empenhos = [{numeroEmpenho: '1', dataEmpenho: '10/02/2021', valorEmpenho: ''}]
            wrapper.vm.empenhoObrigatorio = true
            expect(wrapper.vm.podeContinuar).toEqual(false)
        })

        it('podeContinuar deve ser false se empenhos obrigatórios e pelo menos um dos empenhos não estiver com campo valorEmpenho 0', async () => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.empenhos = [{numeroEmpenho: '1', dataEmpenho: '10/02/2021', valorEmpenho: 0}]
            wrapper.vm.empenhoObrigatorio = true
            expect(wrapper.vm.podeContinuar).toEqual(false)
        })

        it('podeContinuar deve ser false se empenhos obrigatórios e pelo menos um dos empenhos não estiver com campo dataEmpenho preenchido', async () => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.empenhos = [{numeroEmpenho: '1', dataEmpenho: '', valorEmpenho: 12}]
            wrapper.vm.empenhoObrigatorio = true
            expect(wrapper.vm.podeContinuar).toEqual(false)
        })

        it('podeContinuar deve ser false se empenhos obrigatórios e pelo menos um dos empenhos não estiver com campo numeroEmpenho preenchido', async () => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.empenhos = [{numeroEmpenho: '', dataEmpenho: '10/02/2021', valorEmpenho: 12}]
            wrapper.vm.empenhoObrigatorio = true
            expect(wrapper.vm.podeContinuar).toEqual(false)
        })

        it('podeContinuar deve ser true se empenhos obrigatórios e todos campos de empenho obrigatorios preenchidos e todos campos obrigatórios da incorporação preenchidos', async () => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.empenhos = [{numeroEmpenho: '1', dataEmpenho: '10/02/2021', valorEmpenho: 12}]
            wrapper.vm.empenhoObrigatorio = true
            expect(wrapper.vm.podeContinuar).toEqual(true)
        })

        it('podeContinuar deve ser false se campos obrigatorios da incorporação não estiverem preenchidos', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getNomeRotulosPersonalizados: () => jest.fn(),
                    getObrigatorioRotulosPersonalizados: () => jest.fn(),
                    getTooltipRotulosPersonalizados: () => jest.fn(),
                    getIncorporacaoValidado: () => jest.fn().mockReturnValue(false)
                }
            }
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            flushPromises()

            wrapper.setData({
                empenhoObrigatorio: false
            })

            expect(wrapper.vm.podeContinuar).toEqual(false)
        })
    })

    describe('watch', () => {

        it('deve buscar convênio dinamicamente', async () => {
            wrapper = defaultMount()
            wrapper.setData({
                buscarConvenioDinamicamente: 'AZ'
            })
            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE].mock.calls[0][1]).toEqual('AZ')
        })

        it('deve não buscar convênio dinamicamente se valor for null', async () => {
            wrapper = defaultMount()
            wrapper.setData({
                buscarConvenioDinamicamente: 'a'
            })
            await flushPromises()
            wrapper.vm.buscarConvenioDinamicamente = null
            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]).not.toHaveBeenCalled()
        })

        it('deve não buscar convênio dinamicamente quando encontrar erros', async () => {
            actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE] = jest.fn().mockRejectedValueOnce(new Error())
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            wrapper.setData({
                buscarConvenioDinamicamente: 'AZ'
            })
            flushPromises()
            wrapper.vm.buscarConvenioDinamicamente = null
            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]).not.toHaveBeenCalled()
        })

        it('deve buscar fornecedor dinamicamente', async () => {
            actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID] = jest.fn().mockReturnValue({reconhecimento: {id: 5}})
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            await flushPromises()
            wrapper.setData({
                buscaFornecedorDinamicamente: '00'
            })
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_FORNECEDORES]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_FORNECEDORES].mock.calls[0][1]).toEqual('00')
        })

        it('deve não buscar fornecedor dinamicamente quando encontrar erros', async () => {
            actions[actionTypes.COMUM.BUSCAR_FORNECEDORES] = jest.fn().mockRejectedValueOnce(new Error())
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            wrapper.setData({
                buscaFornecedorDinamicamente: '00'
            })

            wrapper.vm.buscaFornecedorDinamicamente = null
            flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_FORNECEDORES]).not.toHaveBeenCalled()
        })

        it('Deve buscar comodantes dinamicamente', async () => {
            wrapper = defaultMount()
            wrapper.setData({
                buscarComodantesDinamicamente: 'AZ'
            })
            await flushPromises()
            expect(actions[actionTypes.COMODATO.BUSCAR_COMODANTES_DINAMICAMENTE]).toHaveBeenCalled()
            expect(actions[actionTypes.COMODATO.BUSCAR_COMODANTES_DINAMICAMENTE].mock.calls[0][1]).toEqual('AZ')
        })

        it('Deve falhar ao buscar comodante dinamicamente se valor for null', async () => {
            wrapper = defaultMount()
            wrapper.setData({
                buscarComodantesDinamicamente: 'a'
            })
            await flushPromises()
            wrapper.vm.buscarComodantesDinamicamente = null
            await flushPromises()
            expect(actions[actionTypes.COMODATO.BUSCAR_COMODANTES_DINAMICAMENTE]).not.toHaveBeenCalled()
        })
    })

    describe('filter', () => {
        it('deve retornar o nome do fornecedor quando passar o id do fornecedor', () => {
            wrapper = defaultMount()

            const nome = wrapper.vm.$options.filters.fornecedorFilter(1, fornecedores.items)

            expect(nome).toBe('Nubank S.A')
        })

        it('deve retornar o nome do fornecedor quando passar objeto', () => {
            wrapper = defaultMount()

            const fornecedor = fornecedores.items[1]

            const nome = wrapper.vm.$options.filters.fornecedorFilter(fornecedor, fornecedores.items)

            expect(nome).toBe('Fortaleza L.T.D.A')
        })

        it('deve retornar string vazia quando fornecedor não encontrado', () => {
            wrapper = defaultMount()

            const nome = wrapper.vm.$options.filters.fornecedorFilter(3, fornecedores.items)

            expect(nome).toBe('')
        })
    })

    describe('methods', () => {

        it('deve adicionar um novo empenho a lista', () => {
            wrapper = defaultMount()

            wrapper.vm.adicionarNovoEmpenho()
            expect(wrapper.vm.empenhos[0]).toEqual({incorporacaoId: 1})
            expect(wrapper.vm.empenhos[1]).toEqual({incorporacaoId: 1})
        })

        it('deve remover um empenho do array', async () => {
            wrapper = defaultMount()
            await flushPromises()
            const empenhosInicial = wrapper.vm.empenhos.length

            wrapper.vm.removerEmpenho(empenhos.items[0].id)
            expect(wrapper.vm.empenhos.length).toEqual(empenhosInicial - 1)
        })

        it('deve buscar empenhos', async () => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.buscaEmpenhos()
            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]).toHaveBeenCalled()
        })

        it('deve buscar convênio por id', async () => {
            incorporacao.convenio = 1
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.buscarConvenio()

            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID].mock.calls[0][1]).toEqual(wrapper.vm.dadosDeEntrada.convenio)
        })

        it('deve buscar todos os reconhecimentos', async () => {
            wrapper = defaultMount()
            flushPromises()
            wrapper.vm.buscarReconhecimentos()

            flushPromises()

            expect(mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE].mock.calls[0][1]).toEqual('Carregando...')
            expect(actions[actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]).toHaveBeenCalled()
        })

        it('deve editar a incorporação', async () => {
            wrapper = defaultMount()
            flushPromises()
            wrapper.vm.editarIncorporacao()

            await flushPromises()
            expect(mutations[mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]).toHaveBeenCalled()
            expect(wrapper.emitted().habilitaPasso2).toBeTruthy()
        })

        it('deve mostrar mensagem de erro e buscar incorporação ao ocorrer erro na edição', async () => {
            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockRejectedValueOnce({response: {data: {message: 'erro'}}}),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }

            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            await flushPromises()
            wrapper.vm.editarIncorporacao()

            await flushPromises()
            expect(mutations[mutationTypes.LOKI.DISABLE_AUTO_SAVING]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]).toHaveBeenCalled()
        })

        it('deve validar o valor digitado no auto-complete', async () => {
            wrapper = defaultMount()

             flushPromises()
             expect(wrapper.vm.filtroComboAutoComplete(convenioData.items[1], 'Caixa')).toBe(false)
        })

        it('deve validar o valor digitado no auto-complete do fornecedor', async () => {
            wrapper = defaultMount()

             flushPromises()
             expect(wrapper.vm.filtroAutocompleteFornecedores(fornecedores.items[0], '11')).toBeTruthy()
             expect(wrapper.vm.filtroAutocompleteFornecedores(fornecedores.items[1], 'Fortaleza')).toBeTruthy()
        })

        it('deve fazer fornecedorSelecionado true se fornecedor diferente de null', async () => {
            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.dadosDeEntrada.fornecedor = 1
            wrapper.vm.fornecedorSelecionado = false
            wrapper.vm.verificarSeFornecedorSelecionado()

            expect(wrapper.vm.fornecedorSelecionado).toEqual(true)
        })

        it('deve fazer fornecedorSelecionado false se fornecedor null', async () => {
            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.dadosDeEntrada.fornecedor = null
            wrapper.vm.fornecedorSelecionado = false
            wrapper.vm.verificarSeFornecedorSelecionado()

            expect(wrapper.vm.fornecedorSelecionado).toEqual(false)
        })

        it('deve atualizar o valor da nota de acordo com o valor recebido por parâmetro', async () => {
            wrapper = defaultMount()
            flushPromises()
            const valor = 12.99
            wrapper.vm.atualizarValorInserido(valor)

            expect(wrapper.vm.dadosDeEntrada.valorNota).toEqual(12.99)
        })

        it('deve formatar o CNPJ do fornecedor', async () => {
            wrapper = defaultMount()
            flushPromises()
            const cnpj = '52429104000110'
            const cnpjFormatado = '52.429.104/0001-10'

            expect(wrapper.vm.formataCnpj(cnpj)).toEqual(cnpjFormatado)
        })

        it('deve emitir habilitaPasso2 se podeContinuar true', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getNomeRotulosPersonalizados: () => jest.fn(),
                    getObrigatorioRotulosPersonalizados: () => jest.fn(),
                    getTooltipRotulosPersonalizados: () => jest.fn(),
                    getIncorporacaoValidado: () => jest.fn().mockReturnValue(true)
                }
            }
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {$validator, errors},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            wrapper.vm.verificarSePodeProsseguir()

            expect(wrapper.vm.podeContinuar).toEqual(true)
            expect(wrapper.emitted().habilitaPasso2).toBeTruthy()
        })

        it('deve emitir desabilitaPasso2 se podeContinuar false', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getNomeRotulosPersonalizados: () => jest.fn(),
                    getObrigatorioRotulosPersonalizados: () => jest.fn(),
                    getTooltipRotulosPersonalizados: () => jest.fn(),
                    getIncorporacaoValidado: () => jest.fn().mockReturnValue(false)
                }
            }
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {$validator, errors},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })

            wrapper.vm.verificarSePodeProsseguir()

            expect(wrapper.vm.podeContinuar).toEqual(false)
            expect(wrapper.emitted().desabilitaPasso2).toBeTruthy()
        })

        it('deve retornar falso se empenho sem data e com empenhoObrigatorio verdadeiro', async () => {
            wrapper = defaultMount()
            flushPromises()

            wrapper.setData({
                empenhoObrigatorio: true,
                empenhos: [{
                    numeroEmpenho: '1',
                    dataEmpenho: '',
                    valorEmpenho: 12.00
                }]
            })

            expect(wrapper.vm.verificarSeTodosEmpenhosObrigatoriosEstaoPreenchidos()).toEqual(false)
        })

        it('deve retornar falso se empenho sem valor e com empenhoObrigatorio verdadeiro', async () => {
            wrapper = defaultMount()
            flushPromises()

            wrapper.setData({
                empenhoObrigatorio: true,
                empenhos: [{
                    numeroEmpenho: '1',
                    dataEmpenho: '12/12/2020',
                    valorEmpenho: 0
                }]
            })

            expect(wrapper.vm.verificarSeTodosEmpenhosObrigatoriosEstaoPreenchidos()).toEqual(false)
        })

        it('não deve validar empenhos se empenhoObrigatorio não for verdadeiro', async () => {
            wrapper = defaultMount()
            flushPromises()

            wrapper.setData({
                empenhoObrigatorio: false
            })

            expect(wrapper.vm.podeContinuar).toEqual(true)
        })

        it('deve buscar setores após selecionar orgão', async () => {
            wrapper = defaultMount()
            await flushPromises
            wrapper.vm.dadosDeEntrada.orgao = 140
            wrapper.vm.selecionaOrgao(140)
            await flushPromises
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO].mock.calls[0][1]).toEqual(140)
        })

        it('deve inserir reconhecimento desabilitado para seleção no select de reconhecimentos se inativo e mostrar icone inativo', async () => {
            const incorporacao = {
                id: 1,
                reconhecimento: {
                    id: 6,
                    nome: 'Reconhecimento',
                    situacao: 'INATIVO',
                    execucaoOrcamentaria: true,
                    notaFiscal: true,
                    empenho: true
                },
                numProcesso: '',
                fornecedor: {id: 5, nome: 'Fornecedor', cpfCnpj: '18236120000158', situacao: 'INATIVO'},
                numeroNota: '',
                dataNota: '',
                valorNota: '',
                orgao: {id: 139, descricao: 'Orgao', situacao: 'INATIVO'},
                empenho: '',
                setor: {id: 4233, descricao: 'Setor', situacao: 'INATIVO'},
                dataRecebimento: '',
                convenio: '',
                fundo: '',
                situacao: '',
            }

            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }

            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            await flushPromises()

            expect(wrapper.vm.reconhecimentos[0].id).toEqual(6)
            expect(wrapper.vm.reconhecimentos[0].nome).toEqual('Reconhecimento')
            expect(wrapper.vm.reconhecimentos[0].disabled).toEqual(true)
            expect(wrapper.vm.reconhecimentos[0].situacao).toEqual('INATIVO')
            expect(wrapper.vm.reconhecimentoInativo).toEqual(true)
            expect(wrapper.vm.dadosDeEntrada.reconhecimento.id).toEqual(6)
        })

        it('deve inserir fornecedor desabilitado para seleção no select de fornecedores se inativo e mostrar icone inativo', async () => {
            const incorporacao = {
                id: 1,
                reconhecimento: {
                    id: 6,
                    nome: 'Reconhecimento',
                    situacao: 'INATIVO',
                    execucaoOrcamentaria: true,
                    notaFiscal: true,
                    empenho: true
                },
                numProcesso: '',
                fornecedor: {id: 5, nome: 'Fornecedor', cpfCnpj: '18236120000158', situacao: 'INATIVO'},
                numeroNota: '',
                dataNota: '',
                valorNota: '',
                orgao: {id: 139, descricao: 'Orgao', situacao: 'INATIVO'},
                empenho: '',
                setor: {id: 4233, descricao: 'Setor', situacao: 'INATIVO'},
                dataRecebimento: '',
                convenio: '',
                fundo: '',
                situacao: '',
            }

            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }

            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            await flushPromises()

            expect(wrapper.vm.fornecedores[0].id).toEqual(5)
            expect(wrapper.vm.fornecedores[0].nome).toEqual('Fornecedor')
            expect(wrapper.vm.fornecedores[0].disabled).toEqual(true)
            expect(wrapper.vm.fornecedores[0].situacao).toEqual('INATIVO')
            expect(wrapper.vm.fornecedorInativo).toEqual(true)
            expect(wrapper.vm.dadosDeEntrada.fornecedor.id).toEqual(5)
        })

        it('deve inserir orgao desabilitado para seleção no select de orgaos se inativo e mostrar icone inativo', async () => {
            const incorporacao = {
                id: 1,
                reconhecimento: {
                    id: 6,
                    nome: 'Reconhecimento',
                    situacao: 'INATIVO',
                    execucaoOrcamentaria: true,
                    notaFiscal: true,
                    empenho: true
                },
                numProcesso: '',
                fornecedor: {id: 5, nome: 'Fornecedor', cpfCnpj: '18236120000158', situacao: 'INATIVO'},
                numeroNota: '',
                dataNota: '',
                valorNota: '',
                orgao: {id: 139, descricao: 'Orgao', situacao: 'INATIVO'},
                empenho: '',
                setor: {id: 4233, descricao: 'Setor', situacao: 'INATIVO'},
                dataRecebimento: '',
                convenio: '',
                fundo: '',
                situacao: '',
            }

            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }

            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            await flushPromises()

            expect(wrapper.vm.orgaos[0].id).toEqual(139)
            expect(wrapper.vm.orgaos[0].descricao).toEqual('Orgao')
            expect(wrapper.vm.orgaos[0].disabled).toEqual(true)
            expect(wrapper.vm.orgaos[0].situacao).toEqual('INATIVO')
            expect(wrapper.vm.orgaoInativo).toEqual(true)
            expect(wrapper.vm.dadosDeEntrada.orgao.id).toEqual(139)
        })

        it('deve inserir setor desabilitado para seleção no select de setores se inativo e mostrar icone inativo', async () => {
            const incorporacao = {
                id: 1,
                reconhecimento: {
                    id: 6,
                    nome: 'Reconhecimento',
                    situacao: 'INATIVO',
                    execucaoOrcamentaria: true,
                    notaFiscal: true,
                    empenho: true
                },
                numProcesso: '',
                fornecedor: {id: 5, nome: 'Fornecedor', cpfCnpj: '18236120000158', situacao: 'INATIVO'},
                numeroNota: '',
                dataNota: '',
                valorNota: '',
                orgao: {id: 139, descricao: 'Orgao', situacao: 'INATIVO'},
                empenho: '',
                setor: {id: 4233, descricao: 'Setor', situacao: 'INATIVO'},
                dataRecebimento: '',
                convenio: '',
                fundo: '',
                situacao: '',
            }

            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }

            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            await flushPromises()

            expect(wrapper.vm.setores[0].id).toEqual(4233)
            expect(wrapper.vm.setores[0].descricao).toEqual('Setor')
            expect(wrapper.vm.setores[0].disabled).toEqual(true)
            expect(wrapper.vm.setores[0].situacao).toEqual('INATIVO')
            expect(wrapper.vm.setorInativo).toEqual(true)
            expect(wrapper.vm.dadosDeEntrada.setor.id).toEqual(4233)
        })

        it('deve ocultar icone de reconhecimento inativo se o reconhecimento for ativo', async () => {
            const incorporacao = {
                id: 1,
                reconhecimento: {
                    id: 5,
                    nome: 'Reconhecimento',
                    situacao: 'ATIVO',
                    execucaoOrcamentaria: true,
                    notaFiscal: true,
                    empenho: true
                },
                numProcesso: '',
                fornecedor: {id: 5, nome: 'Fornecedor', cpfCnpj: '18236120000158', situacao: 'INATIVO'},
                numeroNota: '',
                dataNota: '',
                valorNota: '',
                orgao: {id: 139, descricao: 'Orgao', situacao: 'INATIVO'},
                empenho: '',
                setor: {id: 4233, descricao: 'Setor', situacao: 'INATIVO'},
                dataRecebimento: '',
                convenio: '',
                fundo: '',
                situacao: '',
            }
            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            flushPromises()

            expect(wrapper.vm.reconhecimentoInativo).toEqual(false)
        })

        it('deve ocultar icone de fornecedor inativo se o fornecedor for ativo', async () => {
            const incorporacao = {
                id: 1,
                reconhecimento: {
                    id: 5,
                    nome: 'Reconhecimento',
                    situacao: 'INATIVO',
                    execucaoOrcamentaria: true,
                    notaFiscal: true,
                    empenho: true
                },
                numProcesso: '',
                fornecedor: {id: 5, nome: 'Fornecedor', cpfCnpj: '18236120000158', situacao: 'ATIVO'},
                numeroNota: '',
                dataNota: '',
                valorNota: '',
                orgao: {id: 139, descricao: 'Orgao', situacao: 'INATIVO'},
                empenho: '',
                setor: {id: 4233, descricao: 'Setor', situacao: 'INATIVO'},
                dataRecebimento: '',
                convenio: '',
                fundo: '',
                situacao: '',
            }
            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }
            wrapper = defaultMount()

            await flushPromises()

            expect(wrapper.vm.fornecedorInativo).toEqual(false)
        })

        it('deve ocultar icone de orgao inativo se o orgao for ativo', async () => {
            const incorporacao = {
                id: 1,
                reconhecimento: {
                    id: 5,
                    nome: 'Reconhecimento',
                    situacao: 'INATIVO',
                    execucaoOrcamentaria: true,
                    notaFiscal: true,
                    empenho: true
                },
                numProcesso: '',
                fornecedor: {id: 5, nome: 'Fornecedor', cpfCnpj: '18236120000158', situacao: 'INATIVO'},
                numeroNota: '',
                dataNota: '',
                valorNota: '',
                orgao: {id: 139, descricao: 'Orgao', situacao: 'ATIVO'},
                empenho: '',
                setor: {id: 4233, descricao: 'Setor', situacao: 'INATIVO'},
                dataRecebimento: '',
                convenio: '',
                fundo: '',
                situacao: '',
            }
            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            flushPromises()

            expect(wrapper.vm.orgaoInativo).toEqual(false)
        })

        it('deve ocultar icone de setor inativo se o setor for ativo', async () => {
            const incorporacao = {
                id: 1,
                reconhecimento: {
                    id: 5,
                    nome: 'Reconhecimento',
                    situacao: 'INATIVO',
                    execucaoOrcamentaria: true,
                    notaFiscal: true,
                    empenho: true
                },
                numProcesso: '',
                fornecedor: {id: 5, nome: 'Fornecedor', cpfCnpj: '18236120000158', situacao: 'INATIVO'},
                numeroNota: '',
                dataNota: '',
                valorNota: '',
                orgao: {id: 139, descricao: 'Orgao', situacao: 'INATIVO'},
                empenho: '',
                setor: {id: 4233, descricao: 'Setor', situacao: 'ATIVO'},
                dataRecebimento: '',
                convenio: '',
                fundo: '',
                situacao: '',
            }
            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            flushPromises()

            expect(wrapper.vm.setorInativo).toEqual(false)
        })

        it('deve selecionar o setor se houver apenas um disponível', async () => {
            const incorporacao = {
                id: 1,
                reconhecimento: {
                    id: 5,
                    nome: 'Reconhecimento',
                    situacao: 'INATIVO',
                    execucaoOrcamentaria: true,
                    notaFiscal: true,
                    empenho: true
                },
                orgao: {id: 139, descricao: 'Orgao', situacao: 'INATIVO'},
                setor: null,
            }
            const setores = {items: [{id: 1}]}
            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            flushPromises()

            wrapper.vm.selecionaSetorSeArrayConterApenasUmSetor(setores.items)
            flushPromises()
            expect(wrapper.vm.dadosDeEntrada.setor).toEqual(setores.items[0].id)
        })

        it('deve selecionar o órgão se houver apenas um disponível', () => {
            const incorporacao = {
                id: 1,
                reconhecimento: {
                    id: 5,
                    nome: 'Reconhecimento',
                    situacao: 'INATIVO',
                    execucaoOrcamentaria: true,
                    notaFiscal: true,
                    empenho: true
                },
                orgao: null,
                setor: null,
            }
            const orgaos = {
                items: [{id: 1}],
                totalItems: 1
            }
            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            flushPromises()

            wrapper.vm.selecionaOrgaoSeArrayConterApenasUmOrgao(orgaos.items)

            expect(wrapper.vm.dadosDeEntrada.orgao).toEqual(orgaos.items[0].id)
        })

        it('deve selecionar o fundo se houver apenas um disponível', () => {
            const incorporacao = {
                id: 1,
                reconhecimento: {
                    id: 5,
                    nome: 'Reconhecimento',
                    situacao: 'INATIVO',
                    execucaoOrcamentaria: true,
                    notaFiscal: true,
                    empenho: true
                },
                orgao: null,
                setor: null,
                fundo: null
            }
            const fundos = {
                items: [{id: 1}],
                totalItems: 1
            }
            actions = {
                [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
                [actionTypes.COMUM.BUSCAR_FORNECEDORES]: jest.fn().mockReturnValue(fornecedores),
                [actionTypes.COMUM.BUSCAR_FORNECEDOR_POR_ID]: jest.fn().mockReturnValue(fornecedores.items[0]),
                [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenio),
                [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_DINAMICAMENTE]: jest.fn().mockReturnValue(convenioData),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }
            wrapper = mount(DadosGerais, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            flushPromises()

            wrapper.vm.selecionaFundoSeArrayConterApenasUmFundo(fundos.items)

            expect(wrapper.vm.dadosDeEntrada.fundo).toEqual(fundos.items[0].id)
        })

        it('Deve BuscarIncorporacao caso de alguma exceção na api na edição da incorporacao', async () => {
            wrapper = defaultMount()
            flushPromises()
            wrapper.vm.editarIncorporacao()

            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]).toHaveBeenCalled()
        })

        it('Deve setar fornecedor nulo e chamar edição da incorporação', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.removerFornecedor()

            await flushPromises()
            expect(wrapper.vm.dadosDeEntrada.fornecedor).toEqual(null)
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.ATUALIZAR_INCORPORACAO]).toHaveBeenCalled()
        })

        it('Deve deletar incorporação', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.removerIncorporacao()

            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DELETAR_INCORPORACAO]).toHaveBeenCalled()
        })

        it('Deve redirecionar para listagem da incorporação', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.tratarEventoVoltar()

            await flushPromises()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'IncorporacaoListagem'})
        })

        it('Deve redirecionar para ItensIncorporacaoListagem', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.tratarEventoContinuar()

            await flushPromises()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({
                name: 'ItensIncorporacaoListagem',
                params: {incorporacaoId: 1}
            })
        })

        it('Deve remover incorporação ao sair da página com incorporação sem reconhecimento', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.setData({
                dadosDeEntrada: {
                    id: 1,
                    reconhecimento: null
                }
            })
            const beforeRouteLeave = wrapper.vm.$options.beforeRouteLeave[0]
            let nextFun = jest.fn()

            beforeRouteLeave.call(wrapper.vm, {name: 'toObj'}, {name: 'fromObj'}, nextFun)

            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DELETAR_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DELETAR_INCORPORACAO].mock.calls[0][1]).toEqual(wrapper.vm.dadosDeEntrada.id)
        })

        it('Deve atualizar as informações com os dados atualizados', async() => {
            const dados = {
                codigo: '8',
                comodante: {id: 1, nome: 'Amanda'},
                convenio: 2,
                dataNota: '2021-03-02T00:00:00.000-0400',
                dataNotaLancamentoContabil: null,
                dataRecebimento: '2020-09-01T23:00:00.000-0400',
                fornecedor: {cpfCnpj: '06990590000123', id: 8, nome: 'Google Brasil Internet Ltda', situacao: 'ATIVO'},
                fundo: null,
                id: 7,
                naturezaDespesa: null,
                nota: '112',
                numProcesso: '1',
                numeroNotaLancamentoContabil: null,
                orgao: {descricao: 'DPGE - Defensoria Pública Geral do Estado', id: 8773, situacao: 'ATIVO'},
                origemComodato: true,
                origemConvenio: true,
                origemFundo: false,
                origemProjeto: true,
                projeto: 'teste',
                reconhecimento: {empenho: false, execucaoOrcamentaria: true, id: 5, nome: 'Compra', notaFiscal: null, situacao: 'ATIVO'},
                setor: {descricao: '1SUBDEF - Primeira Subdefensoria Pública-Geral', id: 8774, situacao: 'ATIVO'},
                situacao: 'EM_ELABORACAO',
                valorNota: null
            }

            wrapper = defaultMount()

            wrapper.vm.dadosDeEntrada = {
                id: 7,
                reconhecimento: {id: 6, nome: 'Doação', situacao: 'ATIVO'},
                fornecedor: 8,
                orgao: 8773,
                setor: 8774,
                convenio: 5
            }

            wrapper.vm.setarDadosAtualizados(dados)

            expect(wrapper.vm.dadosDeEntrada).toEqual(dados)
        })

        it('Deve setar o id da incorporação ao montar componente', async() => {
            wrapper = defaultMount()

            expect(wrapper.vm.incorporacaoId).toBe(1)
        })

    })
})
