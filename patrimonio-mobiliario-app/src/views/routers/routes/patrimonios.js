import Patrimonios from '@/views/pages/patrimonios/Patrimonios'
import incorporacao from './incorporacoes'
import PatrimoniosListagem from '@/views/pages/patrimonios/patrimonio/listagem/PatrimoniosListagem'

export default [
    {
        path: '/patrimonio',
        name: 'patrimonio',
        component: Patrimonios,
        meta: {
            menu: {
                title: 'Patrimônios',
                icon: 'fas fa-clipboard-check'
            },
        },
        children:[
            ...incorporacao,
            {
                path: '/patrimonios',
                name: 'PatrimoniosListagem',
                component: PatrimoniosListagem,
                meta: {
                    menu: {
                        title: 'Patrimônios'
                    },
                    page: {
                        title: 'Patrimônios',
                        subtitle: 'Lista geral de patrimônios'
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Normal','Mobiliario.Consulta']
                }
            },
        ]
    }
]
