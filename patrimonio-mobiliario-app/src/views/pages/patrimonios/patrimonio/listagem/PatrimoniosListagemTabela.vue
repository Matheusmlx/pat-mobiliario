<template>
    <az-container>
        <az-form>
            <v-data-table
                ref="table"
                :headers="colunas"
                :items="itens"
                :server-items-length="totalItens"
                :options.sync="paginacaoInterna"
                :loading="false"
                no-data-text="Não há patrimônios cadastrados"
                class="az-table-list pr-tabela-todos-patrimonios listagem-tabela"
                hide-default-footer
                @click:row="tratarEventoAcessar">
                <template v-slot:header.descricao>
                    <label-personalizado campo="numero" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:header.orgao>
                    <label-personalizado campo="orgao" :tela="'INCORPORACAO_DADOS_GERAIS'" apenasNome/>
                </template>
                <template v-slot:header.setor>
                    <label-personalizado campo="setor" :tela="'INCORPORACAO_DADOS_GERAIS'" apenasNome/>
                </template>
                <template v-slot:header.ultimaMovimentacao>
                    <label>Última movimentação</label>
                </template>
                <template v-slot:header.situacao>
                    <label>Situação</label>
                </template>
                <template v-slot:item.descricao="{item}">
                    <span class="d-inline-block text-truncate max-25">{{item.descricao | textoSemValor}}</span>
                </template>
                <template v-slot:item.orgao="{item}">
                    <span class="d-inline-block text-truncate max-10">{{item.orgao | textoSemValor}}</span>
                </template>
                <template v-slot:item.setor="{item}">
                    <span class="d-inline-block text-truncate max-20">{{item.setor | textoSemValor}}</span>
                </template>
                <template v-slot:item.situacao="{item}">
                    <span class="d-inline-block text-truncate max-10">
                        {{ item.situacao | azEnum(situacoesPatrimonio) }}
                    </span>
                </template>
                <template v-slot:item.acoes="{  }">
                    <v-icon>keyboard_arrow_right</v-icon>
                    <span class="mobile">Acessar</span>
                </template>
            </v-data-table>
            <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>
        </az-form>
    </az-container>
</template>

<script>
    import {mutationTypes, situacoesPatrimonio, ultimaMovimentacaoPatrimonio} from '@/core/constants'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'

    export default {
        name: 'PatrimoniosListagemTabela',
        components: {Paginacao, LabelPersonalizado},
        props: ['itens', 'paginacao', 'paginas', 'totalItens'],
        data() {
            return {
                paginacaoInterna: this.paginacao,
                situacoesPatrimonio,
                ultimaMovimentacaoPatrimonio,
                nomeTela: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO'
            }
        },
        computed:{
            colunas() {
                return this.criarColunas(5,
                                         [],
                                         ['descricao','orgao','setor','situacao','acoes'],
                                         [false,false,false,false,false],
                                         ['left','left','left','left','right'],
                                         ['30%','10%','20%','10%','20%'],
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
                this.$store.commit(mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO_LISTAGEM)
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
    .pr-tabela-todos-patrimonios
        th
            font-size 13px !important
            font-weight bold
            color #666666 !important

        tr
            height 55px !important

        td
            margin-top 15px !important
            padding 0 10px !important

    .pr-tabela-todos-patrimonios td:hover
        cursor pointer !important

    .max-10
        max-width 10vw

    .max-15
        max-width 15vw

    .max-20
        max-width 20vw

    .max-25
        max-width 25vw

    @media (max-width: 720px)
       .max-10, .max-15, .max-20, .max-25
            max-width 40vw !important
</style>
