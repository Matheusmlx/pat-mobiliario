import VisualizarRegistroMovimentacaoInterna from './VisualizarRegistroMovimentacaoInterna'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {shallowMount} from '@vue/test-utils'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'

let router, state, actions, mutations, store, localVue

describe('VisualizarRegistroMovimentacaoInterna', () => {

    const movimentacaoInterna = {
        id: 7,
        codigo: '00001',
        tipo: 'DISTRIBUICAO',
        usuarioCriacao: 'Lucas',
        dataFinalizacao: '2021-01-14T04:00:00.000-0400',
        situacao: 'FINALIZADO',
        numeroNotaLancamentoContabil: '1335',
        dataNotaLancamentoContabil: '2021-01-14T04:00:00.000-0400',
        numeroProcesso: '123456',
        orgao: 'AGEPAN - Agência Estadual de Regulação',
        setorOrigem: 'Assessoria Jurídica',
        setorDestino: 'Assessoria Jurídica',
        motivoObservacao: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.'
    }

    const tooltip = {
        razaoPatrimoniosDevolvidos: '1/2 patrimônios devolvidos'
    }

    const setores = {
        items: [{
            id: 1,
            descricao: 'ARI - Assessoria de Relações Institucionais'
        }]
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
            push: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'MovimentacaoInternaEdicaoDadosGerais',
                    params: { movimentacaoInternaId: 7}
                }
            }
        }

        actions = {
            [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.ATUALIZAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO]: jest.fn(),
            [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.DEVOLVER_TODOS_PATRIMONIOS]:jest.fn(),
            [actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO]: jest.fn().mockReturnValue(movimentacaoInterna),
            [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_INFORMACAO_TOOLTIP]: jest.fn().mockReturnValue(tooltip),
            [actionTypes.COMUM.BUSCAR_SETORES_POSSUI_ACESSO]: jest.fn().mockReturnValue(setores)
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
        }

        localVue = applicationTestBuilder.build()

        store = new Vuex.Store({actions, mutations, state})

    })

    const defaultMount = () => shallowMount(VisualizarRegistroMovimentacaoInterna, {
        store,
        router,
        localVue
    })

    describe('Methods', () => {

        it('deve setar o id da movimentação', async() => {
            const wrapper = defaultMount()

            expect(wrapper.vm.movimentacaoInternaId).toBe(7)
        })

        it('deve buscar movimentação interna', async() => {
            const wrapper = defaultMount()

            await flushPromises()

            expect(wrapper.vm.movimentacao).toEqual(movimentacaoInterna)
        })

        it('deve editar movimentação interna', async() => {
            const wrapper = defaultMount()

            const dados = {
                dataNotaLancamentoContabil: '2021-01-15T04:00:00.000-0400'
            }

            await wrapper.vm.tratarEventoEdicao(dados)

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.ATUALIZAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO]).toBeCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.ATUALIZAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO].mock.calls[0][1]).toEqual(dados)
        })

        it('Deve chamar a action de devolver todos patrimonios', async () => {
            const wrapper = defaultMount()

            const idMovimentacao = 7

            await wrapper.vm.tratarDevolucaoPatrimonios(idMovimentacao)

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.DEVOLVER_TODOS_PATRIMONIOS]).toBeCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.DEVOLVER_TODOS_PATRIMONIOS].mock.calls[0][1]).toEqual(7)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toBeCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1]).toEqual({message: 'Operação realizada com sucesso', type: 'success'})
        })

        it('deve buscar as informações do tooltip se a movimentação interna for do tipo temporária e estiver com a situação igual a devolução parcial', async () => {
            const wrapper = defaultMount()
            wrapper.setData({
                movimentacao: {
                    id: 7,
                    tipo: 'TEMPORARIA',
                    situacao: 'DEVOLVIDO_PARCIAL'
                }
            })
            await wrapper.vm.buscarTooltipMovimentacaoTemporaria()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_INFORMACAO_TOOLTIP]).toBeCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_INFORMACAO_TOOLTIP].mock.calls[0][1]).toEqual(wrapper.vm.$data.movimentacao.id)
        })

        it('não deve buscar as informações do tooltip se a movimentação interna mão for do tipo temporária e não estiver com a situação igual a devolução parcial', async () => {
            const wrapper = defaultMount()
            wrapper.setData({
                movimentacao: {
                    id: 7,
                    tipo: 'DEFINITIVA',
                    situacao: 'FINALIZADO'
                }
            })
            await wrapper.vm.buscarTooltipMovimentacaoTemporaria()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_INFORMACAO_TOOLTIP]).not.toBeCalled()
        })

        it('deve buscar as informações atualizadas da movimentação', async() => {
            const wrapper = defaultMount()

            const buscarTooltipMovimentacaoTemporaria = jest.spyOn(wrapper.vm, 'buscarTooltipMovimentacaoTemporaria')

            await wrapper.vm.buscarInformacoesAtualizadasMovimentacao()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_MOVIMENTACAO_INTERNA_VISUALIZACAO_REGISTRO]).toBeCalled()
            expect(buscarTooltipMovimentacaoTemporaria).toBeCalled()
        })

        it('deve validar se o usuário tem acesso ao setor destino da movimentação e retornar false', async () => {
            const wrapper = defaultMount()
            await flushPromises

            wrapper.vm.validarUsuarioEDestinatario()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_POSSUI_ACESSO]).toBeCalled()
            expect(wrapper.vm.$data.ehDestinatario).toEqual(false)
        })

        it('deve validar se o usuário tem acesso ao setor destino da movimentação e retornar true', async () => {
            actions[actionTypes.COMUM.BUSCAR_SETORES_POSSUI_ACESSO] = jest.fn().mockReturnValue({items: [{descricao: 'Assessoria Jurídica'}]})
            const wrapper = shallowMount(VisualizarRegistroMovimentacaoInterna, {
                store: new Vuex.Store({actions, mutations, state}),
                router,
                localVue
            })
            await flushPromises()

            wrapper.vm.validarUsuarioEDestinatario()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_POSSUI_ACESSO]).toBeCalled()
            expect(wrapper.vm.$data.ehDestinatario).toEqual(true)
        })
    })

})
