import Vue from 'vue'
import Vuetify from 'vuetify/lib'
import pt from 'vuetify/es5/locale/pt'

Vue.use(Vuetify)

export default new Vuetify({
    icons: {
        iconfont: 'md',
    },
    lang: {
        locales: { pt },
        current: 'pt'
    },
    theme: {
        themes: {
            light: {
                primary: '#487b9c',
                secondary: '#487b9c'
            }
        }
    },
    options: {
        customProperties: true
    }
})
