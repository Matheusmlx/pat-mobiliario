import {actionTypes, mutationTypes} from '@/core/constants'
import api from '@/core/apiclient'

export default {

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_INTERVALO](context, reserva) {
        const {data} = await api.reservaIntervalo.buscarIntervalo(reserva) || {data: undefined}
        return data
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SALVAR_INTERVALO](context, intervalo) {
        const {data} = await api.reservaIntervalo.salvarIntervalo(intervalo)
        return data
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM]({state, commit}, reservaId) {
        const {paginacao} = state.resultadoBuscaTodosIntervalosListagem
        const {data} = await api.reservaIntervalo.buscarTodos(reservaId, paginacao)
        commit(mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_RESULTADO_BUSCA_TODOS_INTERVALOS, data)
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.REMOVER_INTERVALO_POR_ID](context, {reservaId, intervaloId}) {
        await api.reservaIntervalo.excluirIntervalo(reservaId, intervaloId)
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO]({commit}, reservaId) {
        const {data} = await api.reservaIntervalo.existeEmElaboracao(reservaId)
        commit(mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_EXISTE_EM_ELABORACAO, data.existe)
        return data.existe
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_PROXIMO_NUMERO](context, {reservaId, maiorNumeroFimIntervalo}) {
        const {data} = await api.reservaIntervalo.buscarProximoNumero(reservaId, maiorNumeroFimIntervalo)
        return data
    },

    async [actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.VALIDAR_INTERVALO](context, {reservaId, numeroInicio, numeroFim} ) {
        const {data} = await api.reservaIntervalo.validarIntervalo(reservaId, numeroInicio, numeroFim)
        return data
    }
}
