import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ReservaVisualizacaoDadosGerais from './ReservaVisualizacaoDadosGerais'
import {mutationTypes} from '@/core/constants'


describe('ReservaVisualizacaoDadosGerais', () => {
    let wrapper, state, store, localVue, mutations

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO'
                }
            }
        }

        mutations = {
            [mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO]: jest.fn()
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, mutations})

        localVue = applicationTestBuilder.build()
    })

    const defaultMount = () => shallowMount(ReservaVisualizacaoDadosGerais, {
        localVue,
        store,
        propsData: {
            itens: [
                {
                    id: 1,
                    codigo: '23213',
                    orgao: 'ASDE - Agência mundial da saúdee',
                    quantidadeReservada: 200,
                    preenchimento: 'AUTOMATICO',
                    numeroInicio: '100',
                    numeroFim: '199',
                    podeExcluir: true
                },
            ],
            paginacao: {
                page: 1,
                rowsPerPage: 10,
                sortDesc: [false],
                sortBy: ['situacao']
            },
            paginas: 1,
            totalItens: 1,
        },
        sync: false,
    })

    describe('computed', () => {
        it('Deve formatar a quantidade de intervalos cadastrados', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.quantidadeIntervalos).toBe('01')
        })
    })

    describe('methods', () => {

        it('Deve emitir o evento de paginar', async () => {
            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.paginacaoInterna = {
                page: 2,
                rowsPerPage: 10,
                descending: false,
            }
            await flushPromises()

            expect(wrapper.emitted('paginar')).toBeTruthy()
        })

        it('Deve resetar paginação', async () => {
            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.resetaPage()
            await flushPromises()

            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO]).toHaveBeenCalled()
        })

        it('Deve emitir evento para gerar termo de guarda', () => {
            wrapper = defaultMount()

            wrapper.vm.gerarTermoDeGuarda({id:1})

            expect(wrapper.emitted().gerarTermoDeGuarda).toBeTruthy()
            expect(wrapper.emitted().gerarTermoDeGuarda[0][0]).toEqual(1)
        })

    })
})
