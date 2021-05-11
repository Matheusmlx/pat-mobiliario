import TextoSemValor from './texto-sem-valor'

describe('texto-sem-valor', () => {
    it('Deve retornar texto sem valor', () => {
        const val = ''
       const valor = TextoSemValor(val)
        expect(valor).toEqual('-------')
    })

    it('Deve retornar valor', () => {
        const val = '0123'
        const valor = TextoSemValor(val)
        expect(valor).toEqual('0123')
    })
})
