import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import MovimentacaoInternaListagemItensVazia from './MovimentacaoInternaListagemItensVazia'

describe('MovimentacaoInternaListagemItensVazia', () => {

    let wrapper, store, localVue, router, state

    beforeEach(() => {

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Movimentacoes', hasAccess: true}]
                }
            }
        }

        router = {
            routes: [],
            push: jest.fn(),
            init: jest.fn(),
            history: {
                current: {
                    name: 'MovimentacaoInternaListagemItens',
                    params: { movimentacaoInternaId: 1}
                }
            }
        }

        localVue = applicationTestBuilder.build()

        store = new Vuex.Store({state})
    })

    const defaultMount = () => shallowMount(MovimentacaoInternaListagemItensVazia, {
        store,
        router,
        localVue
    })

    describe('methods', () => {
        it('deve chamar adicionar Itens', () => {
            wrapper = defaultMount()
            wrapper.vm.adicionarItem()

            expect(wrapper.emitted().adicionarItem).toBeTruthy()
        })
    })
})
