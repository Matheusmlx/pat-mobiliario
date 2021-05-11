import {mutationTypes} from '@/core/constants'

export default {
    [mutationTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.REMOVER_DOCUMENTOS](state, documento) {
        let posicao = state.documentos.indexOf(documento, state.documentos[0])
        state.documentos.splice(posicao, 1)
    },

    [mutationTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.SET_DOCUMENTOS](state, documentos) {
        state.documentos = documentos
    },
}
