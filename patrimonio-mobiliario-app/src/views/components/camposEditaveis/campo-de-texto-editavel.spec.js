import {shallowMount} from '@vue/test-utils'
import CampoDeTextoEditavel from './campo-de-texto-editavel'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('CampoDeTextoEditavel',()=>{
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
            wrapper = shallowMount(CampoDeTextoEditavel,{
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
            wrapper = shallowMount(CampoDeTextoEditavel,{
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
            wrapper = shallowMount(CampoDeTextoEditavel,{
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

    })
})
