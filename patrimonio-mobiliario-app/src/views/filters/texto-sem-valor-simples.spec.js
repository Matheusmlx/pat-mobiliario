import TextoSemValorSimples from './texto-sem-valor-simples'

describe('texto-sem-valor', () => {
    it('Deve retornar texto sem valor', () => {
        const val = ''
       const valor = TextoSemValorSimples(val)
        expect(valor).toEqual('-')
    })

    it('Deve retornar valor', () => {
        const val = '0123'
        const valor = TextoSemValorSimples(val)
        expect(valor).toEqual('0123')
    })
})
