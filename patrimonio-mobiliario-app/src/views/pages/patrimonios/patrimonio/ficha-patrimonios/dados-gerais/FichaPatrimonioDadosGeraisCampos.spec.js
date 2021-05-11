import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import FichaPatrimonioDadosGeraisCampos from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/dados-gerais/FichaPatrimonioDadosGeraisCampos'

describe('FichaPatrimonioDadosGeraisCampos', () => {
    let wrapper, localVue, vuetify, router, state, store

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

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getObrigatorioRotulosPersonalizados: () => jest.fn()
        }
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {current: { name: 'FichaPatrimonioDadosGerais', params:{patrimonioId: patrimonio.id}}}
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

        store = new Vuex.Store({state, modules: {rotulosPersonalizados}})
    })

    const defaultMount = () => shallowMount(FichaPatrimonioDadosGeraisCampos, {
        localVue,
        vuetify,
        router,
        store,
        propsData:{patrimonio:patrimonio}
    })

    describe('Computed', () => {
        it('deve formatar a placa caso não esteja formatada', async() => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.$options.computed.placaFormatada.call(wrapper.vm)).toBe('JDJ-5A56')
        })

        it('deve retornar a placa caso já esteja formatada', async() => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.patrimonio.placa = 'ABC-6E56'

            expect(wrapper.vm.$options.computed.placaFormatada.call(wrapper.vm)).toBe('ABC-6E56')
        })

        it('deve retornar nulo caso não possua placa', async() => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.patrimonio = {}

            expect(wrapper.vm.$options.computed.placaFormatada.call(wrapper.vm)).toBe(null)
        })

        it('deve formatar o chassi caso não esteja formatado', async() => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.$options.computed.chassiFormatado.call(wrapper.vm)).toBe('ssd 32473 2 rewur3sW')
        })

        it('deve retornar o chassi caso esteja formatado', async() => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.patrimonio.chassi = 'asc 19567 8 afroa3bY'

            expect(wrapper.vm.$options.computed.chassiFormatado.call(wrapper.vm)).toBe('asc 19567 8 afroa3bY')
        })

        it('deve retornar nulo caso não possua chassi', async() => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.patrimonio = {}

            expect(wrapper.vm.$options.computed.chassiFormatado.call(wrapper.vm)).toBe(null)
        })
    })

    describe('Events', () => {
        it('deve emitir evento para atualizar patrimonio', async () => {
            wrapper = defaultMount()
            await flushPromises()

            wrapper.vm.atualizarPatrimonio(patrimonio)

            expect(wrapper.emitted().atualizarPatrimonio).toBeTruthy()
            expect(wrapper.emitted().atualizarPatrimonio[0][0]).toEqual(patrimonio)
        })
    })
})
