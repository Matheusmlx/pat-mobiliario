<template>
    <div>
        <div class="itens-listagem-tabela-itens-movimentacao">
            <v-col md="12" sm="12" xs="12" class="d-flex align-end justify-end pb-0">
                <botao-adicionar-patrimonio @adicionarPatrimonio="adicionarItem" :desabilitar="!verificaPermissaoEdicao" />
            </v-col>

            <az-container class="mt-0">
                <az-form class="movimentacao-interna-itens-tabela">
                    <v-data-table
                        ref="table"
                        :headers="colunasTabela"
                        :items="itens"
                        :server-items-length="totalItens"
                        :options.sync="paginacaoInterna"
                        no-data-text="Não há patrimônios adicionados"
                        class="az-table-lista pr-tabela-movimentacao-interna-itens item-movimentacao-listagem-tabela"
                        hide-default-footer>
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
                            <span class="d-inline-block text-truncate max-10">{{ item.patrimonioNumero | textoSemValor }}</span>
                        </template>
                        <template v-slot:item.patrimonioDescricao="{item}">
                            <span class="d-inline-block text-truncate max-50">{{ item.patrimonioDescricao | textoSemValor }}</span>
                        </template>
                        <template v-slot:item.incorporacaoCodigo="{item}">
                            <span class="d-inline-block text-truncate max-10">{{ item.incorporacaoCodigo | textoSemValor }}</span>
                        </template>
                        <template v-slot:item.acoes="{item}">
                            <botao-remover v-if="verificaPermissaoEdicao" @remover="tratarEventoDeletarItem(item.patrimonioId)"
                                           class="botao-excluir-item-movimentacao"/>
                        </template>
                    </v-data-table>
                    <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>
                </az-form>
            </az-container>
        </div>
    </div>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapMutations} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import BotaoRemover from '@/views/components/botoes/BotaoRemover'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import BotaoAdicionarPatrimonio from '@/views/components/botoes/BotaoAdicionarPatrimonio'

    export default {
        name: 'MovimentacaoInternaListagemItens',
        props: ['itens', 'paginas', 'totalItens'],
        components: {Paginacao, BotaoRemover, BotaoAdicionarPatrimonio, LabelPersonalizado},
        data() {
            return {
                paginacaoInterna: this.$store.state.movimentacaoInternaItem.resultadoBuscaTodosItensAdicionadosMovimentacaoInterna.paginacao,
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
        },
        computed: {
            existeItens() {
                return (this.totalItens > 0)
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Movimentacoes'])
            }
        },
        async mounted() {
            this.colunasTabela = await this.obterColunasTabela()
        },
        methods: {
            ...mapActions([
                actionTypes.MOVIMENTACAO_INTERNA.ITEM.DELETAR_ITEM_MOVIMENTACAO_INTERNA,
                actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA
            ]),
            ...mapMutations([
                mutationTypes.MOVIMENTACAO_INTERNA.ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA,
                mutationTypes.MOVIMENTACAO_INTERNA.ITEM.RESETA_PAGE_BUSCA_TODOS_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA
            ]),
            adicionarItem(){
                this.$emit('adicionarItem')
            },
            buscar() {
                this.$emit('buscarTodosItens')
            },
            async tratarEventoDeletarItem(patrimonioId) {
                if (!this.verificaPermissaoEdicao) return
                const data = {movimentacaoInternaId: this.$route.params.movimentacaoInternaId, itemId: patrimonioId}
                await this.deletarItemMovimentacaoInterna(data)
                this.buscar()
            },
            tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosItensAdicionadosMovimentacaoInterna(paginacao)
                this.buscar()
            },
            resetaPage() {
                this.resetaPageBuscaTodosItensAdicionadosMovimentacaoInterna()
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
                                             ['15%','55%','20%','10%'],
                                             'gray--text')
                }
                return this.criarColunas(3,
                                         [],
                                         ['patrimonioNumero','patrimonioDescricao','acoes'],
                                         [false,false,false],
                                         ['left','left','right'],
                                         ['13%','70%','20%'],
                                         'gray--text')
            }
        }
    }
</script>

<style lang="stylus">
.item-movimentacao-listagem-tabela
    min-height 50vh

.itens-listagem-tabela-itens-movimentacao
    min-height 62vh

.itens-movimentacao-toolbar
    background-color white

.movimentacao-interna-itens-tabela
    padding 0px

.pr-tabela-movimentacao-interna-itens
    th
        font-size 13px !important
        font-weight bold
        color #666666 !important

    tr
        height 55px !important

    td
        margin-top 15px !important
        padding 0 16px !important


.v-data-table button
    visibility visible !important
    margin 0px !important

.az-table-lista
    color #777 !important

.pr-tabela-movimentacao-interna-itens td:hover
    cursor default !important

.max-10
    max-width 10vw

.max-50
    max-width 50vw

.alinhamentoFiltragem
    margin-top -20px !important

.botao-excluir-item-movimentacao
    padding 0 15% 2% 60%

@media (max-width: 720px)
    .max-10, .max-25
        max-width 50vw !important

</style>

