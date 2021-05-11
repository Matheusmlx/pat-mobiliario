import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalEstornarPatrimoniosMotivo from './ModalEstornarPatrimoniosMotivo'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'
import {mutationTypes} from '@/core/constants'

describe('ModalEstornarPatrimoniosMotivo', () => {

    let localVue, router, vuetify, state, wrapper, errors, mutations

    const rotulosPersonalizados = {
        namespaced: true,
        getters: {
            getNomeRotulosPersonalizados: () => jest.fn(),
        }
    }

    const defaultMount = () => shallowMount(ModalEstornarPatrimoniosMotivo, {
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
            estorno: {motivo:'Motivo'}
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
                    name: 'ModalEstornarPatrimoniosMotivo',
                    params: {incorporacaoId: 7}
                }
            }
        }
        state = {
            patrimonio:{
                estorno:{motivo:false}
            },
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

        mutations = {[mutationTypes.PATRIMONIO.SET_ESTORNO]: jest.fn()}
    })

    describe('Computed', () => {
        it('Deve verificar formatqação da data', () => {
            wrapper = defaultMount()
            flushPromises()
            wrapper.vm.data = new Date('1995-12-17T03:24:00-0300')
            expect(wrapper.vm.dataAtual).toEqual('17/12/1995 - 03h24')
        })
    })

    describe('Methods', () => {
        it('Deve redirecionar ao fechar o modal', () => {
            wrapper = defaultMount()
            wrapper.vm.fecharModal()
            flushPromises()
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'VisualizarRegistroIncorporacao',params: {incorporacaoId: 7}})
        })
        it('Deve setar estorno e redirecionar para listagem', () => {
            wrapper = defaultMount()
            wrapper.vm.data = new Date('1995-12-17T03:24:00')
            wrapper.vm.continuar()
            flushPromises()

            expect(wrapper.vm.estorno.data).toEqual(wrapper.vm.data)
            expect(wrapper.vm.estorno.usuario).toEqual(wrapper.vm.usuario)
            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'ModalEstornarPatrimoniosListagem'})
            expect(mutations[mutationTypes.PATRIMONIO.SET_ESTORNO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.SET_ESTORNO].mock.calls[0][1]).toEqual(wrapper.vm.estorno)
        })
    })
})
