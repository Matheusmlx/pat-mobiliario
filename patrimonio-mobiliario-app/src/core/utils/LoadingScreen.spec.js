import Vuex from 'vuex'
import {createLocalVue} from '@vue/test-utils'
import LoadingScreen from './LoadingScreen'

const localVue = createLocalVue()
localVue.use(Vuex)

const mutations = {
    setGlobalLoading: jest.fn(),
    setLoadingMessage: jest.fn()
}

const state = {
    defaultLoadingMessage: 'mock de mensagem'
}

const store = new Vuex.Store({ state, mutations })

const loadingScreen = new LoadingScreen(store)

describe('LoadingScreen', () => {

    it('Deve commitar uma mutation setGlobalLoading com payload igual a true ', () => {
        loadingScreen.start()
        expect(mutations.setGlobalLoading).toHaveBeenCalledWith(state, true)
    })

    it('Deve commitar uma mutation setGlobalLoading com payload igual a false ', () => {
        loadingScreen.stop()
        expect(mutations.setGlobalLoading).toHaveBeenCalledWith(state, false)
        expect(mutations.setLoadingMessage).toHaveBeenCalledWith(state, state.defaultLoadingMessage)
    })

    it('Deve commitar uma mutation setLoadingMessage com payload igual a msg default ', () => {
        loadingScreen.reset()
        expect(mutations.setLoadingMessage).toHaveBeenCalledWith(state, state.defaultLoadingMessage)
    })

})
