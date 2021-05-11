import { shallowMount } from '@vue/test-utils'
import flushPromises from 'flush-promises'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ReservaIntervaloDadosGeraisNovo from './ReservaIntervaloDadosGeraisNovo'
import {actionTypes,mutationTypes} from '@/core/constants'
import Vuex from 'vuex'

const reservaId = 1


describe('ReservaIntervaloDadosGeraisNovo', () => {
    let wrapper, router, localVue,state, store

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'ReservaVisualizacao',
                    params: {
                        id: 1,
                    },
                },
            },
        }

        state = {
            possuiNumerosUtilizados:true
        }

        store = new Vuex.Store({state})


    })



    const defaulShallowMount = () => shallowMount(ReservaIntervaloDadosGeraisNovo, {
            store,
            router,
            localVue,
            propsData: {
                reservaId: reservaId,
            }
        })

    describe('Methods',() => {

        it('Deve chamar modal de adição de intervalos', async () => {
            wrapper = defaulShallowMount()

            await wrapper.vm.adicionarIntervalos()
            await flushPromises()

            expect(router.replace).toHaveBeenCalled()
            expect(router.replace.mock.calls[0][0]).toEqual({name:'ModalAdicionarIntervalos', params: {id: reservaId}})
        })
    })
})
