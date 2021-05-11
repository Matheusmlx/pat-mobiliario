import rotulosPersonalizadosDefault from './rotulosPersonalizadosDefault'
import VueI18n from 'vue-i18n'

const rotulosPersonalizados = localStorage.getItem('rotulosPersonalizados') && JSON.parse(localStorage.getItem('rotulosPersonalizados')) || rotulosPersonalizadosDefault

window.$i18n = new VueI18n({locale: 'rotulosPersonalizados', messages: {rotulosPersonalizados}})

export default {
    rotulosPersonalizados
}
