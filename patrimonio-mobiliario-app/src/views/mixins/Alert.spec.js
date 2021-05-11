import Vue from 'vue'
import alert from './alert'
import {shallowMount} from '@vue/test-utils'
import {mutationTypes} from '@/core/constants/'
import Vuex from 'vuex'

Vue.use(Vuex)

describe('Alert', () => {
    let mutations, store, mockComponente, wrapper

    mutations = {
        [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
    }
    store = new Vuex.Store({
        mutations
    })

    beforeEach(() => {
        mockComponente = {
            render() {},
            mixins: [alert]
        }
        wrapper = shallowMount(mockComponente, { store })
    })

    it('mostrarNotificacaoErro', () => {
        wrapper.vm.mostrarNotificacaoErro('teste')

        expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('teste')
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('error')

    })

    it('mostrarNotificacaoErro General', () => {
        wrapper.vm.mostrarNotificacaoErro('GENERAL')

        expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Falha ao buscar as informações.')
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('error')

    })

    it('mostrarNotificacaoErroDefault', () => {
        wrapper.vm.mostrarNotificacaoErroDefault()

        expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Ocorreu um erro ao realizar a operação.')
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('error')
    })

    it('mostrarNotificacaoSucesso', () => {
        wrapper.vm.mostrarNotificacaoSucesso('teste')

        expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('teste')
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('success')
    })

    it('mostrarNotificacaoSucessoDefault', () => {
        wrapper.vm.mostrarNotificacaoSucessoDefault()

        expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Operação realizada com sucesso.')
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('success')
    })

    it('mostrarNotificacaoAviso', () => {
        wrapper.vm.mostrarNotificacaoAviso('teste')

        expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('teste')
        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
    })
})
