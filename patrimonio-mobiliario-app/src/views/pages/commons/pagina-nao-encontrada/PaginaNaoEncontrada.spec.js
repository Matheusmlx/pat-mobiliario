import {shallowMount} from '@vue/test-utils'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import PaginaNaoEncontrada from './PaginaNaoEncontrada'

describe('RedirecionaEditarUsuario', () => {
    let localVue, router, wrapper

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'PaginaNaoEncontrada',
                },
            },
        }
    })

    describe('methods', () => {
        it('deve redirecionar para página inicial', async () => {
           wrapper = shallowMount(PaginaNaoEncontrada, {
                localVue,
                router
            })

            wrapper.vm.irParaInicio()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'Inicial'})
        })
    })
})
