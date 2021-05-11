<template>
    <v-dialog persistent v-model="dialog" width="800">
        <v-card>
            <v-card-title
                class="body-2  primary white--text"
                primary-title>
                <span class="font-weight-bold mr-1">Estornar Patrimônios 2/3</span>- Selecione os Patrimônios
                <v-spacer/>
                <v-btn min-width="10px" class="pa-1 white--text" text @click="fecharModal">
                    <v-icon>fas fa-times</v-icon>
                </v-btn>
            </v-card-title>
            <az-toolbar class="search-listagem-patrimonios-estorno">
                <az-search
                    :filter="filtrosInterno"
                    @remove-filter="tratarEventoRemoverFiltro"
                    :maxlengthInput="500"
                    @simple-search="tratarEventoBuscaSimples"
                    simple-search-placeholder="Busque por: número ou descrição"
                    slot="simpleSearch"
                    class="searchItensCatalogoNovo"/>
            </az-toolbar>

            <v-responsive class="overflow-y-auto" max-height="70vh">
                <v-data-table
                    v-model="estorno.patrimoniosId"
                    :headers="colunas"
                    :items="patrimonios"
                    ref="table"
                    :options.sync="paginacaoInterna"
                    :server-items-length="totalItens"
                    class="pr-tabela-listagem-patrimonios-estorno"
                    hide-default-footer
                    item-key="id"
                    show-select
                    no-data-text="Não há patrimônios cadastrados">
                    <template v-slot:header.data-table-select="{ on, props }">
                        <v-simple-checkbox :ripple="false" color="grey" v-bind="props" v-on="on" @click="selecionaTodosPatrimoniosTodasPaginas(props)"></v-simple-checkbox>
                    </template>
                    <template v-slot:item.data-table-select="{ isSelected, item }">
                        <v-simple-checkbox :ripple="false" :value="isSelected" @click="isSelected ? deselectItem(item): selectItem(item)"/>
                    </template>
                    <template v-slot:item.numero="{item}">
                        <span class="grey--text text--darken-2 text-truncate max-8">{{item.numero}}</span>
                    </template>
                    <template v-slot:item.descricao="{item}">
                        <v-tooltip top nudge-top max-width="600" open-delay="500">
                            <template v-slot:activator="{ on }">
                                <div v-on="on">
                                    <span class="grey--text text--darken-2 d-inline-block text-truncate max-18">{{item.descricao | textoSemValor}}</span>
                                </div>
                            </template>
                            {{item.descricao}}
                        </v-tooltip>
                    </template>
                    <template v-slot:item.valor="{item}">
                        <span class="grey--text text--darken-2 d-inline-block max-10">{{item.valor | valorParaReal}}</span>
                    </template>
                </v-data-table>

            <paginacao class="ml-5 mr-5" :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>


            <v-divider></v-divider>
            <v-card-actions>
                <v-row cols="12" md="12" class="mx-1">
                    <v-col cols="6" class="d-flex justify-start">
                        <botao-voltar @voltar="voltar"/>
                    </v-col>
                    <v-col cols="6" class="d-flex justify-end">
                        <botao-continuar v-if="quantidadePatrimonios !== 0" @continuar="continuar"/>
                        <botao-continuar-desabilitado v-else/>
                    </v-col>
                </v-row>
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
    import BotaoVoltar from '@/views/components/botoes/BotaoVoltar'
    import BotaoContinuar from '@/views/components/botoes/BotaoContinuar'
    import BotaoContinuarDesabilitado from '@/views/components/botoes/BotaoContinuarDesabilitado'

    export default {
        name: 'ModalEstornarPatrimoniosListagem',
        components: {Paginacao, BotaoVoltar, BotaoContinuar, BotaoContinuarDesabilitado},
        props: {
            estorno: {
                type: Object,
            }
        },
        data() {
            return {
                dialog: true,
                patrimonios: [],
                filtrosInterno: this.getFiltros(),
                paginacaoInterna: this.getPaginacao(),
                paginas: 0,
                totalItens: 0
            }
        },
        async mounted() {
            await this.buscar()
        },
        computed:{
            quantidadePatrimonios(){
                return this.estorno.patrimoniosId ? this.estorno.patrimoniosId.length : 0
            },
            colunas(){
                return this.criarColunas(3,
                                         ['Patrimônio','Descrição','Valor'],
                                         ['numero','descricao','valor'],
                                         [false,false,false],
                                         ['left','left','left'],
                                         ['30%','40%','30%'],
                                         'gray--text')
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.tratarEventoPaginar(novoValor)
                },
                deep: true,
            }
        },
        methods: {
            ...mapActions([actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_DE_TODOS_ITENS]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS,
                mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS,
                mutationTypes.PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_DE_TODOS_ITENS,
                mutationTypes.PATRIMONIO.SET_ESTORNO
            ]),
            async buscaTodosPatrimonios() {
                const resultado = await this.buscarTodosPatrimoniosDeTodosItens(this.$route.params.incorporacaoId)
                if (resultado) {
                    this.patrimonios = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                    this.estorno.quantidadeTotalItens = resultado.totalElementsOfAllPages
                    this.selecionaTodosPatrimoniosSeNecessario()
                }
            },
            selecionaTodosPatrimoniosSeNecessario(){
                if(this.estorno.todosSelecionados){
                    this.estorno.patrimoniosId = this.filtrarPatrimonios(this.patrimonios)
                }
            },
            filtrarPatrimonios(patrimonios) {
                return patrimonios.filter(patrimonio => {
                    return !(this.estorno.patrimoniosExcecao.includes(patrimonio.id))
                })
            },
            fecharModal() {
                this.setEstorno({motivo: null, patrimoniosId: []})
                this.$router.replace({
                    name: 'VisualizarRegistroIncorporacao',
                    params: {incorporacaoId: this.$route.params.incorporacaoId}
                })
            },
            voltar(){
                this.$router.replace({name:'ModalEstornarPatrimoniosMotivo'})
            },
            continuar() {
                this.setEstorno(this.estorno)
                this.$router.replace({name:'ModalEstornarPatrimoniosFinalizar'})
            },
            async buscar() {
                this.setFiltrosBuscaTodosPatrimoniosDeTodosItens(this.getFiltrosInterno())
                await this.buscaTodosPatrimonios()
            },
            tratarEventoBuscaSimples(valor) {
                this.resetaPageBuscaTodosPatrimoniosDeTodosItens()
                this.filtrosInterno.conteudo.value = valor
                this.buscar()
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.patrimonio.resultadoBuscaTodosPatrimoniosDeTodosItens.filtros)
            },
            getPaginacao(){
                return _.cloneDeep(this.$store.state.patrimonio.resultadoBuscaTodosPatrimoniosDeTodosItens.paginacao)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                this.buscar()
            },
            tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosPatrimoniosDeTodosItens(paginacao)
                this.resetarPaginacaoSortBy(paginacao)
                this.buscar()
            },
            resetaPage(){
                this.resetaPageBuscaTodosPatrimoniosDeTodosItens()
            },
            resetarPaginacaoSortBy(paginacao) {
                if (!paginacao.sortBy[0]) {
                    this.$store.state.patrimonio.resultadoBuscaTodosPatrimoniosDeTodosItens.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            },
            selecionaTodosPatrimoniosTodasPaginas(props){
                if(props.value){
                    this.estorno.todosSelecionados = false
                    this.resetarPatrimoniosExcecao()
                }else{
                    this.estorno.todosSelecionados = true
                    this.resetarPatrimoniosExcecao()
                }
            },
            resetarPatrimoniosExcecao() {
                this.estorno.patrimoniosExcecao = []
            },
            deselectItem(item) {
                if (this.estorno.todosSelecionados) {
                    this.estorno.patrimoniosExcecao.push(item.id)
                }
                this.estorno.patrimoniosId = this.estorno.patrimoniosId.filter(patrimonio => patrimonio.id !== item.id)
            },
            selectItem(item) {
                if (this.estorno.todosSelecionados) {
                    this.estorno.patrimoniosExcecao = this.estorno.patrimoniosExcecao.filter(patrimonio => patrimonio !== item.id)
                }
                this.estorno.patrimoniosId.push(item)
            }
        }
    }
</script>

<style scoped lang="stylus">
.modal-estornar-listagem
    max-height 50vh

.max-18
    max-width 420px

.search-listagem-patrimonios-estorno .input-search
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
    .search-listagem-patrimonios-estorno .input-search
        span
            max-width 230px

@media (max-width 720px)
    .search-listagem-patrimonios-estorno .input-search
        background-color #fff !important
        width 50vw !important

        span
            max-width 210px

.search-listagem-patrimonios-estorno
    display flex
    align-items center
    justify-content center
    background-color white !important

>>>
    .pr-tabela-listagem-patrimonios-estorno
        th
            font-size 13px !important
            font-weight bold
            color #666666 !important

        tr.v-data-table__selected
            background white !important

        tr.v-data-table__selected td .v-icon
            color grey !important

</style>
