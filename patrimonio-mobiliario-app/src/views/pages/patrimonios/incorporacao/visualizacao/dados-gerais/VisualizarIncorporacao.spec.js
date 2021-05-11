import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {mount} from '@vue/test-utils'
import VisualizarIncorporacao from './VisualizarIncorporacao'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('VisualizarIncorporacao.vue', () => {

    let wrapper, actions, localVue, mutations, state, router, vuetify, errors, $validator

    const setores = {
        items: [{id: 1}]
    }

    const fundos = {
        items: [{id: 1}, {id: 2}]
    }

    const orgaos = {
        items: [
            {
                id: 1
            },
            {
                id: 2
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



    const reconhecimentos = {
        items: [
            {
                nome: 'Compra abc',
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
        push: jest.fn(),
        init: jest.fn(),
        history: {
            current: {
                name: 'VisualizarIncorporacao',
                params: {
                    id: incorporacao.id,
                    incorporacaoId: 1
                }
            }
        }
    }

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
                        name: 'Mobiliario.Consulta',
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
            [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
            [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
            [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
            [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos),
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_AUTO_SAVING]: jest.fn()
        }
    })

    const defaultMount = () => mount(VisualizarIncorporacao, {
        localVue,
        vuetify,
        mocks: {$validator, errors},
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
            wrapper = mount(VisualizarIncorporacao, {
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

    describe('methods', () => {
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

        it('deve validar o valor digitado no auto-complete', async () => {
            wrapper = defaultMount()

            flushPromises()
            expect(wrapper.vm.filtroComboAutoComplete({nome: 'Compra abc'}, 'Com')).toBeTruthy()
        })

        it('deve validar o valor digitado no auto-complete do fornecedor', async () => {
            wrapper = defaultMount()

            flushPromises()
            expect(wrapper.vm.filtroAutocompleteFornecedores(fornecedores.items[0], '11')).toBeTruthy()
            expect(wrapper.vm.filtroAutocompleteFornecedores(fornecedores.items[1], 'Fortaleza')).toBeTruthy()
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
            wrapper = mount(VisualizarIncorporacao, {
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
            wrapper = mount(VisualizarIncorporacao, {
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
            wrapper.vm.selecionaOrgao()
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
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }

            wrapper = mount(VisualizarIncorporacao, {
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
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }

            wrapper = mount(VisualizarIncorporacao, {
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
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }

            wrapper = mount(VisualizarIncorporacao, {
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
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }

            wrapper = mount(VisualizarIncorporacao, {
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
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }
            wrapper = mount(VisualizarIncorporacao, {
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
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
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
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }
            wrapper = mount(VisualizarIncorporacao, {
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
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos),
                [actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_RECONHECIMENTOS_AUTO_COMPLETE]: jest.fn().mockReturnValue(reconhecimentos),
                [actionTypes.COMUM.BUSCAR_FUNDOS]: jest.fn().mockReturnValue(fundos)
            }
            wrapper = mount(VisualizarIncorporacao, {
                localVue,
                vuetify,
                mocks: {errors, $validator},
                router,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})
            })
            flushPromises()

            expect(wrapper.vm.setorInativo).toEqual(false)
        })

        it('Deve redirecionar para listagem da incorporação', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.tratarEventoVoltar()

            await flushPromises()
            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'IncorporacaoListagem'})
        })

        it('Deve redirecionar para ItensIncorporacaoListagem', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.tratarEventoContinuar()

            await flushPromises()
            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({
                name: 'ItensIncorporacaoListagem',
                params: {incorporacaoId: 1}
            })
        })
    })
})
