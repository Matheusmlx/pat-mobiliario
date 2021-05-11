import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalAdicionarIntervalos from './ModalAdicionarIntervalos'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('ModalAdicionarIntervalos', () => {

    let wrapper, store, localVue, router, actions, state, mutations, $validator

    const orgaos = {
        items: [
            {
                id: 1,
                descricao: 'descricao',
                nome: 'orgao',
                sigla: 'sigla'
            }
        ],
        totalPages: 1,
        totalElements: 1,
        totalElementsOfAllPages: 1
    }

    $validator = {
        validateAll: jest.fn().mockReturnValue(true),
        reset: jest.fn(),
        resume: jest.fn(),
        pause: jest.fn()
    }

    const errors = {
        collect() {
        },
        clear: jest.fn()
    }

    const intervalo = {numeroInicio: 1, numeroFim: 10}

    const reserva = {
        id: 1,
        orgaoId: 1,
        preenchimento: 'AUTOMATICO',
        numeroInicio: 1,
        numeroFim: 10,
        quantidadeReservada: 50,
        quantidadeRestante: 30
    }

    beforeEach(() => {

        state = {
            reserva: {
                todosOrgaos: {
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
                        sortDesc: [false],
                        sortBy: ['situacao']
                    }
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
                    name: 'ModalAdicionarIntervalos',
                    params: {id: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_TODOS_ORGAOS_PAGINADO]: jest.fn().mockReturnValue(orgaos),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]: jest.fn().mockReturnValue(reserva),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_INTERVALO]: jest.fn().mockReturnValue(intervalo),
            [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SALVAR_INTERVALO]: jest.fn()
        }

        mutations = {
            [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.SET_FILTRO_BUSCA_ORGAOS_RESERVA]: jest.fn(),
            [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.SET_PAGINACAO_BUSCA_ORGAOS_RESERVA]: jest.fn(),
            [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.RESETA_PAGE_ORGAOS_RESERVA]: jest.fn(),
        }

        store = new Vuex.Store({actions, mutations, state})
    })

    const defaultMount = () => shallowMount(ModalAdicionarIntervalos, {
        store,
        router,
        localVue,
        mocks: {errors, $validator},
    })

    describe('methods', () => {

        it('deve tratar o evento de salvar', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.tratarEventoAdicionar()

            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SALVAR_INTERVALO]).toHaveBeenCalled()
            expect(wrapper.emitted().recarregarDadosReserva).toBeTruthy()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ReservaEdicao', params: {id: 1}})
        })

        it('deve buscar a reserva por id', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.buscarReserva()

            await flushPromises()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID].mock.calls[0][1]).toEqual(wrapper.vm.$data.reservaId)
        })

        it('deve buscar todos os órgãos que o usuário tem acesso', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.buscar()

            await flushPromises()
            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.SET_FILTRO_BUSCA_ORGAOS_RESERVA]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_TODOS_ORGAOS_PAGINADO]).toHaveBeenCalled()
        })

        it('deve tratar o evento de paginar', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.tratarEventoPaginar()

            await flushPromises()
            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.SET_PAGINACAO_BUSCA_ORGAOS_RESERVA]).toHaveBeenCalled()
            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.SET_FILTRO_BUSCA_ORGAOS_RESERVA]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_TODOS_ORGAOS_PAGINADO]).toHaveBeenCalled()
        })

        it('deve resetar a pagina', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.resetaPage()

            await flushPromises()
            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.RESETA_PAGE_ORGAOS_RESERVA]).toHaveBeenCalled()
        })

        it('deve fechar o modal', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.fecharModal()

            await flushPromises()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ReservaEdicao', params: {id: 1}})
        })

        it('deve retornar os fitros', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.getFiltros()

            await flushPromises()
            expect(wrapper.vm.getFiltros()).toEqual(state.reserva.todosOrgaos.filtros)
        })

        it('deve retornar os fitros internos', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.getFiltrosInterno()

            await flushPromises()
            expect(wrapper.vm.getFiltrosInterno()).toEqual(wrapper.vm.$data.filtrosInterno)
        })

        it('deve tratar evento de remover os filtros', async () => {
            const propriedade = {conteudo: {default: null, label: 'Pesquisa', value: ''}}
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.tratarEventoRemoverFiltro(propriedade)

            await flushPromises()
            expect(wrapper.vm.filtrosInterno).toEqual(propriedade)
        })

        it('deve retornar a paginação', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.getPaginacao()

            await flushPromises()
            expect(wrapper.vm.getPaginacao()).toEqual(state.reserva.todosOrgaos.paginacao)
        })

        it('deve tratar o evento de busca simples', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.tratarEventoBuscaSimples('Teste')

            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.RESETA_PAGE_ORGAOS_RESERVA]).toHaveBeenCalled()
            expect(wrapper.vm.filtrosInterno.conteudo.value).toBe('Teste')
            expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_TODOS_ORGAOS_PAGINADO]).toHaveBeenCalled()
        })

        it('deve retornar as colunas da tabela', async () => {
            wrapper = defaultMount()

            const colunasTabelaReserva = [
                {
                    text: 'Descrição',
                    value: 'descricao',
                    sortable: null,
                    align: null,
                    width: null,
                    class: 'gray--text'
                },
                {
                    text: 'Preenchimento',
                    value: 'preenchimento',
                    sortable: null,
                    align: null,
                    width: null,
                    class: 'gray--text'
                },
                {
                    text: 'Quantidade',
                    value: 'quantidadeReservada',
                    sortable: null,
                    align: null,
                    width: null,
                    class: 'gray--text'
                },
                {
                    text: 'Intervalo',
                    value: 'intervalo',
                    sortable: null,
                    align: null,
                    width: null,
                    class: 'gray--text'
                }
            ]

            wrapper.vm.criarColunasTabela()
            expect(wrapper.vm.colunasTabela).toEqual(colunasTabelaReserva)
        })

        describe('buscarProximoIntervalo', () => {
            it('deve buscar próximo intervalo e atualizar objeto selecionado com informações do intervalo', async () => {
                const reserva = {
                    reservaId: 1,
                    orgaoId: 1,
                    quantidadeReservada: 10
                }

                const reservaCompleta = {
                    reservaId: 1,
                    orgaoId: 1,
                    quantidadeReservada: 10,
                    items:[{orgaoId: 1, quantidadeReservada: 10, numeroFim: 10}]
                }

                actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_INTERVALO] = jest.fn().mockReturnValue({
                    quantidadeReservada: 10,
                    numeroInicio: 200,
                    numeroFim: 300
                })

                wrapper = shallowMount(ModalAdicionarIntervalos, {
                    store: new Vuex.Store({state, mutations, actions}),
                    router,
                    localVue,
                    mocks: {errors, $validator},
                })
                await flushPromises()

                wrapper.setData({
                    reservaIntervalos: [{
                        id: 1,
                        orgaoId: 1,
                        quantidadeReservada: '',
                        numeroInicio: '',
                        numeroFim: ''
                    }],
                    reservaIntervalosSelecionados: [{orgaoId: 1, quantidadeReservada: 10, numeroFim: 10}]
                })

                wrapper.vm.buscarProximoIntervalo(reserva)
                await flushPromises()

                expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_INTERVALO]).toHaveBeenCalled()
                expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_INTERVALO].mock.calls[0][1]).toEqual(reservaCompleta)
                expect(wrapper.vm.reservaIntervalos[0].orgaoId).toEqual(reserva.orgaoId)
                expect(wrapper.vm.reservaIntervalos[0].numeroInicio).toEqual(200)
                expect(wrapper.vm.reservaIntervalos[0].numeroFim).toEqual(300)
            })

            it('não deve atualizar os objetos com informações do intervalo quando ocorrer um erro ao buscar o intervalo', async () => {
                const reserva = {
                    reservaId: 1,
                    orgaoId: 1,
                    quantidadeReservada: 10
                }

                const reservaCompleta = {
                    reservaId: 1,
                    orgaoId: 1,
                    quantidadeReservada: 10,
                    items:[{orgaoId: 1, quantidadeReservada: 10, numeroFim: 10}]
                }

                actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_INTERVALO] = jest.fn().mockReturnValue(undefined)

                wrapper = shallowMount(ModalAdicionarIntervalos, {
                    store: new Vuex.Store({state, mutations, actions}),
                    router,
                    localVue,
                    mocks: {errors, $validator},
                })
                await flushPromises()

                wrapper.setData({
                    reservaIntervalos: [{
                        id: 1,
                        orgaoId: 1,
                        quantidadeReservada: '',
                        numeroInicio: '',
                        numeroFim: ''
                    }],
                    reservaIntervalosSelecionados: [{orgaoId: 1, quantidadeReservada: 10, numeroFim: 10}]
                })

                wrapper.vm.buscarProximoIntervalo(reserva)
                await flushPromises()

                expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_INTERVALO]).toHaveBeenCalled()
                expect(actions[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_INTERVALO].mock.calls[0][1]).toEqual(reservaCompleta)
                expect(wrapper.vm.reservaIntervalos[0].orgaoId).toEqual(reserva.orgaoId)
                expect(wrapper.vm.reservaIntervalos[0].numeroInicio).toEqual('')
                expect(wrapper.vm.reservaIntervalos[0].numeroFim).toEqual('')
            })
        })

        it('deve atualizar array de reserva intervalo com as reservas intervalos que foram selecionadas', () => {
            wrapper = defaultMount()

            wrapper.vm.reservaIntervalos = [{orgaoId: 1}]
            wrapper.vm.reservaIntervalosSelecionados = [{orgaoId: 1, reservaId: 2}]
            wrapper.vm.atualizaReservaIntervalosComSelecionados()

            expect(wrapper.vm.reservaIntervalos).toEqual(wrapper.vm.reservaIntervalosSelecionados)
        })

        it('deve selecionar todas as reservas intervalos', () => {
            wrapper = defaultMount()

            wrapper.vm.reservaIntervalos = [{orgaoId: 1, selecionado: false}, {orgaoId: 2, selecionado: true}]
            wrapper.vm.trtarSelecionarTodasReservaIntervalos({value:false})

            expect(wrapper.vm.reservaIntervalosSelecionados).toEqual([{orgaoId: 1, selecionado: true}])
        })

        it('deve deselecionar todas as reservas intervalos', () => {
            wrapper = defaultMount()

            wrapper.vm.reservaIntervalos = [{orgaoId: 1, selecionado: false}, {orgaoId: 2, selecionado: true}]
            wrapper.vm.reservaIntervalosSelecionados = [{orgaoId: 2, selecionado: true}]
            wrapper.vm.trtarSelecionarTodasReservaIntervalos({value:true})

            expect(wrapper.vm.reservaIntervalosSelecionados).toEqual([])
            expect(wrapper.vm.reservaIntervalos).toEqual([
              {
                  orgaoId: 1,
                  selecionado: false,
                  preenchimento: 'AUTOMATICO',
                  quantidadeReservada: null,
                  numeroInicio: '',
                  numeroFim: ''
              },
              {
                  orgaoId: 2,
                  selecionado: false,
                  preenchimento: 'AUTOMATICO',
                  quantidadeReservada: null,
                  numeroInicio: '',
                  numeroFim: ''
              }])
        })

        it('deve substituir maiorNumeroFimIntervalo', () => {
            wrapper = defaultMount()

            wrapper.vm.maiorNumeroFimIntervalo = 10
            wrapper.vm.verificarMaiorNumeroFimIntervalo(20)

            expect(wrapper.vm.maiorNumeroFimIntervalo).toEqual(20)
        })

        it('não deve eve substituir maiorNumeroFimIntervalo', () => {
            wrapper = defaultMount()

            wrapper.vm.maiorNumeroFimIntervalo = 10
            wrapper.vm.verificarMaiorNumeroFimIntervalo(8)

            expect(wrapper.vm.maiorNumeroFimIntervalo).toEqual(10)
        })

        it('deve substituir maiorNumeroFimIntervalo com maior numero fim das reservas intervalos selecionadas', () => {
            wrapper = defaultMount()

            wrapper.vm.maiorNumeroFimIntervalo = 10
            wrapper.vm.reservaIntervalosSelecionados = [{numeroFim:20},{numeroFim:30}]
            wrapper.vm.verificarMaiorNumeroFimIntervalo(null)

            expect(wrapper.vm.maiorNumeroFimIntervalo).toEqual(30)
        })

        it('deve incluir reserva intervalo em array de reservas intervalos selecionadas', () => {
            wrapper = defaultMount()

            const reservaIntervalo = {orgaoId: 1}
            wrapper.vm.reservaIntervalosSelecionados = []
            wrapper.vm.selecionaReservaIntervalo(reservaIntervalo)

            expect(wrapper.vm.reservaIntervalosSelecionados).toEqual([reservaIntervalo])
        })

        it('deve substituir reserva intervalo em array de reservas intervalos selecionadas com mais atual', () => {
            wrapper = defaultMount()

            const reservaIntervalo = {orgaoId: 1, reservaId: 1}
            wrapper.vm.reservaIntervalosSelecionados = [{orgaoId: 1}]
            wrapper.vm.selecionaReservaIntervalo(reservaIntervalo)

            expect(wrapper.vm.reservaIntervalosSelecionados).toEqual([reservaIntervalo])
        })


    })

    describe('computed', () => {

        describe('quantidadeSelecionadaIgualZero', () => {
            it('deve retornar true quando não selecionou nenhum orgão', () => {
                wrapper = defaultMount()

                wrapper.setData({
                    reservaIntervalosSelecionados: []
                })

                expect(wrapper.vm.quantidadeSelecionadaIgualZero).toBeTruthy()
            })

            it('deve retornar false quando selecionou orgãos', () => {
                wrapper = defaultMount()

                wrapper.setData({
                    reservaIntervalosSelecionados: ['1']
                })

                expect(wrapper.vm.quantidadeSelecionadaIgualZero).toBeFalsy()
            })
        })

        describe('existeIntervalosSemPreenchimento', () => {
            it('deve retornar false quando não houver nenhum intervalo', () => {
                wrapper = defaultMount()

                wrapper.setData({
                    reservaIntervalosSelecionados: []
                })

                expect(wrapper.vm.existeIntervalosSemPreenchimento).toBeFalsy()
            })

            it('deve retornar false quando os intervalos selecionados estiverem preenchidos corretamente', () => {
                wrapper = defaultMount()

                wrapper.setData({
                    reservaIntervalosSelecionados: [{
                        quantidadeReservada: 10,
                        numeroInicio: 1,
                        numeroFim: 10
                    }, {
                        quantidadeReservada: 10,
                        numeroInicio: 11,
                        numeroFim: 20
                    }]
                })

                expect(wrapper.vm.existeIntervalosSemPreenchimento).toBeFalsy()
            })

            it('deve retornar true quando existir intervalos sem preenchimento', () => {
                wrapper = defaultMount()

                wrapper.setData({
                    reservaIntervalosSelecionados: [{
                        quantidadeReservada: 10,
                        numeroInicio: 1,
                        numeroFim: 10
                    }, {
                        quantidadeReservada: 10,
                        numeroInicio: '',
                        numeroFim: ''
                    }]
                })

                expect(wrapper.vm.existeIntervalosSemPreenchimento).toBeTruthy()
            })
        })
    })
})
