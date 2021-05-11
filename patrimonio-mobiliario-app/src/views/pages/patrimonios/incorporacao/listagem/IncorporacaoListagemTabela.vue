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
                class="az-table-list pr-tabela-todas-incorporacoes listagem-tabela"
                :item-class="retornaClasseDoItem"
                hide-default-footer
                must-sort
                no-data-text="Não há incorporações cadastradas">
                <template v-slot:header.id>
                    <label-personalizado campo="codigo" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:header.fornecedor>
                    <label-personalizado campo="fornecedor" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:header.orgao>
                    <label-personalizado campo="orgao" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:header.dataRecebimento>
                    <label-personalizado campo="dataRecebimento" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:header.situacao>
                    <label-personalizado campo="situacao" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:item.id="{item}">
                    <span class="d-inline-block text-truncate max-8">{{item.id}}</span>
                </template>
                <template v-slot:item.fornecedor="{item}">
                    <span class="d-inline-block text-truncate max-25">{{item.fornecedor | textoSemValor}}</span>
                </template>
                <template v-slot:item.orgao="{item}">
                    <span class="d-inline-block text-truncate max-10">{{item.orgao | textoSemValor}}</span>
                </template>
                <template v-slot:item.dataRecebimento="{item}">
                    <span class="d-inline-block text-truncate max-15">{{item.dataRecebimento | azDate()}}</span>
                </template>
                <template v-slot:item.situacao="{item}">
                    <span class="d-inline-block text-truncate max-15">
                         <v-icon
                             class="mr-1"
                             :color="retornaCorIcone(item)"
                             size="13">
                             {{retornaIcone(item)}}
                         </v-icon>
                        {{ item.situacao | azEnum(situacoesIncorporacao) }}
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
    import {mutationTypes} from '@/core/constants'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'

    export default {
        name: 'IncorporacaoListagemTabela',
        components: {Paginacao, LabelPersonalizado},
        props: ['itens', 'paginacao', 'paginas', 'totalItens'],
        data() {
            return {
                paginacaoInterna: this.paginacao,
                nomeTela: 'INCORPORACAO_DADOS_GERAIS'
            }
        },
        computed:{
            colunas(){
                return this.criarColunas(6,
                                         [],
                                         ['id','fornecedor','orgao','dataRecebimento','situacao','acoes'],
                                         [true,true,true,true,true,false],
                                         ['left','left','left','left','left','right'],
                                         ['90px','100px','170px','130px','1px','10px'],
                                         'gray--text')
            }
        },
        methods: {
            tratarEventoAcessar(item) {
                this.$emit('acessar', item)
            },
            tratarPaginacao(pagina) {
                this.paginacaoInterna.page = pagina
            },
            resetaPage() {
                this.$store.commit(mutationTypes.PATRIMONIO.INCORPORACAO.RESETA_PAGE_INCORPORACAO)
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

        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.$emit('paginar', novoValor)
                },
                deep: true,
            },
        },
    }
</script>

<style lang="stylus">
    .pr-tabela-todas-incorporacoes
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
