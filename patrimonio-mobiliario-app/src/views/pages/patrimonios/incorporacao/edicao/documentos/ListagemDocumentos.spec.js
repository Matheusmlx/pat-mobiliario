import {shallowMount} from '@vue/test-utils'
import ListagemDocumentos from './ListagemDocumentos'
import {mutationTypes, actionTypes} from '@/core/constants'
import Vuex from 'vuex'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

jest.mock('vue-sweetalert2')

describe('ListagemDocumentos', () => {
    let wrapper, localVue, store, state, mutations, router, actions, errors, $validator, $swal

    const itensIncorporacao = {
        items: [
            {id: 1, situacao: 'EM_ELABORACAO'},
            {id: 2, situacao: 'FINALIZADO'},
        ]
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        state = {
            documentos: {},
            items: [],
            loki: {
                user: {
                    domainId: 1,
                    domains: [],
                    type: 'INTERNO',
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'Mobiliario.Normal'
                        },
                    ]
                }
            },
            documentoVazio: true,
            showDesfazer: false,
            rota: {
                origem: 'Inicial'
            },
            documento: {
                documentos: [{}],
                documentoBackup: {}
            },
            incorporacao: {
                situacaoIncorporacao: 'EM_PROCESSAMENTO'
            }
        }

        errors = {
            collect: jest.fn(),
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            },
        }

        $swal = () => ({
            then: jest.fn()
        })

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'IncorporacaoDocumentosEdicao',
                    params: {
                        incorporacaoId: 1,
                    },
                },
            },
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]: jest.fn().mockReturnValue(itensIncorporacao),
            [actionTypes.PATRIMONIO.INCORPORACAO.FINALIZAR_INCORPORACAO]: jest.fn().mockReturnValue({situacao: 'EM_PROCESSAMENTO'}),
            [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]: jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
        }

        store = new Vuex.Store({state, mutations, actions})
    })

    const defaultMount = () => shallowMount(ListagemDocumentos, {
        store,
        localVue,
        router,
        mocks: {$validator, errors, $swal},
    })

    describe('Mounted', () => {
        it('Deve montar listagemDocumentos', async () => {
            wrapper = defaultMount()
            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]).toHaveBeenCalled()
        })
    })

    describe('Computed', () => {
        it('Deve pegar situação da incorporação do state', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.situacaoIncorporacao).toEqual('EM_PROCESSAMENTO')
        })

        it('Deve retornar se situação da incorporação em processamento', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.incorporacaoEmProcessamento).toEqual(true)
        })

        it('Deve retornar id da incorporação', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.incorporacaoId).toEqual(1)
        })

        it('Deve retornar se permissão Mobiliario.Normal', () => {
            wrapper = defaultMount()
            expect(wrapper.vm.verificaPermissaoEdicao).toEqual(true)
        })

        it('Deve retornar se permissão diferente de Mobiliario.Normal', () => {
            state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            wrapper = defaultMount()
            expect(wrapper.vm.verificaPermissaoEdicao).toEqual(false)
        })
    })

    describe('Methods', () => {
        it('Deve finalizar a incorporacao e redirecionar para listagem', async () => {
            wrapper = defaultMount()
            wrapper.vm.possuiItensNaoFinalizados = false
            const mostrarNotificacaoSucessoComConfirmacao = jest.spyOn(wrapper.vm, 'mostrarNotificacaoSucessoComConfirmacao')
            wrapper.vm.tratarEventoFinalizar()
            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.FINALIZAR_INCORPORACAO]).toHaveBeenCalled()
            expect(mostrarNotificacaoSucessoComConfirmacao).toHaveBeenCalled()
        })

        it('Deve finalizar a incorporacao e redirecionar para visualização', async () => {
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]: jest.fn().mockReturnValue(itensIncorporacao),
                [actionTypes.PATRIMONIO.INCORPORACAO.FINALIZAR_INCORPORACAO]: jest.fn().mockReturnValue({situacao: 'FINALIZADO'}),
                [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO]: jest.fn()
            }
            wrapper = shallowMount(ListagemDocumentos, {
                store: new Vuex.Store({state, mutations, actions}),
                localVue,
                router,
                mocks: {$validator, errors, $swal},
            })
            wrapper.vm.possuiItensNaoFinalizados = false
            const mostrarNotificacaoSucessoComConfirmacao = jest.spyOn(wrapper.vm, 'mostrarNotificacaoSucessoComConfirmacao')
            wrapper.vm.tratarEventoFinalizar()
            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.FINALIZAR_INCORPORACAO]).toHaveBeenCalled()
            expect(mostrarNotificacaoSucessoComConfirmacao).not.toHaveBeenCalled()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({
                name: 'VisualizarRegistroIncorporacao',
                params: {incorporacaoId: wrapper.vm.incorporacaoId}
            })
        })

        it('Nao deve finalizar a incorporacao', async () => {
            wrapper = defaultMount()
            wrapper.setData({possuiItensNaoFinalizados: true})

            wrapper.vm.tratarEventoFinalizar()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.FINALIZAR_INCORPORACAO]).not.toHaveBeenCalled()
        })

        it('Deve voltar para dados de entrada edição', async () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoVoltar()

            expect(router.replace.mock.calls[0][0]).toEqual({
                name: 'ItensIncorporacaoListagem',
                params: {incorporacaoId: 1}
            })
        })

        it('Deve voltar para dados de entrada edição na visualização', async () => {
            store.state.loki.user.authorities[0].name = 'Mobiliario.Consulta'
            wrapper = defaultMount()
            wrapper.vm.tratarEventoVoltar()

            expect(router.replace.mock.calls[0][0]).toEqual({
                name: 'VisualizarItensIncorporacao',
                params: {incorporacaoId: 1}
            })
        })

        it('Deve retornar texto de bloqueio para usuário sem permissão', async () => {
            state.loki.user.authorities = [{hasAccess: true, name: 'Mobiliario.Consulta'}]
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.retornaTextoBloqueio()).toEqual('Você não possui permissão para finalizar.')
        })

        it('Deve retornar texto de bloqueio para incorporação que possui itens não finalizados', async () => {
            wrapper = defaultMount()
            await flushPromises()

            expect(wrapper.vm.retornaTextoBloqueio()).toEqual('Todos os itens do passo 2 devem estar finalizados.')
        })
    })
})
