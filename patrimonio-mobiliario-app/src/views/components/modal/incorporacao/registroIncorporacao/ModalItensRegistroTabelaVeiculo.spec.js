import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import ModalItensRegistroTabelaVeiculo from './ModalItensRegistroTabelaVeiculo'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('ModalItensRegistroTabelaVeiculo', () => {
    let wrapper, localVue, vuetify, errors, router, state, $validator, $t = jest.fn()

    const defaultMount = (stubs) => shallowMount(ModalItensRegistroTabelaVeiculo, {
        localVue,
        router,
        vuetify,
        stubs,
        store: new Vuex.Store({state}),
        mocks: {$validator, errors, $t},
    })

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        state = {
            loki: {
                user: {
                    domainId: 1
                },
                uploadedFiles: [],
            },
            patrimonio: {
                colunasVeiculoRegistro: []
            }
        }

        errors = {
            collect: jest.fn()
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'ModalItemVisualizarRegistro',
                    params: {
                        incorporacaoId: 1
                    }
                }
            }
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true)
            }
        }

    })

    describe('Methods', () => {

        it('Deve retornar a placa formatada', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.formataPlaca('HTE4576')).toEqual('HTE-4576')
        })

        it('Deve retornar o chassi formatado', () => {
            wrapper = defaultMount()

            expect(wrapper.vm.formataChassi('4xB3ApAWNwu6e8838')).toEqual('4xB 3ApAW N wu6e8838')
        })

        it('Deve emitir evento para redirecionar para ficha individual do patrimônio', () => {
            wrapper = defaultMount()

            const patrimonio = {id: 5}

            wrapper.vm.tratarEventoRedirecionarFichaPatrimonio(patrimonio)

            expect(wrapper.emitted().redirecionarFichaPatrimonio).toBeTruthy()
            expect(wrapper.emitted().redirecionarFichaPatrimonio[0][0]).toEqual(patrimonio)
        })

    })
})
