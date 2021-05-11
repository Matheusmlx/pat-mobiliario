import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalEstornarPatrimoniosVisualizarEstorno from './ModalEstornarPatrimoniosVisualizarEstorno'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('ModalEstornarPatrimoniosVisualizarEstorno', () => {

    let localVue, router, vuetify, state, mutations, wrapper, errors

    const rotulosPersonalizados = {
        namespaced: true,
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
        }
    }

    const defaultMount = () => shallowMount(ModalEstornarPatrimoniosVisualizarEstorno, {
        vuetify,
        localVue,
        router,
        store: new Vuex.Store({
            state, mutations,
            modules: {
                rotulosPersonalizados
            }
        }),
        mocks: {errors},
        propsData:{
            estorno: {motivo:'Motivo', data:new Date('1995-12-17T03:24:00-0300'), usuario:'admin'}
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
                    name: 'ModalEstornarPatrimoniosVisualizarEstorno',
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

        mutations = {
            setEstorno: jest.fn()
        }

        errors = {
            collect:jest.fn()
        }
    })

    describe('Computed', () => {
        it('Deve verificar formatqação da data', () => {
            wrapper = defaultMount()
            flushPromises()
            expect(wrapper.vm.dataEstorno).toEqual('17/12/1995 - 03h24')
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
    })
})
