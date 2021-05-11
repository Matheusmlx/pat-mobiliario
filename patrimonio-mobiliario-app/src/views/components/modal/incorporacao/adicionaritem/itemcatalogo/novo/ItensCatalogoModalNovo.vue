<template>
    <div>
        <az-toolbar class="search-item-catalogo-novo">
            <az-search
                :filter="filtrosInterno"
                @remove-filter="tratarEventoRemoverFiltro"
                :maxlengthInput="500"
                @simple-search="tratarEventoBuscaSimples"
                simple-search-placeholder="Busque por: código ou descrição"
                slot="simpleSearch"
                class="searchItensCatalogoNovo"/>
        </az-toolbar>

        <itens-catalogo-modal-listagem
            v-model="itens"
            :paginas="paginas"
            :paginacao="todosItens.paginacao"
            :total-itens="totalItens"
            @camposAceitos="tratarEventoCamposAceitos"
            @paginar="tratarEventoPaginar"
        />
    </div>
</template>

<script>
    import ItensCatalogoModalListagem from './ItensCatalogoModalListagemNovo'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import _ from 'lodash'
    import {mapActions, mapMutations, mapState} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        name: 'ItensCatalogoModal',
        props: ['modalItens'],
        components: {ItensCatalogoModalListagem},
        data() {
            return {
                filtrosInterno: this.getFiltros(),
                dialog: true,
                itens: [],
                paginas: 0,
                totalItens: 0,
                incorporacaoId: null
            }
        },
        computed: {
            ...mapState({todosItens: state => state.itemcatalogo.todosItens})
        },
        mounted() {
            this.buscar()
            this.setIncorporacaoId()
            if (!this.verificaPermissaoEdicao()) {
                this.etapaUmVisualizacao()
            }
        },
        methods: {
            ...mapMutations([
                mutationTypes.ITEM_CATALOGO.SET_FILTROS_BUSCA_TODOS_ITENS_CATALOGO,
                mutationTypes.ITEM_CATALOGO.SET_PAGINACAO_BUSCA_TODOS_ITENS_CATALOGO,
                mutationTypes.ITEM_CATALOGO.RESETA_PAGE_CATALOGO
            ]),
            ...mapActions([
                actionTypes.ITEM_CATALOGO.BUSCAR_TODOS_ITENS_CATALOGO
            ]),
            tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                this.buscar()
            },
            async buscar() {
                this.setFiltrosBuscaTodosItensCatalogo(this.getFiltrosInterno())
                await this.buscarItens()
            },
            async buscarItens() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.buscarTodosItensCatalogo()
                this.habilitarLoadingGlobal()
                if (resultado) {
                    this.itens = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                    this.paginas++
                }
            },
            tratarEventoBuscaSimples(valor) {
                this.resetaPageCatalogo()
                this.filtrosInterno.conteudo.value = valor
                this.buscar()
            },
            tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosItensCatalogo(paginacao)
                this.resetarPaginacaoSortBy(paginacao)
                this.buscar()
            },
            resetarPaginacaoSortBy(paginacao) {
                if (!paginacao.sortBy[0]) {
                    this.todosItens.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.itemcatalogo.todosItens.filtros)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            tratarEventoCamposAceitos() {
                this.$emit('verificarContinuar')
            },
            removerCampo() {
                this.itens.splice(0, 1)
            },
            setIncorporacaoId() {
                if (this.$route.params.incorporacaoId) {
                    this.incorporacaoId = this.$route.params.incorporacaoId
                }
            },
            etapaUmVisualizacao() {
                this.$router.push({
                    name: 'ItensCatalogoModalVisualizacao',
                    params: {incorporacaoId: this.incorporacaoId}
                })
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
            },
        }
    }
</script>

<style lang="stylus">
    .searchItensCatalogoNovo .input-search
        background-color #fff !important
        width 30vw !important

        span
            max-width 350px

        .v-chip__content
            width 100%

            span
                white-space nowrap
                overflow hidden
                text-overflow ellipsis

    @media (max-width 1250px)
        .searchItensCatalogoEdicao .input-search
            span
                max-width 230px

    @media (max-width 720px)
        .searchItensCatalogoNovo .input-search
            background-color #fff !important
            width 50vw !important

            span
                max-width 210px

    .search-item-catalogo-novo
        display flex
        align-items center
        justify-content center

    .v-dialog
        overflow hidden

    .close__button::before
        background transparent

    .az-table-list
        .v-text-field__details
            display flex !important
            margin-top 2px

            th
                font-size 15px !important
                font-weight bold

            tr
                height 70px !important

    .input >>> .v-input__control
        background-color white

    .input >>> .v-input
        max-width 300px

    .v-data-table
        cursor pointer !important

        tbody tr:hover
            button
                visibility visible
                float right !important
                margin-top 3px
                margin-right 2%

        button
            visibility hidden
            text-align center !important


    .v-data-table tbody tr:after
        cursor pointer !important
</style>
