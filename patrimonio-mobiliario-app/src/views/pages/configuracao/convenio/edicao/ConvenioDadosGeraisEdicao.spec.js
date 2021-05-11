import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import ConvenioDadosGeraisEdicao from './ConvenioDadosGeraisEdicao'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('ConvenioDadosGeraisEdicao', () => {
    let wrapper, actions, localVue, vuetify, errors, router, state, mutations, $validator, $t = jest.fn()

    const convenioData = {
        id: 1,
        numero: '095848',
        nome: 'Caixa Seguradora de Bens Social',
        concedente: {id: 1, nome: 'Bradesco Seguros'},
        fonteRecurso: {id: 2, nome: 'Aquisição de protótipos'},
        situacao: 'ATIVO',
        dataInicioPeriodo: new Date('2018-02-05T12:00:00'),
        dataFimPeriodo: new Date('2018-02-10T12:00:00')
    }

    const concedentesData = {
        'items':
            [
                {
                    id: 1,
                    cpfCnpj: '00156005800001',
                    nome: 'Caixa Economica Federal',
                    situacao: 'ATIVO'
                },
                {
                    id: 2,
                    cpfCnpj: '00156005800001',
                    nome: 'Caixa Economica Federal',
                    situacao: 'ATIVO'
                },
                {
                    id: 3,
                    cpfCnpj: '00156005800001',
                    nome: 'Caixa Economica Federal',
                    situacao: 'ATIVO'
                },
                {
                    id: 4,
                    cpf: '00156005800001',
                    concedente: 'Caixa Economica Federal',
                    situacao: 'ATIVO'
                }
            ],
        'paginas': 3,
        'totalItens': 5
    }

    const concedenteDataSelect = {id: 1, nome: 'Bradesco Seguros'}

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
            getObrigatorioRotulosPersonalizados: () => jest.fn(),
            getObjetoValidado: () => jest.fn().mockReturnValue(true)
        }
    }

    const defaultMount = (stubs) => shallowMount(ConvenioDadosGeraisEdicao, {
        localVue,
        router,
        vuetify,
        stubs,
        store: new Vuex.Store({
            state, mutations, actions,
            modules: {
                rotulosPersonalizados
            }
        }),
        mocks: {$validator, errors, $t},
    })

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        state = {
            loki: {
                user: {
                    domainId: 1
                },
                uploadedFiles: []
            },
            convenio: {
                dadosGerais: {},
                rota: {
                    origem: 'ConvenioListagem'
                }
            }
        }

        errors = {
            collect: jest.fn()
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'ConvenioDadosGeraisNovo',
                    params: {
                        id: null
                    }
                }
            }
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true)
            }
        }

        mutations = {
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
        }

        actions = {
            [actionTypes.CONFIGURACAO.CONVENIO.EDITAR_CONVENIO]: jest.fn(),
            [actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_CONVENIO_POR_ID]: jest.fn().mockReturnValue(convenioData),
            [actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_CONCEDENTE_POR_ID]: jest.fn().mockReturnValue(concedenteDataSelect),
            [actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_CONCEDENTES_DINAMICAMENTE]: jest.fn().mockReturnValue(concedentesData),
        }
    })

    describe('Methods', () => {

        it('Deve voltar a listagem de convênios', () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoCancelar()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'ConvenioListagem'})
        })

        it('Deve verificar se o convênio foi setado para edição', async () => {
            wrapper = defaultMount()

            await flushPromises()
            wrapper.vm.$route.params.id = 5
            wrapper.vm.buscarConvenio()
            await flushPromises()

            expect(wrapper.vm.dadosGerais).toEqual(convenioData)
        })

        it('Deve verificar se o click de gerenciar concedentes chamou o método corretamente', async () => {
            wrapper = defaultMount({ConvenioDadosGerais: {
                template: '<div><button class="stub" @click="$emit(\'gerenciarConcedentes\', {})"></button></div>'
            }})

            expect(wrapper.emitted().gerenciarConcedentes).toBeTruthy
        })

        it('Deve editar o convênio se todos os campos obrigatórios preenchidos', async () => {
            wrapper = defaultMount()

            await flushPromises()

            wrapper.vm.tratarEventoEditar()
            await flushPromises()

            const dadosGerais = Object.assign({}, wrapper.vm.dadosGerais)

            expect(mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE].mock.calls[0][1]).toBe('Salvando alterações do convênio...')
            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.EDITAR_CONVENIO]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.EDITAR_CONVENIO].mock.calls[0][1]).toEqual(dadosGerais)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
        })

        it('Não deve salvar o convênio se algum dos campos obrigatórios não estiverem preenchidos', async () => {
            const rotulosPersonalizados = {
                namespaced: true,
                getters: {
                    getNomeRotulosPersonalizados: () => jest.fn(),
                    getObrigatorioRotulosPersonalizados: () => jest.fn(),
                    getObjetoValidado: () => jest.fn().mockReturnValue(false)
                }
            }
            wrapper = shallowMount(ConvenioDadosGeraisEdicao, {
                localVue,
                router,
                vuetify,
                store: new Vuex.Store({
                    state, mutations, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {$validator, errors, $t},
            })

            await flushPromises()

            wrapper.vm.tratarEventoEditar()
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.CONVENIO.EDITAR_CONVENIO]).not.toHaveBeenCalled()
        })

        it('Deve validar o valor digitado no auto-complete', async () => {
            wrapper = defaultMount()

            await flushPromises()
            await expect(wrapper.vm.filtroComboAutoComplete(concedentesData.items[1], 'filial')).toBeTruthy
        })

        it('Deve abrir o modal concedente', async () => {
            wrapper = defaultMount()

            wrapper.vm.modalConcedente = false

            wrapper.vm.abrirModalConcedente()
            expect(wrapper.vm.modalConcedente).toBe(true)
        })

        it('Deve fechar o modal concedente', async () => {
            wrapper = defaultMount()

            wrapper.vm.modalConcedente = true

            wrapper.vm.tratarEventoFecharModal()
            expect(wrapper.vm.modalConcedente).toBe(false)
        })

        it('Deve buscar concedente na api pelo cnpj', async () => {
            wrapper = defaultMount()
            wrapper.vm.buscaConcedenteDinamicamente = '46456456'
            await flushPromises()

            expect(actions[actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_CONCEDENTES_DINAMICAMENTE]).toHaveBeenCalled()
            expect(actions[actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_CONCEDENTES_DINAMICAMENTE].mock.calls[0][1]).toEqual('46456456')
        })

        it('Deve resetar concedentes', async () => {
            wrapper = defaultMount()
            wrapper.vm.buscaConcedenteDinamicamente = '12345'
            await flushPromises()
            wrapper.vm.buscaConcedenteDinamicamente = null
            await flushPromises()

            expect(wrapper.vm.dadosGerais.concedente).toEqual(null)
            expect(wrapper.vm.concedentes.length).toEqual(0)
        })

        it('Deve filtrar item por nome', () => {
            wrapper = defaultMount()
            const item = {
                nome: 'abc',
                cpfCnpj:'1234'
            }

            expect(wrapper.vm.filtroAutocompleteConcedentes(item, 'ab')).toEqual(true)
        })

        it('Deve filtrar item por cpf', () => {
            wrapper = defaultMount()
            const item = {
                nome: 'abc',
                cpfCnpj:'1234'
            }

            expect(wrapper.vm.filtroAutocompleteConcedentes(item, '12')).toEqual(true)
        })

        it('Deve filtrar item por nome se não tiver cpf', () => {
            wrapper = defaultMount()
            const item = {
                nome: 'abc',
                cpfCnpj: null
            }

            expect(wrapper.vm.filtroAutocompleteConcedentes(item, 'ab')).toEqual(true)
        })

        it('Deve selecionar o concedente vindo do modal de concedentes', () => {
            wrapper = defaultMount()

            const concedente = {
                id: 5,
                nome: 'concedente selecionado'
            }

            wrapper.vm.selecionarConcedente(concedente)

            expect(wrapper.vm.dadosGerais.concedente).toEqual(5)
            expect(wrapper.vm.concedentes[0]).toEqual(concedente)
        })
    })
})
