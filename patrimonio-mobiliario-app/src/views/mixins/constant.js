import {
    categoriaItemIncorporacao,
    combustivelItemIncorporacao,
    exceptions,
    formatosDefault,
    fusoHorario,
    mensagens,
    mesesLicenciamento,
    metodosContaContabil,
    notificacoesDefault,
    numeracaoPatrimonial,
    preenchimentoReserva,
    produto,
    situacoesConcedente,
    situacoesConvenio,
    situacoesIncorporacao,
    situacoesItemIncorporacao,
    situacoesMovimentacaoInterna,
    situacoesReconhecimento,
    situacoesReservas,
    tipoBem,
    tipoContaContabil,
    tipoMovimentacoes,
    tipoReserva
} from '@/core/constants'

export default {
    data() {
        return {
            categoriaItemIncorporacao,
            combustivelItemIncorporacao,
            exceptions,
            formatosDefault,
            fusoHorario,
            mensagens,
            mesesLicenciamento,
            metodosContaContabil,
            notificacoesDefault,
            numeracaoPatrimonial,
            preenchimentoReserva,
            produto,
            situacoesConcedente,
            situacoesConvenio,
            situacoesIncorporacao,
            situacoesItemIncorporacao,
            situacoesMovimentacaoInterna,
            situacoesReconhecimento,
            situacoesReservas,
            tipoBem,
            tipoContaContabil,
            tipoMovimentacoes,
            tipoReserva
        }
    },
    methods: {
        formatarLista(constant) {
            const list = Object.getOwnPropertyNames(constant).sort()
            if (list.indexOf('__ob__') >= 0) {
                list.splice(list.indexOf('__ob__'), 1)
            }
            return list
        }
    }
}
