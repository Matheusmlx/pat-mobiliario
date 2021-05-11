<template>
    <div>
        <az-toolbar>
            <v-flex slot="simpleSearch">
                <az-search
                    :filter="filtrosInterno"
                    @remove-filter="tratarEventoRemoverFiltro"
                    @simple-search="tratarEventoBuscaSimples"
                    :maxlengthInput="500"
                    id="azSearch"
                    simple-search-placeholder="Busque por: Patrimônio, órgão, setor ou situação"
                    class="searchPatrimonioListagem"/>
            </v-flex>
        </az-toolbar>
        <patrimonios-listagem-tabela
            :itens="itens"
            :paginas="paginas"
            :total-itens="totalItens"
            :paginacao="todosPatrimonios.paginacao"
            @acessar="tratarEventoAcessar"
            @paginar="tratarEventoPaginar"/>
    </div>
</template>

<script>
    import _ from 'lodash'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapMutations, mapState} from 'vuex'
    import PatrimoniosListagemTabela from './PatrimoniosListagemTabela'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        name: 'PatrimoniosListagem',
        components: {PatrimoniosListagemTabela},
        data() {
            return {
                filtrosInterno: this.getFiltros(),
                itens: [],
                paginas: 0,
                totalItens: 0,
            }
        },
        computed: {
            ...mapState({todosPatrimonios: state => state.patrimonio.resultadoBuscaTodosPatrimoniosListagem}),
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_LISTAGEM
            ]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_LISTAGEM,
                mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_LISTAGEM,
                mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_LISTAGEM
            ]),
            async buscar() {
                this.setFiltrosBuscaTodosPatrimoniosListagem(this.getFiltrosInterno())
                await this.buscaTodosPatrimonios()
            },
            async buscaTodosPatrimonios() {
                const resultado = await this.buscarTodosPatrimoniosListagem()
                if (resultado) {
                    this.itens = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                }
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.patrimonio.resultadoBuscaTodosPatrimoniosListagem.filtros)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            tratarEventoAcessar(item) {
                const id = item.id
                if (this.verificaPermissaoEdicao()) {
                    this.$router.push({name: 'FichaPatrimonioDadosGerais', params: {patrimonioId: id}})
                }else{
                    this.$router.push({name: 'FichaPatrimonioDadosGeraisVisualizacao', params: {patrimonioId: id}})
                }
            },
            async tratarEventoBuscaSimples(valor) {
                this.resetaPagePatrimonioListagem()
                this.filtrosInterno.conteudo.value = valor
                await this.buscar()
            },
            async tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosPatrimoniosListagem(paginacao)
                this.verificarOrdenacao(paginacao)
                await this.buscar()
            },
            async tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                await this.buscar()
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
            },
            verificarOrdenacao(paginacao) {
                if(!this.todosPatrimonios.paginacao.sortBy[0]) {
                    this.todosPatrimonios.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            }
        }
    }
</script>
<style lang="stylus">
    .searchPatrimonioListagem .input-search
        background-color #fff !important
        width 28vw !important

        span
            max-width 350px

        .v-chip__content
            width 100%

            span
                white-space nowrap
                overflow hidden
                text-overflow ellipsis

    @media (max-width 1250px)
        .searchPatrimonioListagem .input-search
            background-color #fff !important
            width 40vw !important

            span
                max-width 230px

    @media (max-width 890px)
        .searchPatrimonioListagem .input-search
            background-color #fff !important
            width 50vw !important

    @media (max-width 720px)
        .searchPatrimonioListagem .input-search
            background-color #fff !important
            width 55vw !important
</style>
