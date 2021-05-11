import {shallowMount} from '@vue/test-utils'
import CampoDeDataEditavelFicha from './campo-de-data-editavel-ficha'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('CampoDeDataEditavelFicha',()=>{
    let wrapper,localVue,$validator,errors

    beforeEach(()=>{
        localVue = applicationTestBuilder.build()

        errors = {
            collect:jest.fn()
        }


        $validator = {
            validateAll: jest.fn().mockReturnValue(true),
            errors: {
                clear: jest.fn(),
            }
        }

    })

    const defaultMount = () => shallowMount(CampoDeDataEditavelFicha,{
        localVue,
        mocks:{
            $validator,
            errors
        },
        directives: {
            mask: jest.fn()
        }
    })

    describe('Methods',()=>{

        it('Deve emitir um evento de estar adicionando um valor',async()=>{
            wrapper = defaultMount()
            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.vm.editing).toEqual(true)
        })

        it('Deve emitir um evento de setar edição de um valor',async()=>{
            wrapper = defaultMount()

            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.vm.editing).toEqual(true)
        })

        it('Deve emitir um evento quando estiver retirando a edição',async()=>{
            wrapper = defaultMount()

            wrapper.vm.cancelEdit()
            await flushPromises()

            expect(wrapper.vm.editing).toEqual(false)
        })

        it('Deve emitir os eventos ao submeter a edição',async()=>{
            wrapper = shallowMount(CampoDeDataEditavelFicha,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                directives: {
                    mask: jest.fn()
                },
                propsData:{ required: true, value: '2020-12-18T04:00:00.000-0400'}
            })

            await wrapper.vm.submitEdit()
            await flushPromises()

            expect(wrapper.emitted().input).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(false)
        })
    })
})
