import mutationTypes from '@/core/constants/mutationTypes'

export default {
    [mutationTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.REMOVER_DOCUMENTO_MOVIMENTACAO_INTERNA](state, documento) {
        const index = state.documentos.indexOf(documento)

        if (index !== -1) {
            state.documentos.splice(index, 1)
        }
    },

    [mutationTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.SET_DOCUMENTOS_MOVIMENTACAO_INTERNA](state, documentos) {
        state.documentos = documentos
    }
}
