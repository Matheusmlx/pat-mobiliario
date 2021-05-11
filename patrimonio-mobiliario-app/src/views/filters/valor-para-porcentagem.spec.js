import ValorParaPorcentagem from './valor-para-porcentagem'

describe('valor-para-porcentagem', () => {

  it('Deve retornar texto sem valor', () => {
    const val = ''
    const valor = ValorParaPorcentagem(val)
    expect(valor).toEqual('-')
  })

  it('Deve retornar texto sem valor (null)', () => {
    const val = null
    const valor = ValorParaPorcentagem(val)
    expect(valor).toEqual('-')
  })

  it('Deve retornar texto sem valor (undefined)', () => {
    const valor = ValorParaPorcentagem()
    expect(valor).toEqual('-')
  })

  it('Deve retornar texto sem valor (zero)', () => {
    const val = 0
    const valor = ValorParaPorcentagem(val)
    expect(valor).toEqual('-')
  })

  it('Deve retornar porcentagem', () => {
    const val = 1
    const valor = ValorParaPorcentagem(val)
    expect(valor).toEqual('1,00%')
  })

  it('Deve retornar porcentagem para valor com virgula', () => {
    const val = 5.4
    const valor = ValorParaPorcentagem(val)
    expect(valor).toEqual('5,40%')
  })
})
