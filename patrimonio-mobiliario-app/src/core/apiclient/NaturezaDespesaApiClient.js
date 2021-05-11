import axios from 'axios'

class NaturezaDespesaApiClient {

    async buscarNaturezasDespesa(codigoItem) {
        const params = new URLSearchParams({itemCatalogoCodigo: codigoItem}).toString()
        const url = 'api/natureza-despesa?' + params
        return axios.get(url)
    }

}

export default new NaturezaDespesaApiClient()
