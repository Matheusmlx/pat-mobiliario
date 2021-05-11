import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import ModalIncorporacaoAcoes from './ModalIncorporacaoAcoes'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'


describe('ModalIncorporacaoAcoes.vue' , () => {
    let wrapper, localVue, state, errors, vuetify, store, router

    beforeEach(() => {

        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()


        errors = {
            collect: jest.fn()
        }
        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'ModalIncorporacaoItem',
                    params: {
                        incorporacaoId: 1,
                        itemIncorporacaoId: 1
                    },
                    meta: {
                        modal: {
                            title: 'Etapa 1 - Selecione o item',
                            podeVoltar: false,
                            showModal: true
                        }
                    }
                },
            },
        }


        state = {
            loki: {
                user: {
                    domainId: 1
                }
            },
            itemIncorporacao: {
                dadosGerais: {
                    id:1,
                    idIncorporacao: 1,
                    quantidade: 2,
                    valorTotal: 50,
                    contaContabil: { id: 1 }
                },
                autoSave: {
                    show: false
                },
                rotaModal:{
                    origem: 'ItensIncorporacaoListagem'
                }
            }
        }

        store = new Vuex.Store({state})

    })

    describe('Events' , () => {

        it('Deve emitir o evento de voltar para a etapa 1 (Adicionar Novo)', async () => {
            wrapper = shallowMount(ModalIncorporacaoAcoes, {
                localVue,
                store,
                router
            })
            await wrapper.vm.adicionar()

            expect(wrapper.emitted().voltarPassoNovo).toBeTruthy()
        })


        it('Deve emitir o evento de continuar para a etapa 2 (Após adicionar um novo item)', async () => {
            wrapper = shallowMount(ModalIncorporacaoAcoes, {
                localVue,
                store,
                router
            })
            wrapper.vm.$router.history.current.name = 'ItensCatalogoModalNovo'
            await wrapper.vm.continuar()

            expect(wrapper.emitted().salvarNovoItem).toBeTruthy()
        })

        it('Deve emitir o evento de continuar para a etapa 2 (Após editar um item na etapa 1)', async () => {
            wrapper = shallowMount(ModalIncorporacaoAcoes, {
                localVue,
                store,
                router
            })
            wrapper.vm.$router.history.current.name = 'ItensCatalogoModalEdicao'
            await wrapper.vm.continuar()

            expect(wrapper.emitted().editarItemPassoUm).toBeTruthy()
        })

        it('Deve emitir o evento de continuar para a etapa 2 (Modo visualização)', async () => {
            wrapper = shallowMount(ModalIncorporacaoAcoes, {
                localVue,
                store,
                router
            })
            wrapper.vm.$router.history.current.name = 'ItensCatalogoModalVisualizacaoCadastrado'
            await wrapper.vm.continuar()

            expect(wrapper.emitted().avancarEtapaDoisVisualizar).toBeTruthy()
        })

        it('Deve emitir o evento de continuar para a etapa 3 (Apos editar o item na etapa 2)', async () => {
            wrapper = shallowMount(ModalIncorporacaoAcoes, {
                localVue,
                store,
                router
            })
            wrapper.vm.$router.history.current.name = 'DadosGeraisDoItemModal'
            await wrapper.vm.continuar()

            expect(wrapper.emitted().avancarEtapaTres).toBeTruthy()
        })

        it('Deve emitir o evento de continuar para a etapa 3 (Modo visualização)', async () => {
            wrapper = shallowMount(ModalIncorporacaoAcoes, {
                localVue,
                store,
                router
            })
            wrapper.vm.$router.history.current.name = 'VisualizacaoDadosGerais'
            await wrapper.vm.continuar()

            expect(wrapper.emitted().avancarEtapaTresVisualizar).toBeTruthy()
        })

        it('Deve emitir o evento de fechar o modal', async () => {
            wrapper = shallowMount(ModalIncorporacaoAcoes, {
                localVue,
                store,
                router
            })
            wrapper.vm.$router.history.current.name = 'VisualizacaoDadosGerais'
            await wrapper.vm.fecharModal()

            expect(wrapper.emitted().fechar).toBeTruthy()
        })


        it('Deve emitir o evento de voltar para a etapa 1 (Modo edição)', async () => {
            wrapper = shallowMount(ModalIncorporacaoAcoes, {
                localVue,
                store,
                router
            })
            wrapper.vm.$router.history.current.name = 'DadosGeraisDoItemModal'
            await wrapper.vm.voltar()

            expect(wrapper.emitted().voltarPassoUmEdicao).toBeTruthy()
        })

        it('Deve emitir o evento de voltar para a etapa 1 (Modo visualizar)', async () => {
            wrapper = shallowMount(ModalIncorporacaoAcoes, {
                localVue,
                store,
                router
            })
            wrapper.vm.$router.history.current.name = 'VisualizacaoDadosGerais'
            await wrapper.vm.voltar()

            expect(wrapper.emitted().voltarPassoUmVisualizar).toBeTruthy()
        })

        it('Deve emitir o evento de voltar para a etapa 2 (Modo edição)', async () => {
            wrapper = shallowMount(ModalIncorporacaoAcoes, {
                localVue,
                store,
                router
            })
            wrapper.vm.$router.history.current.name = 'IncorporacaoItemListagemPatrimonios'
            await wrapper.vm.voltar()

            expect(wrapper.emitted().voltarPassoDoisEdicao).toBeTruthy()
        })


    })

})
