import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {mount} from '@vue/test-utils'
import EdicaoDadosGeraisDistribuicao from './EdicaoDadosGeraisDistribuicao'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('EdicaoDadosGeraisDistribuicao', () => {

    let wrapper, localVue, vuetify, router, actions, mutations, state

    const orgaos = {
        items: [
            {
                id: 1,
                nome: 'orgao',
                sigla: 'sigla'
            },
            {
                id: 2
            }
        ],
        totalItems: 2
    }

    const setoresOrigem = {
        items: [{id: 1},{id: 2}],
        totalItems: 2
    }

    const setoresDestino = {
        items: [{id: 2},{id: 3}],
        totalItems: 2
    }

    const distribuicao = {
        id:1,
        motivoObservacao: 'motivo',
        orgao: 1,
        setorOrigem: 2,
        setorDestino: 3
    }

    const responsaveis = [
        {
            id: 1,
            nome: 'Maria'
        },
        {
            id: 2,
            nome: 'João'
        },
        {
            id: 3,
            nome: 'Cleber'
        }
    ]

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
            getTooltipRotulosPersonalizados: () => jest.fn(),
            getObrigatorioRotulosPersonalizados: () => jest.fn(),
            getDistribuicaoValidado: () => jest.fn().mockReturnValue(true)
        }
    }
    const errors = {
        collect() {
        },
        clear: jest.fn()
    }

    const moment = () => ({
        format: jest.fn(),
        tz: jest.fn().mockReturnValue({
            format: jest.fn().mockReturnValue('-04:00')
        }),
        utcOffset: jest.fn().mockReturnValue({
            format: (dateTime) => dateTime
        })
    })

    const createDefaultStore = () => new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}})

    beforeEach(() => {

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Movimentacoes', hasAccess: true}]
                }
            },
            movimentacaointerna: {
                situacaoMovimentacaoInterna: 'EM_ELABORACAO'
            }
        }

        router = {
            routes: [],
            push: jest.fn(),
            replace: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'EdicaoDadosGeraisDistribuicao',
                    params: { movimentacaoInternaId: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        actions = {
            [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS] :jest.fn().mockReturnValue(orgaos),
            [actionTypes.RESPONSAVEL.BUSCAR_TODOS_RESPONSAVEIS]: jest.fn().mockReturnValue(responsaveis),
            [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO] :jest.fn().mockReturnValue(setoresOrigem),
            [actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO] :jest.fn().mockReturnValue(setoresDestino),
            [actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.ATUALIZAR_DISTRIBUICAO] :jest.fn().mockReturnValue(distribuicao),
            [actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.BUSCAR_DISTRIBUICAO_POR_ID] :jest.fn().mockReturnValue(distribuicao)
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
        }
    })

    const defaultMount = () => mount(EdicaoDadosGeraisDistribuicao, {
        localVue,
        vuetify,
        store: createDefaultStore(),
        router,
        mocks:{errors, moment}
    })

    describe('mounted', () => {
        it('deve buscar distribuição e orgãos', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.BUSCAR_DISTRIBUICAO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.BUSCAR_DISTRIBUICAO_POR_ID].mock.calls[0][1]).toEqual(router.history.current.params.movimentacaoInternaId)

            expect(actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]).toHaveBeenCalled()
            expect(wrapper.vm.distribuicao).toEqual(distribuicao)
            expect(wrapper.vm.orgaosOrigem).toEqual(orgaos.items)
        })
    })

    describe('computed', () => {
        it('deve permitir edição', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.verificaPermissaoEdicao).toEqual(true)
        })

        it('não deve permitir edição', async () => {
            state.loki.user.authorities[0].name = 'Mobiliario.consulta'
            wrapper = mount(EdicaoDadosGeraisDistribuicao, {
                localVue,
                vuetify,
                store: createDefaultStore(),
                router,
                mocks:{errors, moment}
            })
            await flushPromises()

            expect(wrapper.vm.verificaPermissaoEdicao).toEqual(false)
        })

        it('deve permitir continuar', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.podeContinuar).toEqual(true)
            expect(wrapper.emitted().habilitaPasso3).toBeTruthy()
        })

        it('não deve permitir continuar', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                actions: {getAllRotulosPersonlizados: jest.fn()},
                getters: {
                    getNomeRotulosPersonalizados: () => jest.fn(),
                    getTooltipRotulosPersonalizados: () => jest.fn(),
                    getObrigatorioRotulosPersonalizados: () => jest.fn(),
                    getDistribuicaoValidado: () => jest.fn().mockReturnValue(false)
                }
            }
            wrapper = mount(EdicaoDadosGeraisDistribuicao, {
                localVue,
                vuetify,
                store: new Vuex.Store({actions, mutations, state, modules: {rotulosPersonalizados}}),
                router,
                mocks:{errors, moment}
            })
            await flushPromises()

            expect(wrapper.vm.podeContinuar).toEqual(false)
            expect(wrapper.emitted().desabilitaPasso3).toBeTruthy()
        })
    })

    describe('methods', () => {
        it('deve habilitar o passo 3', async() => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.habilitaPasso3()

            expect(wrapper.emitted().habilitaPasso3).toBeTruthy()
        })

        it('deve desabilitar o passo 3', async() => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.desabilitaPasso3()

            expect(wrapper.emitted().desabilitaPasso3).toBeTruthy()
        })

        it('deve redirecionar para MovimentacaoInternaEdicaoTipo', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.voltar()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoTipo'})
        })

        it('deve redirecionar para MovimentacaoInternaEdicaoItens', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.continuar()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoItens', params: {movimentacaoInternaId: 1}})
        })

        it('deve validar o valor nome digitado no auto-complete', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.filtroComboAutoComplete(orgaos.items[0], 'org')).toBeTruthy()
        })

        it('deve validar o valor sigla digitado no auto-complete', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.filtroComboAutoComplete(orgaos.items[0], 'sig')).toBeTruthy()
        })

        it('não deve validar o valor digitado no auto-complete', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.filtroComboAutoComplete(orgaos.items[0], 'ab')).not.toBeTruthy()
        })

        it('deve buscar orgãos', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.buscarOrgaosOrigem()
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]).toHaveBeenCalled()
            expect(wrapper.vm.orgaosOrigem).toEqual(orgaos.items)
        })

        it('deve buscar setores origem', async () => {
            wrapper = defaultMount()
            wrapper.vm.distribuicao.orgao = 6
            wrapper.vm.buscarSetoresOrigem()
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO].mock.calls[0][1]).toEqual(6)
            expect(wrapper.vm.setoresOrigem).toEqual(setoresOrigem.items)
            expect(wrapper.vm.distribuicao.setorOrigem).toEqual(2)
        })

        it('deve buscar setores destino', async () => {
            wrapper = defaultMount()
            wrapper.vm.distribuicao.orgao = 7
            wrapper.vm.buscarSetoresDestino()
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(7)
            expect(wrapper.vm.setoresDestino).toEqual(setoresDestino.items)
            expect(wrapper.vm.distribuicao.setorDestino).toEqual(3)
        })

        it('deve pegar id do primeiro item do array de setoresOrigem se houver apenas um setor no array', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.distribuicao.setorOrigem = null
            const setoresOrigem = [{id:3}]
            wrapper.vm.selecionaSetorSeArrayConterApenasUmSetorOrigem(setoresOrigem)

            expect(wrapper.vm.distribuicao.setorOrigem).toEqual(setoresOrigem[0].id)
        })

        it('deve pegar id do primeiro item do array de setoresDestino se houver apenas um setor no array', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.distribuicao.setorDestino = null
            const setoresDestino = [{id:4}]
            wrapper.vm.selecionaSetorSeArrayConterApenasUmSetorDestino(setoresDestino)

            expect(wrapper.vm.distribuicao.setorDestino).toEqual(setoresDestino[0].id)
        })

        it('deve pegar id do primeiro item do array de orgaosOrigem se houver apenas um orgao no array', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.distribuicao.orgao = null
            const orgaosOrgiem = [{id:5}]
            wrapper.vm.selecionaOrgaoSeArrayConterApenasUmOrgaoOrigem(orgaosOrgiem)

            expect(wrapper.vm.distribuicao.orgao).toEqual(orgaosOrgiem[0].id)
        })

        it('deve editar distribuição', async () => {
            wrapper = defaultMount()
            wrapper.vm.distribuicao = {id:2}
            wrapper.vm.editarDistribuicao()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.ATUALIZAR_DISTRIBUICAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.ATUALIZAR_DISTRIBUICAO].mock.calls[0][1]).toEqual({id:2})
            expect(wrapper.vm.distribuicao).toEqual(distribuicao)
        })

        it('deve buscar distribuição', async () => {
            wrapper = defaultMount()

            wrapper.vm.buscarDistribuicao()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.BUSCAR_DISTRIBUICAO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.BUSCAR_DISTRIBUICAO_POR_ID].mock.calls[0][1]).toEqual(router.history.current.params.movimentacaoInternaId)
            expect(wrapper.vm.distribuicao).toEqual(distribuicao)
        })

        it('deve anular setor origem e destino, chamar edição da distribuição, busca de stores origem e destino ao mudar um orgao', async () => {
            wrapper = defaultMount()

            const distribuicaoData = {id:2, orgao:8}
            wrapper.vm.distribuicao = distribuicaoData
            wrapper.vm.tratarEventoMudancaOrgaoOrigem()
            expect(wrapper.vm.distribuicao.setorOrigem).toEqual(null)
            expect(wrapper.vm.distribuicao.setorDestino).toEqual(null)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.ATUALIZAR_DISTRIBUICAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.ATUALIZAR_DISTRIBUICAO].mock.calls[0][1]).toEqual(distribuicaoData)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO].mock.calls[0][1]).toEqual(distribuicao.orgao)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(distribuicao.orgao)

            expect(wrapper.vm.distribuicao).toEqual(distribuicao)

        })

        it('deve chamar edição da distribuição, busca de stores origem e destino ao buscar um orgao', async () => {
            wrapper = defaultMount()

            const distribuicaoData = {id:2, orgao:8, setorOrigem: 10, setorDestino: 20 }
            wrapper.vm.distribuicao = distribuicaoData
            wrapper.vm.trataEventoSelecaoDeOrgaoOrigem()
            expect(wrapper.vm.distribuicao.setorOrigem).toEqual(10)
            expect(wrapper.vm.distribuicao.setorDestino).toEqual(20)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.ATUALIZAR_DISTRIBUICAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.ATUALIZAR_DISTRIBUICAO].mock.calls[0][1]).toEqual(distribuicaoData)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO].mock.calls[0][1]).toEqual(distribuicao.orgao)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(distribuicao.orgao)

            expect(wrapper.vm.distribuicao).toEqual(distribuicao)

        })

        it('deve anular campos de setores', async () => {
            wrapper = defaultMount()

            wrapper.vm.anulaSetoresAoMudarOrgao()

            expect(wrapper.vm.distribuicao.setorOrigem).toEqual(null)
            expect(wrapper.vm.distribuicao.setorDestino).toEqual(null)

        })

        it('deve limpar a lista de setores', async() => {
            wrapper = defaultMount()

            wrapper.vm.setoresOrigem = setoresOrigem
            wrapper.vm.setoresDestino = setoresDestino

            wrapper.vm.limparListaSetores()

            expect(wrapper.vm.setoresOrigem).toEqual([])
            expect(wrapper.vm.setoresDestino).toEqual([])
        })

        it('deve atualizar as informações com os dados atualizados', async () => {
            const distribuicao = {
                dataNotaLancamentoContabil: '2021-03-01T00:00:00.000-0400',
                id: 41,
                motivoObservacao: 'asdf',
                numeroNotaLancamentoContabil: null,
                numeroProcesso: '1234123',
                orgao: 8773,
                responsavel: 7,
                setorDestino: 8822,
                setorOrigem: 8774,
                situacao: 'EM_ELABORACAO'
            }

            wrapper = defaultMount()

            await flushPromises()

            wrapper.vm.setarDadosAtualizados(distribuicao)

            expect(wrapper.vm.distribuicao).toEqual(distribuicao)
        })

    })
})
