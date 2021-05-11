import mutations from './mutations'
import getters from './getters'
import rotulosPersonalizadosDefault from './rotulosPersonalizadosDefault'

jest.mock('vue-i18n')

describe('mutations', () => {
    let windowSpy

    beforeEach(() => {
        windowSpy = jest.spyOn(global, 'window', 'get')
        windowSpy.mockImplementation(() => ({
            $i18n: {
                locale: 'rotulosPersonalizados',
                getLocaleMessage: () => rotulosPersonalizadosDefault.i18n
            }
        }))
    })

    const state = {
        rotulosPersonalizados: {}
    }

    it('deve setar um novo rotuloPersonalizado', () => {
        mutations.SET_ROTULOS_PERSONALIZADOS(state, rotulosPersonalizadosDefault.i18n)
        expect(getters.getRotulosPersonalizados()).toEqual(rotulosPersonalizadosDefault.i18n)
    })
})
