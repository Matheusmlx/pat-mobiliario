import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import VisualizarRegistroIncorporacaoEmpenho from './VisualizarRegistroIncorporacaoEmpenho'
import flushPromises from 'flush-promises'
import {actionTypes} from '@/core/constants'

describe('VisualizarRegistroIncorporacaoEmpenho', () => {

    let wrapper, actions, store, localVue, router, state

    const empenhos = {
        items: [
            {numeroEmpenho: '3', dataEmpenho: '2020-09-03T23:00:00.000-0400', valorEmpenho: 30.98},
            {numeroEmpenho: '2', dataEmpenho: '2020-09-01T23:00:00.000-0400', valorEmpenho: 25.98},
            {numeroEmpenho: '1', dataEmpenho: '2020-09-02T23:00:00.000-0400', valorEmpenho: 27.98},
            {numeroEmpenho: '3', dataEmpenho: '2020-09-03T23:00:00.000-0400', valorEmpenho: 30.98}
        ]
    }

    const propsData = {
        incorporacaoId: 1
    }

    beforeEach(() => {

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {name: 'VisualizarRegistroIncorporacao', params: {incorporacaoId: 1}}
            }
        }
        state = {
            loki: {
                user: {
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                },
            },
            incorporacao: {rota: {origem: 'IncorporacaoListagem'}}
        }

        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]: jest.fn().mockReturnValue(empenhos)
        }

        store = new Vuex.Store({actions, state})
    })

    describe('watch', () => {

        it('deve buscar os empenhos quando trocar a incorporação', async () => {
            wrapper = shallowMount(VisualizarRegistroIncorporacaoEmpenho, {
                localVue,
                router,
                store,
                propsData
            })

            await flushPromises()

            const novoIncorporacaoId = 2
            wrapper.setProps({
                incorporacaoId: novoIncorporacaoId
            })
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS].mock.calls[1][1]).toEqual(novoIncorporacaoId)
        })

    })

    describe('methods', () => {

        it('deve buscar todos empenhos e ordenar por numeroEmpenho', async () => {
            wrapper = shallowMount(VisualizarRegistroIncorporacaoEmpenho, {
                localVue,
                router,
                store,
                propsData
            })

            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS].mock.calls[0][1]).toEqual(1)
            expect(wrapper.vm.empenhos).toEqual(empenhos.items)
            expect(wrapper.vm.empenhos[0].numeroEmpenho).toEqual('1')
            expect(wrapper.vm.empenhos[1].numeroEmpenho).toEqual('2')
            expect(wrapper.vm.empenhos[2].numeroEmpenho).toEqual('3')
            expect(wrapper.vm.tamanhoTabela).toEqual('300')
        })


    })
})
