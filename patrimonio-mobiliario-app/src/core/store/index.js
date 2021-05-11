import Vuex from 'vuex'
import comodato from './comodato'
import {actions, mutations, state} from './comum'
import concedente from './configuracao/concedente'
import contaContabil from './configuracao/conta-contabil'
import convenio from './configuracao/convenio'
import parametros from './configuracao/parametros'
import reconhecimento from './configuracao/reconhecimento'
import reservaGeral from './configuracao/reserva'
import intervaloGeral from './configuracao/reserva/intervalo'
import reservaGlobal from './configuracao/reserva/global'
import reservaPorOrgao from './configuracao/reserva/por-orgao'
import reservaIntervaloGlobal from './configuracao/reserva/global/intervalo'
import rotulosPersonalizados from './configuracao/rotulospersonalizados'
import documento from './documento'
import empenho from './empenho'
import incorporacao from './incorporacao'
import itemcatalogo from './itemcatalogo'
import itemIncorporacao from './itemincorporacao'
import movimentacaointerna from './movimentacao-interna'
import devolucaoAlmoxarifado from './movimentacao-interna/devolucao-almoxarifado'
import distribuicao from './movimentacao-interna/distribuicao'
import movimentacaoInternaDocumento from './movimentacao-interna/documento'
import entreEstoques from './movimentacao-interna/entre-estoques'
import definitiva from './movimentacao-interna/entre-setores'
import movimentacaoInternaItem from './movimentacao-interna/item'
import relatorio from './movimentacao-interna/relatorio'
import temporaria from './movimentacao-interna/temporaria'
import movimentacaoInternaVisualizacaoRegistro from './movimentacao-interna/visualizacao'
import movimentacaoInternaVisualizacaoRegistroItem from './movimentacao-interna/visualizacao/devolucao-item'
import notificacao from './notificacao'
import patrimonio from './patrimonio'
import responsavel from './responsavel'

import parametroState from '@/core/store/configuracao/parametros/state'

const reservaPatrimonialGlobal = parametroState.parametros.reservaPatrimonialGlobal

export default new Vuex.Store({
    actions,
    mutations,
    state,
    modules: {
        comodato,
        concedente,
        contaContabil,
        convenio,
        parametros,
        reconhecimento,
        reservaGeral,
        intervaloGeral,
        reserva: reservaPatrimonialGlobal ? reservaGlobal : reservaPorOrgao,
        intervalo: reservaIntervaloGlobal,
        rotulosPersonalizados,
        documento,
        empenho,
        incorporacao,
        itemcatalogo,
        itemIncorporacao,
        movimentacaointerna,
        devolucaoAlmoxarifado,
        distribuicao,
        movimentacaoInternaDocumento,
        entreEstoques,
        definitiva,
        movimentacaoInternaItem,
        relatorio,
        temporaria,
        movimentacaoInternaVisualizacaoRegistro,
        movimentacaoInternaVisualizacaoRegistroItem,
        notificacao,
        patrimonio,
        responsavel
    }
})
