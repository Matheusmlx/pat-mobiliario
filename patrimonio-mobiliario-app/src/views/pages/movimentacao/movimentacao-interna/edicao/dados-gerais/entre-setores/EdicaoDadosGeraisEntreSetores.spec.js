import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {mount} from '@vue/test-utils'
import EdicaoDadosGeraisEntreSetores from './EdicaoDadosGeraisEntreSetores'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('EdicaoDadosGeraisEntreSetores', () => {

    let wrapper, store, localVue, vuetify, router, actions, mutations, state

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

    const setores = {
        items: [{id: 1}, {id: 2}],
        totalItems: 2
    }

    const entreSetores = {
        id: 1,
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
                    name: 'EdicaoDadosGeraisEntreSetores',
                    params: {movimentacaoInternaId: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        actions = {
            [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
            [actionTypes.RESPONSAVEL.BUSCAR_TODOS_RESPONSAVEIS]: jest.fn().mockReturnValue(responsaveis),
            [actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
            [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.ATUALIZAR_ENTRE_SETORES]: jest.fn().mockReturnValue(entreSetores),
            [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.BUSCAR_ENTRE_SETORES_POR_ID]: jest.fn().mockReturnValue(entreSetores)
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
        }

        store = new Vuex.Store({actions, mutations, state})
    })

    const defaultMount = () => mount(EdicaoDadosGeraisEntreSetores, {
        localVue,
        vuetify,
        store,
        router,
        mocks: {errors, moment}
    })

    describe('mounted', () => {
        it('deve buscar entreSetores e orgãos', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.BUSCAR_ENTRE_SETORES_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.BUSCAR_ENTRE_SETORES_POR_ID].mock.calls[0][1]).toEqual(router.history.current.params.movimentacaoInternaId)

            expect(actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]).toHaveBeenCalled()
            expect(wrapper.vm.entreSetores).toEqual(entreSetores)
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
            store.state.loki.user.authorities[0].name = 'Mobiliario.consulta'
            wrapper = mount(EdicaoDadosGeraisEntreSetores, {
                localVue,
                vuetify,
                store,
                router,
                mocks: {errors, moment}
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
            wrapper = defaultMount()
            await flushPromises()
            wrapper.setData({
                entreSetores: {
                    orgao: null,
                    setorDestino: null,
                    setorOrigem: null
                }
            })
            await flushPromises()

            expect(wrapper.vm.podeContinuar).toEqual(false)
            expect(wrapper.emitted().desabilitaPasso3).toBeTruthy()
        })
    })

    describe('methods', () => {
        it('deve habilitar o passo 3', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.habilitaPasso3()

            expect(wrapper.emitted().habilitaPasso3).toBeTruthy()
        })

        it('deve desabilitar o passo 3', async () => {
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
            expect(router.replace.mock.calls[0][0]).toEqual({
                name: 'MovimentacaoInternaEdicaoItens',
                params: {movimentacaoInternaId: 1}
            })
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
            wrapper.vm.entreSetores.orgao = 6
            await wrapper.vm.buscarSetoresOrigem()
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(6)
        })

        it('deve buscar setores destino', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.entreSetores.orgao = 7
            wrapper.vm.entreSetores.setorOrigem = 5
            wrapper.vm.setoresOrigem = [{id: 5}, {id: 6}, {id: 10}]
            wrapper.vm.buscarSetoresDestino()
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(7)
        })

        it('deve buscar setores destino', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.entreSetores.orgao = 7
            wrapper.vm.entreSetores.setorOrigem = 5
            wrapper.vm.setoresOrigem = [{id: 5}, {id: 6}, {id: 10}]
            wrapper.vm.buscarSetoresDestino()
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(7)
        })

        it('deve editar entreSetores', async () => {
            wrapper = defaultMount()
            wrapper.vm.entreSetores = {id: 2}
            wrapper.vm.editarEntreSetores()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.ATUALIZAR_ENTRE_SETORES]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.ATUALIZAR_ENTRE_SETORES].mock.calls[0][1]).toEqual({id: 2})
            expect(wrapper.vm.entreSetores).toEqual(entreSetores)
        })

        it('deve buscar entreSetores', async () => {
            wrapper = defaultMount()

            wrapper.vm.buscarEntreSetores()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.BUSCAR_ENTRE_SETORES_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.BUSCAR_ENTRE_SETORES_POR_ID].mock.calls[0][1]).toEqual(router.history.current.params.movimentacaoInternaId)
            expect(wrapper.vm.entreSetores).toEqual(entreSetores)
        })

        it('deve anular setor origem e destino, chamar edição da entreSetores, busca de stores origem e destino ao mudar um orgao', async () => {
            wrapper = defaultMount()

            const definitivaData = {id: 2, orgao: 8}
            wrapper.vm.entreSetores = definitivaData
            wrapper.vm.tratarEventoMudancaOrgaoOrigem()
            expect(wrapper.vm.entreSetores.setorOrigem).toEqual(null)
            expect(wrapper.vm.entreSetores.setorDestino).toEqual(null)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.ATUALIZAR_ENTRE_SETORES]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.ATUALIZAR_ENTRE_SETORES].mock.calls[0][1]).toEqual(definitivaData)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(entreSetores.orgao)

            expect(wrapper.vm.entreSetores).toEqual(entreSetores)

        })

        it('deve chamar edição da entreSetores, busca de stores origem e destino ao buscar um orgao', async () => {
            wrapper = defaultMount()

            const definitivaData = {id: 2, orgao: 8, setorOrigem: 10, setorDestino: 20}
            wrapper.vm.entreSetores = definitivaData
            wrapper.vm.trataEventoSelecaoDeOrgaoOrigem()
            expect(wrapper.vm.entreSetores.setorOrigem).toEqual(10)
            expect(wrapper.vm.entreSetores.setorDestino).toEqual(20)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.ATUALIZAR_ENTRE_SETORES]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.ATUALIZAR_ENTRE_SETORES].mock.calls[0][1]).toEqual(definitivaData)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(entreSetores.orgao)

            expect(wrapper.vm.entreSetores).toEqual(entreSetores)

        })

        it('deve anular campos de setores', async () => {
            wrapper = defaultMount()

            wrapper.vm.anulaSetoresAoMudarOrgao()

            expect(wrapper.vm.entreSetores.setorOrigem).toEqual(null)
            expect(wrapper.vm.entreSetores.setorDestino).toEqual(null)

        })

        it('deve limpar a lista de setores', async() => {
            wrapper = defaultMount()

            wrapper.vm.setoresDestino = setores

            wrapper.vm.limparListaSetores()

            expect(wrapper.vm.setoresOrigem).toEqual([])
            expect(wrapper.vm.setoresDestino).toEqual([])
        })

        it('deve atualizar as informações com os dados atualizados', async () => {
            const entreSetores = {
                dataNotaLancamentoContabil: '2021-03-01T00:00:00.000-0400',
                id: 45,
                motivoObservacao: 'motivo',
                numeroNotaLancamentoContabil: '5143NL255645',
                numeroProcesso: '98788798',
                orgao: 8773,
                responsavel: 8,
                setorDestino: 8822,
                setorOrigem: 8810,
                situacao: 'EM_ELABORACAO'
            }

            wrapper = defaultMount()

            await flushPromises()

            wrapper.vm.setarDadosAtualizados(entreSetores)

            expect(wrapper.vm.entreSetores).toEqual(entreSetores)
        })
    })
})
