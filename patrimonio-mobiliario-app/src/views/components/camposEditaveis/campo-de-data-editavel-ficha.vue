<template>
    <div class="az-text-edit az-text-view-edit-date campo-input-edit" @keyup.enter="submitEdit" >
        <div v-if="possuiCamposLabelCustomizavel && !editing">
            <label-personalizado class="font-weight-bold texto" :campo="campo" :tela="tela" apenas-nome/>
        </div>
        <div v-if="!editing">
            <v-row class="ml-0">
                <span class="texto">{{value | azDate }}</span>

                <v-tooltip top v-if="!disabled">
                    <template v-slot:activator="{ on }">
                        <v-btn v-on="on" x-small icon class="edit-btn ml-2 pt-1" @click="startEdit">
                            <v-icon  x-small>fas fa-edit</v-icon>
                        </v-btn>
                    </template>
                    {{ labelBtnEdit }}
                </v-tooltip>
            </v-row>
        </div>

        <div v-if="possuiCamposLabelCustomizavel && editing">
            <label-personalizado class="font-weight-bold texto" :campo="campo" :tela="tela"/>
        </div>
        <az-date
            class="editable-date"
            v-if="editing"
            v-model="model"
            :label="label"
            date
            :name-date="name"
            :is-required="required"
            v-mask="mask"
            :max-date="maxDate"
            :min-date="minDate">
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
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'

    export default {
        name: 'campo-de-data-editavel-ficha',
        components: {
            LabelPersonalizado
        },
        props: {
            value: {
                required: true
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
            editar:{
                type: Boolean
            },
            disabled: {
                type: Boolean,
                default: false
            },
            campo: {
                type: String,
                default: ''
            },
            tela: {
                type: String,
                default: ''
            }
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
                if(val){
                    this.startEdit()
                }else{
                    this.cancelEdit()
                }
            }
        },
        computed: {
            possuiCamposLabelCustomizavel() {
                return this.campo !== '' && this.tela !== ''
            }
        },
        methods: {
            startEdit() {
                this.editing = true
            },
            async submitEdit() {
                if (await this.validarDadosFormulario()){
                    this.editing = false
                    this.$emit('input', this.model)
                }
            },
            async validarDadosFormulario() {
                return await this.$validator.validateAll()
            },
            cancelEdit() {
                this.editing = false
                this.model = this.value
            }
        }
    }
</script>

<style scoped lang="stylus">
    .texto
        font-size 13px
        color #777 !important

    .edit-btn
        opacity .4
        top -3px

        &:hover
            opacity 1

    .editable-date
        >>>
        .az-date
            margin-top -25px

            .v-input__slot::after
                border-color #777 !important
</style>
