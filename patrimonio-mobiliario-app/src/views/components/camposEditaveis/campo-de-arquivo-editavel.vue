<template>
    <div class="az-text az-text-edit az-text-view-edit-text-area alinhaMentoBox">
        <div v-if="!editing">
            <label>{{ label }}</label>
            <p>
                <span class="d-inline-block truncate formatoTexto">{{nomeAnexo}}</span>
                <v-tooltip top v-if="!disabled">
                    <template v-slot:activator="{ on }">
                        <v-btn x-small v-on="on" class="start-edit-btn" icon @click="startEdit">
                            <v-icon class="mt-2" x-small>fas fa-edit</v-icon>
                        </v-btn>
                    </template>
                    {{labelBtnEdit}}
                </v-tooltip>
                <v-tooltip v-if="value" top>
                    <template v-slot:activator="{ on }">
                        <v-btn x-small v-on="on" icon slot="activator" :href="criarLinkDownload(value)" target="_blank">
                            <v-icon x-small color="primary" class="mb-1 d-flex align-start">fas fa-download</v-icon>
                        </v-btn>
                    </template>
                    Download
                </v-tooltip>
            </p>
        </div>

        <div :class="{'pt-7' : editing}">
            <v-file-input
                v-if="editing"
                v-model="model"
                class="file-input-component"
                :label="label"
                :name="name"
                dense
                outlined
                :class="stylus"
                :maxlength="maxlength"
                :error-messages="errorTamanhoExcedido === 1 ? 'Tamanho permitido: 15MB' : errors.collect(name)"
                v-validate="validate"
                :counter="counter"
                :placeholder="placeholder"
                :estaAdicionando="estaAdicionando"
                auto-grow
                prepend-icon
                rows="1"
                :clearable="false"
                @keyupEsc="cancelEdit"
                @click:append="cancelEdit">
                <template v-slot:append>
                    <v-tooltip top>
                        <template v-slot:activator="{ on }">
                            <v-btn v-on="on" icon @click="submitEdit">
                                <v-icon class="primary--text" small>fas fa-check</v-icon>
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
            </v-file-input>
            <p v-if="editing && !this.model && errorTamanhoExcedido!==1" class="tamanhoPermitidoClass">Tamanho
                permitido: 15MB</p>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'campo-de-arquivo-editavel',
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
            stylus: {
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
                type: Number,
                default: 255
            },
            validate: {
                type: String,
                default: 'required'
            },
            counter: {
                type: Number
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
            errorObrigatoriedadeAnexo: 0,
            errorTamanhoExcedido: 0,
        }),
        watch: {
            value(val) {
                this.model = val
            }
        },
        methods: {
            startEdit() {
                this.model = null
                if(this.estaAdicionando){
                    this.$emit('estaAdicionando')
                }else{
                    this.editing = true
                    this.$emit('setaEditando', {nome: this.name, id: this.idObjeto})
                }
            },
            async submitEdit() {
                if (this.model) {
                    const anexo = this.model
                    if(this.extensaoArquivoValida(anexo) === -1){
                        this.mostrarNotificacaoErro('Extensão do arquivo inválida. Envie arquivos nos seguintes formatos .pdf .jpg .png')
                        this.model = null
                    } else {
                        if (this.validarTamanhoMaximo(this.model.size)) {
                            this.errorTamanhoExcedido = 1
                            this.model = await null
                            setTimeout(() => {
                                this.errorTamanhoExcedido = 0
                                this.errorObrigatoriedadeAnexo = 1
                                this.$validator._base.validateAll()
                            }, 3000)
                        } else {
                            this.editing = false
                            this.$emit('retiraEditando', {nome: this.name, id: this.idObjeto})
                            this.$emit('input', anexo)
                        }

                    }
                } else {
                    this.mostrarNotificacaoErro('Sem documento anexo para salvar')
                }
            },
            validarTamanhoMaximo(tamanhoAnexo) {
                if (tamanhoAnexo >= 15116247) {
                    return true
                }
                return false
            },
            extensaoArquivoValida(nomeArquivo) {
                const extensaoAceitas = ['.pdf', '.PDF', '.jpg', '.JPG', '.png', '.PNG', '.jpeg', '.JPEG']
                const extensao = nomeArquivo.name.substr(nomeArquivo.name.lastIndexOf('.'), nomeArquivo.name.length)
                return extensaoAceitas.indexOf(extensao)
            },

            cancelEdit() {
                this.editing = false
                this.$emit('retiraEditando', {nome: this.name, id: this.idObjeto})
            },
        },
        computed: {
            nomeAnexo() {
                return typeof this.value === 'string' ? this.value.split('/')[1] : '-'
            }
        }
    }
</script>

<style lang="stylus" scoped>
    alinhaMentoBox
        margin-bottom: 20px

    .formatoTexto
        color: #6b6b6b

    .file-input-component
        font-size 13px

    .truncate
        width: 130px
        white-space: nowrap
        overflow: hidden
        text-overflow: ellipsis

    @media (max-width: 1100px)
        .truncate
            width 125px

</style>
