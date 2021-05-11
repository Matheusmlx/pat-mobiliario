import {shallowMount} from '@vue/test-utils'
import CampoDeDataEditavel from './campo-de-data-editavel'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('CampoDeDataEditavel',()=>{
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

        it('Deve emitir um evento de estar adicionando um valor',async()=>{
            wrapper = shallowMount(CampoDeDataEditavel,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                directives: {
                    mask: jest.fn()
                },
                propsData: { estaAdicionando: true }
            })
            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.emitted().estaAdicionando).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(false)
        })

        it('Deve emitir um evento de setar edição de um valor',async()=>{
            wrapper = shallowMount(CampoDeDataEditavel,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                directives: {
                    mask: jest.fn()
                }
            })
            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.emitted().setaEditando).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(true)
        })

        it('Deve emitir um evento quando estiver retirando a edição',async()=>{
            wrapper = shallowMount(CampoDeDataEditavel,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                directives: {
                    mask: jest.fn()
                }
            })
            wrapper.vm.cancelEdit()
            await flushPromises()
            expect(wrapper.emitted().retiraEditando).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(false)
        })

        it('Deve emitir os eventos ao submeter a edição',async()=>{
            wrapper = shallowMount(CampoDeDataEditavel,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                directives: {
                    mask: jest.fn()
                },
                propsData:{ required: true, value: '2020-10-14T04:00:00.000-0400'}
            })
            wrapper.vm.submitEdit()
            await flushPromises()
            expect(wrapper.emitted().retiraEditando).toBeTruthy()
            expect(wrapper.emitted().input).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(false)
        })
    })
})
