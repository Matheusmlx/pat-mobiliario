import alert from './alert'
import constant from './constant'
import date from './date'
import loading from './loading'
import telefone from './telefone'
import file from './file'
import colunas from './colunas'
import verificarMudancasPropriedades from './verificarMudancasPropriedades'

export default {
    install(Vue) {
        Vue.mixin(file)
        Vue.mixin(alert)
        Vue.mixin(constant)
        Vue.mixin(date)
        Vue.mixin(loading)
        Vue.mixin(telefone)
        Vue.mixin(colunas)
        Vue.mixin(verificarMudancasPropriedades)
    }
}
