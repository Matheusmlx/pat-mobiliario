import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {mount} from '@vue/test-utils'
import EdicaoDadosGeraisDevolucaoAlmoxarifado from './EdicaoDadosGeraisDevolucaoAlmoxarifado'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('EdicaoDadosGeraisDevolucaoAlmoxarifado', () => {

    let wrapper, store, localVue, vuetify, router, actions, mutations, state, devolucaoAlmoxarifado

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
        items: [{id: 1}, {id: 2}],
        totalItems: 2
    }

    const setoresDestino = {
        items: [{id: 2}, {id: 3}],
        totalItems: 2
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

        devolucaoAlmoxarifado = {
            orgao: 7,
            setorOrigem: 2,
            setorDestino: 3
        }

        router = {
            routes: [],
            push: jest.fn(),
            replace: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'EdicaoDadosGeraisDevolucaoAlmoxarifado',
                    params: {movimentacaoInternaId: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        actions = {
            [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(orgaos),
            [actionTypes.RESPONSAVEL.BUSCAR_TODOS_RESPONSAVEIS]: jest.fn().mockReturnValue(responsaveis),
            [actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]: jest.fn().mockReturnValue(setoresDestino),
            [actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]: jest.fn().mockReturnValue(setoresOrigem),
            [actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.ATUALIZAR_DEVOLUCAO_ALMOXARIFADO]: jest.fn().mockReturnValue(devolucaoAlmoxarifado),
            [actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.BUSCAR_DEVOLUCAO_ALMOXARIFADO_POR_ID]: jest.fn().mockReturnValue(devolucaoAlmoxarifado)
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
        }

        store = new Vuex.Store({actions, mutations, state})
    })

    const defaultMount = () => mount(EdicaoDadosGeraisDevolucaoAlmoxarifado, {
        localVue,
        vuetify,
        store,
        router,
        mocks: {errors, moment}
    })

    describe('mounted', () => {
        it('deve buscar devolução almoxaridado e orgãos', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.BUSCAR_DEVOLUCAO_ALMOXARIFADO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.BUSCAR_DEVOLUCAO_ALMOXARIFADO_POR_ID].mock.calls[0][1]).toEqual(router.history.current.params.movimentacaoInternaId)

            expect(actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]).toHaveBeenCalled()
            expect(wrapper.vm.devolucaoAlmoxarifado).toEqual(devolucaoAlmoxarifado)
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
            wrapper = mount(EdicaoDadosGeraisDevolucaoAlmoxarifado, {
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
                devolucaoAlmoxarifado: {
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
            wrapper.vm.devolucaoAlmoxarifado.orgao = 6
            await wrapper.vm.buscarSetoresOrigem()
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(6)
        })

        it('deve buscar setores destino', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.devolucaoAlmoxarifado.orgao = 7
            wrapper.vm.devolucaoAlmoxarifado.setorOrigem = 5
            wrapper.vm.setoresOrigem = [{id: 5}, {id: 6}, {id: 10}]
            wrapper.vm.buscarSetoresDestino()
            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO].mock.calls[0][1]).toEqual(7)
        })


        it('deve pegar id do primeiro item do array de setoresOrigem se houver apenas um setor no array', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.devolucaoAlmoxarifado.setorOrigem = null
            const setoresOrigem = [{id: 3}]
            wrapper.vm.selecionaSetorSeArrayConterApenasUmSetorOrigem(setoresOrigem)

            expect(wrapper.vm.devolucaoAlmoxarifado.setorOrigem).toEqual(setoresOrigem[0].id)
        })

        it('deve pegar id do primeiro item do array de setoresDestino se houver apenas um setor no array', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.devolucaoAlmoxarifado.setorDestino = null
            const setoresDestino = [{id: 4}]
            wrapper.vm.selecionaSetorSeArrayConterApenasUmSetorDestino(setoresDestino)

            expect(wrapper.vm.devolucaoAlmoxarifado.setorDestino).toEqual(setoresDestino[0].id)
        })

        it('deve pegar id do primeiro item do array de orgaosOrigem se houver apenas um orgao no array', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.devolucaoAlmoxarifado.orgao = null
            const orgaosOrgiem = [{id: 5}]
            wrapper.vm.selecionaOrgaoSeArrayConterApenasUmOrgaoOrigem(orgaosOrgiem)

            expect(wrapper.vm.devolucaoAlmoxarifado.orgao).toEqual(orgaosOrgiem[0].id)
        })

        it('deve editar devolução almoxaridado', async () => {
            wrapper = defaultMount()
            wrapper.vm.devolucaoAlmoxarifado = {id: 2}
            wrapper.vm.editarDevolucaoAlmoxarifado()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.ATUALIZAR_DEVOLUCAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.ATUALIZAR_DEVOLUCAO_ALMOXARIFADO].mock.calls[0][1]).toEqual({id: 2})
            expect(wrapper.vm.devolucaoAlmoxarifado).toEqual(devolucaoAlmoxarifado)
        })

        it('deve buscar devolução almoxaridado', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.buscarDevolucaoAlmoxarifado()
            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.BUSCAR_DEVOLUCAO_ALMOXARIFADO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.BUSCAR_DEVOLUCAO_ALMOXARIFADO_POR_ID].mock.calls[0][1]).toEqual(router.history.current.params.movimentacaoInternaId)
            expect(wrapper.vm.devolucaoAlmoxarifado).toEqual(devolucaoAlmoxarifado)
        })

        it('deve anular setor origem e destino, chamar edição da devolução almoxaridado, busca de stores origem e destino ao mudar um orgao', async () => {
            wrapper = defaultMount()

            const devolucaoAlmoxarifadoData = {id: 2, orgao: 8}
            wrapper.vm.devolucaoAlmoxarifado = devolucaoAlmoxarifadoData
            wrapper.vm.tratarEventoMudancaOrgaoOrigem()
            expect(wrapper.vm.devolucaoAlmoxarifado.setorOrigem).toEqual(null)
            expect(wrapper.vm.devolucaoAlmoxarifado.setorDestino).toEqual(null)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.ATUALIZAR_DEVOLUCAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.ATUALIZAR_DEVOLUCAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(devolucaoAlmoxarifadoData)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO].mock.calls[0][1]).toEqual(devolucaoAlmoxarifado.orgao)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(devolucaoAlmoxarifado.orgao)

            expect(wrapper.vm.devolucaoAlmoxarifado).toEqual(devolucaoAlmoxarifado)

        })

        it('deve chamar edição da devolução almoxaridado, busca de stores origem e destino ao buscar um orgao', async () => {
            wrapper = defaultMount()

            const devolucaoAlmoxarifadoData = {id: 2, orgao: 8, setorOrigem: 10, setorDestino: 20}
            wrapper.vm.devolucaoAlmoxarifado = devolucaoAlmoxarifadoData
            wrapper.vm.trataEventoSelecaoDeOrgaoOrigem()
            expect(wrapper.vm.devolucaoAlmoxarifado.setorOrigem).toEqual(10)
            expect(wrapper.vm.devolucaoAlmoxarifado.setorDestino).toEqual(20)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.ATUALIZAR_DEVOLUCAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.ATUALIZAR_DEVOLUCAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(devolucaoAlmoxarifadoData)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO].mock.calls[0][1]).toEqual(devolucaoAlmoxarifado.orgao)

            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO]).toHaveBeenCalled()
            expect(actions[actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO].mock.calls[0][1]).toEqual(devolucaoAlmoxarifado.orgao)

            expect(wrapper.vm.devolucaoAlmoxarifado).toEqual(devolucaoAlmoxarifado)

        })

        it('deve anular campos de setores', async () => {
            wrapper = defaultMount()

            wrapper.vm.anulaSetoresAoMudarOrgao()

            expect(wrapper.vm.devolucaoAlmoxarifado.setorOrigem).toEqual(null)
            expect(wrapper.vm.devolucaoAlmoxarifado.setorDestino).toEqual(null)

        })

        it('deve limpar a lista de setores', async () => {
            wrapper = defaultMount()

            wrapper.vm.setoresOrigem = setoresOrigem
            wrapper.vm.setoresDestino = setoresDestino

            wrapper.vm.limparListaSetores()

            expect(wrapper.vm.setoresOrigem).toEqual([])
            expect(wrapper.vm.setoresDestino).toEqual([])
        })

        it('deve atualizar a devoluçãoAlmoxarifado', async () => {
            devolucaoAlmoxarifado = {
                dataNotaLancamentoContabil: '2021-03-01T00:00:00.000-0400',
                id: 42,
                motivoObservacao: 'motivo',
                numeroNotaLancamentoContabil: '8989NL713475',
                numeroProcesso: '23423',
                orgao: 109,
                responsavel: 5,
                setorDestino: 1164,
                setorOrigem: 7166,
                situacao: 'EM_ELABORACAO'
            }

            wrapper = defaultMount()

            await flushPromises()

            wrapper.vm.setarDadosAtualizados(devolucaoAlmoxarifado)

            expect(wrapper.vm.devolucaoAlmoxarifado).toEqual(devolucaoAlmoxarifado)
        })

        it('Deve setar o setorDestino caso o mesmo não seja igual ao setorOrigem', async () => {

            wrapper = defaultMount()

            await flushPromises()

            wrapper.vm.editarSetorDestino(524)

            expect(wrapper.vm.devolucaoAlmoxarifado.setorDestino).toEqual(524)

        })

        it('Não deve setar o setorDestino caso o mesmo seja igual ao setorOrigem', async () => {
            devolucaoAlmoxarifado = {
                setoresOrigem: 2,
                setoresDestino: 3
            }

            wrapper = defaultMount()

            await flushPromises()

            wrapper.vm.editarSetorDestino(2)

            expect(wrapper.vm.devolucaoAlmoxarifado.setorDestino).toEqual(3)

        })

        it('Deve setar o setorOrigem caso o mesmo não seja igual ao setorDestino', async () => {
            devolucaoAlmoxarifado = {
                setorDestino: 2
            }

            actions = {
                [actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.BUSCAR_DEVOLUCAO_ALMOXARIFADO_POR_ID]: jest.fn().mockReturnValue(devolucaoAlmoxarifado)
            }

            wrapper = defaultMount()

            await flushPromises()

            wrapper.vm.editarSetorOrigem(524)

            expect(wrapper.vm.devolucaoAlmoxarifado.setorOrigem).toEqual(524)

        })

        it('Não deve setar o setorOrigem caso o mesmo seja igual ao setorDestino', async () => {

            devolucaoAlmoxarifado = {
                setorDestino: 3,
                setorOrigem: 2
            }

            wrapper = defaultMount()

            await flushPromises()

            wrapper.vm.editarSetorOrigem(3)

            expect(wrapper.vm.devolucaoAlmoxarifado.setorOrigem).toEqual(2)

        })
    })
})
