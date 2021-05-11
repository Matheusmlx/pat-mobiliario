import {shallowMount} from '@vue/test-utils'
import CampoDeArquivoEditavel from './campo-de-arquivo-editavel'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'

describe('CampoDeArquivoEditavel',()=>{
    let wrapper,localVue,$validator,errors, state, store

    beforeEach(()=>{
        localVue = applicationTestBuilder.build()

        errors = {
            collect:jest.fn()
        }

        state = {
            loki: {
                user: {
                    authorities: [{name: 'Mobiliario.Normal', hasAccess: true}]
                },
                file: {
                    api: 'api'
                }
            },
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            }
        }

        store = new Vuex.Store({state})
    })


    describe('Methods',()=>{

        it('Deve emitir um evento de estar adicionando um arquivo',async()=>{
            wrapper = shallowMount(CampoDeArquivoEditavel,{
                localVue,
                store,
                mocks:{
                    $validator,
                    errors
                },
                propsData: { estaAdicionando: true, name: 'arquivo' }
            })
            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.emitted().estaAdicionando).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(false)
        })

        it('Deve emitir um evento de setar edição de um arquivo',async()=>{
            wrapper = shallowMount(CampoDeArquivoEditavel,{
                localVue,
                store,
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

        it('Deve limpar o valor do input ao iniciar a edição',async()=>{
            wrapper = shallowMount(CampoDeArquivoEditavel,{
                localVue,
                store,
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
            wrapper = shallowMount(CampoDeArquivoEditavel,{
                localVue,
                store,
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
