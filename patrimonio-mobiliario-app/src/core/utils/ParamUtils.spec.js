import ParamUtils from './ParamUtils'

describe('ParamUtils', () => {

    it('Deve converter um objeto para uma string de parametros', () => {
        const obj = {
            'chave1': 'valor1',
            'chave2': 'valor2'
        }
        expect(ParamUtils.ObjectToParams(obj))
            .toBe('chave1=valor1&chave2=valor2')
    })

    it('Deve converter um objeto para uma string de parametros com descending', () => {
        const obj = {
            'descending': true,
            'chave2': 'valor2'
        }
        expect(ParamUtils.ObjectToParams(obj))
            .toBe('sort=DESC&chave2=valor2')
    })

    it('Deve converter um array de objetos para uma string de parametros', () => {
        const obj = {
            'chave1': 'valor1',
            'chave2': 'valor2'
        }

        const pagination = {
            'size': 25,
            'currentPage': 2
        }
        expect(ParamUtils.ObjectsToParams([obj, pagination]))
            .toBe('chave1=valor1&chave2=valor2&size=25&currentPage=2')
    })

    it('Deve converter um objeto que contém array de strings para uma string de parametros', () => {
        const obj = {
            'chave1': 'valor1',
            'chave2': 'valor2',
            'chave3': ['valor3a', 'valor3b']
        }
        expect(ParamUtils.ObjectToParams(obj))
            .toBe('chave1=valor1&chave2=valor2&chave3=valor3a&chave3=valor3b')
    })
})
