<template>
    <az-container class="contasContabeis">
        <az-form>
            <v-data-table
                :headers="colunasTable"
                :items="value"
                :loading="false"
                :options.sync="paginacaoInterna"
                :server-items-length="totalItens"
                class="az-table-list listagem-tabela"
                hide-default-footer
                no-data-text="Não há Contas Contábeis Cadastradas">
                <template v-slot:header.codigoContabil>
                    <label-personalizado campo="codigoContabil" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:header.contaContabil>
                    <label-personalizado campo="contaContabil" :tela="nomeTela" :apenasNome="true"/>
                </template>
                <template v-slot:header.tipo>
                    <label-personalizado campo="tipoConta" :tela="nomeTela" :apenasNome="mostrarApenasNome"/>
                </template>
                <template v-slot:header.tipoBem>
                    <label-personalizado campo="tipoBem" :tela="nomeTela" :apenasNome="mostrarApenasNome"/>
                </template>
                <template v-slot:header.vidaUtil>
                    <label-personalizado
                        campo="vidaUtil"
                        :tela="nomeTela"
                        mensagemPadraoTooltip="Vida útil em meses"
                        apresentaTooltip
                        :apenas-nome="mostrarApenasNomeCamposObrigatorios"/>
                </template>
                <template v-slot:header.percentualResidual>
                    <label-personalizado campo="residual" complementoLabel="(%)" :tela="nomeTela" :apenas-nome="mostrarApenasNomeCamposObrigatorios"/>
                </template>
                <template v-slot:item="{item}">
                    <ContasContabeisListagemform
                        v-model="item"
                        :permitir-acoes="permitirAcoes"
                        @cancelarEdicao="cancelarEdicao"
                        @desativarAcoes="desativarAcoes"
                        @habilitarAcoes="habilitarAcoes"
                        @paginar="tratarPaginacao"
                        @salvar="tratarEventoSalvar"
                        @alterarTipo="alterarTipo"/>
                </template>
            </v-data-table>
            <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage"/>
        </az-form>
    </az-container>
</template>

<script>
    import ContasContabeisListagemform from './ContaContabilListagemForm'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import Paginacao from '@/views/components/tabela/Paginacao'

    export default {
        name: 'ContasContabeisListagem',
        props: [
            'value',
            'paginacao',
            'paginas',
            'totalItens',
            'colunasTable',
            'todasContasContabeis',
        ],
        components: {Paginacao, ContasContabeisListagemform,LabelPersonalizado},
        data() {
            return {
                paginacaoInterna: this.paginacao,
                permitirAcoes: true,
                nomeTela: 'CONTA_CONTABIL',
                modoEdicao: false,
                tipo: ''
            }
        },
        computed: {
            mostrarApenasNome() {
                return !this.modoEdicao
            },
            mostrarApenasNomeCamposObrigatorios() {
                return !(this.modoEdicao && this.tipo === 'DEPRECIAVEL')
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
            cancelarEdicao() {
                this.habilitarAcoes()
                this.alterarTipo('')
                this.$emit('cancelarEdicao')
            },
            desativarAcoes() {
                this.permitirAcoes = false
                this.modoEdicao = true
            },
            habilitarAcoes() {
                this.permitirAcoes = true
                this.modoEdicao = false
            },
            resetaPage() {
                this.$store.state.contaContabil.todasContasContabeis.paginacao.page = 1
            },
            tratarPaginacao(pagina) {
                this.habilitarAcoes()
                this.paginacaoInterna.page = pagina
            },
            async tratarEventoSalvar(contaContabil) {
                this.habilitarAcoes()
                this.$emit('salvar', contaContabil)
            },
            alterarTipo(tipo) {
                this.tipo = tipo
            }
        },
    }
</script>

<style lang="stylus">
    .contasContabeis
        .az-table-list
            .v-text-field__details
                display flex !important
                margin-top 2px

            th
                font-size 13px !important
                font-weight bold
                color #666666 !important

            tr
                height 55px !important

            td
                margin-top 15px !important
                padding 0 10px !important
                overflow hidden

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

            tbody tr:hover
                button
                    visibility visible
                    margin-top 3px
                    margin-right 2%

            button
                visibility hidden
                text-align center !important

        .v-data-table tbody tr:after
            cursor pointer !important
</style>
