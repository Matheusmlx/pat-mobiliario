<template>
    <v-data>
        <tr>
            <template v-if="!modoEdicao">
                <td>{{value.codigo}}</td>
                <td class="formatContaContabil">{{value.descricao}}</td>
                <td>{{value.tipo | azEnum(tipoContaContabil)}}</td>
                <td>{{value.tipoBem | azEnum(tipoBem)}}</td>
                <td>{{value.vidaUtil ? value.vidaUtil : ''}}</td>
                <td>{{value.percentualResidual ?  $options.filters.valorParaPorcentagem(value.percentualResidual) : ''}}</td>
                <td>
                    <botao-icone-editar v-if="permitirAcoes" @ativarEdicao="ativarModoEdicao" />
                </td>
            </template>
            <template v-if="modoEdicao" class="grey">
                <td class="v-data-table_selected">{{value.codigo}}</td>
                <td class="formatContaContabil v-data-table_selected">{{value.descricao}}</td>
                <td class="v-data-table_selected">
                    <az-combo-enum
                        v-model="value.tipo"
                        class="input_conta_contabil"
                        name="tipo"
                        is-required
                        :enum-object="tipoContaContabil"
                        :insert-null-item="false"
                        placeholder="Selecione"
                        @input="mudarTipo"/>
                </td>
                <td class="v-data-table_selected">
                    <az-combo-enum
                        v-model="value.tipoBem"
                        class="input_conta_contabil"
                        name="tipoBem"
                        is-required
                        :enum-object="tipoBem"
                        :insert-null-item="false"
                        placeholder="Selecione"/>
                </td>
                <td class="v-data-table_selected">
                    <v-text-field
                        v-if="depreciavel"
                        v-model="value.vidaUtil"
                        maxlength="6"
                        type="number"
                        class="input_conta_contabil"
                        min="0"
                        name="vidaUtil"
                        v-mask="'#####'"
                        placeholder="Informe vida útil"
                        v-validate="value.tipo === 'DEPRECIAVEL' ? 'required|max:100' : ''"
                        :error-messages="errors.collect('vidaUtil')"/>
                </td>
                <td class="v-data-table_selected">
                    <az-money
                        v-if="depreciavel"
                        v-model="value.percentualResidual"
                        :required="value.tipo === 'DEPRECIAVEL'"
                        :maxLength="6"
                        placeholder="Informe % residual"
                        name="percentualResidual"
                        requerid-message="O campo é obrigatório"
                        :validation-field="validateRequeridAzMoney"
                        class="input_conta_contabil"
                        :prefix="''"
                        :suffix="'%'"/>
                </td>
                <td class="v-data-table_selected">
                    <acoes-icones-salvar-cancelar @salvar="salvar" @cancelar="cancelar"/>
                </td>
            </template>
        </tr>
    </v-data>
</template>

<script>
    import {mapState} from 'vuex'
    import AcoesIconesSalvarCancelar from '@/views/components/acoes/AcoesIconesSalvarCancelar'
    import BotaoIconeEditar from '@/views/components/botoes/BotaoIconeEditar'

    export default {
        name: 'ContaContabilListagemTabela',
        components:{BotaoIconeEditar, AcoesIconesSalvarCancelar},
        props: {
            value: {
                required: true
            },
            permitirAcoes: {
                required: true
            }
        },
        data() {
            return {
                modoEdicao: false,
                validateRequeridAzMoney: 0
            }
        },
        computed: {
            ...mapState(['contaContabil']),
            depreciavel() {
                return this.value.tipo !== 'NAO_DEPRECIAVEL'
            }
        },
        watch: {
            'value'() {
                this.desativarModoEdicao()
            },
            'contaContabil.todasContasContabeis.filtros.conteudo.value'() {
                this.habilitarAcoes()
                this.desativarModoEdicao()
            },
            'contaContabil.todasContasContabeis.paginacao.page'() {
                this.habilitarAcoes()
                this.desativarModoEdicao()
            },
            'value.percentualResidual'() {
                this.anulaPercentualSeMaiorQuePermitido()
            }
        },
        methods: {
            ativarModoEdicao() {
                this.modoEdicao = true
                this.$validator._base.reset()
                this.desativarAcoes()
                this.mudarTipo(this.value.tipo)
            },
            cancelar() {
                this.desativarModoEdicao()
                this.$emit('cancelarEdicao')
            },
            desativarAcoes() {
                this.$emit('desativarAcoes')
            },
            desativarModoEdicao() {
                this.modoEdicao = false
                this.$validator._base.reset()
            },
            habilitarAcoes() {
                this.$emit('habilitarAcoes')
            },
            async salvar() {
                this.validateRequeridAzMoney ++
                this.anulaCamposQueNaoDevemSerPreenchidos()
                this.anulaVidaUtilSeIgualAZero()
                this.anulaPercentualSeIgualAZero()

                if (await this.validarDadosFormulario()) {
                    this.desativarModoEdicao()
                    this.mostrarNotificacaoAviso('Atenção: alterar o tipo da conta contábil não afeta operações realizadas anteriormente')
                    this.$emit('salvar', this.value)
                }
            },
            async validarDadosFormulario() {
                return await this.$validator._base.validateAll()
            },
            mudarTipo(tipo) {
                this.$emit('alterarTipo', tipo)
            },
            anulaCamposQueNaoDevemSerPreenchidos() {
                if (this.value.tipo === 'NAO_DEPRECIAVEL') {
                    this.value.vidaUtil = null
                    this.value.percentualResidual = null
                }
            },
            anulaVidaUtilSeIgualAZero() {
                if (this.value.vidaUtil === '0') {
                    this.value.vidaUtil = null
                }
            },
            anulaPercentualSeMaiorQuePermitido() {
                if (this.value.percentualResidual > 99.99) {
                    this.value.percentualResidual = null
                    this.mostrarNotificacaoAviso('Percentual residual não deve ser maior que 99,99%.')
                }
            },
            anulaPercentualSeIgualAZero() {
                if (this.value.percentualResidual === 0) {
                    this.value.percentualResidual = null
                }
            }
        },
    }
</script>

<style lang="stylus">
    .input_conta_contabil
        padding-top 0 !important

    .desabilitarEdicao
        button
            visibility hidden !important

    .formatContaContabil
        font-size 13px !important

    .label_input
        font-size 15px
        font-weight bold
        color #777 !important

    .inputNome
        margin-top 3px
        padding-top 3px

    .botoes_acoes
        display inline-flex

        button
            margin 0 !important
            padding 0 !important
            visibility visible !important
            display inline-flex !important

    .max-20
        max-width 20vw

    @media (max-width 720px)
        .inputNome
            margin-top 0 !important

    .v-btn--icon.v-size--default .v-icon, .v-btn--fab.v-size--default .v-icon
        width 2px !important;
</style>
