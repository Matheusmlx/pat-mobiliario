import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {mount} from '@vue/test-utils'
import EdicaoDadosGeraisEntreEstoques from './EdicaoDadosGeraisEntreEstoques'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('EdicaoDadosGeraisEntreEstoques', () => {

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

    const entreEstoques = {
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
                    name: 'EdicaoDadosGeraisEntreEstoques',
                    params: {movimentacaoInternaId: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        actions = {
            [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
            [actionTypes.RESPONSAVEL.BUSCAR_TODOS_RESPONSAVEIS]: jest.fn().mockReturnValue(responsaveis),
            [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setores),
            [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.ATUALIZAR_ENTRE_ESTOQUES]: jest.fn().mockReturnValue(entreEstoques),
            [actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.BUSCAR_ENTRE_ESTOQUES_POR_ID]: jest.fn().mockReturnValue(entreEstoques)
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
        }

        store = new Vuex.Store({actions, mutations, state})
    })

    const defaultMount = () => mount(EdicaoDadosGeraisEntreEstoques, {
        localVue,
        vuetify,
        store,
        router,
        mocks: {errors, moment}
    })

    describe('mounted', () => {
        it('deve buscar entre estoques e orgãos', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.BUSCAR_ENTRE_ESTOQUES_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.BUSCAR_ENTRE_ESTOQUES_POR_ID].mock.calls[0][1]).toEqual(router.history.current.params.movimentacaoInternaId)

            expect(actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]).toHaveBeenCalled()
            expect(wrapper.vm.entreEstoques).toEqual(entreEstoques)
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
            wrapper = mount(EdicaoDadosGeraisEntreEstoques, {
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
                entreEstoques: {
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
            wrapper.vm.entreEstoques.orgao = 6
            await wrapper.vm.buscarSetoresOrigem()
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO].mock.calls[0][1]).toEqual(6)
        })

        it('deve buscar setores destino', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.entreEstoques.orgao = 7
            wrapper.vm.entreEstoques.setorOrigem = 5
            wrapper.vm.setoresOrigem = [{id: 5}, {id: 6}, {id: 10}]
            wrapper.vm.buscarSetoresDestino()
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO].mock.calls[0][1]).toEqual(7)
        })

        it('deve pegar id do primeiro item do array de setoresOrigem se houver apenas um setor no array', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.entreEstoques.setorOrigem = null
            const setoresOrigem = [{id: 3}]
            wrapper.vm.selecionaSetorSeArrayConterApenasUmSetorOrigem(setoresOrigem)

            expect(wrapper.vm.entreEstoques.setorOrigem).toEqual(setoresOrigem[0].id)
        })

        it('deve pegar id do primeiro item do array de orgaosOrigem se houver apenas um orgao no array', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.entreEstoques.orgao = null
            const orgaosOrgiem = [{id: 5}]
            wrapper.vm.selecionaOrgaoSeArrayConterApenasUmOrgaoOrigem(orgaosOrgiem)

            expect(wrapper.vm.entreEstoques.orgao).toEqual(orgaosOrgiem[0].id)
        })

        it('deve editar entre estoques', async () => {
            wrapper = defaultMount()
            wrapper.vm.entreEstoques = {id: 2}
            wrapper.vm.editarEntreEstoques()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.ATUALIZAR_ENTRE_ESTOQUES]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.ATUALIZAR_ENTRE_ESTOQUES].mock.calls[0][1]).toEqual({id: 2})
            expect(wrapper.vm.entreEstoques).toEqual(entreEstoques)
        })

        it('deve buscar entre estoques', async () => {
            wrapper = defaultMount()

            wrapper.vm.buscarEntreEstoques()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.BUSCAR_ENTRE_ESTOQUES_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.BUSCAR_ENTRE_ESTOQUES_POR_ID].mock.calls[0][1]).toEqual(router.history.current.params.movimentacaoInternaId)
            expect(wrapper.vm.entreEstoques).toEqual(entreEstoques)
        })

        it('deve anular setor origem e destino, chamar edição da entreEstoques, busca de stores origem e destino ao mudar um orgao', async () => {
            wrapper = defaultMount()

            const entreEstoquesData = {id: 2, orgao: 8}
            wrapper.vm.entreEstoques = entreEstoquesData
            wrapper.vm.tratarEventoMudancaOrgaoOrigem()
            expect(wrapper.vm.entreEstoques.setorOrigem).toEqual(null)
            expect(wrapper.vm.entreEstoques.setorDestino).toEqual(null)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.ATUALIZAR_ENTRE_ESTOQUES]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.ATUALIZAR_ENTRE_ESTOQUES].mock.calls[0][1]).toEqual(entreEstoquesData)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO].mock.calls[0][1]).toEqual(entreEstoques.orgao)

            expect(wrapper.vm.entreEstoques).toEqual(entreEstoques)

        })

        it('deve chamar edição da entre estoques, busca de stores origem e destino ao buscar um orgao', async () => {
            wrapper = defaultMount()

            const entreEstoquesData = {id: 2, orgao: 8, setorOrigem: 10, setorDestino: 20}
            wrapper.vm.entreEstoques = entreEstoquesData
            wrapper.vm.trataEventoSelecaoDeOrgaoOrigem()
            expect(wrapper.vm.entreEstoques.setorOrigem).toEqual(10)
            expect(wrapper.vm.entreEstoques.setorDestino).toEqual(20)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.ATUALIZAR_ENTRE_ESTOQUES]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.ENTRE_ESTOQUES.ATUALIZAR_ENTRE_ESTOQUES].mock.calls[0][1]).toEqual(entreEstoquesData)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO].mock.calls[0][1]).toEqual(entreEstoques.orgao)

            expect(wrapper.vm.entreEstoques).toEqual(entreEstoques)

        })

        it('deve anular campos de setores', async () => {
            wrapper = defaultMount()

            wrapper.vm.anulaSetoresAoMudarOrgao()

            expect(wrapper.vm.entreEstoques.setorOrigem).toEqual(null)
            expect(wrapper.vm.entreEstoques.setorDestino).toEqual(null)

        })

        it('deve limpar a lista de setores', async() => {
            wrapper = defaultMount()

            wrapper.vm.setoresOrigem = setores

            wrapper.vm.limparListaSetores()

            expect(wrapper.vm.setoresOrigem).toEqual([])
            expect(wrapper.vm.setoresDestino).toEqual([])
        })

        it('deve atualizar as informações com os dados atualizados', async () => {
            const entreEstoques = {
                dataNotaLancamentoContabil: '2021-03-01T00:00:00.000-0400',
                id: 44,
                motivoObservacao: 'motivo',
                numeroNotaLancamentoContabil: '8979NL798798',
                numeroProcesso: '342534',
                orgao: 8773,
                responsavel: 6,
                setorDestino: 8775,
                setorOrigem: 8774,
                situacao: 'EM_ELABORACAO'
            }

            wrapper = defaultMount()

            await flushPromises()

            wrapper.vm.setarDadosAtualizados(entreEstoques)

            expect(wrapper.vm.entreEstoques).toEqual(entreEstoques)
        })
    })
})
