import mutations from './mutations'
import { mutationTypes } from '@/core/constants'

describe('Mutations', () => {

    it('Deve chamar a mutation PATRIMONIO.INCORPORACAO.DOCUMENTO.SET_DOCUMENTOS e setar o documento', () => {
        const state = {
            documentos: [],
        }
        const documento = {}
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.SET_DOCUMENTOS](state, documento)
        expect(state.documentos).toEqual({})
    })

    it('Deve chamar a mutation PATRIMONIO.INCORPORACAO.DOCUMENTO.REMOVER_DOCUMENTOS', () => {
        const state = {
            documentos: [],
        }
        const documento = {}
        mutations[mutationTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.REMOVER_DOCUMENTOS](state, documento)
        expect(state.documentos).toEqual([])
    })
})
