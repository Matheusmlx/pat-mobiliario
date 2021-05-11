<template>
    <div>
        <az-toolbar>
            <botao-novo slot="actions" @click="tratarEventoNovoConvenio"/>

            <v-flex slot="simpleSearch">
                <az-search
                    :filter="filtrosInterno"
                    @remove-filter="tratarEventoRemoverFiltro"
                    @simple-search="tratarEventoBuscaSimples"
                    :maxlengthInput="500"
                    id="azSearch"
                    class="searchConvenio"
                    simple-search-placeholder="Busque por: n°, Nome, concedente, situação ou fonte de recurso"/>
            </v-flex>
        </az-toolbar>
        <convenio-listagem-tabela
            :itens="itens"
            :paginas="paginas"
            :total-itens="totalItens"
            :paginacao="$store.state.convenio.resultadoBuscaTodosConvenios.paginacao"
            @acessar="tratarEventoAcessar"
            @paginar="tratarEventoPaginar"
        />
    </div>
</template>

<script>
    import _ from 'lodash'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import BotaoNovo from '@/views/components/botoes/BotaoNovo'
    import ConvenioListagemTabela from './ConvenioListagemTabela'
    import {mapActions, mapMutations, mapState} from 'vuex'

    export default {
        name: 'ConvenioListagem',
        components: {
            BotaoNovo,
            ConvenioListagemTabela
        },
        data() {
            return {
                filtrosInterno: this.getFiltros(),
                itens: [],
                paginas: 0,
                totalItens: 0,
                idItemParaDeletar: null
            }
        },
        computed: {
            ...mapState({todosConvenios: state => state.convenio.resultadoBuscaTodosConvenios})
        },
        methods: {
            ...mapActions([actionTypes.CONFIGURACAO.CONVENIO.BUSCAR_TODOS_CONVENIOS]),
            ...mapMutations([
                mutationTypes.CONFIGURACAO.CONVENIO.SET_FILTROS_BUSCA_TODOS_CONVENIOS,
                mutationTypes.CONFIGURACAO.CONVENIO.SET_PAGINACAO_BUSCA_TODOS_CONVENIOS,
                mutationTypes.CONFIGURACAO.CONVENIO.RESETA_PAGE_CONVENIO
            ]),
            async buscar() {
                this.setFiltrosBuscaTodosConvenios(this.getFiltrosInterno())
                await this.buscaTodosConvenios()
            },
            async buscaTodosConvenios() {
                const resultado = await this.buscarTodosConvenios()
                if (resultado) {
                    this.itens = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                }
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.convenio.resultadoBuscaTodosConvenios.filtros)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            tratarEventoAcessar(item) {
                const id = item.id
                this.$router.push({name: 'ConvenioEdicao', params: {id}})
            },
            async tratarEventoBuscaSimples(valor) {
                this.resetaPageConvenio()
                this.filtrosInterno.conteudo.value = valor
                await this.buscar()
            },
            tratarEventoNovoConvenio() {
                this.$router.push({name: 'ConvenioCadastro'})
            },
            async tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosConvenios(paginacao)
                this.verificarOrdenacao(paginacao)
                await this.buscar()
            },
            async tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                await this.buscar()
            },
            verificarOrdenacao(paginacao) {
                if(!this.todosConvenios.paginacao.sortBy[0]) {
                    this.todosConvenios.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            }
        }
    }
</script>
<style lang="stylus">
    .searchConvenio .input-search
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
        .searchConvenio .input-search
            background-color #fff !important
            width 45vw !important

            span
                max-width 230px

    @media (max-width 1000px)
        .searchConvenio .input-search
            background-color #fff !important
            width 50vw !important

    @media (max-width 890px)
        .searchConvenio .input-search
            background-color #fff !important
            width 62vw !important
</style>
