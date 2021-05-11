import RedirecionaEditarUsuario from '@/views/pages/commons/perfil/RedirecionaEditarUsuario'
import PaginaNaoEncontrada from '@/views/pages/commons/pagina-nao-encontrada/PaginaNaoEncontrada'
import AcessoNegado from '@/views/pages/commons/acesso-negado/AcessoNegado'


export default [
    {
        path: '/perfil',
        name: 'RedirecionarEditarUsuario',
        component: RedirecionaEditarUsuario,
    },
    {
        path: '*',
        name: 'PaginaNaoEncontrada',
        component: PaginaNaoEncontrada,
        meta: {
            page: {
                title: 'Ooops',
                subtitle: 'Página não encontrada'
            }
        }
    },
    {
        path: '*',
        name: 'AcessoNegado',
        component: AcessoNegado,
        meta: {
            page: {
                title: 'Acesso Negado',
            }
        }
    },
]
