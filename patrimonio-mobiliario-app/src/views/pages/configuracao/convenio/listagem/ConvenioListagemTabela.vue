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
                no-data-text="Não há convênios cadastrados"
                class="az-table-lista pr-tabela-todos-convenios listagem-tabela"
                hide-default-footer
                must-sort
                @click:row="tratarEventoAcessar">
                <template v-slot:header.numero>
                    <label-personalizado campo="numero" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:header.nome>
                    <label-personalizado campo="nome" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:header.concedente.nome>
                    <label-personalizado campo="concedente" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:header.fonteRecurso>
                    <label-personalizado campo="fonteRecurso" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:header.situacao>
                    <label-personalizado campo="situacao" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:item.numero="{item}">
                    <span class="d-inline-block text-truncate max-13">{{item.numero}}</span>
                </template>
                <template v-slot:item.nome="{item}">
                    <span class="d-inline-block text-truncate max-16">{{item.nome}}</span>
                </template>
                <template v-slot:item.concedente.nome="{item}">
                    <span class="d-inline-block text-truncate max-15">{{item.concedente.nome}}</span>
                </template>
                <template v-slot:item.fonteRecurso="{item}">
                    <span class="d-inline-block text-truncate max-16">{{item.fonteRecurso}}</span>
                </template>
                <template v-slot:item.situacao="{item}">
                    <span
                        class="d-inline-block text-truncate max-10"
                    >{{ item.situacao | azEnum(situacoesConvenio) }}</span>
                </template>
            </v-data-table>
            <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>
        </az-form>
    </az-container>
</template>

<script>
    import mutationTypes from '@/core/constants/mutationTypes'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import Paginacao from '@/views/components/tabela/Paginacao'

    export default {
        name: 'ConvenioListagaemTabela',
        props: ['itens', 'paginacao', 'paginas', 'totalItens'],
        components:{Paginacao, LabelPersonalizado},
        data() {
            return {
                paginacaoInterna: this.paginacao,
                nomeTela: 'CONVENIO'
            }
        },
        computed:{
            colunas(){
                return this.criarColunas(5,
                                         ['Número','Nome','Concedente','Fonte de Recurso','Situação'],
                                         ['numero','nome','concedente.nome','fonteRecurso','situacao'],
                                         [true,true,true,true,true],
                                         ['left','left','left','left','left'],
                                         ['90px','90px','130px','170px','120px'],
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
                this.$store.commit(mutationTypes.CONFIGURACAO.CONVENIO.RESETA_PAGE_CONVENIO)
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
    .pr-tabela-todos-convenios
        th
            font-size 13px !important
            font-weight bold
            color #666666 !important

        tr
            height 55px !important

        td
            margin-top 15px !important
            padding 0 16px !important

    .az-table-lista
        color #777 !important

    .v-data-table__empty-wrapper
        color #777777 !important

        td
            font-size 15px !important
            font Roboto

    .pr-tabela-todos-convenios td:hover
        cursor pointer !important

    .max-10
        max-width 10vw

    .max-13
        max-width 13vw

    .max-15
        max-width 15vw

    .max-16
        max-width 16vw

    .max-20
        max-width 20vw

    @media (max-width: 720px)
        .max-15, .max-13, .max-16, .max-20
            max-width 40vw !important
</style>
