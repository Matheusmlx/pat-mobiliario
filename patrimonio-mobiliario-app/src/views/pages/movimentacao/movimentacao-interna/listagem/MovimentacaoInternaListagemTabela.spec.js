import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import MovimentacaoInternaListagemTabela from './MovimentacaoInternaListagemTabela'
import {mutationTypes} from '@/core/constants'


describe('MovimentacaoInternaListagemTabela', () => {
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
            [mutationTypes.MOVIMENTACAO_INTERNA.RESETA_PAGE_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]: jest.fn()
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, modules: rotulosPersonalizados, mutations})

        localVue = applicationTestBuilder.build()
    })

    const defaultMount = () => shallowMount(MovimentacaoInternaListagemTabela, {
        localVue,
        store,
        propsData: {
            itens:[
                {
                    id:1,
                    codigo: '0934830947',
                    tipo: 'DISTRIBUICAO',
                    orgao: 'AGEPAN',
                    setorOrigem: 'Almoxarifado',
                    setorDestino: 'Copa e Cozinha',
                    situacao: 'EM_ELABORACAO'
                }
            ],
            paginacao: {
                page: 1,
                rowsPerPage: 10,
                sortBy: ['codigo'],
                defaultSortBy: ['codigo'],
                sortDesc: [false]
            },
            paginas: 1,
            totalItens: 1,
        },
        sync: false,
    })

    describe('methods', () => {
        it('Deve emitir o evento de acessar passando a movimentacao', async () => {
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
            wrapper.vm.paginacaoInterna = {
                page: 2,
                rowsPerPage: 10,
                descending: false,
            }
            const pagina = 3
            wrapper.vm.tratarPaginacao(pagina)
            await flushPromises()
            expect(wrapper.vm.paginacaoInterna.page).toEqual(3)
        })

        it('Deve resetar a paginação', async () => {
            wrapper = defaultMount()
            await flushPromises()
            wrapper.vm.resetaPage()
            expect(mutations[mutationTypes.MOVIMENTACAO_INTERNA.RESETA_PAGE_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]).toHaveBeenCalled()
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
