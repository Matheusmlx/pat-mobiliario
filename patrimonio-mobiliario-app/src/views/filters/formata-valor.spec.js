import formataValor from './formata-valor'

describe('formata-valor', () => {

    it('Deve retornar texto sem valor', () => {
        const val = ''
        const valor = formataValor(val)
        expect(valor).toEqual(' - ')
    })

    it('Deve retornar valor 5', () => {
        const val = 5
        const valor = formataValor(val)
        expect(valor).toEqual('5')
    })

    it('Deve retornar valor 10', () => {
        const val = 10
        const valor = formataValor(val)
        expect(valor).toEqual('10')
    })

    it('Deve retornar valor 100', () => {
        const val = 100
        const valor = formataValor(val)
        expect(valor).toEqual('100')
    })

    it('Deve retornar valor 1000', () => {
        const val = 1000
        const valor = formataValor(val)
        expect(valor).toEqual('1.000')
    })

    it('Deve retornar valor 10.000', () => {
        const val = 10000
        const valor = formataValor(val)
        expect(valor).toEqual('10.000')
    })

    it('Deve retornar valor 100.000', () => {
        const val = 100000
        const valor = formataValor(val)
        expect(valor).toEqual('100.000')
    })

    it('Deve retornar valor 1.000.000', () => {
        const val = 1000000
        const valor = formataValor(val)
        expect(valor).toEqual('1.000.000')
    })

})
