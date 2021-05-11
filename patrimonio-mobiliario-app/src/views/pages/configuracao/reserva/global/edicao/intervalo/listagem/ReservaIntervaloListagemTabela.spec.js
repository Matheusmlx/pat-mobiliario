import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ReservaIntervaloListagemTabela from './ReservaIntervaloListagemTabela'
import {mutationTypes} from '@/core/constants'


describe('ReservaIntervaloListagemTabela', () => {
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

    const defaultMount = () => shallowMount(ReservaIntervaloListagemTabela, {
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

        it('Deve mudar paginação', async () => {
            wrapper = defaultMount()

            await flushPromises()
            const pagina = 3
            wrapper.vm.tratarPaginacao(pagina)
            await flushPromises()

            expect(wrapper.vm.paginacaoInterna.page).toEqual(3)
        })

        it('Deve resetar paginação', async () => {
            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.resetaPage()
            await flushPromises()

            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO]).toHaveBeenCalled()
        })

        it('Deve emitir o evento para remover o intervalo', async () => {
            wrapper = defaultMount()

            const intervaloId = 1
            wrapper.vm.removerIntervalo(intervaloId)

            expect(wrapper.emitted().removerIntervalo).toBeTruthy()
            expect(wrapper.emitted().removerIntervalo[0][0]).toEqual(intervaloId)
        })







    })
})
