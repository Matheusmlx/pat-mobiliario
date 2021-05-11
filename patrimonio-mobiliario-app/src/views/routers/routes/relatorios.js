import Relatorio from '@/views/pages/relatorio/Relatorio'
import Inventario from '@/views/pages/relatorio/inventario/RelatorioInventario.vue'

export default [
    {
        path: '/relatorio',
        name: 'Relatorios',
        component: Relatorio,
        meta:{
            menu:{
                title:'Relatórios ',
                icon: 'fas fa-file-download'
            }
        },
        children: [
            {
                path: '/inventario',
                name: 'Inventario',
                component:Inventario,
                meta:{
                    menu:{
                        title: 'Inventário'
                    },
                    page:{
                        title: 'Relatórios',
                        subtitle: 'Inventário'
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Normal','Mobiliario.Consulta']
                }
            }
        ]
    }

]
