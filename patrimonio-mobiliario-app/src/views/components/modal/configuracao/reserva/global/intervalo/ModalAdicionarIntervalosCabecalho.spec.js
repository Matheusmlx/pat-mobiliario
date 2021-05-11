import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import ModalAdicionarIntervalosCabecalho from './ModalAdicionarIntervalosCabecalho'

describe('ModalAdicionarIntervalosCabecalho', () => {

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
    const defaultMount = () => shallowMount(ModalAdicionarIntervalosCabecalho, {
        store,
        router,
        localVue,
        propsData: { reserva: {quantidadeReservada: 0}}
    })

    describe('methods', () => {
        it('deve emitir evento de fechar modal', () => {
            wrapper = defaultMount()
            wrapper.vm.fecharModal()
            expect(wrapper.emitted().fecharModal).toBeTruthy()
        })

        it('deve emitir evento de remover filtro', () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoRemoverFiltro(1)

            expect(wrapper.emitted().removerFiltro).toBeTruthy()
            expect(wrapper.emitted().removerFiltro[0][0]).toEqual(1)
        })

        it('deve emitir evento de busca simples', () => {
            wrapper = defaultMount()
            wrapper.vm.tratarEventoBuscaSimples(1)

            expect(wrapper.emitted().buscaSimples).toBeTruthy()
            expect(wrapper.emitted().buscaSimples[0][0]).toEqual(1)
        })
    })
})
