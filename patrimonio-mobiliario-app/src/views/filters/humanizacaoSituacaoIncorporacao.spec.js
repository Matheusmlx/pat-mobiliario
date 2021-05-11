import HumanizacaoSituacao from './humanizacaoSituacaoIncorporacao'

describe('texto-sem-valor', () => {
    it('Deve retornar finalizado', () => {
        const val = 'FINALIZADO'
       const valor = HumanizacaoSituacao(val)
        expect(valor).toEqual('Finalizado')
    })

    it('Deve retornar Em Elaboração', () => {
        const val = 'EM_ELABORACAO'
        const valor = HumanizacaoSituacao(val)
        expect(valor).toEqual('Em Elaboração')
    })

    it('Deve retornar valor de entrada', () => {
        const val = 'ATIVO'
        const valor = HumanizacaoSituacao(val)
        expect(valor).toEqual('ATIVO')
    })
})
