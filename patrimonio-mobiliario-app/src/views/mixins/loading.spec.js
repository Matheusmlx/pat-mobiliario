import Vue from 'vue'
import loading from './loading'
import {shallowMount} from '@vue/test-utils'
import {mutationTypes} from '@/core/constants/'
import Vuex from 'vuex'

Vue.use(Vuex)

describe('Loading', () => {
    let mutations, store, mockComponente, wrapper

    mutations = {
        [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
        [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
        [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn()
    }

    store = new Vuex.Store({
        mutations
    })

    beforeEach(() => {
        mockComponente = {
            render() {},
            mixins: [loading]
        }
        wrapper = shallowMount(mockComponente, { store })
    })

    it('desabilitarLoadingGlobal', () => {
        wrapper.vm.desabilitarLoadingGlobal()

        expect(mutations[mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]).toHaveBeenCalled()
    })

    it('habilitarLoadingGlobal', () => {
        wrapper.vm.habilitarLoadingGlobal()

        expect(mutations[mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]).toHaveBeenCalled()
    })

    it('setMensagemLoading', () => {
        wrapper.vm.setMensagemLoading('teste')

        expect(mutations[mutationTypes.LOKI.SET_LOADING_MESSAGE]).toHaveBeenCalled()
    })
})
