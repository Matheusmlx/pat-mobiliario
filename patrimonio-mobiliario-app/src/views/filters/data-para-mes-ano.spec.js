import DataParaMesAno from './data-para-mes-ano'

describe('data-para-mes-ano', () => {

  it('Deve retornar texto sem valor', () => {
    const val = ''
    const valor = DataParaMesAno(val)
    expect(valor).toEqual('-')
  })

  it('Deve retornar texto sem valor (null)', () => {
    const val = null
    const valor = DataParaMesAno(val)
    expect(valor).toEqual('-')
  })

  it('Deve retornar texto sem valor (undefined)', () => {
    const valor = DataParaMesAno()
    expect(valor).toEqual('-')
  })

  it('Deve retornar mês/ano', () => {
    const val = '2020-02-10T00:00:00.000-0400'
    const valor = DataParaMesAno(val)
    expect(valor).toEqual('02/2020')
  })
})
