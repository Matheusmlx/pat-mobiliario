import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalReaberturaIncorporacao from './ModalReaberturaIncorporacao'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import actionTypes from '@/core/constants/actionTypes'

describe('ModalReaberturaIncorporacao', () => {

    let localVue, router, vuetify, actions

    const defaultMount = () => shallowMount(ModalReaberturaIncorporacao, {
        localVue,
        router,
        vuetify,
        store: new Vuex.Store({actions})
    })

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {
                    params: {
                        incorporacaoId: 7
                    }
                }
            }
        }

        actions = {
            [actionTypes.PATRIMONIO.INCORPORACAO.REABRIR_INCORPORACAO]: jest.fn()
        }
    })

    describe('Methods', () => {

        it('Deve emitir evento para fechar o modal', () => {
            const wrapper = defaultMount()

            wrapper.vm.fecharModal()

            expect(wrapper.vm.dialog).toBe(false)
        })

        it('Deve tratar evento para reabrir a incorporação', async() => {
            const wrapper = defaultMount()

            await wrapper.vm.tratarEventoReabrirIncorporacao()

            expect(wrapper.emitted().reabrirIncorporacao).toBeTruthy()
        })
    })
})
