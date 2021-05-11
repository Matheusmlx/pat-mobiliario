import textoSemValor from './texto-sem-valor'
import textoSemValorSimples from './texto-sem-valor-simples'
import valorParaDinheiro from './valor-para-dinheiro'
import tipoBem from './tipo-bem'
import humanizacaoSituacaoIncorporacao from './humanizacaoSituacaoIncorporacao'
import valorParaReal from './valor-para-real'
import valorParaPorcentagem from './valor-para-porcentagem'
import dataParaMesAno from './data-para-mes-ano'
import formataValor from './formata-valor'

export default {
    install(Vue) {
        Vue.filter('textoSemValor', textoSemValor)
        Vue.filter('textoSemValorSimples', textoSemValorSimples)
        Vue.filter('valorParaDinheiro', valorParaDinheiro)
        Vue.filter('tipoBem', tipoBem)
        Vue.filter('humanizacaoSituacaoIncorporacao',humanizacaoSituacaoIncorporacao)
        Vue.filter('valorParaReal', valorParaReal)
        Vue.filter('valorParaPorcentagem', valorParaPorcentagem)
        Vue.filter('dataParaMesAno', dataParaMesAno)
        Vue.filter('formataValor', formataValor)
    }
}
