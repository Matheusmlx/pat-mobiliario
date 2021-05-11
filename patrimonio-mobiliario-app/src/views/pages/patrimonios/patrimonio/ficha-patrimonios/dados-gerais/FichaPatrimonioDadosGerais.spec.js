import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import FichaPatrimonioDadosGerais from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/dados-gerais/FichaPatrimonioDadosGerais'

describe('FichaPatrimonioDadosGerais', () => {
    let wrapper, mutations, actions, localVue, router, state, store

    const patrimonio = {
        id: 1,
        numero: 132323,
        dataIncorporado: '2020-09-01T23:00:00.000-0400',
        situacao: 'ATIVO',
        setor: 'FUNDESPORTE - Fundação de Desporto e Lazer de Mato Grosso do Sul',
        orgao: 'FUNDESPORTE - Fundação de Desporto e Lazer de Mato Grosso do Sul',
        descricao: 'FUNDESPORTE - Fundação de Desporto e Lazer de Mato Grosso do Sul',
        reconhecimento: 'FUNDESPORTE - Fundação de Desporto e Lazer de Mato Grosso do Sul',
        numeroSerie: '1234',
        tipo: 'VEICULO',
        placa: 'JDJ5A56',
        renavam: '12345678901234567',
        licenciamento: 'JANEIRO',
        motor: '12345678901234567',
        chassi: 'ssd324732rewur3sW',
        categoria: 'abcgdij',
        metodo: 'QUOTAS_CONSTANTES',
        valorDepreciadoMensal: 0,
        valorDepreciadoAcumulado: 0,
        prazoVidaUtil: 9
    }
    const patrimonioRetorno = {
        id: 1,
        numero: 132323,
        dataIncorporado: '2020-09-01T23:00:00.000-0400',
        situacao: 'ATIVO',
        setor: 'FUNDESPORTE - Fundação de Desporto e Lazer de Mato Grosso do Sul',
        orgao: 'FUNDESPORTE - Fundação de Desporto e Lazer de Mato Grosso do Sul',
        descricao: 'FUNDESPORTE - Fundação de Desporto e Lazer de Mato Grosso do Sul',
        reconhecimento: 'FUNDESPORTE - Fundação de Desporto e Lazer de Mato Grosso do Sul',
        numeroSerie: 'numeroSerie',
        tipo: 'VEICULO',
        placa: 'placa',
        renavam: 'renavam',
        licenciamento: 'licenciamento',
        motor: 'motor',
        chassi: 'chassi',
        categoria: 'abcgdij',
        metodo: 'QUOTAS_CONSTANTES',
        valorDepreciadoMensal: 0,
        valorDepreciadoAcumulado: 0,
        prazoVidaUtil: 9
    }

    const dadosFicha = {
        numeroNotaLancamentoContabil: '123',
        valorTotal: 100000.00,
        situacao: 'FINALIZADO',
        dataFinalizacao: '2021-02-22 16:53:50.094000',
        dataCriacao: '2021-02-22 16:53:50.094000'
    }

    const depreciacoes = [
        {
            orgaoSigla: 'AGEPAN',
            dataInicial: '2020-02-10T00:00:00.000-0400',
            taxaAplicada: '4,55',
            valorAnterior: 100000,
            valorPosterior: 95450,
            valorSubtraido: 4550
        }
    ]

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {current: {name: 'FichaPatrimonioDadosGerais', params: {patrimonioId: patrimonio.id}}}
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                }
            }
        }

        actions = {
            [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID_FICHA]: jest.fn().mockReturnValue(patrimonio),
            [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn().mockReturnValue(patrimonioRetorno),
            [actionTypes.PATRIMONIO.BUSCAR_DEPRECIACOES]: jest.fn().mockReturnValue(depreciacoes)
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        store = new Vuex.Store({state, mutations, actions})
    })

    const defaultMount = () => shallowMount(FichaPatrimonioDadosGerais, {
        localVue,
        router,
        store,
        propsData: {
            dadosFicha: dadosFicha
        }
    })

    describe('Methods', () => {

        it('deve emitir o evento de buscar patrimonio', async () => {
            wrapper = defaultMount()
            wrapper.vm.buscarPatrimonio()

            await flushPromises()

            expect(wrapper.emitted().buscarPatrimonio).toBeTruthy()
        })

        it('deve fazer uma cópia da props dadosFicha para patrimonio', () => {
            wrapper = defaultMount()

            wrapper.vm.preencherPatrimonio()
            expect(wrapper.vm.patrimonio).toEqual(dadosFicha)
        })

        it('deve criar patrimonio para atualização', async () => {
            wrapper = defaultMount()
            await flushPromises()
            const patrimonioNovo = {
                id: 1,
                numeroSerie: '1234',
                placa: 'JDJ5A56',
                renavam: '12345678901234567',
                licenciamento: 'JANEIRO',
                motor: '12345678901234567',
                chassi: 'ssd324732rewur3sW',
            }

            expect(wrapper.vm.criaPatrimonioParaAtualizacao(patrimonio)).toEqual(patrimonioNovo)
        })

        it('deve atualizar patrimonio', async () => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.atualizar(patrimonio)
            await flushPromises()

            const patrimonioNovo = {
                id: 1,
                numeroSerie: '1234',
                placa: 'JDJ5A56',
                renavam: '12345678901234567',
                licenciamento: 'JANEIRO',
                motor: '12345678901234567',
                chassi: 'ssd324732rewur3sW'
            }

            expect(actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO].mock.calls[0][1]).toEqual(patrimonioNovo)
            expect(wrapper.vm.patrimonio.numeroSerie).toEqual('numeroSerie')
            expect(wrapper.vm.patrimonio.placa).toEqual('placa')
            expect(wrapper.vm.patrimonio.renavam).toEqual('renavam')
            expect(wrapper.vm.patrimonio.licenciamento).toEqual('licenciamento')
            expect(wrapper.vm.patrimonio.motor).toEqual('motor')
            expect(wrapper.vm.patrimonio.chassi).toEqual('chassi')
        })

        it('deve atualizar dados do patrimonio após edição', async () => {
            wrapper = defaultMount()
            await flushPromises()
            const patrimonioNovo = {
                id: 1,
                numeroSerie: 'numeroSerie',
                placa: 'placa',
                renavam: 'renavam',
                licenciamento: 'licenciamento',
                motor: 'motor',
                chassi: 'chassi',
            }

            wrapper.vm.setaAtualizacaoDeCamposEditados(patrimonioNovo)

            expect(wrapper.vm.patrimonio.numeroSerie).toEqual('numeroSerie')
            expect(wrapper.vm.patrimonio.placa).toEqual('placa')
            expect(wrapper.vm.patrimonio.renavam).toEqual('renavam')
            expect(wrapper.vm.patrimonio.licenciamento).toEqual('licenciamento')
            expect(wrapper.vm.patrimonio.motor).toEqual('motor')
            expect(wrapper.vm.patrimonio.chassi).toEqual('chassi')
        })

        it('deve buscar depreciações do bem', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.BUSCAR_DEPRECIACOES]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_DEPRECIACOES].mock.calls[0][1]).toEqual(wrapper.vm.$route.params.patrimonioId)

            expect(wrapper.vm.depreciacoesDoBem).toEqual(depreciacoes)
        })
    })
})
