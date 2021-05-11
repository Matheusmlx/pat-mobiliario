<template>
    <v-row>
        <v-col cols="9" lg="11" xs="12">
            <v-card class="elevation-0 documento">
                <v-container fluid>
                    <v-form enctype="multipart/form-data" class="az-form-content pb-0" lazy-validation ref="form">
                        <v-row class="ma-0">
                            <v-col cols="12" lg="2" sm="12" xs="12" md="2" :class="{ bloquearCursor: !permissaoEdicao}">
                                <v-text-field
                                    v-model="value.numero"
                                    name="numero"
                                    maxlength="50"
                                    v-validate="'required'"
                                    :error-messages="errors.collect('numero')"
                                    :counter="50"
                                    :disabled="!permissaoEdicao"
                                    placeholder="Informe o número"
                                    @blur="tratarEventoEdicaoCampo">
                                    <template v-slot:label>
                                        <label-personalizado campo="numero" :tela="nomeTelaRotulos"
                                                             obrigatorioPorPadrao/>
                                    </template>
                                </v-text-field>
                            </v-col>
                            <v-col cols="12" lg="2" sm="12" xs="12" md="2" :class="{ bloquearCursor: !permissaoEdicao}">
                                <az-date
                                    v-model="value.data"
                                    date
                                    :is-disabled="!permissaoEdicao"
                                    v-mask="'##/##/####'"
                                    name-date="data"
                                    @input="tratarEventoEdicaoCampo"
                                    :is-required="todosCamposObrigatorios || getObrigatorioRotulosPersonalizados('data', nomeTelaRotulos)">
                                    <template v-slot:label-date>
                                        <label-personalizado campo="data" :tela="nomeTelaRotulos"
                                                             :permissaoEdicao="todosCamposObrigatorios || getObrigatorioRotulosPersonalizados('data', nomeTelaRotulos)"
                                                             :obrigatorioPorPadrao="todosCamposObrigatorios"/>
                                    </template>
                                </az-date>
                            </v-col>
                            <v-col cols="12" lg="2" sm="12" xs="12" md="2" @focusout="tratarEventoEdicaoCampo"
                                   :class="{ bloquearCursor: !permissaoEdicao}">
                                <az-money
                                    v-model="value.valor"
                                    :required="todosCamposObrigatorios || getObrigatorioRotulosPersonalizados('valor', nomeTelaRotulos)"
                                    requeridMessage="O campo é obrigatório"
                                    :disabled="!permissaoEdicao"
                                    :validation-field="validateRequeridAzMoney"
                                    :maxLength="18"
                                    :name="'valor'+index"
                                    placeholder="Informe o valor">
                                    <template v-slot:label>
                                        <label-personalizado campo="valor" :tela="nomeTelaRotulos"
                                                             :permissaoEdicao="todosCamposObrigatorios || getObrigatorioRotulosPersonalizados('valor', nomeTelaRotulos)"
                                                             :obrigatorioPorPadrao="todosCamposObrigatorios"/>
                                    </template>
                                </az-money>
                            </v-col>
                            <v-col cols="12" lg="3" sm="12" xs="12" md="2" :class="{ bloquearCursor: !permissaoEdicao}">
                                <v-autocomplete
                                    v-model="value.tipo"
                                    :items="tipoDocumento"
                                    item-text="descricao"
                                    item-value="id"
                                    name="tipo"
                                    no-data-text="Não há tipos com esse nome"
                                    placeholder="Selecione"
                                    :error-messages="errors.collect('tipo')"
                                    :disabled="!permissaoEdicao"
                                    v-validate="'required'"
                                    @keydown.enter="tratarEventoEdicaoCampo"
                                    @blur="tratarEventoEdicaoCampo"
                                    @input="tratarEventoEdicaoCampo">
                                    <template v-slot:label>
                                        <label-personalizado campo="tipo" :tela="nomeTelaRotulos" obrigatorioPorPadrao/>
                                    </template>
                                </v-autocomplete>
                            </v-col>
                            <v-col cols="12" lg="3" sm="12" xs="12" md="4" :class="{ bloquearCursor: !permissaoEdicao}">
                                <v-file-input
                                    class="mt-3"
                                    v-model="value.uriAnexo"
                                    v-if="!value.uriAnexo"
                                    name="uriAnexo"
                                    :error-messages="errorTamanhoExcedido === 1 ? 'Tamanho permitido: 15MB' : errors.collect('uriAnexo')"
                                    file
                                    dense
                                    outlined
                                    append-icon="fa-file-upload"
                                    v-validate="getObrigatorioRotulosPersonalizados('uriAnexo', this.nomeTelaRotulos) ? 'required' : ''"
                                    prepend-icon
                                    accept="image/png, image/jpeg, image/jpg, application/pdf"
                                    placeholder="Selecione o Arquivo"
                                    @change="tratarEventoSalvarUri">
                                    <template v-slot:label>
                                        <label-personalizado campo="uriAnexo" :tela="nomeTelaRotulos"/>
                                    </template>
                                </v-file-input>
                                <p class="tamanhoPermitidoClass"
                                   v-if=" !value.uriAnexo && errorTamanhoExcedido!==1 && errorObrigatoriedadeAnexo!==1">
                                    Tamanho permitido: 15MB</p>
                                <v-text-field
                                    outlined
                                    readonly
                                    v-if="value.uriAnexo"
                                    :value="nomeAnexo">
                                    <template v-slot:label>
                                        <label-personalizado campo="uriAnexo" :tela="nomeTelaRotulos"/>
                                    </template>
                                    <template v-slot:append>
                                        <v-tooltip top>
                                            <template v-slot:activator="{ on }">
                                                <a v-on="on"
                                                   slot="activator"
                                                   v-on:click="anularCampoAnexo"
                                                   target="_blank"
                                                   class="az-action-link-icon mt-1">
                                                    <i v-if="permissaoEdicao" class="fas fa-times-circle"></i>
                                                </a>
                                            </template>
                                            Remover
                                        </v-tooltip>
                                    </template>
                                    <template v-slot:append-outer>
                                        <v-tooltip v-if="value.id" top>
                                            <template v-slot:activator="{ on }">
                                                <a v-on="on"
                                                   slot="activator"
                                                   :href="criarLinkDownload(value.uriAnexo)"
                                                   target="_blank"
                                                   class="az-action-link-icon mt-1">
                                                    <i class="fas fa-download"></i>
                                                </a>
                                            </template>
                                            Download
                                        </v-tooltip>
                                    </template>
                                </v-text-field>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-container>
            </v-card>
        </v-col>
        <v-col align="center" class="mt-12" lg="1" md="1" ml-1 pl-5 sm="1" xs="12">
            <botao-remover
                :color="configBotaoRemover.color"
                :icon="configBotaoRemover.icon"
                :size="configBotaoRemover.size"
                @remover="removerDocumento"/>
        </v-col>
    </v-row>
</template>

<script>
    import {createNamespacedHelpers} from 'vuex'
    import BotaoRemover from '@/views/components/botoes/BotaoRemover'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        components: {BotaoRemover, LabelPersonalizado},
        props: {
            value: {
                default: () => {
                    return {}
                }
            },
            tipoDocumento: {
                type: Array,
                default: () => {
                    return []
                }
            },
            index: {
                type: Number
            },
            permissaoEdicao: {
                type: Boolean,
                required: true
            },
            configBotaoRemover: {
                type: Object,
                default: () => {
                    return {
                        icon: 'fas fa-minus-circle',
                        color: 'grey',
                        size: '22'
                    }
                }
            }
        },
        $_veeValidate: {
            validator: 'new',
        },
        data() {
            return {
                errorObrigatoriedadeAnexo: 0,
                errorTamanhoExcedido: 0,
                validateRequeridAzMoney: 0,
                regrasAnexo: '',
                nomeAnexo: '',
                apagar: true,
                todosCamposObrigatorios: false,
                nomeTelaRotulos: 'DOCUMENTOS'
            }
        },
        async mounted() {
            this.tratarCampoAnexo()
            await this.verificarCamposObrigatorios()
        },
        watch: {
            async 'value.uriAnexo'() {
                this.tratarCampoAnexo()
                if (typeof this.value.uriAnexo == 'string') {
                    await this.tratarEventoEdicaoCampo()
                }
            },
        },
        computed: {
            ...mapGetters([
                'getObrigatorioRotulosPersonalizados',
                'getDocumentoTodosObrigatoriosValidado',
                'getDocumentoNumeroTipoObrigatorioValidado'
            ])
        },
        methods: {
            tratarCampoAnexo() {
                if (typeof this.value.uriAnexo == 'string') {
                    this.nomeAnexo = this.value.uriAnexo.split('/')[1]
                }
            },
            anularCampoAnexo() {
                if (this.permissaoEdicao) {
                    this.value.uriAnexo = null
                    this.errorObrigatoriedadeAnexo = 0
                    this.errorTamanhoExcedido = 0
                }
            },
            async verificarCamposObrigatorios() {
                if (this.tipoDocumento && this.value && this.value.tipo) {
                    const documentoEncontrado = this.tipoDocumento.find(element => element.id === this.value.tipo)

                    if (documentoEncontrado &&
                        (documentoEncontrado.descricao === 'Nota Fiscal' || documentoEncontrado.descricao === 'Nota de Empenho')) {
                        this.todosCamposObrigatorios = true
                        this.validateRequeridAzMoney++
                        await this.$validator._base.validateAll()
                    } else {
                        this.todosCamposObrigatorios = false
                    }
                    return true
                }
                return false
            },
            async tratarEventoEdicaoCampo() {
                if (
                    this.todosCamposObrigatorios ||
                    this.getObrigatorioRotulosPersonalizados('valor', this.nomeTelaRotulos)
                ) {
                    this.validateRequeridAzMoney++
                }
                if (this.errors.collect('uriAnexo')) {
                    this.errorObrigatoriedadeAnexo = 1
                }
                const verificarCamposObrigatorios = await this.verificarCamposObrigatorios()
                if (verificarCamposObrigatorios && this.permissaoEdicao) {
                    this.retiraEspacosVazios()
                    await this.$validator._base.validateAll()

                    if (this.todosCamposObrigatorios) {
                        if (this.getDocumentoTodosObrigatoriosValidado(this.value, this.nomeTelaRotulos)) {
                            this.$emit('salvar', this.value)
                        }
                    } else {
                        if (this.getDocumentoNumeroTipoObrigatorioValidado(this.value, this.nomeTelaRotulos)) {

                            this.$emit('salvar', this.value)
                        }
                    }
                }
            },
            retiraEspacosVazios() {
                if (this.value.numero) {
                    this.value.numero = this.value.numero.trim()
                }
            },
            async tratarEventoSalvarUri() {
                if (this.value && this.value.uriAnexo) {
                    if (this.extensaoArquivoValida(this.value.uriAnexo) === -1) {
                        this.mostrarNotificacaoErro(
                            'Extensão do arquivo inválida. Envie arquivos nos seguintes formatos .pdf .jpg .png'
                        )
                        this.value.uriAnexo = null
                    } else {
                        if (this.validarTamanhoMaximo(this.value.uriAnexo.size)) {
                            this.errorTamanhoExcedido = 1
                            this.value.uriAnexo = await null
                            setTimeout(() => {
                                this.errorTamanhoExcedido = 0
                                this.errorObrigatoriedadeAnexo = 1
                                this.$validator._base.validateAll()
                            }, 3000)
                        } else {
                            await this.$emit('salvarUri')
                        }
                    }
                }
            },
            extensaoArquivoValida(nomeArquivo) {
                const extensaoAceitas = [
                    '.pdf',
                    '.PDF',
                    '.jpg',
                    '.JPG',
                    '.png',
                    '.PNG',
                    '.jpeg',
                    '.JPEG',
                ]
                const extensao = nomeArquivo.name.substr(
                    nomeArquivo.name.lastIndexOf('.'),
                    nomeArquivo.name.length
                )
                return extensaoAceitas.indexOf(extensao)
            },
            validarTamanhoMaximo(tamanhoAnexo) {
                return (tamanhoAnexo >= 15116247)
            },
            removerDocumento() {
                this.$emit('removerDocumento', this.value)
            }
        },
    }
</script>

<style lang="stylus" scoped>
    .bloquearCursor
        cursor not-allowed

    .documento
        border 1px solid #d9d9d9

    .tamanhoPermitidoClass
        color #a5a5a5
        margin-top -10px
        font-size 12px
</style>
