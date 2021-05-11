<template>
    <div>
        <v-row class="pl-5">
            <incorporacao-listagem-patrimonios-cabecalho :item="incorporacaoItem"/>
        </v-row>
        <v-divider></v-divider>
        <div class="container-tabela-modal">
            <tipo-veiculo-tabela
                v-if="tipoVeiculo"
                :itens="itens"
                :paginas="paginas"
                :total-itens="totalItens"
                :paginacao="$store.state.patrimonio.resultadoBuscaTodosPatrimonios.paginacao"
                @paginar="tratarEventoPaginar"
                @atualizarPatrimonio="(patrimonio) => atualizarPatrimonioItem(patrimonio)"/>
            <tipo-movel-tabela
                v-if="tipoMovel"
                :itens="itens"
                :paginas="paginas"
                :total-itens="totalItens"
                :paginacao="$store.state.patrimonio.resultadoBuscaTodosPatrimonios.paginacao"
                @paginar="tratarEventoPaginar"
                @atualizarPatrimonio="(patrimonio) => atualizarPatrimonioItem(patrimonio)"/>
            <tipo-equipamento-armamento-tabela
                v-if="tipoEquipamentoArmamento"
                :itens="itens"
                :paginas="paginas"
                :total-itens="totalItens"
                :paginacao="$store.state.patrimonio.resultadoBuscaTodosPatrimonios.paginacao"
                @paginar="tratarEventoPaginar"
                @atualizarPatrimonio="(patrimonio) => atualizarPatrimonioItem(patrimonio)"
            />
        </div>
    </div>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapMutations, mapState} from 'vuex'
    import IncorporacaoListagemPatrimoniosCabecalho from './IncorporacaoListagemPatrimoniosCabecalho'
    import TipoMovelTabela from './tipoMovel/TipoMovelTabela'
    import TipoEquipamentoArmamentoTabela from './tipoEquipamentoArmamento/TipoEquipamentoArmamentoTabela'
    import TipoVeiculoTabela from './tipoVeiculo/TipoVeiculoTabela'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        name: 'IncorporacaoListagemPatrimonios',
        components: {
            TipoVeiculoTabela,
            TipoEquipamentoArmamentoTabela,
            TipoMovelTabela,
            IncorporacaoListagemPatrimoniosCabecalho
        },
        data() {
            return {
                incorporacaoItem: {},
                itens: [],
                paginas: 0,
                totalItens: 0,
                itemIncorporacaoId: null,
                incorporacaoId: null
            }
        },
        computed:{
            tipoMovel() {
                return this.incorporacaoItem.tipoBem === 'MOVEL'
            },
            tipoEquipamentoArmamento() {
                return this.incorporacaoItem.tipoBem === 'EQUIPAMENTO' || this.incorporacaoItem.tipoBem === 'ARMAMENTO'
            },
            tipoVeiculo() {
                return this.incorporacaoItem.tipoBem === 'VEICULO'
            },
            ...mapState({todosPatrimonios: state => state.patrimonio.resultadoBuscaTodosPatrimonios})
        },
        async mounted() {
            this.setItemIncorporacaoId()
            this.setIncorporacaoId()
            await this.buscaItemIncorporacao()
            if (!this.verificaPermissaoEdicao()) {
                await this.etapaTresVisualizacao()
            }
        },
        watch: {
            async $route() {
                this.setItemIncorporacaoId()
                await this.buscaItemIncorporacao()
            },
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS,
                actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO,
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO
            ]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS,
                mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO,
                mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DESATIVAR_SALVAMENTO_AUTOMATICO
            ]),
            async atualizarPatrimonioItem(item) {
                this.desabilitarLoadingGlobal()
                try {
                    await this.atualizarPatrimonio(item)
                } catch (err) {
                    this.desativarSalvamentoAutomatico()
                    this.mostrarNotificacaoErro(err.response.data.message)
                    await this.buscaTodosPatrimonios()
                }
            },
            async buscaTodosPatrimonios() {
                if (this.itemIncorporacaoId) {
                    const resultado = await this.buscarTodosTodosPatrimonios(this.itemIncorporacaoId)
                    if (resultado) {
                        this.itens = resultado.items
                        this.paginas = resultado.totalPages
                        this.totalItens = resultado.totalElements
                    }
                }
            },
            async buscaItemIncorporacao() {
                if (this.itemIncorporacaoId && this.incorporacaoId) {
                    this.incorporacaoItem = await this.buscarItemIncorporacao({ idItem: this.itemIncorporacaoId, idIncorporacao: this.incorporacaoId})
                }
            },
            tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosPatrimonios(paginacao)
                this.resetarPaginacaoSortBy(paginacao)
                this.buscaTodosPatrimonios()
            },
            setItemIncorporacaoId() {
                if (this.$route.params.itemIncorporacaoId) {
                    this.itemIncorporacaoId = this.$route.params.itemIncorporacaoId
                }
            },
            setIncorporacaoId() {
                if (this.$route.params.incorporacaoId) {
                    this.incorporacaoId = this.$route.params.incorporacaoId
                }
            },
            async etapaTresVisualizacao(){
                await this.$router.push({
                    name: 'IncorporacaoItemListagemPatrimoniosVisualizacao',
                    params: {
                        itemIncorporacaoId: this.itemIncorporacaoId,
                        incorporacaoId: this.incorporacaoId
                    }
                })
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
            },
            resetarPaginacaoSortBy(paginacao) {
                if (!paginacao.sortBy[0]) {
                    this.todosPatrimonios.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            }
        }
    }
</script>

<style lang="stylus">
    .container-tabela-modal
        padding 0px 10px

    .tabela-modal-incorporacao-itens
        thead > tr > th
            font-size: 13px !important

        tr
            color #777
            height 55px

        td:hover
            cursor pointer

        .v-select__selection
            font-size 13px
            color #777

        td
            margin-top 15px !important
            padding 0 16px !important

        .v-input__slot
            font-size 0.8em !important
            max-height 65px !important

        .v-input__slot
            input
                color #777

        .tabela-modal-incorporacao-itens td:hover
            cursor pointer !important

    .paginationModal3
        padding 0 15px

    .max-10
        max-width 10vw

    .max-13
        max-width: 13vw
</style>
