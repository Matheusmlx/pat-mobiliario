<template>
    <div
        :class="['az-text az-text-edit az-text-view-edit-auto-complete', {'alinhamento-icone' : tipo === 'DOCUMENTO'}]">
        <div v-if="!editing" :class="{autoCompleteFornecedorTexto : textoAzTextView}">
            <label>{{ label }}</label>
            <span class="formatoTexto" v-if="model && tipo === 'DOCUMENTO'">{{setarTipoDocumento().descricao}}</span>
            <span class="formatoTexto" v-if="model && tipo !== 'DOCUMENTO'">{{model}}</span>
            <span v-if="!model"> - </span>
            <v-tooltip top v-if="!disabled">
                <template v-slot:activator="{ on }">
                    <v-btn v-on="on" icon class="start-edit-btn" @click="startEdit">
                        <v-icon class="mt-1" x-small>fas fa-edit</v-icon>
                    </v-btn>
                </template>
                {{labelBtnEdit}}
            </v-tooltip>
        </div>
        <div v-else class="campo-input-edit">
            <v-col cols="2" md="12" sm="12" xs="7"
                   :class="{'text-select-modal' : tipo !== 'DOCUMENTO', 'py-0 my-0' : tipo === 'DOCUMENTO'}">
                <v-autocomplete
                    v-model="model"
                    :label="label"
                    :items="items"
                    item-value="id"
                    item-text="descricao"
                    :no-data-text="noDataText"
                    :name="name"
                    :error-messages="errors.collect(name)"
                    :search-input.sync="verificarCampoLimpo"
                    v-validate="validate"
                    :placeholder="placeholder"
                    :estaAdicionando="estaAdicionando"
                    @keyup.enter="submitEdit"
                    @keyup.esc="cancelEdit">
                    <template v-slot:label v-if="this.$slots['label']">
                        <slot name="label"/>
                    </template>
                    <template v-slot:append>
                        <v-tooltip top>
                            <template v-slot:activator="{ on }">
                                <v-btn v-on="on" icon @click="submitEdit">
                                    <v-icon small class="primary--text">fas fa-check</v-icon>
                                </v-btn>
                            </template>
                            {{labelBtnSave}}
                        </v-tooltip>
                        <v-tooltip top>
                            <template v-slot:activator="{ on }">
                                <v-btn v-on="on" icon @click="cancelEdit">
                                    <v-icon small>fas fa-times</v-icon>
                                </v-btn>
                            </template>
                            {{labelBtnCancel}}
                        </v-tooltip>
                    </template>
                </v-autocomplete>
            </v-col>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'campo-de-select-editavel',
        props: {
            value: {
                required: true
            },
            idObjeto: {
                type: Number
            },
            textoAzTextView: {
                type: String
            },
            iconClose: {
                type: Boolean,
                default: false
            },
            name: {
                required: true,
                type: String
            },
            items: {
                required: true,
                type: Array,
                default: () => {
                    return []
                }
            },
            tipo: {
                type: String,
                default: 'DOCUMENTO'
            },
            label: {
                type: String,
                default: ''
            },
            labelBtnCancel: {
                type: String,
                default: 'Cancelar'
            },
            labelBtnSave: {
                type: String,
                default: 'Salvar'
            },
            labelBtnEdit: {
                type: String,
                default: 'Editar'
            },
            maxlength: {
                type: Number,
                default: 255
            },
            validate: {
                type: String
            },
            placeholder: {
                type: String
            },
            returnObject: {
                type: Boolean,
                default: true
            },
            itemText: {
                type: String
            },
            itemValue: {
                type: String
            },
            formatValue: {
                type: Function
            },
            noDataText: {
                type: String,
                default: 'Não há tipos com esse nome'
            },
            search: {
                type: Function
            },
            estaAdicionando: {
                type: Boolean,
                default: false
            },
            disabled: {
                type: Boolean,
                default: false
            }
        },
        $_veeValidate: {
            validator: 'new'
        },
        created() {
            this.setarTipoDocumento()
        },
        data: () => ({
            model: undefined,
            editing: false,
            verificarCampoLimpo: null,
            campoLimpo: null
        }),
        watch: {
            value: {
                handler(val) {
                    this.model = val
                },
                immediate: true
            },
            verificarCampoLimpo(val) {
                this.campoLimpo = val
            },
        },
        methods: {
            setarTipoDocumento() {
                if (this.tipo === 'DOCUMENTO') {
                    return this.items.find(element => element.id === this.model)
                }
                return false
            },
            startEdit() {
                if (this.estaAdicionando) {
                    this.$emit('estaAdicionando')
                }else{
                    this.editing = true
                    this.$emit('setaEditando', {nome: this.name, id: this.idObjeto})
                }
            },
            submitEdit() {
                if(!this.campoLimpo && !this.validate) {
                    this.$validator.pause()
                    this.editing = false
                    this.$emit('retiraEditando', {nome: this.name, id: this.idObjeto})
                    this.$emit('input', null)
                } else if (this.validarDadosFormulario()) {
                    this.editing = false
                    this.$emit('retiraEditando', {nome: this.name, id: this.idObjeto})
                    this.$emit('input', this.model)
                }
            },
            cancelEdit() {
                this.editing = false
                this.$emit('retiraEditando', {nome: this.name, id: this.idObjeto})
                this.model = this.value
            },
            validarDadosFormulario() {
                const item = this.$validator._base.errors.items.find(item => item.field === this.name)
                return !item
            },
        },
    }
</script>

<style lang="stylus" scoped>
    .autoCompleteFornecedor
        max-height: 55px;

    .autoCompleteFornecedorTexto
        max-height: 50px;

    .formatoTexto
        font-size 13px
        color #777 !important

    .alinhaMentoBox
        margin-bottom: 20px

    .text-select-modal
        margin-top -23px
        padding 0px

    .campo-input-edit >>> .v-text-field input
        padding-top 0
        margin-bottom 5px
</style>
