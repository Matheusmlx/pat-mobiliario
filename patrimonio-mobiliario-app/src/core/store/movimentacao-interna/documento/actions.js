import api from '@/core/apiclient'
import actionTypes from '@/core/constants/actionTypes'
import mutationTypes from '@/core/constants/mutationTypes'

export default {
    async [actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.CADASTRAR_DOCUMENTO_MOVIMENTACAO_INTERNA]({commit}, documento) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const {data} = await api.movimentacaoInternaDocumento.cadastrarDocumento(documento)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.ATUALIZAR_DOCUMENTO_MOVIMENTACAO_INTERNA]({commit}, documento) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const {data} = await api.movimentacaoInternaDocumento.atualizarDocumento(documento)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.DELETAR_DOCUMENTO_MOVIMENTACAO_INTERNA]({commit}, documento) {
        await api.movimentacaoInternaDocumento.deletarDocumento(documento)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.REMOVER_DOCUMENTO_MOVIMENTACAO_INTERNA, documento)
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO_MOVIMENTACAO_INTERNA]() {
        const { data } = await api.incorporacao.buscarTipoDocumento()
        return data.items
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.BUSCAR_DOCUMENTOS_MOVIMENTACAO_INTERNA]({ commit }, id) {
        const { data } = await api.movimentacaoInternaDocumento.buscarDocumentos(id)
        commit(mutationTypes.MOVIMENTACAO_INTERNA.DOCUMENTO.SET_DOCUMENTOS_MOVIMENTACAO_INTERNA, data.items)
        return data.items
    },

}
