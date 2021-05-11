<template>
    <v-dialog persistent v-model="dialog" width="800">
        <v-card>
            <v-card-title
                class="body-2 primary white--text"
                primary-title>
                Selecione os Patrimônios
                <v-spacer/>
                <v-btn min-width="10px" class="pa-1 white--text" text @click="fecharModal">
                    <v-icon small>fas fa-times</v-icon>
                </v-btn>
            </v-card-title>
            <az-toolbar class="search-listagem-patrimonios-devolucao">
                <az-search
                    :filter="filtrosInterno"
                    @remove-filter="tratarEventoRemoverFiltro"
                    :maxlengthInput="500"
                    @simple-search="tratarEventoBuscaSimples"
                    simple-search-placeholder="Busque por: número do patrimônio ou descrição"
                    slot="simpleSearch"
                    class="searchItensCatalogoNovo"/>
            </az-toolbar>

            <v-responsive class="overflow-y-auto" max-height="70vh">
                <v-data-table
                    v-model="movimentacaoInterna.patrimoniosId"
                    :headers="colunas"
                    :items="patrimonios"
                    ref="table"
                    :options.sync="paginacaoInterna"
                    :server-items-length="totalItens"
                    class="pr-tabela-listagem-itens-movimentacao-interna"
                    hide-default-footer
                    item-key="patrimonioId"
                    show-select
                    no-data-text="Não há patrimônios a serem adicionados">
                    <template v-slot:header.data-table-select="{ on, props }">
                        <v-simple-checkbox :ripple="false" color="grey" v-bind="props" v-on="on" @click="selecionaTodosPatrimoniosTodasPaginas(props)"/>
                    </template>
                    <template v-slot:item.data-table-select="{ isSelected, item }">
                        <v-simple-checkbox :ripple="false" :value="isSelected" @click="isSelected ? desselecionarPatrimonio(item): selecionarPatrimonio(item)"/>
                    </template>
                    <template v-slot:header.patrimonioNumero>
                        <label-personalizado campo="numero" :tela="nomeTelaPatrimonio" apenasNome/>
                    </template>
                    <template v-slot:header.patrimonioDescricao>
                        <label-personalizado campo="descricao" :tela="nomeTela" apenasNome/>
                    </template>
                    <template v-slot:item.patrimonioNumero="{item}">
                        <span class="grey--text text--darken-2 text-truncate max-8">{{item.patrimonioNumero}}</span>
                    </template>
                    <template v-slot:item.patrimonioDescricao="{item}">
                        <v-tooltip top nudge-top="" max-width="600">
                            <template v-slot:activator="{ on }">
                                <div v-on="on">
                                    <span class="grey--text text--darken-2 d-inline-block text-truncate max-18">{{item.patrimonioDescricao | textoSemValor}}</span>
                                </div>
                            </template>
                            {{item.patrimonioDescricao}}
                        </v-tooltip>
                    </template>
                </v-data-table>

                <paginacao class="ml-5 mr-5" :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>

                <v-divider></v-divider>
                <v-card-actions>
                    <v-col md="12" sm="12" xl="12" xs="12" class="pa-0">
                        <acoes-botoes-devolver-parcial-cancelar
                            :controleContinuar="quantidadeSelecionados !== 0"
                            :quantidade="quantidadeSelecionados"
                            @cancelar="fecharModal"
                            @devolverParcial="tratarEventoDevolver"
                            class="mt-3 pb-3"/>
                    </v-col>
                </v-card-actions>
            </v-responsive>
        </v-card>
    </v-dialog>
</template>

<script>
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapMutations, mapActions} from 'vuex'
    import _ from 'lodash'
    import AcoesBotoesDevolverParcialCancelar from '@/views/components/acoes/AcoesBotoesDevolverParcialCancelar'

    export default {
        name: 'ModalDevolverPatrimonios',
        components: {
            LabelPersonalizado,
            Paginacao,
            AcoesBotoesDevolverParcialCancelar
        },
        data() {
            return {
                dialog: true,
                patrimonios: [],
                movimentacaoInterna: {
                    id: this.$route.params.movimentacaoInternaId,
                    patrimoniosId: []
                },
                paginas: 0,
                totalItens: 0,
                quantidadeSelecionados: 0,
                filtrosInterno: this.getFiltros(),
                paginacaoInterna: this.getPaginacao(),
                filtroAtivo: false,
                nomeTela: 'MOVIMENTACAO_INTERNA_ITENS',
                nomeTelaPatrimonio: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO'
            }
        },
        computed: {
            colunas(){
                return this.criarColunas(2,
                                         [],
                                         ['patrimonioNumero','patrimonioDescricao'],
                                         [false,false],
                                         ['left','left'],
                                         ['15%','85%'],
                                         'gray--text')
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
        mounted() {
            this.buscar()
        },
        methods: {
            ...mapActions([
                actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.BUSCAR_TODOS_ITENS_PARA_DEVOLUCAO,
                actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.DEVOLVER_ITENS_PARCIAL,
                actionTypes.MOVIMENTACAO_INTERNA.TEMPORARIA.DEVOLVER_TODOS_PATRIMONIOS
            ]),
            ...mapMutations([
                mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO,
                mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO,
                mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.DEVOLUCAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_PARA_DEVOLUCAO
            ]),
            getFiltros() {
                return _.cloneDeep(this.$store.state.movimentacaoInternaVisualizacaoRegistroItem.resultadoBuscaTodosPatrimoniosParaDevolucao.filtros)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            getPaginacao() {
                return _.cloneDeep(this.$store.state.movimentacaoInternaVisualizacaoRegistroItem.resultadoBuscaTodosPatrimoniosParaDevolucao.paginacao)
            },
            async buscar() {
                this.setFiltrosBuscaTodosItensParaDevolucao(this.getFiltrosInterno())
                await this.buscaTodosPatrimonios()
                this.verificarSeFiltroAtivo()
            },
            async buscaTodosPatrimonios() {
                const resultado = await this.buscarTodosItensParaDevolucao(this.$route.params.movimentacaoInternaId)
                if (resultado) {
                    this.patrimonios = resultado.itens
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                    this.quantidadeItensTodasPaginas = resultado.totalElements
                    this.selecionaTodosPatrimoniosSeNecessario()
                }
            },
            selecionaTodosPatrimoniosSeNecessario(){
                if(this.movimentacaoInterna.devolverTodos){
                    this.movimentacaoInterna.patrimoniosId = this.filtrarPatrimonios(this.patrimonios)
                }
            },
            filtrarPatrimonios(patrimonios) {
                return patrimonios.filter(patrimonio => {
                    return !(this.movimentacaoInterna.patrimoniosNaoConsiderar.includes(patrimonio.patrimonioId))
                })
            },
            calculaQuantidadePatrimoniosSelecionados(){
                if(this.movimentacaoInterna.devolverTodos) {
                    this.quantidadeSelecionados = (this.quantidadeItensTodasPaginas - this.movimentacaoInterna.patrimoniosNaoConsiderar.length)
                }else{
                    this.quantidadeSelecionados = this.movimentacaoInterna.patrimoniosId.length
                }
            },
            async fecharModal() {
                await this.$router.push({
                    name: 'VisualizarRegistroMovimentacaoInterna',
                    params: {movimentacaoInternaId: this.$route.params.movimentacaoInternaId}
                })
            },
            tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosItensParaDevolucao(paginacao)
                this.resetarPaginacaoSortBy(paginacao)
                this.buscar()
            },
            async tratarEventoDevolver() {
                const movimentacao = _.cloneDeep(this.movimentacaoInterna)
                movimentacao.patrimoniosId = this.filtraIdPatrimoniosSelecionados()

                try {
                    if (movimentacao.devolverTodos) {
                        await this.devolverTodosPatrimonios(this.movimentacaoInterna.id)
                    } else {
                        await this.devolverItensParcial(movimentacao)
                    }
                    this.mostrarNotificacaoSucesso('Operação realizada com sucesso')
                } catch (err) {
                    this.mostrarNotificacaoErro(err.response.data.message)
                }

                this.resetaPage()
                this.buscarInformacoesAtualizadasMovimentacao()
                await this.redirecionarParaVisualizacaoMovimentacao()
            },
            filtraIdPatrimoniosSelecionados() {
                return this.movimentacaoInterna.patrimoniosId.map(item => item.patrimonioId)
            },
            tratarEventoBuscaSimples(valor) {
                this.resetaPage()
                this.filtrosInterno.conteudo.value = valor
                this.filtroAtivo = true
                this.buscar()
            },
            tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                this.filtroAtivo = false
                this.buscar()
            },
            resetarPaginacaoSortBy(paginacao) {
                if (!paginacao.sortBy[0]) {
                    this.$store.state.movimentacaoInternaVisualizacaoRegistroItem.resultadoBuscaTodosPatrimoniosParaDevolucao.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            },
            async redirecionarParaVisualizacaoMovimentacao() {
                await this.$router.push({
                    name: 'VisualizarRegistroMovimentacaoInterna',
                    params: {movimentacaoInternaId: this.$route.params.movimentacaoInternaId}
                })
            },
            resetaPage() {
                this.resetaPageBuscaTodosItensParaDevolucao()
            },
            resetarPatrimoniosExcecao() {
                this.movimentacaoInterna.patrimoniosNaoConsiderar = []
            },
            selecionaTodosPatrimoniosTodasPaginas(props) {
                if (props.value || this.filtroAtivo) {
                    this.movimentacaoInterna.devolverTodos = false
                    this.resetarPatrimoniosExcecao()
                } else {
                    this.movimentacaoInterna.devolverTodos = true
                    this.resetarPatrimoniosExcecao()
                }
            },
            selecionarPatrimonio(item) {
                if (this.movimentacaoInterna.devolverTodos) {
                    this.movimentacaoInterna.patrimoniosNaoConsiderar = this.movimentacaoInterna.patrimoniosNaoConsiderar.filter(patrimonio => patrimonio !== item.patrimonioId)
                }
                this.movimentacaoInterna.patrimoniosId.push(item)
            },
            desselecionarPatrimonio(item) {
                if (this.movimentacaoInterna.devolverTodos) {
                    this.movimentacaoInterna.devolverTodos = false
                    this.movimentacaoInterna.patrimoniosNaoConsiderar.push(item.patrimonioId)
                }
                this.movimentacaoInterna.patrimoniosId = this.movimentacaoInterna.patrimoniosId.filter(patrimonio => patrimonio.patrimonioId !== item.patrimonioId)
            },
            buscarInformacoesAtualizadasMovimentacao() {
                this.$emit('buscarInformacoesAtualizadasMovimentacao')
            },
            verificarSeFiltroAtivo() {
                this.filtroAtivo = !!this.filtrosInterno.conteudo.value
            }
        }
    }
</script>

<style scoped lang="stylus">
.max-18
    max-width 420px

.search-listagem-patrimonios-devolucao .input-search
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
    .search-listagem-patrimonios-devolucao .input-search
        span
            max-width 230px

@media (max-width 720px)
    .search-listagem-patrimonios-devolucao .input-search
        background-color #fff !important
        width 50vw !important

        span
            max-width 210px

.search-listagem-patrimonios-devolucao
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
