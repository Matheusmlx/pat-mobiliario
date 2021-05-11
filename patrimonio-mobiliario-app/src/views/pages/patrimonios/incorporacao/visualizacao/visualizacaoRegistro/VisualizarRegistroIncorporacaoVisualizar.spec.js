import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import VisualizarRegistroIncorporacaoVisualizar from './VisualizarRegistroIncorporacaoVisualizar'
import flushPromises from 'flush-promises'
import {actionTypes} from '@/core/constants'

describe('VisualizarRegistroIncorporacaoVisualizar', () => {

    let wrapper, actions, store, localVue, router, state

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
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: { name: 'VisualizarRegistroIncorporacaoVisualizacao', params: {incorporacaoId: incorporacao.id}}
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
            [actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO_POR_ID]: jest.fn().mockReturnValue(incorporacao)
        }

        store = new Vuex.Store({actions, state})
    })

    describe('methods', () => {


        it('deve buscar a incorporacao pelo id', async () => {
            wrapper = shallowMount(VisualizarRegistroIncorporacaoVisualizar, {
                localVue,
                router,
                store
            })

            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_REGISTRO_INCORPORACAO_POR_ID].mock.calls[0][1]).toEqual(wrapper.vm.incorporacao.id)
            expect(wrapper.vm.incorporacao).toEqual(incorporacao)
        })

        it('Deve abrir o modal ao estar na rota do modal de itens', async () =>{
            const router = {
                init: jest.fn(),
                push: jest.fn(),
                history: {
                    current: { name: 'ModalItemVisualizarRegistroVisualizacao', params: {incorporacaoId: 1 , itemIncorporacaoId: 2}}
                }
            }
            wrapper = shallowMount(VisualizarRegistroIncorporacaoVisualizar, {
                localVue,
                router,
                store
            })

            expect(wrapper.vm.modalItem).toBeTruthy
            expect(router.push.mock.calls[0][0]).toEqual({name: 'ModalItemVisualizarRegistroVisualizacao', params: {incorporacaoId: 1, itemIncorporacaoId: 2}})
        })

        it('deve fechar modal', async () => {
            wrapper = shallowMount(VisualizarRegistroIncorporacaoVisualizar, {
                localVue,
                router,
                store
            })
            wrapper.vm.fecharModalItem()

            await flushPromises()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'VisualizarRegistroIncorporacaoVisualizacao', params: {incorporacaoId: 1}})
        })


    })
})
