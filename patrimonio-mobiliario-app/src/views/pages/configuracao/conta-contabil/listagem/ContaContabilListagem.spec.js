import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ContaContabilListagem from './ContaContabilListagem'
import Vuex from 'vuex'

describe('ContaContabilListagem', () => {
    let wrapper, localVue, errors, $validator, store, state

    $validator = {
        _base: {
            validateAll: jest.fn().mockReturnValue(true),
            errors: {
                clear: jest.fn()
            }
        }
    }

    errors = {
        collect: jest.fn()
    }

    beforeEach(() => {

        state = {
            contaContabil: {
                todasContasContabeis: {
                    filtros: {
                        conteudo: {
                            default: null,
                            label: 'Pesquisa',
                            value: ''
                        }
                    },
                    paginacao: {
                        descending: false,
                        groupBy: [],
                        groupDesc: [],
                        itemsPerPage: 10,
                        multiSort: false,
                        mustSort: false,
                        page: 1,
                        rowsPerPage: 10,
                        sortBy: ['codigo'],
                        sortDesc: [false]
                    }
                }
            }
        }

        localVue = applicationTestBuilder.build()

        store = new Vuex.Store({state})
    })

    const defaultMount = () => shallowMount(ContaContabilListagem, {
        localVue,
        store,
        mocks: {
            $validator,
            errors
        },
        propsData: {
            value: {},
            paginacao: {},
            paginas: {},
            totalItens: {},
            colunasTable: {},
            todasContasContabeis: {}
        }
    })

    describe('Computed', () => {
        it('deve retornar se o sinal de obrigatoriedade irá aparecer', () => {
            wrapper = defaultMount()

            wrapper.setData({modoEdicao: false})

            expect(wrapper.vm.$options.computed.mostrarApenasNome.call(wrapper.vm)).toBe(true)
        })

        it('deve retornar se o sinal de obrigatoriedade irá aparecer caso o tipo seja DEPRECIAVEL', () => {
            wrapper = defaultMount()

            wrapper.setData({modoEdicao: true, tipo: 'DEPRECIAVEL'})

            expect(wrapper.vm.$options.computed.mostrarApenasNomeCamposObrigatorios.call(wrapper.vm)).toBe(false)
        })

    })

    describe('watch', () => {

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
    })
    describe('methods', () => {

        it('deve emitir um evento cancelarEdicao', async () => {
            wrapper = defaultMount()

            wrapper.vm.cancelarEdicao()
            await flushPromises()

            expect(wrapper.emitted().cancelarEdicao).toBeTruthy()
        })

        it('deve desativar as ações dos ícones de editar', () => {
            wrapper = defaultMount()

            wrapper.vm.desativarAcoes()

            expect(wrapper.vm.permitirAcoes).toEqual(false)
        })

        it('deve habilitar as ações dos ícones de editar', () => {
            wrapper = defaultMount()

            wrapper.vm.habilitarAcoes()

            expect(wrapper.vm.permitirAcoes).toEqual(true)
        })

        it('deve resetar a página', () => {
            wrapper = defaultMount()

            wrapper.vm.resetaPage()

            expect(state.contaContabil.todasContasContabeis.paginacao.page).toEqual(1)
        })

        it('deve tratar o evento de paginação', () => {
            const pagina = 10
            wrapper = defaultMount()

            flushPromises()
            wrapper.vm.tratarPaginacao(pagina)

            flushPromises()
            expect(wrapper.vm.paginacaoInterna.page).toEqual(pagina)
        })

        it('deve tratar o evento de salvar', () => {
            const contaContabil = {id: 1}
            wrapper = defaultMount()

            flushPromises()
            wrapper.vm.tratarEventoSalvar(contaContabil)

            flushPromises()
            expect(wrapper.emitted().salvar).toBeTruthy()
            expect(wrapper.emitted().salvar[0][0]).toEqual({id: 1})
        })

        it('deve mudar o valor do tipo da conta contábil', () => {
            wrapper = defaultMount()

            wrapper.vm.alterarTipo('NAO_DEPRECIAVEL')

            expect(wrapper.vm.tipo).toBe('NAO_DEPRECIAVEL')
        })
    })
})
