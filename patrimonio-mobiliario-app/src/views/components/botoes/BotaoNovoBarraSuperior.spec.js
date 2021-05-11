import {mount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoNovoBarraSuperior from './BotaoNovoBarraSuperior'

describe('BotaoNovoBarraSuperior', () => {
    let wrapper, state, store, localVue

    beforeEach(() => {
        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO'
                }
            }
        }

        Vue.use(Vuex)
        store = new Vuex.Store({state})

        localVue = applicationTestBuilder.build()
    })

    describe('renderização', () => {
        it('deve mostrar o botão desabilitado', () => {
            wrapper = mount(BotaoNovoBarraSuperior, {
                localVue,
                store,
                propsData: {
                    desabilitar: true,
                    mensagemTooltip: 'Mensagem tooltip'
                }
            })

            expect(wrapper.find('button').attributes().disabled).toBe('disabled')
            expect(wrapper.find('.v-tooltip')).toBeDefined()
        })

        it('deve mostrar o botão habilitado', () => {
            wrapper = mount(BotaoNovoBarraSuperior, {
                localVue,
                store
            })

            expect(wrapper.find('button').attributes().disabled).not.toBeDefined()
            expect(wrapper.find('.v-tooltip').exists()).toBeFalsy()
        })
    })

    describe('eventos', () => {
        it('Deve emitir o evento de click quando estiver habilitado ', async () => {
            const component = {
                template: '<div><botao-novo-barra-superior @click="test"/></div>',
                components: {BotaoNovoBarraSuperior},
                methods: {
                    test: jest.fn()
                }
            }

            wrapper = mount(component, {
                localVue,
                store
            })

            wrapper.find('button').trigger('click')
            await wrapper.vm.$nextTick()

            expect(component.methods.test).toHaveBeenCalled()
        })
    })
})
