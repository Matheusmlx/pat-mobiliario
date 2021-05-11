import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import ModalItensRegistroIncorporacao from './ModalItensRegistroIncorporacao'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('ModalItensRegistroIncorporacao', () => {
    let wrapper, actions, localVue, vuetify, errors, router, state, mutations, $validator, $t = jest.fn()

    const patrimonios = [
        {
            id: 1,
            numero: '001',
            valor: 10
        },
        {
            id: 2,
            numero: '002',
            valor: 20
        }
    ]


    const itemIncorporacao = {
        id: 1,
        estadoConservacao: 1,
        contaContabil: {
            id: 1,
            codigo: '123110500',
            descricao: 'TESTE'
        },
        tipoBem: 'MOVEL'
    }

    const defaultMount = (stubs) => shallowMount(ModalItensRegistroIncorporacao, {
        localVue,
        router,
        vuetify,
        stubs,
        store: new Vuex.Store({state, mutations, actions}),
        propsData: {value: true, item: itemIncorporacao},
        mocks: {$validator, errors, $t},
    })

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                },
                uploadedFiles: []
            },
            patrimonio:{
                resultadoBuscaTodosPatrimoniosRegistro: {
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['numero'],
                        defaultSortBy: ['numero'],
                        sortDesc: [false]
                    }
                },
            },
            itemIncorporacao:{
                autoSave: {
                    show: false
                },
            }
        }

        errors = {
            collect: jest.fn()
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'ModalItemVisualizarRegistro',
                    params: {
                        incorporacaoId: 1,
                        itemIncorporacaoId: 1
                    }
                }
            }
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true)
            }
        }

        mutations = {
            [mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_REGISTRO]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_REGISTRO]: jest.fn(),
            [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO_REGISTRO]: jest.fn().mockReturnValue(itemIncorporacao),
            [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_REGISTRO]: jest.fn().mockReturnValue(patrimonios),
        }

    })

    describe('Methods', () => {

        it('Deve buscar o item de incorporacao', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO_REGISTRO]).toHaveBeenCalled()
        })

        it('Deve buscar os patrimônios', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_REGISTRO]).toHaveBeenCalled()
        })

        it('Deve realizar a paginação e buscar os patrimônios', async () => {
            wrapper = defaultMount()

            const paginacao = {
                descending: false,
                groupBy: [],
                groupDesc: [],
                itemsPerPage: 10,
                multiSort: false,
                mustSort: false,
                page: 1,
                rowsPerPage: 10,
                sortBy: ['numero'],
                defaultSortBy: ['numero'],
                sortDesc: [false]
            }
            const resetarPaginacaoSortBy = jest.spyOn(wrapper.vm, 'resetarPaginacaoSortBy')

            wrapper.vm.tratarEventoPaginar(paginacao)
            await flushPromises()
            expect(mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_REGISTRO]).toHaveBeenCalled()
            expect(resetarPaginacaoSortBy).toHaveBeenCalledWith(state.patrimonio.resultadoBuscaTodosPatrimoniosRegistro.paginacao)
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_REGISTRO]).toHaveBeenCalled()
        })

        it('Deve resetar a páginação', async () => {
            wrapper = defaultMount()
            await wrapper.vm.resetaPage()
            await flushPromises()
            expect(mutations[mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_REGISTRO]).toHaveBeenCalled()
        })

        it('Deve fechar o modal', () =>{
            wrapper = defaultMount()
            wrapper.vm.fecharModal()

            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO]).toHaveBeenCalled()
            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarRegistroIncorporacao', params: {incorporacaoId: 1}})
        })

        it('Deve redirecionar para a ficha individual do patrimônio', async() => {
            wrapper = defaultMount()

            const item = {id: 5}

            await wrapper.vm.redirecionarFichaPatrimonio(item)

            expect(router.push).toBeCalled()
            expect(router.push).toBeCalledWith({name: 'FichaPatrimonioDadosGerais', params: {patrimonioId: 5}})
        })

    })
})
