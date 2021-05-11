<template>
    <v-row justify="center">
        <v-dialog v-model="exibeModal" persistent max-width="1100" scrollable @keydown.esc="fecharModal">
            <v-card>
                <v-toolbar dark color="primary" class="styleToolBar">
                    <v-toolbar-title><span class="etapaStyle">{{titulo}}</span></v-toolbar-title>
                    <v-spacer/>
                    <az-auto-save v-if="$store.state.itemIncorporacao.autoSave.show" class="auto-save-modal"/>
                    <v-btn class="close__button" icon @click="fecharModal">
                        <v-icon size="20">close</v-icon>
                    </v-btn>
                </v-toolbar>
                <v-card-text class="modal-incorporacao-container">
                    <router-view @fechar="fecharModal" @verificarContinuar="tratarEventoVerificarContinuar"
                                 @trocarItem="tratarEventoSelecionarItem"/>
                </v-card-text>
                <div>
                    <v-divider/>
                    <modal-incorporacao-acoes
                        :podeContinuar="continuar"
                        @salvarNovoItem="tratarEventoSalvarNovoItem"
                        @editarItemPassoUm="tratarEventoEditarItemPassoUm"
                        @avancarEtapaDoisVisualizar="tratarEventoAvancarEtapaDoisVisualizar"
                        @avancarEtapaTres="tratarEventoAvancarEtapaTres"
                        @avancarEtapaTresVisualizar="tratarEventoAvancarEtapaTresVisualizar"
                        @voltarPassoNovo="tratarEventoVoltarPassoUm"
                        @voltarPassoUmEdicao="tratarEventoVoltarPassoUmEdicao"
                        @voltarPassoUmVisualizar="tratarEventoVoltarPassoUmVisualizar"
                        @voltarPassoDoisEdicao="tratarEventoVoltarPassoDoisEdicao"
                        @voltarPassoDoisVisualizar="tratarEventoVoltarPassoDoisVisualizar"
                        @fechar="fecharModal"/>
                </div>
            </v-card>
        </v-dialog>
    </v-row>
</template>
<script>
    import ModalIncorporacaoAcoes from './ModalIncorporacaoAcoes'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapMutations, createNamespacedHelpers} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'ModalIncorporacaoItem',
        components: {ModalIncorporacaoAcoes},
        data() {
            return {
                exibeModal: true,
                etapa: '',
                descricaoEtapa: '',
                continuar: false,
                incorporacaoItem: {},
                incorporacaoId: null,
                itemIncorporacaoId: null,
                manteveItemCatalogo: true,
            }
        },
        async mounted() {
            await this.buscaItemIncorporacao()
        },
        computed: {
            ...mapGetters([
                'getItemIncorporacaoValidado',
            ]),
            titulo() {
                return this.$route.meta.modal.title
            },
        },
        watch: {
            $route: {
                immediate: true,
                handler: function (newVal) {
                    this.exibirModal = newVal.meta && newVal.meta.modal.showModal
                }
            }
        },
        methods: {
            ...mapMutations([
                mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO
            ]),
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.CADASTRAR_ITEM_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DELETAR_ITEM_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO,
                actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO
            ]),
            setarDadosParaEdicao(item) {
                this.incorporacaoItem.codigo = item.codigo
                this.incorporacaoItem.descricao = item.descricao
                this.incorporacaoItem.idIncorporacao = this.incorporacaoId
                if (this.incorporacaoItem.naturezaDespesa && typeof this.incorporacaoItem.naturezaDespesa.id !== 'undefined') {
                    this.incorporacaoItem.naturezaDespesa = this.incorporacaoItem.naturezaDespesa.id
                }
            },
            async buscaItemIncorporacao() {
                this.setIncorporacaoId()
                this.setItemIncorporacaoId()
                if (this.itemIncorporacaoId && this.incorporacaoId) {
                    this.incorporacaoItem = await this.buscarItemIncorporacao({
                        idItem: this.itemIncorporacaoId,
                        idIncorporacao: this.incorporacaoId
                    })
                }
            },
            async cadastraPatrimonio() {
                let item = this.$store.state.itemIncorporacao.dadosGerais

                const patrimonio = {
                    quantidade: item.quantidade,
                    valorTotal: item.valorTotal,
                    valorTotalAnterior: item.valorTotalAnterior,
                    contaContabilId: item.contaContabil.id,
                    itemIncorporacaoId: this.$route.params.itemIncorporacaoId
                }

                await this.cadastrarPatrimonio(patrimonio)
            },
            async fecharModal() {
                await this.tratarEventoFinalizarItemIncorporacao()
                this.desativarSalvamentoAutomatico()
                this.exibeModal = false
                this.$emit('fechar')
                if(this.verificaPermissaoEdicao()){
                    await this.$router.replace({ name: 'ItensIncorporacaoListagem', params: {incorporacaoId: this.incorporacaoId}})
                } else{
                    await this.$router.replace({ name: 'VisualizarItensIncorporacao', params: {incorporacaoId: this.incorporacaoId}})
                }
            },
            setIncorporacaoId() {
                if (this.$route.params.incorporacaoId) {
                    this.incorporacaoId = this.$route.params.incorporacaoId
                }
            },
            setItemIncorporacaoId() {
                if (this.$route.params.itemIncorporacaoId) {
                    this.itemIncorporacaoId = this.$route.params.itemIncorporacaoId
                }
            },
            async tratarEventoAvancarEtapaTres() {
                await this.cadastraPatrimonio()
                this.desativarSalvamentoAutomatico()
                await this.$router.replace({
                    name: 'IncorporacaoItemListagemPatrimonios',
                    params: {
                        itemIncorporacaoId: this.$route.params.itemIncorporacaoId,
                        incorporacaoId: this.incorporacaoId
                    }
                })
            },
            tratarEventoAvancarEtapaDoisVisualizar() {
                this.$router.replace({
                    name: 'VisualizacaoDadosGerais',
                    params: {
                        itemIncorporacaoId: this.$route.params.itemIncorporacaoId,
                        incorporacaoId: this.incorporacaoId
                    }
                })
            },
            tratarEventoAvancarEtapaTresVisualizar() {
                this.$router.replace({
                    name: 'IncorporacaoItemListagemPatrimoniosVisualizacao',
                    params: {incorporacaoId: this.incorporacaoId}
                })
            },
            async tratarEventoSelecionarItem() {
                let item = this.$store.state.itemIncorporacao.dadosGerais
                if (this.verificarSeHouveTrocaDeItem(item) && this.manteveItemCatalogo) {
                    this.mostrarNotificacaoAviso('Os campos preenchidos na "Etapa 2 - Dados gerais do item compra" serão resetados.')
                    this.manteveItemCatalogo = false
                }
            },
            async tratarEventoEditarItemPassoUm() {
                let item = this.$store.state.itemIncorporacao.dadosGerais
                if (this.verificarSeHouveTrocaDeItem(item)) {
                    const dados = {idIncorporacao: this.incorporacaoId, idItem: this.incorporacaoItem.id}
                    await this.deletarItemIncorporacao(dados)
                    await this.tratarEventoSalvarNovoItem()
                    this.setItemIncorporacaoId()
                } else {
                    await this.$router.replace({
                        name: 'DadosGeraisDoItemModal',
                        params: {itemIncorporacaoId: this.itemIncorporacaoId},
                    })
                }
            },
            verificarSeHouveTrocaDeItem(item) {
                return this.incorporacaoItem.codigo !== item.codigo
            },
            async tratarEventoFinalizarItemIncorporacao() {
                if (this.$route.meta.modal.visualizacao || this.$route.name !== 'IncorporacaoItemListagemPatrimonios') return
                if (await this.verificarItemIncorporacaoValidado()) {
                    if (this.incorporacaoItem.situacao === 'FINALIZADO') return
                    this.setarDadosParaEdicao(this.incorporacaoItem)
                    this.incorporacaoItem.situacao = 'FINALIZADO'
                    await this.editarItemIncorporacao(this.incorporacaoItem)
                }
            },
            async tratarEventoSalvarNovoItem() {
                this.desabilitarLoadingGlobal()
                let item = this.$store.state.itemIncorporacao.dadosGerais
                item = {...item, idIncorporacao: this.incorporacaoId}
                const retorno = await this.cadastrarItemIncorporacao(item)

                if (retorno.id) {
                    await this.$router.replace({
                        name: 'DadosGeraisDoItemModal',
                        params: {itemIncorporacaoId: retorno.id},
                    })
                }
                this.habilitarLoadingGlobal()
            },
            tratarEventoVerificarContinuar(condicao) {
                if (condicao !== undefined) {
                    this.continuar = condicao
                } else {
                    this.continuar = true
                }
            },
            tratarEventoVoltarPassoUm() {
                this.desativarSalvamentoAutomatico()
                if (this.verificaPermissaoEdicao()) {
                    this.$router.replace({
                        name: 'ItensCatalogoModalNovo',
                        params: {incorporacaoId: this.incorporacaoId}
                    })
                } else {
                    this.$router.replace({
                        name: 'ItensCatalogoModalVisualizacao',
                        params: {incorporacaoId: this.incorporacaoId}
                    })
                }
                this.continuar = false
            },
            async tratarEventoVoltarPassoUmEdicao() {
                this.manteveItemCatalogo = true
                await this.buscaItemIncorporacao()
                this.desativarSalvamentoAutomatico()
                await this.$router.replace({
                    name: 'ItensCatalogoModalEdicao',
                    params: {
                        incorporacaoId: this.incorporacaoId,
                        itemIncorporacaoId: this.$route.params.itemIncorporacaoId
                    }
                })
                this.continuar = false
            },
            tratarEventoVoltarPassoUmVisualizar() {
                this.$router.replace({
                    name: 'ItensCatalogoModalVisualizacaoCadastrado',
                    params: {itemIncorporacaoId: this.itemIncorporacaoId}
                })
                this.continuar = true
            },
            tratarEventoVoltarPassoDoisEdicao() {
                this.desativarSalvamentoAutomatico()
                this.$router.replace({
                    name: 'DadosGeraisDoItemModal',
                    params: {itemIncorporacaoId: this.$route.params.itemIncorporacaoId},
                })
            },
            tratarEventoVoltarPassoDoisVisualizar() {
                this.$router.replace({
                    name: 'VisualizacaoDadosGerais',
                    params: {itemIncorporacaoId: this.itemIncorporacaoId}
                })
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
            },
            async verificarItemIncorporacaoValidado() {
                await this.buscaItemIncorporacao()
                const tipoVeiculo = (this.incorporacaoItem.tipoBem === 'VEICULO')
                return this.getItemIncorporacaoValidado(this.incorporacaoItem, tipoVeiculo, 'ITEM_INCORPORACAO')
            }


        }
    }
</script>

<style lang="stylus">
    .item-patrimonio-tabela
        min-height 58vh

    .item-catalogo-tabela
        min-height 61vh

    .etapaStyle
        font-weight bold
        font-size 16px

    .auto-save-modal
        color white!important

    .modal-incorporacao-container
        padding 0px !important
        overflow-x hidden
        height 75vh

    @media (max-height: 870px)
        .item-patrimonio-tabela
            min-height 55vh

        .item-catalogo-tabela
            min-height 58vh

    @media (max-height: 780px)
        .item-patrimonio-tabela
            min-height 49vh

        .item-catalogo-tabela
            min-height 52vh

</style>
