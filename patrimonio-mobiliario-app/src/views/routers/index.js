import Router from 'vue-router'
import patrimonio from './routes/patrimonios'
import relatorios from './routes/relatorios'
import configuracoes from './routes/configuracoes'
import comum from './routes/comum'
import fichapatrimonio from './routes/fichapatrimonio'
import movimentacaointerna from './routes/movimentacaointerna'

const index = new Router({
    routes: [
        {
            path: '/',
            name: 'Inicial',
            redirect: () => {
                return { name: 'IncorporacaoListagem' }
            },
        },
        ...fichapatrimonio,
        ...patrimonio,
        ...movimentacaointerna,
        ...relatorios,
        ...configuracoes,
        ...comum
    ],
    scrollBehavior: (to, from, savedPosition) => {
        if (to.meta && to.meta.scrollTop === false) {
            return
        }

        if (from.meta && from.meta.scrollTopAfterLeave === false) {
            return
        }

        if (to.hash) {
            return {selector: to.hash, behavior: 'smooth'}
        } else if (savedPosition) {
            return {x: 0, y: savedPosition.y, behavior: 'smooth'}
        } else {
            return {x: 0, y: 0, behavior: 'smooth'}
        }
    },
})

export default index
