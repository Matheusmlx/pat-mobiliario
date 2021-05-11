import {shallowMount} from '@vue/test-utils'
import VisualizarRegistroIncorporacaoItensEstornados from './VisualizarRegistroIncorporacaoItensEstornados'
import actionTypes from '@/core/constants/actionTypes'
import mutationTypes from '@/core/constants/mutationTypes'
import ApplicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'

describe('VisualizarRegistroIncorporacaoItensEstornados', () => {

    let state, actions, mutations, localVue, vuetify, router

    const propsData = {
        incorporacaoId: 7
    }

    localVue = ApplicationTestBuilder.build()
    vuetify = ApplicationTestBuilder.getVuetify()

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                }
            },
            patrimonio: {
                resultadoBuscaTodosPatrimoniosEstornados: {
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
                }
            }
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {
                    params: {
                        incorporacaoId: propsData.incorporacaoId
                    }
                },
            }
        }

        actions = {
            [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_ESTORNADOS]: jest.fn().mockReturnValue({})
        }

        mutations = {
            [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS]: jest.fn(),
            [mutationTypes.PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS]: jest.fn(),
        }
    })

    const defaultMount = () => (
        shallowMount(VisualizarRegistroIncorporacaoItensEstornados, {
            localVue,
            vuetify,
            router,
            store: new Vuex.Store({actions, mutations, state})
        })
    )

    describe('Watch', () => {

        it('Deve buscar os patrimônios estornados quando trocar a incorporação', async () => {
            const wrapper = defaultMount()
            await flushPromises()

            const novoIncorporacaoId = 1

            wrapper.setProps({
                incorporacaoId: novoIncorporacaoId
            })
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_ESTORNADOS]).toBeCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_ESTORNADOS].mock.calls[1][1]).toBe(novoIncorporacaoId)
        })

    })

    describe('Methods', () => {

        it('deve montar componente corretamente', async () => {
            const wrapper = defaultMount()

            const buscarPatrimoniosEstornados = jest.spyOn(wrapper.vm, 'buscarPatrimoniosEstornados')

            await flushPromises()

            expect(wrapper.vm.paginacaoInterna).toBe(state.patrimonio.resultadoBuscaTodosPatrimoniosEstornados.paginacao)
            expect(buscarPatrimoniosEstornados).toBeCalled()
        })

        it('deve paginar elemento quando paginação mudar', async () => {
            const wrapper = defaultMount()

            const paginacao = {
                defaultSortBy: ['numero'],
                descending: false,
                groupBy: [],
                groupDesc: [],
                itemsPerPage: 10,
                multiSort: false,
                mustSort: false,
                page: 2,
                rowsPerPage: 10,
                sortBy: ['numero'],
                sortDesc: [false]
            }

            const buscarPatrimoniosEstornados = jest.spyOn(wrapper.vm, 'buscarPatrimoniosEstornados')

            wrapper.vm.paginacaoInterna.page = 2

            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS]).toBeCalled()
            expect(mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS].mock.calls[0][1]).toEqual(paginacao)
            expect(buscarPatrimoniosEstornados).toBeCalled()
        })

        it('Deve emitir evento para mostrar motivo do estorno', async () => {
            const wrapper = defaultMount()

            const item = {
                numero: '000000001',
                descricao: 'automovel',
                valor: 100.54,
                usuario: 'admin',
                motivo: 'motivo'
            }

            await wrapper.vm.tratarEventoMostrarMotivo(item)

            expect(wrapper.emitted().mostrarMotivoEstorno).toBeTruthy()
            expect(wrapper.emitted().mostrarMotivoEstorno[0][0]).toEqual(item)
        })

        it('Deve redirecionar o usuario para a tela FichaPatrimonioDadosGerais caso ele tenha permissão de edição', async () => {

            state.loki.user.authorities[0].hasAccess = true

            const wrapper = defaultMount()

            const item = {id:1}

            wrapper.vm.tratarEventoAcessar(item)

            await flushPromises()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'FichaPatrimonioDadosGerais', params: {patrimonioId: 1}})
        })

        it('Deve redirecionar o usuario para a tela FichaPatrimonioDadosGeraisVisualizacao caso ele não tenha permissão de edição', async () => {

            state.loki.user.authorities[0].hasAccess = false

            const wrapper = defaultMount()

            const item = {id:1}

            wrapper.vm.tratarEventoAcessar(item)

            await flushPromises()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'FichaPatrimonioDadosGeraisVisualizacao', params: {patrimonioId: 1}})
        })
    })
})
