import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import IncorporacaoListagemTabela from './IncorporacaoListagemTabela'
import {mutationTypes} from '@/core/constants'


describe('IncorporacaoListagemTabela', () => {
    let wrapper, state, store, localVue, mutations

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
        }
    }

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
            [mutationTypes.PATRIMONIO.INCORPORACAO.RESETA_PAGE_INCORPORACAO]: jest.fn()
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, modules: rotulosPersonalizados, mutations})

        localVue = applicationTestBuilder.build()
    })

    const defaultMount = () => shallowMount(IncorporacaoListagemTabela, {
        localVue,
        store,
        propsData: {
            items: [
                {
                    id: 1,
                    codigo: 388381,
                    fornecedor: {id: 1, nome: 'Loja de Armas e Munições o Mercado S.A'},
                    quantidade: 1000,
                    recebimento: '2020-05-25T00:00:00',
                    situacao: 'EM_ELABORACAO'
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
        it('Deve emitir o evento de acessar passando o registro de incorporação ', async () => {
            wrapper = defaultMount()

            await flushPromises()
            await wrapper.vm.tratarEventoAcessar()
            await flushPromises()
            expect(wrapper.emitted('acessar')).toBeTruthy()
            expect(wrapper.emitted().acessar[0][0]).toEqual(wrapper.vm.$props.item)
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
            wrapper.vm.paginacaoInterna = {
                page: 2,
                rowsPerPage: 10,
                descending: false,
            }
            await flushPromises()
            const pagina = 3
            wrapper.vm.tratarPaginacao(pagina)
            await flushPromises()
            expect(wrapper.vm.paginacaoInterna.page).toEqual(3)
        })

        it('Deve resetar paginação', async () => {
            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.paginacaoInterna = {
                page: 2,
                rowsPerPage: 10,
                descending: false,
            }
            await flushPromises()
            wrapper.vm.resetaPage()
            await flushPromises()
            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.RESETA_PAGE_INCORPORACAO]).toHaveBeenCalled()
        })

        it('Deve retornar classe do item quando EM_PROCESSAMENTO ', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(wrapper.vm.retornaClasseDoItem({situacao:'EM_PROCESSAMENTO'})).toEqual('pr-cursor-tabela')
        })

        it('Não deve retornar classe do item quando diferente de EM_PROCESSAMENTO ', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(wrapper.vm.retornaClasseDoItem({situacao:'ERRO_PROCESSAMENTO'})).toEqual('')
        })

        it('Deve retornar icone fas fa-exclamation-triangle quando ERRO_PROCESSAMENTO ', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(wrapper.vm.retornaIcone({situacao:'ERRO_PROCESSAMENTO'})).toEqual('fas fa-exclamation-triangle')
        })

        it('Deve retornar icone fas fa-clock quando EM_PROCESSAMENTO ', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(wrapper.vm.retornaIcone({situacao:'EM_PROCESSAMENTO'})).toEqual('fas fa-clock')
        })

        it('Deve retornar icone fas fa-edit quando EM_ELABORACAO ', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(wrapper.vm.retornaIcone({situacao:'EM_ELABORACAO'})).toEqual('fas fa-edit')
        })

        it('Não deve retornar icone quando diferente de EM_ELABORACAO,EM_PROCESSAMENTO e ERRO_PROCESSAMENTO ', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(wrapper.vm.retornaIcone({situacao:'FINALIZADO'})).toEqual('')
        })

        it('Deve retornar cor icone red quando ERRO_PROCESSAMENTO ', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(wrapper.vm.retornaCorIcone({situacao:'ERRO_PROCESSAMENTO'})).toEqual('red')
        })

        it('Deve retornar cor icone #ff9800 quando EM_PROCESSAMENTO ', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(wrapper.vm.retornaCorIcone({situacao:'EM_PROCESSAMENTO'})).toEqual('#ff9800')
        })

        it('Deve retornar cor icone #ff9800 quando EM_ELABORACAO ', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(wrapper.vm.retornaCorIcone({situacao:'EM_ELABORACAO'})).toEqual('#ff9800')
        })
    })
})
