import {shallowMount} from '@vue/test-utils'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import FichaPatrimonioDadosGeraisCabecalho from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/dados-gerais/FichaPatrimonioDadosGeraisCabecalho'

describe('FichaPatrimonioDadosGeraisCampos', () => {
    let wrapper, localVue, vuetify

    let patrimonio = {
        id: 1,
        numero: 132323,
        dataIncorporado: '2020-09-01T23:00:00.000-0400',
        situacao: 'ATIVO'
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()
    })

    const defaultShallowMount = () => shallowMount(FichaPatrimonioDadosGeraisCabecalho,{
        localVue,
        vuetify,
        propsData:{patrimonio:patrimonio}
    })


    describe('Render', () => {
        it('Deve mostrar a situação ativo', async () => {

            wrapper = defaultShallowMount()

            const headerCabecalhoSituacao = wrapper.findAll('.headerCabecalho > p').at(2)
            expect(headerCabecalhoSituacao.text()).toBe('Ativo')
        })

        it('Deve mostrar a situação inativo', () => {
             patrimonio = {
                id: 1,
                numero: 132323,
                dataIncorporado: '2020-09-01T23:00:00.000-0400',
                situacao: 'INATIVO'
            }

            wrapper = defaultShallowMount()

            const headerCabecalhoSituacao = wrapper.findAll('.headerCabecalho > p').at(2)
            expect(headerCabecalhoSituacao.text()).toBe('Inativo')
        })

        it('Deve mostrar a situação estornado', () => {
            patrimonio = {
                id: 1,
                numero: 132323,
                dataIncorporado: '2020-09-01T23:00:00.000-0400',
                situacao: 'ESTORNADO'
            }

            wrapper = defaultShallowMount()

            const headerCabecalhoSituacao = wrapper.findAll('.headerCabecalho > p').at(2)
            expect(headerCabecalhoSituacao.text()).toBe('Estornado')
        })
    })
})
