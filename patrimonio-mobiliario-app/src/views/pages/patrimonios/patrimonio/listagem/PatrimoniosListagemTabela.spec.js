import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import PatrimoniosListagemTabela from './PatrimoniosListagemTabela'
import {mutationTypes} from '@/core/constants'


describe('PatrimoniosListagemTabela', () => {
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
            [mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_LISTAGEM]: jest.fn()
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, mutations})

        localVue = applicationTestBuilder.build()
    })

    const defaultMount = () => shallowMount(PatrimoniosListagemTabela, {
        localVue,
        store,
        propsData: {
            itens: [
                {
                    id: 1,
                    descricao: '9999 - teste',
                    orgao: '3333 - orgao',
                    setor: 'CCP - Copa e Cozinha',
                    ultimaMovimentacao: '2020-05-25T00:00:00',
                    situacao: 'ATIVO'
                },
            ],
            paginacao: {
                page: 1,
                rowsPerPage: 10,
                descending: false,
            },
            paginas: 1,
            totalItens: 1,
        },
        sync: false,
    })

    describe('methods', () => {

        it('Deve emitir o evento de acessar passando o patrimônio', async () => {
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
            expect(mutations[mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_LISTAGEM]).toHaveBeenCalled()
        })
    })
})
