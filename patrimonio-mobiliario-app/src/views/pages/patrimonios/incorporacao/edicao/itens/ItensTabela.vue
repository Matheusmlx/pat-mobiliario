<template>
    <az-container class="mt-0">
        <az-form class="incorporacao-item-tabela">
            <v-data-table
                ref="table"
                :headers="colunas"
                :items="itens"
                :server-items-length="totalItens"
                :options.sync="paginacaoInterna"
                :loading="false"
                no-data-text="Não há itens cadastrados"
                class="az-table-lista pr-tabela-todos-itens-incorporacao item-incorporacao-listagem-tabela"
                must-sort
                hide-default-footer
                @click:row="tratarEventoAcessar">
                <template v-slot:header.imagem>
                    <label-personalizado campo="uriImagem" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:header.codigo>
                    <label-personalizado campo="codigo" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:header.descricao>
                    <label-personalizado campo="descricao" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:header.quantidade>
                    <label-personalizado campo="quantidade" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:header.valorTotal>
                    <label-personalizado campo="valorTotal" :tela="nomeTela" apenasNome/>
                </template>
                <template v-slot:item.imagem="{item}">
                    <v-img :src="'data:image/png;base64,' + item.imagem" max-width="40" max-height="40" aspect-ratio="1" class="img-item-incorporacao"/>
                </template>
                <template v-slot:item.codigo="{item}">
                    <span class="d-inline-block text-truncate max-10">{{item.codigo | textoSemValor}}</span>
                </template>
                <template v-slot:item.descricao="{item}">
                    <span class="d-inline-block text-truncate max-20">{{item.descricao | textoSemValor}}</span>
                </template>
                <template v-slot:item.quantidade="{item}">
                    <span class="d-inline-block text-truncate max-5">{{item.quantidade | textoSemValor}}</span>
                </template>
                <template v-slot:item.valorTotal="{item}">
                    <span class="d-inline-block text-truncate max-7">{{ item.valorTotal | valorParaDinheiro}}</span>
                </template>
                <template v-slot:item.situacao="{item}">
                    <span class="d-inline-block text-truncate max-10">
                           <v-icon
                               class="mr-1"
                               color="#ff9800"
                               size="13"
                               v-if="item.situacao === 'EM_ELABORACAO'">
                                fas fa-edit
                         </v-icon>
                        {{ item.situacao | azEnum(situacoesItemIncorporacao)}}
                    </span>
                </template>
                <template v-slot:item.acoes="{item}">
                    <botao-remover v-if="verificaPermissaoEdicao" @remover="tratarEventoDeletarItem(item.id)" class="botao-excluir-item"/>
                </template>
            </v-data-table>
            <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>
        </az-form>
    </az-container>
</template>

<script>
    import {mutationTypes} from '@/core/constants'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import BotaoRemover from '@/views/components/botoes/BotaoRemover'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'

    export default {
        name: 'ItensTabela',
        components: {LabelPersonalizado, Paginacao, BotaoRemover},
        props: ['itens', 'paginacao', 'paginas', 'totalItens'],
        data() {
            return {
                paginacaoInterna: this.paginacao,
                nomeTela: 'ITEM_INCORPORACAO'
            }
        },
        computed:{
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
            },
            colunas(){
                return this.criarColunas(7,
                                         [null,null,null,null,null,'Situação',null],
                                         ['imagem','codigo','descricao','quantidade','valorTotal','situacao','acoes'],
                                         [false,false,false,false,false,false,false],
                                         ['left','left','left','left','left','left','right'],
                                         ['10px','100px','100px','130px','120px','110px','1px'],
                                         'gray--text')
            }
        },
        methods: {
            tratarEventoDeletarItem(id){
                this.$emit('deletar', id)
            },
            tratarEventoAcessar(item) {
                this.$emit('acessar', item)
            },
            tratarPaginacao(pagina) {
                this.paginacaoInterna.page = pagina
            },
            resetaPage() {
                this.$store.commit(mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_INCORPORACAO_ITEM)
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
.item-incorporacao-listagem-tabela
    min-height 49vh
.incorporacao-item-tabela
        padding 0px

    .pr-tabela-todos-itens-incorporacao
        th
            font-size 13px !important
            font-weight bold
            color #666666 !important

        tr
            height 55px !important

        td
            margin-top 15px !important
            padding 0 16px !important

    .img-item-incorporacao
        border 1px solid #777

    .v-data-table button
        visibility visible !important
        margin 0px !important

    .az-table-lista
        color #777 !important

    .pr-tabela-todos-itens-incorporacao td:hover
        cursor pointer !important

    .max-5
        max-width 5vw
    .max-7
        max-width 7vw

    .max-10
        max-width 10vw

    .max-25
        max-width 20vw

    .alinhamentoFiltragem
        margin-top -20px !important

    .botao-excluir-item
        padding 0 15% 2% 60%

    @media (max-width: 720px)
        .max-5, .max-7, .max-10, .max-25
            max-width 50vw !important
</style>
