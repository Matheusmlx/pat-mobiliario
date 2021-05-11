import VueI18n from 'vue-i18n'

export default {
    SET_ROTULOS_PERSONALIZADOS: (state, rotulosPersonalizados) => {
        window.$i18n = new VueI18n({
            locale: 'rotulosPersonalizados',
            messages: {rotulosPersonalizados: rotulosPersonalizados}
        })
        localStorage.setItem('rotulosPersonalizados', JSON.stringify(rotulosPersonalizados))
        state.rotulosPersonalizados = rotulosPersonalizados
    }
}
