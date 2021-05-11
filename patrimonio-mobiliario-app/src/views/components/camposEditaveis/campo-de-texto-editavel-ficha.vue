<template>
    <div class="az-text-edit">
        <label-personalizado v-if="!editing" class="label" :campo="campo" :tela="tela" apenas-nome/>
        <div v-if="!editing">
            <v-row class="ml-0">
                <span v-if="placa" class="texto">{{ placaFormatada | textoSemValorSimples }}</span>
                <span v-else-if="chassi" class="texto">{{ chassiFormatado | textoSemValorSimples }}</span>
                <v-tooltip v-else-if="value && value.length > 26" top nudge-top max-width="800">
                    <template v-slot:activator="{ on }">
                        <div v-on="on">
                            <p class="mb-0 text-truncate texto max-10">{{value | textoSemValorSimples}}</p>
                        </div>
                    </template>
                    <p class="mb-0" >{{value | textoSemValorSimples}}</p>
                </v-tooltip>
                <span v-else class="texto">{{value | textoSemValorSimples}}</span>

                <v-tooltip top v-if="!disabled">
                    <template v-slot:activator="{ on }">
                        <v-btn v-on="on" x-small icon class="start-edit-btn ml-2 pt-1" @click="startEdit">
                            <v-icon  x-small>fas fa-edit</v-icon>
                        </v-btn>
                    </template>
                    {{ labelBtnEdit }}
                </v-tooltip>
            </v-row>
        </div>
        <label-personalizado v-if="editing" class="label" :campo="campo" :tela="tela" :apenas-nome="labelCustomizadoApenasNome"/>
        <v-text-field
            v-if="editing && !mask"
            v-model="model"
            :name="name"
            :maxlength="maxlength"
            :error-messages="errors.collect(name)"
            :counter="counter"
            :placeholder="placeholder"
            v-validate="validate"
            :data-vv-as="name"
            dense
            color="#777"
            class="input-texto"
            @input="model.toUpperCase()"
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
            :name="name"
            :maxlength="maxlength"
            :error-messages="errors.collect(name)"
            :counter="counter"
            :placeholder="placeholder"
            v-validate="validate"
            :data-vv-as="name"
            v-mask="mask"
            dense
            color="#777"
            class="input-texto"
            @input="model.toUpperCase()"
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
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    export default {
        inject: ['$validator'],
        name: 'campo-de-texto-editavel',
        components:{LabelPersonalizado},
        props: {
            value: {
                required: true
            },
            name: {
                required: true,
                type: String
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
            },
            disabled: {
                type: Boolean,
                default: false
            },
            campo:{
                type: String
            },
            tela:{
                type: String
            },
            placa:{
                type: Boolean,
                default: false
            },
            chassi: {
                type: Boolean,
                default: false
            },
            labelCustomizadoApenasNome: {
                type: Boolean,
                default: true
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
        computed:{
            placaFormatada(){
                if(this.model) {
                    if (this.model.charAt(3) !== '-') {
                        const placa1 = this.model.substr(0, 3)
                        const placa2 = this.model.substr(3, 7)
                        return placa1 + '-' + placa2
                    }
                    return this.model
                }
                return null
            },
            chassiFormatado(){
                if(this.model) {
                    if (this.model.charAt(3) !== ' ') {
                        const chassi1 = this.model.substr(0, 3)
                        const chassi2 = this.model.substr(3, 5)
                        const chassi3 = this.model.charAt(8)
                        const chassi4 = this.model.substr(9, 8)
                        return chassi1 + ' ' + chassi2 + ' ' + chassi3 + ' ' + chassi4
                    }
                    return this.model
                }
                return null
            }
        },
        methods: {
            startEdit() {
                this.editing = true
            },
            async submitEdit() {
                if (this.validarDadosFormulario()) {
                    this.editing = false
                    this.$emit('input', this.model)
                }
            },
            cancelEdit() {
                this.editing = false
                this.model = this.value
            },
            validarDadosFormulario() {
                const item = this.$validator._base.errors.items.find( item => item.field === this.name)
                return !item
            },
        }
    }
</script>

<style scoped lang="stylus">
    .texto
        font-size 13px
        color #777 !important
    .label
        font-size 13px
        font-weight bold
        color #777 !important

    >>>
        .input-texto
            margin-top -13px
            padding 8px 0 0
            font-size 13px

        .v-text-field__slot input
            color #777 !important
</style>
