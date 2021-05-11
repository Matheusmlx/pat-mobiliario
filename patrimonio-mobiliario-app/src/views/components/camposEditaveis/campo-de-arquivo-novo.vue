<template>
    <div class="ajusteInput campo-input-edit">
        <div v-if="!editing">
            <v-text-field
                v-if="typeof value === 'string'"
                readonly
                clearable
                v-model="value.split('/')[1]"
                :label="label"
                @click:clear="startEdit"
            />
        </div>
        <div :class="{'pt-7' : editing}">
            <v-file-input
                v-model="model"
                v-show="editing"
                class="file-input-component"
                dense
                outlined
                append-icon="fa-file-upload"
                :label="label"
                :name="name"
                prepend-icon
                :maxlength="maxlength"
                :error-messages="errorTamanhoExcedido ===1 ? 'Tamanho permitido: 15MB' : errors.collect(name)"
                v-validate="validate"
                :counter="counter"
                :placeholder="placeholder"
                :clearable="false"
                @change="submitEdit"/>
            <span class="tamanhoPermitidoClassInput"
                  v-if="!this.model && errorTamanhoExcedido!==1 && errorObrigatoriedadeAnexo!==1">Tamanho permitido: 15MB</span>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'campo-de-arquivo-novo',
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
            }
        },
        $_veeValidate: {
            validator: 'new'
        },
        data: () => ({
            model: undefined,
            editing: true,
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
                this.editing = true
            },
            async submitEdit() {
                const anexo = this.model
                if (anexo) {
                    if (this.extensaoArquivoValida(anexo) === -1) {
                        this.mostrarNotificacaoErro('Extensão do arquivo inválida. Envie arquivos nos seguintes formatos .pdf .jpg .png')
                        this.model = null
                    } else {
                        if (this.validarTamanhoMaximo(anexo.size)) {
                            this.errorTamanhoExcedido = 1
                            this.model = await null
                            setTimeout(() => {
                                this.errorTamanhoExcedido = 0
                                this.errorObrigatoriedadeAnexo = 1
                                this.$validator._base.validateAll()
                            }, 3000)

                        } else {
                            this.editing = false
                            this.$emit('input', anexo)
                        }
                    }
                }
            },
            validarTamanhoMaximo(tamanhoAnexo) {
                return tamanhoAnexo >= 15116247

            },
            extensaoArquivoValida(nomeArquivo) {
                const extensaoAceitas = ['.pdf', '.PDF', '.jpg', '.JPG', '.png', '.PNG', '.jpeg', '.JPEG']
                const extensao = nomeArquivo.name.substr(nomeArquivo.name.lastIndexOf('.'), nomeArquivo.name.length)
                return extensaoAceitas.indexOf(extensao)
            },
        }
    }
</script>

<style lang="stylus">
    .ajusteInput
        width 180px
        margin-top 14px
        margin-left -6px

    .file-input-component
        font-size 13px

        .v-text-field__details
            margin-bottom 0px

    .tamanhoPermitidoClassInput
        color #a5a5a5
        margin 0px
        font-size 12px
</style>
