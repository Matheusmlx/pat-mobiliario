<template>
    <div class="empenho">
        <div class="empenhoForm">
            <v-row class="form">
                <v-col cols="12" md="4" sm="6" xs="12" :class="[{active: !verificaPermissaoEdicao}, 'empenho']">
                    <v-text-field
                        v-model="empenho.numeroEmpenho"
                        :disabled="!verificaPermissaoEdicao || statusReconhecimento"
                        :class="{desativado: !verificaPermissaoEdicao}"
                        :name="'numeroEmpenho'"
                        :placeholder="verificaPermissaoEdicao ? 'Informe o número' : ' '"
                        maxlength="50"
                        @change="editarEmpenho"
                        v-validate=" obrigatorio ? 'required' : ''"
                        :error-messages="errors.collect('numeroEmpenho')">
                        <template v-slot:label>
                            <label-personalizado campo="numeroEmpenho" :tela="nomeTela" :permissaoEdicao="obrigatorio && verificaPermissaoEdicao"
                                                 obrigatorioPorPadrao/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="12" md="4" sm="6" xs="12">
                    <az-date
                        v-model="empenho.dataEmpenho"
                        :is-disabled="!verificaPermissaoEdicao || statusReconhecimento"
                        :name-date="'dataEmpenho'"
                        :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                        :is-required="obrigatorio"
                        date
                        :placeholderDate="(empenho.dataEmpenho) ? ' ' : placeholders.date"
                        @input="editarEmpenho">
                        <template v-slot:label-date>
                            <label-personalizado campo="dataEmpenho" :tela="nomeTela" :permissaoEdicao="obrigatorio && verificaPermissaoEdicao"
                                                 obrigatorioPorPadrao/>
                        </template>
                    </az-date>
                </v-col>
                <v-col cols="12" md="4" sm="6" xs="12">
                    <az-money
                        v-model="empenho.valorEmpenho"
                        :maxLength="18"
                        :name="'valorEmpenho'"
                        :event-submit="'blur'"
                        :required="obrigatorio"
                        requeridMessage="O campo é obrigatório"
                        class="botaoExcluirMoney"
                        :placeholder="verificaPermissaoEdicao ? 'Informe o valor' : ' '"
                        :disabled="!verificaPermissaoEdicao || statusReconhecimento"
                        :class="{desativado: !verificaPermissaoEdicao}"
                        v-validate=" obrigatorio ? 'required' : ''"
                        @input="atualizarValorInserido($event)">
                        <template v-slot:label>
                            <label-personalizado campo="valorEmpenho" :tela="nomeTela" :permissaoEdicao="obrigatorio && verificaPermissaoEdicao"
                                                 obrigatorioPorPadrao/>
                        </template>
                    </az-money>
                </v-col>
            </v-row>
        </div>
        <v-col cols="2" class="botoesEmpenho">
            <acoes-icones-circular-adicionar-remover
                :desabilitar-adicionar="!verificaPermissaoEdicao || !this.camposPreenchidos"
                :desabilitar-remover="!verificaPermissaoEdicao || quantidadeEmpenhos ===1"
                :ocultar-adicionar="primeiroEmpenho && quantidadeEmpenhos <10"
                @remover="remover"
                @adicionar="adicionarNovoEmpenho"/>
        </v-col>
    </div>
</template>

<script>
    import {mapActions} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import {actionTypes} from '@/core/constants'
    import moment from 'moment'
    import AcoesIconesCircularAdicionarRemover from '@/views/components/acoes/AcoesIconesCircularAdicionarRemover'

    export default {
        name: 'Empenho',
        components:{AcoesIconesCircularAdicionarRemover, LabelPersonalizado},
        props: {
            value: Object,
            obrigatorio: {
                type: Boolean,
                default: false
            },
            statusReconhecimento:{
                type: Boolean,
                default: true
            },
            primeiroEmpenho: {
                type: Boolean,
                default: false
            },
            idIncorporacao:{
                required: true
            },
            quantidadeEmpenhos: {
                required: true
            }
        },
        data() {
            return {
                placeholders: {
                    date: '__/__/____',
                },
                empenho: this.value,
                nomeTela: 'INCORPORACAO_DADOS_GERAIS'
            }
        },
        computed: {
            camposPreenchidos() {
                return this.empenho.numeroEmpenho && this.empenho.dataEmpenho && this.empenho.valorEmpenho
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
            },
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.INSERIR_EMPENHO,
                actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.ATUALIZAR_EMPENHO,
                actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.REMOVER_EMPENHO
            ]),
            atualizarValorInserido(valor) {
                this.empenho.valorEmpenho = valor
                this.editarEmpenho()
            },
            async editarEmpenho() {
                if (this.camposPreenchidos) {
                    await this.desabilitarLoadingGlobal()
                    if (this.empenho.id) {
                        await this.editar()
                    } else {
                        await this.salvar()
                    }
                    await this.habilitarLoadingGlobal()
                    this.$emit('buscarEmpenhos')
                }
            },
            async editar() {
                if (this.empenho.dataEmpenho !== null) {
                    this.empenho.dataEmpenho = moment(this.empenho.dataEmpenho).format('YYYY-MM-DDTHH:mm:ss.SSSZZ')
                }
                this.empenho.incorporacaoId = this.idIncorporacao
                await this.atualizarEmpenho(this.empenho)

            },
            async salvar() {
                await this.inserirEmpenho(this.empenho)
            },
            async adicionarNovoEmpenho() {
                if (this.quantidadeEmpenhos >= 10) {
                    this.mostrarNotificacaoAviso('Não é possível cadastrar mais que 10 empenhos!')
                }
                if (this.camposPreenchidos && this.quantidadeEmpenhos < 10) {
                    this.$emit('adicionarNovoEmpenho')
                }
            },
            async remover() {
                if (this.empenho.id) {
                    await this.removerEmpenho(this.empenho.id)
                }
                this.$emit('removerEmpenho', this.empenho.id)
            }
        }
    }
</script>

<style scoped lang="stylus">
    .form
        margin 0

    .empenho
        display flex
        align-items center
        width 100%

        .empenhoForm
            width 100%

    [class^="col"]
        padding 15px 12px 0 15px

    .botoesEmpenho
        max-width: 100px
</style>
