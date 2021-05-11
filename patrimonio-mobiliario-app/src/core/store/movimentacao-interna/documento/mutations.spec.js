import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

let state

describe('Mutations', () => {

    beforeEach(() => {
        state = {
            documentos: [
                {id: 5, movimentacao: 8},
                {id: 7, movimentacao: 3}
            ]
        }
    })

    it ('deve chamar mutation MOVIMENTACAO_INTERNA.DOCUMENTO.REMOVER_DOCUMENTO', () => {
        const documento = state.documentos[1]

        mutations[mutationTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.REMOVER_DOCUMENTO_MOVIMENTACAO_INTERNA](state, documento)

        expect(state.documentos.length).toBe(1)
        expect(state.documentos).toEqual([{id: 5, movimentacao: 8}])
    })

    it ('deve chamar mutation MOVIMENTACAO_INTERNA.DOCUMENTO.SET_DOCUMENTOS', () => {
        const documentos = [
            {id: 12, movimentacao: 25},
            {id: 7, movimentacao: 3}
        ]

        expect(state.documentos).toEqual([
            {id: 5, movimentacao: 8},
            {id: 7, movimentacao: 3}
        ])

        mutations[mutationTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.SET_DOCUMENTOS_MOVIMENTACAO_INTERNA](state, documentos)

        expect(state.documentos).toEqual(documentos)
    })

})
