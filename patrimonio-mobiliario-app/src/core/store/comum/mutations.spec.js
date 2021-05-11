import mutations from './mutations'
import {mutationTypes} from '@/core/constants'

describe('Mutations', () => {

    it('deve chamar a mutation SET_EXPANDIR_MENU e atualizar o state', () => {
        const state = {
            loki: {
                asideClosed: true
            }
        }

        mutations[mutationTypes.COMUM.SET_EXPANDIR_MENU](state)
        expect(state.loki.asideClosed).toBeFalsy()
    })

    it('deve chamar a mutation SET_LINK_ARQUIVO e atualizar o state', () => {
        const state = {
            loki: {
                file: {}
            }
        }

        mutations[mutationTypes.COMUM.SET_LINK_ARQUIVO](state)
        expect(state.loki.file.api).toEqual('/patrimonio-mobiliario/api/v1/arquivos')
    })

    it('deve chamar a mutation SET_MENU_AVATAR e atualizar o state para tipo "Externo"', () => {
        const state = {
            loki: {
                user: {
                    type: 'EXTERNO'
                },
                avatarActions: {}
            }
        }

        const actions = {
            1: { title: 'Perfil', icon: 'person', path: '/perfil' }
        }

        mutations[mutationTypes.COMUM.SET_MENU_AVATAR](state)
        expect(state.loki.avatarActions).toEqual(actions)
    })

    it('deve chamar a mutation SET_MENU_AVATAR e atualizar o state para tipo "Interno"', () => {
        const state = {
            loki: {
                user: {
                    type: 'INTERNO'
                },
                avatarActions: {}
            }
        }

        const actions = {
            1: { title: 'Perfil', icon: 'person', path: '/perfil' }
        }

        mutations[mutationTypes.COMUM.SET_MENU_AVATAR](state)
        expect(state.loki.avatarActions).toEqual(actions)
    })

    it('deve chamar a mutation SET_PRODUTO e atualizar o state', () => {
        const state = {
            loki: {
                product: {}
            }
        }

        const data = {
            id: 20,
            nome: 'Compra Direta',
            atributosExtendidos: {
                logoMenu: 'logo1.png',
                logoMenuRetraido: 'logo2.png',
                logoMobile: 'logo3.png'
            }
        }

        mutations[mutationTypes.COMUM.SET_PRODUTO](state, data)
        expect(state.loki.product.id).toEqual(data.id)
        expect(state.loki.product.name).toEqual(data.nome)
        expect(state.loki.product.mainLogo).toEqual('/hal/public/arquivos?uri=logo1.png&thumbnail=false')
        expect(state.loki.product.symbolLogo).toEqual('/hal/public/arquivos?uri=logo2.png&thumbnail=false')
        expect(state.loki.product.logoMobile).toEqual('/hal/public/arquivos?uri=logo3.png&thumbnail=false')
    })


    it('deve chamar a mutation SET_RETRAIR_MENU e atualizar o state', () => {
        const state = {
            loki: {
                asideClosed: false
            }
        }

        mutations[mutationTypes.COMUM.SET_RETRAIR_MENU](state)
        expect(state.loki.asideClosed).toBeTruthy()
    })

    it('deve chamar a mutation SET_USUARIO_LOGADO e atualizar o state', () => {
        const state = {
            loki: {
                user: ''
            }
        }

        const usuario = {
            userId: 1,
            userName: 'Fulano',
            name: 'Fulano das Quantas',
            tipoUsuario: 'INTERNO',
            domainId: 34,
            domainName: 'teste',
            domainType: 'TESTE',
            authorities: [
                {
                    id: 78
                }
            ]
        }

        mutations[mutationTypes.COMUM.SET_USUARIO_LOGADO](state, usuario)

        expect(state.loki.user.id).toEqual(usuario.userId)
        expect(state.loki.user.name).toEqual(usuario.userName)
        expect(state.loki.user.fullName).toEqual(usuario.name)
        expect(state.loki.user.type).toEqual(usuario.tipoUsuario)
        expect(state.loki.user.domainId).toEqual(usuario.domainId)
        expect(state.loki.user.domainName).toEqual(usuario.domainName)
        expect(state.loki.user.domainType).toEqual(usuario.domainType)
        expect(state.loki.user.authorities).toEqual(usuario.authorities)
        expect(state.loki.user.photo).toEqual('')
    })

    it('deve setar parâmetros', () => {
        const state = {
            parametros: {}
        }
        const param = 'teste'

        mutations[mutationTypes.COMUM.SET_PARAMETROS](state, param)
        expect(state.parametros).toBeTruthy()
        expect(state.parametros).toEqual(param)
    })
})
