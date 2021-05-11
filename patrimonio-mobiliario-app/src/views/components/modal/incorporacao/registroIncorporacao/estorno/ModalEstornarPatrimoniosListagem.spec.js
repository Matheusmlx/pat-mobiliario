import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import {actionTypes, mutationTypes} from '@/core/constants'
import ModalEstornarPatrimoniosListagem from './ModalEstornarPatrimoniosListagem'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'


describe('ModalEstornarPatrimoniosListagem', () => {

    let localVue, router, vuetify, state, wrapper, errors, mutations, actions

    const defaultMount = () => shallowMount(ModalEstornarPatrimoniosListagem, {
        vuetify,
        localVue,
        router,
        store: new Vuex.Store({state, actions, mutations}),
        mocks: {errors},
        propsData:{
            estorno: {motivo:'Motivo', data:new Date('1995-12-17T03:24:00'), usuario:'admin'}
        }
    })

    const patrimonios = {
        items:[{id: 1},{id:2}],
        totalPages: 1,
        totalElements: 2
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        router = {
            init: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'ModalEstornarPatrimoniosListagem',
                    params: {incorporacaoId: 7}
                }
            }
        }
        state = {
            patrimonio:{
                estorno:{motivo:false},
                colunasTodosPatrimoniosDeTodosItens:'colunas',
                resultadoBuscaTodosPatrimoniosDeTodosItens:{
                    paginacao: {
                        itemsPerPage: 10,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['numero'],
                        defaultSortBy: ['numero'],
                        sortDesc: [false],
                    },
                    filtros: {
                        conteudo: {
                            default: null,
                            label: 'Pesquisa',
                            value: ''
                        }
                    }
                },
            }
        }

        errors = {
            collect:jest.fn()
        }

        actions = {
            [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS]: jest.fn().mockReturnValue(patrimonios)
        }

        mutations = {
            [mutationTypes.PATRIMONIO.SET_ESTORNO]: jest.fn(),
            [mutationTypes.PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS]: jest.fn()
        }
    })

    describe('Mounted', () => {
        it('Deve buscar patrimonios', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS].mock.calls[0][1]).toEqual(7)
        })
    })

    describe('Computed', () => {
        it('Deve retornar header da tabela', () => {
            wrapper = defaultMount()
            flushPromises()

            const headers = [
                {align: 'left', class: 'gray--text', sortable: false, text: 'Patrimônio', value: 'numero', width: '30%'},
                {align: 'left', class: 'gray--text', sortable: false, text: 'Descrição', value: 'descricao', width: '40%'},
                {align: 'left', class: 'gray--text', sortable: false, text: 'Valor', value: 'valor', width: '30%'}]


            expect(wrapper.vm.colunas).toEqual(headers)
        })

        it('Deve retornar quantidade patrimonios 0', () => {
            wrapper = defaultMount()
            flushPromises()

            expect(wrapper.vm.quantidadePatrimonios).toEqual(0)
        })

        it('Deve retornar quantidade patrimonios', async () => {
            wrapper = shallowMount(ModalEstornarPatrimoniosListagem, {
                vuetify,
                localVue,
                router,
                store: new Vuex.Store({state, actions, mutations}),
                mocks: {errors},
                propsData:{
                    estorno: {motivo:'Motivo', data:new Date('1995-12-17T03:24:00'), usuario:'admin', patrimoniosId:[1,2]}
                }
            })
            await flushPromises()
            expect(wrapper.vm.quantidadePatrimonios).toEqual(2)
        })
    })

    describe('Watch', () => {
        it('Deve emitir paginação ao mudar paginação interna', async () => {
            wrapper = defaultMount()
            wrapper.vm.paginacaoInterna.page = 2
            await flushPromises()
            expect(mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS].mock.calls[0][1]).toEqual(wrapper.vm.paginacaoInterna)
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS].mock.calls[0][1]).toEqual(7)
        })
    })

    describe('Methods', () => {
        it('Deve buscar patrimonios para estorno', async () => {
            wrapper = defaultMount()

            await flushPromises()
            expect(wrapper.vm.patrimonios).toEqual(patrimonios.items)
            expect(wrapper.vm.paginas).toEqual(patrimonios.totalPages)
            expect(wrapper.vm.totalItens).toEqual(patrimonios.totalElements)
        })

        it('Deve fechar modal', () => {
            wrapper = defaultMount()
            wrapper.vm.fecharModal()
            flushPromises()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'VisualizarRegistroIncorporacao',params: {incorporacaoId: 7}})
        })

        it('Deve redirecionar para modal motivo', () => {
            wrapper = defaultMount()
            wrapper.vm.voltar()
            flushPromises()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'ModalEstornarPatrimoniosMotivo'})
        })

        it('Deve setar estorno e redirecionar para finalizar', () => {
            wrapper = defaultMount()
            wrapper.vm.continuar()
            flushPromises()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'ModalEstornarPatrimoniosFinalizar'})
            expect(mutations[mutationTypes.PATRIMONIO.SET_ESTORNO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.SET_ESTORNO].mock.calls[0][1]).toEqual(wrapper.vm.estorno)
        })

        it('Deve realizar busca simples', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoBuscaSimples('simples')
             await flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('simples')
            expect(mutations[mutationTypes.PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS].mock.calls[0][1]).toEqual(7)
        })

        it('Deve retornar filtros',  () => {
            wrapper = defaultMount()
            flushPromises()

            expect(wrapper.vm.getFiltros()).toEqual(state.patrimonio.resultadoBuscaTodosPatrimoniosDeTodosItens.filtros)
        })

        it('Deve retornar paginacao',  () => {
            wrapper = defaultMount()
            flushPromises()

            expect(wrapper.vm.getPaginacao()).toEqual(state.patrimonio.resultadoBuscaTodosPatrimoniosDeTodosItens.paginacao)
        })

        it('Deve retornar filtrosInternos',  () => {
            wrapper = defaultMount()
            flushPromises()

            expect(wrapper.vm.getFiltrosInterno()).toEqual(wrapper.vm.filtrosInterno)
        })

        it('Deve remover filtro',  () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoRemoverFiltro('conteudo')
            flushPromises()

            expect(wrapper.vm.filtrosInterno.conteudo.value).toBe(null)
            expect(mutations[mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS].mock.calls[0][1]).toEqual(7)
        })

        it('Deve paginar',  () => {
            wrapper = defaultMount()
            const paginacao = {sortBy: []}
            wrapper.vm.tratarEventoPaginar(paginacao)
            flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS].mock.calls[0][1]).toEqual(paginacao)
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS].mock.calls[0][1]).toEqual(7)
        })

        it('Deve resetar paginacao',  () => {
            wrapper = defaultMount()
            wrapper.vm.resetaPage()
            flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS]).toHaveBeenCalled()
        })

        it('Deve resetar sortBy',  () => {
            wrapper = defaultMount()
            const paginacao = {sortBy: [], defaultSortBy:'default'}
            wrapper.vm.resetarPaginacaoSortBy(paginacao)
            flushPromises()

            expect(state.patrimonio.resultadoBuscaTodosPatrimoniosDeTodosItens.paginacao.sortBy[0]).toEqual(paginacao.defaultSortBy)
        })

        it('Deve colocar todos patrimonios no array de selecionados caso estorno.todosSelecionados = true',  () => {
            wrapper = defaultMount()
            wrapper.vm.estorno.todosSelecionados = true
            wrapper.vm.estorno.patrimoniosExcecao = []
            wrapper.vm.patrimonios = [1,2,3]
            wrapper.vm.selecionaTodosPatrimoniosSeNecessario()
            flushPromises()

            expect(wrapper.vm.estorno.patrimoniosId).toEqual(wrapper.vm.patrimonios)
        })

        it('Não deve colocar todos patrimonios no array de selecionados caso estorno.todosSelecionados = false',  () => {
            wrapper = defaultMount()
            wrapper.vm.estorno.todosSelecionados = false
            wrapper.vm.patrimonios = [1,2,3]
            wrapper.vm.selecionaTodosPatrimoniosSeNecessario()
            flushPromises()

            expect(wrapper.vm.estorno.patrimoniosId).not.toEqual(wrapper.vm.patrimonios)
        })

        it('Deve setar estorno.todosSelecionados = true se props.value = false(checkbox de seleção de todos patrimonios marcado)',  () => {
            wrapper = defaultMount()
            wrapper.vm.selecionaTodosPatrimoniosTodasPaginas({value:false})

            flushPromises()

            expect(wrapper.vm.estorno.todosSelecionados).toEqual(true)
        })

        it('Deve setar estorno.todosSelecionados = false se props.value = true(checkbox de seleção de todos patrimonios não marcado)',  () => {
            wrapper = defaultMount()
            wrapper.vm.selecionaTodosPatrimoniosTodasPaginas({value:true})
            flushPromises()

            expect(wrapper.vm.estorno.todosSelecionados).toEqual(false)
        })

        it('Deve filtrar os patrimônios que não foram desselecionados', () => {
            wrapper = defaultMount()
            wrapper.vm.estorno.patrimoniosExcecao = [1]
            wrapper.vm.patrimonios = [
                {id: 1},
                {id: 2},
                {id: 3}
            ]

            const patrimonios = wrapper.vm.filtrarPatrimonios(wrapper.vm.patrimonios)

            expect(patrimonios).toEqual([{id: 2}, {id: 3}])
        })

        it('Deve limpar a lista de patrimônios que foram desselecionados', () => {
            wrapper = defaultMount()
            wrapper.vm.estorno.patrimoniosExcecao = [1]

            wrapper.vm.resetarPatrimoniosExcecao()

            expect(wrapper.vm.estorno.patrimoniosExcecao).toEqual([])
        })

        it('Deve selecionar o patrimônio', () => {
            wrapper = defaultMount()
            wrapper.vm.estorno.patrimoniosId = []

            wrapper.vm.selectItem({id: 1})

            expect(wrapper.vm.estorno.patrimoniosId).toEqual([{id: 1}])
        })

        it('Deve desselecionar o patrimônio', () => {
            wrapper = defaultMount()
            wrapper.vm.estorno.patrimoniosId = [
                {id: 1},
                {id: 2},
                {id: 3}
            ]

            wrapper.vm.deselectItem({id: 2})

            expect(wrapper.vm.estorno.patrimoniosId).toEqual([{id: 1}, {id: 3}])
        })
    })
})
