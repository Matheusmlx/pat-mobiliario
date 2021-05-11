import {shallowMount} from '@vue/test-utils'
import Empenho from './Empenho'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'
import Vue from 'vue'
import Vuex from 'vuex'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('Empenho', () => {
    let actions, mutations, store, state, wrapper, localVue, $validator, router, errors

    const rotulosPersonalizados = {
        namespaced: true,
        actions: {getAllRotulosPersonlizados: jest.fn()},
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn()
        }
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            routes: [],
            push: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: '/incorporacao/:id/editarIncorporacao',
                    params: {
                        incorporacaoId: 1
                    },
                },
            },
        }
        state = {
            comum: {
                rota: {
                    origem: 'Inicial'
                },
            },
            loki: {
                user: {
                    authorities: [{name: 'Mobiliario.Retroativo', hasAccess: true}]
                },
            }
        }

        errors = {
            collect: jest.fn()
        }

        $validator = {
            validate: jest.fn().mockReturnValue(true)
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.ATUALIZAR_EMPENHO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.INSERIR_EMPENHO]: jest.fn(),
            [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.REMOVER_EMPENHO]: jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.ENABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_AUTO_SAVING]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state, actions, mutations, modules: {rotulosPersonalizados}})
    })

    describe('Methods', () => {
        it('Deve editar empenho', async() => {
            const empenho = {
                id: 5,
                numeroEmpenho: '135153',
                dataEmpenho: '2020-07-15T23:00:00.000-0400',
                valorEmpenho: '356500',
                statusCampos: true
            }

            wrapper = shallowMount(Empenho,
                {localVue,
                    mocks: {$validator, errors},
                    router,
                    store,
                    propsData: {
                        value: empenho,
                        idIncorporacao:2
                    }})

            await wrapper.vm.editarEmpenho()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.ATUALIZAR_EMPENHO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.ATUALIZAR_EMPENHO].mock.calls[0][1]).toEqual(empenho)
        })

        it('Deve salvar empenho', async() => {
            const empenho = {
                numeroEmpenho: '135153',
                dataEmpenho: '2020-07-15T23:00:00.000-0400',
                valorEmpenho: '356500',
                statusCampos: true
            }

            wrapper = shallowMount(Empenho,
                {localVue,
                    mocks: {$validator, errors},
                    router,
                    store,
                    propsData: {
                        value: empenho,
                        idIncorporacao:2
                    }})

            await wrapper.vm.editarEmpenho()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.INSERIR_EMPENHO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.INSERIR_EMPENHO].mock.calls[0][1]).toEqual(empenho)
        })

        it('Deve emitir evento para adicionar novo empenho se todos campos preenchidos e quantidade menor ou igual a dez', async() => {
            const empenho = {
                numeroEmpenho: '135153',
                dataEmpenho: '2020-07-15T23:00:00.000-0400',
                valorEmpenho: '356500',
                statusCampos: true
            }

            wrapper = shallowMount(Empenho,
                {localVue,
                    mocks: {$validator, errors},
                    router,
                    store,
                    propsData: {
                        value: empenho,
                        obrigatorio: true,
                        quantidadeEmpenhos: 9,
                        idIncorporacao:2
                    }})

            await wrapper.vm.adicionarNovoEmpenho()
            await flushPromises()

            expect(wrapper.emitted().adicionarNovoEmpenho).toBeTruthy()
        })

        it('Não deve emitir evento para adicionar novo empenho se numeroEmpenho não preenchido', () => {
            const empenho = {
                numeroEmpenho: null,
                dataEmpenho: '2020-07-15T23:00:00.000-0400',
                valorEmpenho: '356500',
                statusCampos: true
            }

            wrapper = shallowMount(Empenho,
                {localVue,
                    mocks: {$validator, errors},
                    router,
                    store,
                    propsData: {
                        value: empenho,
                        obrigatorio: true,
                        idIncorporacao:2
                    }})

            wrapper.vm.quantidadeEmpenhos = 9
            wrapper.vm.adicionarNovoEmpenho()
            flushPromises()

            expect(wrapper.emitted().adicionarNovoEmpenho).not.toBeTruthy()
        })

        it('Não deve emitir evento para adicionar novo empenho se dataEmpenho não preenchido', () => {
            const empenho = {
                numeroEmpenho: '32434',
                dataEmpenho: null,
                valorEmpenho: '356500',
                statusCampos: true
            }

            wrapper = shallowMount(Empenho,
                {localVue,
                    mocks: {$validator, errors},
                    router,
                    store,
                    propsData: {
                        value: empenho,
                        obrigatorio: true,
                        idIncorporacao:2
                    }})

            wrapper.vm.quantidadeEmpenhos = 9
            wrapper.vm.adicionarNovoEmpenho()
            flushPromises()

            expect(wrapper.emitted().adicionarNovoEmpenho).not.toBeTruthy()
        })

        it('Não deve emitir evento para adicionar novo empenho se valorEmpenho não preenchido', () => {
            const empenho = {
                numeroEmpenho: '32434',
                dataEmpenho: '2020-07-15T23:00:00.000-0400',
                valorEmpenho: null,
                statusCampos: true
            }

            wrapper = shallowMount(Empenho,
                {localVue,
                    mocks: {$validator, errors},
                    router,
                    store,
                    propsData: {
                        value: empenho,
                        obrigatorio: true,
                        idIncorporacao:2
                    }})

            wrapper.vm.quantidadeEmpenhos = 9
            wrapper.vm.adicionarNovoEmpenho()
            flushPromises()

            expect(wrapper.emitted().adicionarNovoEmpenho).not.toBeTruthy()
        })

        it('Não deve emitir evento para adicionar novo empenho se quantidade maior ou igual a dez', async() => {
            const empenho = {
                numeroEmpenho: '135153',
                dataEmpenho: '2020-07-15T23:00:00.000-0400',
                valorEmpenho: '356500',
                statusCampos: true
            }

            wrapper = shallowMount(Empenho,
                {localVue,
                    mocks: {$validator, errors},
                    router,
                    store,
                    propsData: {
                        value: empenho,
                        obrigatorio: true,
                        idIncorporacao:2
                    }})

            wrapper.vm.quantidadeEmpenhos = 10
            await wrapper.vm.adicionarNovoEmpenho()
            await flushPromises()

            expect(wrapper.emitted().adicionarNovoEmpenho).not.toBeTruthy()
        })

        it('Deve remover empenho na api se empenho conter id', async() => {
            const empenho = {
                id:1,
                numeroEmpenho: '135153',
                dataEmpenho: '2020-07-15T23:00:00.000-0400',
                valorEmpenho: '356500',
                statusCampos: true
            }

            wrapper = shallowMount(Empenho,
                {localVue,
                    mocks: {$validator, errors},
                    router,
                    store,
                    propsData: {
                        value: empenho,
                        idIncorporacao:2
                    }})

            await flushPromises()
            await wrapper.vm.remover()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.REMOVER_EMPENHO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.REMOVER_EMPENHO].mock.calls[0][1]).toEqual(empenho.id)

        })

        it('Deve remover empenho no array se empenho não conter id', async() => {
            const empenho = {
                numeroEmpenho: '135153',
                dataEmpenho: '2020-07-15T23:00:00.000-0400',
                valorEmpenho: '356500',
                statusCampos: true
            }

            wrapper = shallowMount(Empenho,
                {localVue,
                    mocks: {$validator, errors},
                    router,
                    store,
                    propsData: {
                        value: empenho,
                        idIncorporacao:2
                    }})

            await wrapper.vm.remover()
            await flushPromises()

            expect(wrapper.emitted().removerEmpenho).toBeTruthy()
        })

        it('Deve atualizar valorEmpenho do empenho', () => {
            const empenho = {
                id: 5,
                numeroEmpenho: '135153',
                dataEmpenho: '2020-07-15T23:00:00.000-0400',
                valorEmpenho: '356500',
                statusCampos: true
            }

            wrapper = shallowMount(Empenho,
                {localVue,
                    mocks: {$validator, errors},
                    router,
                    store,
                    propsData: {
                        value: empenho,
                        idIncorporacao:2
                    }})

            wrapper.vm.atualizarValorInserido('1234567')
            flushPromises()

            expect(wrapper.vm.empenho.valorEmpenho).toEqual('1234567')
        })
    })
})
