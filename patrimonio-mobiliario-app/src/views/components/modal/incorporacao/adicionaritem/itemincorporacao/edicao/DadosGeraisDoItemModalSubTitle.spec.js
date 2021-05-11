import {shallowMount} from '@vue/test-utils'
import DadosGeraisDoItemModalSubTitle from './DadosGeraisDoItemModalSubTitle'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('DadosGeraisDoItemModalSubTitle', async ()=>{
    let wrapper,localVue,vuetify,$options,$validator,errors

    const defaultShallMount = stubs => shallowMount(DadosGeraisDoItemModalSubTitle,{
        localVue,
        propsData:{
            value:{
                codigo:'1245687',
                descricao:'MOVEIS E ARMAMENTOS'
            }
        }
    })

    beforeEach(async () => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()
        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            },
        }
        errors = {
            collect: jest.fn(),
        }
    })

    describe('Render', async () =>{
        it('Deve renderizar o codigo e a descrição do item vindo por props', async () => {
            wrapper = await defaultShallMount()

            expect(wrapper.text()).toContain('1245687')
            expect(wrapper.text()).toContain('MOVEIS E ARMAMENTOS')
        })
    })    
})