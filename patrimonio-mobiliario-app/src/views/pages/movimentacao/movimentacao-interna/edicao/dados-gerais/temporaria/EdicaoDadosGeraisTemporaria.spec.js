import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {mount} from '@vue/test-utils'
import EdicaoDadosGeraisTemporaria from './EdicaoDadosGeraisTemporaria'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('EdicaoDadosGeraisTemporaria', () => {
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

    const temporaria = {
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
        add: () => ({format: (dateTime) => dateTime}),
        utcOffset: () => ({format: (dateTime) => dateTime})
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
                    name: 'EdicaoDadosGeraisTemporaria',
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
            [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ATUALIZAR_TEMPORARIA]: jest.fn().mockReturnValue(temporaria),
            [actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_TEMPORARIA_POR_ID]: jest.fn().mockReturnValue(temporaria)
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
        }

        store = new Vuex.Store({actions, mutations, state})
    })

    const defaultMount = () => mount(EdicaoDadosGeraisTemporaria, {
        localVue,
        vuetify,
        store,
        router,
        mocks: {errors, moment}
    })

    describe('mounted', () => {
        it('deve buscar temporaria e orgãos', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_TEMPORARIA_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.BUSCAR_TEMPORARIA_POR_ID].mock.calls[0][1]).toEqual(router.history.current.params.movimentacaoInternaId)

            expect(actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]).toHaveBeenCalled()
            expect(wrapper.vm.temporaria).toEqual(temporaria)
            expect(wrapper.vm.orgaosOrigem).toEqual(orgaos.items)
        })

        it('deve permitir edição', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.verificaPermissaoEdicao).toEqual(true)
        })

        it('não deve permitir edição', async () => {
            store.state.loki.user.authorities[0].name = 'Mobiliario.consulta'
            wrapper = mount(EdicaoDadosGeraisTemporaria, {
                localVue,
                vuetify,
                store,
                router,
                mocks: {errors, moment}
            })
            await flushPromises()

            expect(wrapper.vm.verificaPermissaoEdicao).toEqual(false)
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

    describe('Methods', () => {

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

        it('Deve redirecionar o usuario para a rota MovimentacaoInternaEdicaoTipo', async () => {
            wrapper = defaultMount()

            wrapper.vm.voltar()
            await flushPromises()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: 'MovimentacaoInternaEdicaoTipo'})
        })

        it('Deve redirecionar o usuario para a rota de MovmentacaoInternaItens', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.continuar()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({
                name: 'MovimentacaoInternaEdicaoItens',
                params: {movimentacaoInternaId: 1}
            })

        })

        it('deve buscar setores destino', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.temporaria.orgao = 7
            wrapper.vm.temporaria.setorOrigem = 5
            wrapper.vm.setoresOrigem = [{id: 5}, {id: 6}, {id: 10}]
            wrapper.vm.buscarSetoresDestino()
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(1)
        })

        it('Deve anular o setor De origem e o Setor de Destino', async () => {
            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.anulaSetoresAoMudarOrgao()

            expect(wrapper.vm.temporaria.setorOrigem).toEqual(null)
            expect(wrapper.vm.temporaria.setorDestino).toEqual(null)
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

        it('Deve editar temporaria', async () => {
            wrapper = defaultMount()
            wrapper.vm.temporaria = {id: 2,setorDestino:3,setorOrigem:2}
            wrapper.vm.editarTemporaria()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ATUALIZAR_TEMPORARIA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ATUALIZAR_TEMPORARIA].mock.calls[0][1]).toEqual({id: 2,setorDestino:3,setorOrigem:2})
            expect(wrapper.vm.temporaria).toEqual(temporaria)
        })

        it('deve anular setor origem e destino, chamar edição da temporaria, busca de stores origem e destino ao mudar um orgao', async () => {
            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.tratarEventoMudancaOrgaoOrigem()
            expect(wrapper.vm.temporaria.setorOrigem).toEqual(null)
            expect(wrapper.vm.temporaria.setorDestino).toEqual(null)

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ATUALIZAR_TEMPORARIA]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.ATUALIZAR_TEMPORARIA].mock.calls[0][1]).toEqual(temporaria)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(temporaria.orgao)

            expect(wrapper.vm.temporaria).toEqual(temporaria)

        })

        it('deve anular campos de setores', async () => {
            wrapper = defaultMount()

            wrapper.vm.anulaSetoresAoMudarOrgao()

            expect(wrapper.vm.temporaria.setorOrigem).toEqual(null)
            expect(wrapper.vm.temporaria.setorDestino).toEqual(null)

        })

        it('deve limpar a lista de setores', async() => {
            wrapper = defaultMount()

            wrapper.vm.setoresOrigem = setores
            wrapper.vm.setoresDestino = setores

            wrapper.vm.limparListaSetores()

            expect(wrapper.vm.setoresOrigem).toEqual([])
            expect(wrapper.vm.setoresDestino).toEqual([])
        })

        it('deve atualizar as informações com os dados atualizados', async () => {
            const temporaria = {
                dataDevolucao: '2021-03-14T00:00:00.000-0400',
                dataNotaLancamentoContabil: '2021-03-01T00:00:00.000-0400',
                id: 47,
                motivoObservacao: 'motivo',
                numeroNotaLancamentoContabil: '8795NL434759',
                numeroProcesso: '345241234',
                orgao: 8773,
                responsavel: 10,
                setorDestino: 8822,
                setorOrigem: 8810,
                situacao: 'EM_ELABORACAO'
            }

            wrapper = defaultMount()

            await flushPromises()

            wrapper.vm.setarDadosAtualizados(temporaria)

            expect(wrapper.vm.temporaria).toEqual(temporaria)
        })

    })
})
