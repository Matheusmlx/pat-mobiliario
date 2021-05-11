import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import VisualizarRegistroIncorporacao from './VisualizarRegistroIncorporacao'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'

describe('VisualizarRegistroIncorporacao', () => {

    let wrapper, actions, store, localVue, router, state,mutations

    const incorporacao = {
        id:1,
        codigo:1234343,
        fornecedor:'fornecedor',
        dataRecebimento: '2020-09-01T23:00:00.000-0400',
        situacao:'FINALIZADO',
        reconhecimento:'Aquisição Separada',
        numProcesso: 2545435,
        orgao: 'DPGE - Órgão doido',
        setor: 'SETOR- Setor pirado',
        dataFinalizacao:  '2020-09-01T23:00:00.000-0400',
        dataNota:  '2020-09-01T23:00:00.000-0400',
        valorNota: 200.35,
        convenio: 'convenio'
    }

    beforeEach(() => {

        router = {
            routes:[],
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: { name: 'VisualizarRegistroIncorporacao', params: {incorporacaoId: incorporacao.id}}
            }
        }
        state = {
            loki: {
                user: {
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                },
            },
            incorporacao:{rota:{origem:'IncorporacaoListagem'}}
        }

        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao),
            [actionTypes.PATRIMONIO.INCORPORACAO.REABRIR_INCORPORACAO]: jest.fn()
        }

        mutations = {
            [mutationTypes.PATRIMONIO.INCORPORACAO.RESETA_SITUACAO_REABERTURA]: jest.fn()
        }

        store = new Vuex.Store({actions, state,mutations})
    })

    describe('methods', () => {


        it('deve buscar a incorporacao pelo id', async () => {
            wrapper = shallowMount(VisualizarRegistroIncorporacao, {
                localVue,
                router,
                store
            })

            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO_POR_ID].mock.calls[0][1]).toEqual(wrapper.vm.incorporacao.id)
            expect(wrapper.vm.incorporacao).toEqual(incorporacao)
        })

        it('deve tratar evento para reabrir incorporação', async () => {
            wrapper = shallowMount(VisualizarRegistroIncorporacao, {
                localVue,
                router,
                store
            })

            await wrapper.vm.tratarEventoReabrirIncorporacao(7)

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.REABRIR_INCORPORACAO]).toBeCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.REABRIR_INCORPORACAO].mock.calls[0][1]).toBe(7)
        })

    })
})
