import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalEstornarPatrimoniosFinalizar from './ModalEstornarPatrimoniosFinalizar'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('ModalEstornarPatrimoniosFinalizar', () => {

    let localVue, router, vuetify, state, wrapper, errors, actions, mutations

    const rotulosPersonalizados = {
        namespaced: true,
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
        }
    }

    const defaultMount = () => shallowMount(ModalEstornarPatrimoniosFinalizar, {
        vuetify,
        localVue,
        router,
        store: new Vuex.Store({
            state, actions, mutations,
            modules: {
                rotulosPersonalizados
            }
        }),
        mocks: {errors},
        propsData:{
            estorno: {motivo:'Motivo', data:new Date('1995-12-17T03:24:00-0300'), usuario:'admin', patrimoniosId:[{id:1},{id:2}]}
        }
    })

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        router = {
            init: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'ModalEstornarPatrimoniosFinalizar',
                    params: {incorporacaoId: 7}
                }
            }
        }
        state = {
            loki:{
                user:{
                    name:'admin'
                },
                timezone:'America/Campo_Grande'
            }
        }

        errors = {
            collect:jest.fn()
        }

        actions = {[actionTypes.PATRIMONIO.ESTORNAR_PATRIMONIOS]: jest.fn()}

        mutations = {
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_ESTORNO]: jest.fn(),
            [mutationTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO]: jest.fn()
        }
    })

    describe('Mounted', () => {
        it('Deve setar incorporacaoId', () => {
            wrapper = defaultMount()
            flushPromises()
            expect(wrapper.vm.estorno.incorporacaoId).toEqual(wrapper.vm.$route.params.incorporacaoId)
        })
    })

    describe('Computed', () => {
        it('Deve verificar formatqação da data', () => {
            wrapper = defaultMount()
            flushPromises()
            expect(wrapper.vm.dataAtual).toEqual('17/12/1995 - 03h24')
        })

        it('Deve retornar quantidade patrimonios 0', () => {
            wrapper = shallowMount(ModalEstornarPatrimoniosFinalizar, {
                vuetify,
                localVue,
                router,
                store: new Vuex.Store({
                    state, actions,
                    modules: {
                        rotulosPersonalizados
                    }
                }),
                mocks: {errors},
                propsData:{
                    estorno: {motivo:'Motivo', data:new Date('1995-12-17T03:24:00'), usuario:'admin', patrimoniosId:[]}
                }
            })
            flushPromises()

            expect(wrapper.vm.quantidadePatrimonios).toEqual(0)
        })

        it('Deve retornar quantidade patrimonios', () => {
            wrapper = defaultMount()
            flushPromises()

            expect(wrapper.vm.quantidadePatrimonios).toEqual(2)
        })
    })

    describe('Methods', () => {
        it('Deve redirecionar ao fechar o modal e pedir busca de registro para atualização de informações na tela', () => {
            wrapper = defaultMount()
            wrapper.vm.fecharModal()
            flushPromises()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'VisualizarRegistroIncorporacao',params: {incorporacaoId: 7}})
        })

        it('Deve finalizar estorno', async () => {
            wrapper = defaultMount()
            wrapper.vm.finalizar()
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.ESTORNAR_PATRIMONIOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.ESTORNAR_PATRIMONIOS].mock.calls[0][1]).toEqual(wrapper.vm.estorno)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(router.replace).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO]).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'VisualizarRegistroIncorporacao', params: {incorporacaoId: 7}})
        })

        it('Deve redirecionar para modal listagem', () => {
            wrapper = defaultMount()
            wrapper.vm.voltar()
            flushPromises()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'ModalEstornarPatrimoniosListagem'})
        })

        it('Deve pegar apenas id dos patrimonios selecionados', () => {
            wrapper = defaultMount()
            wrapper.vm.filtraIdPatrimoniosSelecionados()
            flushPromises()
            expect(wrapper.vm.estorno.patrimoniosId).toEqual([1, 2])
        })

        it('Deve preparar data/hora para envio', () => {
            wrapper = defaultMount()
            flushPromises()
            expect(wrapper.vm.retornaDataHoraComTimezoneDoSistema(new Date('1995-12-17T03:24:00-0300'))).toEqual('1995-12-17T03:24:00.000-0300')
        })

        it('Deve retornar offset', () => {
            wrapper = defaultMount()
            flushPromises()
            expect(wrapper.vm.retornaOffset(new Date('1995-12-17T03:24:00'))).toEqual('-03:00')
        })
    })
})
