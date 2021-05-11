import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import IncorporacaoEdicao from './IncorporacaoEdicao'
import {actionTypes, mutationTypes} from '@/core/constants'
import flushPromises from 'flush-promises'

describe('IncorporacaoEdicao.vue', () => {

    let wrapper, actions, store, localVue, router, mutations, state,vuetify

    const incorporacao = {
        id: 1,
        codigo: 1,
        reconhecimento: 'teste',
        numProcesso: '',
        fornecedor: '',
        numeroNota: '',
        dataNota: '',
        valorNota: '',
        orgao: null,
        empenho: '',
        setor: null,
        dataRecebimento: '',
        convenio: '',
        fundo: '',
        situacao: 'EM_ELABORACAO',
    }

    const passo =
        {
            titulo: 'Dados Gerais',
            rotaPadrao: 'TipoVisualizacao',
            rotaEdicao: 'EditarIncorporacao',
            desabilitado: false,
            numero: 1,
        }

    beforeEach(() => {

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                }
            },
            incorporacao: {
                rota: {
                    origem: 'IncorporacaoListagem'
                }
            },
            situacaoIncorporacao:{}
        }

        router = {
            routes: [],
            replace: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'incorporacao',
                    params: {
                        incorporacaoId: incorporacao.id
                    }
                }
            }
        }

        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.DELETAR_INCORPORACAO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]:jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.COMUM.SET_EXPANDIR_MENU]: jest.fn(),
            [mutationTypes.COMUM.SET_RETRAIR_MENU]: jest.fn(),
            [mutationTypes.PATRIMONIO.INCORPORACAO.SET_SITUACAO_INCORPORACAO]:jest.fn()
        }
    })

    const defaultShallMount = () => shallowMount(IncorporacaoEdicao, {
        render:null,
        localVue,
        vuetify,
        router,
        store: new Vuex.Store({state,actions,mutations})
    })

    describe('methods', () => {

        it('deve expandir o menu lateral quando sair da página', () => {
            wrapper = defaultShallMount()

            wrapper.vm.expandirMenu()
            expect(mutations[mutationTypes.COMUM.SET_EXPANDIR_MENU]).toHaveBeenCalled()
        })

        it('deve retrair o menu lateral quando entrar na página', () => {
            wrapper = defaultShallMount()

            wrapper.vm.retrairMenu()
            expect(mutations[mutationTypes.COMUM.SET_RETRAIR_MENU]).toHaveBeenCalled()
        })


        it('deve tratar o evento de deletar incorporação', async () => {
            wrapper = defaultShallMount()

            await flushPromises

            wrapper.vm.incorporacaoId = 1

            await wrapper.vm.tratarEventoDeletarIncorporacao()

            await flushPromises
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DELETAR_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.DELETAR_INCORPORACAO].mock.calls[0][1]).toEqual(wrapper.vm.incorporacaoId)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name: state.incorporacao.rota.origem})
        })

        it('deve habilitar o passo 3', () => {
            wrapper = defaultShallMount()

            wrapper.vm.habilitaPasso3()

            expect(wrapper.vm.passos[2].desabilitado).toEqual(false)
        })

        it('deve desabilitar o passo 3', () => {
            wrapper = defaultShallMount()

            wrapper.vm.desabilitaPasso3()

            expect(wrapper.vm.passos[2].desabilitado).toEqual(true)
        })

        it('deve tratar o evento de click redirecionando para a rota de edição', () => {
            wrapper = defaultShallMount()
            wrapper.vm.tratarEventoClick(passo)

            expect(router.replace.mock.calls[0][0]).toEqual({name: 'EditarIncorporacao'})
        })

        it('deve tratar o evento de click redirecionando para a rota de novo', () => {
            router = {
                routes: [],
                replace: jest.fn(),
                init: jest.fn(),
                history: {
                    current: {
                        name: 'incorporacao',
                        params: {
                            incorporacaoId: null
                        }
                    }
                }
            }
            wrapper = defaultShallMount()

            wrapper.vm.tratarEventoClick(passo)

            expect(router.replace.mock.calls[0][0]).toEqual({name: state.incorporacao.rota.origem})
        })

        it('deve pegar o id da incorporação pelo rota e setar na variável incorporacaoId', () => {

            wrapper = defaultShallMount()

            wrapper.vm.setIncorporacaoId()

            expect(wrapper.vm.incorporacaoId).toEqual(incorporacao.id)
        })

        it('deve controlar o acesso dos próximos passos na rota IncorporacaoItensEdicao', () => {
            router = {
                routes: [],
                replace: jest.fn(),
                init: jest.fn(),
                history: {
                    current: {
                        name: 'ItensIncorporacaoListagem',
                        params: {
                            id: incorporacao.id
                        }
                    }
                }
            }
            wrapper = defaultShallMount()

            wrapper.vm.contralaAcessoAosPassosEmCadaRota()

            expect(wrapper.vm.passos[1].desabilitado).toEqual(false)
            expect(wrapper.vm.passos[2].desabilitado).toEqual(true)
        })

        it('deve controlar o acesso dos próximos passos na rota EditarIncorporacao', () => {
            router = {
                routes: [],
                replace: jest.fn(),
                init: jest.fn(),
                history: {
                    current: {
                        name: 'EditarIncorporacao',
                        params: {
                            id: incorporacao.id
                        }
                    }
                }
            }
            wrapper = defaultShallMount()

            wrapper.vm.contralaAcessoAosPassosEmCadaRota()

            expect(wrapper.vm.passos[1].desabilitado).toEqual(true)
        })

        it('deve controlar o acesso dos próximos passos na rota IncorporacaoDocumentosEdicao', () => {
            router = {
                routes: [],
                replace: jest.fn(),
                init: jest.fn(),
                history: {
                    current: {
                        name: 'IncorporacaoDocumentosEdicao',
                        params: {
                            id: incorporacao.id
                        }
                    }
                }
            }
            wrapper = defaultShallMount()

            wrapper.vm.contralaAcessoAosPassosEmCadaRota()

            expect(wrapper.vm.passos[0].desabilitado).toEqual(false)
            expect(wrapper.vm.passos[1].desabilitado).toEqual(false)
            expect(wrapper.vm.passos[2].desabilitado).toEqual(false)
        })

        it('deve desabilitar passo 2', () => {
            wrapper = defaultShallMount()

            wrapper.vm.desabilitaPasso2()

            expect(wrapper.vm.passos[1].desabilitado).toEqual(true)
        })

        it('deve habilitar passo 2', () => {
            wrapper = defaultShallMount()

            wrapper.vm.habilitaPasso2()

            expect(wrapper.vm.passos[1].desabilitado).toEqual(false)
        })

        it('deve redirecionar para rota de visualizacao', () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            wrapper = defaultShallMount()

            wrapper.vm.incorporacaoId = 1
            wrapper.vm.tratarEventoClick({desabilitado:false, rotaVisualizacao: 'VisualizarIncorporacaoDocumentos'})

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'VisualizarIncorporacaoDocumentos'})
        })

        it('Deve redirecionar para tela de IncorporacaoListagem caso a situação da incorporação seja diferente de EM_ELABORACAO ou ERRO_PROCESSAMENTO', async () => {
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]: jest.fn().mockReturnValue('FINALIZADO')
            }

            wrapper = defaultShallMount()

            await flushPromises()

            await wrapper.vm.buscarSituacaoDaIncorporacao()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.SET_SITUACAO_INCORPORACAO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'IncorporacaoListagem'})

        })

        it('Deve buscar a situacao da incorporação e não redirecionar para tela de IncorporacaoListagem caso a situação da incorporação  seja igual a EM_ELABORACAO ou ERRO_PROCESSAMENTO', async () => {
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]: jest.fn().mockReturnValue('EM_ELABORACAO')
            }

            wrapper = defaultShallMount()

            await flushPromises()

            await wrapper.vm.buscarSituacaoDaIncorporacao()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.SET_SITUACAO_INCORPORACAO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
            expect(router.replace).not.toHaveBeenCalled()
        })

        it('Deve validar a situação da incorporação', async () =>{
            wrapper = defaultShallMount()

            await flushPromises()

            await wrapper.vm.validarIncorporacaoEmModoElaboracao('FINALIZADO')

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'IncorporacaoListagem'})

        })
    })

    describe('Destroyed', () => {
        it('Deve expandir a barra lateral ao ser destruido',async () => {
            wrapper = defaultShallMount()

            await flushPromises()

            wrapper.vm.$destroy()

            expect(mutations[mutationTypes.COMUM.SET_RETRAIR_MENU]).toHaveBeenCalled()
        })
    })
})
