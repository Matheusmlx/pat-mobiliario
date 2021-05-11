import {actionTypes} from '@/core/constants'
import api from '@/core/apiclient'
const PDF = 'application/pdf'

export default {

    async [actionTypes.CONFIGURACAO.RESERVA.INTERVALO.BAIXAR_TERMO_DE_GUARDA](context, {reservaId, reservaIntervaloId}) {
        const { data } = await api.reservaIntervaloGeral.baixarTermoDeGuarda(reservaId, reservaIntervaloId)
        let blob = new Blob([data],{ type: PDF })
        const downloadLink = document.createElement('a')
        downloadLink.href = window.URL.createObjectURL(blob)
        downloadLink.download = 'Termo_de_guarda_e_responsabilidade.pdf'
        downloadLink.click()
    }
}
