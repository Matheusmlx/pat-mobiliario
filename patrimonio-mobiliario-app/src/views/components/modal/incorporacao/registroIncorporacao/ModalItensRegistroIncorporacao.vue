<template>
    <v-row justify="center">
        <v-dialog v-model="value" persistent max-width="1100" scrollable @keydown.esc="fecharModal">
            <v-card>
                <v-toolbar dark color="primary" class="styleToolBar">
                    <v-toolbar-title><span class="etapaStyle">Visualização de Item</span></v-toolbar-title>
                    <v-spacer/>
                    <az-auto-save v-if="$store.state.itemIncorporacao.autoSave.show" class="auto-save-modal"/>
                    <v-btn class="close__button" icon @click="fecharModal">
                        <v-icon size="20">close</v-icon>
                    </v-btn>
                </v-toolbar>
                <v-card-text class="container-item-registro">
                    <modal-itens-registro-incorporacao-dados v-if="dadosGerais.idIncorporacao && verificaPermissaoEdicao" :value="dadosGerais"/>
                    <modal-itens-registro-incorporacao-dados-visualizar v-if="dadosGerais.idIncorporacao && !verificaPermissaoEdicao" :value="dadosGerais"/>
                    <div class="container-item-tabela">
                    <modal-itens-registro-tabela-veiculo
                        v-if="tipoVeiculo"
                        :itens="patrimonios"
                        :paginas="paginas"
                        :total-itens="totalItens"
                        :paginacao="$store.state.patrimonio.resultadoBuscaTodosPatrimoniosRegistro.paginacao"
                        @paginar="tratarEventoPaginar"
                        @redirecionarFichaPatrimonio="redirecionarFichaPatrimonio"
                    />
                    <modal-itens-registro-tabela-movel
                        v-if="tipoMovel"
                        :itens="patrimonios"
                        :paginas="paginas"
                        :total-itens="totalItens"
                        :paginacao="$store.state.patrimonio.resultadoBuscaTodosPatrimoniosRegistro.paginacao"
                        @paginar="tratarEventoPaginar"
                        @redirecionarFichaPatrimonio="redirecionarFichaPatrimonio"
                    /><modal-itens-registro-tabela-equipamento-armamento
                        v-if="tipoEquipamentoArmamento"
                        :itens="patrimonios"
                        :paginas="paginas"
                        :total-itens="totalItens"
                        :paginacao="$store.state.patrimonio.resultadoBuscaTodosPatrimoniosRegistro.paginacao"
                        @paginar="tratarEventoPaginar"
                        @redirecionarFichaPatrimonio="redirecionarFichaPatrimonio"
                    />
                    </div>
                </v-card-text>
                <div>
                    <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage" class="mr-5 ml-5"/>
                </div>
            </v-card>
        </v-dialog>
    </v-row>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapMutations, mapState} from 'vuex'
    import ModalItensRegistroIncorporacaoDados from './ModalItensRegistroIncorporacaoDados'
    import ModalItensRegistroTabelaVeiculo from './ModalItensRegistroTabelaVeiculo'
    import ModalItensRegistroTabelaMovel from './ModalItensRegistroTabelaMovel'
    import ModalItensRegistroTabelaEquipamentoArmamento from './ModalItensRegistroTabelaEquipamentoArmamento'
    import ModalItensRegistroIncorporacaoDadosVisualizar from './ModalItensRegistroIncorporacaoDadosVisualizar'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        name: 'ModalItensRegistroIncorporacao',
        components: {
            ModalItensRegistroIncorporacaoDadosVisualizar,
            Paginacao,
            ModalItensRegistroIncorporacaoDados,
            ModalItensRegistroTabelaVeiculo,
            ModalItensRegistroTabelaMovel,
            ModalItensRegistroTabelaEquipamentoArmamento
        },
        data() {
            return {
                dadosGerais: {
                    idIncorporacao: null,
                    tipoBem: ''
                },
                paginacaoInterna: this.$store.state.patrimonio.resultadoBuscaTodosPatrimoniosRegistro.paginacao,
                patrimonios: [],
                paginas: 0,
                totalItens: 0,
                itemIncorporacaoId: null,
                incorporacaoId: null,
                value:true
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.tratarEventoPaginar(novoValor)
                },
                deep: true,
            },
        },
        computed:{
            tipoVeiculo() {
                return this.dadosGerais.tipoBem === 'VEICULO'
            },
            tipoMovel() {
                return this.dadosGerais.tipoBem === 'MOVEL'
            },
            tipoEquipamentoArmamento() {
                return this.dadosGerais.tipoBem === 'EQUIPAMENTO' || this.dadosGerais.tipoBem === 'ARMAMENTO'
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
            },
            ...mapState({todosPatrimonios: state => state.patrimonio.resultadoBuscaTodosPatrimoniosRegistro}),
        },
        async mounted() {
            this.setItemIncorporacaoId()
            this.setIncorporacaoId()
            await this.buscarItemDeIncorporacao()
            this.resetaPage()
            await this.buscaTodosPatrimonios()
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO_REGISTRO,
                actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_REGISTRO
            ]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_REGISTRO,
                mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_REGISTRO,
                mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO
            ]),
            async buscarItemDeIncorporacao() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.buscarItemIncorporacaoRegistro({id: this.itemIncorporacaoId})
                this.habilitarLoadingGlobal()
                this.dadosGerais = resultado
                this.setDadosGerais()
            },
            async buscaTodosPatrimonios() {
                if (this.itemIncorporacaoId) {
                    const resultado = await this.buscarTodosTodosPatrimoniosRegistro(this.itemIncorporacaoId)
                    if (resultado) {
                        this.patrimonios = resultado.items
                        this.paginas = resultado.totalPages
                        this.totalItens = resultado.totalElements
                    }
                }
            },
            tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosPatrimoniosRegistro(paginacao)
                this.resetarPaginacaoSortBy(paginacao)
                this.buscaTodosPatrimonios()
            },
            resetarPaginacaoSortBy(paginacao) {
                if (!paginacao.sortBy[0]) {
                    this.todosPatrimonios.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            },
            resetaPage() {
                this.resetaPagePatrimonioRegistro()
            },
            fecharModal() {
                this.desativarSalvamentoAutomatico()
                this.$router.push({
                    name: 'VisualizarRegistroIncorporacao',
                    params: {incorporacaoId: this.incorporacaoId}
                })
            },
            setDadosGerais(){
                this.dadosGerais.idIncorporacao = this.incorporacaoId
                this.dadosGerais.naturezaDespesa = null
                this.dadosGerais.estadoConservacao = null
            },
            setItemIncorporacaoId(){
                if (this.$route.params.itemIncorporacaoId) {
                    this.itemIncorporacaoId = this.$route.params.itemIncorporacaoId
                }
            },
            setIncorporacaoId(){
                if(this.$route.params.incorporacaoId){
                    this.incorporacaoId = this.$route.params.incorporacaoId
                }
            },
            async redirecionarFichaPatrimonio(item) {
                await this.$router.push({name: 'FichaPatrimonioDadosGerais', params: {patrimonioId: item.id}})
            }
        }
    }
</script>

<style scoped lang="stylus">

    .etapaStyle
        font-weight bold
        font-size 16px

    .auto-save-modal
        color white

    .container-item-registro
        padding 0px !important
        overflow-x hidden
        height 75vh

    .container-item-tabela
        padding 0px 10px

</style>
