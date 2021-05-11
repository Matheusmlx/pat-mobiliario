import {mount} from '@vue/test-utils'
import Vuex from 'vuex'
import Vue from 'vue'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Chat from './Chat'

describe('Chat', () => {

    let state, getters, store, localVue

    beforeEach(() => {
        state = {
            parametros: {
                urlChatSuporte: 'http://urlChatSuporte.com.br'
            }
        }

        getters = {
            parametrosSistema: () => state.parametros
        }

        Vue.use(Vuex)
        store = new Vuex.Store({
            modules: {parametros: {namespaced: true, state, getters}}
        })

        localVue = applicationTestBuilder.build()
    })

    it('deve adicionar o script do chat quando montar o app', () => {
        let s1=document.createElement('script')
        document.body.append(s1)

        mount(Chat, {
            localVue,
            store
        })

        let expectedUrl = document.getElementById('chat-suporte').attributes['src'].value
        expect(expectedUrl).toEqual(state.parametros.urlChatSuporte)
    })
})
