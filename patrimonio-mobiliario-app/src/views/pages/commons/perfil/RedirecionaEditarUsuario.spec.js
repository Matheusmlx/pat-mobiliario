import {shallowMount} from '@vue/test-utils'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'
import {actionTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import RedirecionaEditarUsuario from './RedirecionaEditarUsuario'

describe('RedirecionaEditarUsuario', () => {
    let actions, store, localVue

    const linkEditarUsuario = 'http://teste'

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        actions = {
            [actionTypes.COMUM.BUSCAR_LINK_EDITAR_USUARIO]: jest.fn().mockReturnValue(linkEditarUsuario)
        }

        store = new Vuex.Store({ actions })
    })

    describe('@created', () => {
        it('deve renderizar somente a barra de ações sem dados e sem ações', async () => {
            window.location.assign = jest.fn()

            shallowMount(RedirecionaEditarUsuario, {
                store,
                localVue
            })

            await flushPromises()
            expect(actions[actionTypes.COMUM.BUSCAR_LINK_EDITAR_USUARIO]).toHaveBeenCalled()
            expect(window.location.assign).toHaveBeenCalledWith(linkEditarUsuario)
        })
    })
})
