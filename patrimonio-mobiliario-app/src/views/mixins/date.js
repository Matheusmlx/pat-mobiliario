import {formatosDefault} from '@/core/constants'

export default {
    methods: {
        humanizarFormatoDataHora() {
            return formatosDefault.DATA_HORA_MINUTO
        },
        humanizarFormatoData() {
            return formatosDefault.DATA
        },
        humanizarFormatoHora() {
            return formatosDefault.HORA
        }
    }
}
