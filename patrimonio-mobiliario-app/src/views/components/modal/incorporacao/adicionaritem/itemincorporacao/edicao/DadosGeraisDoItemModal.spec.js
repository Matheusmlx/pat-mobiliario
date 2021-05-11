import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import DadosGeraisDoItemModal from './DadosGeraisDoItemModal'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import {mutationTypes,actionTypes} from '@/core/constants'

const  dadosGeraisDoItem = {
    id:1,
    contaContabil: {
        codigo: '123110500',
        descricao: 'VEÍCULOS',
        id: 41
    },
    codigo:'0151425',
    descricao:'AUTOMOVEL MARCA VOLKSWAGEN, MOD.SATANA 2.0',
    marca:'Ford',
    modelo:'A356',
    uriImagem:'1/2.pdf',
    quantidade:'10',
    naturezaDespesa: {
        id:1,
        situacao:'INATIVO',
        disabled: false
    }
}

const estadosConservacao = {
    estadosConservacao: [
        {
            id: 1,
            nome: 'Ótimo',
            prioridade: 1
        },
        {
            id: 2,
            nome: 'Bom',
            prioridade: 2
        },
        {
            id: 3,
            nome: 'Regular',
            prioridade: 3
        }
    ]
}

describe('DadosGeraisDoItemModal.vue' , () => {
    let wrapper, localVue, state, mutations, actions, errors, vuetify, store, router

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
                    name: 'DadosGeraisDoItemModal',
                    params: {
                        incorporacaoId: 1,
                        itemIncorporacaoId: 1
                    },
                },
            },
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    authorities: [{name: 'Mobiliario.Retroativo', hasAccess: true}]
                }
            },
            dadosDeEntrada:{}
        }

        mutations = {
            [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO]: jest.fn(),
            [mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_INCORPORACAO_ITEM]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
        }

        actions = {
          [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
          [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
          [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO]: jest.fn().mockReturnValue(estadosConservacao),
          [actionTypes.COMUM.BUSCAR_NATUREZAS_DESPESA]: jest.fn().mockReturnValue({items:[{id:1,descricao:'VEICULOS DIVERSOS',despesa:'44905248'}]}),
          [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.UPLOAD]: jest.fn()
        }

        store = new Vuex.Store({actions,mutations,state})

    })
    describe('Event', () => {
        it('Deve emitir evento para verificar se pode continuar' , async () => {
            wrapper  = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })


            wrapper.vm.liberarContinuar(true)
            await flushPromises()
            expect(wrapper.vm.camposObrigatoriosNaoPreenchidos).toEqual(true)
            expect(mutations[mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_INCORPORACAO_ITEM]).toHaveBeenCalled()
            expect(wrapper.emitted().verificarContinuar[0][0]).toEqual(true)
        })
    })

    describe('Mounted' , () => {
        it('Deve gerar array com ano de fabricação/modelo com ano minimo 1980/1980 e máximo anoAtual/anoAtual+1', async () => {
            wrapper = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })
            await flushPromises()

            wrapper.vm.geraAnoModeloFabricacao()
            const anoAtual = new Date().getFullYear()
            const tamanhoArray = wrapper.vm.anoFabricacaoModelo.length
            expect(wrapper.vm.anoFabricacaoModelo[0]).toEqual(anoAtual+'/'+ parseInt(anoAtual+1))
            expect(wrapper.vm.anoFabricacaoModelo[tamanhoArray-1]).toEqual('1980/1980')
        })

        it('Não deve salvar anexo se tamanho maior que 15116247', async () => {
            const dadosGeraisDoItem = {uriImagem: {name: 'data.png', size: 15116248, type: 'image/png'}}
            wrapper = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })

            await flushPromises()
            wrapper.vm.tratarEventoSalvarAnexo(dadosGeraisDoItem)
            await flushPromises()

            expect(wrapper.vm.validarTamanhoMaximo(dadosGeraisDoItem.uriImagem.size)).toEqual(true)
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]).not.toHaveBeenCalled()
        })

        it('Não deve salvar anexo se extensão invalida', async () => {
            const  dadosGeraisDoItem = {uriImagem:{name:'data.mgp',size:2,type:'mgp'}}
            wrapper = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })

            await flushPromises()
            wrapper.vm.tratarEventoSalvarAnexo(dadosGeraisDoItem)
            await flushPromises()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Extensão do arquivo inválida. Envie arquivos nos seguintes formatos .jpeg .jpg .png')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('error')
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]).not.toHaveBeenCalled()
        })

        it('Deve salvar anexo', async () => {
            const dadosGeraisDoItem = {uriImagem: {name: 'data.png', size: 5000, type: 'image/png'}}
            wrapper = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })

            await flushPromises()
            wrapper.vm.tratarEventoSalvarAnexo(dadosGeraisDoItem)
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
        })

        it('Deve buscar os itens gerais' , async () => {
            wrapper  = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })

            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO].mock.calls[0][1]).toEqual({idIncorporacao: 1, idItem: 1})
        })

        it('Deve salvar os dados do item', async () => {
            wrapper  = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                data: {
                    itemIncorporacaoId: 2
                },
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })
            const dadosGeraisDoItem = {uriImagem:{}}

            const tratarEventoUploadImagem = jest.spyOn(wrapper.vm, 'tratarEventoUploadImagem')

            await wrapper.vm.salvarDadosGerais(dadosGeraisDoItem)

            expect(mutations[mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]).toHaveBeenCalled()
            expect(tratarEventoUploadImagem).toHaveBeenCalledWith(dadosGeraisDoItem)
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.UPLOAD]).toHaveBeenCalled()
        })

        it('Natureza de despesa deve receber o id da natureza ao salvar' , async () => {
            wrapper  = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })

            wrapper.vm.salvarDadosGerais({naturezaDespesa:{id:1}})
            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO].mock.calls[0][1].naturezaDespesa).toEqual(1)
        })

        it('Deve selecionar diretamente a natureza despesa se houver apenas uma', async() => {
            dadosGeraisDoItem.naturezaDespesa= null
            const naturezaDespesas = [{id:1,descricao:'VEICULOS DIVERSOS',despesa:'44905248'}]
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO]: jest.fn().mockReturnValue(estadosConservacao),
                [actionTypes.COMUM.BUSCAR_NATUREZAS_DESPESA]: jest.fn().mockReturnValue({items:[naturezaDespesas[0]]}),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.UPLOAD]: jest.fn()
            }

            wrapper = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })

            const selecionarNaturezaDespesaCasoTenhaUmaApenas = jest.spyOn(wrapper.vm, 'selecionarNaturezaDespesaCasoTenhaUmaApenas')

            await flushPromises()
            expect(selecionarNaturezaDespesaCasoTenhaUmaApenas).toHaveBeenCalledWith(naturezaDespesas)
            expect(wrapper.vm.dadosDeEntrada.naturezaDespesa).toBe(naturezaDespesas[0].id)
            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]).toHaveBeenCalled()
        })
    })

    describe('Methods', () => {

        it('Deve verificar se o item está válido e setar situação edição pendente' , async () => {
            wrapper  = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })
            await flushPromises()
            wrapper.vm.dadosDeEntrada.situacao = 'FINALIZADO'
            wrapper.vm.verificarSituacaoItemIncorporacao(true)

            expect(wrapper.vm.dadosDeEntrada.situacao).toEqual('EM_ELABORACAO')
        })

        it('Deve buscar todos estados de conservação', async () => {
            wrapper = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })

            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO]).toHaveBeenCalled()
        })

        it('Deve emitir mensagem se não houver natureza de despesa', async () => {
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO]: jest.fn().mockReturnValue(estadosConservacao),
                [actionTypes.COMUM.BUSCAR_NATUREZAS_DESPESA]: jest.fn().mockReturnValue({items:[]}),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.UPLOAD]: jest.fn()
            }
            wrapper = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })
            await flushPromises()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Item sem conta contábil, natureza de despesa ou tipo ativos. Escolha outro item')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('warning')
        })

        it('Deve tratar campo natureza de despesa se for inativa', async () => {
            const dadosGeraisDoItem = {naturezaDespesa: {id:1, situacao: 'INATIVO'}}
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO]: jest.fn().mockReturnValue(estadosConservacao),
                [actionTypes.COMUM.BUSCAR_NATUREZAS_DESPESA]: jest.fn().mockReturnValue({items:[]}),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.UPLOAD]: jest.fn()
            }
            wrapper = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })
            await flushPromises()

            expect(wrapper.vm.naturezaDespesaInativo).toEqual(true)
            expect(wrapper.vm.naturezasDespesa[0].id).toEqual(1)
            expect(wrapper.vm.naturezasDespesa[0].situacao).toEqual('INATIVO')
            expect(wrapper.vm.naturezasDespesa[0].disabled).toEqual(true)
            expect(wrapper.vm.dadosDeEntrada.naturezaDespesa).toEqual(1)
        })

        it('Deve ocultar tooltip de natureza inativa se natureza ativa', async () => {
            const dadosGeraisDoItem = {naturezaDespesa: {id:1, situacao: 'ATIVO'}}
            actions = {
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO]: jest.fn().mockReturnValue(dadosGeraisDoItem),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO]: jest.fn().mockReturnValue(estadosConservacao),
                [actionTypes.COMUM.BUSCAR_NATUREZAS_DESPESA]: jest.fn().mockReturnValue({items:[]}),
                [actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.IMAGEM.UPLOAD]: jest.fn()
            }
            wrapper = shallowMount(DadosGeraisDoItemModal, {
                localVue,
                vuetify,
                router,
                store: new Vuex.Store({
                    state, mutations, actions
                }),
                mocks:{errors}
            })
            await flushPromises()

            expect(wrapper.vm.naturezaDespesaInativo).toEqual(false)
        })
    })
})
