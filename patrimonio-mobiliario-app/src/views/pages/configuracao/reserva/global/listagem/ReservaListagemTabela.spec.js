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

        it('Deve retornar se a situação da reserva está parcial ', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.situacaoParcialOuElaboracao({situacao: 'PARCIAL'})).toEqual(true)
        })

        it('Deve retornar se a situação da reserva está em elaboração', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.situacaoParcialOuElaboracao({situacao: 'EM_ELABORACAO'})).toEqual(true)
        })

        it('Deve retornar false se a situação da reserva não está em elaboração ou parcial', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.situacaoParcialOuElaboracao({situacao: 'FINALIZADO'})).toEqual(false)
        })

        it('Deve formatar lista de órgãos para formato (SIGLA1) quando houver apenas um órgão na lista', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.formataOrgaosParaExibicao(['ABC'])).toEqual('ABC')
        })

        it('Deve formatar lista de órgãos para formato (SIGLA1, SIGLA2) quando houver até dois órgãos na lista', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.formataOrgaosParaExibicao(['ABC','DEF'])).toEqual('ABC, DEF')
        })

        it('Deve formatar lista de órgãos para formato (SIGLA1, SIGLA2... +Restante) quando houver mais de dois orgãos na lista', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.formataOrgaosParaExibicao(['ABC','DEF','GHI'])).toEqual('ABC, DEF... +1')
        })

        it('Deve retornar se restante maior que zero', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.existeRestante(1)).toEqual(true)
        })

        it('Deve retornar false se restante igual que zero', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.existeRestante(0)).toEqual(false)
        })

        it('Deve formatar mensagem para tooltip informando o restante de reservas', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.formataMensagemRestantesTooltip(50)).toEqual('50 Restantes')
        })

    })
})
