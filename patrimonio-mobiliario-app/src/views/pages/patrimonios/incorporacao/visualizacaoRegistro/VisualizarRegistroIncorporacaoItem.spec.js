import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import actionTypes from '@/core/constants/actionTypes'
import mutationTypes from '@/core/constants/mutationTypes'
import {shallowMount} from '@vue/test-utils'
import VisualizarRegistroIncorporacaoItem from './VisualizarRegistroIncorporacaoItem.vue'

describe('VisualizarRegistroIncorporacaoItem.vue', () => {

    let wrapper, actions, mutations, localVue, vuetify, state, router

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    const propsData = {
        incorporacaoId: 7
    }

    const returnValue = {
        items: [
            {
                id: 8,
                quantidade: 34,
                valorTotal: 0.23,
                numeracaoPatrimonial: 'AUTOMATICA',
                tipoBem: 'VEICULO',
                uriImagem: 'repo1:patrimoniomobiliario/image20200701T214413922Z.png',
                anoFabricacaoModelo: null,
                combustivel: null,
                categoria: null,
                descricao: 'Caminhonete pick up - descrição: cabine dupla tração 4x4 motor 163 CV, direção hidráulica, CRLV original nº 77743511036 - placa JJU0801 - Chassi: 8AFER13P9AJ285974 -',
                codigo: '0000019',
                situacao: 'FINALIZADO',
                contaContabil: 41,
                naturezaDespesa: 1,
                estadoConservacao: 1
            },
            {
                id: 12,
                quantidade: 30,
                valorTotal: 5102.20,
                numeracaoPatrimonial: 'AUTOMATICA',
                tipoBem: null,
                uriImagem :null,
                anoFabricacaoModelo: null,
                combustivel: null,
                categoria: null,
                descricao: 'Caminhão semi-pesado, zero quilômetro, cabina semi-avançada,motor diesel,turbinado, potência mínima de 200 cv, branco equipado com 3ø eixo, com dispositivo de leva...',
                codigo: '0053235',
                imagem: null,
                situacao: 'FINALIZADO',
                contaContabil: 42,
                naturezaDespesa: 2,
                estadoConservacao: 1
            }
        ],
        totalPages: 2,
        totalElements: 11
    }

    beforeEach( async() => {
        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {
                    name:'VisualizarRegistroIncorporacao',
                    params: {
                        incorporacaoId: propsData.incorporacaoId
                    }
                },
            }
        }

        state = {
            itemIncorporacao: {
                buscaRegistroItensIncorporacao: {
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
                        defaultSortBy: ['codigo'],
                        sortDesc: [false]
                    }
                }
            }
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_REGISTRO_ITENS_INCORPORACAO]: jest.fn().mockReturnValue(returnValue)
        }

        mutations = {
            [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_PAGINACAO_BUSCA_ITENS_INCORPORADOS]: jest.fn(),
            [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_BUSCA_ITENS_INCORPORADOS]: jest.fn()
        }
    })

    const defaultMount = () => shallowMount(VisualizarRegistroIncorporacaoItem, {
        localVue,
        propsData,
        vuetify,
        router,
        store: new Vuex.Store({actions, mutations, state})
    })

    describe('Watch', async () => {
        it('Deve buscar a primeira página de itens quando trocar a incorporação', async () => {
            const novoIncorporacaoId = 2

            wrapper = defaultMount()
            await flushPromises()

            wrapper.setProps({
                incorporacaoId: novoIncorporacaoId
            })
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_BUSCA_ITENS_INCORPORADOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_REGISTRO_ITENS_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_REGISTRO_ITENS_INCORPORACAO].mock.calls[1][1]).toBe(novoIncorporacaoId)
        })
    })

    describe('Methods', () => {
        it('Deve chamar método para buscar itens incorporados', async() => {
            wrapper = defaultMount()

            const buscarItensIncorporacao = jest.spyOn(wrapper.vm, 'buscarItensIncorporacao')

            await flushPromises()

            expect(buscarItensIncorporacao).toHaveBeenCalled()
        })

        it('Deve emitir evento para acessar item', async() => {
            wrapper = defaultMount()
            const item={id: 1}
            wrapper.vm.tratarEventoAcessar(item)

            expect(router.push.mock.calls[0][0]).toEqual({name: 'ModalItemVisualizarRegistro', params: {incorporacaoId: 7, itemIncorporacaoId: 1}})
        })

        it('Deve buscar itens incorporação', async() => {
            wrapper = defaultMount()

            wrapper.vm.buscarItensIncorporacao()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_REGISTRO_ITENS_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_REGISTRO_ITENS_INCORPORACAO].mock.calls[0][1]).toBe(7)
        })

        it('Deve tratar evento paginar', async() => {
            wrapper = defaultMount()

            const paginacao = {
                page: 3,
                rowsPerPage: 10,
                sortBy: ['codigo'],
                defaultSortBy: ['codigo'],
                sortDesc: [false]
            }

            wrapper.vm.tratarEventoPaginar(paginacao)

            const buscarItensIncorporacao = jest.spyOn(wrapper.vm, 'buscarItensIncorporacao')

            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_PAGINACAO_BUSCA_ITENS_INCORPORADOS]).toHaveBeenCalled()
            expect(buscarItensIncorporacao).toHaveBeenCalled()
        })
    })

})
