import {mount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import BotaoNovo from './BotaoNovo'

describe('BotaoNovo', () => {
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
        it('Deve renderizar o componente corretamente', () => {
            wrapper = mount(BotaoNovo, {
                localVue,
                store
            })

            expect(wrapper.find('button')).toBeDefined()
            expect(wrapper.find('button').classes().includes('botao-novo')).toBeTruthy()
            expect(wrapper.find('button').classes().includes('call-to-action')).toBeTruthy()
        })
    })

    describe('eventos', () => {
        it('deve emitir o evento click quando for clicado', async () => {
            const component = {
                template: '<div><botao-novo @click="test"/></div>',
                components: {BotaoNovo},
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
