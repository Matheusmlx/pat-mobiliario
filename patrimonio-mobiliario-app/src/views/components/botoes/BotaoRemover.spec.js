import {shallowMount} from '@vue/test-utils'
import BotaoRemover from './BotaoRemover'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('BotaoRemover',()=>{
    let wrapper,localVue,$validator,errors

    beforeEach(()=>{
        localVue = applicationTestBuilder.build()

        errors = {
            collect:jest.fn()
        }


        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            }
        }

    })

    describe('Methods',()=>{
        it('Deve setar o estado do botão',()=>{
            wrapper = shallowMount(BotaoRemover,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.estadoBotao()

            expect(wrapper.vm.color).toEqual('red')
        })

        it('Deve mudar o estado do botão',()=>{
            wrapper = shallowMount(BotaoRemover,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.estadoBotao()
            wrapper.vm.estadoBotao()

            expect(wrapper.vm.color).toEqual('grey')
        })
    })

    describe('Events', () => {
        it('Deve emitir evento remover',()=>{
            wrapper = shallowMount(BotaoRemover,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.remove()

            expect(wrapper.emitted().remover).toBeTruthy()
        })
    })
})
