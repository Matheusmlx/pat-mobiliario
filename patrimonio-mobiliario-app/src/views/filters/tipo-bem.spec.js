import tipoBem from './tipo-bem'

describe('tipo-bem', () => {

    it('Deve retornar tipo Veículo', () => {
        const val = 'VEICULO'
        const valor = tipoBem(val)
        expect(valor).toEqual('Veículo')
    })

    it('Deve retornar tipo Armamento', () => {
        const val = 'ARMAMENTO'
        const valor = tipoBem(val)
        expect(valor).toEqual('Armamento')
    })

    it('Deve retornar tipo Móvel', () => {
        const val = 'MOVEL'
        const valor = tipoBem(val)
        expect(valor).toEqual('Móvel')
    })

    it('Deve retornar tipo Equipamento', () => {
        const val = 'EQUIPAMENTO'
        const valor = tipoBem(val)
        expect(valor).toEqual('Equipamento')
    })

    it('Deve retornar tipo vazio', () => {
        const val = ''
        const valor = tipoBem(val)
        expect(valor).toEqual('')
    })

})
