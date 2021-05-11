import date from './date'

describe('date', () => {
    it('Humaniza Formato DataHora', () => {
        const valor = date.methods.humanizarFormatoDataHora()
        expect(valor).toEqual('DD/MM/YYYY [-] HH[h] mm[min]')
    })

    it('Humaniza Formato Data', () => {
        const valor = date.methods.humanizarFormatoData()
        expect(valor).toEqual('DD/MM/YYYY')
    })

    it('Humaniza Formato Hora', () => {
        const valor = date.methods.humanizarFormatoHora()
        expect(valor).toEqual('HH[h] mm[min]')
    })
})
