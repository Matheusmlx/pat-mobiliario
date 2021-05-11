import Reserva from '@/views/pages/configuracao/reserva/global/Reserva'
import ReservaDadosGeraisCadastro from '@/views/pages/configuracao/reserva/global/novo/ReservaDadosGeraisCadastro'
import ReservaListagem from '@/views/pages/configuracao/reserva/global/listagem/ReservaListagem'
import ReservaEdicao from '@/views/pages/configuracao/reserva/global/edicao/ReservaEdicao'
import ReservaVisualizacao from '@/views/pages/configuracao/reserva/global/visualizacao/ReservaVisualizacao'
import ModalAdicionarIntervalos from '@/views/components/modal/configuracao/reserva/global/intervalo/ModalAdicionarIntervalos'

import ReservaListagemPorOrgao from '@/views/pages/configuracao/reserva/por-orgao/listagem/ReservaListagem'
import ReservaCadastroPorOrgao from '@/views/components/modal/configuracao/reserva/por-orgao/novo/ModalAdicionarReservaPorOrgao'

import store from '@/core/store'

const reservaPatrimonialGlobal = store.state.parametros.parametros.reservaPatrimonialGlobal

class ReservaGlobal {

    rotas() {
        return [
            {
                path: '/reservas',
                name: 'ReservaListagem',
                component: ReservaListagem,
                meta: {
                    menu: {
                        title: 'Reservas'
                    },
                    page: {
                        title: 'Reserva Patrimonial',
                        subtitle: 'Lista geral de placas de reserva patrimonial'
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Configuracao']
                }
            },
            {
                path: '/reserva',
                name: 'Reserva',
                component: Reserva,
                children: [
                    {
                        path: '/reserva/novo',
                        name: 'ReservaCadastro',
                        component: ReservaDadosGeraisCadastro,
                        meta: {
                            page: {
                                title: 'Adicionar Nova Reserva Patrimonial',
                                subtitle: 'Lista geral de placas de reserva patrimonial'
                            },
                            requiresAuth: true,
                            authorities: ['Mobiliario.Configuracao']
                        }
                    },
                    {
                        path: '/reserva/edicao/:id',
                        name: 'ReservaEdicao',
                        component: ReservaEdicao,
                        meta: {
                            page: {
                                title: 'Reserva',
                                subtitle: 'Lista geral de placas de reserva patrimonial'
                            },
                            requiresAuth: true,
                            authorities: ['Mobiliario.Configuracao']
                        },
                        children: [
                            {
                                path: 'adicionar-orgaos',
                                name: 'ModalAdicionarIntervalos',
                                component: ModalAdicionarIntervalos,
                                meta: {
                                    requiresAuth: true,
                                    authorities: ['Mobiliario.Configuracao']
                                }
                            },
                        ]
                    },
                    {
                        path: '/reserva/:id/visualizacao',
                        name: 'ReservaVisualizacao',
                        component: ReservaVisualizacao,
                        meta: {
                            page: {
                                title: 'Visualizar Reserva Patrimonial'
                            },
                            requiresAuth: true,
                            authorities: ['Mobiliario.Configuracao']
                        }
                    }
                ]
            }
        ]
    }
}

class ReservaPorOrgao {
    rotas() {
        return [
            {
                path: '/reservas',
                name: 'ReservaListagem',
                component: ReservaListagemPorOrgao,
                meta: {
                    menu: {
                        title: 'Reservas'
                    },
                    page: {
                        title: 'Reserva Patrimonial',
                        subtitle: 'Lista geral de placas de reserva patrimonial'
                    },
                    requiresAuth: true,
                    authorities: ['Mobiliario.Configuracao']
                },
                children: [
                    {
                        path: '/reserva/novo',
                        name: 'ReservaCadastro',
                        component: ReservaCadastroPorOrgao,
                        meta: {
                            requiresAuth: true,
                            authorities: ['Mobiliario.Configuracao']
                        }
                    }
                ]
            }
        ]
    }
}

export default reservaPatrimonialGlobal ? new ReservaGlobal() : new ReservaPorOrgao()
