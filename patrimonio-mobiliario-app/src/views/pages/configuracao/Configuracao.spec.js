import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {shallowMount} from '@vue/test-utils'
import Configuracao from './Configuracao'

describe('Configuracao.vue', () => {

    let wrapper, store, localVue, state, router

    beforeEach(() => {

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {}
            },
            currentRoute: {
                matched: [
                    {
                        path: '/configuracao',
                        name: 'configuracao'
                    },
                    {
                        path: '/convenios',
                        name: 'ConvenioListagem'
                    }
                ]
            }
        }

        state = {
            comum: {
                rota: {
                    origem: 'Inicial'
                },
            },
            loki: {
                user: {
                    authorities: [{name: 'Mobiliario.Retroativo', hasAccess: true}]
                },
                menuActions: [
                    {
                        children: [],
                        icon: 'fas fa-clipboard-check',
                        name: 'Patrimônios',
                        path: '/patrimoniosListagem',
                        expanded: false,
                        selected: false
                    },
                    {
                        children: [],
                        icon: 'sync_alt',
                        name: 'Movimentações',
                        path: '/movimentacao',
                        expanded: false,
                        selected: false
                    },
                    {
                        children: [],
                        icon: 'fas fa-file-download',
                        name: 'Relatórios ',
                        path: '/relatorio',
                        expanded: false,
                        selected: false
                    },
                    {
                        children: [],
                        icon: 'fas fa-cog',
                        name: 'Configurações',
                        path: '/configuracao',
                        expanded: false,
                        selected: false
                    }
                ]
            }
        }

        localVue = applicationTestBuilder.build()

        store = new Vuex.Store({state})
    })

    describe('methods', () => {

        it('deve verificar se o usuário está na rota de configurações quando a página for carregada e setar true para o menu de configurações expandir', () => {
            wrapper = shallowMount(Configuracao, {
                store,
                localVue,
                router
            })

            wrapper.vm.validarRotaConfiguracao()

            expect(state.loki.menuActions[3].expanded).toEqual(true)
            expect(state.loki.menuActions[3].selected).toEqual(true)
        })

        it('não deve setar true para o menu de configuração pois o usuário esta em outra página', () => {
            router.currentRoute.matched =
                [
                    {
                        path: '/incorporacao',
                        name: 'incorporacao'
                    },
                    {
                        path: '/novo',
                        name: 'NovaEntrada'
                    }
                ]
            wrapper = shallowMount(Configuracao, {
                store,
                localVue,
                router
            })

            wrapper.vm.validarRotaConfiguracao()

            expect(state.loki.menuActions[3].expanded).toEqual(false)
            expect(state.loki.menuActions[3].selected).toEqual(false)
        })
    })
})
