import FichaPatrimonio from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/FichaPatrimonio'
import FichaPatrimonioDadosGerais from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/dados-gerais/FichaPatrimonioDadosGerais'
import FichaPatrimonioMovimentacoes from '@/views/pages/patrimonios/patrimonio/ficha-patrimonios/movimentacoes/FichaPatrimonioMovimentacoes'
import FichaPatrimonioVisualizar from '@/views/pages/patrimonios/patrimonio/visualizacao/ficha-patrimonios/FichaPatrimonioVisualizar'
import FichaPatrimonioDadosGeraisVisualizar from '@/views/pages/patrimonios/patrimonio/visualizacao/ficha-patrimonios/dados-gerais/FichaPatrimonioDadosGeraisVisualizar'
import ModalTabelaDepreciacoes from '@/views/components/modal/patrimonio/ficha-patrimonios/dados-gerais/ModalTabelaDepreciacoes'

export default [
    {
        path: '/patrimonio/:patrimonioId/',
        name: 'FichaPatrimonio',
        component: FichaPatrimonio,
        meta: {
            page: {
                title: 'Patrimônios',
                subtitle: 'Ficha individual do patrimônio'
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Normal']
        },
        children: [
            {
                path: 'ficha',
                name: 'FichaPatrimonioDadosGerais',
                component: FichaPatrimonioDadosGerais,
                meta: {
                    page: {
                        title: 'Patrimônios',
                        subtitle: 'Ficha individual do patrimônio'
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Normal']
                },
                children : [
                    {
                        path: 'depreciacoes',
                        name: 'ModalTabelaDepreciacoes',
                        component: ModalTabelaDepreciacoes,
                        meta: {
                            modal: {
                                showModal: true
                            },
                            requiresAuth: true,
                            authorities: ['Mobiliario.Normal']
                        }
                    },
                ]
            },
            {
                path: 'movimentacoes',
                name: 'FichaPatrimonioMovimentacoes',
                component: FichaPatrimonioMovimentacoes,
                meta: {
                    requiresAuth: true,
                    authorities: ['Mobiliario.Normal']
                }
            }
        ]
    },
    {
        path: '/patrimonio/:patrimonioId/',
        name: 'FichaPatrimonioVisualizacao',
        component: FichaPatrimonioVisualizar,
        meta: {
            page: {
                title: 'Patrimônios',
                subtitle: 'Ficha individual do patrimônio'
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Normal', 'Mobiliario.Consulta']
        },
        children: [
            {
                path: 'ficha/visualizacao',
                name: 'FichaPatrimonioDadosGeraisVisualizacao',
                component: FichaPatrimonioDadosGeraisVisualizar,
                meta: {
                    requiresAuth: true,
                    authorities: ['Mobiliario.Consulta']
                }
            },
            {
                path: 'movimentacoes/visualizacao',
                name: 'FichaPatrimonioMovimentacoesVisualizacao',
                component: FichaPatrimonioMovimentacoes,
                meta: {
                    requiresAuth: true,
                    authorities: ['Mobiliario.Consulta']
                }
            }
        ]
    },
]
