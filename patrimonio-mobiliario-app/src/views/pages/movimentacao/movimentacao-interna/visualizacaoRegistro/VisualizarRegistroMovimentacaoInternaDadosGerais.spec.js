import VisualizarRegistroMovimentacaoInternaDadosGerais from './VisualizarRegistroMovimentacaoInternaDadosGerais'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'

let router, state, store, localVue

describe('VisualizarRegistroMovimentacaoInternaDadosGerais', () => {

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
            },
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

        localVue = applicationTestBuilder.build()

        const rotulosPersonalizados = {
            namespaced: true,
            getters: {
                getObrigatorioRotulosPersonalizados: () => jest.fn().mockReturnValue('false')
            }
        }

        store = new Vuex.Store({state, modules: {rotulosPersonalizados}})

    })

    const defaultMount = () => shallowMount(VisualizarRegistroMovimentacaoInternaDadosGerais, {
        propsData: {
            movimentacao: movimentacaoInterna
        },
        store,
        router,
        localVue
    })

    describe('Computed', () => {
        it('deve retornar se o usuário possui permissão para edição', () => {
            const wrapper = defaultMount()

            expect(wrapper.vm.verificaPermissaoEdicao).toBe(true)
        })

        it('deve retornar true se o tipo da movimentação for TEMPORARIA', () => {
            const wrapper = shallowMount(VisualizarRegistroMovimentacaoInternaDadosGerais, {
                propsData: {
                    movimentacao: {
                        ...movimentacaoInterna,
                        tipo: 'TEMPORARIA'
                    }
                },
                store,
                router,
                localVue
            })

            expect(wrapper.vm.movimentacaoTemporaria).toBe(true)
        })

        it('deve retornar false se o tipo da movimentação não for TEMPORARIA', () => {
            const wrapper = defaultMount()

            expect(wrapper.vm.movimentacaoTemporaria).toBe(false)
        })
    })

    describe('Methods', () => {

        it('deve emitir evento para edição dos dados da movimentação', async() => {
            const wrapper = defaultMount()

            const movimentacaoInternaAtualizada = {
                id: 7,
                codigo: '00001',
                tipo: 'DISTRIBUICAO',
                usuarioCriacao: 'Lucas',
                dataFinalizacao: '2021-01-18T04:00:00.000-0400',
                situacao: 'FINALIZADO',
                numeroNotaLancamentoContabil: '1335',
                dataNotaLancamentoContabil: '2021-01-12T04:00:00.000-0400',
                numeroProcesso: '123456',
                orgao: 'AGEPAN - Agência Estadual de Regulação',
                setorOrigem: 'Assessoria Jurídica',
                setorDestino: 'Assessoria Jurídica',
                motivoObservacao: 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.'
            }

            wrapper.vm.movimentacao.dataNotaLancamentoContabil = '2021-01-12T04:00:00.000-0400'

            await wrapper.vm.tratarEventoEdicao(movimentacaoInterna)

            expect(wrapper.emitted().editar).toBeTruthy()
            expect(wrapper.emitted().editar[0][0]).toEqual(movimentacaoInternaAtualizada)
        })

        it('Deve setar as labels setor Origem e Destino para Almoxarifado Origem e Destino caso seja uma Movimentação entre estoques', async () => {
            const wrapper = shallowMount(VisualizarRegistroMovimentacaoInternaDadosGerais,{
                propsData: {
                    movimentacao: {
                        ...movimentacaoInterna,
                        tipo: 'ENTRE_ESTOQUES'
                    }
                },
                store,
                router,
                localVue
            })

            wrapper.vm.formatarLabels()
            await  flushPromises()

            expect(wrapper.vm.setorOrigem).toEqual('Almoxarifado Origem')
            expect(wrapper.vm.setorDestino).toEqual('Almoxarifado Destino')
        })

        it('Não deve alterar as labels setor Origem e Destino caso seja uma Movimentação Temporaria', async () => {
            const wrapper = shallowMount(VisualizarRegistroMovimentacaoInternaDadosGerais,{
                propsData: {
                    movimentacao: {
                        ...movimentacaoInterna,
                        tipo: 'TEMPORARIA'
                    }
                },
                store,
                router,
                localVue
            })

            wrapper.vm.formatarLabels()
            await  flushPromises()

            expect(wrapper.vm.setorOrigem).toEqual('Setor Origem')
            expect(wrapper.vm.setorDestino).toEqual('Setor Destino')
        })

        it('Deve alterar a label setor Origem para Almoxarifado Origem caso seja uma  Distribuição', async () => {
            const wrapper = shallowMount(VisualizarRegistroMovimentacaoInternaDadosGerais,{
                propsData: {
                    movimentacao: {
                        ...movimentacaoInterna,
                        tipo: 'DISTRIBUICAO'
                    }
                },
                store,
                router,
                localVue
            })

            wrapper.vm.formatarLabels()
            await  flushPromises()

            expect(wrapper.vm.setorOrigem).toEqual('Almoxarifado Origem')
            expect(wrapper.vm.setorDestino).toEqual('Setor Destino')
        })

        it('Deve alterar a label setor Destino para Almoxarifado Destino caso seja uma  devolução almoxarifado', async () => {
            const wrapper = shallowMount(VisualizarRegistroMovimentacaoInternaDadosGerais,{
                propsData: {
                    movimentacao: {
                        ...movimentacaoInterna,
                        tipo: 'DEVOLUCAO_ALMOXARIFADO'
                    }
                },
                store,
                router,
                localVue
            })

            wrapper.vm.formatarLabels()
            await  flushPromises()

            expect(wrapper.vm.setorOrigem).toEqual('Setor Origem')
            expect(wrapper.vm.setorDestino).toEqual('Almoxarifado Destino')
        })

    })

})
