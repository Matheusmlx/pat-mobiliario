import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import ConcedenteModalListagem from './ConcedenteModalListagem'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {mutationTypes} from '@/core/constants'

describe('ConcedenteModalListagem', () => {
    let wrapper, localVue, state, mutations, actions, errors, vuetify, $emit = jest.fn()

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    errors = {
        collect: jest.fn()
    }

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1
                }
            }
        }

        mutations = {
            [mutationTypes.CONFIGURACAO.CONCEDENTE.SET_PAGINACAO_BUSCA_TODOS_CONCEDENTES]: jest.fn(),
            [mutationTypes.CONFIGURACAO.CONCEDENTE.RESETA_PAGE_CONCEDENTE]: jest.fn()
        }
    })

    const defaultMount = () => shallowMount(ConcedenteModalListagem, {
        localVue,
        vuetify,
        propsData: {
            paginacao: {
                page: 1,
                rowsPerPage: 5,
                sortDesc: [false],
                sortBy: ['situacao']
            }
        },
        store: new Vuex.Store({
            state, mutations, actions
        }),
        mocks: {errors, $emit}
    })

    describe('Methods', () => {
        it('Deve emitir evento para cancelar edição', async () => {
            wrapper = defaultMount()

            wrapper.vm.cancelar()

            expect(wrapper.emitted().cancelar).toBeTruthy()
        })

        it('Deve emitir evento para editar o concedente', async () => {
            wrapper = defaultMount()

            const concedente = {
                cpf: '12345678911',
                concedente: 'Teste',
                situacao: 'ATIVO'
            }

            wrapper.vm.editarConcedente(concedente)

            expect(wrapper.emitted().editarConcedente).toBeTruthy()
        })

        it('Deve emitir evento para salvar concedente', async () => {
            wrapper = defaultMount()

            const concedente = {
                cpf: '12345678911',
                concedente: 'Teste',
                situacao: 'ATIVO'
            }

            wrapper.vm.salvarConcedente(concedente)

            expect(wrapper.emitted().salvarConcedente).toBeTruthy()
        })

        it('Deve emitir evento de paginar quando mudar a página', async () => {
            wrapper = shallowMount(ConcedenteModalListagem, {
                localVue,
                vuetify,
                propsData: {
                    paginacao: {
                        page: 1,
                        rowsPerPage: 5
                    }
                },
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks: {errors, $emit}
            })

            wrapper.vm.paginacaoInterna.page = 2

            await flushPromises()

            expect(wrapper.emitted().paginar).toBeTruthy()
        })

        it('Deve trocar a página', () => {
            wrapper = shallowMount(ConcedenteModalListagem, {
                localVue,
                vuetify,
                propsData: {
                    paginacao: {
                        page: 1,
                        rowsPerPage: 5
                    }
                },
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks: {errors, $emit}
            })

            wrapper.vm.tratarPaginacao(5)

            expect(wrapper.vm.paginacaoInterna.page).toBe(5)
        })

        it('Deve emitir evento para selecionar concedente',() => {
            wrapper = defaultMount()

            const concedente = {
                id: 7,
                nome: 'concedente'
            }

            wrapper.vm.selecionarConcedente(concedente)

            expect(wrapper.emitted().selecionarConcedente).toBeTruthy()
            expect(wrapper.emitted().selecionarConcedente[0][0]).toEqual(concedente)
        })

        it('Deve resetar a paginação',() => {
            wrapper = defaultMount()

            wrapper.vm.resetaPage()

            expect(mutations[mutationTypes.CONFIGURACAO.CONCEDENTE.RESETA_PAGE_CONCEDENTE]).toHaveBeenCalled()
        })

        it('Deve setar valor em concedenteEditando',() => {
            wrapper = defaultMount()

            expect(wrapper.vm.concedenteEditando).toEqual(false)

            wrapper.vm.setaEditando(true)

            expect(wrapper.vm.concedenteEditando).toEqual(true)
        })

        it('deve desabilitar botao novo', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.desabilitaBotaoNovo()

            await flushPromises()
            expect(wrapper.emitted().desabilitaBotaoNovo).toBeTruthy()
        })

        it('deve habilitar botao novo', async () => {
            wrapper = defaultMount()
            await flushPromises()
            await wrapper.vm.habilitaBotaoNovo()

            await flushPromises()
            expect(wrapper.emitted().habilitaBotaoNovo).toBeTruthy()
        })
    })
})
