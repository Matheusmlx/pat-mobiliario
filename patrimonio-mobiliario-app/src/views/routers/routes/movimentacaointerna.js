import Movimentacao from '@/views/pages/movimentacao/Movimentacao'
import MovimentacaoInternaListagem from '@/views/pages/movimentacao/movimentacao-interna/listagem/MovimentacaoInternaListagem'
import MovimentacaoInternaNovoTipo
    from '@/views/pages/movimentacao/movimentacao-interna/novo/tipo/MovimentacaoInternaNovoTipo'
import MovimentacaoInternaEdicaoTipo
    from '@/views/pages/movimentacao/movimentacao-interna/edicao/tipo/MovimentacaoInternaEdicaoTipo'
import MovimentacaoInternaEdicao from '@/views/pages/movimentacao/movimentacao-interna/MovimentacaoInternaEdicao'
import MovimentacaoInternaEdicaoDadosGerais
    from '@/views/pages/movimentacao/movimentacao-interna/edicao/dados-gerais/MovimentacaoInternaEdicaoDadosGerais'
import MovimentacaoInternaEdicaoItens
    from '@/views/pages/movimentacao/movimentacao-interna/edicao/item/MovimentacaoInternaEdicaoItens'
import MovimentacaoInternaEdicaoDocumentos
    from '@/views/pages/movimentacao/movimentacao-interna/edicao/documentos/MovimentacaoInternaEdicaoDocumentos'
import ModalMovimentacaoInternaListagemItens
    from '@/views/components/modal/movimentacao-interna/edicao/patrimonios/ModalMovimentacaoInternaListagemItens'
import VisualizarRegistroMovimentacaoInterna
    from '@/views/pages/movimentacao/movimentacao-interna/visualizacaoRegistro/VisualizarRegistroMovimentacaoInterna'
import ModalDevolverPatrimonios
    from '@/views/components/modal/movimentacao-interna/visualizacaoRegistro/devolucao/ModalDevolverPatrimonios'

export default [
    {
        path: '/movimentacao',
        name: 'Movimentacao',
        component: Movimentacao,
        meta: {
            menu: {
                title: 'Movimentações',
                icon: 'sync_alt'
            },
        },
        children: [
            {
                path: '/movimentacao/interna',
                name: 'MovimentacaoInternaListagem',
                component: MovimentacaoInternaListagem,
                meta: {
                    menu: {
                        title: 'Internas'
                    },
                    page: {
                        title: 'Movimentações Internas',
                        subtitle: 'Lista geral das movimentações dos patrimônios'
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Movimentacoes']
                }
            }
        ]
    },
    {
        path: '/movimentacao/interna',
        name: 'MovimentacaoInternaNovo',
        component: MovimentacaoInternaEdicao,
        children: [
            {
                path: 'tipo/novo',
                name: 'MovimentacaoInternaNovoTipo',
                component: MovimentacaoInternaNovoTipo,
                meta: {
                    page: {
                        title: 'Adicionar Nova Movimentação',
                        showAutoSave: true
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Movimentacoes', 'Mobiliario.Consulta']
                }
            }
        ]
    },
    {
        path: '/movimentacao/interna/:movimentacaoInternaId',
        name: 'MovimentacaoInternaEdicao',
        component: MovimentacaoInternaEdicao,
        children: [
            {
                path: 'tipo/edicao',
                name: 'MovimentacaoInternaEdicaoTipo',
                component: MovimentacaoInternaEdicaoTipo,
                meta: {
                    page: {
                        title: 'Adicionar Nova Movimentação',
                        showAutoSave: true
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Movimentacoes']
                }
            },
            {
                path: 'dados-gerais',
                name: 'MovimentacaoInternaEdicaoDadosGerais',
                component: MovimentacaoInternaEdicaoDadosGerais,
                meta: {
                    page: {
                        title: 'Adicionar Nova Movimentação',
                        showAutoSave: true
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Movimentacoes']
                }
            },
            {
                path: 'patrimonios/edicao',
                name: 'MovimentacaoInternaEdicaoItens',
                component: MovimentacaoInternaEdicaoItens,
                meta: {
                    page: {
                        title: 'Adicionar Nova Movimentação',
                        showAutoSave: true
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Movimentacoes']
                },
                children: [
                    {
                        path: 'adicionar-patrimonios',
                        name: 'ModalMovimentacaoInternaListagemItens',
                        component: ModalMovimentacaoInternaListagemItens,
                        meta: {
                            requiresAuth: true,
                            authorities: ['Mobiliario.Movimentacoes']
                        }
                    },
                ]
            },
            {
                path: 'documentos/edicao',
                name: 'MovimentacaoInternaEdicaoDocumentos',
                component: MovimentacaoInternaEdicaoDocumentos,
                meta: {
                    page: {
                        title: 'Adicionar Nova Movimentação',
                        showAutoSave: true
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Movimentacoes']
                }
            }
        ]
    },
    {
        path: '/movimentacao/interna/:movimentacaoInternaId/visualizacaoRegistro',
        name: 'VisualizarRegistroMovimentacaoInterna',
        component: VisualizarRegistroMovimentacaoInterna,
        meta: {
            page: {
                title: 'Visualizar Movimentação',
                showAutoSave: true
            },
            requiresAuth: true,
            authorities: ['Mobiliario.Movimentacoes']
        },
        children: [
            {
                path: 'devolver-patrimonios',
                name: 'ModalDevolverPatrimonios',
                component: ModalDevolverPatrimonios,
                meta: {
                    requiresAuth: true,
                    authorities: ['Mobiliario.Movimentacoes']
                }
            },
        ]
    }
]
