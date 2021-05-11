import { actionTypes } from '@/core/constants'
import api from '@/core/apiclient'

export default {
    async [actionTypes.CONFIGURACAO.RESERVA.BUSCAR_TODAS_RESERVAS_LISTAGEM]({ state }) {
        const {filtros,paginacao} = state.resultadoBuscaTodasReservasListagem
        const { data } = await api.reservaGeral.buscarTodos(filtros, paginacao)

        return data
    }
}
