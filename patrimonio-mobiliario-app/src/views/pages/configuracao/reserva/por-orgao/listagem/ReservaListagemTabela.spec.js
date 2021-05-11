import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ReservaListagemTabela from './ReservaListagemTabela'
import {mutationTypes} from '@/core/constants'


describe('ReservaListagemTabela', () => {
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
            [mutationTypes.CONFIGURACAO.RESERVA.RESETA_PAGE_RESERVA]: jest.fn()
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, mutations})

        localVue = applicationTestBuilder.build()
    })

    const defaultMount = () => shallowMount(ReservaListagemTabela, {
        localVue,
        store,
        propsData: {
            itens: [
                {
                    codigo: 32434,
                    dataCriacao: '2020-07-15T23:00:00.000-0400',
                    quantidadeReservada: 250,
                    restante: 50,
                    orgaos: ['SED', 'SAD', 'AGEPAN', 'SISPAT'],
                    situacao: 'PARCIAL'
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
        it('Deve emitir o evento de acessar passando a reserva', async () => {
            wrapper = defaultMount()

            await flushPromises()
            await wrapper.vm.tratarEventoAcessar(wrapper.vm.$props.itens[0])
            await flushPromises()
            expect(wrapper.emitted('acessar')).toBeTruthy()
            expect(wrapper.emitted().acessar[0][0]).toEqual(wrapper.vm.$props.itens[0])
        })

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

            expect(mutations[mutationTypes.CONFIGURACAO.RESERVA.RESETA_PAGE_RESERVA]).toHaveBeenCalled()
        })

        it('Deve formatar lista de órgãos para para retornar somente o primeiro', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.formataOrgaoParaExibicao(['ABC'])).toEqual('ABC')
        })

    })
})
