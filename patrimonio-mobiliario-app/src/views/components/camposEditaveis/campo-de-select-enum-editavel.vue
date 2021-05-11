<template>
    <div class="az-text az-text-edit az-text-view-edit-auto-complete alinhaMentoBox">
        <div v-if="!editing" :class="{autoCompleteFornecedorTexto : textoAzTextView}">
            <label>{{ label }}</label>
            <span class="formatoTexto" v-if="model">{{model | azEnum(items) }}</span>
            <span v-else> - </span>
            <v-tooltip top v-if="!disabled">
                <template v-slot:activator="{ on }">
                    <v-btn v-on="on" icon class="start-edit-btn" @click="startEdit">
                        <v-icon class="mt-1" x-small >fas fa-edit</v-icon>
                    </v-btn>
                </template>
                {{labelBtnEdit}}
            </v-tooltip>
        </div>
        <div v-else class="campo-input-edit">
            <v-col cols="2" md="12" sm="12" xs="7" class="text-select-enum-modal">
                <az-combo-enum
                    v-model="model"
                    :placeholder="placeholder"
                    :name="name"
                    :order-by="orderBy"
                    :is-required="validate"
                    :enum-object="items"
                    :insertNullItem="insertNullItem"
                    @keyup.enter="submitEdit"
                    @keyup.esc="cancelEdit">
                    <template v-slot:label v-if="this.$slots['label']">
                        <slot name="label"/>
                    </template>
                    <template v-slot:append>
                        <v-tooltip top>
                            <template v-slot:activator="{ on }">
                                <v-btn v-on="on" icon @click="submitEdit">
                                    <v-icon small class="primary--text" >fas fa-check</v-icon>
                                </v-btn>
                            </template>
                            {{labelBtnSave}}
                        </v-tooltip>
                        <v-tooltip top>
                            <template v-slot:activator="{ on }">
                                <v-btn v-on="on" icon @click="cancelEdit">
                                    <v-icon small >fas fa-times</v-icon>
                                </v-btn>
                            </template>
                            {{labelBtnCancel}}
                        </v-tooltip>
                    </template>
                </az-combo-enum>
            </v-col>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'campo-de-select-enum-editavel',
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
            orderBy: {
                type: String,
                default: 'text'
            },
            insertNullItem: {
                type: Boolean,
                default: true
            },
            name: {
                required: true,
                type: String
            },
            items: {
                required: true,
                type: Object,
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
            validate: {
                type: String
            },
            placeholder: {
                type: String
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
        data: () => ({
            model: undefined,
            editing: false,
        }),
        watch: {
            value: {
                handler(val) {
                    this.model = val
                },
                immediate: true
            },
        },
        methods: {
            startEdit() {
                if(this.estaAdicionando){
                    this.$emit('estaAdicionando')
                }
                this.editing = true
                this.$emit('setaEditando',{ nome: this.name, id: this.idObjeto })
            },
            submitEdit() {
                this.editing = false
                this.$emit('retiraEditando',{ nome: this.name, id: this.idObjeto })
                this.$emit('input',this.model)
            },
            cancelEdit() {
                this.editing = false
                this.$emit('retiraEditando',{ nome: this.name, id: this.idObjeto })
                this.model = this.value
            },
        }
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

    .text-select-enum-modal
        margin-top -23px
        padding 0px

    .campo-input-edit >>> .v-text-field .v-select__selection--comma
            margin-bottom 0
</style>
