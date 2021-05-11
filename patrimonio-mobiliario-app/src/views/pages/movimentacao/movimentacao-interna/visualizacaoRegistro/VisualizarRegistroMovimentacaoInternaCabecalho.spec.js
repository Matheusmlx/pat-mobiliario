import VisualizarRegistroMovimentacaoInternaCabecalho from './VisualizarRegistroMovimentacaoInternaCabecalho'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'

let localVue, wrapper, state, store, router

describe('VisualizarRegistroMovimentacaoInternaCabecalho', () => {

    const movimentacaoInterna = {
        id: 7,
        codigo: '00001',
        tipo: 'DISTRIBUICAO',
        usuarioCriacao: 'Lucas',
        dataFinalizacao: '2021-01-18T04:00:00.000-0400',
        situacao: 'FINALIZADO',
        numeroNotaLancamentoContabil: '1335',
        dataNotaLancamentoContabil: '2021-01-18T04:00:00.000-0400',
        numeroProcesso: '123456',
        orgao: 'AGEPAN - Agência Estadual de Regulação',
        setorOrigem: 'Assessoria Jurídica',
        setorDestino: 'Assessoria Jurídica',
        motivoObservacao: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.'
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
            push: jest.fn(),
            init: jest.fn(),
            history: {
                current: jest.fn()
            }
        }

        localVue = applicationTestBuilder.build()

        store = new Vuex.Store({state})
    })

    const defaultMount = () => shallowMount(VisualizarRegistroMovimentacaoInternaCabecalho, {
        propsData: {
            movimentacao: movimentacaoInterna
        },
        store,
        localVue,
        router
    })

    describe('Computed', () => {
        it('Deve retornar true se a movimentação possui itens para devolução', () => {
            movimentacaoInterna.situacao = 'DEVOLVIDO_PARCIAL'
            wrapper = defaultMount()

            expect(wrapper.vm.possuiItensParaDevolver).toBe(true)
        })

        it('Deve retornar false se a movimentação não possui itens para devolução', () => {
            movimentacaoInterna.situacao = 'DEVOLVIDO'
            wrapper = defaultMount()

            expect(wrapper.vm.possuiItensParaDevolver).toBe(false)
        })
    })

    describe('Methods', () => {
        it('Deve emitir evento de remover todos patrimonios', async() => {
            const wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.devolverTodosPatrimonios()

            expect(wrapper.emitted().devolverTodosPatrimonios).toBeTruthy()
        })

        it('Deve abrir modal para devolução parcial dos patrimônios', async() => {
            const wrapper = defaultMount()

            wrapper.vm.abrirModalDevolverPatrimonios()

            expect(router.push).toBeCalled()
            expect(router.push).toBeCalledWith({name: 'ModalDevolverPatrimonios'})
        })
    })
})
