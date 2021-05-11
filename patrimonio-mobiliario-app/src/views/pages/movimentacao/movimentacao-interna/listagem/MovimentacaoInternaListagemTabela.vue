<template>
    <az-container>
        <az-form>
            <v-data-table
                :headers="colunas"
                :items="itens"
                ref="table"
                :loading="false"
                :options.sync="paginacaoInterna"
                :server-items-length="totalItens"
                @click:row="tratarEventoAcessar"
                class="az-table-list pr-tabela-todas-movimentacoes-internas listagem-tabela"
                :item-class="retornaClasseDoItem"
                hide-default-footer
                must-sort
                no-data-text="Não há movimentações internas cadastradas">
                <template v-slot:header.codigo>
                    <label-personalizado campo="codigo" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:header.tipo>
                    <label-personalizado campo="tipo" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:header.orgaoOrigem>
                    <label-personalizado campo="orgao" :tela="nomeTelaIncorporacao" apenasNome/>
                </template>
                <template v-slot:header.setorOrigem>
                    <label-personalizado campo="setorOrigem" :tela="nomeTelaDadosGerais" apenasNome/>
                </template>
                <template v-slot:header.setorDestino>
                    <label-personalizado campo="setorDestino" :tela="nomeTelaDadosGerais" apenasNome/>
                </template>
                <template v-slot:header.situacao>
                    <label-personalizado campo="situacao" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:item.codigo="{item}">
                    <span class="d-inline-block text-truncate max-8">{{item.codigo}}</span>
                </template>
                <template v-slot:item.tipo="{item}">
                    <span class="d-inline-block text-truncate max-10">{{item.tipo | azEnum(tipoMovimentacoes)}}</span>
                </template>
                <template v-slot:item.orgaoOrigem="{item}">
                    <span class="d-inline-block text-truncate max-15">{{item.orgao | textoSemValor}}</span>
                </template>
                <template v-slot:item.setorOrigem="{item}">
                    <span class="d-inline-block text-truncate max-15">{{item.setorOrigem | textoSemValor}}</span>
                </template>
                <template v-slot:item.setorDestino="{item}">
                    <span class="d-inline-block text-truncate max-15">{{item.setorDestino | textoSemValor}}</span>
                </template>
                <template v-slot:item.situacao="{item}">
                    <span class="d-inline-block text-truncate max-10">
                         <v-icon
                             class="mr-1"
                             :color="retornaCorIcone(item)"
                             size="13">
                             {{retornaIcone(item)}}
                         </v-icon>
                        {{ item.situacao | azEnum(situacoesMovimentacaoInterna) }}
                    </span>
                </template>
                <template v-slot:item.acoes="{ item }">
                    <v-icon v-if="item.situacao !== 'EM_PROCESSAMENTO'">keyboard_arrow_right</v-icon>
                    <span class="mobile">Acessar</span>
                </template>
            </v-data-table>
            <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>
        </az-form>
    </az-container>
</template>

<script>
    import {mapMutations} from 'vuex'
    import {mutationTypes} from '@/core/constants'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'

    export default {
        name: 'MovimentacaoInternaListagemTabela',
        components: {Paginacao, LabelPersonalizado},
        props: ['itens', 'paginacao', 'paginas', 'totalItens'],
        data(){
            return {
                paginacaoInterna: this.paginacao,
                nomeTela: 'MOVIMENTACAO_INTERNA_LISTAGEM',
                nomeTelaDadosGerais: 'MOVIMENTACAO_INTERNA_DADOS_GERAIS',
                nomeTelaIncorporacao: 'INCORPORACAO_DADOS_GERAIS'
            }
        },
        computed:{
            colunas(){
                return this.criarColunas(7,
                                         [],
                                         ['codigo','tipo','orgaoOrigem','setorOrigem','setorDestino','situacao','acoes'],
                                         [true,true,true,true,true,true,false],
                                         ['left','left','left','left','left','left','right'],
                                         ['90px','10px','170px','100px','100px','10px','10px'],
                                         'gray--text')
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.$emit('paginar', novoValor)
                },
                deep: true,
            },
        },
        methods: {
            ...mapMutations([mutationTypes.MOVIMENTACAO_INTERNA.RESETA_PAGE_BUSCA_TODAS_MOVIMENTACOES_INTERNAS]),
            tratarEventoAcessar(item) {
                this.$emit('acessar', item)
            },
            tratarPaginacao(pagina) {
                this.paginacaoInterna.page = pagina
            },
            resetaPage() {
                this.resetaPageBuscaTodasMovimentacoesInternas()
            },
            retornaClasseDoItem(item){
                if(item.situacao === 'EM_PROCESSAMENTO'){
                    return 'pr-cursor-tabela'
                }
                return ''
            },
            retornaIcone(item){
                if(item.situacao === 'ERRO_PROCESSAMENTO'){
                    return 'fas fa-exclamation-triangle'
                }else if(item.situacao === 'EM_PROCESSAMENTO'){
                    return 'fas fa-clock'
                }else if(item.situacao === 'EM_ELABORACAO'){
                    return 'fas fa-edit'
                }
                return ''
            },
            retornaCorIcone(item){
                return  item.situacao === 'ERRO_PROCESSAMENTO' ? 'red' : '#ff9800'
            }
        }
    }
</script>

<style lang="stylus">
.pr-tabela-todas-movimentacoes-internas
    th
        font-size 13px !important
        font-weight bold
        color #666666 !important

    tr
        height 55px !important

    td
        margin-top 15px !important
        padding 0 10px !important
        cursor pointer !important

.pr-cursor-tabela td:hover
    cursor auto !important

.max-5
    max-width 5vw

.max-8
    max-width 8vw

.max-10
    max-width 10vw

.max-15
    max-width 15vw

.max-25
    max-width 25vw

@media (max-width: 720px)
    .max-8, .max-10, .max-15, .max-20, .max-25
        max-width 40vw !important
</style>
