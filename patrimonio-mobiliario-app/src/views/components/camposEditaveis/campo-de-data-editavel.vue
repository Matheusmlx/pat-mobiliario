<template>
    <div class="az-text az-text-edit az-text-view-edit-date campo-input-edit alinhamento-icone" @keyup.enter="submitEdit" >
        <div v-if="!editing">
            <label>{{ label }}</label>
            <p>
                <span v-if="value" class="formatoTexto">{{ $options.filters.azDate(value) }}</span>
                <span v-else> -</span>

                <v-tooltip top v-if="!disabled">
                    <template v-slot:activator="{ on }">
                        <v-btn v-on="on" icon class="start-edit-btn" @click="startEdit">
                            <v-icon class="mt-1" x-small >fas fa-edit</v-icon>
                        </v-btn>
                    </template>
                    {{labelBtnEdit}}
                </v-tooltip>
            </p>
        </div>

        <az-date
            v-if="editing"
            v-model="model"
            :label="label"
            date
            :name-date="name"
            :is-required="required"
            v-mask="mask"
            :max-date="maxDate"
            :min-date="minDate"
            :estaAdicionando="estaAdicionando"
            @input="$emit('verificaObrigatoriedade')">
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
        </az-date>
    </div>
</template>

<script>
    export default {
        name: 'campo-de-data-editavel',
        props: {
            value: {
                required: true
            },
            name: {
                required: true,
                type: String
            },
            idObjeto: {
                type: Number
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
            mask: {
                type: String,
                default: '##/##/####'
            },
            minDate: {
                type: String
            },
            maxDate: {
                type: String
            },
            estaAdicionando: {
                type: Boolean,
                default: false
            },
            editar:{
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
            editar(val){
                if(!this.model) {
                    if (val) {
                        this.startEdit(val)
                    } else {
                        this.cancelEdit()
                    }
                }
            }
        },
        methods: {
            startEdit(val) {
                if(this.estaAdicionando && val !== true){
                    this.$emit('estaAdicionando')
                }else{
                    this.$emit('verificaObrigatoriedade')
                    this.editing = true
                    this.$emit('setaEditando',{ nome: this.name, id: this.idObjeto })
                }
            },
            async submitEdit() {
                if(await this.validarDadosFormulario()){
                    this.editing = false
                    this.$emit('retiraEditando',{ nome: this.name, id: this.idObjeto })
                    this.$emit('input', this.model)
                }
            },
            async validarDadosFormulario() {
                const estaValidado = await this.$validator._base.validateAll()
                return estaValidado
            },
            cancelEdit() {
                this.editing = false
                this.$emit('retiraEditando',{ nome: this.name, id: this.idObjeto })
                this.model = this.value
            }
        }
    }
</script>

<style scoped lang="stylus">
    .formatoTexto
        color: #6b6b6b
</style>
