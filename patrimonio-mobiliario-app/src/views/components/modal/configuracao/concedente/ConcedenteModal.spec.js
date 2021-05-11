import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import ConcedenteModal from './ConcedenteModal'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('ConcedenteModal', () => {
    let wrapper, localVue, state, mutations, actions, vuetify, $emit = jest.fn()

    const concedentesData = {
        'items':
            [
                {
                    id: 1,
                    cpfCnpj: '00156005800001',
                    nome: 'Caixa Economica Federal',
                    situacao: 'ATIVO'
                },
                {
                    id: 2,
                    cpfCnpj: '00156005800001',
                    nome: 'Caixa Economica Federal',
                    situacao: 'ATIVO'
                },
                {
                    id: 3,
                    cpfCnpj: '00156005800001',
                    nome: 'Caixa Economica Federal',
                    situacao: 'ATIVO'
                },
                {
                    id: 4,
                    cpf: '00156005800001',
                    concedente: 'Caixa Economica Federal',
                    situacao: 'ATIVO'
                }
            ],
        'paginas': 3,
        'totalItens': 5
    }

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                }
            },
            concedente: {
                concedentes: [],
                todosConcedentes: {
                    paginacao: {
                        page: 1,
                        rowsPerPage: 5,
                        sortDesc: [false],
                        sortBy: ['situacao']
                    },
                    filtros: {
                        conteudo: {
                            value: null,
                            default: null,
                            label: 'Pesquisa'
                        }
                    }
                }
            }
        }

        mutations = {
            [mutationTypes.CONFIGURACAO.CONCEDENTE.SET_FILTROS_BUSCA_TODOS_CONCEDENTES]: jest.fn(),
            [mutationTypes.CONFIGURACAO.CONCEDENTE.SET_PAGINACAO_BUSCA_TODOS_CONCEDENTES]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.CONFIGURACAO.CONCEDENTE.RESETA_PAGE_CONCEDENTE]: jest.fn()
        }
        actions = {
            [actionTypes.CONFIGURACAO.CONCEDENTE.SALVAR_CONCEDENTE]: jest.fn(),
            [actionTypes.CONFIGURACAO.CONCEDENTE.EDITAR_CONCEDENTE]: jest.fn(),
            [actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_TODOS_CONCEDENTES]: jest.fn().mockReturnValue(concedentesData)
        }
    })

    const defaultMount = () => shallowMount(ConcedenteModal, {
        localVue,
        vuetify,
        propsData: {
            concedentes: []
        },
        store: new Vuex.Store({
            state, mutations, actions
        }),
        mocks: {$emit}
    })

    describe('Computed', () => {
        it('Deve emitir evento para fechar modal', async () => {
            wrapper = defaultMount()

            wrapper.setData({concedenteModal: false})

            expect(wrapper.emitted().fecharModal).toBeTruthy()
        })

        it('Não deve emitir evento para fechar modal', async () => {
            wrapper = defaultMount()

            wrapper.setData({concedenteModal: true})

            expect(wrapper.emitted().fecharModal).toBeFalsy()
        })
    })

    describe('Events', () => {
        it('Deve emitir o evento de paginar quando alterar a paginação', async () => {
            const paginacao = {
                page: 1,
                rowsPerPage: 5,
                sortDesc: [false],
                sortBy: [null]
            }
            wrapper = shallowMount(ConcedenteModal, {
                localVue,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                stubs: {
                    ConcedenteModalListagem: {
                        template: '<div><button class="stub" @click="$emit(\'paginar\', {\n' +
                                '                        page: 1,\n' +
                            '                        rowsPerPage: 5,\n' +
                            '                        sortDesc: [false],\n' +
                            '                        sortBy: [null]\n})"></button></div>'
                    }
                }
            })

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.CONFIGURACAO.CONCEDENTE.SET_PAGINACAO_BUSCA_TODOS_CONCEDENTES].mock.calls[0][1]).toEqual(paginacao)
        })

        it('Deve emitir o evendo de buscar simples', async () => {
            wrapper = shallowMount(ConcedenteModal, {
                localVue,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                stubs: {
                    'az-search': {
                        template: '<div><button class="stub" @click="$emit(\'simple-search\', \'Teste\')"></button></div>'
                    }
                }
            })

            await flushPromises()
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_TODOS_CONCEDENTES]).toHaveBeenCalled()
            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('Teste')
        })

        it('Deve emitir o evento de remover os filtros da pesquisa', async () => {
            wrapper = shallowMount(ConcedenteModal, {
                localVue,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                stubs: {
                    'az-search': {
                        template: '<div><button class="stub" @click="$emit(\'remove-filter\', \'conteudo\')"></button></div>'
                    }
                }
            })
            wrapper.vm.filtrosInterno.conteudo.value = 'Teste'
            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual(null)
        })
    })

    describe('Methods', () => {
        it('Deve adicionar um novo campo', async () => {
            wrapper = defaultMount()

            wrapper.vm.adicionarCampo()

            expect(wrapper.vm.concedentes.length).toBe(1)
        })

        it('Deve ajustar o filtro para busca dos concedentes', async () => {
            wrapper = defaultMount()

            wrapper.vm.itens = []
            await wrapper.vm.buscar()

            await flushPromises()

            expect(mutations[mutationTypes.CONFIGURACAO.CONCEDENTE.SET_FILTROS_BUSCA_TODOS_CONCEDENTES]).toHaveBeenCalled()
            expect(mutations[mutationTypes.CONFIGURACAO.CONCEDENTE.SET_FILTROS_BUSCA_TODOS_CONCEDENTES].mock.calls[0][1]).toEqual(wrapper.vm.getFiltrosInterno())
        })

        it('Deve buscar concedentes', async () => {
            wrapper = defaultMount()

            await wrapper.vm.buscarConcedentes()

            await flushPromises()

            expect(mutations[mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_TODOS_CONCEDENTES]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]).toHaveBeenCalled()
            expect(wrapper.vm.concedentes).toBe(concedentesData.items)
            expect(wrapper.vm.paginas).toBe(concedentesData.totalPages)
            expect(wrapper.vm.totalItens).toBe(concedentesData.totalItens)
        })

        it('Deve fazer a busca com o conteudo passsado', async () => {
            wrapper = defaultMount()

            wrapper.setData({
                filtrosInterno: {
                    conteudo: {
                        value: null,
                        default: null,
                        label: 'Pesquisa'
                    }
                }
            })

            wrapper.vm.tratarEventoBuscaSimples('Teste')

            expect(wrapper.vm.filtrosInterno.conteudo.value).toBe('Teste')
            expect(wrapper.vm.buscar()).toBeTruthy()
        })

        it('Deve realizar a paginação do conteúdo', async () => {
            wrapper = defaultMount()

            wrapper.setData({
                paginacaoInterna: {
                    page: 1,
                    rowsPerPage: 5
                }
            })

            const paginacao = {
                page: 1,
                rowsPerPage: 5,
                sortDesc: [false],
                sortBy: ['situacao']
            }

            wrapper.vm.tratarEventoPaginar(paginacao)

            expect(mutations[mutationTypes.CONFIGURACAO.CONCEDENTE.SET_PAGINACAO_BUSCA_TODOS_CONCEDENTES]).toHaveBeenCalled()
            expect(wrapper.vm.buscar()).toBeTruthy()
        })

        it('Deve remover o filtro', async () => {
            wrapper = defaultMount()

            wrapper.setData({
                filtrosInterno: {
                    conteudo: {
                        value: 'Teste',
                        default: null,
                        label: 'Pesquisa'
                    }
                }
            })

            wrapper.vm.tratarEventoRemoverFiltro('conteudo')

            expect(wrapper.vm.filtrosInterno.conteudo.value).toBe(null)
            expect(wrapper.vm.buscar()).toBeTruthy()
        })

        it('Deve retornar um clone dos filtros', async() => {
            wrapper = defaultMount()

            const resultado = wrapper.vm.getFiltros()

            expect(resultado).toEqual(state.concedente.todosConcedentes.filtros)
        })

        it('Deve retornar um clone dos filtros internos', async() => {
            wrapper = defaultMount()

            wrapper.vm.filtrosInterno = wrapper.vm.getFiltros()
            const resultado = wrapper.vm.getFiltrosInterno()

            expect(resultado).toEqual(wrapper.vm.filtrosInterno)
        })

        it('Deve atualizar todos os campos', async () => {
            wrapper = defaultMount()

            wrapper.vm.itens = [{
                id: 1,
                cpf: '00156005800001',
                concedente: 'Caixa Economica Federal',
                situacao: 'ATIVO'
            }]

            wrapper.vm.cancelar()

            expect(wrapper.vm.itens).toEqual([])
            expect(wrapper.vm.buscar()).toBeTruthy()
        })

        it('Deve remover o campo', async() => {
            wrapper = defaultMount()

            wrapper.vm.itens = [{
                cpf: '',
                concedente: '',
                situacao: 'ATIVO'
            }]

            wrapper.vm.removerCampo()

            expect(wrapper.vm.itens).toEqual([])
        })

        it('Deve salvar o concedente', async () => {
            wrapper = defaultMount()

            const dados = {
                cpf: '50890891087',
                nome: 'João',
                situacao: 'ATIVO'
            }

            await wrapper.vm.salvaConcedente(dados)
            await flushPromises()

            expect(mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE].mock.calls[0][1]).toBe('Salvando o concedente...')
            expect(actions[actionTypes.CONFIGURACAO.CONCEDENTE.SALVAR_CONCEDENTE]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(wrapper.vm.buscarConcedentes()).toBeTruthy()
        })

        it('Deve editar o concedente', async () => {
            wrapper = defaultMount()

            const dados = {
                id: 1,
                cpf: '50890891087',
                nome: 'João',
                situacao: 'ATIVO'
            }

            await wrapper.vm.editaConcedente(dados)
            await flushPromises()

            expect(mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE].mock.calls[0][1]).toBe('Editando o concedente...')
            expect(actions[actionTypes.CONFIGURACAO.CONCEDENTE.EDITAR_CONCEDENTE]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
        })

        it('Deve emitir evento para fechar modal', async() => {
            wrapper = defaultMount()

            wrapper.vm.fecharModal()

            expect(wrapper.emitted().fecharModal).toBeTruthy()
        })

        it('Deve emitir evento para selecionar concedente', async() => {
            wrapper = defaultMount()

            const concedente = {
                id: 7,
                nome: 'concedente'
            }

            wrapper.vm.selecionarConcedente(concedente)

            expect(wrapper.emitted().selecionarConcedente).toBeTruthy()
            expect(wrapper.emitted().selecionarConcedente[0][0]).toEqual(concedente)
        })

        it('Deve retornar true pois existe um novo concedente', () => {
            wrapper = defaultMount()

            const concedente = {
                nome: 'concedente'
            }
            wrapper.vm.concedentes = [concedente]

            expect(wrapper.vm.validarSeExisteNovoConcedente()).toEqual(true)
        })

        it('Deve retornar false pois não existe um novo concedente', () => {
            wrapper = defaultMount()

            const concedente = {
                id:1,
                nome: 'concedente'
            }
            wrapper.vm.concedentes = [concedente]

            expect(wrapper.vm.validarSeExisteNovoConcedente()).toEqual(false)
        })

        it('Deve retornar false pois não existe concedente', () => {
            wrapper = defaultMount()

            wrapper.vm.concedentes = []

            expect(wrapper.vm.validarSeExisteNovoConcedente()).toEqual(false)
        })

        it('deve desabilitar botao novo', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.desabilitaBotaoNovo()

            await flushPromises()
            expect(wrapper.vm.botaoNovoDesabilitado).toEqual(true)
        })

        it('deve habilitar botao novo', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.habilitaBotaoNovo()

            await flushPromises()
            expect(wrapper.vm.botaoNovoDesabilitado).toEqual(false)
        })
    })
})
