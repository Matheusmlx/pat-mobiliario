import ConvenioListagem from '@/views/pages/configuracao/convenio/listagem/ConvenioListagem'
import Convenio from '@/views/pages/configuracao/convenio/Convenio'
import ConvenioDadosGeraisCadastro from '@/views/pages/configuracao/convenio/novo/ConvenioDadosGeraisCadastro'
import ConvenioDadosGeraisEdicao from '@/views/pages/configuracao/convenio/edicao/ConvenioDadosGeraisEdicao'

export default [
    {
        path: '/convenios',
        name: 'ConvenioListagem',
        component: ConvenioListagem,
        meta: {
            menu: {
                title: 'Convênios'
            },
            page: {
                title: 'Convênios',
                subtitle: 'Lista geral de cadastro de convênios'
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Configuracao']
        }
    },
    {
        path: '/convenio',
        name: 'Convenio',
        component: Convenio,
        children: [
            {
                path: '/convenio/novo',
                name: 'ConvenioCadastro',
                component: ConvenioDadosGeraisCadastro,
                meta: {
                    page: {
                        title: 'Convênio',
                        subtitle: 'Novo Convênio'
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Configuracao']
                }
            },
            {
                path: '/convenio/edicao/:id',
                name: 'ConvenioEdicao',
                component: ConvenioDadosGeraisEdicao,
                meta: {
                    page: {
                        title: 'Convênio',
                        subtitle: 'Edição de Convênio'
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Configuracao']
                }
            }
        ]
    }
]
