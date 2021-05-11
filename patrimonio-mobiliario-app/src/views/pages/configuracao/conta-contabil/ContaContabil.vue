<template>
    <div>
        <az-toolbar>
            <az-search
                :filter="filtros"
                @remove-filter="removerFiltro"
                :maxlengthInput="500"
                @simple-search="buscaSimples"
                simple-search-placeholder="Busque por: conta ou código contábil"
                slot="simpleSearch"
                class="searchContaContabil"/>
        </az-toolbar>
        <conta-contabil-listagem
            v-model="itens"
            :todasContasContabeis=contaContabil.todasContasContabeis
            :colunasTable="colunas"
            :paginacao="contaContabil.todasContasContabeis.paginacao"
            :paginas="paginas"
            :total-itens="totalElements"
            @paginar="paginar"
            @salvar="tratarEventoSalvar"
            @cancelarEdicao="cancelarEdicao"/>
    </div>
</template>

<script>
    import ContaContabilListagem from './listagem/ContaContabilListagem'
    import {createNamespacedHelpers, mapState} from 'vuex'
    import {actionTypes} from '@/core/constants'
    import _ from 'lodash'

    const { mapMutations, mapActions } = createNamespacedHelpers('contaContabil')
    const { mapGetters } = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        name: 'ContaContabil',
        components: { ContaContabilListagem },
        data() {
            return {
                filtros: {},
                itens: [],
                paginas: 0,
                totalElements: 0,
                paginacaoInterna:0
            }
        },
        computed: {
            ...mapState(['contaContabil']),
            colunas(){
                return this.criarColunas(7,
                                         [],
                                         ['codigoContabil','contaContabil','tipo','tipoBem','vidaUtil','percentualResidual','acao'],
                                         [false,false,false,false,false,false,false],
                                         ['left','left','left','left','left','left','left'],
                                         ['10%','34%','12%','12%','12%','14%','6%'],
                                         'gray--text')
            }
        },
        mounted() {
            this.getFiltros()
        },
        methods: {
            ...mapGetters(['getRotulosPersonalizados']),
            ...mapActions([
                actionTypes.CONFIGURACAO.CONTA_CONTABIL.BUSCAR_TODAS_CONTAS_CONTABEIS,
                actionTypes.CONFIGURACAO.CONTA_CONTABIL.EDITAR_CONTA_CONTABIL,
                actionTypes.CONFIGURACAO.CONTA_CONTABIL.SALVAR_CONTA_CONTABIL
            ]),
            ...mapMutations(['setFiltrosBuscaTodasContasContabeis','setPaginacaoBuscaTodasContasContabeis', 'resetaPageContaContabil']),
            async buscarFiltros() {
                this.filtrosInterno = await _.cloneDeep(this.contaContabil.todasContasContabeis.filtros)
            },
            async buscar() {
                this.setFiltrosBuscaTodasContasContabeis(this.getFiltrosInterno())
                this.setMensagemLoading('Carregando....')
                const responseApi = await this.buscarTodasContasContabeis()
                this.itens = responseApi.items
                this.paginas = responseApi.totalPages
                this.totalElements = responseApi.totalElements
            },
            getFiltros() {
                this.filtros = _.cloneDeep(
                    this.contaContabil.todasContasContabeis.filtros
                )
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtros)
            },
            async buscaSimples(valor) {
                this.resetaPageContaContabil()
                this.filtros.conteudo.value = valor
                await this.buscar()
            },
            async removerFiltro(propriedade) {
                if (this.filtros[propriedade]) {
                    this.filtros[propriedade].value = this.filtros[propriedade].default
                }
                await this.buscar()
            },
            async paginar(paginacao){
                this.setPaginacaoBuscaTodasContasContabeis(paginacao)
                await this.buscar()
            },
            async tratarEventoSalvar(contaContabil) {
                try {
                    await this.salvarContaContabil(contaContabil)
                } catch (err) {
                    this.mostrarNotificacaoErro(err.response.data.message)
                }
                await this.buscar()
            },
            async cancelarEdicao() {
                await this.buscar()
            }
        },
    }
</script>

<style lang="stylus">
    .searchContaContabil .input-search
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
        .searchContaContabil .input-search
            background-color #fff !important
            width 30vw !important

            span
                max-width 230px

    @media (max-width 890px)
        .searchContaContabil .input-search
            background-color #fff !important
            width 35vw !important

    @media (max-width 720px)
        .searchContaContabil .input-search
            background-color #fff !important
            width 55vw !important
</style>
