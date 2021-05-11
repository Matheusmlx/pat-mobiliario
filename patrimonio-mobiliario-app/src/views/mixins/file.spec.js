import Vue from 'vue'
import file from './file'
import alert from './alert'
import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import mutationTypes from '../../core/constants/mutationTypes'

Vue.use(Vuex)

describe('File', () => {
    let store, mockState, mockComponente, wrapper, mutations

    mockState = {
        loki: {
            file: {
                api: 'api_teste/',
                maxSize: '10mb'
            }
        }
    }
    mutations = {
        [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
    }
    store = new Vuex.Store({
        state: mockState,
        mutations
    })

    beforeEach(() => {
        mockComponente = {
            render() {},
            mixins: [file, alert]
        }
        wrapper = shallowMount(mockComponente, { store })
    })

    it('criarLinkDownload', () => {
        const link = wrapper.vm.criarLinkDownload('teste')

        expect(link).toEqual('api_teste/?uri=teste&thumbnail=false')
    })

    it('criarLinkDownloadTemporario', () => {
        const id = 1
        const link = wrapper.vm.criarLinkDownloadTemporario(id)

        expect(link).toEqual(`api_teste//temporario?uri=${id}&thumbnail=false`)
    })

    it('tratarErroEnvioArquivo', () => {
        wrapper.vm.tratarErroEnvioArquivo()

        expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('O arquivo selecionado deve ter no máximo 10mb.')
    })
})
