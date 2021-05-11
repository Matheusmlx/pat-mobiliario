import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ContaContabil from './ContaContabil'
import flushPromises from 'flush-promises'
import {mutationTypes, actionTypes} from '@/core/constants'

describe('ContaContabil', () => {
    let wrapper, localVue, vuetify, errors, state, $validator, contaContabil, mutations, actions, store

    const todasContasContabeis = {
        todasContasContabeis: {
            filtros: {
                conteudo: {
                    default: null,
                    label: 'Pesquisa',
                    value: ''
                }
            },
            paginacao: {
                descending: false,
                groupBy: [],
                groupDesc: [],
                itemsPerPage: 10,
                multiSort: false,
                mustSort: false,
                page: 1,
                rowsPerPage: 10,
                sortBy: ['codigo'],
                sortDesc: [false]
            },
            linhasPorPagina: [10, 25, 50, 100]
        },
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        errors = {
            collect: jest.fn()
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true)
            }
        }
        state = {
            filtros: {},
            itens: [],
            paginas: 0,
            totalElements: 0,
            paginacaoInterna: 0
        }
        contaContabil = {
            namespaced: true,
            state: { todasContasContabeis },
            actions: {
                buscarTodasContasContabeis: jest.fn().mockReturnValue({
                    items: [{ codigoConta: 123654, descricao: 'MÁQUINAS,FERRAMENTAS', situacao: 'ATIVO' }],
                    totalPages: 1,
                    totalElements: 1
                }),
                salvarContaContabil: jest.fn()
            },
            mutations: {
                setFiltrosBuscaTodasContasContabeis: jest.fn(),
                setPaginacaoBuscaTodasContasContabeis: jest.fn(),
                resetaPageContaContabil: jest.fn()
            },
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
        }

        actions = {
            [actionTypes.CONFIGURACAO.CONTA_CONTABIL.SALVAR_CONTA_CONTABIL]: jest.fn()
        }

        store = new Vuex.Store({state, mutations, actions, modules: {contaContabil}})
    })

    const defaultMount = (stubs) => shallowMount(ContaContabil, {
        localVue,
        vuetify,
        store,
        stubs,
        mocks: { $validator, errors }
    })

    describe('Methods', () => {
        it('Deve buscar todas ContasContabeis', async () => {
            wrapper = await defaultMount()

            await wrapper.vm.buscar()
            await flushPromises()

            expect(contaContabil.mutations.setFiltrosBuscaTodasContasContabeis).toHaveBeenCalled()
            expect(contaContabil.mutations.setFiltrosBuscaTodasContasContabeis).toHaveReturnedTimes(1)
            expect(contaContabil.mutations.setFiltrosBuscaTodasContasContabeis.mock.calls[0][1]).toEqual(wrapper.vm.getFiltrosInterno())
            expect(mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE].mock.calls[0][1]).toEqual('Carregando....')
            expect(contaContabil.actions.buscarTodasContasContabeis).toHaveBeenCalled()
            expect(wrapper.vm.itens).toEqual([{ codigoConta: 123654, descricao: 'MÁQUINAS,FERRAMENTAS', situacao: 'ATIVO' }])
            expect(wrapper.vm.paginas).toEqual(1)
            expect(wrapper.vm.totalElements).toEqual(1)

        }),

        it('Deve setar o conteudo vindo por paremetro na propriedade filtrosInterno e realizar a busca', async () => {
            wrapper = await defaultMount()

            wrapper.vm.filtros = { conteudo: {} }

            await wrapper.vm.buscaSimples('Máquinas')
            await flushPromises()
            expect(wrapper.vm.filtros.conteudo.value).toEqual('Máquinas')
        }),

        it('Deve remover os filtros atual pelo default ', async ()=>{
            wrapper = await  defaultMount()

            wrapper.vm.filtros = {propriedade:154}

            await wrapper.vm.removerFiltro(154)
            await flushPromises()

            expect(wrapper.vm.filtros.propriedade).toEqual(154)
        })

        it('Deve salvar conta contabil', async ()=>{
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.tratarEventoSalvar({id:1})
            await flushPromises()

            expect(contaContabil.actions.salvarContaContabil).toHaveBeenCalled()
            expect(contaContabil.actions.buscarTodasContasContabeis).toHaveBeenCalled()
        })

        it('Deve chamar cancelar edição', async ()=>{
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.cancelarEdicao()
            await flushPromises()

            expect(contaContabil.actions.buscarTodasContasContabeis).toHaveBeenCalled()
        })

        it('Deve setar paginação', async ()=>{
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.paginar({page:1})
            await flushPromises()

            expect(contaContabil.mutations.setPaginacaoBuscaTodasContasContabeis).toHaveBeenCalled()
        })
    })

})
