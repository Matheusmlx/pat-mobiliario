import actionTypes from '@/core/constants/actionTypes'
import api from '@/core/apiclient'
const PDF = 'application/pdf'

export default {
    async [actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_TERMO_DE_RESPONSABILDADE]({ commit }, idMovimentacao) {
        const {data} = await api.relatorio.baixarTermoDeResponsabilidade(idMovimentacao)
        let blob = new Blob([data],{
            type: PDF
        })
        const downloadLink = document.createElement('a')
        document.body.appendChild(downloadLink)
        downloadLink.setAttribute('type', 'hidden')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = 'termo-responsabilidade.pdf'
        downloadLink.click()
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_EM_ELABORACAO]({ commit }, idMovimentacao) {
        const {data} = await api.relatorio.baixarMemorandoMovimentacaoEmElaboracao(idMovimentacao)
        let blob = new Blob([data],{
            type: PDF
        })
        const downloadLink = document.createElement('a')
        document.body.appendChild(downloadLink)
        downloadLink.setAttribute('type', 'hidden')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = 'memorando-movimentacao-em-elaboracao.pdf'
        downloadLink.click()
    },

    async [actionTypes.MOVIMENTACAO_INTERNA.RELATORIO.GERAR_MEMORANDO_MOVIMENTACAO_FINALIZADA]({ commit }, idMovimentacao) {
        const {data} = await api.relatorio.baixarMemorandoMovimentacaoFinalizada(idMovimentacao)
        let blob = new Blob([data], {
            type: PDF
        })
        const downloadLink = document.createElement('a')
        document.body.appendChild(downloadLink)
        downloadLink.setAttribute('type', 'hidden')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = 'memorando-movimentacao-finalizada.pdf'
        downloadLink.click()
    }
}
