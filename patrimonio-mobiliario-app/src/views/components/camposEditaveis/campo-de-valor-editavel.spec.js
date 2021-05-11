import {shallowMount} from '@vue/test-utils'
import CampoDeValorEditavel from './campo-de-valor-editavel'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('CampoDeValorEditavel',()=>{
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

        it('Deve anular o valor caso seja zero',async()=>{
            wrapper = shallowMount(CampoDeValorEditavel,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                propsData:{
                    value: 0
                }
            })
            wrapper.vm.anulaSeValorZero()
            await flushPromises()

            expect(wrapper.emitted().verificaObrigatoriedade).toBeTruthy()
            expect(wrapper.vm.model).toEqual(null)


        })

        it('Deve emitir um evento de estar adicionando um valor',async()=>{
            wrapper = shallowMount(CampoDeValorEditavel,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                propsData: { estaAdicionando: true }
            })
            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.emitted().estaAdicionando).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(false)
        })

        it('Deve emitir um evento de setar edição de um valor',async()=>{
            wrapper = shallowMount(CampoDeValorEditavel,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.emitted().setaEditando).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(true)
        })

        it('Deve emitir um evento quando estiver retirando a edição',async()=>{
            wrapper = shallowMount(CampoDeValorEditavel,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.cancelEdit()
            await flushPromises()
            expect(wrapper.emitted().retiraEditando).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(false)
        })

        it('Deve emitir os eventos ao submeter a edição',async()=>{
            wrapper = shallowMount(CampoDeValorEditavel,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                propsData:{ required: true }
            })
            wrapper.vm.model = 5.5
            wrapper.vm.submitEdit()
            await flushPromises()
            expect(wrapper.emitted().retiraEditando).toBeTruthy()
            expect(wrapper.emitted().input).toBeTruthy()
            expect(wrapper.emitted().change).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(false)
        })
    })
})
