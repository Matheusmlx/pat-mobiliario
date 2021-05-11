import mutations from './mutations'

describe('mutations', () => {

    const state = {
        parametros: {}
    }

    const parametros = {
        urlChatSuporte: 'http://urlChatSuporte.com.br',
        quantidadeDigitosNumeroPatrimonio: '10',
        reservaPatrimonialGlobal: true
    }

    it('deve setar um novo rotuloPersonalizado', () => {
        mutations.SET_PARAMETROS(state, parametros)
        expect(state.parametros).toEqual(parametros)
    })
})
