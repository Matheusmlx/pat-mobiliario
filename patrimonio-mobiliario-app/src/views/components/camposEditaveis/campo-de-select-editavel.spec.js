import {shallowMount} from '@vue/test-utils'
import CampoDeSelectEditavel from './campo-de-select-editavel'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('CampoDeSelectEditavel',()=>{
    let wrapper,localVue,$validator,errors

    const items = [
        {
            id: 1,
            descricao: 'teste'
        },
        {
            id: 2,
            descricao: 'numero'
        }
    ]

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

        it('Deve retornar o objeto enumerado de acordo com o selecionado',async()=>{
            wrapper = shallowMount(CampoDeSelectEditavel,{
                localVue,
                mocks:{
                    $validator,
                    errors
                },
                propsData:{
                    items: items,
                    value: 1
                }
            })
            await flushPromises()

            expect(wrapper.vm.setarTipoDocumento()).toEqual({ descricao: 'teste', id: 1})
        })

        it('Deve emitir um evento de estar adicionando um valor',async()=>{
            wrapper = shallowMount(CampoDeSelectEditavel,{
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
            wrapper = shallowMount(CampoDeSelectEditavel,{
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
            wrapper = shallowMount(CampoDeSelectEditavel,{
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
