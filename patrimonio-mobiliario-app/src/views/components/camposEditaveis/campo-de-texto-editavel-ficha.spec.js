import {shallowMount} from '@vue/test-utils'
import CampoDeTextoEditavelFicha from './campo-de-texto-editavel-ficha'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('CampoDeTextoEditavelFicha',()=>{
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
                    items: []
                },
            }
        }

    })


    describe('Methods',()=>{

        it('Deve emitir um evento de estar adicionando um valor',async()=>{
            wrapper = shallowMount(CampoDeTextoEditavelFicha,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.vm.editing).toEqual(true)
        })

        it('Deve enviar valor',async()=>{
            wrapper = shallowMount(CampoDeTextoEditavelFicha,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.submitEdit()
            await flushPromises()


            expect(wrapper.vm.editing).toEqual(false)
            expect(wrapper.emitted().input).toBeTruthy()
        })

        it('Deve emitir um evento quando estiver retirando a edição',async()=>{
            wrapper = shallowMount(CampoDeTextoEditavelFicha,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.cancelEdit()
            await flushPromises()

            expect(wrapper.vm.editing).toEqual(false)
        })

        it('Deve formatar placa', ()=>{
            wrapper = shallowMount(CampoDeTextoEditavelFicha,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.model = 'JDK5567'

            expect(wrapper.vm.placaFormatada).toEqual('JDK-5567')
        })

        it('Não deve formatar placa ja formatada', ()=>{
            wrapper = shallowMount(CampoDeTextoEditavelFicha,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.model = 'JDK-5567'

            expect(wrapper.vm.placaFormatada).toEqual('JDK-5567')
        })

        it('deve retornar null se não houver model', ()=>{
            wrapper = shallowMount(CampoDeTextoEditavelFicha,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.model = null

            expect(wrapper.vm.placaFormatada).toEqual(null)
        })




        it('Deve formatar chassi', ()=>{
            wrapper = shallowMount(CampoDeTextoEditavelFicha,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.model = 'aswRteWfdGtg5RghW'

            expect(wrapper.vm.chassiFormatado).toEqual('asw RteWf d Gtg5RghW')
        })

        it('Não deve formatar chassiFormatado ja formatado', ()=>{
            wrapper = shallowMount(CampoDeTextoEditavelFicha,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.model = 'asw RteWf d Gtg5RghW'

            expect(wrapper.vm.chassiFormatado).toEqual('asw RteWf d Gtg5RghW')
        })

        it('deve retornar null em chassiFormatado se não houver model', ()=>{
            wrapper = shallowMount(CampoDeTextoEditavelFicha,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.model = null

            expect(wrapper.vm.chassiFormatado).toEqual(null)
        })


    })
})
