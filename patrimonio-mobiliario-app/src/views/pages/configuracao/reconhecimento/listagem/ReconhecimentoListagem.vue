<template>
    <az-container class="reconhecimento">
        <az-form>
            <v-data-table
                ref="table"
                :headers="colunas"
                :items="value"
                :loading="false"
                :options.sync="paginacaoInterna"
                :server-items-length="totalItens"
                hide-default-footer
                must-sort
                class="az-table-list pr-tabela-todos-convenios listagem-tabela"
                no-data-text="Não há reconhecimentos cadastrados">
                <template v-slot:header.nome>
                    <label-personalizado campo="nome" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:header.situacao>
                    <label-personalizado campo="situacao" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:header.execucaoOrcamentaria>
                    <label-personalizado campo="execucaoOrcamentaria" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:header.camposObrigatorios>
                    <label-personalizado campo="camposObrigatorios" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:item="{ item }">
                    <reconhecimento-form
                        v-model="item"
                        :permitir-acoes="permitirAcoes"
                        @atualizarReconhecimento="atualizarReconhecimento"
                        @cancelarEdicao="cancelarEdicao"
                        @desabilitaBotaoNovo="desabilitaBotaoNovo"
                        @habilitaBotaoNovo="habilitaBotaoNovo"
                        @desativarAcoes="desativarAcoes"
                        @habilitarAcoes="habilitarAcoes"
                        @inserirNovoReconhecimento="inserirNovoReconhecimento"/>
                </template>
            </v-data-table>
            <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>
        </az-form>
    </az-container>
</template>

<script>
    import mutationTypes from '@/core/constants/mutationTypes'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import ReconhecimentoForm from './ReconhecimentoForm'
    import Paginacao from '@/views/components/tabela/Paginacao'

    export default {
        name: 'ReconhecimentoListagem',
        components: {Paginacao, ReconhecimentoForm, LabelPersonalizado},
        props: ['value', 'paginacao', 'paginas', 'totalItens', 'permitirEdicao'],
        data() {
            return {
                paginacaoInterna: this.paginacao,
                permitirAcoes: this.permitirEdicao,
                nomeTela: 'RECONHECIMENTO'
            }
        },
        computed:{
            colunas(){
                return this.criarColunas(5,
                                         [],
                                         ['nome','situacao','execucaoOrcamentaria','camposObrigatorios','acoes'],
                                         [true,true,true,false,false],
                                         ['left','left','left','left','left'],
                                         ['50px','50px','150px','150px','10px'],
                                         'gray--text')
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.$emit('paginar', novoValor)
                },
                deep: true
            }
        },
        methods: {
            atualizarReconhecimento(reconhecimento) {
                this.$emit('atualizarReconhecimento', reconhecimento)
            },
            cancelarEdicao() {
                this.habilitarAcoes()
                this.$emit('cancelarEdicao')
            },
            desabilitaBotaoNovo() {
                this.$emit('desabilitaBotaoNovo')
            },
            habilitaBotaoNovo() {
                this.$emit('habilitaBotaoNovo')
            },
            desativarAcoes() {
                this.permitirAcoes = false
            },
            habilitarAcoes() {
                this.permitirAcoes = true
            },
            inserirNovoReconhecimento(novoReconhecimento) {
                this.$emit('inserirNovoReconhecimento', novoReconhecimento)
            },
            tratarPaginacao(pagina) {
                this.paginacaoInterna.page = pagina
            },
            resetaPage() {
                this.$store.commit(mutationTypes.CONFIGURACAO.RECONHECIMENTO.RESETA_PAGE_RECONHECIMENTO)
            }
        }
    }
</script>

<style scoped lang="stylus">
    .reconhecimento
        .az-table-list
            .v-text-field__details
                display flex !important
                margin-top 2px

            th
                font-weight bold
                color #666666 !important
                font-size 13px !important

            tr
                height 55px !important

            td
                margin-top 15px !important
                padding 0 10px !important

            .v-input__slot
                border none
                font-size 0.8em
                padding-left 0
                padding-right 0
                max-height 65px

                input
                    color #777
                    border-color #777

        .input >>> .v-input__control
            background-color white

        .input >>> .v-input
            max-width 300px

        .v-data-table
            cursor pointer !important

            .execucao-orcamentaria
                margin-top 10px
                display inline-flex

        .v-data-table tbody tr:after
            cursor pointer !important

        @media (max-width: 720px)
            .pr-tabela-todos-convenios
                td::before
                    display flex
                    flex-direction column

                td:nth-of-type(1):before
                    content "Nome: "
                    font 14px Roboto, sans-serif
                    font-weight 600
                    color #777

                td:nth-of-type(2):before
                    content "Situação:"
                    font 14px Roboto, sans-serif
                    font-weight 600
                    color #777

                td:nth-of-type(3):before
                    content "Execução Orçamentária: "
                    font 14px Roboto, sans-serif
                    font-weight 600
                    color #777

            br
                display none
</style>
