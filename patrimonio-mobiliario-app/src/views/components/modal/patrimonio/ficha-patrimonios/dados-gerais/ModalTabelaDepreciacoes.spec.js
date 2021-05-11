import {shallowMount} from '@vue/test-utils'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import ModalTabelaDepreciacoes
    from '@/views/components/modal/patrimonio/ficha-patrimonios/dados-gerais/ModalTabelaDepreciacoes'

describe('ModalTabelaDepreciacoes', () => {
    let wrapper, localVue, vuetify, router

    const patrimonio = { id:1 }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {current: { name: 'ModalTabelaDepreciacoes', params:{patrimonioId: patrimonio.id}}}
        }
    })

    const defaultMount  = () => shallowMount(ModalTabelaDepreciacoes, {
        localVue,
        vuetify,
        router
    })

    describe('Methods', () => {
        it('deve fechar modal e redirecionar para ficha dos patrimonios', async () => {
            wrapper = defaultMount()

            wrapper.vm.fecharModal()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'FichaPatrimonioDadosGerais',params:{patrimonioId: patrimonio.id}})
        })
    })
})
