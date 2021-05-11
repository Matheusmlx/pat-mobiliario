<template>
    <div class="az-text az-text-edit az-text-view-edit-money campo-input-edit alinhamento-icone">
        <div v-if="!editing">
            <label>{{ label }}</label>
            <p>
                <span v-if="value" class="formatoTextoValor">{{ value | azCurrency }}</span>
                <span v-else> - </span>
                <v-tooltip top v-if="!disabled">
                    <template v-slot:activator="{ on }">
                        <v-btn v-on="on" icon class="start-edit-btn" @click="startEdit">
                            <v-icon class="mt-1" x-small>fas fa-edit</v-icon>
                        </v-btn>
                    </template>
                    {{labelBtnEdit}}
                </v-tooltip>
            </p>
        </div>

        <az-money
            v-show="editing"
            v-model="model"
            :label="label"
            :name="name"
            :required="required"
            :validation-field="validationField"
            :requerid-message="requiredMessage"
            :placeholder="placeholder"
            :estaAdicionando="estaAdicionando"
            @keyupEnter="submitEdit"
            @input="anulaSeValorZero"
            @keyupEsc="cancelEdit">
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
        </az-money>
    </div>
</template>

<script>
    export default {
        name: 'campo-de-valor-editavel',
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
            required: {
                type: Boolean
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
            validationField: {
                type: Number
            },
            requiredMessage: {
                type: String,
                default: 'O campo é obrigatório'
            },
            editar: {
                type: Boolean
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
            editing: false
        }),
        watch: {
            value: {
                handler(val) {
                    this.model = val
                },
                immediate: true
            },
            async editar(val) {
                if(!this.model) {
                    if (val) {
                        this.startEdit(val)
                        this.required = true
                        this.validationField++
                    } else {
                        this.required = false
                        this.cancelEdit()
                    }
                }
                await this.$validator._base.validateAll()
            }
        },
        methods: {
            anulaSeValorZero() {
                this.$emit('verificaObrigatoriedade')
                if (this.model === 0) {
                    this.model = null
                }
            },
            startEdit(val) {
                if (this.estaAdicionando && val !== true) {
                    this.$emit('estaAdicionando')
                }else{
                    this.editing = true
                    this.$emit('setaEditando', {nome: this.name, id: this.idObjeto})
                }
            },
            async submitEdit() {
                if (await this.validarDadosFormulario()) {
                    this.editing = false
                    this.$emit('retiraEditando', {nome: this.name, id: this.idObjeto})
                    this.$emit('input', this.model)
                    this.$emit('change')
                }
            },
            async validarDadosFormulario() {
                const estaValidado = await this.$validator._base.validateAll()
                return estaValidado && (!this.required || this.model)
            },
            cancelEdit() {
                this.editing = false
                this.$emit('retiraEditando', this.name)
                this.model = this.value
            },
        }
    }
</script>

<style lang="stylus">
    .formatoTextoValor
        color: #6b6b6b

</style>
