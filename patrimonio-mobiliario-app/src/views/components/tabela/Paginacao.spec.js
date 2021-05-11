import {shallowMount} from '@vue/test-utils'
import Paginacao from './Paginacao'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('Pagination', () => {
    let wrapper, localVue

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
    })

    describe('Events', () => {
        it('Deve emitir evento para resetar página',() => {
            wrapper = shallowMount(Paginacao, {
                localVue,
                propsData:{
                    paginacaoInterna: {page: 1, rowsPerPage: 10},
                    paginas: 2
                }
            })

            wrapper.vm.resetaPage()

            expect(wrapper.emitted().resetaPage).toBeTruthy()
        })
    })
})
