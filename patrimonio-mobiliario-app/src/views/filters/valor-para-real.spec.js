import ValorParaReal from './valor-para-real'

describe('valor-para-real', () => {

    it('Deve retornar texto sem valor', () => {
        const val = ''
        const valor = ValorParaReal(val)
        expect(valor).toEqual('-')
    })

    it('Deve retornar a string caso não seja númerico', () => {
        const val = 'texto'
        const valor = ValorParaReal(val)
        expect(valor).toEqual('texto')
    })

    it('Deve retornar valor', () => {
        const val = 5.4
        const valor = ValorParaReal(val)
        expect(valor).toEqual('R$ 5,40')
    })

    it('Deve retornar valor com pontos e virgula', () => {
        const val = 11111.11
        const valor = ValorParaReal(val)
        expect(valor).toContain('R$ 11.111,11')
    })
})
