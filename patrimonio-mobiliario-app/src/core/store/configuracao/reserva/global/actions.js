import { actionTypes } from '@/core/constants'
import api from '@/core/apiclient'

export default {

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.SALVAR_RESERVA](context, reserva) {
        const {data} = await api.reserva.salvarReserva(reserva)
        return data
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.EDITAR_RESERVA](context, reserva) {
        const {data} = await api.reserva.editarReserva(reserva) || {}
        return data
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID](context, reservaId) {
        const {data} = await api.reserva.buscarReservaPorId(reservaId)
        return data
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_PROXIMO_NUMERO_DISPONIVEL]() {
        const {data} = await api.reserva.buscarProximoNumeroDisponivel()
        return data
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_TODOS_ORGAOS_PAGINADO]({state}) {
        const {filtros, paginacao} = state.todosOrgaos
        const {data} = await api.unidadeOrganizacional.buscarTodosOrgaosPaginado(filtros, paginacao)
        return data
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA](context, reservaId) {
        const response = await api.reserva.finalizarReserva(reservaId)
        return response.status === 200
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.VERIFICAR_RESERVA_POSSUI_NUMERO_UTILIZADO](context, idReserva) {
        const {data} = await api.reserva.buscarNumeroReservaUtilizado(idReserva)
        return data
    },

    async[actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA](contex, idReserva) {
        await api.reserva.deletarReserva(idReserva)
    }
}
