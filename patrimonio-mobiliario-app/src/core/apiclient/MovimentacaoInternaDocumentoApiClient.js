import axios from 'axios'

class MovimentacaoInternaDocumentoApiClient {

    async cadastrarDocumento(documento) {
        return axios.post(`api/movimentacoes/internas/${documento.movimentacao}/documentos`, documento)
    }

    async atualizarDocumento(documento) {
        return axios.put(`api/movimentacoes/internas/${documento.movimentacao}/documentos/${documento.id}`, documento)
    }

    async deletarDocumento(documento) {
        return axios.delete(`api/movimentacoes/internas/${documento.movimentacao}/documentos/${documento.id}`, documento)
    }

    async buscarDocumentos(movimentacaoId) {
        return axios.get(`api/movimentacoes/internas/${movimentacaoId}/documentos`)
    }

}

export default new MovimentacaoInternaDocumentoApiClient()
