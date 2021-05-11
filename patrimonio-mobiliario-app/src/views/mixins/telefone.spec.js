import telefone from './telefone'

describe('date', () => {
    it('Deve remover máscara do telefone', () => {
        const valor = telefone.methods.removerMascaraTelefone('(67) 99941-3141')
        expect(valor).toEqual('67999413141')
    })

    it('Deve retornar vazio', () => {
        const valor = telefone.methods.removerMascaraTelefone('')
        expect(valor).toEqual('')
    })
})
