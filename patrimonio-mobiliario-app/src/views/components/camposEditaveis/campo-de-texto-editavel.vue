<template>
    <div :class="[{'text-input-modal ': tipo !== 'DOCUMENTO', 'az-text-edit alinhamento-icone': tipo === 'DOCUMENTO'}, 'az-text campo-input-edit']">
        <div v-if="!editing" >
            <label>{{ label }}</label>
            <v-card class="elevation-0">
                <v-card-text class="pa-0">
                    <p class="formatoTexto" :class="[{'text-truncate max-8' : tipo === 'DOCUMENTO' }, {'text-truncate max-text' : tipo !== 'DOCUMENTO' }, 'd-inline-block  ']" >
                        {{ value? value : '-' }}
                    </p>
                    <v-tooltip top v-if="!disabled">
                        <template v-slot:activator="{ on }">
                            <v-btn v-on="on" icon class="start-edit-btn" @click="startEdit">
                                <v-icon class="mt-1" x-small>fas fa-edit</v-icon>
                            </v-btn>
                        </template>
                        {{labelBtnEdit}}
                    </v-tooltip>
                </v-card-text>
            </v-card>
        </div>

        <v-text-field
            v-show="editing && (!mask || mask === '')"
            v-model="model"
            :label="label"
            :name="name"
            :maxlength="maxlength"
            :error-messages="errors.collect(name)"
            :counter="counter"
            :placeholder="placeholder"
            v-validate="validate"
            :data-vv-as="name"
            @keyup.enter="submitEdit"
            @keyup.esc="cancelEdit"
            @click:append="cancelEdit"
            @click:clear="submitEdit">
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
        </v-text-field>

        <v-text-field
            v-if="editing && mask"
            v-model="model"
            :label="label"
            :name="name"
            :maxlength="maxlength"
            :error-messages="errors.collect(name)"
            :counter="counter"
            :placeholder="placeholder"
            v-validate="validate"
            :data-vv-as="name"
            v-mask="mask"
            @keyup.enter="submitEdit"
            @keyup.esc="cancelEdit"
            @click:append="cancelEdit"
            @click:clear="submitEdit">
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
        </v-text-field>
    </div>
</template>

<script>
    export default {
        inject: ['$validator'],
        name: 'campo-de-texto-editavel',
        props: {
            value: {
                required: true
            },
            idObjeto: {
                type: Number
            },
            name: {
                required: true,
                type: String
            },
            tipo: {
                type: String,
                default: 'DOCUMENTO'
            },
            label: {
                type: String,
                default: ''
            },
            labelBtnSave: {
                type: String,
                default: 'Salvar'
            },
            labelBtnCancel: {
                type: String,
                default: 'Cancelar'
            },
            labelBtnEdit: {
                type: String,
                default: 'Editar'
            },
            maxlength: {
                default: 255
            },
            validate: {
                type: String
            },
            counter: {
                type: Number
            },
            placeholder: {
                type: String
            },
            mask: {
                type: String,
                default: ''
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
        data() {
            return {
                model: undefined,
                editing: false
            }
        },
        watch: {
            value: {
                handler(val) {
                    this.model = val
                },
                immediate: true
            }
        },
        methods: {
            startEdit() {
                if (this.estaAdicionando) {
                    this.$emit('estaAdicionando')
                }else{
                    this.editing = true
                    this.$emit('setaEditando', {nome: this.name, id: this.idObjeto})
                }
            },
            async submitEdit() {
                if (this.validarDadosFormulario()) {
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
                const item = this.$validator._base.errors.items.find( item => item.field === this.name)
                return !item
            },
        }
    }
</script>

<style lang="stylus">
    .formatoTexto
        font-size 13px
        color #777 !important

    .az-text-edit
        label
            top -3px
            position relative

        .v-text-field input
            padding 8px 0 0

        .start-edit-btn
            opacity .4

            &:hover
                opacity 1

            .v-btn__content
                top -3px

    .alinhamento-icone
        .start-edit-btn
            top -5px

    .max-8
        max-width 8vw

    .max-text
        max-width 8vw
        margin-bottom 0px !important
        color #838383

    .text-input-modal
        label
            top -3px
            position relative

        .v-text-field
            margin-top -13px
            padding 0px
        .v-text-field input
            padding 8px 0 0

        .start-edit-btn
            top -5px
            opacity .4

            &:hover
                opacity 1

            .v-btn__content
                top -3px
</style>
