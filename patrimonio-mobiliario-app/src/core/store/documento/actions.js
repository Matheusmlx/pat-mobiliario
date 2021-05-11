import api from '@/core/apiclient'
import {actionTypes, mutationTypes} from '@/core/constants'

export default {

    async [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.CADASTRAR_DOCUMENTO]({ commit }, documento) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const { data } = await api.incorporacao.cadastrarDocumento(documento)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_DOCUMENTOS]({ commit }, id) {
        const { data } = await api.incorporacao.buscarDocumentos(id)
        commit(mutationTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.SET_DOCUMENTOS, data.items)
        return data.items
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.BUSCAR_TIPO_DOCUMENTO]() {
        const { data } = await api.incorporacao.buscarTipoDocumento()
        return data.items
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.ATUALIZAR_DOCUMENTO]({ commit }, documento) {
        commit(mutationTypes.LOKI.ENABLE_AUTO_SAVING)
        const { data } = await api.incorporacao.atualizarDocumento(documento)
        commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
        return data
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_UPLOAD](context, documento) {
        let formData = new FormData()
        formData.append('file', documento.uriAnexo)
        const { data } = await api.incorporacao.uploadAnexoDocumento(formData)
        return data.uri
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DOCUMENTO_DOWNLOAD](context, anexo) {
        const { data } = await api.incorporacao.downloadAnexo(anexo)
        let blob = new Blob([data], { type: ['img/pdf', 'img/jpeg', 'img/png'] })
        const downloadLink = document.createElement('a')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = 'Relatorio_de_Protocolos.pdf'
        downloadLink.click()
    },

    async [actionTypes.PATRIMONIO.INCORPORACAO.DOCUMENTO.DELETAR_DOCUMENTO]({ state }, documento) {
        state.documentoBackup = documento
        await api.incorporacao.deletarDocumento(documento)
        const posicao = state.documentos.indexOf(documento, state.documentos[0])
        state.documentos.splice(posicao, 1)
    },

}
