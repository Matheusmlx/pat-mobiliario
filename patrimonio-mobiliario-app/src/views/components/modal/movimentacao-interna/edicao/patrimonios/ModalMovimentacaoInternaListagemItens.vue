<template>
    <v-dialog persistent v-model="dialog" width="800">
        <v-card>
            <v-card-title
                class="body-2  primary white--text"
                primary-title>
                Selecione os Patrimônios
                <v-spacer/>
                <v-btn min-width="10px" class="pa-1 white--text" text @click="fecharModal">
                    <v-icon small>fas fa-times</v-icon>
                </v-btn>
            </v-card-title>
            <az-toolbar class="search-listagem-itens-movimentacao">
                <az-search
                    :filter="filtrosInterno"
                    @remove-filter="tratarEventoRemoverFiltro"
                    :maxlengthInput="500"
                    @simple-search="tratarEventoBuscaSimples"
                    simple-search-placeholder="Busque por: descrição, número do patrimônio ou código da incorporação"
                    slot="simpleSearch"
                    class="searchItensCatalogoNovo"/>
            </az-toolbar>

            <v-responsive class="overflow-y-auto" max-height="70vh">
                <v-data-table
                    v-model="movimentacaoInterna.patrimoniosId"
                    :headers="colunasTabela"
                    :items="patrimonios"
                    ref="table"
                    :options.sync="paginacaoInterna"
                    :server-items-length="totalItens"
                    class="pr-tabela-listagem-itens-movimentacao-interna"
                    hide-default-footer
                    item-key="patrimonioId"
                    show-select
                    no-data-text="Não há patrimônios a serem adicionados">
                    <template v-slot:header.patrimonioNumero>
                        <label-personalizado campo="numero" :tela="nomeTelaPatrimonio" apenasNome/>
                    </template>
                    <template v-slot:header.patrimonioDescricao>
                        <label-personalizado campo="descricao" :tela="nomeTela" apenasNome/>
                    </template>
                    <template v-slot:header.incorporacaoCodigo>
                        <label-personalizado campo="incorporacao" :tela="nomeTela" apenasNome/>
                    </template>
                    <template v-slot:item.patrimonioNumero="{item}">
                        <span class="grey--text text--darken-2 text-truncate max-8">{{item.patrimonioNumero}}</span>
                    </template>
                    <template v-slot:item.patrimonioDescricao="{item}">
                        <v-tooltip top nudge-top max-width="600" open-delay="500">
                            <template v-slot:activator="{ on }">
                                <div v-on="on">
                                    <span class="grey--text text--darken-2 d-inline-block text-truncate max-18">{{item.patrimonioDescricao | textoSemValor}}</span>
                                </div>
                            </template>
                            {{item.patrimonioDescricao}}
                        </v-tooltip>
                    </template>
                    <template v-slot:item.incorporacaoCodigo="{item}">
                        <span class="grey--text text--darken-2 d-inline-block max-10">{{item.incorporacaoCodigo | textoSemValor}}</span>
                    </template>
                </v-data-table>

                <paginacao class="ml-5 mr-5" :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>

                <v-divider></v-divider>
                <v-card-actions>
                    <v-col md="12" sm="12" xl="12" xs="12" class="pa-0">
                        <acoes-botoes-adicionar-cancelar
                            :controleContinuar="quantidadeSelecionados !== 0"
                            :quantidade="quantidadeSelecionados"
                            @cancelar="fecharModal"
                            @adicionar="tratarEventoAdicionar"
                            class="mt-3 pb-3"/>
                    </v-col>
                </v-card-actions>
            </v-responsive>
        </v-card>
    </v-dialog>
</template>

<script>
    import _ from 'lodash'
    import {mapActions, mapMutations} from 'vuex'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import AcoesBotoesAdicionarCancelar from '@/views/components/acoes/AcoesBotoesAdicionarCancelar'

    export default {
        name: 'ModalMovimentacaoInternaListagemItens',
        components: {AcoesBotoesAdicionarCancelar, Paginacao, LabelPersonalizado},
        data() {
            return {
                dialog: true,
                patrimonios: [],
                paginas: 0,
                totalItens: 0,
                filtrosInterno: this.getFiltros(),
                paginacaoInterna: this.getPaginacao(),
                movimentacaoInterna:{movimentacaoInternaId: this.$route.params.movimentacaoInternaId, patrimoniosId:[]},
                quantidadeSelecionados: 0,
                nomeTela: 'MOVIMENTACAO_INTERNA_ITENS',
                nomeTelaPatrimonio: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO',
                colunasTabela: []
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.tratarEventoPaginar(novoValor)
                },
                deep: true,
            },
            'movimentacaoInterna.patrimoniosId'(){
                this.calculaQuantidadePatrimoniosSelecionados()
            }
        },
        async mounted() {
            this.colunasTabela = await this.obterColunasTabela()
            await this.buscar()
        },
        methods:{
            ...mapActions([
                actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA,
                actionTypes.MOVIMENTACAO_INTERNA.ITEM.ADICIONAR_ITENS_MOVIMENTACAO_INTERNA,
                actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA
            ]),
            ...mapMutations([
                mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA,
                mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA,
                mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_SELECAO_MOVIMENTACAO_INTERNA
            ]),
            async buscar() {
                this.setFiltrosBuscaTodosItensParaSelecaoMovimentacaoInterna(this.getFiltrosInterno())
                await this.buscaTodosPatrimonios()
            },
            async buscaTodosPatrimonios() {
                const resultado = await this.buscarTodosItensParaSelecaoMovimentacaoInterna(this.$route.params.movimentacaoInternaId)
                if (resultado) {
                    this.patrimonios = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                }
            },
            filtrarPatrimonios(patrimonios) {
                return patrimonios.filter(patrimonio => {
                    return !(this.movimentacaoInterna.patrimoniosNaoConsiderar.includes(patrimonio.patrimonioId))
                })
            },
            tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                this.buscar()
            },
            tratarEventoBuscaSimples(valor) {
                this.resetaPage()
                this.filtrosInterno.conteudo.value = valor
                this.buscar()
            },
            tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosItensParaSelecaoMovimentacaoInterna(paginacao)
                this.resetarPaginacaoSortBy(paginacao)
                this.buscar()
            },
            resetaPage(){
                this.resetaPageBuscaTodosItensParaSelecaoMovimentacaoInterna()
            },
            resetarPaginacaoSortBy(paginacao) {
                if (!paginacao.sortBy[0]) {
                    this.$store.state.movimentacaoInternaItem.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            },
            async fecharModal() {
                this.resetaPage()
                this.buscarItensAdicionados()
                await this.$router.replace({
                    name: 'MovimentacaoInternaEdicaoItens',
                    params: {movimentacaoInternaId: this.$route.params.movimentacaoInternaId}
                })
            },
            async tratarEventoAdicionar(){
                const movimentacao = _.cloneDeep(this.movimentacaoInterna)
                movimentacao.patrimoniosId = this.filtraIdPatrimoniosSelecionados()
                await this.adicionarItensMovimentacaoInterna(movimentacao)
                await this.fecharModal()
            },
            filtraIdPatrimoniosSelecionados() {
                let patrimonios = []
                this.movimentacaoInterna.patrimoniosId.forEach(item => {
                    patrimonios.push(item.patrimonioId)
                })
                return patrimonios
            },
            calculaQuantidadePatrimoniosSelecionados(){
                this.quantidadeSelecionados = this.movimentacaoInterna.patrimoniosId.length
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.movimentacaoInternaItem.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.filtros)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            getPaginacao(){
                return _.cloneDeep(this.$store.state.movimentacaoInternaItem.resultadoBuscaTodosItensParaSelecaoMovimentacaoInterna.paginacao)
            },
            buscarItensAdicionados(){
                this.$emit('buscarItensAdicionados')
            },
            async obterTipoMovimentacaoInterna() {
                return this.buscarTipoMovimentacaoInterna(this.$route.params.movimentacaoInternaId)
            },
            async obterColunasTabela() {
                const tipoMovimentacaoInterna = await this.obterTipoMovimentacaoInterna()
                if (tipoMovimentacaoInterna === 'DISTRIBUICAO') {
                    return this.criarColunas(4,
                                             [],
                                             ['patrimonioNumero','patrimonioDescricao','incorporacaoCodigo','acoes'],
                                             [false,false,false,false],
                                             ['left','left','left','right'],
                                             ['15%','50%','15%','20%'],
                                             'gray--text')
                }
                return this.criarColunas(2,
                                         [],
                                         ['patrimonioNumero','patrimonioDescricao'],
                                         [false,false],
                                         ['left','left'],
                                         ['15%','85%'],
                                         'gray--text')
            }
        }
    }
</script>

<style scoped lang="stylus">
.modal-estornar-listagem
    max-height 50vh

.max-18
    max-width 420px

.search-listagem-itens-movimentacao .input-search
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
    .search-listagem-itens-movimentacao .input-search
        span
            max-width 230px

@media (max-width 720px)
    .search-listagem-itens-movimentacao .input-search
        background-color #fff !important
        width 50vw !important

        span
            max-width 210px

.search-listagem-itens-movimentacao
    display flex
    align-items center
    justify-content center
    background-color white !important

>>>
.pr-tabela-listagem-itens-movimentacao-interna
    th
        font-size 13px !important
        font-weight bold
        color #666666 !important

    tr.v-data-table__selected
        background white !important

    tr.v-data-table__selected td .v-icon
        color grey !important

</style>
