import axios from 'axios'

class EmpenhoApiClient {

    async buscarEmpenhos(incorporacaoId) {
        return axios.get(`api/patrimonios/incorporacao/${incorporacaoId}/empenhos`)
    }

    async salvar(empenho) {
        return axios.post('api/patrimonios/incorporacao/empenhos', empenho)
    }

    async atualizar(empenho) {
        return axios.put(`api/patrimonios/incorporacao/empenhos/${empenho.id}`, empenho)
    }

    async remover(empenhoId) {
        axios.delete(`api/patrimonios/incorporacao/empenhos/${empenhoId}`)
    }

}

export default new EmpenhoApiClient()
