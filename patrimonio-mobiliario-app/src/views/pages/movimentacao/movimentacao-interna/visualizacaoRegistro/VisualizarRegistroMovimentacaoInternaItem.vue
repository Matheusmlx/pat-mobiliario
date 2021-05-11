<template>
    <v-row class="white pl-3 pr-3 ml-0 mr-0 pb-5">
        <v-col cols="12">
            <v-card class="movimentacao-itens" elevation="0">
                <v-expansion-panels v-model="exibirPanel" active-class="" flat>
                    <v-expansion-panel>
                        <v-expansion-panel-header class="movimentacao-itens-panel-header" flat>
                            <h4 class="titulo">Patrimônios ({{quantidadeTotalPatrimoniosFormatada}})</h4>
                        </v-expansion-panel-header>
                        <v-expansion-panel-content class="panel-content" flat>
                            <v-data-table
                                ref="table"
                                :headers="colunasTabela"
                                :items="movimentacaoInternaItens"
                                :server-items-length="quantidadeTotalPatrimonios"
                                :options.sync="paginacaoInterna"
                                no-data-text="Não há patrimônios adicionados"
                                class="az-table-lista pr-tabela-movimentacao-interna-itens"
                                hide-default-footer>
                                <template v-slot:header.patrimonioNumero>
                                    <label-personalizado campo="numero" :tela="nomeTelaPatrimonio" apenasNome/>
                                </template>
                                <template v-slot:header.patrimonioDescricao>
                                    <label-personalizado campo="descricao" :tela="nomeTelaMovimentacaoItens" apenasNome/>
                                </template>
                                <template v-slot:header.incorporacaoCodigo v-if="deveExibirColunaIncorporacao">
                                    <label-personalizado campo="incorporacao" :tela="nomeTelaMovimentacaoItens" apenasNome/>
                                </template>
                                <template v-slot:header.patrimonioSituacao v-if="deveExibirColunaSituacao">
                                    <div class="label-personalizado">Situação</div>
                                </template>
                                <template v-slot:item.patrimonioNumero="{item}">
                                    <span class="d-inline-block text-truncate max-6">{{ item.patrimonioNumero | textoSemValor }}</span>
                                </template>
                                <template v-slot:item.patrimonioDescricao="{item}">
                                    <span class="d-inline-block text-truncate max-50" >{{ item.patrimonioDescricao | textoSemValor }}</span>
                                </template>
                                <template v-slot:item.incorporacaoCodigo="{item}" v-if="deveExibirColunaIncorporacao">
                                    <span class="d-inline-block text-truncate max-9">{{ item.incorporacaoCodigo | textoSemValor }}</span>
                                </template>
                                <template v-slot:item.patrimonioSituacao="{item}" v-if="deveExibirColunaSituacao">
                                    <span v-if="!item.dataDevolucao" class="d-inline-block text-truncate max-9">Aguardando Devolução</span>
                                    <span v-else class="d-inline-block text-truncate max-11">Devolvido em {{ item.dataDevolucao | azDate }}</span>
                                </template>
                            </v-data-table>
                            <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>
                        </v-expansion-panel-content>
                    </v-expansion-panel>
                </v-expansion-panels>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapMutations} from 'vuex'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import Paginacao from '@/views/components/tabela/Paginacao'

    export default {
        name: 'VisualizarRegistroMovimentacaoInternaItem',
        components: {
            LabelPersonalizado,
            Paginacao
        },
        props: {
            movimentacao: {
                type: Object
            }
        },
        data() {
            return {
                exibirPanel: 0,
                movimentacaoInternaId: null,
                movimentacaoInternaItens: [],
                quantidadeTotalPatrimonios: 0,
                quantidadeTotalPatrimoniosFormatada: '',
                paginas: 0,
                nomeTelaMovimentacaoItens: 'MOVIMENTACAO_INTERNA_ITENS',
                nomeTelaPatrimonio: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO',
                paginacaoInterna: {}
            }
        },
        created() {
            this.setMovimentacaoInternaId()
            this.resetaPage()
            this.paginacaoInterna = this.getPaginacao()
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.tratarEventoPaginar(novoValor)
                },
                deep: true
            },
            async movimentacao() {
                this.setMovimentacaoInternaId()
                this.resetaPage()
                await this.buscarMovimentacaoItens()
            }
        },
        computed: {
            deveExibirColunaIncorporacao() {
                return this.movimentacao.tipo === 'DISTRIBUICAO'
            },
            deveExibirColunaSituacao() {
                return this.movimentacao.tipo === 'TEMPORARIA'
            },
            colunasTabela() {
                if (this.deveExibirColunaIncorporacao) {
                    return this.criarColunas(3,
                                             [],
                                             ['patrimonioNumero','patrimonioDescricao','incorporacaoCodigo'],
                                             [false,false,false],
                                             ['left','left','left'],
                                             ['10%','55%','25%'],
                                             'gray--text')
                } else if (this.deveExibirColunaSituacao) {
                    return this.criarColunas(3,
                                             [],
                                             ['patrimonioNumero','patrimonioDescricao','patrimonioSituacao'],
                                             [false,false,false],
                                             ['left','left','left'],
                                             ['10%','55%','25%'],
                                             'gray--text')
                } else {
                    return this.criarColunas(2,
                                             [],
                                             ['patrimonioNumero','patrimonioDescricao'],
                                             [false,false],
                                             ['left','left'],
                                             ['10%','60%'],
                                             'gray--text')
                }
            }
        },
        methods: {
            ...mapActions([
                actionTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.BUSCAR_TODOS_ITENS_ADICIONADOS_VISUALIZACAO_REGISTRO
            ]),
            ...mapMutations([
                mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.SET_PAGINACAO_BUSCA_TODOS_ITENS_VISUALIZACAO,
                mutationTypes.MOVIMENTACAO_INTERNA.VISUALIZACAO.RESETA_PAGE_BUSCA_TODOS_ITENS_VISUALIZACAO
            ]),
            setMovimentacaoInternaId() {
                this.movimentacaoInternaId = this.movimentacao.id
            },
            getPaginacao() {
                return this.$store.state.movimentacaoInternaVisualizacaoRegistro
                    .resultadoBuscaTodosItensMovimentacaoInternaVisualizacao.paginacao
            },
            async buscarMovimentacaoItens() {
                const resultado = await this.buscarTodosItensAdicionadosVisualizacaoRegistro(this.movimentacaoInternaId)
                this.movimentacaoInternaItens = resultado.items
                this.quantidadeTotalPatrimonios = resultado.totalElements
                this.quantidadeTotalPatrimoniosFormatada = this.formatarQuantidadePatrimonios(resultado.totalElements)
                this.paginas = resultado.totalPages
            },
            formatarQuantidadePatrimonios(quantidade) {
                if (typeof quantidade == 'number') {
                    return quantidade.toLocaleString('pt-BR', {minimumIntegerDigits: 2})
                }
                return quantidade
            },
            tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosItensVisualizacao(paginacao)
                this.buscarMovimentacaoItens()
            },
            resetaPage() {
                this.resetaPageBuscaTodosItensVisualizacao()
            }
        }
    }
</script>

<style scoped lang="stylus">
.movimentacao-itens
    border solid 1px #e7e7e7 !important
    border-radius 5px !important

.movimentacao-itens-panel-header
    border-bottom solid 1px #e7e7e7 !important
    background-color #F6F6F6
    border-radius 5px !important
    min-height 20px !important
    padding-bottom 10px
    padding-top 10px

.titulo
    font-size 15px
    font-weight bold
    color #777 !important

.max-10
    max-width 10vw

.max-50
    max-width 49vw

</style>
