import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import ModalItensRegistroTabelaEquipamentoArmamento from './ModalItensRegistroTabelaEquipamentoArmamento'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('ModalItensRegistroTabelaEquipamentoArmamento', () => {
    let wrapper, localVue, vuetify, router, state, store

    const defaultMount = () => shallowMount(ModalItensRegistroTabelaEquipamentoArmamento, {
        localVue,
        router,
        vuetify,
        store
    })

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        store = new Vuex.Store({state})
    })

    describe('Watchers', () => {
        it('Deve emitir evento para paginar', () => {
            const paginacao = {
                page: 1,
                rowsPerPage: 10,
                sortBy: ['numeroPatrimonio'],
                sortDesc: [false]
            }

            const paginacao2 = {
                page: 2,
                rowsPerPage: 10,
                sortBy: ['numeroPatrimonio'],
                sortDesc: [false]
            }

            wrapper = shallowMount(ModalItensRegistroTabelaEquipamentoArmamento, {
                localVue,
                router,
                vuetify,
                store,
                propsData: {
                    paginacao
                }
            })

            wrapper.vm.$options.watch.paginacaoInterna.handler.call(wrapper.vm, paginacao2)

            expect(wrapper.emitted().paginar).toBeTruthy()
            expect(wrapper.emitted().paginar[0][0]).toBe(paginacao2)
        })
    })

    describe('Methods', () => {
        it('Deve emitir evento para redirecionar para ficha individual do patrimônio', () => {
            wrapper = defaultMount()

            const item = {id: 5}

            wrapper.vm.tratarEventoRedirecionarFichaPatrimonio(item)

            expect(wrapper.emitted().redirecionarFichaPatrimonio).toBeTruthy()
            expect(wrapper.emitted().redirecionarFichaPatrimonio[0][0]).toEqual(item)
        })

    })
})
