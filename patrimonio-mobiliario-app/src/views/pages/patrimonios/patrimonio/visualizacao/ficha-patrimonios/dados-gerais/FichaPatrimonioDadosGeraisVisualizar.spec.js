import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import {actionTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import FichaPatrimonioDadosGeraisVisualizar from './FichaPatrimonioDadosGeraisVisualizar'

describe('FichaPatrimonioDadosGeraisVisualizar', () => {
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

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {current: { name: 'FichaPatrimonioDadosGeraisVisualizacao', params:{patrimonioId: patrimonio.id}}}
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [{name: 'Mobiliario.Consulta', hasAccess: true}]
                }
            }
        }

        actions = {
            [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID_FICHA]: jest.fn().mockReturnValue(patrimonio),
        }

        store = new Vuex.Store({state, mutations, actions})
    })

    const defaultMount = () => shallowMount(FichaPatrimonioDadosGeraisVisualizar, {
        localVue,
        router,
        store
    })

    describe('Methods', () => {

        it('deve buscar patrimonio', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID_FICHA]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID_FICHA].mock.calls[0][1]).toEqual(patrimonio.id)
        })

    })

})
