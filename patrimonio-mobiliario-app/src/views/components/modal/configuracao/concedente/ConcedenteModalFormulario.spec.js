import {mount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import ConcedenteModalFormulario from './ConcedenteModalFormulario'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('ConcedenteModalFormulario', () => {
    let wrapper, localVue, state, mutations, $validator, actions, errors, vuetify, $emit = jest.fn()

    $validator = {
        resume: jest.fn(),
        pause: jest.fn(),
        validateAll: jest.fn().mockReturnValue(true)
    }

    localVue = applicationTestBuilder.build()
    vuetify = applicationTestBuilder.getVuetify()

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
            getObrigatorioRotulosPersonalizados: () => jest.fn(),
        }
    }

    errors = {
        collect: jest.fn()
    }

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1
                }
            },
            concedente: {
                todosConcedentes: {
                    filtros: {
                        conteudo: {
                            value: null
                        }
                    }
                }
            }
        }
    })

    const defaultMount = () => mount(ConcedenteModalFormulario, {
        localVue,
        vuetify,
        propsData: {
            value: {
                id:1,
                cpfCnpj: '12345678911',
                nome: 'Teste',
                situacao: 'ATIVO'
            }
        },
        store: new Vuex.Store({
            state, mutations, actions,
            modules: {
                rotulosPersonalizados
            }
        }),
        mocks: {$validator, errors, $emit}
    })

    describe('Computed', () => {
        it('Deve formatar cpf', async () => {
            const localThis = {
                dadosGerais: {
                    cpfCnpj: '12345678911',
                    nome: 'Teste',
                    situacao: 'ATIVO'
                }
            }

            expect(ConcedenteModalFormulario.computed.formatarCpfCnpj.call(localThis)).toBe('123.456.789-11')
        })

        it('Deve formatar cnpj', async () => {
            const localThis = {
                dadosGerais: {
                    cpfCnpj: '00156005800001',
                    nome: 'Teste',
                    situacao: 'ATIVO'
                }
            }

            expect(ConcedenteModalFormulario.computed.formatarCpfCnpj.call(localThis)).toBe('00.156.005/8000-01')
        })
    })

    describe('Methods', () => {
        it('Deve ativar o modo edição', async () => {
            wrapper = defaultMount()

            wrapper.vm.ativarModoEdicao()

            expect(wrapper.vm.modoEdicao).toBe(true)
            expect(wrapper.emitted().desabilitaBotaoNovo).toBeTruthy()
        })

        it('Deve desativar o modo edição', async () => {
            wrapper = defaultMount()

            wrapper.vm.desativarModoEdicao()

            expect(wrapper.vm.modoEdicao).toBe(false)
        })

        it('Deve emitir evento para cancelar', async () => {
            wrapper = defaultMount()

            wrapper.setData({
                value: {
                    cpfCnpj: '12345678911',
                    nome: 'Teste',
                    situacao: 'ATIVO'
                }
            })

            wrapper.vm.desativarModoEdicao()

            expect(wrapper.vm.modoEdicao).toBe(false)
        })

        it('Deve retornar true quando todos os campos forem preenchidos corretamente', async () => {
            wrapper = mount(ConcedenteModalFormulario, {
                localVue,
                vuetify,
                propsData: {
                    value: {
                        cpfCnpj: '12341231234123',
                        nome: 'Teste',
                        situacao: 'ATIVO'
                    }
                },
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $emit}
            })

            const resultado = await wrapper.vm.validarDadosFormulario()

            await flushPromises()

            expect(wrapper.vm.validarDadosFormulario()).toBeTruthy()
            expect(resultado).toBe(true)
        })

        it('Deve retornar false quando há campos não preenchidos', async () => {
            wrapper = mount(ConcedenteModalFormulario, {
                localVue,
                vuetify,
                propsData: {
                    value: {
                        cpfCnpj: '',
                        nome: 'Teste',
                        situacao: 'ATIVO'
                    }
                },
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $emit}
            })

            await wrapper.vm.validarDadosFormulario()

            await flushPromises()

            expect(wrapper.vm.validarDadosFormulario()).toBeTruthy()
        })

        it('Deve remover os caracteres especiais', async () => {
            wrapper = defaultMount()

            wrapper.setData({
                value: {
                    cpfCnpj: '123.456.788-88',
                    nome: 'Teste',
                    situacao: 'ATIVO'
                }
            })

            wrapper.vm.removerCaracteresEspeciais()

            expect(wrapper.vm.value.cpfCnpj).toBe('12345678888')
        })

        it('Deve emitir evento para salvar concedente quando o concedente não possuir id', async () => {
            wrapper = mount(ConcedenteModalFormulario, {
                localVue,
                vuetify,
                propsData: {
                    value: {
                        cpfCnpj: '12345678911',
                        nome: 'Teste',
                        situacao: 'ATIVO'
                    }
                },
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $emit}
            })

            await flushPromises()

            await wrapper.vm.salvarConcedente()

            await flushPromises()

            expect(wrapper.emitted().salvarConcedente).toBeTruthy()
            expect(wrapper.vm.validarDadosFormulario()).toBeTruthy()
        })

        it('Deve emitir evento para editar concedente quando o concedente possuir id', async () => {
            wrapper = mount(ConcedenteModalFormulario, {
                localVue,
                vuetify,
                propsData: {
                    value: {
                        id: 1,
                        cpfCnpj: '12345678911',
                        nome: 'Teste',
                        situacao: 'ATIVO'
                    }
                },
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks: {$validator, errors, $emit}
            })

            await wrapper.vm.salvarConcedente()

            await flushPromises()

            expect(wrapper.emitted().editarConcedente).toBeTruthy()
            expect(wrapper.vm.validarDadosFormulario()).toBeTruthy()
        })

        it('Deve emitir evento para selecionar concedente', () => {
            wrapper = defaultMount()

            wrapper.vm.selecionarConcedente()

            expect(wrapper.emitted().selecionarConcedente).toBeTruthy()
            expect(wrapper.emitted().selecionarConcedente[0][0]).toEqual(wrapper.vm.value)
        })

        it('Deve emitir evento para desativar icones quando estiver editando um concedente', ()=>{
            wrapper = defaultMount()

            wrapper.vm.ativarModoEdicao()

            expect(wrapper.emitted().setaEditando).toBeTruthy()
            expect(wrapper.emitted().setaEditando[0][0]).toEqual(true)
        })

        it('Deve emitir evento para ativar icones quando estiver cancelado a edição ou salvo um concedente', ()=>{
            wrapper = defaultMount()

            wrapper.vm.cancelarModoEdicao()

            expect(wrapper.emitted().setaEditando).toBeTruthy()
            expect(wrapper.emitted().setaEditando[0][0]).toEqual(false)
        })
    })
})
