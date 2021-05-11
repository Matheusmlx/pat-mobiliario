<template>
    <div>
        <az-toolbar class="search-item-catalogo-edicao">
            <az-search
                :filter="filtrosInterno"
                @remove-filter="tratarEventoRemoverFiltro"
                :maxlengthInput="30"
                @simple-search="tratarEventoBuscaSimples"
                simple-search-placeholder="Busque por: código ou descrição"
                slot="simpleSearch"
                class="searchItensCatalogoEdicao"/>
        </az-toolbar>

        <itens-catalogo-modal-listagem
            v-model="itens"
            :paginas="paginas"
            :paginacao="todosItens.paginacao"
            :total-itens="totalItens"
            @paginar="tratarEventoPaginar"
        />
    </div>
</template>


<script>
    import ItensCatalogoModalListagem from './ItensCatalogoModalListagemVisualizacao'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import _ from 'lodash'
    import {mapActions, mapMutations, mapState} from 'vuex'

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
                incorporacaoId: null,
                itemIncorporacaoId: null
            }
        },
        computed: {
            ...mapState({todosItens: state => state.itemcatalogo.todosItens})
        },
        async mounted() {
            this.setIncorporacaoId()
            this.setItemIncorporacaoId()
            this.buscar()
            this.tratarEventoCamposAceitos()
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
            tratarEventoCamposAceitos() {
                if(this.itemIncorporacaoId && this.itemIncorporacaoId) this.$emit('verificarContinuar')
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
        }
    }
</script>

<style lang="stylus">
    .searchItensCatalogoEdicao .input-search
        background-color #fff !important
        width 30vw !important

    @media (max-width 720px)
        .searchItensCatalogoEdicao .input-search
            background-color #fff !important
            width 50vw !important

    .search-item-catalogo-edicao
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
