import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {shallowMount} from '@vue/test-utils'
import ModalAdicionarReservaPorOrgaoAcoes from './ModalAdicionarReservaPorOrgaoAcoes'

describe('ModalAdicionarReservaPorOrgaoAcoes', () => {

    let wrapper, store, localVue, router

    beforeEach(() => {
        router = {
            routes: [],
            push: jest.fn(),
            replace: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'ReservaCadastro'
                }
            }
        }

        localVue = applicationTestBuilder.build()
    })

    const defaultMount = () => shallowMount(ModalAdicionarReservaPorOrgaoAcoes, {
        router,
        localVue,
        propsData: {
            podeFinalizar: false
        },
    })

    describe('methods', () => {
        it('Deve fechar o modal', () => {
            wrapper = defaultMount()
            wrapper.vm.fecharModal()
            expect(router.push.mock.calls[0][0]).
              toEqual({ name: 'ReservaListagem' })
        })

        it('Deve finalizar', () => {
            wrapper = defaultMount()
            wrapper.vm.finalizar()
            expect(wrapper.emitted().finalizar).toBeTruthy()
        })
    })
})
