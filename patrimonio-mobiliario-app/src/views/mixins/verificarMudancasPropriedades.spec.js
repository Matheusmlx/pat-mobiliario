import verificarMudancasPropriedades from './verificarMudancasPropriedades'
import {shallowMount} from '@vue/test-utils'

describe('verificarMudancasPropriedades', () => {

    let wrapper, mockComponente

    beforeEach(() => {
        mockComponente = {
            render: jest.fn(),
            mixins: [verificarMudancasPropriedades]
        }
        wrapper = shallowMount(mockComponente)
    })

    it('Deve realizar modificações quando a propriedade do objeto antigo possuir um objeto', () => {
        const novoObjeto = {
            comodante: {id: 1, nome: 'Amanda'},
            id: 7,
            setor: {id: 8774},
            situacao: 'EM_ELABORACAO',
            valorNota: null
        }

        const objetoAntigo = {
            comodante: {id: 2, nome: 'João'},
            id: 7,
            setor: {id: 8774},
            situacao: 'EM_ELABORACAO',
            valorNota: null
        }

        const objetoAtual = {
            comodante: 1,
            id: 7,
            setor: {id: 8774},
            situacao: 'EM_ELABORACAO',
            valorNota: null
        }

        wrapper.vm.verificarMudancasPropriedades(novoObjeto, objetoAntigo, objetoAtual)

        expect(objetoAntigo).toEqual(novoObjeto)
        expect(objetoAtual).toEqual(novoObjeto)
    })

    it('Deve realizar modificações quando a propriedade do objeto antigo não possuir um objeto', () => {
        const novoObjeto = {
            comodante: {id: 1, nome: 'Amanda'},
            id: 7,
            setor: {id: 8774},
            situacao: 'EM_ELABORACAO',
            valorNota: null
        }

        const objetoAntigo = {
            comodante: {id: 1, nome: 'Amanda'},
            id: 7,
            setor: 8773,
            situacao: 'EM_ELABORACAO',
            valorNota: null
        }

        const objetoAtual = {
            comodante: {id: 1, nome: 'Amanda'},
            id: 7,
            setor: 8773,
            situacao: 'EM_ELABORACAO',
            valorNota: null
        }

        wrapper.vm.verificarMudancasPropriedades(novoObjeto, objetoAntigo, objetoAtual)

        expect(objetoAntigo).toEqual(novoObjeto)
        expect(objetoAtual).toEqual(novoObjeto)
    })

    it('Deve realizar modificações quando a propriedade do objeto antigo e novo forem valores simples', () => {
        const novoObjeto = {
            comodante: {id: 1, nome: 'Amanda'},
            id: 7,
            setor: {id: 8774},
            situacao: 'EM_ELABORACAO',
            valorNota: 123456.00
        }

        const objetoAntigo = {
            comodante: {id: 1, nome: 'Amanda'},
            id: 7,
            setor: {id: 8774},
            situacao: 'EM_ELABORACAO',
            valorNota: 12345
        }

        const objetoAtual = {
            comodante: {id: 1, nome: 'Amanda'},
            id: 7,
            setor: {id: 8774},
            situacao: 'EM_ELABORACAO',
            valorNota: 123456
        }

        wrapper.vm.verificarMudancasPropriedades(novoObjeto, objetoAntigo, objetoAtual)

        expect(objetoAntigo).toEqual(novoObjeto)
        expect(objetoAtual).toEqual(novoObjeto)
    })

})
