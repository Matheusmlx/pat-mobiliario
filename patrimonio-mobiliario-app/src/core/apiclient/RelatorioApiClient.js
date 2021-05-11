import axios from 'axios'

class RelatorioApiClient {
    async baixarTermoDeResponsabilidade(idMovimentacao) {
        const url = `api/movimentacao/${idMovimentacao}/termo-guarda-responsabilidade`
        return axios.get(url,{responseType:'arraybuffer'})
    }

    async baixarMemorandoMovimentacaoEmElaboracao(idMovimentacao) {
        const url = `api/movimentacao/${idMovimentacao}/memorando/em-elaboracao`
        return axios.get(url,{responseType:'arraybuffer'})
    }

    async baixarMemorandoMovimentacaoFinalizada(idMovimentacao) {
        const url = `api/movimentacao/${idMovimentacao}/memorando/finalizado`
        return axios.get(url,{responseType:'arraybuffer'})
    }
}

export default new RelatorioApiClient()
