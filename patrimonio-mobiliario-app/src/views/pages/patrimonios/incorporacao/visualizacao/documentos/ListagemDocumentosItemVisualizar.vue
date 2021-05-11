<template>
    <v-row>
        <v-col cols="9" lg="11" xs="12">
            <v-card class="elevation-0 documento">
                <v-container fluid>
                    <v-form enctype="multipart/form-data" class="az-form-content pb-0" lazy-validation ref="form">
                        <v-row class="ma-0">
                            <v-col cols="12" lg="2" sm="12" xs="12" md="2" :class="{ active: false}">
                                <v-text-field
                                    v-model="value.numero"
                                    name="Número"
                                    maxlength="50"
                                    v-validate="'required|max:50'"
                                    :counter="50"
                                    disabled
                                    placeholder=" ">
                                    <template v-slot:label>Número</template>
                                </v-text-field>
                            </v-col>
                            <v-col cols="12" lg="2" sm="12" xs="12" md="2" :class="{ active: false}">
                                <az-date
                                    v-model="value.data"
                                    date
                                    is-disabled
                                    v-mask="'##/##/####'"
                                    name-date="Data">
                                    <template v-slot:label-date>Data</template>
                                </az-date>
                            </v-col>
                            <v-col cols="12" lg="2" sm="12" xs="12" md="2" :class="{ active: false}">
                                <az-money
                                    v-model="value.valor"
                                    requeridMessage="O campo é obrigatório"
                                    :validation-field="validateRequeridAzMoney"
                                    disabled
                                    :name="'valorTotal'+index"
                                    placeholder=" ">
                                    <template v-slot:label>Valor</template>
                                </az-money>
                            </v-col>
                            <v-col cols="12" lg="3" sm="12" xs="12" md="2" :class="{ active: false}">
                                <v-autocomplete
                                    v-model="value.tipo"
                                    :items="tipoDocumento"
                                    item-text="descricao"
                                    item-value="id"
                                    name="Tipo"
                                    no-data-text="Não há tipos com esse nome"
                                    disabled
                                    placeholder=" "
                                    v-validate="'required'">
                                    <template v-slot:label>Tipo</template>
                                </v-autocomplete>
                            </v-col>
                            <v-col cols="12" lg="3" sm="12" xs="12" md="4" :class="{ active: false}">
                                <v-file-input
                                    class="mt-3"
                                    v-if="!value.uriAnexo"
                                    v-model="value.uriAnexo"
                                    name="Anexo"
                                    file
                                    dense
                                    outlined
                                    append-icon="fa-file-upload"
                                    prepend-icon
                                    accept="image/png, image/jpeg, image/jpg, application/pdf"
                                    placeholder=" "
                                    disabled>
                                    <template v-slot:label>Anexo</template>
                                </v-file-input>
                                <p class="tamanhoPermitidoClass"
                                    v-if=" !value.uriAnexo && errorTamanhoExcedido!=1 && errorObrigatoriedadeAnexo!=1">Tamanho permitido: 15MB</p>
                                <v-text-field
                                    outlined
                                    readonly
                                    disabled
                                    v-if="value.uriAnexo"
                                    :value="nomeAnexo">
                                    <template v-slot:label>Anexo</template>
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
                :color="'grey'"
                desabilitado
                size="22"
                icon="fas fa-minus-circle"/>
        </v-col>
    </v-row>
</template>

<script>
    import {createNamespacedHelpers} from 'vuex'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'ListagemDocumentosItemVisualizar',
        props: {
            value: {
                default: () => {
                    return {}
                },
            },
            tipoDocumento: {
                type: Array,
                default: () => {
                    return []
                },
            },
            index: {
                type: Number,
            },
        },
        $_veeValidate: {
            validator: 'new',
        },
        data() {
            return {
                EhUsuarioNormal: true,
                errorObrigatoriedadeAnexo: 0,
                errorTamanhoExcedido: 0,
                regrasAnexo: '',
                nomeAnexo: '',
                apagar: true,
                validateRequeridAzMoney: 0,
                valorDataObrigatorio: false,
            }
        },
        mounted() {
            this.tratarCampoAnexo()
        },
        computed: {
            ...mapGetters([
                'getNomeRotulosPersonalizados',
                'getObrigatorioRotulosPersonalizados',
            ]),
        },
        methods: {
            tratarCampoAnexo() {
                if (typeof this.value.uriAnexo == 'string') {
                    this.nomeAnexo = this.value.uriAnexo.split('/')[1]
                }
            }
        },
    }
</script>

<style lang="stylus" scoped>
    .active
        cursor not-allowed

    .documento
        border 1px solid #d9d9d9

    .tamanhoPermitidoClass
        color #a5a5a5
        margin-top -10px
        font-size 12px
</style>
