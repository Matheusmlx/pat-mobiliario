<template>
    <div>
        <az-toolbar>
            <botao-novo
                slot="actions"
                :disabled="!verificarPermissaoEdicao"
                @click="tratarEventoNovaMovimentacaoInterna"
            />
            <v-flex slot="simpleSearch">
                <az-search
                    :filter="filtrosInterno"
                    @remove-filter="tratarEventoRemoverFiltro"
                    @simple-search="tratarEventoBuscaSimples"
                    :maxlengthInput="500"
                    id="azSearch"
                    simple-search-placeholder="Busque por: código, tipo, origem, destino ou situação"
                    class="searchMovimentacaoInternaListagem"/>
            </v-flex>
        </az-toolbar>
        <movimentacao-interna-listagem-tabela
            :itens="itens"
            :paginas="paginas"
            :total-itens="totalItens"
            :paginacao="filtrosEPaginacao.paginacao"
            @acessar="tratarEventoAcessar"
            @paginar="tratarEventoPaginar"/>
    </div>
</template>

<script>
    import _ from 'lodash'
    import {createNamespacedHelpers, mapActions, mapMutations, mapState} from 'vuex'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import BotaoNovo from '@/views/components/botoes/BotaoNovo'
    import MovimentacaoInternaListagemTabela from './MovimentacaoInternaListagemTabela'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'MovimentacaoInternaListagem',
        components: {BotaoNovo, MovimentacaoInternaListagemTabela},
        data() {
            return {
                itens: [],
                paginas: 0,
                totalItens: 0,
                filtrosInterno: this.getFiltros(),
                nomeTelaDadosGerais: 'MOVIMENTACAO_INTERNA_DADOS_GERAIS',
                nomeTelaIncorporacao: 'INCORPORACAO_DADOS_GERAIS',
                nomeTelaDistribuicao: 'DISTRIBUICAO_DADOS_GERAIS'
            }
        },
        computed: {
            ...mapGetters(['getDistribuicaoValidado', 'getMovimentacaoInternaValidado', 'getTemporariaValidado']),
            ...mapState({filtrosEPaginacao: state => state.movimentacaointerna.resultadoBuscaTodasMovimentacoesInternas}),
            verificarPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Movimentacoes'])
            },
        },
        methods: {
            ...mapActions([
                actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TODAS_MOVIMENTACOES_INTERNAS,
                actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA
            ]),
            ...mapMutations([
                mutationTypes.MOVIMENTACAO_INTERNA.RESETA_PAGE_BUSCA_TODAS_MOVIMENTACOES_INTERNAS,
                mutationTypes.MOVIMENTACAO_INTERNA.SET_PAGINACAO_BUSCA_TODAS_MOVIMENTACOES_INTERNAS,
                mutationTypes.MOVIMENTACAO_INTERNA.SET_FILTROS_BUSCA_TODAS_MOVIMENTACOES_INTERNAS
            ]),
            async buscar() {
                this.setFiltrosBuscaTodasMovimentacoesInternas(this.getFiltrosInterno())
                await this.buscarTodasMovimentacoes()
            },
            async buscarTodasMovimentacoes() {
                const resultado = await this.buscarTodasMovimentacoesInternas()
                if (resultado) {
                    this.itens = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                }
            },
            async tratarEventoAcessar(item) {
                if(item.situacao !== 'EM_PROCESSAMENTO') {
                    if (item.situacao === 'EM_ELABORACAO') {
                        await this.tratarRedirecionamentosParaItemSituacaoEmElaboracao(item)
                    } else {
                        await this.tratarRedirecionamentosParaItemSituacaoDiferenteDeElaboracao(item)
                    }
                }
            },
            async tratarRedirecionamentosParaItemSituacaoEmElaboracao(item){
                if (await this.verificarSeCamposObrigatoriosPreenchidos(item)) {
                    if (await this.verificarSePossuiItensNaMovimentacao(item.id)) {
                        this.redirecionaParaDocumentos(item.id)
                    } else {
                        this.redirecionaParaItens(item.id)
                    }
                } else {
                    this.redirecionaParaDadosGerais(item.id)
                }
            },
            async tratarRedirecionamentosParaItemSituacaoDiferenteDeElaboracao(item){
                if(item.situacao === 'ERRO_PROCESSAMENTO') {
                    await this.redirecionaParaDadosGerais(item.id)
                }else{
                    this.redirecionaParaVisualizacaoMovimentacao(item.id)
                }
            },
            async verificarSeCamposObrigatoriosPreenchidos(item) {
                if(item.tipo === 'DISTRIBUICAO') {
                    return this.getDistribuicaoValidado(item, this.nomeTelaDadosGerais, this.nomeTelaIncorporacao, this.nomeTelaDistribuicao)
                } else if(['ENTRE_ESTOQUES', 'ENTRE_SETORES', 'DEVOLUCAO_ALMOXARIFADO'].includes(item.tipo)) {
                    return this.getMovimentacaoInternaValidado(item)
                } else if(item.tipo === 'TEMPORARIA') {
                    return this.getTemporariaValidado(item)
                }
                return false
            },
            async verificarSePossuiItensNaMovimentacao(id) {
                const resultado = await this.buscarQuantidadeItensAdicionadosMovimentacaoInterna(id)
                return resultado ? (resultado.quantidadeItens > 0) : false
            },
            redirecionaParaVisualizacaoMovimentacao(id) {
                this.$router.push({name: 'VisualizarRegistroMovimentacaoInterna', params: {movimentacaoInternaId: id}})
            },
            redirecionaParaDocumentos(id) {
                this.$router.replace({name: 'MovimentacaoInternaEdicaoDocumentos', params: {movimentacaoInternaId: id}})
            },
            redirecionaParaItens(id) {
                this.$router.replace({name: 'MovimentacaoInternaEdicaoItens', params: {movimentacaoInternaId: id}})
            },
            redirecionaParaDadosGerais(id) {
                this.$router.replace({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: id}})
            },
            async tratarEventoNovaMovimentacaoInterna() {
                await this.$router.replace({name: 'MovimentacaoInternaNovoTipo'})
            },
            async tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                await this.buscar()
            },
            async tratarEventoBuscaSimples(valor) {
                this.resetaPage()
                this.filtrosInterno.conteudo.value = valor
                await this.buscar()
            },
            async tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodasMovimentacoesInternas(paginacao)
                this.resetarPaginacaoSortBy(paginacao)
                await this.buscar()
            },
            resetarPaginacaoSortBy(paginacao) {
                if (!paginacao.sortBy[0]) {
                    this.$store.state.movimentacaointerna.resultadoBuscaTodasMovimentacoesInternas.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.movimentacaointerna.resultadoBuscaTodasMovimentacoesInternas.filtros)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            resetaPage(){
                this.resetaPageBuscaTodasMovimentacoesInternas()
            },
        }
    }
</script>

<style lang="stylus">
.searchMovimentacaoInternaListagem .input-search
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
    .searchMovimentacaoInternaListagem .input-search
        background-color #fff !important
        width 40vw !important

        span
            max-width 230px

@media (max-width 890px)
    .searchMovimentacaoInternaListagem .input-search
        background-color #fff !important
        width 50vw !important

@media (max-width 720px)
    .searchMovimentacaoInternaListagem .input-search
        background-color #fff !important
        width 55vw !important
</style>
