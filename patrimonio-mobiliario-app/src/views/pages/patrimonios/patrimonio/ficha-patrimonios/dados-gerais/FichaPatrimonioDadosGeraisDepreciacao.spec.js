import {shallowMount} from '@vue/test-utils'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import FichaPatrimonioDadosGeraisDepreciacao from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/dados-gerais/FichaPatrimonioDadosGeraisDepreciacao'
import valorParaDinheiro from '@/views/filters/valor-para-dinheiro'

describe('FichaPatrimonioDadosGeraisDepreciacao', () => {
    let wrapper, localVue, vuetify, router

    const patrimonio = {
        id:1,
        valorResidual: 123456.50,
        dadosDepreciacao: {
            metodo:'QUOTAS_CONSTANTES',
            vidaUtil:125,
            valorDepreciacaoMensal:123456.20,
            valorDepreciacaoAcumulado:654321.12
        }
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {current: { name: 'FichaPatrimonioDadosGerais', params:{patrimonioId: patrimonio.id}}}
        }
    })

    const defaultMount  = () => shallowMount(FichaPatrimonioDadosGeraisDepreciacao, {
        localVue,
        vuetify,
        router,
        propsData:{patrimonio: patrimonio},
        filters: {valorParaDinheiro}
    })

    describe('Computed', () => {
        it('deve mostrar valor residual formatado', async () => {
            wrapper = defaultMount()

            const valorResidual = wrapper.findAll('td').at(5)
            expect(valorResidual.text()).toBe('R$ 123.456,50')
        })

        it('Deve mostrar os Dados de depreciação formatados', async () => {
            wrapper = defaultMount()

            const metodo = wrapper.findAll('td').at(0)
            const vidaUtil = wrapper.findAll('td').at(1)
            const valorAmortizadoMensal = wrapper.findAll('td').at(2)
            const valorAmortizadoAcumulado = wrapper.findAll('td').at(3)
            const valorLiquido = wrapper.findAll('td').at(4)

            expect(metodo.text()).toBe('Quotas Constantes')
            expect(vidaUtil.text()).toBe('125')
            expect(valorAmortizadoMensal.text()).toBe('R$ 123.456,20')
            expect(valorAmortizadoAcumulado.text()).toBe('R$ 654.321,12')
            expect(valorLiquido.text()).toBe('-')
        })

    })

    describe('Methods', () => {
        it('deve redirecionar para modal de detalhes das depreciações', async () => {
            wrapper = defaultMount()

            wrapper.vm.abrirModal()

            expect(router.push).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'ModalTabelaDepreciacoes',params:{patrimonioId: patrimonio.id}})
        })
    })
})
