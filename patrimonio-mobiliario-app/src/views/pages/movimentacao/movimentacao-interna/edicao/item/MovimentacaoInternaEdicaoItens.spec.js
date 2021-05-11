import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import MovimentacaoInternaEdicaoItens from './MovimentacaoInternaEdicaoItens'
import {actionTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('MovimentacaoInternaEdicaoItens', () => {

    let wrapper, store, localVue, router, actions, state

    const Itens = {
        items:[
            {
                patrimonioId:1,
                patrimonioNumero: '000493',
                patrimonioDescricao: 'descrição',
                incorporacaoCodigo: '0082832'
            }
        ],
        totalPages: 1,
        totalElements: 1
    }

    beforeEach(() => {

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Movimentacoes', hasAccess: true}]
                }
            }
        }

        router = {
            routes: [],
            replace: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'MovimentacaoInternaEdicaoItens',
                    params: { movimentacaoInternaId: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()

        actions = {[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(Itens)}

        store = new Vuex.Store({actions, state})
    })

    const defaultMount = () => shallowMount(MovimentacaoInternaEdicaoItens, {
        store,
        router,
        localVue
    })

    describe('mounted', () => {
        it('deve buscar todos itens', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA].mock.calls[0][1]).toEqual(router.history.current.params.movimentacaoInternaId)
            expect(wrapper.vm.itens).toEqual(Itens.items)
            expect(wrapper.vm.podeContinuar).toEqual(true)
            expect(wrapper.emitted().habilitaPasso4).toBeTruthy()
        })
    })

    describe('methods', () => {
        it('deve habilitar o passo 4', () => {
            wrapper = defaultMount()
            wrapper.vm.habilitaPasso4()

            expect(wrapper.emitted().habilitaPasso4).toBeTruthy()
        })

        it('deve desabilitar o passo 4', () => {
            wrapper = defaultMount()
            wrapper.vm.desabilitaPasso4()

            expect(wrapper.emitted().desabilitaPasso4).toBeTruthy()
        })

        it('deve redirecionar para MovimentacaoInternaEdicaoDadosGerais', () => {
            wrapper = defaultMount()
            wrapper.vm.voltar()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: 1}})
        })

        it('deve redirecionar para MovimentacaoInternaEdicaoDocumentos', () => {
            wrapper = defaultMount()
            wrapper.vm.continuar()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoDocumentos', params: {movimentacaoInternaId: 1}})
        })

        it('deve desabilitar passo 4 se não houver nenhum Item adicionado', async () => {
            Itens.items = []
            actions = {[actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA]: jest.fn().mockReturnValue(Itens)}
            wrapper = shallowMount(MovimentacaoInternaEdicaoItens, {
                store: new Vuex.Store({actions, state}),
                router,
                localVue
            })
            await flushPromises()

            expect(wrapper.vm.podeContinuar).toEqual(false)
            expect(wrapper.emitted().desabilitaPasso4).toBeTruthy()
        })

        it('deve abrir modal para adição de itens', () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoAdicionarItem()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'ModalMovimentacaoInternaListagemItens', params: {movimentacaoInternaId: 1}})
        })
    })
})
