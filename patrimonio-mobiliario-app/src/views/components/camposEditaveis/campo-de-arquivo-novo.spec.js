import {shallowMount} from '@vue/test-utils'
import CampoDeArquivoNovo from './campo-de-arquivo-novo'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('CampoDeArquivoNovo',()=>{
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

        it('Deve limpar o valor do input ao iniciar a edição',async()=>{
            wrapper = shallowMount(CampoDeArquivoNovo,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                propsData: { value: 'teste-arquivo' }
            })
            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.vm.model).toEqual(null)
            expect(wrapper.vm.editing).toEqual(true)
        })

        it('Deve emitir um evento quando submeter a edição',async()=>{
            wrapper = shallowMount(CampoDeArquivoNovo,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.model = { name: 'teste-arquivo.png' }
            wrapper.vm.submitEdit()
            await flushPromises()
            expect(wrapper.emitted().input).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(false)
        })

    })
})
