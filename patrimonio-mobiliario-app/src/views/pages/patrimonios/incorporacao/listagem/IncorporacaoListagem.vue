<template>
    <div>
        <az-toolbar>
            <botao-novo
                slot="actions"
                :disabled="!verificarPermissaoEdicao"
                @click="tratarEventoNovaIncorporacao"
            />
            <v-col slot="simpleSearch">
                <az-search
                    id="azSearch"
                    :filter="filtrosInterno"
                    :maxlength-input="500"
                    class="searchIncorporacaoListagem"
                    simple-search-placeholder="Busque por: código, fornecedor, órgão ou situação"
                    @remove-filter="tratarEventoRemoverFiltro"
                    @simple-search="tratarEventoBuscaSimples"
                />
            </v-col>
        </az-toolbar>
        <incorporacao-listagem-tabela
            :itens="itens"
            :paginacao="todasIncorporacoes.paginacao"
            :paginas="paginas"
            :total-itens="totalItens"
            @acessar="tratarEventoAcessar"
            @paginar="tratarEventoPaginar"
        />
    </div>
</template>

<script>
    import _ from 'lodash'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import BotaoNovo from '@/views/components/botoes/BotaoNovo'
    import IncorporacaoListagemTabela from './IncorporacaoListagemTabela'
    import {createNamespacedHelpers, mapActions, mapMutations, mapState} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        name: 'IncorporacaoListagem',
        components: {BotaoNovo, IncorporacaoListagemTabela},
        data() {
            return {
                filtrosInterno: this.getFiltros(),
                itens: [],
                paginas: 0,
                totalItens: 0,
                nomeTela: 'INCORPORACAO_DADOS_GERAIS',
                loading: false,
            }
        },
        computed: {
            ...mapState({todasIncorporacoes: state => state.incorporacao.resultadoBuscaTodasIncorporacoes}),
            verificarPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Normal'])
            },
            ...mapGetters([
                'getIncorporacaoValidado',
            ]),
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_TODAS_INCORPORACOES,
                actionTypes.PATRIMONIO.INCORPORACAO.CADASTRAR_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS,
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO,
            ]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.INCORPORACAO.SET_FILTROS_BUSCA_TODAS_INCORPORACOES,
                mutationTypes.PATRIMONIO.INCORPORACAO.SET_PAGINACAO_BUSCA_TODAS_INCORPORACOES,
                mutationTypes.PATRIMONIO.INCORPORACAO.RESETA_PAGE_INCORPORACAO,
            ]),
            async buscar() {
                this.setFiltrosBuscaTodasIncorporacoes(this.getFiltrosInterno())
                await this.buscaTodasIncorporacoes()
            },
            async buscaTodasIncorporacoes() {
                const resultado = await this.buscarTodasIncorporacoes()
                if (resultado) {
                    this.itens = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                }
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.incorporacao.resultadoBuscaTodasIncorporacoes.filtros)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            async tratarEventoAcessar(item) {
                if (item.situacao !== 'EM_PROCESSAMENTO') {
                    if (item.situacao === 'EM_ELABORACAO') {
                        await this.tratarRedirecionamentosParaItemSituacaoEmElaboracao(item)
                    } else {
                        this.tratarRedirecionamentosParaItemSituacaoDiferenteDeElaboracao(item)
                    }
                }
            },
            async tratarRedirecionamentosParaItemSituacaoEmElaboracao(item) {
                if (await this.verificaSeCamposObrigatoriosPreenchidos(item)) {
                    if (await this.buscarItensIncorporacao(item.id)) {
                        this.verificarRotaIncorproacaoDocumentos(item.id)
                    } else {
                        this.verificarRotaItensIncorporacaoListagem(item.id)
                    }
                } else {
                    this.verificarRotaIncorporacao(item.id)
                }
            },
            tratarRedirecionamentosParaItemSituacaoDiferenteDeElaboracao(item) {
                if (item.situacao === 'ERRO_PROCESSAMENTO') {
                    this.verificarRotaIncorporacao(item.id)
                } else {
                    this.verificarRotaVisualizacaoRegistro(item.id)
                }
            },
            verificarRotaVisualizacaoRegistro(id) {
                if (this.verificarPermissaoEdicao) {
                    this.$router.push({name: 'VisualizarRegistroIncorporacao', params: {incorporacaoId: id}})
                } else {
                    this.$router.push({name: 'VisualizarRegistroIncorporacaoVisualizacao', params: {incorporacaoId: id}})
                }
            },
            verificarRotaIncorproacaoDocumentos(id) {
                if (this.verificarPermissaoEdicao) {
                    this.$router.push({name: 'IncorporacaoDocumentosEdicao', params: {incorporacaoId: id}})
                } else {
                    this.$router.push({name: 'VisualizarIncorporacaoDocumentos', params: {incorporacaoId: id}})
                }
            },
            verificarRotaItensIncorporacaoListagem(id) {
                if (this.verificarPermissaoEdicao) {
                    this.$router.push({name: 'ItensIncorporacaoListagem', params: {incorporacaoId: id}})
                } else {
                    this.$router.push({name: 'VisualizarItensIncorporacao', params: {incorporacaoId: id}})
                }
            },
            verificarRotaIncorporacao(id) {
                if (this.verificarPermissaoEdicao) {
                    this.$router.push({name: 'EditarIncorporacao', params: {incorporacaoId: id}})
                } else {
                    this.$router.push({name: 'VisualizarIncorporacao', params: {incorporacaoId: id}})
                }
            },
            async verificaSeCamposObrigatoriosPreenchidos(item) {
                if (item && item.reconhecimento) {
                    if (this.empenhoObrigatorio(item)) {
                        if (!await this.verificarSeTodosEmpenhosObrigatoriosEstaoPreenchidos(item)) {
                            return false
                        }
                    }
                    return this.getIncorporacaoValidado(item, item.origemConvenio, item.origemFundo,
                                                        item.origemProjeto, item.origemComodato,
                                                        this.notaObrigatorio(item), this.nomeTela)
                }
                return false
            },
            async buscarItensIncorporacao(id) {
                const resultado = await this.buscarTodosItensIncorporacao(id)
                if (resultado) {
                    return (resultado.totalElements > 0)
                }
                return false
            },
            empenhoObrigatorio(item) {
                return item.reconhecimento.empenho
            },
            notaObrigatorio(item) {
                return item.reconhecimento.notaFiscal
            },
            async verificarSeTodosEmpenhosObrigatoriosEstaoPreenchidos(item) {
                const empenhos = await this.buscaEmpenhos(item.id)
                if (empenhos.length === 0) {
                    return !this.empenhoObrigatorio(item)
                }
                return empenhos.every(empenho => empenho.numeroEmpenho && empenho.dataEmpenho && empenho.valorEmpenho && empenho.valorEmpenho !== 0)
            },
            async buscaEmpenhos(incorporacaoId) {
                let empenhos = []
                const resultado = await this.buscarTodosEmpenhos(incorporacaoId)
                if (resultado && resultado.items.length > 0) {
                    empenhos = resultado.items
                }
                return empenhos
            },
            async tratarEventoBuscaSimples(valor) {
                this.resetaPageIncorporacao()
                this.filtrosInterno.conteudo.value = valor
                await this.buscar()
            },
            async tratarEventoNovaIncorporacao() {
                const incorporacaoSalva = await this.cadastrarIncorporacao()
                await this.$router.push({name: 'EditarIncorporacao', params: {incorporacaoId: incorporacaoSalva.id}})
            },
            async tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodasIncorporacoes(paginacao)
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
                if (!this.todasIncorporacoes.paginacao.sortBy[0]) {
                    this.todasIncorporacoes.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            },
        },
    }
</script>
<style lang="stylus">
.searchIncorporacaoListagem .input-search
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
    .searchIncorporacaoListagem .input-search
        background-color #fff !important
        width 40vw !important

        span
            max-width 230px

@media (max-width 890px)
    .searchIncorporacaoListagem .input-search
        background-color #fff !important
        width 50vw !important

@media (max-width 720px)
    .searchIncorporacaoListagem .input-search
        background-color #fff !important
        width 55vw !important
</style>
