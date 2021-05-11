import convenio from './convenio'
import Configuracao from '@/views/pages/configuracao/Configuracao'
import ContaContabil from '@/views/pages/configuracao/conta-contabil/ContaContabil'
import Reconhecimento from '@/views/pages/configuracao/reconhecimento/Reconhecimento'
import reservas from './reservas'

export default [
    {
        path: '/configuracao',
        name: 'configuracao',
        component: Configuracao,
        meta: {
            menu: {
                title: 'Configurações',
                icon: 'fas fa-cog'
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Configuracao']
        },
        children: [
            {
                path: '/contas-contabeis',
                name: 'ContaContabil',
                component: ContaContabil,
                meta: {
                    menu: {
                        title: 'Contas Contábeis'
                    },
                    page: {
                        title: 'Contas Contábeis',
                        subtitle: 'Lista geral de contas contábeis'
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Configuracao']
                }
            },
            ...convenio,
            {
                path: '/reconhecimentos',
                name: 'Reconhecimento',
                component: Reconhecimento,
                meta: {
                    menu: {
                        title: 'Reconhecimentos'
                    },
                    page: {
                        title: 'Reconhecimentos',
                        subtitle: 'Lista geral de cadastro de reconhecimentos'
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Configuracao']
                }
            },
            ...reservas.rotas()
        ]
    }
]
