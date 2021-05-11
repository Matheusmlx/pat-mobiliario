import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalAdicionarIntervalosAcoes from './ModalAdicionarIntervalosAcoes'

describe('ModalAdicionarIntervalosAcoes', () => {

    let wrapper, store, localVue, router, state

    beforeEach(() => {

        router = {
            routes: [],
            push: jest.fn(),
            replace: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'ModalAdicionarIntervalos',
                    params: {id: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()

        store = new Vuex.Store({state})
    })
    const defaultMount = () => shallowMount(ModalAdicionarIntervalosAcoes, {
        store,
        router,
        localVue
    })

    describe('methods', () => {

        it('deve emitir evento de fechar modal', () => {
            wrapper = defaultMount()
            wrapper.vm.fecharModal()

            expect(wrapper.emitted().fecharModal).toBeTruthy()
        })

        it('deve emitir evento de adicionar', () => {
            wrapper = defaultMount()
            wrapper.vm.adicionar()

            expect(wrapper.emitted().adicionar).toBeTruthy()
        })

    })
})
